<template>
    <section>
        <nac-info back title="任务批次详情">
            <el-input v-model="searchForm.taskName" style="width: 120px" placeholder="请输入任务名称" clearable/>
            <el-button type="primary" @click="$init" size="mini">搜 索</el-button>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
                <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
                    <column-index :page="page"/>
                    <el-table-column label="全宗" prop="fondName" show-overflow-tooltip align="center"/>
                    <el-table-column label="分类" prop="categoryCode" show-overflow-tooltip align="center"/>
                    <el-table-column label="机构或问题" prop="orgCode" show-overflow-tooltip align="center" min-width="120"/>
                    <el-table-column label="档号" prop="archiveCode" show-overflow-tooltip align="center"/>
                    <el-table-column label="题名" prop="title" show-overflow-tooltip align="center"/>
                    <el-table-column label="关键词" prop="keyWords" show-overflow-tooltip align="center"/>
                    <el-table-column label="年度" prop="year" show-overflow-tooltip align="center"/>
                    <el-table-column label="发起人" prop="sourcePersonName" show-overflow-tooltip align="center"/>
                    <el-table-column label="发起时间" prop="sourceDate"
                                     :formatter="(r,c,cell)=>$moment(cell).format('YYYY-MM-DD')"
                                     show-overflow-tooltip
                                     align="center"/>
                    <el-table-column label="状态" prop="status" align="center">
                        <template v-slot="scope">
                            {{ scope.row.status|dict({0: "待审核", 1: "通过", '-1': "未通过"}) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" header-align="center" width="60" fixed="right">
                        <template v-slot="scope">
                            <el-link type="danger" v-if="scope.row.status==='0'" size="mini" @click="audit(scope.row)">审 核</el-link>
                            <el-link type="danger" v-else size="mini" @click="audit(scope.row)">查 看</el-link>
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
            audit(row) {
                this.$router.push({
                    path: '/archive/task/batch/detail/audit',
                    query: {
                        id: row.id,
                        batchId: row.batchId,
                        taskId: this.$route.query.taskId,
                        taskStatus: this.$route.query.taskStatus
                    }
                })
            },
            async loadData() {
                this.loading = true
                const {data} = await this.$post('/batch/batchDetailPage', {
                    id: this.$route.query.batchId,
                    batchType: this.$route.query.taskType
                })
                this.data = data.data.data
                this.page.index = data.data.start / data.data.size + 1
                this.page.size = data.data.size
                this.page.total = data.data.total
                this.loading = false
            },
            $init() {
                this.loadData()
            }
        }
    }
</script>
