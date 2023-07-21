<template>
  <table-index :fields="fields"
               path="archiveAppraisalTask"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               :checkAble="true"
               back
               ref="table"
               style="height:80vh;display: flex;flex-direction: column">
    <el-table-column prop="status" label="是否鉴定" width="100" align="center">
      <template v-slot="scope">
        <el-tag v-if="scope.row.status==1">鉴定中</el-tag>
        <el-tag v-if="scope.row.status==2" type="success">已鉴定</el-tag>
        <el-tag v-if="scope.row.status==0" type="danger">未鉴定</el-tag>
      </template>
    </el-table-column>
    <template v-slot:search-$btns="scope">
      <task-container :task-instance-id="taskId"
                      :business-id="$route.query.businessId"
                      style="display: inline-flex">
        <template v-slot:sendNext="params">
          <send-next :before-open="beforeOpenDialogOpen" @sendSaved="sendSaved">
          </send-next>
        </template>
        <template v-slot:endProcess="params">
          <end-process :before-open="beforeOpenDialogOpen" @saveEnd="saveEnd">
          </end-process>
        </template>
      </task-container>
      <el-dropdown trigger="click"
                   @command="fastAppraisal">
        <el-button type="primary">
          一键鉴定<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item command="select">鉴定选中</el-dropdown-item>
          <el-dropdown-item command="search">鉴定查询</el-dropdown-item>
          <el-dropdown-item command="all">鉴定全部</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="goAppraisal(scope)" type="text" width="20">鉴定</el-button>
    </template>
    <fast-examination ref="fastExamination" v-on:saveForm="confirmExamination"></fast-examination>
  </table-index>
</template>
<script>
import {endProcess, sendNext, taskContainer} from '@dr/process/src/lib'
import fastExamination from "./fastExamination";

export default {
  components: {taskContainer, sendNext, endProcess, fastExamination},
  data() {
    return {
      fields: {
        fondCode: {label: '全宗编码', search: false, edit: false, align: 'center', width: 100},
        categoryCode: {label: '门类编码', search: false, edit: false, align: 'center', width: 100},
        archiveCode: {label: '档号', search: true, edit: false, align: 'center', width: 200},
        title: {label: '题名', search: true, edit: false, align: 'center'},
        vintages: {label: '年份', search: true, edit: false, align: 'center', width: 100},
        filetime: {label: '文件时间', search: false, edit: false, align: 'center', width: 100},
        status: {
          label: '是否鉴定', search: true, edit: false, align: 'center', fieldType: 'select',
          mapper: [{
            id: '1',
            label: '已鉴定'
          }, {
            id: '0',
            label: '未鉴定'
          }],
          width: 100
        },
        sign: {
          label: '是否标记', search: true, edit: false, align: 'center', fieldType: 'select',
          mapper: [{
            id: '1',
            label: '标记'
          }, {
            id: '0',
            label: '未标记'
          }],
          width: 100
        }
      },
      defaultSearchForm: {
        taskId: this.$route.query.businessId
      },
      defaultInsertForm: {},
      taskId: '',
      businessId: '',
      fastAppraisalCommand: '',
      taskData: {}
    }
  },
  methods: {
    $init() {
      this.taskId = this.$route.query.taskId
      this.businessId = this.$route.query.businessId
      this.defaultSearchForm.taskId = this.$route.query.businessId
      this.getBatchTaskDetail()
    },
    search() {
      this.$refs.search.show = true
    },
    getBatchTaskDetail() {
      this.$http.post("/appraisalBatchTask/detail", {id: this.$route.query.businessId})
          .then(({data}) => {
            if (data.success) {
              this.taskData = data.data
            }
          })
    },
    resLoadSearch(param) {
      this.defaultSearchForm = param
      this.$refs.table.loadData(this.defaultSearchForm)
    },
    goAppraisal(scope) {
      sessionStorage.setItem("taskId", this.$route.query.businessId)
      this.$router.push({
        path: '/appraisal/toBeAppraisal/newAppraisal', query: {
          formDefinitionId: scope.row.formDefinitionId,
          formDataId: scope.row.formDataId,
          index: scope.$index + (parseInt(this.$refs.table.data.page.index) - 1) * parseInt(this.$refs.table.data.page.size)
        }
      })
    },
    submitBatchTask() {
      this.$http.post("/archiveAppraisalTask/checkSubmit", {taskId: this.$route.query.taskId})
          .then(({data}) => {
            if (data.success) {
              if (data.data == 0) {
                this.$refs.pro.open()
              } else {
                this.$message.error("有未鉴定档案，请鉴定之后再进行操作")
              }
            } else {
              this.$message.error(data.message)
            }
          })
    },
    beforeSendDialogOpen() {

    },
    async beforeOpenDialogOpen() {
      const {data} = await this.$http.post("/archiveAppraisalTask/checkSubmit", {taskId: this.$route.query.businessId})
      if (data.success && data.data === 0) {
        return true
      } else {
        this.$message.error("有未审核档案信息，请处理之后重试")
        return false
      }
    },
    sendSaved() {
      this.$message.success('发送成功！')
      this.$router.back()
    },
    saveEnd() {
      this.$message.success('办结成功！')
      this.$router.back()
    },
    fastAppraisal(command) {
      this.fastAppraisalCommand = command
      if (command == 'select') {
        let ids = []
        this.$refs.table.tableSelection.forEach(v => {
          ids.push(v.id)
        })
        if (ids.length === 0) {
          this.$message.error("无选中档案,请先勾选要鉴定档案!")
          return
        }
      }
      this.$refs.fastExamination.appraisalType = this.taskData.appraisalType
      this.$refs.fastExamination.show = true
    },
    confirmExamination(auxiliaryResult) {
      if (this.fastAppraisalCommand == 'all') {
        this.$http.post("/archiveAppraisalTask/fastAppraisal", {taskId: this.businessId, auxiliaryResult})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("一键鉴定" + data.data + "份档案。")
                this.key++
                this.$refs.table.loadData()
              } else {
                this.$message.error(data.message)
              }
            })
      }
      if (this.fastAppraisalCommand == 'select') {
        const ids = []
        this.$refs.table.tableSelection.forEach(v => {
          ids.push(v.id)
        })
        this.$http.post("/archiveAppraisalTask/fastAppraisalByIds", {
          taskId: this.businessId,
          auxiliaryResult,
          ids: ids.join(',')
        })
            .then(({data}) => {
              if (data.success) {
                this.$message.success("一键鉴定" + data.data + "份档案。")
                this.key++
                this.$refs.table.loadData()
              } else {
                this.$message.error(data.message)
              }
            })
      }
      if (this.fastAppraisalCommand == 'search') {
        this.$http.post("/archiveAppraisalTask/fastAppraisalBySearch", Object.assign({
          taskId: this.businessId,
          auxiliaryResult
        }, this.$refs.table.searchFormModel))
            .then(({data}) => {
              if (data.success) {
                this.$message.success("一键鉴定" + data.data + "份档案。")
                this.key++
                this.$refs.table.loadData()
              } else {
                this.$message.error(data.message)
              }
            })
      }
    }
  }
}
</script>
