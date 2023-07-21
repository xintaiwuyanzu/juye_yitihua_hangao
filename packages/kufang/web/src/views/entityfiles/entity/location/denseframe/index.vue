<template>
    <table-index :title="areaName1"
                 :fields="fields"
                 path="position"
                 :default-insert-form="defaultInsertForm"
                 :default-search-form="defaultInsertForm"
                 back>
    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fields: {
                    bookCaseNo: {
                        label: '密集架编号',
                        align: 'center',
                        component: 'text',
                        route: true,
                        routerPath: '/entityfiles/entity/location/denseframe/detail',
                        search: true,
                        required: true,
                        queryProp: ['id', 'caseName','areaName'],
                    },
                    caseName: {
                        label: '密集架名称',
                        align: 'center',
                        required: true,
                        search: true,
                    },
                    columnNum: {
                        label: '密集架面数',
                        align: 'center',
                        required: true,
                        fieldType: 'select', mapper: {'1': '单面', '2': '双面'},
                        width: 150,
                    },
                    columnNo: {
                        label: '密集架节数',
                        align: 'center',
                        required: true,type: 'number',  width: 150
                    },
                    layer: {label: '密集架层数', align: 'center', type: 'number', required: true, width: 150},
                    createDate: {
                        label: '创建时间',
                        edit: false,
                        search: false,
                        dateFormat: "YYYY-MM-DD HH:mm:ss",
                        width: 150
                    },
                },
                defaultInsertForm: {areaId:this.$route.query.id,locId: this.$route.query.kufangId, areaId:this.$route.query.id},
                areaName: '',
                areaName1: '',
                kufangId: '',
                areaId: '',
            }
        },
        mounted() {
            this.areaId = this.$route.query.id
            this.kufangId = this.$route.query.kufangId
            this.areaName = this.$route.query.areaName
            this.areaName1 = this.$route.query.areaName + "密集架管理"

        }
    }
</script>
