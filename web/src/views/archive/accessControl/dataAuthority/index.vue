<template>
  <table-index title="权限管理" :fields="fields" path="data_authority"
               :insert="false" :edit="false" :delete="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm">
    <template v-slot:search-$btns="scope">
      <el-button type="primary" size="mini" width="90" @click="addAccess" style="float: left">添加</el-button>
      <el-dialog :title="flag?'权限添加':'权限编辑'" :visible.sync="dialogFormVisible">
        <el-form :model="form" >
          <el-form-item label="权限类型" :label-width="formLabelWidth">
            <el-input v-model="form.accessLevel" autocomplete="off" disabled></el-input>
          </el-form-item>
          <el-form-item label="权限级别" :label-width="formLabelWidth">
            <el-input v-model="form.levelCode" autocomplete="off"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
        </div>
      </el-dialog>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button v-show="scope.row.levelCode!='开放'" width="40" type="text" @click="editAccess(scope.row)" style="float: left">编辑</el-button>
      <el-button v-show="scope.row.levelCode!='开放'" width="40" type="text" @click="deleteAccess(scope.row.id)">删除</el-button>
    </template>
  </table-index>

</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
export default {
  name: "index",
  mixins: [indexMixin],
  data() {
    return {
      flag:false,//false为编辑 true为添加
      searchForm:{accessCode:'',accessLevel:'',levelCode:'',},//搜索表单
      options:[],//权限级别
      types:[],//权限类别
      fields: [
        {prop: 'createDate', label: '创建时间',dateFormat: "YYYY-MM-DD HH:mm:ss",search: false,insert: false,edit: false},
        {prop: 'updateDate', label: '更新时间',dateFormat: "YYYY-MM-DD HH:mm:ss",search: false,insert: false,edit: false},
        {prop: 'updatePerson', label: '更新人', width: "150", search: false,insert: false,edit: false},
        {prop: 'accessLevel', label: '权限级别',width: "150", search: true,},
        {prop: 'levelCode', label: '级别类型',width: "80",edit: true,},
      ],
      defaultSearchForm: {accessCode: '', accessLevel: ''},
      dialogFormVisible: false,
      form: {
        accessLevel:'',levelCode:''
      },
      formLabelWidth: '120px'
    }
  },
  methods: {
    $init(){
      this.getAuthority()
    },
    async getAuthority(){
      const {data} = await this.$post('data_authority/getAllDataAuthority')
        this.options = data.data
    },
    //添加
    addAccess(){
      this.flag = true
      this.dialogFormVisible=!this.dialogFormVisible;
      this.form={accessLevel:'控制'}

    },
    //编辑
    editAccess(row){
      console.log(row)
      this.flag = false
      this.dialogFormVisible=!this.dialogFormVisible;
      this.form = row

    },
    //删除
    deleteAccess(id){
      new Promise((resolve, reject) => {
        this.$confirm("确认删除","提示",{
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(res=>resolve(res)).catch(()=>reject)
      }).then(()=>{
            this.$post('/data_authority/delete',{id:id}).then(({data})=>{
              if (data && data.success){
                this.$message.success("删除成功")
                this.$refs.table.loadData()
              }
      })
      })
    },
    //取消
    cancel(){
      this.dialogFormVisible = !this.dialogFormVisible;
      // this.$refs.table.loadData()
    },
    //提交
    submit(){
      this.$post('/data_authority/'+(this.flag?'insert':'update'),this.form).then(({data})=>{
        if (data&&data.success){
          console.log(data)
          this.$message.success(this.flag?"添加成功":'更新成功')
          this.$refs.table.loadData()
        }else {
          this.$message.error(this.flag?data.message:data.message)
          this.$refs.table.loadData()
        }
        this.dialogFormVisible=!this.dialogFormVisible;
      })
    },
    //重置
    reset(){
      this.searchForm = {}
      this.$refs.table.loadData()
    },
    //搜索
    search(){
      // this.$post('/data_authority/page',this.searchForm).then(({data})=>{
      //   console.log(data)
      //   if (data&&data.success){
      //     this.$refs.table.loadData()
      //     this.$refs.table.data=[]
      //     this.$refs.table.data = data.data
      //   }
      // })
    }
  },
  watch:{
    'searchForm.accessLevel'(val){
          this.types = this.options[val]
    }
  }
}
</script>
