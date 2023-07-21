<template>
  <section class="hand-over-detail">
    <section class="breadcrumb-container">
      <el-radio-group v-model="active">
        <el-radio-button label="detail">{{ batchName }}详情</el-radio-button>
        <el-radio-button label="juan" v-if="form.batchType==='handOver'">全宗卷</el-radio-button>
        <el-radio-button label="archive">档案详情</el-radio-button>
        <el-radio-button label="history" v-if="processInstanceId">流转历史</el-radio-button>
      </el-radio-group>
      <section class="slot">
        <!--环节按钮-->
        <task-buttons/>
        <!--跳转编辑全宗按钮-->
        <el-button type="primary" v-if="showJuan" @click="$router.push('/archive/management')">编辑全宗卷</el-button>
        <!--启动流程按钮-->
        <process-container v-if="isProcess"
                           :process-type="processType"
                           :business-id="handoverId"
                           ref="process"
                           @saved="processStart">
          <el-button type="primary" :loading="loading" @click="saveAndStart" v-if="isEdit">保存并提交申请
          </el-button>
          <section style="display: inline-block" v-else>
            <el-button type="primary" :loading="loading" @click="updateDag('30')" v-show="isDag==='dag'">接收并审核
            </el-button>
            <el-button type="primary" :loading="loading" @click="updateDag('31')" v-show="isDag==='dag'">退回
            </el-button>
          </section>
        </process-container>
        <!--保存按钮-->
        <el-button type="primary" v-if="isEdit" :loading="loading" @click="update">保存</el-button>
        <el-button type="primary" @click="$router.back()">返回</el-button>
      </section>
    </section>
    <div v-loading="loading" class="index_main">
      <form-render :model="form"
                   :fields="fields"
                   ref="form"
                   :inline="true"
                   label-width="120px"
                   v-if="active==='detail'"
                   :disabled="!isEdit">
      </form-render>
      <manage-list v-if="active==='juan'" :batch-id="handoverId"/>
      <detail-list v-if="active==='archive'" :handover-id="handoverId"/>
      <task-history-container :process-instance-id="processInstanceId" v-if="active==='history'"/>
    </div>
  </section>
</template>
<script>
import {processContainer, taskHistoryContainer} from '@dr/process/src/lib'
import DetailList from "./detailList";
import ManageList from "./manageList";
import TaskButtons from "./taskButtons";

/**
 * 移交批次详情列表
 */
export default {
  name: "editIndex",
  components: {TaskButtons, ManageList, DetailList, processContainer, taskHistoryContainer},
  data() {
    return {
      /*processInstanceId: this.$route.query.processInstanceId,
      handoverId: this.$route.query.id || this.$route.query.businessId,*/
      active: 'detail',
      loading: false,
      form: {},
      isDag: false
    }
  },
  computed: {
    /**
     * 流程实例Id
     */
    processInstanceId() {
      return this.$route.query.processInstanceId
    },
    /**
     * 移交批次Id
     */
    handoverId() {
      return this.$route.query.id || this.$route.query.businessId
    },
    fields() {
      const batchName = this.batchName
      return {
        transfer: {
          fieldType: 'object',
          header: `${batchName}信息`,
          children: {
            transferPersonName: {label: `${batchName}人`, disabled: true},
            sourceOrgName: {label: `${batchName}单位`, disabled: true},
            detailNum: {label: `${batchName}档案数量`, newLine: true, disabled: true},
            tarOrgName: {label: '接收单位', disabled: true},
            delayDay: {
              newLine: true,
              show: !this.isHandOver,
              label: '延期目标时间',
              fieldType: 'date',
              required: true
            },
            sequence: {
              label: '载体起止顺序号',
              newLine: true,
              type: 'textarea',
              autosize: {minRows: 4, maxRows: 8},
              singleLine: true
            },
            other: {
              label: '备注',
              type: 'textarea',
              singleLine: true
            }
          }
        },
      }
    },
    batchName() {
      return this.isHandOver ? '移交' : '延期'
    },
    /**
     * 判断是否移交
     */
    isHandOver() {
      return this.form.batchType ? this.form.batchType === 'handOver' : true
    },
    /**
     * 判断是否编辑状态
     * @return {boolean}
     */
    isEdit() {
      return this.form.status === '10' && this.active === 'detail'
    },
    isDagReceive() {
      return this.form.status === '25'
    },
    /**
     * 是否显示流程按钮
     */
    isProcess() {
      return (this.isEdit || this.form.status === '25') && !this.processInstanceId
    },
    /**
     * 是否显示编辑全宗卷功能
     */
    showJuan() {
      return this.isEdit && this.active === 'juan'
    },
    /**
     * 启动的流程类型
     */
    processType() {
      const isGuan = this.form.status === '25'
      if (isGuan) {
        return this.isHandOver ? 'handOver_dag' : 'delay_dag'
      } else {
        return this.isHandOver ? 'handOver_das' : 'delay_das'
      }
    }
  },
  methods: {
    /**
     * 初始化加载数据
     * @return {Promise<void>}
     */
    async $init() {
      await this.getOrganiseType()
      await this.getDetail()
    },
    async getOrganiseType() {
      const {data} = await this.$post('/manage/handover/organiseType')
      if (data.success && data.data) {
        this.isDag = data.data.organiseType
      }
    },
    async getDetail() {
      this.loading = true
      const {data} = await this.$post('/manage/handover/detail', {id: this.handoverId})
      if (data.success && data.data) {
        this.form = data.data
        if (!this.isHandOver && !this.form.delayDay) {
          this.form.delayDay = new Date().getTime()
        }
      } else {
        this.$message.warning('未查询到指定的移交信息')
        this.$router.back()
      }
      this.loading = false
    },
    /**
     * 保存数据
     * @return {Promise<boolean>}
     */
    async update() {
      if (this.$refs.form) {
        this.loading = true
        const data = await this.$refs.form.submit('/manage/handover/update')
        this.loading = false
        if (data.success) {
          this.form = data.data
          this.$message.success('保存成功')
        } else {
          this.$message.warning(data.message)
          return false
        }
      }
    },
    /**
     * 档案馆更新状态
     * @return {Promise<void>}
     */
    async updateDag(status) {
      this.loading = true
      const data = await this.$refs.form.submit('/manage/handover/updateDag', {status})
      this.loading = false
      if (data.success) {
        this.form = data.data
        this.$message.success('保存成功')
        if (status === '30') {
          await this.$refs.process.open()
        }
      }
    },
    /**
     * 保存并提交审批
     * @return {Promise<void>}
     */
    async saveAndStart() {
      const result = await this.update()
      if (result !== false) {
        await this.$refs.process.open()
      }
    },
    /**
     * 流程启动成功回调
     * @return {Promise<void>}
     */
    async processStart() {
      this.$message.success('提交成功！')
      this.$router.back()
    }
  }
}
</script>
<style lang="scss">
.hand-over-detail {
  .handOverDetails {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: auto;

    .table-wrapper {
      padding: 0px;
    }
  }

}
</style>