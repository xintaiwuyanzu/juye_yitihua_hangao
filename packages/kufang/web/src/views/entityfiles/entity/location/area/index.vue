<template>


    <table-index :title=houseName :fields="fields"
                 path="kufangArea"
                 :edit="true"
                 :delete="true"
                 :insert="true"
                 :defaultInsertForm="defaultInsertForm"
                 :defaultSearchForm="defaultInsertForm"
                 ref="table" back></table-index>

</template>
<script>
    export default {
        name: "index",
        data() {
            return {
                fields: {
                    areaName: {
                        label: '区域名称',
                        align: 'center',
                        component: 'text',
                        route: true,
                        routerPath: '/entityfiles/entity/location/denseframe',
                        search: true,
                        required: true,
                        queryProp: ['id', 'kufangId', 'areaName']
                    },
                    areaCode: {label: '区号', width: "80", required: true},
                    createDate: {label: '创建时间', edit: false, search: false, dateFormat: "YYYY-MM-DD HH:mm:ss"}
                },
                defaultInsertForm: {kufangId: this.$route.query.id},
                houseName: '',
                houseName1: '',
                kufangId: ''
            }
        },
        mounted() {
            this.kufangId = this.$route.query.id
            this.houseName1 = this.$route.query.name
            this.houseName = this.$route.query.name + "区域管理"
        }
    }
</script>
