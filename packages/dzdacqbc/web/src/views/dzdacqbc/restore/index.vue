<template>
    <table-index :fields="fields" path="/restore" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm" :edit="false" :delete="false" :insert="false">
    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {
                        prop: 'recordName',
                        label: '恢复记录名称',
                        required: true
                    },
                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 170, edit: false},
                    {prop: 'archiveCode', label: '档案编号', width: 230, search: true},
                    {
                        prop: 'timing',
                        label: '档案名称',
                        search: true,width: 230,
                    },
                    {prop: 'fond_code', label: '全宗号', width: 80},
                    {prop: 'categoryCode', label: '门类号', width: 80},
                    {prop: 'backPath', label: '备份地址', width: 320},
                    {
                        prop: 'reType', label: '备份类型', width: 100, component: 'tag', showTypeKey: 'show', edit: false,
                        mapper: {
                            'bj': {label: '报警恢复', show: 'warning'},
                            'bf': {label: '备份恢复', show: 'success'}
                        },
                    },

                ],
                searchForm: {strategy: this.$route.query.strategy},
                id: '',
                categories: []
            }
        },
        watch: {},
        methods: {}
    }
</script>