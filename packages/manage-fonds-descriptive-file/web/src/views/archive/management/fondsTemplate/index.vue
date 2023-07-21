<template>
  <table-index title="全宗卷模板" :fields="fields" path="compilationTemplate" :edit="true"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :delete="hasRole(['DASJ_ADMIN','admin'])"
               ref="table"/>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {
          prop: 'templateName',
          label: '模板名称',
          route: true,
          component: 'text',
          align: 'left',
          routerPath: '/compilation/compilationTemplate/edit',
          queryProp: ['id'],
          required: true,
          search: true
        },
        {
          prop: 'templateTypeCode',
          label: '类型',
          required: false, autosize: true, search: false, align: 'center',
          fieldType: 'select',
          labelKey: 'name',
          valueKey: 'code',
          filterable: true,
          url: 'dossierType/page',
          params: {page: false}
        },
        // {prop: 'compilationContent', label: '内容'},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false}
      ],
      defaultInsertForm: {businessType: 'fonds'},
      defaultSearchForm: {businessType: 'fonds'}
    }
  }
}
</script>