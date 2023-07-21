<template>
    <table-index :fields="fields"
                 path="deliveryBatchDetailItem"
                 :defaultSearchForm="defaultSearchForm"
                 :edit="false"
                 :delete="false"
                 :insert="false"
                 ref="table"
                 style="height: 70vh;display: flex;flex-direction: column">
        <template v-slot:search-$btns="scope">
            <el-button type="primary" @click="">返回</el-button>
        </template>
    </table-index>
</template>

<script>
    export default {
        name: "index",
        data() {
            return {
                fields: {
                    archiveCode: {
                        label: '档号',
                        component: 'text',
                        route: true,
                        search: true,
                        align: 'center',
                        width: 200,
                    },
                    title: {
                        label: '题名',
                        route: true,
                        search: true,
                        align: 'center',
                    },
                    year: {
                        label: '年度',
                        search: false,
                        edit: false,
                        width: 100
                    },
                    deliveryTime: {
                        label: '出库时间',
                        search: false,
                        edit: false,
                    },
                },
                defaultSearchForm: {deliveryId: this.$route.query.batchId},
            }
        },
    }
</script>

<style scoped>

</style>