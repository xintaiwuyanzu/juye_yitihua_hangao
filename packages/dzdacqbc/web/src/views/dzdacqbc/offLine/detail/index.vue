<template>
    <table-index title="批次备份详情" :fields="fields" path="/backBatchDetail" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm" :edit="false" :insert="false" :delete="false" back>
    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {prop: 'archiveCode', label: '档案编号',},
                    {
                        prop: 'timing',
                        label: '档案名称',
                        search: true
                    },
                    {prop: 'fond_code', label: '全宗号', width: 140},
                    {prop: 'categoryCode', label: '门类号', width: 140},
                    {prop: 'nd', label: '年度', width: 100},
                    {prop: 'backPath', label: '备份地址'},
                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 160, edit: false},

                ],
                searchForm: {batchId: this.$route.query.batchId, backType:'lx'},
                id: '',
            }
        },
        watch: {},
        methods: {}
    }
</script>