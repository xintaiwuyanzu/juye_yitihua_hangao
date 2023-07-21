<template>
  <section>
    <el-table :data="data" border height="215px">
      <el-table-column label="排序" type="index" align="center"/>
      <el-table-column prop="spaceName" label="存储空间名称" align="center" show-overflow-tooltip/>
      <el-table-column prop="catalogue" label="挂载目录" align="center"/>
      <el-table-column prop="capacity" label="容量" align="center">
        <template v-slot="scope">
          <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.percent"
                       :status="scope.row.percent <= 50? 'success' :
                             scope.row.percent <= 70? 'warning' : 'exception'" :format="setItemText(scope.row)"/>
        </template>
      </el-table-column>
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
      return 'earchive/spaces'
    },
    setItemText(row) {
      return () => {
        return row.usedSpace + 'G/' + row.capacity + 'G \xa0\xa0|\xa0\xa0 ' + row.percent + '%'
      }
    }
  }
}
</script>
