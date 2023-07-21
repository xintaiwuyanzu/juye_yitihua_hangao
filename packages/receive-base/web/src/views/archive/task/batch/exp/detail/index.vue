<template>
  <table-index :default-search-form="defaultSearchForm" :delete="false" :edit="false" :fields="fields" :insert="false"
               back path="batchDetail" title="导出详情"></table-index>
</template>
<script>
import indexMixin from '@/util/indexMixin'

/**
 * 批次列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      fields: {
        title: {label: '题名', search: true},
        fondName: {label: '全宗', search: true},
        orgCode: {label: '机构或问题'},
        archiveCode: {label: '档号'},
        keyWords: {label: '关键词'},
        year: {label: '年度'}
      },
    }
  },
  methods: {
    defaultSearchForm() {
      return {
        batchId: this.$route.query.batchId
      }
    }
  }
}
</script>
