<template>
  <table-index :fields="fields"
               title="档案销毁记录"
               path="destoryHistory"
               :edit="false"
               :delete="false"
               :insert="false"
               :back="true"
               ref="table"
               style="height:70vh;display: flex;flex-direction: column">
    <template v-slot:table-$btns="scope">
      <el-button  @click="goDetail(scope)"  type="text" width="60">查看</el-button>
      <el-button  @click="exp(scope)"  type="text" width="60">导出销毁清册</el-button>

    </template>
  </table-index>
</template>

<script>
export default {
  data(){
    return{
      fields: {
        batchId: {label: '鉴定批次', search: true, edit:false, align: 'center',
          fieldType: 'select',
          url:'appraisalBatch/page',
          params:{page:false,appraisalType:"DQJD"},
          labelKey:'batchName'},
        destoryQuantity: {label: '销毁数量', search: false, edit:false,
          route: true,
          routerPath: 'appraisal/destory',
          queryProp: ['batchId'],
          align: 'center',width:200},
        createPersonName: {label: '操作人员', search: false, edit:false, align: 'center',width:250},
        createDate: {label: '销毁时间', edit:false,search:false, dateFormat: "YYYY-MM-DD HH:mm:ss",width: 250},
      }
    }
  },
  methods:{
    goDetail(s){
      this.$router.push({path:"/appraisal/destory",query:{batchId:s.row.batchId}})
    },
    exp(s){
      let url = "api/appraisalBatch/expDestoryArchive?batchId="+s.row.batchId
      window.open(url)
    }
  }
}
</script>
