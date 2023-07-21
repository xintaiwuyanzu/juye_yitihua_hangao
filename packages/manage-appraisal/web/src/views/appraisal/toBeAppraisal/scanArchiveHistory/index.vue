<template>
  <table-index :fields="fields"
               path="scanArchiveHistory"
               pagePath="scanArchiveHistory/queryScanArchiveHistory"
               :edit="false"
               :delete="false"
               :insert="false"
               :back="true"
               ref="table"
               style="height:70vh;display: flex;flex-direction: column">
  </table-index>
</template>

<script>

export default {
  data(){
    return{
      fields: {
        startTime: {label: '开始时间', search: false, edit:false, dateFormat: "YYYY-MM-DD HH:mm:ss", align: 'center'},
        endTime: {label: '结束时间', search: false, edit:false, dateFormat: "YYYY-MM-DD HH:mm:ss", align: 'center'},
        status: {label: '刷新状态', component: 'tag', search: true,fieldType:'select',
          width:120,
          mapper:[
            {id:'0',label:'正在进行'},
            {id:'1',label:'已完成'}
          ],
          align: 'center'}
      }
    }
  }
}
</script>
