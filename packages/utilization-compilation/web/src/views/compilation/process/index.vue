<template>
  <process-container
      title="发起档案编研审核流程"
      :before-save="appendParams"
      processType="compilation"
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
  data() {
    return {}
  },
  methods: {
    /**
     * 追加启动数据
     * @param form
     */
    appendParams(form) {
      //TODO 需要判读是添加、修改、删除的审批
      // form.historyType = ''
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
    },
  }
}
</script>