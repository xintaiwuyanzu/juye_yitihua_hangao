<template>
  <process-container
      title="发起档案调整审核流程"
      :before-save="appendParams"
      processType="archivechange"
      :businessId="form.businessId"
      ref="pro"
      @saved="notifyResult">
    <el-button type="primary" width="20" @click="submitBatchTask">提交审核</el-button>
  </process-container>
</template>
<script>
import {processContainer} from '@dr/process/src/lib'

/**
 * 启动流程按钮
 */
export default {
  name: "startProcess",
  components: {processContainer},
  props: {
    form: {type: Object}
  },
  methods: {
    open() {
      this.$refs.pro.open()
    },
    /**
     * 追加启动数据
     * @param form
     */
    appendParams(form) {
      form.formDefinitionId = this.form.formDefinitionId
      form.formDataId = this.form.formDataId
      form.fieldsCode = this.form.fieldsCode
      form.newValues = this.form.newValues
      form.oldValues = this.form.oldValues
      form.fieldNames = this.form.fieldNames
      form.errorType = this.form.errorType
      form.errorDescription = this.form.errorDescription
      form.errorSource = this.form.errorSource
      if(this.form.status === '0'){
        form.status = '2'
      }else{
        form.status = this.form.status
      }
      form.fondId = this.form.fondId
      form.categoryId = this.form.categoryId
    },
    submitBatchTask() {
      this.$emit('validate')
    },
    getbusinessId(){
      return this.form.businessId
    },
    /**
     *提示返回消息
     * @param data
     */
    notifyResult(data) {
      if (data.success) {
        this.$message.success('启动成功')
        this.$emit('afterStartProcess', data)
      } else {
        this.$message.error(data.message)
      }
    }
  }
}
</script>