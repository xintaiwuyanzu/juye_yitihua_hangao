<template>
  <section>
    <nac-info>
      <log-form ref="logForm" @func="loadData" :pid="pid"/>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center">
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="档案类别" align="center" header-align="center">
            {{ archiveTypeName }}
          </el-table-column>
          <el-table-column prop="propertyName" label="属性" align="center" header-align="center">
          </el-table-column>

          <el-table-column label="操作" width="200" header-align="center" align="center"
          >
            <template v-slot="scope">
              <el-link type="success" size="small"
                       @click="editForm(scope.row)">编 辑
              </el-link>
              <el-divider direction="vertical"></el-divider>
              <el-link type="danger" size="small"
                       @click="remove(scope.row.id)">删 除
              </el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
        @current-change="index=>loadData({pageIndex:index-1},true)"
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
      pid: '',
      archiveTypeName: '',
      loading: false,
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    loadData() {
      this.$http.post('/property/page', {archiveTypeId: this.pid}).then(({data}) => {
        if (data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    //
    apiPath() {
      return 'property'
    },
    editForm(row) {
      this.$refs.logForm.editForm(row)
    }
  },
  mounted: function () {
    this.pid = this.$route.query.id
    this.archiveTypeName = this.$route.query.archiveTypeName
  }
}
</script>
