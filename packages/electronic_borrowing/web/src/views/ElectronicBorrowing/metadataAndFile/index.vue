<template>
  <section>
    <nac-info back title="原文预览"/>
    <metadata-file-view :title="$route.query.title" :formDataId="$route.query.formDataId"
                        :formDefinitionId="$route.query.formDefinitionId" :refType="$route.query.refType"
                        :group-code="$route.query.groupCode"
                        :form-data="formData"
                        :watermarkStatus="watermarkO"
                        :printStatus="print"
                        :downloadStatus="downloadState"
                        :defaultShowFormData="true"
                        :defaultShowTabs="false"
                        @getCurr="getCurr"
                        ref="metadataFileView" :showHeader="false" style="height: 100%;">
    </metadata-file-view>

  </section>

</template>
<script>
export default {
  //接收暂存库、管理库 查看档案目录详情页面
  data() {
    return {
      rules: {},
      formData: {},
      currentNode:'123',
      aaa:'aaa',
      fields: [],
      //转换成boolean
      watermarkO:Boolean(this.$route.query.watermark===undefined?"0":this.$route.query.watermark),
      print:false,
      downloadState:false,
    }
  },
  methods: {
    /**
     * 初始化加载表单数据
     * @returns {Promise<void>}
     */
    async $init() {

      this.print=JSON.parse(this.$route.query.printStatus===undefined?false:this.$route.query.printStatus)
      this.downloadState=JSON.parse(this.$route.query.downloadState==undefined?false:this.$route.query.printStatus)


      // this.print=JSON.parse(this.$route.query.printStatus)
      // this.downloadState=JSON.parse(this.$route.query.downloadState)
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
    },
    getCurr(param){
      this.currentNode=param
    },
    changeField(item) {
      item.oldValue = this.formData[item.field] ? this.formData[item.field] : '暂无数据'
      item.fieldName = this.fields.find(v => v.key === item.field).label
    },
  }
}
</script>