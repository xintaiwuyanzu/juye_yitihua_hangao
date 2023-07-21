<template>
  <table-index :fields="fields"
               path="appraisalBatch/getAppraisalBatchByPerson"
               pagePath="appraisalBatch/getAppraisalBatchByPerson"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               ref="table"
               style="height: 70vh;display: flex;flex-direction: column"
  >
    <template v-slot:table-$btns="scope">
      <el-button type="text" width="20"  @click="detail(scope)" >进入</el-button>
    </template>
  </table-index>
</template>

<script>
export default {
  data(){
    return{
      fields: {
        batchName: {label: '名称', search: true,align: 'center',width:300,},
        appraisalType: {label: '类型', search: true,fieldType:'select',
          width:100,
          mapper:[{
            id: 'KFJD',
            label: '开放鉴定'
          }, {
            id: 'DQJD',
            label: '销毁鉴定'
          }],
          align: 'center'},
        fondNames: {label: '包含全宗', search: false, edit:false, align: 'center'},
        archiveQuantity: {label: '档案数量', search: false, edit:false, align: 'center',width: 100},
        startVintages: {label: '起始年度', search: false, edit:false, align: 'center',width: 100},
        endVintages: {label: '截止年度', search: false, edit:false, align: 'center',width: 100},
        createDate: {label: '创建时间', search: false,dateFormat:'YYYY-MM-DD hh:mm:ss', edit:false,width: 200},
        status: {label: '任务状态', search: false,edit:false,fieldType:'select',width: 100,component: 'tag',
          mapper:[{
            id: '2',
            label: '已完成'
          }, {
            id: '1',
            label: '进行中'
          }, {
            id: '0',
            label: '未开始'
          }]}
      },
      defaultSearchForm:{status:1}
    }
  },
  methods:{
    detail(scope){
      sessionStorage.setItem('batchId',scope.row.id);
      this.$router.push({path:"/appraisal/appraisalTask/task",query:{batchId:scope.row.id,createPerson:true}})
    }
  }
}
</script>
