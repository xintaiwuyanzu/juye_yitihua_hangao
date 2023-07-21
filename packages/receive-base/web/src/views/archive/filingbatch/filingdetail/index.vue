<template>
  <section>
    <nac-info back>
<!--      <el-input v-model="searchForm.taskName" style="width: 120px" placeholder="请输入任务名称" clearable/>-->
      <!--      <el-button type="primary" @click="$init" size="mini">搜 索</el-button>-->
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
          <column-index :page="page"/>
          <el-table-column label="题名" prop="title" show-overflow-tooltip align="center"/>
          <el-table-column label="全宗" prop="fondName" show-overflow-tooltip align="center"/>
          <el-table-column label="分类" prop="categoryCode" show-overflow-tooltip align="center"/>
          <el-table-column label="机构或问题" prop="orgCode" show-overflow-tooltip align="center" min-width="120"/>
          <el-table-column label="档号" prop="archiveCode" show-overflow-tooltip align="center"/>
          <el-table-column label="关键词" prop="keyWords" show-overflow-tooltip align="center"/>
          <el-table-column label="年度" prop="year" show-overflow-tooltip align="center"/>
          <el-table-column label="提交人" prop="createPerson" show-overflow-tooltip align="center"/>
          <el-table-column label="提交时间" prop="createDate"
                           :formatter="(r,c,cell)=>$moment(cell).format('YYYY-MM-DD')"
                           show-overflow-tooltip
                           align="center"/>
        </el-table>
      </div>
      <page :page="page"/>
    </div>
  </section>
</template>
<script>
import indexMixin from '@/util/indexMixin'

/**
 * 批次列表
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
      const {data} = await this.$post('/batch/batchDetailPage', {
        id: this.$route.query.batchId,
        batchType: this.$route.query.batchType
      })
      this.data = data.data.data
      this.page.index = data.data.start / data.data.size + 1
      this.page.size = data.data.size
      this.page.total = data.data.total
      this.loading = false
    },
    $init() {
      this.loadData()
    },
  }
}
</script>
