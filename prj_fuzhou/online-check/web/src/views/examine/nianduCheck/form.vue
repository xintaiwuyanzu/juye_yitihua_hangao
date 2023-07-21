<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="检查人员:" prop="personName">
        <el-input v-model="searchForm.personName" clearable placeholder="请输入检查人员"/>
      </el-form-item>
      <el-form-item label="检查批次:" prop="pici">
        <el-input v-model="searchForm.pici" clearable placeholder="请输入检查批次"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchF" size="mini">搜 索</el-button>
        <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
        <el-button type="primary" @click="add()" size="mini">添加年度检查</el-button>
        <el-button type="primary" @click="addtz()" size="mini">下载检查通知单</el-button>
        <el-button type="primary" @click="tongbao()" size="mini">下载检查通报</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'发起年度检查')" width="60%"

               :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="150px" :inline="false">

        <el-card class="box-card">
          <div>
            <el-row>
              <el-col :span="18">
                <el-form-item label="人员" prop="personId">
                  <el-select multiple
                             style="width: 88%"
                             v-model="form.personId"
                             filterable clearable
                             default-first-option
                             placeholder="请选择人员">
                    <el-option
                        v-for="item in persons"
                        :key="item.id"
                        :label="item.userName"
                        :value="item.id"/>
                  </el-select>
                  <el-button @click="suijiPerson" type="primary">随机</el-button>
                </el-form-item>
              </el-col>
              <el-col :span="18">
                <el-form-item label="检查单位" prop="organiseId">
                  <el-select multiple v-if="!form.id"
                             style="width: 88%"
                             v-model="form.organiseId"
                             filterable clearable
                             default-first-option
                             placeholder="请选择检查单位">
                    <el-option
                        v-for="item in organiseIds"
                        :key="item.id"
                        :label="item.organiseName"
                        :value="item.id"/>
                  </el-select>
                  <el-select v-else disabled
                             style="width: 88%"
                             v-model="form.organiseName"
                             filterable clearable
                             default-first-option
                             :placeholder="form.organiseName">
                  </el-select>

                  <el-button @click="suijiDanwei" type="primary" :disabled="form.id">随机
                  </el-button>

                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-input-number v-model="num" controls-position="right" @change="handleChange" :min="1"
                                 :max="10"></el-input-number>
              </el-col>
              <el-col :span="3">
                <el-span style="line-height: 20px;"> 家单位</el-span>
              </el-col>
              <el-col :span="20">

                <el-form-item label="检查内容" prop="categoryId">

                  <!--                  <select-dict type="zfjc.type" style="width: 200px" v-model="form.categoryId" placeholder="请选择检查内容"-->
                  <!--                               @change="suiji(form.categoryId)"/>-->
                  <el-cascader
                      style="width: 90%"
                      v-model="form.categoryId"
                      :options="options"
                      :props="props"
                      :show-all-levels="false"
                      clearable>

                  </el-cascader>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="计划检查时间" prop="jihuaTime">
                  <el-date-picker
                      v-model="form.jihuaTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">

                <el-form-item label="实际检查时间" prop="shijiTime">
                  <el-date-picker
                      v-model="form.shijiTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结果填写时间" prop="jieguoTime">
                  <el-date-picker
                      v-model="form.jieguoTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="通知整改时间" prop="tongzhiTime">
                  <el-date-picker
                      v-model="form.tongzhiTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="整改到期时间" prop="zhenggaiTime">
                  <el-date-picker
                      v-model="form.zhenggaiTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="整改完成时间" prop="wanchengTime">
                  <el-date-picker
                      v-model="form.wanchengTime"
                      value-format="timestamp"
                      type="datetime"
                      placeholder="选择日期时间"
                      default-time="12:00:00">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" required v-show="form.id">
                  <select-dict v-model="form.status" type="zfjc.status"
                               placeholder="请选择状态"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="批次" v-show="!form.id">
                  <el-input
                      placeholder="请输入批次"
                      v-model="form.pici"
                      clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="saveLibForm" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
    <el-dialog
        title="检查单详情"
        :visible.sync="tongzhiShow"
        width="30%"
        center>
      <div class="block">
        <span class="demonstration">默认</span>
        <el-date-picker
            v-model="timeinterval"
            value-format="yyyy年MM月"
            format="yyyy-MM"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份">
        </el-date-picker>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="addtz" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'
