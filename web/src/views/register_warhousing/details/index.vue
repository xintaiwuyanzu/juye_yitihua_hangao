<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="档案名称:">
          <el-input v-model="searchForm.TIMING" placeholder="请输入档案题名" clearable/>
        </el-form-item>
        <el-form-item label="档案名称:">
          <el-input v-model="searchForm.archiveCode" placeholder="请输入档号" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="backHistory" size="mini">返 回</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
          <column-index :page="page"/>
          <el-table-column label="题名" prop="timing" show-overflow-tooltip/>

          <el-table-column label="档号" prop="archiveCode" show-overflow-tooltip/>

          <el-table-column label="年度" prop="nd" show-overflow-tooltip/>

          <el-table-column label="文件形成日期" prop="wjxcrq" show-overflow-tooltip/>

          <el-table-column label="保管期限" prop="bgqx" show-overflow-tooltip/>

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
 * 批次详情列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      xiazai: true,
      loading: false,
      searchForm: {
        TIMING:'',
        archiveCode:'',

      },
      data: [],
      stateMapper: {'0': '执行中', '1': '成功', '2': '失败'}
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    dateFormatter(r, c, cell) {
      if (cell) {
        return this.$moment(cell).format('YYYY-MM-DD HH:mm:ss')
      }
      return '-'
    },
    async loadData(params) {
      let batchId=this.$route.query.batchId
      this.loading = true
      const {data} = await this.$post('/registerWarehousingDetails/page',{bId:batchId,...params})
      this.data = data.data.data
      this.page.index = data.data.start / data.data.size + 1
      this.page.size = data.data.size
      this.page.total = data.data.total
      this.loading = false
    },
    search() {
      this.loadData(this.searchForm)
    },
    remove(id) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = []
        if (id === true) {
          ids = [...this.selectIds]
        } else if (id) {
          ids = [id]
        }
        if (ids.length > 0) {
          this.loading = true
          this.$http.post('/expBatch/delete', {id: ids.join(','), type: "EXP"})
              .then(({data}) => {
                if (data.success) {
                  this.$message.success('删除成功！')
                  this.selectIds = []
                  this.loadData()
                } else {
                  this.$message.error(data.message)
                  this.loading = false
                }
              })
        } else {
          this.$message.warning('请选择要删除的数据列！')
        }
      })

    },
    backHistory(){
      this.$router.back();//返回上一层
    },
  }
}
</script>
