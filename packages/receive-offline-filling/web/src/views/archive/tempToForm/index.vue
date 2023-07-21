<template>
    <table-index title="入库检测记录"
                 :fields="fields"
                 path="tempToFormal/batch"
                 :insert="false"
                 :edit="false"
                 :delete="false"
                 ref="table"
                 :defaultSearchForm="defaultSearchForm">
<!--        <template slot-scole='form' slot='search-$btns'>
            <imp-cataloglist @loadData="$refs.table.reload()" ref="imp" style="display: inline-block;"/>
        </template>-->
        <el-table-column fixed="right" v-slot="{row}" label="操作" align="center" width="150px">
            <el-button type="text" @click="report(row)" size="mini" width="60">{{ reportTitle }}</el-button>
        </el-table-column>
        <!-- 接收报告 -->
        <el-dialog :visible.sync="dialogVisible" :title="reportTitle" width="40%"
                   :close-on-click-modal=true
                   :modal-append-to-body=false
                   :destroy-on-close=true>
            <el-form ref="reportForm" :model="reportForm" label-width="160px">
                <el-form-item label="接收目录数据总数">
                    <el-input v-model="reportForm.detailNum" disabled/>
                </el-form-item>
                <el-form-item label="四性检测通过数量">
                    <el-input v-model="reportForm.fourSexTestSucessNum" disabled/>
                </el-form-item>
                <el-form-item label="四性检测不通过数量">
                    <el-input v-model="reportForm.fourSexTestFalseNum" disabled/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible=false" type="info">取消</el-button>
            </div>
        </el-dialog>
    </table-index>
</template>
<script>
import impCataloglist from "../receive/offline/record/impCataLog";
    export default {
      components:{impCataloglist},
        data() {
            return {
                defaultSearchForm: {},
                dialogVisible: false,
                loading: false,
                secret: [],
                reportTitle: '接收报告',
                reportForm: {},
                //report:{},
                dataBatchParams: {page: false, batchType: 0},
                fields: [
                    {
                        prop: 'batchName',
                        label: '记录名称',
                        align: 'center',
                        search: true,
                        component: 'text',
                        route: true,
                        routerPath: '/archive/tempToForm/detail',
                    },
                    {
                        prop: 'status', label: '目录状态', width: '130', align: 'center',
                        component: 'tag',
                        mapper: {'-1': '检测失败', '0': '执行中', '1': '已检测'}
                    },
                    {prop: 'startDate', label: '目录接收开始时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '150'},
                    {prop: 'endDate', label: '目录接收结束时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '150'},
                    {prop: 'detailNum', label: '目录数量', width: '100'},
                    {
                        prop: 'testStatus', label: '四性检测', width: 100, component: 'tag', showTypeKey: 'show',
                        mapper: {
                            '0': {label: '不通过', show: 'danger'},
                            '1': {label: '通过', show: 'success'},
                            '-1': {label: '未执行'},
                        },
                    },
                    {prop: 'testTrueNum', label: '四性检测成功数量', width: 120},
                ],
            }
        },
        methods: {
            //接收报告
            async report(row) {
                this.dialogVisible = true
                const {data} = await this.$post('/tempToFormal/batch/getReport', {
                    batchId: row.id
                })
                if (data.success) {
                    this.reportForm = data.data
                }
            },
        },
        computed: {},
        beforeRouteLeave(to, from, next) {
            this.dialogVisible = false
            next()
        }
    }
</script>
<style lang="scss">
    .default .el-button--info:hover, .default .el-button--info:focus {
        background-color: rgba(140, 140, 140, 0.8);
        border-color: rgba(140, 140, 140, 0.8);
        color: white;
    }

    .red .el-button--info:hover, .red .el-button--info:focus {
        background-color: rgba(140, 140, 140, 0.8);
        border-color: rgba(140, 140, 140, 0.8);
        color: white;
    }

    .green .el-button--info:hover, .green .el-button--info:focus {
        background-color: rgba(140, 140, 140, 0.8);
        border-color: rgba(140, 140, 140, 0.8);
        color: white;
    }
</style>
