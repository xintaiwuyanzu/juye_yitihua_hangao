<template>
  <section>
    <nac-info back title="查看审核">
      <el-tag>
        总数：{{ batchCount.total }}
      </el-tag>
      <el-tag type="success">
        通过：{{ batchCount.success }}
      </el-tag>
      <el-tag type="danger">
        未通过：{{ batchCount.fail }}
      </el-tag>
      <el-tag type="warning">
        未办理：{{ batchCount.undo }}
      </el-tag>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-card>
        <div slot="header" class="clearfix">
          <strong>档案基本信息</strong>
        </div>
        <archive-form :form-definition-id="formDefinitionId" :form="form"/>
      </el-card>
      <el-card>
        <div slot="header" class="clearfix">
          <strong>档案原文信息</strong>
        </div>
        <files-list refType="archive" :ref-id="formDataId" :deleter="false" :transform="false"/>
      </el-card>
      <el-card>
        <div slot="header" class="clearfix">
          <strong>审核信息</strong>
        </div>
        <el-row :gutter="24" style="align-content: center">
          <el-col :span="24">审核意见：
            <el-input v-model="detail.advice" :disabled="this.detail.status!=='0'" type="textarea" :rows="3" style="width: 80%"
                      placeholder="请输入审核意见"
                      clearable/>
          </el-col>
        </el-row>
        <el-row :gutter="24" style="align-content: center" v-if="this.detail.status==='0'">
          <el-col :span="24">
             <span style="float: right;margin-top: 15px;margin-right: 15px">
               <el-button type="success" size="mini" @click="onSubmit('1')" v-loading="loading">通过</el-button>
               <el-button type="danger" size="mini" @click="onSubmit('-1')" v-loading="loading">不通过</el-button>
            </span>
          </el-col>
        </el-row>
      </el-card>
    </div>
    <el-dialog width="50%" title="提醒" :visible.sync="dialogShow" append-to-body :show-close="false"
               :close-on-click-modal="false">
      <el-card>
        <div slot="header" class="clearfix">
          <strong>接收人:</strong>
        </div>
        <el-row :gutter="24" style="align-content: center">
          <el-col :span="24">
            <el-select placeholder="请选择接收人" v-model="targetPerson">
              <el-option v-for="person in persons" :key="person.id" :label="person.userName"
                         :value="person.id"/>
            </el-select>
          </el-col>
        </el-row>
        <el-row :gutter="24" style="align-content: center">
          <el-col :span="24">
             <span style="float: right;margin-top: 15px;margin-right: 15px">
               <el-button type="success" size="mini" @click="onFinish()">办结</el-button>
               <el-button type="primary" size="mini" @click="doSend()">发送</el-button>
            </span>
          </el-col>
        </el-row>
      </el-card>
    </el-dialog>
  </section>
</template>
<script>
import archiveForm from "../../../../../../lib/archiveForm/index";
import filesList from "@/components/fileList/content"

/**
 * 审核页面
 */
export default {
  data() {
    return {
      batchId: this.$route.query.batchId,
      loading: false,
      //统计信息
      batchCount: {
        //总数
        total: 0,
        //成功
        success: 0,
        //失败
        fail: 0,
        //undo
        undo: 0
      },
      detail: {},
      form: {},
      dialogShow: false,
      targetPerson: '',
      persons: []
    }
  },
  computed: {
    formDefinitionId() {
      return this.detail.formDefinitionId
    },
    formDataId() {
      return this.detail.formDataId
    }
  },
  watch: {
    formDataId() {
      this.loadForm()
    },
    detail() {
      this.loadCount()
    },
    async 'batchCount'() {
      if (this.batchCount.undo === 0) {
        if (this.detail.status === '0' || this.$route.query.taskStatus === '0') {
          this.dialogShow = true
          if (this.persons.length === 0) {
            const {data} = await this.$post('/person/page', {page: false})
            this.persons = data.data
          }
        }
      }
    }
  },
  methods: {
    /**
     * 加载统计数据
     * @returns {Promise<void>}
     */
    async loadCount() {
      const {data} = await this.$post('/batch/batchCount', {batchId: this.batchId, type: 'SEND_CHECK'})
      this.batchCount = data.data
    },
    async loadForm() {
      if (this.formDataId && this.formDefinitionId) {
        const {data} = await this.$post('/manage/formData/detail', {
          formDefinitionId: this.formDefinitionId,
          formDataId: this.formDataId
        })
        this.form = data.data
      }
    },
    /**
     * 加载
     * @returns {Promise<void>}
     */
    async loadDetail(id) {
      const {data} = await this.$post('/batch/batchDetail', {id: id, type: 'SEND_CHECK'})
      this.detail = data.data
    },
    $init() {
      this.loadCount()
      this.loadDetail(this.$route.query.id)
    },
    async onSubmit(status) {
      this.loading = true
      const {data} = await this.$http.post("/batch/changeStatus", {
        type: 'SEND_CHECK',
        detailId: this.detail.id,
        status: status,
        advice: this.detail.advice
      })
      if (data.success) {
        this.$message.success('审核成功')
        this.status = data.data.status
        this.detail.advice = data.data.advice
      } else {
        this.$message.error(data.message)
      }
      await this.loadCount()
      await this.loadDetail(this.$route.query.id)
      this.loading = false
      await this.loadPending()
    },
    /**
     * 加载未处理档案
     * @returns {Promise<void>}
     */
    async loadPending() {
      this.loading = true
      const {data} = await this.$post('/batch/batchDetailPage', {
        batchType: 'SEND_CHECK',
        id: this.detail.batchId,
        status: 0
      })
      if (data.success && data.data.total > 0) {
        await this.loadDetail(data.data.data[0].id)
      }
      this.loading = false
    },
    /**
     * 办结 批次为已办结，任务为已办结
     * @returns {Promise<void>}
     */
    async onFinish() {
      this.loading = true
      const {data} = await this.$post('/batch/doFinish', {
        batchId: this.detail.batchId,
        status: 1,
        finish: true
      })
      if (data.success && data.data.total > 0) {
        await this.loadDetail(data.data.data[0].id)
      }
      this.loading = false
      this.dialogShow = false
    },
    /**
     *
     * 执行发送任务
     * @returns {Promise<void>}
     */
    async doSend() {
      this.loading = true
      const {data} = await this.$post('/archiveTask/sendTask', {
        targetPersonId: this.targetPerson,
        taskId: this.$route.query.taskId
      })
      if (data.success) {
        this.$message.success('提交成功')
      } else {
        this.$message.error(data.message)
      }
      this.loading = false
      this.dialogShow = false
    }
  },
  components: {filesList, archiveForm}
}
</script>
<style lang="scss" scoped>
.el-card {
  overflow: unset
}
</style>