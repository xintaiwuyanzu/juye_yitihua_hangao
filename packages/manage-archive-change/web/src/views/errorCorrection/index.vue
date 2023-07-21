<template>
  <table-index title="待纠错库" :fields="fields" path="/errorCorrection" :default-insert-form="defaultInsertForm"
               :delete="true" :edit="false" :insert="false">
    <template v-slot:table-$btns="scope">
      <el-button v-if="scope.row.status == 0" type="text"
                 @click="$router.push({path: '/archiveChange', query: {formDataId: scope.row.formDataId,formDefinitionId:scope.row.formDefinitionId,errorType:scope.row.errorType,status:scope.row.status,errorSource:scope.row.errorSource,fondId:scope.row.fondId,categoryId:scope.row.categoriesId,businessId:scope.row.id,errorDescription:scope.row.errorDescription}})"
                 size="mini" width="40">纠错
      </el-button>
    </template>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {
          prop: 'title',
          label: '题名',
          align: 'left',
          search: true,
          component: 'text',
          route: true,
          routerPath: '/archive/metadataAndFileDetail',
          queryProp: ['formDataId', 'formDefinitionId', {refType: 'archive'}]
        },
        {prop: 'archiveCode', label: '档号', width: '140'},
        {
          prop: 'errorSource',
          label: '来源',
          width: '140',
          edit: false,
        },
        {
          prop: 'status',
          label: '状态',
          width: '140',
          component: 'tag',
          showTypeKey: 'show',
          fieldType: 'select',
          edit: false,
          mapper: {
            '0': {label: '待纠错', show: 'warning'},
            '1': {label: '已纠错', show: 'success'},
            '2': {label: '纠错中', show: 'info'},
            '3': {label: '未通过', show: 'danger'}
          }
        },
        {
          prop: 'errorType',
          label: '类型',
          width: '140',
          component: 'tag',
          showTypeKey: 'show',
          fieldType: 'select',
          edit: false,
          mapper: {'1': {label: '手动发起', show: 'warning'}, '2': {label: '推送', show: 'success'}}
        },
        {prop: 'createPersonName', label: '创建人', width: '100', edit: false},
        {prop: 'createDate', label: '创建时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '140', edit: false},
      ],
      defaultInsertForm: {errorType: '1'}
    }
  }
}
</script>

<style scoped>

</style>