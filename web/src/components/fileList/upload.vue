<template>
  <section>
    <el-dialog :title="uploadButton" :visible.sync="imports" :close-on-click-modal=true  :modal-append-to-body=false
               :destroy-on-close=true append-to-body>
      <el-upload style="text-align: center;margin-top: 50px"
                 class="upload-demo"
                 ref="uploadFiles"
                 action="api/files/upload"
                 :before-upload="beforeUpload"
                 :on-success="Push"
                 :on-error="error"
                 accept="*"
                 :limit="1"
                 :headers="myHeader"
                 :data="{
                          refId:this.formDataId,
                          refType:this.refType
                       }"
                 :on-exceed="handleExceed"
                 :auto-upload="false"
                 :file-list="fileList">
        <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">{{ selectionButton }}</el-button>
        <el-button style="margin-left: 10px;" size="medium" type="success" @click="submitUpload">
          <i class="el-icon-upload el-icon--right"/>{{ uploadButton }}
        </el-button>
        <div slot="tip" class="el-upload__tip">{{ explain }}</div>
      </el-upload>
    </el-dialog>
  </section>

</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  data() {
    return {
      imports: false,
      fileList: [],
      myHeader: {
        $token: sessionStorage.getItem('$token')
      },
    }
  },
  methods: {
    $init() {
      this.imports=false
    },
    getConfigScheme() {
      if (!this.formDataId) {
        this.$message.error("请选择一项信息!")

      } else {
        this.imports = true
        this.fileList = []
      }
    },
    cancel() {
      this.imports = false
    },
    Push(val) {
      if (val.success) {
        this.$message.success("上传成功!")
        this.imports = false
        this.drawer = false
        this.$emit('func')
        this.$emit("uploadYuanwen")
      } else {
        this.$message.error(val.message)
      }
    },
    error() {
      this.$message.error("上传失败!")

    },
    submitUpload() {
      this.$refs.uploadFiles.submit();
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeUpload(file) {
      file.name.replace(/.+\./, "");
    }
  },
  props: {
    formDataId: String,
    refType: {default: 'default'},
    groupCode: {default: 'default'},
    //选取按钮的标题
    selectionButton: {default: '选取原文'},
    //上传按钮的标题
    uploadButton: {default: '上传原文'},
    //说明
    explain: {default: '上传文件最大500MB!'},
  },
  mixins: [fromMixin]
}
</script>
