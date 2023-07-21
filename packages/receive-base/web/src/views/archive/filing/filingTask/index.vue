<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="form"></config-form>
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column prop="taskName" label="审批环节名称" align="center" show-overflow-tooltip/>
          <el-table-column prop="examineStatus" label="审批状态" align="center" width="100px" show-overflow-tooltip>
            <template slot-scope="scope">
              <div :style="{'color':
                             scope.row.examineStatus === '0'?'orange':
                             scope.row.examineStatus === '1'?'green':
                             scope.row.examineStatus === '2'?'red':'black'}">
                {{ scope.row.examineStatus | examineStateFilter(scope.row.examineState) }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="approver" label="审批人" align="center" width="140px" show-overflow-tooltip/>
          <el-table-column prop="suggestion" label="审核意见" align="center" width="140px"/>
          <el-table-column prop="currentTask" label="当前审批环节" align="center" width="140px"/>
          <el-table-column prop="taskNumber" label="审批环节数" align="center" width="100px"/>
          <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="showExamine(scope.row)"
                         v-if="scope.row.examineStatus === '0' && scope.row.approverId === personId">审 核
              </el-button>
              <!--              <el-button type="text" size="small" @click="showDetail(scope.row.id, 'TASK')">档案详情</el-button>-->
              <el-button type="text" size="small"
                         @click="$router.push({path: '/archive/filing/filingRecord/detail', query: {id: scope.row.id,type:'TASK'}})">
                档案详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1}, {processType: 'FILING'})"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>

    <el-dialog title="审核" :visible.sync="examineDialogVisible" width="40%" :before-close="beforeCloseForExamine">
      <div style="margin-top:15px;">
        <el-form :model="examineContent" ref="examineContent" label-width="100px">
          <el-form-item label="审核意见:" prop="suggestion">
            <el-input type="textarea" :rows="2" v-model="examineContent.suggestion">
            </el-input>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="examine('1')">通 过</el-button>
        <el-button type="warning" @click="examine('0')">驳 回</el-button>
      </div>
    </el-dialog>

    <el-dialog title="档案详情" :visible.sync="detailVisible" width="80%" center>
      <el-table :data="archiveData" border>
        <el-table-column label="排序" type="index" align="center"/>
        <el-table-column prop="title" label="题名" align="center" show-overflow-tooltip/>
        <el-table-column prop="archiveCode" label="档号" align="center" show-overflow-tooltip width="100px"/>
        <el-table-column prop="fondName" label="全宗名" align="center" show-overflow-tooltip width="100px"/>
        <el-table-column prop="categoryName" label="门类名" align="center" show-overflow-tooltip width="100px"/>
        <el-table-column prop="vintages" label="年度" align="center" show-overflow-tooltip width="80px"/>
        <el-table-column prop="saveTerm" label="保管期限" align="center" width="80px"/>
        <el-table-column prop="ajdh" label="案卷档号" align="center" show-overflow-tooltip width="140px"/>
        <el-table-column label="操作" width="100" header-align="center" align="center">
          <template slot-scope="scope">
            <yuanwen :row="scope.row"></yuanwen>
            <!--                      <el-button type="text" size="small" @click="showYuanWen(scope.row.id)">原 文</el-button>-->
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@/util/indexMixin'
import Yuanwen from "../yuanwen"

export default {
  data() {
    return {
      data: [],
      configForm: {},
      examineDialogVisible: false,
      examineContent: {},
      personId: '',
      detailVisible: false,
      archiveData: [],
    }
  },
  components: {Yuanwen, ConfigForm},
  mixins: [indexMixin],
  methods: {
    beforeCloseForExamine() {
      this.examineContent.suggestion = ''
      this.examineDialogVisible = false
    },
    $init() {
      this.loadData({processType: 'FILING'})
      this.getCurrentPerson()
    },
    apiPath() {
      return '/receive/online'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
    showExamine(row) {
      this.examineContent = row
      this.examineDialogVisible = true
    },
    examine(type) {
      this.$http.post(this.apiPath() + '/examine', {
        type: type,
        id: this.examineContent.id,
        suggestion: this.examineContent.suggestion
      }).then(({data}) => {
        if (data.success) {
          if (this.examineContent.currentTask === this.examineContent.taskNumber && type === "1") {
            this.$message.success('入库成功，请到管理库查看详情')
          } else {
            this.$message.success(type === '1' ? '审核通过' : '驳回成功')
          }
          this.examineDialogVisible = false
          this.loadData({processType: 'FILING'})
        } else {
          this.$message.error(data.message)
        }
      })
    },
    async getCurrentPerson() {
      const {data} = await this.$http.post('/login/info')
      if (data.success) {
        this.personId = data.data.id
      } else {
        this.$message.error(data.message)
      }
    },
    showDetail(id, type) {
      this.$http.post(this.apiPath() + '/queryArchiveDetail', {id: id, type: type}).then(({data}) => {
        if (data && data.success) {
          this.archiveData = data.data
        }
      })
      this.detailVisible = true
    },
    showYuanWen(id) {

    }
  },
  filters: {
    examineStateFilter(v) {
      if (v === '0') return '待审核'
      if (v === '1') return '已通过'
      if (v === '2') return '已驳回'
    },
  },
}
</script>


