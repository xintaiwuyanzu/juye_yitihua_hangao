<template>
  <table-index title="编研模板" :fields="fields" path="compilationTemplate" :edit="true"
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
          fieldType: 'select',
          mapper: {'DSJ': '大事记', 'QZJS': '全宗介绍', 'PICTURE': '图册'}
        },
        // {prop: 'compilationContent', label: '内容'},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false}
      ],
      defaultInsertForm: {businessType: 'compilation'},
      defaultSearchForm: {businessType: 'compilation'}
    }
  }
}
</script>