<template>
  <metadata-file-view :title="formData.ARCHIVE_CODE" :formDataId="$route.query.formDataId"
                      :formDefinitionId="$route.query.formDefinitionId" refType="archive"
                      group-code="default"
                      :form-data="formData"
                      @next="loadTaskQuantity('next')"
                      @reload="$init()"
                      :watermark="true"
                      :detailExcludes="detailExcludes">
    <span>共{{ batchTaskQuantity }}份</span>
    <span style="padding-left: 10px;color: #67C23A">完成{{ finishQuantity }}份</span>
    <span style="padding-left: 10px;color: #F56C6C">剩余{{ batchTaskQuantity - finishQuantity }}份</span>
    <span style="padding-left: 10px;">第{{ parseInt(archiveIndex) + 1 }}份</span>
    <el-button type="primary" @click="loadTaskQuantity('previous')" size="mini">上一份</el-button>
    <el-button type="primary" @click="loadTaskQuantity('next')" size="mini">下一份</el-button>
    <el-button type="primary" @click="goRecommendRecord()" size="mini">关键词推荐记录</el-button>
    <el-button type="primary" @click="$router.back()">返回</el-button>
    <tag-manage :form-data="formData" :form-definition-id="formDefinitionId" slot="detailTop"/>
  </metadata-file-view>
</template>
<script>

import matchingKeyWord from '../matchingKeyWord'
import tagManage from "./tagManage";

export default {
  //接收暂存库、管理库 查看档案目录详情页面
  data() {
    return {
      formData: {},
      activeName: 'first',
      formDefinitionId: this.$route.query.formDefinitionId,
      formDataId: this.$route.query.formDataId,
      loading: false,
      archiveData: {},
      archiveIndex: this.$route.query.index,
      loadPath: sessionStorage.getItem('taskId') ? 'archiveAppraisalTask' : 'archiveToBeAppraisal',
      batchTaskQuantity: '',
      keyWord: '',
      finishQuantity: '',
      detailExcludes: ['errorCorrection', 'tag', 'businessGuidance', 'testRecord']
    }
  },
  components: {matchingKeyWord, tagManage},
  computed: {
    isTask: function () {
      if (sessionStorage.getItem('taskId')) {
        return true
      }
      return false
    }
  },
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
      if (this.isTask) {
        this.$http.post("/appraisalBatchTask/detail", {id: sessionStorage.getItem('taskId')})
            .then(({data}) => {
              if (data.success) {
                this.batchTaskQuantity = data.data.batchTaskQuantity
              }
            })
      }
      this.loadTaskData()
    },
    loadTaskQuantity(type) {
      let pageIndex = this.archiveIndex
      if ('next' == type) {
        pageIndex = parseInt(this.archiveIndex) + 1
      } else {
        if (this.archiveIndex == 0) {
          this.$message.error("已是第一份，没有上一份")
          return
        } else {
          pageIndex = parseInt(this.archiveIndex) - 1
        }
      }
      this.$http.post("/" + this.loadPath + "/page", {
        taskId: sessionStorage.getItem('taskId'),
        pageSize: 1,
        pageIndex
      }).then(({data}) => {
        if (data.success) {
          if (data.data.data.length == 0) {
            this.$message.error("已是最后一份，没有下一份")
            return
          }
          this.formDefinitionId = data.data.data[0].formDefinitionId
          this.formDataId = data.data.data[0].formDataId
          this.archiveIndex = pageIndex
          this.$init()
        }
      })
    },
    goRecommendRecord() {
      this.$router.push({path: "/appraisal/recommendKeyWord"})
    },
    loadTaskData() {
      if (this.isTask) {
        this.$http.post("/appraisalBatchTask/detail", {id: sessionStorage.getItem('taskId')})
            .then(({data}) => {
              if (data.success) {
                this.batchTaskQuantity = data.data.batchTaskQuantity
                this.finishQuantity = data.data.finishQuantity
              }
            })
      }
    },
  }
}
</script>
