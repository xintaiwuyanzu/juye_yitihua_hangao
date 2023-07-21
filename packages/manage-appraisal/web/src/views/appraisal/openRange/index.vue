<template>
  <table-index :fields="fields" path="appraisalOpenRange"
                :insert="true" :edit="true" :delete="true">
  </table-index>
</template>

<script>
export default {
  data() {
    return {
      fields: {
        code: {label: '编码', required:true, search: true,align: 'center',width: 200},
        auxiliaryResult: {label: '结果', required:true, search: true,align: 'center',width: 200,
          fieldType:'select',
          mapper:[{
            id: 'kz',
            label: '控制'
          }, {
            id: 'kf',
            label: '开放'
          }],},
        openRange: {label: '开放范围', required:true, search: true,align: 'center'},
        createDate: {label: '创建时间', edit:false,search:false, dateFormat: "YYYY-MM-DD HH:mm:ss",width: 150},
        priority: {label: '优先级', required:true, search: true,align: 'center',width: 100},
      }
    }
  }
}
</script>
