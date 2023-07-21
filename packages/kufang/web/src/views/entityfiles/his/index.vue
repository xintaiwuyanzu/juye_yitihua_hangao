<template>
  <section >
    <nac-info>
      <log-form ref="logForm" v-if="showSearch"  @func="loadData"/>
    </nac-info >
    <div class="index_main"  v-loading="loading">

      <div class="table-container">
        <el-table :data="data" border>
          <el-table-column label="排序" fixed align="center" width="50" type="index"/>

          <el-table-column label="档案名称" align="center" header-align="center"
                           show-overflow-tooltip>
            <template v-slot="scope">
              <el-button type="text" size="small">
                {{ scope.row.title}}
              </el-button>
            </template>
          </el-table-column>
          <el-table-column prop="archiveCode" label="档号" width="120" align="center" header-align="center" show-overflow-tooltip/>

          <el-table-column label="记录类型" width="140" align="center"
                           header-align="center">
            <template v-slot="scope">
              {{ scope.row.doType|dict({'0': '出库', '1': '入库'}) }}
            </template>
          </el-table-column>
          <el-table-column label="记录时间" width="140" align="center"
                           header-align="center">
            <template v-slot="scope">
              {{ scope.row.createDate|datetime }}
            </template>
          </el-table-column>
          <el-table-column label="库房" width="120" align="center" header-align="center">
            <template v-slot="scope">
              {{ scope.row.locName }}
            </template>
          </el-table-column>
          <el-table-column prop="personName" label="经手人" width="120" align="center" header-align="center"/>
          <el-table-column prop="reason" label="出库原因" show-overflow-tooltip align="center" header-align="center"/>
          <el-table-column prop="remarks" label="备 注" align="center" header-align="center"
                           show-overflow-tooltip/>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData(Object.assign({},$refs.logForm.searchForm,{pageIndex:index-1},),true)"
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
import logForm from './form'

export default {
  mixins: [indexMixin],
  components: {logForm},
  data() {
    return {
      data: [],
      loading: false,
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    //
    apiPath() {
      return 'his'
    },
  }
}
</script>