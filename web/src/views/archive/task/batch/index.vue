<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="任务名称:">
          <el-input v-model="searchForm.taskName" style="" placeholder="请输入任务名称" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="$init" size="mini">搜 索</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
          <column-index :page="page"/>
          <el-table-column label="任务名称" prop="taskName" show-overflow-tooltip align="center"/>
          <el-table-column label="任务类型" prop="taskType" align="center">
            <template v-slot="scope">
              {{ scope.row.taskType }}
            </template>
          </el-table-column>
          <el-table-column label="发起人" prop="sourcePersonName" show-overflow-tooltip align="center"/>
          <el-table-column label="发起时间" prop="sourceDate"
                           :formatter="(r,c,cell)=>$moment(cell).format('YYYY-MM-DD')"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column label="接收人" prop="targetPersonName" align="center"/>
          <el-table-column label="状态" prop="status" align="center"/>
          <el-table-column label="操作" align="center" header-align="center" width="120">
            <template v-slot="scope">
              <el-link type="danger" size="mini" @click="remove(scope.row.id)">删 除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <page :page="page"/>
    </div>
  </section>
</template>
<script>
import indexMixin from '@/util/indexMixin'

/**
 * 批次详情列表
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
  methods: {
    async loadData() {
      this.loading = true
      const {data} = await this.$post('/archiveTask/page')
      this.data = data.data.data
      this.page.index = data.data.start / data.data.size + 1
      this.page.size = data.data.size
      this.page.total = data.data.total
      this.loading = false
    },
    $init() {
      this.loadData()
    }
  }
}
</script>
