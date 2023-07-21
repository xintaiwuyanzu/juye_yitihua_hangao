<template>
  <section>
    <nac-info>
      <config-form @func="loadData" ref="form"/>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table :data="data" border height="80vh" class="table-container">
        <el-table-column prop="order" label="排序" width="80" header-align="center" align="center">
          <template v-slot="scope">
            {{ (page.index - 1) * page.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="createPersonName" label="操作人" header-align="center" show-overflow-tooltip width="220px"/>
        <el-table-column prop="backupRecoveryPath" label="备份位置" header-align="center" show-overflow-tooltip/>
        <el-table-column prop="sysName" label="系统" header-align="center" show-overflow-tooltip width="200px"/>
        <el-table-column prop="versionNum" label="版本号" header-align="center" show-overflow-tooltip width="200px"/>
        <el-table-column label="备份日期" prop="createDate" header-align="center" width="220px"
                         dateFormat="YYYY-MM-DD HH-mm-ss"
                         show-overflow-tooltip
                         align="center"/>
        <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
          <template v-slot="scope">
            <el-link type="danger" @click="remove(scope.row.id)">删 除</el-link>
<!--            |-->
<!--            <el-link type="success" @click="dowload(scope.row)">下 载</el-link>-->
<!--            |-->
<!--            <el-link type="warning" @click="recovery(scope.row)">恢 复</el-link>-->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1},$refs.form.getSearchForm())"
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
import indexMixin from '@archive/core/src/util/indexMixin'

export default {
  data() {
    return {}
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
    apiPath() {
      return 'backup'
    },
    dowload(row) {
      window.open(`${row.showPath}`, "_blank")
    },
    recovery(row) {
      this.$http.post(this.apiPath() + '/recovery?id=' + row.id).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.loadData()
        }
        this.loading = false
      })
    }
  }
}
</script>