import moment from 'dayjs'

export default {

  props: {

    multipleSelection: Array //这样可以指定传入的类型，如果类型不对，会警告
  },
  data() {
    return {
      props: {multiple: true, value: 'id',},
      options: [],
      num: 1,  //随机单位的个数
      form: {
        wanchengTime: '',
        jihuaTime: new Date().getTime(),
        zhenggaiTime: moment().format('yyyy-MM-dd'),
      },
      searchForm: {},
      /**
       * 通知
       *
       */
      tongzhiShow: false,
      edit: false,
      organiseIds: [],
      orgIds: [],//单位id数据 供随机使用
      persons: [],
      personIds: [],//人员id数据 供随机使用
      /**
       * 通知时间区间
       */
      timeinterval: [],
      dict: ['zfjc.status', 'zfjc.type'],
      checkType: 'nd'
    }
  },
  mixins: [fromMixin],
  mounted() {
    //不好使
    this.form.wanchengTime = moment().format('YYYY-MM-DD')
  },
  methods: {
    // 随机单位
    suijiDanwei() {
      //先看看随即几次
      // for (let i = 0; i < this.num; i++) {
      //   //随机
      //   let a = this.orgIds[Math.floor(Math.random() * this.orgIds.length)]
      //   this.form.organiseId.push(a)
      // }
      this.$http.post('/jdzdWeight/random', {num: this.num}).then(({data}) => {
        if (data && data.success) {
          this.form.organiseId = data.data
        }
      })

    },
    // 随机人员
    suijiPerson() {
      //随机
      let a = this.personIds[Math.floor(Math.random() * this.personIds.length)]
      this.form.personId.push(a)
    },
    handleChange(value) {
    },
    // 新增
    add() {
      this.form.personId = []
      this.form = {}
      this.edit = true
    }
    ,
    // 新增
    saveLibForm() {
      this.form.checkType = this.checkType

      let a = this.form.personId
      this.form.personId = a.join(',')
      let b = this.form.organiseId
      this.form.organiseId = b.join(',')
      let c = this.form.categoryId
      // let d= this.form.categorySmallId

      let c1 = ''
      let c2 = ''
      for (let i = 0; i < c.length; i++) {
        c1 += c[i][0] + ','
        c2 += c[i][1] + ','
      }
      this.form.categoryId = c1
      this.form.categorySmallId = c2
      this.loading = true
      this.$refs.form.validate(valid => {
        if (valid) {
          let path = this.apiPath()
          if (this.form.id) {
            path = path + '/change'
          } else {
            path = path + '/add'
          }

          this.$http.post(path, this.form).then(({data}) => {
            if (data && data.success) {
              this.$message.success('保存成功！')
              this.cancel()
              this.form = {}
              this.form.personId = []
              this.form.organiseId = []
              this.$emit('func')
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
        } else {
          this.loading = false
        }
      })

    }
    ,

    // 编辑
    editFormBefore(row) {

      if (row.id) {
        //personName
        let strings1 = row.personId
        if (strings1.length > 32) {
          let arr1 = strings1.split(",");
          for (var i = 0; i < arr1.length; i++) {
            if (arr1[i] == '' || arr1[i] == null) {
              arr1.splice(i, 1);
              i = i - 1;
            }
          }
          row.personId = arr1
        }

        //organiseId
        /*        let strings2 = ''
                strings2 = row.organiseId
                let arr2 = strings2.split(",");
                for (var j = 0; j < arr2.length; j++) {
                  if (arr2[j] == '' || arr2[j] == null || typeof (arr2[j]) == undefined) {
                    arr2.splice(j, 1);
                    j = j - 1;
                  }
                }
                row.organiseId = arr2*/

      }
      this.editForm(row)
    }
    ,
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


    //初始化
    $init() {
      this.loadPersons()
      this.loadOrganiseIds()
      this.loadcategory()
    },
    searchF() {
      this.$emit('func')
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
      this.$http.post('zfjcSpecialist/specialistList', {page: false}).then(({data}) => {
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
    //机构列表
    loadOrganiseIds() {
      this.orgIds = []
      this.$http.post('/jdzdWeight/page', {page: false, status: '1'}).then(({data}) => {
        if (data && data.success) {
          this.organiseIds = data.data
        }
      })

    },

  }
  ,
}
</script>
