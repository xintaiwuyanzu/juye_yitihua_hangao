<template>
  <table-index :fields="fields" path="tagLibArchive"
               :defaultSearchForm="defaultSearchForm"
               :edit="false" :insert="false"
               ref="table" back>
  </table-index>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  name: "index.vue",
  mixins: [indexMixin, formMixin],
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', search: true, required: true},
        {prop: 'archiveCode', label: '档号', search: true, required: true},
        {prop: 'fondCode', label: '全宗号'},
        {prop: 'categoryCode', label: '分类号'},
        {prop: 'year', label: '年度'}
      ],
      defaultSearchForm: {title: '', archiveCode: '',tagLibId: this.$route.query.tagLibId},
    }
  },
  methods: {
    loadData() {
      this.$refs.table.loadData(this.defaultSearchForm);
    }
  }
}
</script>
