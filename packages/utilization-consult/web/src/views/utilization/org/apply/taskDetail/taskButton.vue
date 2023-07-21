<template>
  <task-container :task-instance-id="taskInstanceId" @sendSaved="sendSaved" @saveEnd="saveEnd">
  </task-container>
</template>
<script>
import {taskContainer} from '@dr/process/src/lib'

/**
 * 环节按钮
 */
export default {
  name: "taskButton",
  props: {
    taskInstanceId: {type: String}
  },
  components: {taskContainer},
  methods: {
    /**
     * 发送成功回调
     * @param data
     */
    sendSaved(data) {
      this.$message.success('发送成功！')
      this.$router.back()
    },
    saveEnd() {
      this.$message.success('办结成功！')
      this.$router.back()
    }
  }
}
</script>