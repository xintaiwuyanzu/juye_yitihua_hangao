<template>
  <task-container :task-instance-id="taskId" v-if="taskId">
    <template v-slot:sendNext="params">
      <send-next @sendSaved="sendSaved"/>
    </template>
    <template v-slot:endProcess="params">
      <end-process @saveEnd="sendSaved"/>
    </template>
  </task-container>
</template>
<script>
import {endProcess, sendNext, taskContainer} from '@dr/process/src/lib'

/**
 * 流程
 */
export default {
  name: "taskButtons",
  components: {taskContainer, sendNext, endProcess},
  data() {
    return {
      /*//环节Id
      taskId: this.$route.query.taskId*/
    }
  },
  computed: {
    //环节Id
    taskId() {
      return this.$route.query.taskId
    }
  },
  methods: {
    sendSaved(data) {
      if (data.success) {
        this.$message.success('操作成功！')
        this.$router.back()
      } else {
        this.$message.error(data.message)
      }
    }
  }
}
</script>
