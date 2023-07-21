<template>
  <el-dialog title="档案导入" :visible.sync="show" width="40%">
    <el-form ref="form" :model="form" label-width="110px" :inline="false">
      <el-form-item label="导入记录名称" prop="recordName" required>
        <el-input v-model="form.recordName" clearable/>
      </el-form-item>
      <el-form-item label="档案分类">
        <el-select
            v-model="parentType" :disabled="true"
            filterable
            default-first-option
            placeholder="请选择类型">
          <el-option
              v-for="item in archiveTypes"
              :key="item.id"
              :label="item.archiveTypeName"
              :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="类型" prop="archiveType">
        <el-select
            v-model="archiveType" :disabled="true"
            filterable
            default-first-option
            placeholder="请选择类型">
          <el-option
              v-for="item in archiveTypes"
              :key="item.id"
              :label="item.archiveTypeName"
              :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="卷内文件" prop="isJnwj" v-if="ajlx==='aj'">
        <el-select clearable
                   v-model="isJnwj"
                   filterable clear
                   default-first-option
                   placeholder="请选择类型">
          <el-option
              v-for="item in jnwjTypes"
              :key="item.id"
              :label="item.label"
              :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="remarks" label-width="110px">
        <el-input v-model="remarks" clearable/>
      </el-form-item>
      <el-form-item label-width="0">
        <el-upload style="text-align: center;margin-top: 50px"
                   ref="uploadFile"
                   action="api/impBatch/impArchive"
                   accept="text/xml, application/xml,.xml,.xlsx,.xls,.zip,.dbf,.accdb"
                   :before-upload="beforeUpload"
                   :on-success="Push"
                   :data="{
                         recordName:this.form.recordName,
                         archiveType:this.archiveType,
                         remarks:this.remarks,
                         isJnwj:this.isJnwj
                     }"
                   :limit="1"
                   :on-exceed="handleExceed"
                   :auto-upload="false">
          <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">选取文件</el-button>
          <div style="margin-bottom: 20px">可上传xlsx,xls,xml,zip,dbf,accdb文件</div>
        </el-upload>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="info" @click="reset" class="btn-cancel">取 消</el-button>
      <el-button type="primary" @click="submitUpload" v-loading="loading" class="btn-submit">上 传</el-button>
    </div>
  </el-dialog>
</template>
<script>
export default {
  name: "form",
  data() {
    return {
      show: false,
      classCode: '',
      fondId: '',
      remarks: '',
      form: {
        recordName: '',
      },
      loading: false,
      archiveTypes: [],
      parentType: '',
      archiveType: '',
      ajlx: '',
      isJnwj: '',
      jnwjTypes: [
        {id: 'jnwj', label: '卷内文件'}
      ]
    }
  },
  methods: {
    $init() {
      this.$http.post('/archiveType/page', {page: false})
          .then(({data}) => {
            if (data.success && data.data != null) {
              this.archiveTypes = data.data
              this.openTypes = true
            }
          })
    },
    beforeUpload() {
    },
    Push() {
      this.$message.success("上传成功，可以在上传记录中查询导入记录")
      this.$emit("reload")
      this.reset()
    },
    reset() {
      this.show = false
      this.form = {}
      this.isJnwj = ''
      this.$refs.uploadFile.clearFiles()
    },
    handleExceed() {
    },
    async submitUpload() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$refs.uploadFile.submit();
        }
      })
    },
    getType(val) {
      this.parentType = val.parentType
      this.archiveType = val.archiveType
      this.ajlx = val.ajlx
    },
  }
}
</script>

<style scoped>

</style>
