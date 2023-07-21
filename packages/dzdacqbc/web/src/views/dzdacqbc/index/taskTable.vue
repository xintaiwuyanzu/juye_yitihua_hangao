<template>
  <section>
    <el-table :data="data" border height="250">
      <el-table-column label="排序" type="index" align="center"/>
      <el-table-column prop="taskName" label="任务名称" align="center" width="320px" show-overflow-tooltip/>
      <el-table-column prop="taskType" label="任务类型" align="center" width="100px" show-overflow-tooltip/>
      <el-table-column prop="taskState" label="任务状态" align="center" width="100px" show-overflow-tooltip>
        <template v-slot="scope">
          {{ scope.row.taskState === '1' ? '进行中' : '已完成' }}
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" align="center" dateFormat="YYYY-MM-DD HH:mm:ss"
                       show-overflow-tooltip/>
      <el-table-column prop="endTime" label="结束时间" align="center" dateFormat="YYYY-MM-DD HH:mm:ss"
                       show-overflow-tooltip/>
    </el-table>
    <el-pagination
        @current-change="index=>loadData({pageIndex:index-1})"
        :current-page.sync="page.index"
        :page-size="page.size"
        layout="total, prev, pager, next"
        :total="page.total">
    </el-pagination>
  </section>
</template>

<script>
import indexMixin from '@/util/indexMixin'

export default {

  data() {
    return {
      data: [],
      configForm: {},
    }
  },
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
    apiPath() {
      return '/cqbcTask'
    },
  }
}
</script>
