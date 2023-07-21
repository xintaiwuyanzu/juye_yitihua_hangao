<template>
  <section>
    <el-dialog title="处理" :visible.sync="selectListShow" width="70%" center :before-close="beforeClose">
      <el-table :data="selectList" border ref="archiveTable" height="330">
        <el-table-column label="排序" type="index" align="center"/>
        <el-table-column prop="FOND_CODE" label="所属全宗" align="center" show-overflow-tooltip/>
        <el-table-column prop="CATE_GORY_CODE" label="所属门类" align="center"/>
        <el-table-column prop="TITLE" label="题名" align="center"/>
        <el-table-column prop="ARCHIVE_CODE" label="档号" align="center"/>
        <el-table-column prop="ku" label="来源" align="center">
          <template v-slot="scope">
            {{ scope.row.ku|dict({'manager': '管理库', 'other': '其它'}) }}
          </template>
        </el-table-column>
        <el-table-column prop="NOTE" label="备注" align="center"/>
        <el-table-column label="操作" align="center">
          <template v-slot="scope">
            <el-link type="success" size="mini" width="90" @click="openFile(scope.row)">原文</el-link>
            <el-divider direction="vertical"></el-divider>
            <el-link type="success"
                     @click="huifu(scope.row)"
                     size="mini" width="33">恢复
            </el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
    <!--原文弹窗显示-->
    <el-dialog :visible.sync="fileDialog" title="查看原文" width="80%">
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
        <el-button @click="fileDialog=false">取消</el-button>
      </div>
    </el-dialog>
    <file-list v-on:uploadYuanwen="process" v-if="fileListDialog" refType="cqbc" :formDataId="formDataId"
               style="margin-top: 5px"
               width="50%"
               :transform="false" :upload="true" :deleter="false"/>
  </section>
</template>
<script>
export default {
  data() {
    return {
      selectList: [],
      processContent: {},
      processDialogShow: false,
      selectListShow: false,
      fileDialog: false,
      pdfList: [],
      loading: false,
      columns: [
        {prop: 'name', label: '文件名称'},
        {prop: 'suffix', label: '文件类型', width: '180'},
        {prop: 'saveDate', label: '上传日期', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'description', label: '备注', width: '180'},
      ],
      fileListDialog: false,
      formDataId: ''
    }
  },
  name: "alarmdeal",
  methods: {
    async process() {
      const {data} = await this.$http.post('cqbcAlarm/process', {id: this.processContent.id})
      if (data.success) {
        this.$message.success('处理成功！')
        this.beforeClose()
        this.$emit('func')
      } else {
        this.loading = false
        this.$message.error(data.message)
      }
    },
    showDig(row) {
      this.processContent = row
      this.processContent.processResult = ''
      this.toSelectList(row)
    },
    async toSelectList(row) {
      this.loading = true
      const {data} = await this.$http.post('cqbcAlarm/selecArchivetList', {archiveId: row.archiveId})
      if (data.success) {
        this.selectList = data.data
        this.selectListShow = true
      } else {
        this.$message.error('未从管理库查找到相关档案信息，请手动上传！')
        this.fileListDialog = true
        this.formDataId = row.archiveId
      }
      this.loading = false
    },
    async huifu(row) {
      await this.process()
    },
    beforeClose() {
      this.processContent.processResult = ''
      this.processContent = {}
      this.processDialogShow = false
      this.selectListShow = false
    },
    async openFile(row) {
      this.fileDialog = true
      this.loading = true
      if (row.formDataId) {
        const {data} = await this.$http.post('files/list', {
          refId: row.id,
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
  },
}
</script>

<style scoped>

</style>
