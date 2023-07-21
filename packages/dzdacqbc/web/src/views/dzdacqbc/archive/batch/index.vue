<template>
    <table-index :fields="fields"
                 path="deliveryBatch"
                 :defaultSearchForm="defaultSearchForm"
                 :edit="false"
                 :delete="false"
                 :insert="false"
                 ref="table"
                 style="height: 70vh;display: flex;flex-direction: column">
        <template v-slot:table-$btns="scope">
            <el-button type="text" width="90" @click="detail(scope)">详情</el-button>
            <el-button type="text" @click="toHistory(scope)">流转历史</el-button>
        </template>
        <el-dialog :visible.sync="fileListDialog" title="查看流转历史" width="80%">

            <div slot="footer" class="dialog-footer">
                <el-button @click="fileListDialog=false">关闭</el-button>
            </div>
        </el-dialog>
    </table-index>
</template>

<script>
    export default {
        name: "index",
        data() {
            return {
                fields: {
                    batchName: {
                        label: '名称', component: 'text', route: true,
                        routerPath: './batch/detail',
                        queryProp: [],
                        search: true, align: 'center', width: 300,
                    },
                    deliveryPerson: {
                        label: '出库人',
                        search: false,
                        edit: false,
                    },
                    detailNum: {
                        label: '出库条数',
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
                defaultSearchForm: {},
                fileListDialog: false,
            }
        },
        methods: {
            detail(scope) {
                this.$router.push({path: "./batch/detail", query: {batchId: scope.row.id}})
            },
            toHistory() {
                this.fileListDialog = true;
            },
        },
    }
</script>

<style scoped>

</style>