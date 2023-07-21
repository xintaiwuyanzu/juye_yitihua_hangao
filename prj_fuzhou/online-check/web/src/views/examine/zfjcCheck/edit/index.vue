<template>
  <section>
    <nac-info :title="id=== undefined?'添加':'修改'" back>
      <el-button type="primary" @click="saveLibForm" v-loading="loading">保 存</el-button>
    </nac-info>
    <div class="index_main">
      <el-form :model="form" :rules="rules" ref="form" label-width="150px" :inline="false">
        <el-row>
          <el-col :span="18">
            <el-form-item label="人员" prop="personId" required>
              <el-select multiple
                         style="width: 88%"
                         v-model="form.personId"
                         filterable clearable
                         default-first-option
                         placeholder="请选择人员">
                <el-option
                    v-for="(item,key) in persons"
                    :key="key"
                    :label="item.userName"
                    :value="item.id"/>
              </el-select>
              <el-button @click="suijiPerson()" type="primary">随机
              </el-button>
            </el-form-item>
          </el-col>
          <el-col :span="18">
            <el-form-item label="检查单位" prop="organiseId" required>
              <el-select multiple
                         style="width: 88%"
                         v-model="form.organiseId"
                         filterable clearable
                         default-first-option
                         placeholder="请选择检查单位">
                <el-option
                    v-for="item in organiseIds"
                    :key="item.organiseId"
                    :label="item.organiseName"
                    :value="item.organiseId"/>
              </el-select>
              <!--              <el-select v-else disabled
                                       style="width: 88%"
                                       v-model="form.organiseName"
                                       filterable clearable
                                       default-first-option
                                       :placeholder="form.organiseName">
                            </el-select>-->

              <el-button @click="suijiDanwei" type="primary">随机
              </el-button>

            </el-form-item>
          </el-col>
          <el-col :span="3">
            <el-input-number v-model="num" controls-position="right" :min="1"
                             :max="10"></el-input-number>
          </el-col>
          <el-col :span="3">
            <span style="line-height: 20px;">家单位</span>
          </el-col>
          <el-col :span="20">

            <el-form-item label="检查内容" prop="categoryIdContact"  >
              <el-cascader
                  style="width: 90%"
                  v-model="form.categoryIdContact"
                  :key="modalKey"
                  :options="options"
                  :props="props"
                  :show-all-levels="false"
                  clearable/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="计划检查时间" prop="jihuaTime">
              <el-date-picker
                  v-model="form.jihuaTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">

            <el-form-item label="实际检查时间" prop="shijiTime">
              <el-date-picker
                  v-model="form.shijiTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果填写时间" prop="jieguoTime">
              <el-date-picker
                  v-model="form.jieguoTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="通知整改时间" prop="tongzhiTime">
              <el-date-picker
                  v-model="form.tongzhiTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改到期时间" prop="zhenggaiTime">
              <el-date-picker
                  v-model="form.zhenggaiTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="整改完成时间" prop="wanchengTime">
              <el-date-picker
                  v-model="form.wanchengTime"
                  type="date"
                  value-format="timestamp"
                  default-time="12:00:00"
                  placeholder="选择日期时间"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="批次">
              <el-input
                  placeholder="请输入批次"
                  v-model="form.pici"
                  clearable/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </section>
</template>

