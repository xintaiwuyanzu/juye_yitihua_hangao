<template>
  <el-card header="审核意见" shadow="hover" v-loading="loading">
    <form-render :model="auditForm" :fields="auditFormField" label-width="110px" v-show="status!='1'">
      <el-form-item>
        <el-button type="primary" @click="save()">保 存</el-button>
      </el-form-item>
    </form-render>
    <table-render :columns="columns" :data="data" max-height="160px" height="auto" v-if="data.length>0">
      <el-table-column type="index" width="50" label="序号" align="center" before="createPersonName" prop="index"/>
    </table-render>
  </el-card>
</template>
<script>
/**
 * 查档审批表单
 */
export default {
  name: "auditForm",
  props: {
    /**
     * 查档详情Id
     */
    detailId: {type: String},
    /**
     * 批次Id
     */
    batchId: {type: String},

    status: {type: String},

    /**
     * 环节Id
     */
    taskId: {type: String}
  },
  watch: {
    detailId(n) {
      if (n) {
        //详情Id修改了，则审批意见也跟着修改
        this.loadAudit()
      }
    }
  },
  data() {
    return {
      loading: false,
      //审核表单数据
      auditForm: {useFile: 'true', comment: '同意'},
      /**
       * 审核表单字段
       */
      auditFormField: {
        useFile: {label: '能否查看原文', fieldType: 'radio', mapper: {true: '是', false: '否'}, required: true},
        comment: {label: '审核意见', required: true, type: 'textarea', clearable: true}
      },
      columns: {
        createPersonName: {label: '审核人员', width: 100},
        comment: {label: '审核意见'},
        useFile: {label: '能否查看原文', fieldType: 'radio', mapper: {true: '是', false: '否'}},
        createDate: {label: '审核日期', dateFormat: 'YYYY-MM-DD hh:mm:ss', width: 140}
      },
      data: []
    }
  },
  methods: {
    async save() {
      await this.$post('/batchDetailComment/insert',
          {
            taskId: this.taskId,
            batchId: this.batchId,
            detailId: this.detailId,
            ...this.auditForm
          }
      )
      //await this.loadAudit()
      this.$emit('next')
      this.loadAudit()
    },
    //加载审批意见
    async loadAudit() {
      //加载指定批次，详情，和环节的审批意见
      const {data} = await this.$post('/batchDetailComment/page', {
        page: false,
        batchId: this.batchId,
        detailId: this.detailId
      })
      this.data = data.data
    },
    $init() {
      if (this.detailId && this.batchId) {
        this.loadAudit()
      }
    }
  }
}
</script>