<template>
  <task-container :task-instance-id="taskInstanceId"
                  :business-id="businessId"
                  style="display: inline-flex">
    <template v-slot:sendNext="params">
      <send-next :before-open="beforeSendDialogOpen" @sendSaved="sendSaved">
      </send-next>
    </template>
    <template v-slot:endProcess="params">
      <end-process :before-open="beforeSendDialogOpen" @saveEnd="saveEnd">
        <template v-slot:sendForm="{form}">
          <el-form-item prop="isPass" label="是否通过" required>
            <el-radio-group v-model="form.isPass">
              <el-radio v-model="form.isPass" label="2">是</el-radio>
              <el-radio v-model="form.isPass" label="3">否</el-radio>
            </el-radio-group>
          </el-form-item>
        </template>
      </end-process>
    </template>
  </task-container>
</template>
<script>
import {taskContainer, sendNext, endProcess} from '@dr/process/src/lib'

/**
 * 环节按钮
 */
export default {
  name: "taskButton",
  props: {
    taskInstanceId: {type: String},
    businessId: {type: String},
    compilationContent: {type: String}
  },
  data() {
    return {}
  },
  components: {taskContainer, sendNext, endProcess},
  methods: {
    /**
     * 发送按钮弹窗
     */
    beforeSendDialogOpen(form) {
      this.$set(form, 'isPass', '2')
      this.$set(form, 'businessId', this.$route.query.businessId)
      this.$set(form, 'examinationOpinion', '')
      this.$set(form, 'compilationContent', this.compilationContent)
    },
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
    },
    send(params) {

    }
  }
}
</script>