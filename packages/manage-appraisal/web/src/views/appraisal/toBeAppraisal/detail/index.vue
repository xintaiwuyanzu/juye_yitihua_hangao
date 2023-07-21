<template>
  <section>
    <nac-info title="待鉴定档案" back>
      <span v-if="isTask">共{{ batchTaskQuantity }}份</span>
      <span style="padding-left: 10px;">第{{ parseInt(archiveIndex) + 1 }}份</span>
      <el-button type="primary" @click="loadTaskQuantity('previous')" size="mini">上一份</el-button>
      <el-button type="primary" @click="loadTaskQuantity('next')" size="mini">下一份</el-button>
      <el-button type="primary" @click="goRecommendRecord()" size="mini">关键词推荐记录</el-button>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-row class="container" type="flex" justify="space-between">
        <el-col :span="12">
          <el-card class="box-card" style="height: 79vh">
            <div slot="header">
              <span>原文</span>
              <el-link v-if="keyWord.length>0" style="float: right;margin-left: 10px;color: #409EFF" size="mini"
                       type="text">下一处
              </el-link>
              <el-link v-if="keyWord.length>0" style="float: right;margin-left: 10px;color: #409EFF;" size="mini"
                       type="text">上一处
              </el-link>
              <span v-if="keyWord.length>0" style="float: right">当前匹配关键词：{{ keyWord }}</span>
            </div>
            <meta-data-file-view :form-definition-id="formDefinitionId" :form-data-id="formDataId" ref="archiveFile"
                                 ref-type="archive"></meta-data-file-view>
          </el-card>
        </el-col>
        <el-col :span="12" style="margin-left: 2px">
          <el-card class="box-card" style="height: 28vh">
            <div slot="header">
              <span>基本信息元数据</span>
            </div>
            <archive-data-form :form-data-id="formDataId" :form-definition-id="formDefinitionId"
                               :form-data="archiveData" ref="archiveForm"></archive-data-form>
          </el-card>
          <el-row type="flex" style="margin-top: 2px">
            <el-col :span="12">
              <el-card class="box-card" style="height: 50vh">
                <div slot="header">
                  <span>匹配关键词</span>
                </div>
                <matching-key-word :form-data-id="formDataId" :form-definition-id="formDefinitionId" ref="keyWordTable"
                                   v-on:checkedKeyWord="checkedKeyWord"></matching-key-word>
              </el-card>
            </el-col>
            <el-col :span="12" style="margin-left: 2px">
              <el-card class="box-card" style="height: 50vh">
                <el-tabs v-model="activeName">
                  <el-tab-pane name="first">
                    <label slot="label">鉴定历史</label>
                    <appraisal-history :form-data-id="formDataId" :form-definition-id="formDefinitionId"
                                       ref="history"></appraisal-history>
                  </el-tab-pane>
                  <el-tab-pane label="关键词推荐" name="second">
                    <add-key-word-form ref="addKeyWord"></add-key-word-form>
                  </el-tab-pane>
                </el-tabs>
              </el-card>
            </el-col>
          </el-row>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
<script>

import archiveDataForm from '../archiveDataForm';
import matchingKeyWord from '../matchingKeyWord'
import addKeyWordForm from '../addKeyWordForm'
import appraisalHistory from '../appraisalHistory'
import metaDataFileView from '../metaDataFileView'

export default {
  components: {archiveDataForm, matchingKeyWord, addKeyWordForm, appraisalHistory, metaDataFileView},
  data() {
    return {
      activeName: 'first',
      formDefinitionId: this.$route.query.formDefinitionId,
      formDataId: this.$route.query.formDataId,
      loading: false,
      archiveData: {},
      archiveIndex: this.$route.query.index,
      loadPath: sessionStorage.getItem('taskId') ? 'archiveAppraisalTask' : 'archiveToBeAppraisal',
      batchTaskQuantity: '',
      keyWord: ''
    }
  },
  computed: {
    isTask: function () {
      if (sessionStorage.getItem('taskId')) {
        return true
      }
      return false
    }
  },
  methods: {
    $init() {
      this.refreshArchiveData()
      this.loadTaskData()
    },
    refreshArchiveData() {
      this.$http.post("/manage/formData/detail", {formDefinitionId: this.formDefinitionId, formDataId: this.formDataId})
          .then(({data}) => {
            this.archiveData = data.data
          })
      this.$refs.archiveFile.left = 8
      this.$refs.archiveFile.right = 16
      this.$refs.addKeyWord.formDataId = this.formDataId
      this.$refs.addKeyWord.formDefinitionId = this.formDefinitionId
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
        batchId: sessionStorage.getItem('batchId'),
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
          this.refreshArchiveData()
        }
      })
    },
    loadTaskData() {
      if (this.isTask) {
        this.$http.post("/appraisalBatchTask/detail", {id: sessionStorage.getItem('taskId')})
            .then(({data}) => {
              if (data.success) {
                this.batchTaskQuantity = data.data.batchTaskQuantity
              }
            })
      }
    },
    checkedKeyWord(row) {
      this.keyWord = row.keyWord
    },
    goRecommendRecord() {
      this.$router.push({path: "/appraisal/recommendKeyWord", query: {}})
    }
  }
}
</script>

<style>
.el-card__body {
  padding-top: 6px;
}

.el-tabs__item {
  font-size: 1em;
}
</style>
