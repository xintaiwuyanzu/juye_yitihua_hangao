<template>
  <section>
    <nac-info>
      <config-form @func="loadData" ref="form"/>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table :data="pdfList" border height="80vh" class="table-container">
        <el-table-column prop="order" label="排序" width="80" header-align="center" align="center">
          <template v-slot="scope" v-if="page.index===0">
            {{ scope.$index + 1 }}
          </template>
          <template v-slot="scope" v-else>
            {{ (page.index-1) * page.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="文件名称" header-align="center" show-overflow-tooltip/>
        <el-table-column prop="suffix" label="文件类型" header-align="center" show-overflow-tooltip width="220px"/>
        <el-table-column prop="saveDate" label="上传日期" header-align="center" show-overflow-tooltip width="200px">
          <template slot-scope="scope">
            {{ scope.row.saveDate|date }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="备注" header-align="center" show-overflow-tooltip width="200px"/>
<!--        <el-table-column label="备份日期" prop="createDate" header-align="center" width="220px"
                         dateFormat="YYYY-MM-DD HH-mm-ss"
                         show-overflow-tooltip
                         align="center"/>-->
        <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
          <template v-slot="scope">
            <el-link type="success" @click="downLoad(scope.row)">下 载</el-link>
            <el-divider v-if="deleter" direction="vertical"/>
            <el-link type="danger" v-if="deleter" @click="deletePdf(scope.row)">删 除</el-link>
          </template>
        </el-table-column>
      </el-table>
<!--      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1},$refs.form.getSearchForm())"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>-->
    </div>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@archive/core/src/util/indexMixin'

export default {
  data() {
    return {
      refId:'tool-download',
      pdfList:[],
      deleter:true,
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
    apiPath() {
      return 'recovery'
    },
    async loadData() {
      this.loading = true
      if (this.refId) {
        const {data} = await this.$http.post('files/list', {
          refId: this.refId,
          refType: 'default',
          groupCode: 'default'
        })
        this.loading = false
        if (data.success) {
          this.loading = false
          if (data && data.success) {
            this.pdfList = data.data
            this.$forceUpdate()
          }
        }
      }
      this.loading = false
    },
    async downLoad(row) {
      this.fileInfo = row
      this.src = `/api/files/downLoad/${row.id}`
      window.open(this.src)
      //this.preview = true
    },
    deletePdf(row) {
      this.$confirm('此操作将进行删除信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('files/delete/' + row.id)
            .then(({data}) => {
              if (data.success) {
                this.loadData()
                this.$message.success("删除成功")
                this.$emit("uploadYuanwen")
              }
            })
      })
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
