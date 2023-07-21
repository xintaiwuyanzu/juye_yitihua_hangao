<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="archiveForm"></config-form>
      <div class="table-container">
        <el-table :data="data" border ref="archiveTable" height="330">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column prop="totalName" label="所属全宗" align="center" show-overflow-tooltip/>
          <el-table-column prop="title" label="题名" align="center" />
          <el-table-column prop="archiveCode" label="档号" align="center"/>
          <el-table-column prop="durationStorage" label="保管期限" align="center"/>
          <el-table-column prop="annual" label="年度" align="center"/>
          <el-table-column label="成文时间" prop="written"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column prop="pages" label="页数" align="center"/>
          <el-table-column prop="classificationName" label="所属分类" align="center"/>
          <el-table-column prop="warehouseName" label="入库批次" align="center"/>

<!--          <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">-->
<!--            <template v-slot="scope">-->
<!--              <el-button type="text" size="small" @click="editFormChange(scope.row)">编 辑</el-button>-->
<!--              <el-button type="text" size="small" @click="remove(scope.row.id)">删 除</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
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
  data(){
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
      return '/cqbcarchive'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
    remove(id) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$http.post(this.apiPath() + '/delete', {Id: id})
                .then(({data}) => {
                  if (data.success) {
                    this.$message.success('删除成功！')
                    this.loadData()
                  } else {
                    this.$message.error(data.message)
                    this.loading = false
                  }
                })
      })
    },
    getFileId(id) {
      this.$refs.archiveForm.getId(id)
    },
  }
}
</script>

<style scoped type="scss">

</style>
