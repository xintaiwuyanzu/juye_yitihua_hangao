<template>
  <section>
    <nac-info :back="true">
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table :data="dynamicTags" border height="100%">
          <el-table-column label="排序" type="index" align="center">
            <template slot-scope="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="testRecordType" label="检测类型" align="center" header-align="center">
            <template  slot-scope="scope">
              {{ scope.row.testRecordType|dict({'INTEGRITY': '完整性', 'AUTHENTICITY': '真实性', 'SECURITY': '安全性','USABILITY':'可用性'}) }}
            </template>
          </el-table-column>
          <el-table-column prop="testName" label="检测小项" align="center" header-align="center">
          </el-table-column>
          <el-table-column prop="status" label="检测结果" align="center" header-align="center"
                           show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.status|dict({'0': '未检测', '1': '通过', '2': '未通过'}) }}
            </template>
          </el-table-column>
          <el-table-column prop="testResult" label="检测结果说明" align="center" header-align="center" show-overflow-tooltip>
          </el-table-column>
<!--          <el-table-column prop="createDate" label="检测对象编码" align="center" header-align="center"
                           show-overflow-tooltip>
          </el-table-column>-->
          <el-table-column prop="archive_Data" label="检测对象值" align="center" header-align="center"
                           show-overflow-tooltip>
          </el-table-column>
          <el-table-column prop="createDate" label="检测时间" align="center" header-align="center"
                           show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.createDate|datetime }}
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

    <el-dialog
        title="详情"
        :visible.sync="dialogVisible"
        width="40%"
        :before-close="handleClose">
      <div style="margin-top: 10px">
        <span v-html="content"></span>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handleClose">取 消</el-button>
    <el-button type="primary" @click="handleClose">确 定</el-button>
  </span>
    </el-dialog>
  </section>
</template>
<script>
import indexMixin from '@/util/indexMixin'

export default {
  mixins: [indexMixin],
  data() {
    return {
      data: [],
      loading: false,
      searchForm: {},
      dynamicTags:[],
      dialogVisible: false,
      content: ''
    }
  },
  methods: {
    $init() {
      this.loadData({page:false})
    },
    loadData() {
        this.loading = true
        this.$http.post('/fourNatureRecord/page', {
              page: false,
          businessId: this.$route.query.businessId,
            }).then(({data}) => {
          if (data.success) {
            this.dynamicTags = data.data
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
    },
    apiPath() {
      return '/fourNatureRecord'
    },
    showContent(val) {
      this.content = val.content
      this.dialogVisible = true
    },
    handleClose() {
      this.dialogVisible = false
      this.content = ''
    },
    subContent(val) {
      if (val.content.length >= 50) {
        return val.content.substring(0, 50) + '...'
      } else {
        return val.content
      }
    },
    //已读
    readMsg(row) {
      let path = 'hudong/update'
      row.status = '1'
      this.$http.post(path, row).then(({data}) => {
        if (data && data.success) {
          this.$message.success('请求成功！')
          this.loadData()
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    deleteOne(id) {
      this.$confirm('确定删除当前互动交流记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/hudong/delete', {id: id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("删除成功！")
                this.loadData()
              } else {
                this.$message.error(data.message)
              }

            })

      })
    }

  }
}
</script>
<style lang="scss">

</style>