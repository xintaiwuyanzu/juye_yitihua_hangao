<template>
  <section>
    <section style="height: 10%;">
        <div slot="header">
          <strong>任务详情</strong>
          <el-button type="info" size="mini" style="float: right; margin-right: 5px; margin-top: 5px" @click="$router.back()">返回</el-button>
        </div>
        <el-form :model="task" inline label-width="240px" style="margin-top: 20px">
          <el-row>
            <el-col :span="8">
              <el-form-item label="任务名称：">
                <el-tooltip class="item" effect="dark" :content="task.taskName" placement="top">
                <el-tag>{{task.taskName | valueFilter}}</el-tag>
                </el-tooltip>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="总检测档案数：">
                <el-tag>{{task.allNumber}}</el-tag>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="问题档案数：">
                <el-tag>{{task.problemNumber}}</el-tag>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
    </section>

    <section style="height: 90%;">
      <div class="index_main" v-loading="loading">
        <div slot="header">
          <strong>档案详情</strong>
        </div>
        <config-form ref="form"></config-form>
        <div class="table-container">
          <el-table :data="data" border height="450">
            <el-table-column label="排序" type="index" align="center"/>
            <el-table-column prop="archiveTitle" label="档案题名" align="center" show-overflow-tooltip/>
            <el-table-column prop="archiveCode" label="档号" align="center" show-overflow-tooltip/>
            <el-table-column prop="problemDetail" label="存在问题" align="center" show-overflow-tooltip/>
          </el-table>
        </div>
        <el-pagination
                @current-change="index=>loadData({pageIndex:index-1, taskId: task.id})"
                :current-page.sync="page.index"
                :page-size="page.size"
                layout="total, prev, pager, next"
                :total="page.total">
        </el-pagination>
      </div>
    </section>
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
      taskDetail: {},
      task: this.$route.query,
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {
      console.log('index.task is ===>', this.task)
      this.$refs.form.getTask(this.task)
      this.loadData({taskId: this.task.id})
    },
    apiPath() {
      return '/cqbcTaskArchiveDetail'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
  },
  filters: {
    valueFilter(value) {
      if (!value) return ''
      if (value.length > 20) {
        return value.slice(0,20) + '...'
      }
      return value
    }
  }
}
</script>


