<template>
  <table-index title="人员权限管理" :fields="fields" path="member_authority" :insert="false" :edit="false" :delete="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm">
    <template v-slot:search-$btns="scope">
      <el-button type="primary" width="20" @click="addAccess">添加</el-button>
      <el-dialog :title="operation?'编辑权限':'添加权限'" :visible.sync="levelCodeEdit" style="width:120%">
        <el-form :model="form" :rules="rules" ref="form">
          <!--全宗-->
          <el-form-item style="padding: 10px;margin:10px">
            <fond-transfer ref="fondTransfer"
                           :transferData="transferData" :rightValue="rightValue" @resetBtn="resetBtn"
                           @disableBtn="disableBtn"
                           @undisableLeftBtn="undisableLeftBtn" @getFondId="getFondId"
            ></fond-transfer>
          </el-form-item>
          <!--门类-->
          <el-form-item>
            <category-table :typeOptions="typeOptions" ref="categoryTable"
                            @categoryName="categoryName"></category-table>
          </el-form-item>
          <!--人员-->
          <el-form-item>
            <person-table ref="personTable" :personList="personList" @personNameAndId="personNameAndId"></person-table>
          </el-form-item>
          <el-form-item label="权限级别" prop="accessLevel">
            <el-select v-model="form.accessLevel" placeholder="选择权限级别">
              <el-option
                  v-for="(value,key,index) in options" :key="index"
                  :label="key"
                  :value="key">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="权限类别" prop="levelCode">
            <el-select v-model="form.levelCode" placeholder="选择权限类型">
              <el-option
                  v-for="(i,index) in types" :key="index"
                  :label="i.levelCode"
                  :value="i.levelCode">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </div>
      </el-dialog>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text" width="20" @click="editAccess(scope.row)" style="float: left">编辑</el-button>
      <el-button type="text" width="20" @click="deleteAccess(scope.row)">删除</el-button>
    </template>

  </table-index>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import FondTransfer from './fond/index'
import categoryTable from './category/index'
import personTable from "./person/index"

