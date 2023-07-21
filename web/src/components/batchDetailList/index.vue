<template>
  <table-index title="档案详情" :fields="fields" :path="path" :pagePath="pagePath" :insert="false"
               :edit="false" :delete="false"
               :default-search-form="defaultSearchForm" back ref="table">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="90" @click="openFile(scope.row)">查看原文</el-button>
    </template>
    <!--原文弹窗显示-->
    <el-dialog :visible.sync="fileListDialog" title="查看原文" width="80%">
      <table-render class="table-container"
                    :columns="columns"
                    :data="pdfList">
        <el-table-column label="操作" width="120" header-align="center" align="center">
          <template v-slot="scope">
            <el-link type="success" @click="downLoad(scope.row)">预览</el-link>
          </template>
        </el-table-column>
      </table-render>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fileListDialog=false">取消</el-button>
      </div>
    </el-dialog>
    <!-- TODO 此方式目前有问题，先用上面的方式实现 -->
    <!--    <file-list refType="archive" :formDataId="formDataId" style="margin-top: 5px" width="50%"
                   :visible.sync="fileListDialog"
                   :transform="false" :upload="false" :deleter="false"/>-->
  </table-index>
</template>

<script>
export default {
  name: "index.vue",
  props: {
    path: {type: String},
    pagePath: {type: String},
    batchId: {type: String},
    type: {type: String},
    id: {type: String},
    batchType: {type: String},
  },
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', align: 'left'},
        {prop: 'archiveCode', label: '档号', width: '180'},
        {prop: 'year', label: '形成日期', width: '100'},//TODO 这个字段被用坏了，先这样
        // {prop: 'categoryCode', label: '门类名', width: '150'},
        {prop: 'sourceValue', label: '保管期限', width: '100'},//TODO 这个字段被用坏了，先这样
        // {prop: 'fondName', label: '全宗', width: '150'},
      ],
      defaultSearchForm: {batchId: this.batchId, type: this.type, id: this.id, batchType: this.batchType},
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
    async openFile(row) {
      this.fileListDialog = true
      this.loading = true
      if (row.formDataId) {
        const {data} = await this.$http.post('files/list', {
          refId: row.formDataId,
          refType: 'archive',
        })
        if (data.success) {
          this.loading = false
          if (data && data.success) {
            this.pdfList = data.data
            this.$forceUpdate()
          }
        }
      }
      this.loading = false
    },
    async downLoad(row) {//todo openController已经删除，需要修改
      const suffix = row.suffix.toUpperCase()
      if (suffix === "OFD" || suffix === 'PDF') {
        const {data} = await this.$post('/open/getFilePath', {id: row.id})
        if (data.success) {
          window.open(data.data, "_blank")
        } else {
          this.$message.error(data.message)
        }
      } else if (suffix === "XML") {
        window.open(`api/files/downLoad/${row.id}?download=true`)
      } else {
        window.open(`api/files/downLoad/${row.id}?download=false`, "_blank")
      }
    },
  }
}
</script>
