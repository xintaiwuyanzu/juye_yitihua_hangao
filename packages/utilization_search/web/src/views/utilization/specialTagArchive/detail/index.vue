<template>
  <table-index :fields="fields" path="specialTagArchive"
               :defaultSearchForm="defaultSearchForm"
               :edit="false" :insert="false"
               ref="table" back>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', search: true, required: true},
        {prop: 'archiveCode', label: '档号', search: true, required: true},
        {prop: 'fondCode', label: '全宗号'},
        {prop: 'categoryCode', label: '分类号'},
        {prop: 'year', label: '年度'}
      ],
      defaultSearchForm: {title: '', archiveCode: '', specialTagId: this.$route.query.specialTagId},
    }
  },
}
</script>

<style scoped>

</style>