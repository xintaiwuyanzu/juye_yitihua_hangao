<template>
  <metadata-file-view title="归档档案详情" :formDataId="detail.formDataId"
                      :formDefinitionId="detail.formDefinitionId"
                      :form-data="formData" refType = "archive">
  </metadata-file-view>
</template>
<script>
/**
 * 接收档案详情信息
 */
export default {
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
      const {data} = await this.$post('/receive/onlineDetail/detail', {id: this.$route.query.id})
      if (data.success) {
        this.detail = data.data
        const formData = await this.$http.post('manage/formData/detail?', {
          formDefinitionId: this.detail.formDefinitionId,
          formDataId: this.detail.formDataId
        })
        this.formData = formData.data.data
      } else {
        this.$message.warning(`查询归档详情失败：【${data.message}】`)
        this.$router.back()
      }
    }
  }

}
</script>