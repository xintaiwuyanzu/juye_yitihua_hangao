<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="批次名称:">
          <el-input v-model="searchForm.batchName" style="" placeholder="请输入批次名称" clearable/>
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
          <el-table-column label="批次名称" prop="batchName" show-overflow-tooltip align="center"/>
          <!--          <el-table-column label="批次类型" prop="taskType" align="center">-->
          <!--            <template v-slot="scope">-->
          <!--              {{ scope.row.batchType }}-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column label="提交人" prop="createPerson" show-overflow-tooltip align="center"/>
          <el-table-column label="提交时间" prop="createDate"
                           dateFormat="YYYY-MM-DD HH-mm-ss"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column label="操作" align="center" header-align="center" width="120">
            <template v-slot="scope">
              <el-link type="danger" size="mini" @click="detail(scope.row)">查看详情</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--      <page :page="page"/>-->
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1,batchType: 'ARCHIVE'},this.searchForm)"
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
 * 批次详情列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      loading: false,
      searchForm: {
        batchName: "",
      },
      data: []
    }
  },
  methods: {
    $init() {
      this.loadData({batchType: 'ARCHIVE'})
    },
    apiPath() {
      return "/batch"
    },
    search() {
      this.loadData({batchType: 'ARCHIVE', batchName: this.searchForm.batchName})
    },
    detail(row) {
      this.$router.push({
        path: '/archive/task/filingbatch/filingdetail',
        query: {
          batchId: row.id,
          batchType: row.batchType
        }
      })
    }
  }
}
</script>