export default {
  name: "index",
  mixins: [indexMixin],
  components: {FondTransfer, categoryTable, personTable},
  data() {
    return {
      types: [],//权限类别下的权限级别数组
      options: [],//权限数组
      transferData: [],//穿梭框左边数据
      rightValue: [],//穿梭框右边数据

      typeOptions: [{description: "录音"}, {description: "婚姻档案"},
        {description: "业务数据"}, {description: "社交媒体档案"}, {description: "录像"},
      ],//门类
      personList: [],//人员列表
      levelCodeEdit: false,//是否开启弹窗
      operation: false,//false添加 true编辑
      form: {fondPeron: '', fondPeronId: '', fondId: '', categoryName: '', accessLevel: '', levelCode: ''},
      fields: [
        {
          prop: 'fondPeron', label: '机构人员', width: "150", search: false, required: true,
        },
        {
          prop: 'fondName', label: '全宗', width: "150", search: false, required: true,
          fieldType: 'select',
          column: false,
          mapper: {}
        },
        {
          prop: 'categoryName', label: '门类', width: "150", search: false, required: true,
          fieldType: 'select',
          column: false,
          mapper: {}
        },
        {
          prop: 'createDate',
          label: '创建时间',
          dateFormat: "YYYY-MM-DD HH:mm:ss",
          search: false,
          insert: false,
          edit: false
        },
        {
          prop: 'updateDate',
          label: '更新时间',
          dateFormat: "YYYY-MM-DD HH:mm:ss",
          search: false,
          insert: false,
          edit: false
        },
        {prop: 'updatePerson', label: '更新人', width: "150", search: false, insert: false, edit: false},
        {
          prop: 'accessLevel', label: '权限级别', width: "150", search: false, required: true,
          fieldType: 'select',
          mapper: []
        },
        {
          prop: 'levelCode', label: '级别类型', width: "80", edit: this.levelCodeEdit, required: this.levelCodeEdit,
          fieldType: 'select',
          mapper: {
            '0': {label: '个人', show: ''},
            '     1': {label: '单位', show: ''},
            '2': {label: '全部', show: ''},
          }
        },

      ],
      fonds: [],
      defaultSearchForm: {categoryName: '', accessLevel: ''},
      rules: {
        accessLevel: [
          {required: true, message: '请选择权限', trigger: 'blur'},
        ],
        levelCode: [
          {required: true, message: '请选择级别', trigger: 'blur'},
        ],
      }
    }
  },
  methods: {
    $init() {
      this.getFonds()
      this.getAuthority()
      this.persons()
    },
    //获取人员
    async persons(oId) {
      this.form.targetPerson = ''
      this.$http.post('/person/page', {
        page: false,
        defaultOrganiseId: oId
      }).then(({data}) => {
        if (data && data.success) {
          this.personList = data.data
        }
      })
    },
    //获取全宗
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      this.fonds = data.data
      this.fields[0].mapper = this.fonds
      if (data && data.success) {
        data.data.forEach((i) => {
          this.transferData.push({
            label: i.label,
            key: i.id,
            disabled: false
          })
        })
      }


    },
    //获取门类
    async getCategory(fondId) {
      if (fondId) {
        const result = await this.$post('/member_authority/getCategoryByFondId', {fondId: fondId})
        console.log(result)
        this.typeOptions = result.data.data

      }
    },
    //获取权限
    async getAuthority() {
      const {data} = await this.$post('data_authority/getAllDataAuthority')
      this.options = data.data
    },
    //添加
    addAccess() {
      this.operation = false
      this.levelCodeEdit = true
    },
    //编辑
    editAccess(row) {
      this.operation = true
      //全宗
      if (row.fondId) {
        let fId = []
        fId = row.fondId.split(",")
        //获取全宗的id对象
        fId.forEach(i => {
          this.transferData.forEach(j => {
            //与穿梭框的值做对比
            if (i === j.key) {
              this.rightValue.push(i)
            }
          })
        })
      }
      //门类
      if (row.categoryName) {
        let fId = []
        fId = row.categoryName.split(",")
        this.$nextTick(() => fId.forEach(i => {
          this.$refs.categoryTable.toggleRowSelectioncategoryName(i)
        }))
      }
      //人员
      if (row.fondPeronId) {
        let fId = []
        fId = row.fondPeronId.split(",")
        this.$nextTick(() => fId.forEach(i => {
          this.$refs.personTable.toggleRowSelectionPerson(i)
        }))
      }
      this.form = row
      this.levelCodeEdit = true
    },
    //删除
    deleteAccess(row) {
      this.$confirm("确认删除", "提示", {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post("member_authority/delete", {id: row.id})
            .then(({data}) => {
              if (data && data.success) {
                this.$message.success("删除成功")
                this.$refs.table.loadData();
              }
            })
      })

    },
    //提交
    submit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          console.log(this.form)
          this.$post('/member_authority/' + (this.form.id ? 'update' : 'insertAuthority'), this.form)
              .then(res => {
                if (res) {
                  this.$message.success(this.operation ? '更新成功' : '保存成功')
                  this.cancel()
                } else {
                  this.$message.error(this.operation ? '更新失败' : '保存失败')
                }
              })
        } else {
          return false;
        }
      });

    },
    //取消
    cancel() {
      this.form.accessLevel = ''
      this.form.levelCode = ''
      this.$refs.personTable.toggleSelection()
      this.$refs.categoryTable.toggleSelection()
      this.resetBtn()
      this.$refs.table.loadData();
      this.levelCodeEdit = false
    },
    //全宗的重置
    resetBtn() {
      this.transferData = this.fonds
      this.rightValue = []
    },
    //全宗的不可选
    disableBtn(val) {
      this.transferData = val
    },
    //全宗的不可选
    undisableLeftBtn(val) {
      this.transferData = val
    },
    //获取全宗的id
    getFondId(val) {
      if (val) {
        this.form.fondId = val.toString()
      }
    },
    //门类名称
    categoryName(val) {
      if (val) {
        let arr = []
        val.forEach(i => {
          arr.push(i.description)
        })
        this.form.categoryName = arr.toString();
      }
    },
    //机构人员名称
    personNameAndId(val) {
      if (val) {
        let arr = []
        let arr1 = []
        val.forEach(i => {
          arr.push(i.userName)
          arr1.push(i.id)
        })
        this.form.fondPeron = arr.toString();
        this.form.fondPeronId = arr1.toString();
      }
    }
  },
  watch: {
    'form.accessLevel'(val) {
      this.types = this.options[val]
      if (this.types) {
        this.form.levelCode = this.types[0].levelCode
      }
    },
  }
}
</script>
<style>


</style>