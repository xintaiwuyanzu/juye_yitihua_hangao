<template>
  <table-index :fields="fields" path="tagLibArchive" :edit="false" :insert="false" :delete="false" ref="table">
    <template v-slot:table-$btns="scope">
      <el-button type="primary" @click="update(scope.row,'1')" size="mini" width="50">通过</el-button>
      <el-button type="info" @click="update(scope.row,'0')" size="mini" width="50">拒绝</el-button>
    </template>
  </table-index>
</template>

<script>
/**
 * 自定义标签：查询标签档案表数据
 */
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'tagName', label: '标签名', search: true},
        {prop: 'createDate', label: '添加时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'createPersonName', label: '添加人'},
        {
          prop: 'status', label: '状态', width: "80", component: 'tag', showTypeKey: 'show', mapper: {
            '1': {label: '通过', show: 'success'},
            '0': {label: '拒绝', show: 'warning'}
          }, fieldType: 'select', edit: false
        },
        {
          prop: 'title', label: '相关档案', align: 'left', component: 'text',
          route: true,
          routerPath: '/archive/metadataAndFileDetail',
          queryProp: this.routeQuery
        },
      ]
    }
  },
  methods: {
    routeQuery(row) {
      return {formDefinitionId: row.formDefinitionId, formDataId: row.formDataId, refType: 'archive', watermark: false,}
    },
    async update(row, status) {
      const {data} = await this.$http.post('tagLibArchive/update', {id: row.id, status: status})
      if (data && data.success) {
        this.$message.success("成功！")
        this.$refs.table.reload()
      } else {
        this.$message.error(data.message)
      }
    }
  }
}
</script>

<style scoped>

</style>