<template>
  <metadata-file-view :title="$route.query.title" :formDataId="$route.query.formDataId"
                      :formDefinitionId="$route.query.formDefinitionId" :refType="$route.query.refType"
                      :group-code="$route.query.groupCode"
                      :form-data="formData"
                      :watermark="$route.query.watermark" :back="false">
    <add-archive-car ref="archive" :form-definition-id="$route.query.formDefinitionId" :form-data-id="$route.query.formDataId"/>
    <el-button type="primary" @click="back()">返回</el-button>
  </metadata-file-view>
</template>
<script>
import AddArchiveCar from "../../../components/addArchiveCar";

export default {
  //接收暂存库、管理库 查看档案目录详情页面
  components: {AddArchiveCar},
  data() {
    return {
      formData: {}
    }
  },
  methods: {
    back() {
      this.$refs.archive.change()
      this.$router.back()
    },
    /**
     * 初始化加载表单数据
     * @returns {Promise<void>}
     */
    async $init() {
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
    }
  }
}
</script>
<style lang="scss">
  .table_index .table_index_container .table-wrapper{
    height: 96% !important;
  }
</style>