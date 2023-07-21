<template>
  <metadataFileView :showHeader="false"
                    :formData="formData"
                    refType="archive">
    <audit-form slot="detailTop"
                :task-id="taskId"
                :batch-id="detail.batchId"
                :detail-id="detail.id"
                @next="emitNext"/>
  </metadataFileView>
</template>
<script>
import AuditForm from "./auditForm";

/**
 * 查档每条数据
 */
export default {
  name: "items",
  components: {AuditForm},
  props: {
    taskId: {type: String},
    /**
     * 查档详情
     */
    detail: {type: Object, default: () => ({})},
    /**
     *表单数据
     */
    formData: {type: Object, default: () => ({})}
  },
  methods: {
    emitNext() {
      this.$emit('next')
    }
  }
}
</script>