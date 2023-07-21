<template>
  <table-index title="详情" :fields="fields" path="cqbcDeliveryDetail" :insert="false" :edit="false" :delete="false"
               :default-search-form="defaultInsertForm" back/>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', search: true},
        {prop: 'fondName', label: '全宗', width: 150, search: false},
        {prop: 'categoryName', label: '门类', width: 100, search: false},
        {prop: 'archiveCode', label: '档号', width: 150, search: false},
        {prop: 'annual', label: '年度', width: 80, search: false},
        /*{prop: 'fileTime', label: '形成日期', search: false},*/
        {prop: 'classificationName', label: '所属分类', width: 100, search: false},
      ],
      defaultInsertForm: {deliveryId: this.$route.query.id}
    }
  }
}
</script>