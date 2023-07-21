<template>
  <section>
    <nac-info title="待鉴定档案" back>
      <span>共{{ batchTaskQuantity }}份</span>
      <span style="padding-left: 10px;color: #67C23A">完成{{ finishQuantity }}份</span>
      <span style="padding-left: 10px;color: #F56C6C">剩余{{ batchTaskQuantity - finishQuantity }}份</span>
      <span style="padding-left: 10px;">第{{ parseInt(archiveIndex) + 1 }}份</span>
      <el-select v-model="selectType" @change="" style="padding-left: 10px;width: 160px">
        <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
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
                <el-tabs v-model="activeName1">
                  <el-tab-pane name="matchingKeyWord">
                    <label slot="label">匹配关键词</label>
                    <matching-key-word :form-definition-id="formDefinitionId" :form-data-id="formDataId"
                                       ref="keyWordTable" v-on:checkedKeyWord="checkedKeyWord"></matching-key-word>
                  </el-tab-pane>
                  <el-tab-pane name="appraisalHistory">
                    <label v-if="isSeeHisTory==='0'" slot="label">
                      <el-badge is-dot class="item">
                        鉴定历史
                      </el-badge>
                    </label>
                    <label v-else slot="label">
                      鉴定历史
                    </label>
                    <appraisal-history :form-definition-id="formDefinitionId" :form-data-id="formDataId"
                                       ref="history"></appraisal-history>
                  </el-tab-pane>
                </el-tabs>

              </el-card>
            </el-col>
            <el-col :span="12" style="margin-left: 2px">
              <el-card class="box-card" style="height: 50vh">
                <el-tabs v-model="activeName2">
                  <el-tab-pane name="appraisal">
                    <label slot="label">鉴定</label>
                    <do-appraisal
                        ref="doAppraisal"
                        v-on:reloadTask="loadTaskData"
                        v-on:next="loadTaskQuantity('next')"
                        v-on:setIsSeeHisTory="setIsSeeHisTory"
                    >
                    </do-appraisal>
                  </el-tab-pane>
                  <el-tab-pane label="关键词推荐" name="keyWordAdd">
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
import doAppraisal from '../doAppraisal';
import addKeyWordForm from '../addKeyWordForm'
import appraisalHistory from '../appraisalHistory'
import metaDataFileView from '../metaDataFileView'


export default {
  components: {archiveDataForm, matchingKeyWord, doAppraisal, addKeyWordForm, appraisalHistory, metaDataFileView},
  data() {
    return {
      activeName1: 'matchingKeyWord',
      activeName2: 'appraisal',
      formDefinitionId: this.$route.query.formDefinitionId,
      formDataId: this.$route.query.formDataId,
      loading: false,
      archiveData: {},
      archiveIndex: this.$route.query.index,
      batchTaskQuantity: '',
      finishQuantity: '',
      selectType: '',
      options: [{
        value: 'all',
        label: '全部'
      }, {
        value: 'notEqual',
        label: '与机器结果不一致'
      }, {
        value: 'equal',
        label: '与机器结果一致'
      }, {
        value: 'finish',
        label: '已鉴定'
      }, {
        value: 'notFinish',
        label: '未鉴定'
      }],
      isSeeHisTory: '0',
      keyWord: ''
    }
  },
  watch: {
    activeName1: function () {
      if (this.isSeeHisTory === '0') {
        this.$http.post('/archiveAppraisalTask/updateSeeHistory', {
          formDefinitionId: this.formDefinitionId,
          formDataId: this.formDataId,
          taskId: sessionStorage.getItem('taskId')
        })
            .then(({data}) => {
              if (data.success) {
                this.isSeeHisTory = '1'
              }
            })
      }
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
      this.$refs.keyWordTable.loadData(this.formDefinitionId, this.formDataId)
      this.$refs.doAppraisal.loadData(this.formDefinitionId, this.formDataId)
      this.$refs.history.loadData(this.formDataId, this.formDefinitionId, sessionStorage.getItem('taskId'))
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
      this.loadArchiveData(pageIndex)
    },
    loadTaskData(pageIndex) {
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
    loadArchiveData(pageIndex) {
      this.activeName1 = 'matchingKeyWord'
      this.$http.post("/archiveAppraisalTask/page", {
        taskId: sessionStorage.getItem('taskId'),
        batchId: sessionStorage.getItem('batchId'),
        pageSize: 1,
        pageIndex
      }).then(({data}) => {
        if (data.success) {
          if (data.data.data.length == 0) {
            this.$message.error("无数据")
            return
          }
          this.formDefinitionId = data.data.data[0].formDefinitionId
          this.formDataId = data.data.data[0].formDataId
          this.archiveIndex = pageIndex
          this.refreshArchiveData()
        }
      })
    },
    setIsSeeHisTory(v) {
      this.isSeeHisTory = v
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
