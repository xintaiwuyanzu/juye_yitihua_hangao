<template>
  <table-index :fields="fields"
               path="archiveAppraisalTask"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               ref="table"
               style="height:70vh;display: flex;flex-direction: column">
    <template v-slot:search-$btns="scope">
      <el-button  @click="confirmDes()"  type="primary" width="40">确定销毁</el-button>
      <el-button  @click="history()"  type="primary" width="40">销毁记录</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button  @click="goDetail(scope)"  type="text" width="20">查看</el-button>
    </template>
  </table-index>
</template>

<script>

const result = {'kz':'控制','kf':'开放'}
export default {
  data(){
    return{
      fields: {
        fondCode: {label: '全宗编码', search: false, edit:false,  align: 'center',width:100},
        categoryCode: {label: '门类编码', search: false, edit:false, align: 'center',width:150},
        archiveCode: {label: '档号', search: false, edit:false, align: 'center',width:200},
        title: {label: '题名', search: true, edit:false, align: 'center'},
        vintages: {label: '年份', search: true, edit:false, align: 'center',width:100},
        filetime: {label: '文件时间', search: false, edit:false, align: 'center',width:150},
        batchId: {label: '鉴定批次', search: true, edit:false, align: 'center',width:150,
          fieldType: 'select',
          url:'appraisalBatch/page',
          params:{page:false,appraisalType:"DQJD"},
          labelKey:'batchName'}
      },
      defaultSearchForm:{organiseId:'true',status:'2',batchId:this.$route.query.batchId,personResult:'xh'},
      defaultInsertForm:{}
    }
  },
  methods:{
    $init(){
      this.defaultSearchForm.batchId = this.$route.query.batchId
      this.$http.post("/appraisalOpenRange/page",{page:false})
          .then(({data})=>{
            data.data.forEach(v=>{
              this.fields.auxiliaryResult.mapper.push({id:v.id,label:result[v.auxiliaryResult]})
            })
          })
      setTimeout(() => {
        this.$refs.table.loadData(this.defaultSearchForm);
      }, 10)

    },
    goDetail(scope){
      sessionStorage.removeItem("batchId")
      sessionStorage.removeItem("taskId")
      this.$router.push({path: '/appraisal/toBeAppraisal/newDetail', query: {
          archiveCode:scope.row.archiveCode,
          formDefinitionId:scope.row.formDefinitionId,
          formDataId:scope.row.formDataId,
          index:scope.$index+(parseInt(this.$refs.table.data.page.index)-1)*parseInt(this.$refs.table.data.page.size)
        }})
    },
    async confirmDes(){
      if(!this.$refs.table.searchFormModel.batchId){
        this.$message.error("请先选择需要销毁的鉴定批次")
        return
      }
      const d = await this.$http.post('/appraisalBatch/isDel',{batchId:this.$refs.table.searchFormModel.batchId})
      if('0' == d.data){
        this.$message.error("选中批次已经进行销毁，请前往销毁历史中查看")
        return
      }
      this.$confirm('此操作将销毁选中鉴定批次中销毁的档案数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let url = "api/appraisalBatch/expDestoryArchive?batchId="+this.$refs.table.searchFormModel.batchId
        window.open(url)
      })
    },
    history(){
      this.$router.push({path: '/appraisal/destory/history'})
    }
  }
}
</script>
