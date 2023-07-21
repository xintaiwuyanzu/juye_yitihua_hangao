<template>
  <table-index :fields="fields"
               path="archiveToBeAppraisal"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               ref="table"
               style="height:70vh;display: flex;flex-direction: column">
    <template v-slot:search="scope" style="width:50px">
      <el-form-item label="提名" prop="title">
        <el-input v-model="scope.title" placeholder="请输入提名" clearable style="width: 150px;">

        </el-input>
      </el-form-item>
    </template>
    <template v-slot:search="scope" style="width:50px">
      <el-form-item label="年份" prop="vintages">
        <el-input v-model="scope.vintages" placeholder="请输入年份" clearable style="width: 150px;">

        </el-input>
      </el-form-item>
    </template>
    <el-table-column prop="batchId" label="是否锁定" width="80" align="center">
      <template v-slot="scope">
        {{ scope.row.batchId ? '是' : '否' }}
      </template>
    </el-table-column>
    <template v-slot:search-$btns="scope">
      <!--        <el-button  @click="search(scope)"  type="primary" width="40">高级搜索</el-button>-->
      <el-button @click="createAppraisalTask(scope)" type="primary" width="40">创建鉴定批次</el-button>
      <el-button @click="startScanArchive()" type="primary" width="40">刷新待鉴定库</el-button>
      <el-button @click="scanArchiveHistory()" type="primary" width="40">刷新历史</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="goDetail(scope)" type="text" width="20">查看</el-button>
    </template>
    <search-from ref="search" v-on:search="resLoadSearch"></search-from>
    <archive-batch-form v-on:add="addBatch" ref="archiveBatch"></archive-batch-form>
  </table-index>
</template>

<script>

import searchFrom from './search'
import archiveBatchForm from './archiveBatchForm'

const result = {'kz': '控制', 'kf': '开放'}
export default {
  components: {searchFrom, archiveBatchForm},
  data() {
    return {
      fields: {
        fondCode: {label: '全宗编码', search: false, edit: false, align: 'center', width: 100},
        categoryCode: {label: '门类编码', search: false, edit: false, align: 'center', width: 150},
        archiveCode: {label: '档号', search: false, edit: false, align: 'center', width: 200},
        title: {label: '题名', search: true, edit: false, align: 'center'},
        vintages: {label: '年份', search: true, edit: false, align: 'center', width: 100},
        filetime: {label: '文件时间', search: false, edit: false, align: 'center', width: 150},
        appraisalType: {
          label: '鉴定类型', search: true, fieldType: 'select', component: 'tag',
          width: 100,
          mapper: [{
            id: 'KFJD',
            label: '开放鉴定'
          }, {
            id: 'DQJD',
            label: '销毁鉴定'
          }],
          align: 'center'
        },
        auxiliaryResult: {
          label: '辅助鉴定结果', component: 'tag', search: true, fieldType: 'select',
          width: 220,
          mapper: [],
          align: 'center'
        },
        batchId: {label: '是否锁定', search: false, edit: false, align: 'center', width: 80}
      },
      defaultSearchForm: {},
      defaultInsertForm: {}
    }
  },
  methods: {
    $init() {
      this.$http.post("/appraisalOpenRange/page", {page: false})
          .then(({data}) => {
            this.fields.auxiliaryResult.mapper = []
            data.data.forEach(v => {
              this.fields.auxiliaryResult.mapper.push({id: v.id, label: result[v.auxiliaryResult] + '--' + v.openRange})
            })
            this.fields.auxiliaryResult.mapper.push({id: 'null', label: '无匹配结果'})
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
      sessionStorage.removeItem("batchId")
      sessionStorage.removeItem("taskId")
      this.$router.push({
        path: '/appraisal/toBeAppraisal/newDetail', query: {
          archiveCode: scope.row.archiveCode,
          formDefinitionId: scope.row.formDefinitionId,
          formDataId: scope.row.formDataId,
          index: scope.$index + (parseInt(this.$refs.table.data.page.index) - 1) * parseInt(this.$refs.table.data.page.size)
        }
      })
    },
    createAppraisalTask() {
      this.$refs.archiveBatch.show = true
    },
    addBatch(param) {
      this.$refs.archiveBatch.show = false
      //this.$router.push({path:"/appraisal/toBeAppraisal/statistics/",query: param})
      this.$http.post("/appraisalBatch/insert", param).then(({data}) => {
        if (data.success) {
          this.$message.success("创建成功")
        } else {
          this.$message.error();
        }
      })
    },
    startScanArchive() {
      this.$confirm('此操作会立即刷新原定于晚上十点执行的计划任务，是否执行?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/archiveToBeAppraisal/startScanArchive")
            .then(({data}) => {
              if (data.success) {
                this.$message.success("后台正在刷新待鉴定库")
              }
            })
      })
    },
    scanArchiveHistory() {
      this.$router.push({path: "/appraisal/toBeAppraisal/scanArchiveHistory/"})
    }
  }
}
</script>
