<template>
  <metadata-file-view title="档案详情" :formDataId="detail.formDataId"
                      :formDefinitionId="detail.formDefinitionId"  refType="archive"
                      :form-data="formData">
    <el-button type="primary" @click="back()">返回</el-button>
  </metadata-file-view>
</template>
<script>
export default {
  name: 'handOverArchiveDetail',
  data() {
    return {
      detail: {
        formDataId: '',
        formDefinitionId: ''
      },
      formData: {}
    }
  },
  methods: {
    async $init() {
      const {data} = await this.$post('/manage/handover/details/detail', {id: this.$route.query.id})
      if (data.success) {
        this.detail = data.data
        const formData = await this.$http.post('manage/formData/detail?', {
          formDefinitionId: this.detail.formDefinitionId,
          formDataId: this.detail.formDataId
        })
        this.formData = formData.data.data
      } else {
        this.$message.warning(`查询档案详情失败：【${data.message}】`)
        this.$router.back()
      }
    },
    back() {
      this.$router.back()
    },
  }
}
</script>