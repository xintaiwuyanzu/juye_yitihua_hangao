<template>
  <div>
    <el-dialog
        :before-close="cancel"
        :visible.sync="importDialog"
        title="导入词库"
        width="50%">
      <el-alert
          style="margin-bottom: 10px"
          title="为了展示效果，请以回车分隔"
          type="warning">
      </el-alert>
      <div style="text-align: right">
        <el-upload
            :action="path"
            :before-upload="beforeUpload"
            :file-list="fileList"
            :on-success="onSuccess"
            class="upload-demo">
          <el-button size="small" type="primary">导入文本（txt）</el-button>
        </el-upload>
      </div>
      <h3 style="margin-bottom: 5px">手动录入</h3>
      <el-input :rows="15" type="textarea" v-model="content"></el-input>
      <span class="dialog-footer" slot="footer">
          <el-button @click="cancel">取 消</el-button>
          <el-button @click="saveWord" type="primary" v-loading="loading">确 定</el-button>
        </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "tagWordImport",
    props: {
      importDialog: Boolean,
      path: String,
      fileType: String
    },
    data() {
      return {
        fileList: [],
        loading: false,
        content: ''
      }
    },
    methods: {
      cancel() {
        this.$emit('cancel')
      },
      saveWord() {
        if (this.content.trim() !== '') {
          this.$emit('saveWord', this.content)
        }
      },
      beforeUpload(file) {
        const b = file.type === this.fileType;
        if (!b) {
          this.$message.error('上传文件只能是 TXT 格式!');
        }
        return b
      },
      onSuccess() {
        this.$emit('onSuccess')
      }
    },
    watch: {
      importDialog() {
        this.content = ''
        this.fileList = []
      }
    }
  }
</script>