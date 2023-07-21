<template>
  <section>
    <nac-info>
<!--      <config-form @func="loadData" ref="form"/>-->
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table :data="data" border height="80vh" class="table-container">
        <el-table-column prop="order" label="排序" width="80" header-align="center" align="center">
          <template v-slot="scope" v-if="page.index===0">
            {{ scope.$index + 1 }}
          </template>
          <template v-slot="scope" v-else>
            {{ (page.index-1) * page.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="fileName" label="文件名称" align="center" header-align="center" show-overflow-tooltip/>
        <el-table-column prop="fileSuffix" label="原文件类型" align="center" header-align="center" show-overflow-tooltip width="100px"/>
        <el-table-column prop="afterFileSuffix" label="转换后文件类型" align="center"header-align="center" show-overflow-tooltip width="110px"/>
        <el-table-column prop="fileSize" label="原文件大小" align="center" header-align="center" show-overflow-tooltip width="110px"/>
        <el-table-column prop="afterFileSize" label="转换后原文件大小" align="center" header-align="center" show-overflow-tooltip width="100px"/>
        <el-table-column prop="fileUrl" label="原文件路径" align="center" header-align="center" show-overflow-tooltip/>
        <el-table-column prop="afterFileUrl" label="转换后文件路径" align="center" header-align="center" show-overflow-tooltip/>
        <el-table-column prop="createDate" label="转换时间" align="center" header-align="center" show-overflow-tooltip width="140px">
          <template slot-scope="scope">
            {{ scope.row.createDate|datetime }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" align="center" header-align="center" show-overflow-tooltip width="80px">
          <template slot-scope="scope">
          {{ scope.row.status|dict({'0': '失败', '1': '成功'}) }}
          </template>
        </el-table-column>
<!--        <el-table-column label="备份日期" prop="createDate" header-align="center" width="220px"
                         dateFormat="YYYY-MM-DD HH-mm-ss"
                         show-overflow-tooltip
                         align="center"/>-->
        <el-table-column label="操作" width="100" header-align="center" align="center" fixed="right">
          <template v-slot="scope">
            <el-link type="danger" v-if="deleter" @click="deletePdf(scope.row)">删 除</el-link>
          </template>
        </el-table-column>
      </el-table>
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
      return '/fileConversionRecording'
    },
/*    async loadData(parem) {
      this.loading = true
      if (this.refId) {
        const {data} = await this.$http.post('fileConversionRecording/page',parem)
        this.loading = false
        if (data.success) {
          this.loading = false
          if (data && data.success) {
            this.pdfList = data.data.data
            this.page.index = data.data.start / data.data.size + 1
            this.page.size = data.data.size
            this.page.total = data.data.total
            this.$forceUpdate()
          }
        }
      }
      this.loading = false
    },*/
    async downLoad(row) {
      this.fileInfo = row
      this.src = `/api/files/downLoad/${row.id}?download=false`
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
