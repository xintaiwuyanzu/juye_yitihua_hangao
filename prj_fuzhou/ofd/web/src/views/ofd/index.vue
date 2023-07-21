<template>
  <table-index :back="true" :insert="false" :edit="false" :delete="false" path="ofd_transform" :fields="fields"
               ref="table">
    <template v-slot:search-$btns>
      <el-button icon="el-icon-upload" type="primary" @click="visible = true">上传文件</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button icon="el-icon-refresh" type="text" @click="transform(scope.row)" width="80">重新转换</el-button>
      <el-button icon="el-icon-download" type="text" @click="downloadFile(scope.row,scope.row.fromType)" width="80">
        源文件下载
      </el-button>
      <el-button icon="el-icon-download" type="text" @click="downloadFile(scope.row,'ofd')" width="80"
                 v-show="scope.row.status==='0'">
        ofd下载
      </el-button>
    </template>

    <el-dialog title="文件转换-ofd" :visible.sync="visible">
      <div style="text-align: center">
        <el-upload
            class="upload-demo"
            drag
            :file-list="fileList"
            :auto-upload="false"
            :on-success="onSuccess"
            ref="upload"
            action="/api/ofd_transform/upload"
            multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">支持上传 tif, jpg, pdf 文件，且不超过500MB</div>
        </el-upload>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="visible = false,loading=false">取 消</el-button>
        <el-button type="primary" @click="sure" :loading="loading">保存并转换</el-button>
      </span>
    </el-dialog>
  </table-index>
</template>
<script>
/**
 * TODO ofd文件转换前端页面，待实现
 */
export default {
  name: "index",
  data() {
    return {
      visible: false,
      fileList: [],
      fields: [
        {prop: 'fileName', label: '源文件名', search: true},
        {prop: 'fromType', label: '源文件类型'},
        {prop: 'fileSize', label: '文件大小(KB)', sortable: true},
        {prop: 'updateDate', label: '转换时间', dateFormat: "YYYY-MM-DD HH:mm:ss", sortable: true},
        {
          prop: 'status', label: '转换状态', component: 'tag', mapper: {
            '0': {label: '转换成功', show: 'success'},
            '1': {label: '转换失败', show: 'danger'}
          }
        }
      ],
      loading: false
    }
  },
  methods: {
    onSuccess(response) {
      this.$message({
        message: response.message,
        type: response.success ? 'success' : 'error'
      });
      this.visible = false;
      this.fileList = [];
      this.$refs.table.loadData();
      this.loading = false
    },
    sure() {
      this.loading = true;
      this.$refs.upload.submit()
    },
    downloadFile(record, type) {
      window.location.href = '/api/ofd_transform/download?id=' + record.id + "&type=" + type
    },
    transform(record) {
      this.$post('ofd_transform/transform', record).then(({data}) => {
        if (data && data.success) {
          this.onSuccess(data)
        }
      })
    }
  }
}
</script>