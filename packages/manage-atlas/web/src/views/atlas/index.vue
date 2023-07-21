<template>
  <table-index :defaultSearchForm="defaultSearchForm" :edit="false" :fields="fields" :insert="false" path="atlas"
               ref="table">
    <template v-slot:search-$btns="scope">
      <el-button @click="addAtlas()" type="primary">添加</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="viewAtlas(scope.row)" type="text" width="30">详情</el-button>
    </template>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'atlasName', label: '图册名称', search: true, required: true},
        {
          prop: 'atlasType', label: '图册类型', edit: false, search: true
        },
        {prop: 'createPerson', label: '创建人', edit: false},
        {prop: 'createDate', label: '保存时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false},
      ],
      defaultSearchForm: {atlasType: '', atlasName: ''}
    }
  },
  methods: {
    addAtlas(row) {
      this.$router.push('/atlas/continue')
    },
    viewAtlas(row) {
      this.$router.push({path: '/atlas/detail', query: {id: row.id}})
    }
  }
}
</script>

<style scoped>

</style>