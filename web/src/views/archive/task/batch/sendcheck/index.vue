<template>
    <section>
        <nac-info>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
              <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
                <column-index :page="page"/>
                <el-table-column label="任务名称" prop="taskName" width="250" show-overflow-tooltip align="center"/>
                <el-table-column label="任务类型" prop="taskType" align="center">
                  <template v-slot="scope">
                    {{
                      scope.row.taskType|dict({'SEND_CHECK': '移交', 'JD': '鉴定', 'IMP': '导入', 'ARCHIVE': '归档'})
                    }}
                  </template>
                </el-table-column>
                <el-table-column  label="发起人" prop="sourcePersonName" show-overflow-tooltip
                                 align="center"/>
                <el-table-column  label="发起时间" prop="sourceDate"
                                  dateFormat="YYYY-MM-DD HH-mm-ss"
                                 show-overflow-tooltip
                                 align="center"/>
                <el-table-column  label="接收人" prop="targetPersonName" align="center"/>
                <el-table-column label="状态" prop="status" align="center">
                  <template v-slot="scope">
                    {{ scope.row.status|dict({0: "待处理", 1: "办结", '-1': "未通过"}) }}
                  </template>
                </el-table-column>
                <el-table-column label="操作" align="center" header-align="center" width="120">
                  <template v-slot="scope">
                    <el-link type="danger" size="mini" @click="detail(scope.row)">查看详情</el-link>
                  </template>
                </el-table-column>
              </el-table>
            </div>
            <page :page="page"/>
        </div>
    </section>
</template>
<script>
    import indexMixin from '@/util/indexMixin'

    /**
     * 批次列表
     */
    export default {
        mixins: [indexMixin],
        data() {
            return {
                loading: false,
                searchForm: {
                    formatName: "",
                },
                data: []
            }
        },
        methods: {
            async loadData() {
                this.loading = true
                const {data} = await this.$post('/archiveTask/page', Object.assign(this.searchForm,{taskType: 'SEND_CHECK'}))
                this.data = data.data.data
                this.page.index = data.data.start / data.data.size + 1
                this.page.size = data.data.size
                this.page.total = data.data.total
                this.loading = false
            },
            $init() {
                this.loadData()
            },
          detail(row) {
            switch (row.taskType) {
              case "SEND_CHECK":
                this.$router.push({
                  path: '/archive/task/batch/sendcheck/detail',
                  query: {
                    batchId: row.batchId,
                    taskId: row.id,
                    taskStatus: row.status,
                    taskType: row.taskType
                  }
                })
                break;
            }
          },
        }
    }
</script>
