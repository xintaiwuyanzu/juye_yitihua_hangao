<template>
  <table-index title="我的档案车" :fields="fields" path="archiveCarBatch"
               :insert="false"
               :edit="false"
               ref="table"
               :delete="false">
  </table-index>
</template>
<script>

export default {
  name: "archiveCarIndex",
  data() {
    return {
      fields: [
        {
          prop: 'batchName',
          label: '档案车名称',
          align: 'left',
          search: true,
          component: 'text',
          route: true,
          routerPath: '/archive/archiveCar/detail',
          queryProp: ['id', 'batchType','status']
        },
        {
          prop: 'batchType',
          label: '档案车类型',
          url: '/archiveCarBatch/archiveCarType',
          params: {withTag: false},
          component: 'tag',
          labelKey: 'name',
          search: true,
          fieldType: 'select',
          width: 100,
          valueKey: 'type'
        },
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'detailNum', label: '档案数量', width: 80},
        {prop: 'beizhu', label: '备注', width: 80},
      ]
    }
  }
}
</script>