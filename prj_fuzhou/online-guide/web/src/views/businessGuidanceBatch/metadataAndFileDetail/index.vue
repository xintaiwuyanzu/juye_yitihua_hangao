<template>
  <metadata-file-view :title="$route.query.title" :formDataId="formDataId"
                      :formDefinitionId="formDefinitionId" :refType="$route.query.refType"
                      :group-code="$route.query.groupCode"
                      :form-data="formData"
                      @next="loadTaskQuantity('next')"
                      :watermark="$route.query.watermark">
<!--    <span >共{{ batchTaskQuantity }}份</span>
    <span  style="padding-left: 10px;">第{{ parseInt(archiveIndex) + 1 }}份</span>
    <el-button  type="primary" @click="loadTaskQuantity('previous')" size="mini">上一份</el-button>
    <el-button  type="primary" @click="loadTaskQuantity('next')" size="mini">下一份</el-button>-->
    <el-button type="primary" @click="$router.back()">返回</el-button>
    <tag-manage :form-data="formData" :showStatus="showStatus" :gid="gid" :form-definition-id="formDefinitionId" slot="detailTop"></tag-manage>
  </metadata-file-view>
</template>
<script>
import tagManage from "./tagManage";

export default {
  //接收暂存库、管理库 查看档案目录详情页面
  data() {
    return {
      formData: {},
      batchTaskQuantity: '',
      archiveIndex: this.$route.query.index,
      formDefinitionId: this.$route.query.formDefinitionId,
      formDataId: this.$route.query.formDataId,
      gid:this.$route.query.gid,
      showStatus:this.$route.query.showStatus,
    }
  },
  components:{tagManage},
  methods: {
    /**
     * 初始化加载表单数据
     * @returns {Promise<void>}
     */
    async $init() {
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.formDefinitionId,
        formDataId: this.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
      this.$http.post("/businessGuidanceBatchDetail/getBatchTaskQuantity", {batchId: this.$route.query.batchId})
          .then(({data}) => {
            if (data.success) {
              this.batchTaskQuantity = data.data
            }
          })
    },
    loadTaskQuantity(type){
      let pageIndex = this.archiveIndex
      if('next'==type){
        pageIndex = parseInt(this.archiveIndex)+1
      }else{
        if(this.archiveIndex==0){
          this.$message.error("已是第一份，没有上一份")
          return
        }else{
          pageIndex = parseInt(this.archiveIndex)-1
        }
      }
      this.$http.post("/businessGuidanceBatchDetail/page",{
        batchId:this.$route.query.batchId,
        pageSize:1,
        pageIndex
      }).then(({data})=>{
        if(data.success){
          if(data.data.data.length==0){
            this.$message.error("已是最后一份，没有下一份")
            return
          }
          this.formDefinitionId = data.data.data[0].formDefinitionId
          this.formDataId = data.data.data[0].formDataId
          this.archiveIndex=pageIndex
          this.$init()
        }
      })

    }
  }
}
</script>
