<template>
  <process-container
      title="发起全宗卷审核流程"
      :before-save="appendParams"
      processType="fondsdescriptivefile"
      :business-id="businessId"
      ref="pro"
      @saved="notifyResult">
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
    businessId: {type: String}
  },
  methods: {
    /**
     * 追加启动数据
     * @param form
     */
    appendParams(form) {
      this.$set(form, 'historyType', 'add')
    },
    /**
     *提示返回消息
     * @param data
     */
    notifyResult(data) {
      if (data.success) {
        this.$message.success('启动成功')
        this.$emit('updateStatus', data)
      } else {
        this.$message.error(data.message)
      }
    },
  }
}
</script>