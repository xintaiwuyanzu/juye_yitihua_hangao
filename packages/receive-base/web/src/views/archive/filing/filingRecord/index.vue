<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="form"></config-form>
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column label="记录名称" prop="filingName" align="center" show-overflow-tooltip/>
          <el-table-column prop="status_info" label="审核状态" align="center" width="100px" show-overflow-tooltip>
            <template slot-scope="scope">
              <div :style="{'color':
                             scope.row.status === '0'?'orange':
                             scope.row.status === '1'?'green':
                             scope.row.status === '2'?'red':'black'}">
                {{ scope.row.status | examineStateFilter(scope.row.status) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="remarks" label="备注" align="center" width="140px" show-overflow-tooltip/>
          <el-table-column prop="processName" label="审核流程" align="center" width="140px" show-overflow-tooltip/>
          <el-table-column label="操作" width="100px" header-align="center" align="center" fixed="right">
            <template slot-scope="scope">
              <!--              <el-button type="text" size="small" @click="remove(scope.row.id)">删 除</el-button>-->
              <el-button type="text" size="small"
                         @click="$router.push({path: '/archive/filing/filingRecord/detail', query: {id: scope.row.id,type:'RECORD'}})">
                档案详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change=" index=>loadData({pageIndex:index-1})"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>


    <!--    <el-dialog title="档案详情" :visible.sync="datailVisible" width="80%" center>-->
    <!--      <el-table :data="archiveData" border>-->
    <!--        <el-table-column label="排序" type="index" align="center"/>-->
    <!--        <el-table-column prop="title" label="题名" align="center" show-overflow-tooltip/>-->
    <!--        <el-table-column prop="archiveCode" label="档号" align="center" show-overflow-tooltip width="140px"/>-->
    <!--        <el-table-column prop="fondName" label="全宗名" align="center" show-overflow-tooltip width="100px"/>-->
    <!--        <el-table-column prop="categoryName" label="门类名" align="center" show-overflow-tooltip width="140px"/>-->
    <!--        <el-table-column prop="vintages" label="年度" align="center" show-overflow-tooltip width="80px"/>-->
    <!--        <el-table-column prop="saveTerm" label="保管期限" align="center" width="80px"/>-->
    <!--        &lt;!&ndash;        <el-table-column prop="ajdh" label="案卷档号" align="center" show-overflow-tooltip width="140px"/>&ndash;&gt;-->
    <!--        <el-table-column label="操作" width="100" header-align="center" align="center">-->
    <!--          <template slot-scope="scope">-->
    <!--            <yuanwen :row="scope.row"></yuanwen>-->
    <!--            &lt;!&ndash;                      <el-button type="text" size="small" @click="showYuanWen(scope.row.id)">原 文</el-button>&ndash;&gt;-->
    <!--          </template>-->
    <!--        </el-table-column>-->
    <!--      </el-table>-->
    <!--    </el-dialog>-->
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@/util/indexMixin'
// import Yuanwen from "../yuanwen"

export default {
  data() {
    return {
      data: [],
      configForm: {},
      archiveData: [],
      datailVisible: false,
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      this.loadData()
    },
    apiPath() {
      return '/filing'
    },
    // editFormChange(data) {
    //   this.configForm = data
    //   this.editForm(this.configForm)
    // },
    // remove(id) {
    //   this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
    //     confirmButtonText: '确定',
    //     cancelButtonText: '取消',
    //     type: 'warning'
    //   }).then(() => {
    //     this.loading = true
    //     this.$http.post(this.apiPath() + '/delete', {Id: id})
    //         .then(({data}) => {
    //           if (data.success) {
    //             this.$message.success('删除成功！')
    //             this.loadData()
    //           } else {
    //             this.$message.error(data.message)
    //             this.loading = false
    //           }
    //         })
    //   })
    // },
    // showArchiveDetail(id, type) {
    //   this.$http.post('filingTask/queryArchiveDetail', {id: id, type: type}).then(({data}) => {
    //     if (data && data.success) {
    //       this.archiveData = data.data
    //     }
    //   })
    //   this.datailVisible = true
    // },
    // close() {
    //   this.archiveData = {}
    //   this.datailVisible = false
    // }
  },
  filters: {
    examineStateFilter(v) {
      if (v === '0') return '待审核中'
      if (v === '1') return '审核通过'
      if (v === '2') return '申请被驳回'
    },
  },
}
</script>


