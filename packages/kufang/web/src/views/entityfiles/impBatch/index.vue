<template>
  <table-index :fields="fields"
               path="impBatch"
               :edit="false"
               :delete="false"
               :insert="false"
               ref="table"
  >
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="20" @click="toDetail(scope.row)">详情</el-button>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fields: {
        recordName: {label: '导入记录名称', search: true, width: 300},
        archiveType: {
          label: '档案类型', search: true, width: 100, fieldType: 'select',
          mapper: {
            "wj": '文件',
            "aj": '案卷',
            "jn": '卷内文件'
          },
          align: 'center'
        },
        quantity: {label: '导入数量', search: false, width: 100},
        quantitySuc: {label: '导入成功数量', search: false, width: 100},
        quantityErr: {label: '导入失败数量', search: false, width: 100},
        createDate: {label: '创建时间', edit: false, dateFormat: "YYYY-MM-DD HH:mm:ss", width: 150},
        remarks: {label: '备注', autosize: true, search: false},
        status: {
          label: '状态', width: 80, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '失败', show: 'danger'},
            '1': {label: '成功', show: 'success'},
          },
        },
      },
    }
  },
  methods: {
    toDetail(row) {
      this.$router.push({path: this.$route.path + '/detail', query: {batchId: row.id, archiveType: row.archiveType}})
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
