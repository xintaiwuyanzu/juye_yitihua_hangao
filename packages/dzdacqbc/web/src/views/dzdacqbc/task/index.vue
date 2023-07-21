<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="form"></config-form>
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="taskName" label="任务名称" align="center" show-overflow-tooltip/>
          <el-table-column prop="taskType" label="任务类型" align="center"/>
          <el-table-column prop="taskState" label="任务状态" align="center">
            <template v-slot="scope">
              <div :style="{'color': scope.row.taskState === '1'?'orange':'green'}">
                {{ scope.row.taskState | taskStateFilter(scope.row.taskState) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="startTime" label="开始时间" align="center" dateFormat="YYYY-MM-DD HH:mm:ss">
          </el-table-column>
          <el-table-column prop="endTime" label="结束时间" align="center" dateFormat="YYYY-MM-DD HH:mm:ss"/>
          <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
            <template v-slot="scope">
              <el-button type="text" size="small" @click="detail(scope.row)">详 情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1})"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@/util/indexMixin'

export default {
  data() {
    return {
      data: [],
      configForm: {},
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
    apiPath() {
      return '/cqbctaskdetail'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
    detail(row) {
      this.$router.push({path: './task/detail', query: row})
    },
  },
  filters: {
    taskStateFilter(v) {
      if (v === '1') return '进行中'
      if (v === '2') return '已完成'
    },
  }
}
</script>


