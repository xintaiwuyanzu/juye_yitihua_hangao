<template>
  <section>
    <el-link type="primary" @click="showDialog">素材库</el-link>
  </section>
</template>
<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'material',
  methods: {
    showDialog() {
      this.$http.post('/material/insert', {
        formDefinitionId: this.formId,
        formDataId: this.row.id,
        fileNumber: this.row.ARCHIVE_CODE,
        fileName: this.row.TITLE,
        // wenhao:this.row.
      }).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功!')
              this.eventBus.$emit("loadData")
            } else {
              this.$message.error(data.message)
            }
          })
    },
  },
}
</script>
