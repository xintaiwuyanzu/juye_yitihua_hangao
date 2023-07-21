<template>
  <table-index path="utilization/consult/details"
               :fields="fields"
               :delete="false"
               :edit="false"
               :back="true"
               ref="table"
               :insert="false"
               :defaultSearchForm="searchForm">
    <el-table-column align-="center" label="题名" prop="title" header-align="center">
      <template v-slot="scope">
        <el-button type="text" @click="routeItem(scope)">{{ scope.row.title }}</el-button>
      </template>
    </el-table-column>
    <template v-slot:search-$btns>
      <el-button type="primary">申请电子版</el-button>
      <el-button type="primary">申请查看原文</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button width="40" type="text" @click="routeItem(scope)">查看</el-button>
    </template>
  </table-index>
</template>
<script>
/**
 *查档批次详情
 */
export default {
  data() {
    return {
      searchForm: {batchId: this.$route.query.id},
      fields: {
        fondName: {label: '全宗', width: 140},
        archiveCode: {label: '档号', width: 160, search: true},
        title: {label: '题名', search: true},
        year: {label: '年度', width: 80},
      }
    }
  },
  methods: {
    /**
     * 跳转审核详情
     * @param scope
     */
    routeItem({$index, row}) {
      const page = this.$refs.table.data.page
      //查询参数
      let query = Object.assign({
        detailId: row.id,
        index: (page.index - 1) * page.size + $index,
        total: page.total
      }, this.$route.query)
      this.$router.push({path: '/detailItem', query})
    }
  }
}
</script>