<template>
    <table-index :fields="fields"
                 title="鉴定任务详情"
                 path="appraisalBatchTask"
                 :defaultSearchForm="defaultSearchForm"
                 :edit="false"
                 :delete="false"
                 :insert="false"
                 back
                 ref="table"
                 deleteMulti
                 style="height: 70vh;display: flex;flex-direction: column">
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
        batchTaskCode: {label: '鉴定任务编号', search: true,align: 'center'},
        createDate: {label: '领取时间', search: false,dateFormat:'YYYY-MM-DD hh:mm:ss', edit:false},
        status: {label: '任务状态', search: true,edit:false,fieldType:'select',
          mapper:[{
            id: '1',
            label: '已完成'
          }, {
            id: '0',
            label: '进行中'
          }, {
            id: '-1',
            label: '未开始'
          }]}
      },
      defaultSearchForm:{batchId:this.$route.query.batchId,status: '1'}
    }
  },
  methods:{
    detail(scope){
      sessionStorage.setItem('taskId',scope.row.id)
      this.$router.push({path:"/appraisal/appraisalTask/examination/archive",query:{taskId:scope.row.id,createPerson:this.$route.query.createPerson}})
    },
  }
}
</script>
