<template>
  <table-index path="utilization/consult/details"
               :fields="fields"
               :delete="false"
               :edit="false"
               :back="true"
               ref="table"
               title="查档登记详情"
               :insert="false"
               :defaultSearchForm="searchForm">
    <el-table-column align-="center" label="题名" prop="title" header-align="center">
      <template v-slot="scope">
        <el-button type="text" @click="routeItem(scope)">{{ scope.row.title }}</el-button>
      </template>
    </el-table-column>
    <template v-slot:search-$btns>
      <batch-info/>
      <to-end v-if="$route.query.status!='2'" />
    </template>
    <template v-slot:table-$btns="scope">
      <el-button width="40" type="text" @click="routeItem(scope)">查看</el-button>
    </template>
  </table-index>
</template>

<script>
import BatchInfo from "./batchInfo";
import toEnd from "./toEnd";


/**
 *查档批次详情
 */
export default {
  components: {BatchInfo,toEnd},
  data() {
    return {
      searchForm: {batchId: this.$route.query.id},
      fields: {
        fondName: {label: '全宗', width: 140},
        archiveCode: {label: '档号', width: 160, search: true},
        title: {label: '题名'},
        year: {label: '年度', width: 80},
        useFile: {label: '查看原文', mapper: {true: '是', false: '否'}, width: 70}
      }
    }
  },
  methods: {
    /**
     * 跳转审核详情
     * @param scope
     */
    routeItem({$index, row}) {
      const path = this.$route.path + "/detailItem"
      const page = this.$refs.table.data.page
      //查询参数
      let query = Object.assign({
        detailId: row.id,
        index: (page.index - 1) * page.size + $index,
        total: page.total
      }, this.$route.query)
      this.$router.push({path, query})
    }
  }
}
</script>
