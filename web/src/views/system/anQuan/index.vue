<template>
  <section>
    <nac-info>
      <config-form @func="loadData" ref="form"/>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table :data="data" border height="80vh" class="table-container" align="center">
        <el-table-column prop="order" label="排序" width="50" header-align="center" align="center">
          <template slot-scope="scope">
            {{ (page.index - 1) * page.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="kuFangMingCheng" label="库房名称" align="center" header-align="center" show-overflow-tooltip/>
          <el-table-column prop="createDate" label="记录日期" align="center" header-align="center" show-overflow-tooltip>
              <template slot-scope="scope">
                  {{timestampToTime(scope.row.createDate)}}
              </template>
          </el-table-column>
          <el-table-column prop="fangHuo" label="防火情况" align="center" header-align="center" show-overflow-tooltip/>
          <el-table-column prop="fangDao" label="防盗情况" align="center" header-align="center" show-overflow-tooltip/>
          <el-table-column prop="personName" label="记录人" align="center" header-align="center" show-overflow-tooltip/>
          <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
          <template slot-scope="scope">
            <el-link type="primary" @click="editForm(scope.row)">编 辑</el-link>
            |
            <el-link type="danger" @click="remove(scope.row.id)">删 除</el-link>
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
      <file-list refType="archive" :formDataId="formDataId" :deleter="false" :upload="false" :transform="false"
                 style="margin-top: 5px"
                 width="50%" v-if="fileListDialog"/>
    </div>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@/util/indexMixin'

export default {
  data() {
    return {fileListDialog: false, formDataId: ''}
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
      timestampToTime(timestamp) {
          if (timestamp != 0 && timestamp != undefined) {
              return this.$moment(timestamp).format('YYYY年MM月DD日')
          }
      },
    preview(row) {
      this.fileListDialog = true
      this.formDataId = row.formDataId
      console.log(row)
    }
  }
}
</script>