<script>
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  name: "index",
  mixins: [formMixin],
  data() {
    return {
      props: {multiple: true, value: 'id',},
      options: [],
      num: 1,  //随机单位的个数
      form: {
        personId: [],
        organiseId: [],
        organiseName: '',
        categoryIdContact: []
      },
      searchForm: {},
      /**
       * 通知
       *
       */
      tongzhiShow: false,
      edit: false,
      personIdTs: [],

      //临时存储数组
      temporaryStorageArray: '',
      organiseIds: [],
      orgIds: [],//单位id数据 供随机使用
      persons: [],
      personIds: [],//人员id数据 供随机使用
      /**
       * 通知时间区间
       */
      timeinterval: [],
      dict: ['zfjc.status', 'zfjc.type'],
      //checkType: 'zf',
      modalKey: '0',
      rules: {
        categoryIdContact: [
          {required: true, message: '请输入活动名称', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    //初始化
    $init() {
      this.loadPersons()
      this.loadOrganiseIds()
      this.loadcategory()
      if (this.id !== undefined) {
        this.loadDetail(this.id)
      }

    },
    // 随机单位
    async suijiDanwei() {
      const {data} = await this.$http.post('/jdzdWeight/random', {num: this.num})
      if (data && data.success) {
        this.$set(this.form, 'organiseId', data.data)
      }
    },
    // 随机人员
     suijiPerson() {
      if (this.form.personId !== undefined && (this.form.personId.length === this.personIds.length)) {
        this.$message.warning('已添加所有人员！')
      } else {
        //随机
        let a = this.personIds[Math.floor(Math.random() * this.personIds.length)]
        if (this.form.personId !== undefined && this.form.personId != []) {
          let boo = this.form.personId.indexOf(a);
          if (boo === -1) {
            this.personIdTs = []
            this.form.personId.forEach(i=>{
              this.personIdTs.push(i)
            })
            this.personIdTs.push(a)
            this.$set(this.form,'personId',[])
            this.$set(this.form,'personId',this.personIdTs)
          } else {
            this.suijiPerson()
          }
        } else {
          this.$set(this.form, 'personId', [])
          this.form.personId.push(a)
        }
      }
    },
    // 新增
    async saveLibForm() {
      console.log('执行')

      if (this.form.personId === undefined || this.form.organiseId === undefined || this.form.categoryIdContact === undefined) {
          this.$message.warning('请填写完整表单')
      } else {
        if (this.form.organiseId.length === 0 || this.form.personId.length === 0 || this.form.categoryIdContact.length === 0) {
          this.$message.warning('请填写完整表单')
        } else {
          if (this.validate()) {
            this.form.checkType = this.checkType

            let a = this.form.personId
            this.form.personId = a.join(',')

            let c = this.form.categoryIdContact
            let s = JSON.stringify(c);

            let c1 = ''
            let c2 = ''
            for (let i = 0; i < c.length; i++) {
              c1 += c[i][0] + ','
              c2 += c[i][1] + ','
            }
            this.timeDate(this.form)
            this.form.categoryId = c1
            this.form.categorySmallId = c2
            this.form.categoryIdContact = s
            this.loading = true
            let path = this.apiPath()
            if (this.form.id) {
              path = path + '/change'
            } else {
              path = path + '/insert'
            }
            let b = this.form.organiseId
            this.form.organiseId = b.join(',')
            const {data} = await this.$http.post(path, this.form)
            if (data && data.success) {
              this.$message.success('保存成功！')
              this.$router.back()
            } else {
              this.$message.error(data.message)
              this.reset()
            }
            this.loading = false

          } else {
            this.loading = false
            this.$message.warning('请填写完整表单')
            this.reset()
          }
        }
      }
    },
    reset() {
      this.form = {
        personId: [],
        organiseId: [],
        organiseName: '',
        categoryIdContact: []
      }
    },
    validate() {
      return this.form.personId && this.form.organiseId && this.form.categoryIdContact
    },
    isArray(obj) {
      return (typeof obj == 'object') && obj.constructor == Array;
    },
    //获取详情
    async loadDetail(id) {
      const {data} = await this.$http.post(this.apiPath() + '/detail', {id: id})
      if (data && data.success) {
        this.editFormBefore(data.data)
      }
    },
    // 编辑
    editFormBefore(row) {
      if(row.jieguoTime === 0){
        row.jieguoTime = null
      }else {
        row.jieguoTime = '' + row.jieguoTime
      }

      if (row.jihuaTime === 0) {
        row.jihuaTime = null
      }else {
        row.jihuaTime = '' + row.jihuaTime
      }

      if (row.shijiTime === 0) {
        row.shijiTime = null
      }else {
        row.shijiTime = '' + row.shijiTime
      }

      if (row.tongzhiTime === 0) {
        row.tongzhiTime = null
      }else {
        row.tongzhiTime = '' + row.tongzhiTime
      }

      if (row.wanchengTime === 0) {
        row.wanchengTime = null
      }else {
        row.wanchengTime = '' + row.wanchengTime
      }

      if (row.zhenggaiTime === 0) {
        row.zhenggaiTime = null
      }else {
        row.zhenggaiTime = '' + row.zhenggaiTime
      }
      this.form = row
      let personIdStrings = row.personId
      if (!this.isArray(personIdStrings)) {
        if (personIdStrings.length >= 32) {
          let arr1 = personIdStrings.split(",");
          for (var i = 0; i < arr1.length; i++) {
            if (arr1[i] == '' || arr1[i] == null) {
              arr1.splice(i, 1);
              i = i - 1;
            }
          }
          row.personId = arr1
        }
      }
      let organiseIdStrings = row.organiseId
      if (!this.isArray(organiseIdStrings)) {
        if (organiseIdStrings.length >= 32) {
          let arr1 = organiseIdStrings.split(",");
          for (let j = 0; i < arr1.length; j++) {
            if (arr1[j] == '' || arr1[j] == null) {
              arr1.splice(j, 1);
              j = j - 1;
            }
          }
          row.organiseId = arr1
        }
      }
      if (!this.isArray(row.categoryIdContact)) {
        let ontactontact = JSON.parse(row.categoryIdContact)
        row.categoryIdContact = ontactontact
      }
    },
    //取消弹窗
    cancel() {
      this.edit = false
      this.tongzhiShow = false
    },
    //通知单
    addtz() {
      let a = ''
      for (let i = 0; i < this.multipleSelection.length; i++) {
        a += this.multipleSelection[i].id + ','
      }
      let url = "api/zfjcCheck/exptz?ids=" + a
      window.open(url)
      this.cancel()
    },
    tongbao() {
      //todo 先校验 批次是否输入了 不然不是一个批次就出错了
      let a = ''
      for (let i = 0; i < this.multipleSelection.length; i++) {
        a += this.multipleSelection[i].id + ','
      }
      let url = "api/zfjcCheck/exptb?ids=" + a
      window.open(url)
      this.cancel()
    },
    apiPath() {
      return '/zfjcCheck'
    },
    //类型树
    loadcategory() {
      this.$http.post('/jdzdCategory/cateTree').then(({data}) => {
        if (data && data.success) {
          this.options = data.data
        }
      })
    },
    //人员列表
    loadPersons() {
      this.personIds = []
      this.$http.post('zfjcSpecialist/page', {page: false}).then(({data}) => {
        if (data && data.success) {
          this.persons = data.data
          if (this.persons.length > 0) {
            for (let i = 0; i < data.data.length; i++) {
              this.personIds.push(data.data[i].id)
            }
          }
        }
      })
    },
    timeDate(row){
      if(row.jieguoTime === null){
        row.jieguoTime = 0
      }

      if (row.jihuaTime === null) {
        row.jihuaTime = 0
      }

      if (row.shijiTime === null) {
        row.shijiTime = 0
      }

      if (row.tongzhiTime === null) {
        row.tongzhiTime = 0
      }

      if (row.wanchengTime === null) {
        row.wanchengTime = 0
      }

      if (row.zhenggaiTime === null) {
        row.zhenggaiTime = 0
      }

      this.form = row
    },
    //机构列表
    loadOrganiseIds() {
      this.orgIds = []
      this.$http.post('/jdzdWeight/page', {page: false, status: '1'}).then(({data}) => {
        if (data && data.success) {
          this.organiseIds = data.data
        }
      })
    },
  },
  computed: {
    id() {
      return this.$route.query.id
    },
    checkType(){
      return this.$route.query.checkType
    }
  }
}
</script>