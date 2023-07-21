<template>
  <section>
    <el-link type="success"
             @click="$router.push({path: '/archive/metadataAndFileDetail',query: {formDataId: row.id,formDefinitionId: formId,refType: 'archive',groupCode: 'default',watermark: false}})">
      <div v-html="'详情'"/>
    </el-link>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'yuanwen',
  data() {
    return {}
  },
  mounted() {
    this.$on('uploadYuanwen', this.updateStatusYuanwen)
  },
  methods: {
    updateStatusYuanwen() {
      let param = {'categoryId': this.category.id, 'id': this.row.id, 'formId': this.formId}
      this.$http.post('/manage/formData/updateHaveYuanwen', param).then(
          ({data}) => {
            this.loading = false
            if (data.success) {
              this.row.yw_have = data.data
            } else {
              this.$message.error("更新原文信息失败")
            }
          })
    }
  }
}
</script>