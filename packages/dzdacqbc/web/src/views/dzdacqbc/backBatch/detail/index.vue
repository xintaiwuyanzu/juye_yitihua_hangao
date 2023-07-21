<template>
    <table-index title="批次备份详情" :fields="fields" path="/backBatchDetail" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm" :edit="false" :insert="false" :delete="false" back>
        <template v-slot:table-$btns="scope">
            <el-button type="text" size="mini" width="60" @click="backData(scope.row)" v-show="scope.row.isExpire === '0'">数据恢复</el-button>
        </template>

    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {prop: 'archiveCode', label: '档案编号', width: 230,},
                    {
                        prop: 'timing',
                        label: '档案名称',
                        search: true
                    },
                    {prop: 'fond_code', label: '全宗号', width: 80},
                    {prop: 'categoryCode', label: '门类号', width: 80},
                    {prop: 'nd', label: '年度', width: 80},
                    {prop: 'backPath', label: '备份地址', width: 420},
                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 170, edit: false},
                    {
                        prop: 'isExpire', label: '是否过期', width: 90, component: 'tag', showTypeKey: 'show', edit: false,
                        mapper: {
                            '0': {label: '未过期', show: 'success'},
                            '1': {label: '已过期', show: 'info'}
                        },
                    },
                    {prop: 'backCount', label: '恢复次数', width: 70},
                ],
                searchForm: {batchId: this.$route.query.batchId},
                id: '',
            }
        },
        watch: {},
        methods: {
            backData(row){
                this.$confirm('此操作将备份数据恢复到长期保存库和正式库, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(async () => {
                    this.$http.post('/backBatchDetail/dataRecovery', {detailId: row.id})
                        .then(({data}) => {
                            if (data.success) {
                                this.$message.success('正在恢复，请稍后！')
                            } else {
                                this.$message.error(data.message)
                            }
                        })

                })

            },
        }
    }
</script>