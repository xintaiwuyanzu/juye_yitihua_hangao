<template>
  <table-index title="入库审核详情"
               path="filing"
               :fields="fields"
               :insert="false"
               :edit="false" :delete="false"
               :default-search-form="defaultSearchForm"
               back
               ref="table">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="90" @click="openFile(scope.row)">查看原文</el-button>
    </template>
    <template v-slot:search-$btns="scope">
      <el-button type="primary" @click="checkSend">提交</el-button>
      <el-button type="primary" @click="complete">办结</el-button>
      <el-button type="primary" @click="showHistory($route.query.processInstanceId)">流转历史</el-button>
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
import abstractProcess from "@dr/process/src/lib/abstractProcess";

export default {
  extends: abstractProcess,
  data() {
    const filingId = this.$route.query.id || this.$route.query.processInstanceId
    return {
      fields: [
        {prop: 'title', label: '题名', align: 'left'},
        {prop: 'archiveCode', label: '档号', width: '180'},
        {prop: 'fondName', label: '全宗名', width: '150'},
        {prop: 'categoryName', label: '门类名', width: '150'},
        /*{prop: 'vintages', label: '年度', width: '100'},
        {prop: 'saveTerm', label: '保管期限', width: '100'},*/
      ],
      defaultSearchForm: {filingId, type: this.$route.query.type},
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
          // groupCode: 'default'
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
    /**
     * 发送到下一环节
     * @returns {Promise<void>}
     */
    async checkSend() {
      const data = await this.sendNext(this.$route.query.taskId)
      if (data) {
        this.$message.success('提交成功')
        this.$router.back()
      }
    },
    /**
     * 办结按钮
     * @returns {Promise<void>}
     */
    async complete() {
      const data = await this.sendNext(this.$route.query.taskId, {complete: true})
      if (data) {
        console.log(data)
        this.$message.success('办结成功！')
        this.$router.back()
      }
    }
  }
}
</script>
