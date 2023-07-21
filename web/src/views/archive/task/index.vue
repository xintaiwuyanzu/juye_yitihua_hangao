<template>
  <section>
    <nac-info v-if="isShow">
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="任务名称">
          <el-input v-model="searchForm.taskName" style="" placeholder="请输入任务名称" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
          <column-index :page="page"/>
          <el-table-column label="任务名称" prop="taskName" width="250" show-overflow-tooltip align="center"/>
          <el-table-column v-if="isShow" label="任务类型" prop="taskType" align="center">
            <template v-slot="scope">
              {{
                scope.row.taskType|dict({'SEND_CHECK': '移交', 'JD': '鉴定', 'IMP': '导入', 'ARCHIVE': '归档', 'JDZD': '业务指导','CHECK': '年度检查'})
              }}
            </template>
          </el-table-column>
          <el-table-column v-if="isShow" label="发起人" prop="sourcePersonName" show-overflow-tooltip
                           align="center"/>
          <el-table-column v-if="isShow" label="发起时间" prop="sourceDate"
                           dateFormat="YYYY-MM-DD HH-mm-ss"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column v-if="isShow" label="接收人" prop="targetPersonName" align="center"/>
          <el-table-column label="状态" prop="status" align="center">
            <template v-slot="scope">
              {{ scope.row.status|dict({0: "待处理", 1: "办结", '-1': "未通过"}) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" header-align="center" width="120">
            <template v-slot="scope">
              <el-link type="danger" size="mini" @click="detail(scope.row)">查看详情</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1},this.searchForm)"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>
<script>
import indexMixin from '@/util/indexMixin'

/**
 * 待办任务列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      loading: false,
      searchForm: {
        formatName: "",
      },
      data: []
    }
  },
  props: {
    isShow: {default: true}
  },
  methods: {
    async loadData(params) {
      this.loading = true
      const {data} = await this.$post('/archiveTask/page', params)
      this.data = data.data.data
      this.page.index = data.data.start / data.data.size + 1
      this.page.size = data.data.size
      this.page.total = data.data.total
      this.loading = false
    },
    detail(row) {
      switch (row.taskType) {
        case "SEND_CHECK":
          this.$router.push({
            path: '/archive/task/batch/detail',
            query: {
              batchId: row.batchId,
              taskId: row.id,
              taskStatus: row.status,
              taskType: row.taskType
            }
          })
          break;
        case "JD":
          this.$router.push({
            path: '/archive/task/batch/appraisaldetail',
            query: {
              batchId: row.batchId,
              taskId: row.id,
              taskStatus: row.status,
              taskType: row.taskType,
            }
          })
          break;
        case "JDZD":
          this.$router.push({
            path: '/archive/task/batch/jdzd',
            query: {
              batchId: row.batchId,
              taskId: row.id,
              taskStatus: row.status,
              taskType: row.taskType,
            }
          })
          break;
      }
    },
    $init() {
      this.loadData()
    },
    search() {
      this.loadData(this.searchForm)
    }
  }
}
</script>
