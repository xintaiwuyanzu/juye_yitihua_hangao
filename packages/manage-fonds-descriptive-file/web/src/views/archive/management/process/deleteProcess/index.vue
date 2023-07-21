<template>
  <process-container
      title="发起【删除全宗卷】的审核流程"
      :before-save="appendParams"
      processType="fondsdescriptivefile"
      :business-id="managementId"
      :status="status"
      optType="delete"
      ref="pro"
      @saved="notifyResult">
    <el-button type="text" style="margin-left: 5px" @click="open">删除</el-button>
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
    managementId: {type: String},
    status: {type: String}
  },
  data() {
    return {}
  },
  methods: {
    open() {
      if (this.status === '0') {
        this.$emit('deleteById')
      } else if (this.status === '2') {
        this.$refs.pro.open()
      }
    },
    /**
     * 追加启动数据
     * @param form
     */
    appendParams(form) {
      //需要判读是添加、修改、删除的审批
      form.historyType = 'delete'
    },
    /**
     *提示返回消息
     * @param data
     */
    notifyResult(data) {
      if (data.success) {
        this.$message.success('启动成功')
        this.$emit('loadData', data)
      } else {
        this.$message.error(data.message)
      }
    },
  }
}
</script>