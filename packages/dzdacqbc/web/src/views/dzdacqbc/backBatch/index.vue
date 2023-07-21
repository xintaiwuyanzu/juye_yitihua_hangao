<template>
    <table-index :fields="fields" path="/backBatch" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm" :edit="false" back :delete="false">
        <template v-slot:table-$btns="scope">
            <el-button type="text" size="mini" width="60" @click="startWork(scope.row)" v-show="scope.row.status === '0'">开始执行</el-button>
            <el-button type="text" size="mini" width="60" @click="deleteRow(scope.row)">删 除</el-button>
        </template>

    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {
                        prop: 'batchCode',
                        label: '批次编号',
                        align: 'center',
                        component: 'text',
                        route: true,
                        routerPath: '/dzdacqbc/backBatch/detail',
                        queryProp: this.routeQuery,
                        width: '180',
                        edit: false,
                    },
                    {
                        prop: 'batchName',
                        label: '批次名称',
                        search: true,
                        required: true
                    },
                    {
                        prop: 'strategyId', label: '备份策略', width: 200,
                        fieldType: 'select',
                        url: '/strategy/page',
                        required: true,
                        labelKey: 'strategyName',
                        params: {page: false}
                    },
                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 160, edit: false},
                    {prop: 'backCount', label: '备份数量', width: 130, edit: false},
                    {
                        prop: 'status', label: '备份状态', width: 100, component: 'tag', showTypeKey: 'show', edit: false,
                        mapper: {
                            '0': {label: '未完成', show: 'danger'},
                            '1': {label: '已完成', show: 'success'},
                        },
                    },
                    {
                        prop: 'isExpire', label: '是否过期', width: 100, component: 'tag', showTypeKey: 'show', edit: false,
                        mapper: {
                            '0': {label: '未过期', show: 'success'},
                            '1': {label: '已过期', show: 'info'}
                        },
                    },
                ],
                searchForm: {strategyId: this.$route.query.strategyId},
                id: '',
                categories: []
            }
        },
        watch: {},
        methods: {
            routeQuery(row) {
                return {batchId: row.id}
            },
            startWork(row) {
                this.$http.post('/backBatch/startWork', {batchId: row.id})
                    .then(({data}) => {
                        row.status = "备份中"
                        if (data.success) {
                            this.$message.success('正在备份，请稍后！')
                        } else {
                            this.$message.error(data.message)
                        }
                    })

            },
            deleteRow(row) {
                this.$confirm('此操作将删除批次、批次详情以及对应备份文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    const {data} = await this.$http.post('backBatch/delDetail', {batchId: row.id})
                    //console.log(data.data)
                    if (data && data.success) {
                        this.$message.success('删除成功')
                        this.$refs.table.reload()
                    } else {
                        this.$message.error('删除失败')
                    }

                })
            },
            detail(row) {
                this.$router.push({
                    path: '/dzdacqbc/backBatch/detail',
                    query: {batchId: row.id}
                })
            },
            async changeFond(form) {
                this.$set(form, 'classId', '')
                form.categoryId = ''
                this.categories = []
                await this.editShow(form)
            },
            async editShow(form) {
                if (form.fondId) {
                    const {data} = await this.$http.post('/manage/category/pageByBusinessId', {businessId: form.fondId})
                    if (data.success) {
                        this.categories = data.data
                    } else {
                        this.$message.error(data.message)
                    }
                } else {
                    this.$set(form, 'classId', '')
                }
            }
        }
    }
</script>