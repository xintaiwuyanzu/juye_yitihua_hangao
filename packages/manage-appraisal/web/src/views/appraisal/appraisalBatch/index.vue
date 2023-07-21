<template>
  <table-index :fields="fields"
               path="appraisalBatch"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               ref="table"
               style="height: 70vh;display: flex;flex-direction: column">
    <template v-slot:table-$btns="scope">
      <el-button type="text" width="160" v-show="scope.row.status==='0'" @click="start(scope)">开启</el-button>
      <!--      <el-button type="text" v-show="scope.row.status!='0'" @click="statistics(scope)">统计</el-button>-->
      <el-button type="text" @click="detail(scope)">详情</el-button>
      <el-button type="text" @click="toTask(scope)">任务</el-button>
      <!--      <el-button type="text"   @click="config(scope)">配置</el-button>-->
      <el-button type="text" v-show="scope.row.status!='2'" @click="release(scope)">删除</el-button>
      <el-button type="text" @click="toEnd(scope)">完结</el-button>
    </template>
  </table-index>
</template>

<script>
const result = {'kz': '控制', 'kf': '开放'}
export default {
  data() {
    return {
      fields: {
        batchName: {
          label: '名称',
          // component:'text',
          // route:true,
          // routerPath:'/appraisal/appraisalBatch/detail',
          queryProp: [],
          search: true,
          align: 'center',
          width: 100
        },
        appraisalType: {
          label: '类型', search: true, fieldType: 'select',
          width: 100,
          mapper: [
            {
              id: 'KFJD',
              label: '开放鉴定'
            },
            {
              id: 'DQJD',
              label: '销毁鉴定'
            }],
          align: 'center'
        },
        fondNames: {label: '包含全宗', search: false, edit: false, align: 'center'},
        archiveQuantity: {label: '档案数量', search: false, edit: false, align: 'center', width: 100},
        startVintages: {label: '起始年度', search: false, edit: false, align: 'center', width: 100},
        endVintages: {label: '截止年度', search: false, edit: false, align: 'center', width: 100},
        createDate: {label: '创建时间', search: false, dateFormat: 'YYYY-MM-DD hh:mm:ss', edit: false, width: 200},
        status: {
          label: '任务状态', search: false, edit: false, fieldType: 'select', width: 100, component: 'tag',
          mapper: [
            {
              id: '2',
              label: '已完成'
            },
            {
              id: '1',
              label: '进行中'
            },
            {
              id: '0',
              label: '未开始'
            }]
        }
      },
      defaultSearchForm: {}
    }
  },
  methods: {
    detail(scope) {
      sessionStorage.setItem('appraisalType', scope.row.appraisalType);
      sessionStorage.setItem('batchId', scope.row.id)
      this.$router.push({path: "/appraisal/appraisalBatch/detail", query: {batchId: scope.row.id}})
    },
    changeTaskType() {
      if (this.$refs.table.editFormModel.taskType == 'KFJD') {
        this.fields.wordGroupId.show = true
      } else {
        this.fields.wordGroupId.show = false
      }
    },
    release(scope) {
      this.$confirm('此操作将删除选中批次，并删除已经领取的鉴定任务，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/appraisalBatch/delete", {id: scope.row.id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("释放成功")
                this.$refs.table.loadData(this.defaultSearchForm)
              }
            })
      })
    },
    config(scope) {
      this.$router.push({path: "/appraisal/appraisalBatch/config", query: {batchId: scope.row.id}})
    },
    toTask(scope) {
      sessionStorage.setItem('appraisalType', scope.row.appraisalType);
      sessionStorage.setItem('batchId', scope.row.id)
      this.$router.push({path: "/appraisal/appraisalTask/task", query: {batchId: scope.row.id, createPerson: false}})
    },
    start(scope) {
      let batchFrom = scope.row
      batchFrom.status = 1
      this.$http.post("/appraisalBatch/update", batchFrom)
          .then(({data}) => {
            if (data.success) {
              this.$message.success("开启成功")
              this.detail(scope)
            }
          })
    },
    isWks(row) {
      if ("-1" == row.status.toString()) {
        return true
      }
      return false
    },
    statistics(scope) {
      this.$router.push({
        path: "/appraisal/appraisalBatch/statistics",
        query: {batchId: scope.row.id, type: scope.row.appraisalType}
      })
    },
    toEnd(scope) {
      this.$http.post("/appraisalBatch/endBatch", {id: scope.row.id})
          .then(({data}) => {
            if (data.success) {

            } else {
              this.$message.error(data.message)
            }

          })
    }
  }
}
</script>
