<template>
  <table-index :fields="fields" path="specialTagArchive"
               pagePath="specialTagArchive/selectTagCount"
               :defaultSearchForm="defaultSearchForm"
               :edit="false" :delete="false" :insert="false"
               ref="table">
    <template v-slot:table-$btns="{row}">
      <el-link type="primary" @click="detail(row)">详情</el-link>
    </template>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'tagName', label: '标签名', search: true},
        {prop: 'createPerson', label: '创建人', edit: false},
        {prop: 'archiveCount', label: '应用档案数量'}
      ],
      defaultSearchForm: {tagName: ''},
    }
  },
  methods: {
    detail(row) {
      //跳转到明细页面
      this.$router.push({
        path: '/utilization/specialTagArchive/detail',
        query: {
          specialTagId: row.specialTagId
        }
      })
    },
  }
}
</script>

<style scoped>

</style>