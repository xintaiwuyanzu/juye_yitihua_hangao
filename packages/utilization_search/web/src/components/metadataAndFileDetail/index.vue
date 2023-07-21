<template>
  <metadata-file-view :formDataId="query.formDataId"
                      :formDefinitionId="query.formDefinitionId" :refType="query.refType"
                      :group-code="query.groupCode"
                      :form-data="formData"
                      :watermark="query.watermark" :back="false" style="height: 900px" :showHeader="showHeader">
    <add-archive-car ref="archive" :form-definition-id="query.formDefinitionId" :form-data-id="query.formDataId"/>
  </metadata-file-view>
</template>
<script>
import AddArchiveCar from "../../components/addArchiveCar";


export default {
  //接收暂存库、管理库 查看档案目录详情页面
  components: {AddArchiveCar},
  data() {
    return {
      formData: {},
      showHeader:false
    }
  },
  props:{
    query:{type:Object}
  },
  methods: {
    back() {
      // this.$refs.archive.change()
      this.$router.back()
    },
    /**
     * 初始化加载表单数据
     * @returns {Promise<void>}
     */
    async $init() {
      console.log(this.query)
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.query.formDefinitionId,
        formDataId: this.query.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
    }
  }
}
</script>