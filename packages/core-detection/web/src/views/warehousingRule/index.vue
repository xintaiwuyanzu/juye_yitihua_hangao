<template>
  <table-index title="入库规则检测项设置" :fields="fields" path="warehousingRule/"
               ref="table"
               deleteMulti>
    <template slot-scole='form' slot='search-$btns'>
      <el-button @click="excel">批量导入</el-button>
    </template>
      <el-dialog :title="uploadButton" :visible.sync="imports" :modal-append-to-body='false' append-to-body>
        <el-upload style="text-align: center;margin-top: 50px"
                   class="upload-demo"
                   ref="uploadFiles"
                   action="api/warehousingRule/upload"
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

    <template v-slot:edit="form">
      <el-form-item prop="testPurpose" label="检测目的" required>
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="form.testPurpose">
        </el-input>
      </el-form-item>
      <el-form-item prop="testMethod" label="检测方法">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="form.testMethod">
        </el-input>
      </el-form-item>
      <el-form-item prop="project" label="检测项目">
        <el-input
            type="textarea"
            :rows="2"
            placeholder="请输入内容"
            v-model="form.project">
        </el-input>
      </el-form-item>
      <el-form-item prop="remark" label="备注信息" >
        <el-input v-model="form.remark" placeholder="请输入内容"></el-input>
      </el-form-item>
    </template>
  </table-index>
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
      fields: [
        {prop: 'linkNature', label: '环节性质', width: '150',fieldType: 'select',
          mapper: {
            "1": '归档环节',
            "2": '移交与接收环节',
            "3":'长期保存环节'
          }},
        {prop: 'testObject', label: '检测对象', width: '120', search: true, },
        {prop: 'ruleNature', label: '规则性质', width: '140', search: true,fieldType: 'select',
          mapper: {
            "1": '真实性检测',
            "2": '完整性检测',
            "3":'可用性检测',
            "4":'安全性检测'
          }},
        {prop: 'testCode', label: '编码', width: '140', required: true},
        {prop: 'testType', label: '检测目标类型', width: '140', search: true, required: true,fieldType: 'select',
          mapper: {
            "1": '默认',
            "2": '包结构',
            "3": '元数据'
          }},

      ],
    }
  },
  methods: {
    excel(){
      this.imports = true
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