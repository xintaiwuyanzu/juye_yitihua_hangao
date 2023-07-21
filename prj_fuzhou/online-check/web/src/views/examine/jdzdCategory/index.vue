<template>
  <table-index :fields="fields" path="jdzdCategory/" :insert="true" :edit="true" :delete="true" ref="table"
               deleteMulti
               :defaultSearchForm="defaultSearchForm">
  </table-index>
</template>

<script>

export default {
  components: {},
  data() {
    return {
      dict: [],
      rules: {
        personId: [{required: true, message: '请选择审核人', trigger: 'blur'}],
        name: [{required: true, message: '请输入批次', trigger: 'blur'}]
      },
      //新代码
      fields: [
        // {prop: 'largeCategory', label: '检查内容', search: true},
        //使用的字典
        {
          prop: 'largeCategory',
          label: '检查内容',
          width: "100",
          dictKey: 'zfjc.type',
          search: true,
          fieldType: 'select',
          required: true
        },
        {prop: 'smallCategory', label: '具体内容', search: false, type: 'textarea', align: 'left'},
        {prop: 'rate', label: '比率', search: false, width: "70"},//TODO 这里想搞个计数器 不会搞
        {prop: 'fraction', label: '分数', search: false, width: "70"},
        {prop: 'remark', label: '检查细则与评分说明', search: false, type: 'textarea', align: 'left'},
        {
          prop: 'status', label: '状态', width: "70", component: 'tag', showTypeKey: 'show', fieldType: 'select',
          mapper: {
            '1': {label: '启用', show: 'primary'},
            '0': {label: '禁用', show: 'success'}
          },
        }
      ],
      defaultSearchForm: {categoryId: ''}
    }
  },
  computed: {
    /**
     * 映射table-index 选中数据
     * @returns {[]|*|*[]}
     */
    selection() {
      if (this.$refs.table && this.$refs.table.tableSelection) {
        return this.$refs.table.tableSelection
      } else {
        return []
      }
    }
  },
  filters: {},
}
</script>
