<template>
  <metadata-file-view title="档案详情" :formDataId="detail.formDataId"
                      :formDefinitionId="detail.formDefinitionId"
                      refType="archive"
                      :form-data="formData">
    <el-button type="primary" @click="$router.back()">返回</el-button>
  </metadata-file-view>
</template>

<script>

/**
 * 档案车内档案详情
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
      const {data} = await this.$post('/archiveCarDetail/detail', {id: this.$route.query.id})
      if (data.success) {
        this.detail = data.data
        const formData = await this.$http.post('manage/formData/detail?', {
          formDefinitionId: this.detail.formDefinitionId,
          formDataId: this.detail.formDataId
        })
        this.formData = formData.data.data
      } else {
        this.$message.warning(`查询档案车详情失败：【${data.message}】`)
        this.$router.back()
      }
    }
  }
}
</script>