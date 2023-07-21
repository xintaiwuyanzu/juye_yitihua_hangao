<template>
  <table-index title="档案详情" :fields="fields" :path="this.$route.query.path" :pagePath="this.$route.query.pagePath"
               :insert="false"
               :edit="false" :delete="false"
               :default-search-form="defaultSearchForm" back ref="table">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="90" @click="downloadFile(scope.row)">下载</el-button>
    </template>
  </table-index>
</template>

<script>
export default {
  name: "index.vue",
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', align: 'left'},
        {prop: 'archiveCode', label: '档号', width: '180'},
        {prop: 'written', label: '形成日期', width: '100'},//TODO 这个字段被用坏了，先这样
        // {prop: 'categoryCode', label: '门类名', width: '150'},
        // {prop: 'warehouseName', label: '入库批次', width: '100'},
        // {prop: 'fondName', label: '全宗', width: '150'},
      ],
      defaultSearchForm: {deliveryId: this.$route.query.deliveryId},
      fileListDialog: false,
      pdfList: [],
      loading: false,
      columns: [
        {prop: 'name', label: '文件名称'},
        {prop: 'suffix', label: '文件类型', width: '180'},
        {prop: 'saveDate', label: '上传日期', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'description', label: '备注', width: '180'},
      ]
    }
  },
  methods: {
    downloadFile(row) {
      this.$http.post('delivery/download', {id: row.id}).then(({data}) => {
        if (data && data.success) {
          window.open(data.data)
        }
      })

    },
  }
}
</script>