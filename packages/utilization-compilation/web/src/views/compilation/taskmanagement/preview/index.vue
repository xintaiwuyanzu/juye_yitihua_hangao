<template>
  <table-index title="编研成果预览" :fields="fields" path="compilationtask" :edit="false" :delete="false" :insert="false"
               :default-search-form="defaultSearchForm" ref="table">
    <el-table-column prop="compilationTitle" label="编研主题" align="left" show-overflow-tooltip>
      <template slot-scope="{row}">
        <el-button type="text" size="mini" width="90" @click="preViewer(row)"> {{ row.compilationTitle }}</el-button>
      </template>
    </el-table-column>
    <el-dialog title="编研成果展示" :visible.sync="showPreDialog" width="90%">
      <c-k-editor-bar v-model="content" :value="content"
                      style="height: 100%" :readOnly="true" :toolbar="false" disabled="disabled"/>
      <br/>
      <span v-if="fileList.length>0">【材料清单】：</span>
      <br/>
      <span v-for="(item,index) in fileList" :key="index">
            <el-link type="primary" style="margin-left: 5px;"
                     @click="handlePreview(item)">
              【{{ index + 1 }}】{{ item.fileName }}
            </el-link>
        <br/>
          </span>
    </el-dialog>
    <el-dialog
        :center="true"
        :fullscreen="true"
        :visible.sync="showFile"
        :title="fileInfo.fileName"
        class="fileContent"
        customClass="customWidth">
      <fileContentView :fileName="fileInfo.name" :fileType="fileInfo.suffix" :url="src"
                       style="height: 100%;width: 100%"/>
    </el-dialog>
  </table-index>
</template>
<script>
import {useUser} from "@dr/framework/src/hooks/userUser";

export default {
  name: "index",
  setup() {
    return useUser()
  },
  data() {
    return {
      fields: [
        {
          prop: 'compilationTitle',
          label: '编研主题',
          required: true,
          align: 'left',
          search: true
        },
        {prop: 'creatPersonName', label: '编研人', width: 140, edit: false},
        {prop: 'publishDate', label: '发布时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false},
        {prop: 'compilationDescribe', label: '描述', type: 'textarea', width: 200}
      ],
      defaultSearchForm: {publishStatus: '1', type: 'preview'},//成果预览展示发布后的编研成果
      content: '',
      showPreDialog: false,
      fileList: [],
      showFile: false,
      fileInfo: {},
      src: ''
    }
  },
  methods: {
    preViewer(row) {
      this.showPreDialog = true
      this.content = row.compilationContent
      this.getFileList(row.id)
    },
    downLoadPdf(row) {
      this.$http.post('/management/htmlToPdf', {
        html: row.compilationContent,
        onlyBody: false
      })
    },
    //获取材料清单
    async getFileList(refId) {
      if (refId) {
        const {data} = await this.$http.post('manageFile/page', {businessId: refId, page: false})
        if (data.success) {
          this.fileList = data.data
        } else {
          this.$message.error(data.message)
        }
      }
    },
    handlePreview(file) {
      this.fileInfo = file
      this.showFile = true
      // this.src = `/api/files/downLoad/${file.fileInfoId}?download=false`
      this.src = `/api/fileView/getFile?fileId=${file.fileInfoId}&filePath=${file.fileInfoId}`
    },
  }
}
</script>

<style scoped>

</style>