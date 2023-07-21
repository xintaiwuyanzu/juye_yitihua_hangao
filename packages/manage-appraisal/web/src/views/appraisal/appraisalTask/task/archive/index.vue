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
      <process-container
          v-if="user.id===taskData.currentPerson && isEdit"
          title="发起档案鉴定审核流程"
          :before-save="appendParams"
          processType="appraisal"
          :business-id="$route.query.taskId"
          ref="pro"
          @saved="notifyResult">
        <el-button type="primary" width="20" @click="submitBatchTask()">提交审核</el-button>
      </process-container>
      <el-dropdown trigger="click"
                   @command="fastAppraisal" v-if="isEdit">
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
      <el-button v-if="user.id===taskData.currentPerson && isEdit" @click="goAppraisal(scope)" type="text" width="20">鉴定
      </el-button>
      <el-button v-else @click="goDetail(scope)" type="text" width="20">查看</el-button>
    </template>
    <fast-appraisal :key="key" v-on:saveForm="confirmAppraisal" ref="fastAppraisal"></fast-appraisal>
  </table-index>
</template>
<script>

import {useUser} from "@dr/framework/src/hooks/userUser";
import {processContainer} from '@dr/process/src/lib'

import fastAppraisal from "./fastAppraisal";

export default {
  components: {processContainer, fastAppraisal},
  data() {
    return {
      fields: {
        fondCode: {label: '全宗编码', search: false, edit: false, align: 'center', width: 100},
        categoryCode: {label: '门类编码', search: false, edit: false, align: 'center', width: 150},
        archiveCode: {label: '档号', search: true, edit: false, align: 'center', width: 200},
        title: {label: '题名', search: false, edit: false, align: 'center'},
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
        taskId: this.$route.query.taskId
      },
      defaultInsertForm: {},
      taskData: {},
      key: 0,
      fastAppraisalCommand: '',
      isEdit: true,
    }
  },
  setup() {
    return useUser()
  },
  methods: {
    $init() {
      this.$http.post("/appraisalBatchTask/detail", {id: this.$route.query.taskId})
          .then(({data}) => {
            if (data.success) {
              this.taskData = data.data
              if (data.data.status === '1' || data.data.status === '2') {
                this.isEdit = false
              }
              if ('admin' != this.taskData.currentPerson) {
                this.fields.status.show = false
              }
            }
          })
    },
    search() {
      this.$refs.search.show = true
    },
    resLoadSearch(param) {
      this.defaultSearchForm = param
      this.$refs.table.loadData(this.defaultSearchForm)
    },
    goDetail(scope) {
      sessionStorage.setItem("taskId", this.$route.query.taskId)
      this.$router.push({
        path: '/appraisal/toBeAppraisal/newDetail', query: {
          formDefinitionId: scope.row.formDefinitionId,
          formDataId: scope.row.formDataId,
          index: scope.$index + (parseInt(this.$refs.table.data.page.index) - 1) * parseInt(this.$refs.table.data.page.size)
        }
      })
    },
    goAppraisal(scope) {
      sessionStorage.setItem("taskId", this.$route.query.taskId)
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
            if (data.success && data.data == 0) {
              this.$refs.pro.open()
            } else {
              this.$message.error("有未鉴定档案，请鉴定之后再进行操作")
            }
          })
    },
    appendParams(form) {
      //TODO 需要判读是添加、修改、删除的审批
      // form.historyType = ''
    },
    async notifyResult(params) {
      if (params.success) {
        this.$message.success('提交审核成功！')
        this.$router.back()
      } else {
        this.$message.warning(params.message)
      }
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
      this.$refs.fastAppraisal.appraisalType = this.taskData.appraisalType
      this.$refs.fastAppraisal.show = true
      // this.$confirm('此操作将未鉴定档案且有机器鉴定结果的统一采用机器鉴定结果, 是否继续?', '提示', {
      //   confirmButtonText: '确定',
      //   cancelButtonText: '取消',
      //   type: 'warning'
      // }).then(() => {
      //   this.$http.post("/archiveAppraisalTask/fastAppraisal",{taskId:this.$route.query.taskId})
      //       .then(({data})=>{
      //         if(data.success){
      //           this.$message.success("一键鉴定"+data.data+"份档案。")
      //           this.$refs.table.loadData()
      //         }else{
      //           this.$message.error(data.message)
      //         }
      //       })
      // })
    },
    confirmAppraisal(auxiliaryResult) {
      if (this.fastAppraisalCommand == 'all') {
        this.$http.post("/archiveAppraisalTask/fastAppraisal", Object.assign(auxiliaryResult, {taskId: this.$route.query.taskId}))
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
        if (ids.length === 0) {
          this.$message.error("无选中档案,请先勾选要鉴定档案!")
          return
        }
        this.$http.post("/archiveAppraisalTask/fastAppraisalByIds", Object.assign(auxiliaryResult, {
          taskId: this.$route.query.taskId,
          ids: ids.join(',')
        }))
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
          taskId: this.$route.query.taskId
        }, auxiliaryResult, this.$refs.table.searchFormModel))
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
