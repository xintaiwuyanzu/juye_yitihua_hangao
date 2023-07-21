<template>
    <table-index title="入库检测详情" :fields="fields" path="tempToFormal/batch/detail" :insert="false"
                 :edit="false" :delete="false"
                 :default-search-form="defaultSearchForm" back ref="table">
        <template v-slot:search="form">
            <el-form-item label="四性检测状态：" prop="testStatus">
                <el-select v-model="form.testStatus" clearable>
                    <el-option label="通过" value="1"></el-option>
                    <el-option label="不通过" value="0"></el-option>
                    <el-option label="未检测" value="-1"></el-option>
                    <el-option label="重复" value="2"></el-option>
                </el-select>
            </el-form-item>
        </template>
        <template v-slot:table-$btns="scope">
            <el-button type="text" size="mini" width="50" @click="toTestReport(scope.row)">检测报告</el-button>
        </template>

        <el-table-column prop="archiveCode" after="categoryCode" label="档号" show-overflow-tooltip align="center"
                         width="180">
            <template v-slot="{row}">
                <el-button type="text" @click="routeDetail( row)">{{ row.archiveCode }}</el-button>
            </template>
        </el-table-column>

        <el-dialog width="80%" title="检测报告" :visible.sync="dialogShow" append-to-body>
            <el-table :data="data" border class="table-container" v-loading="dialogLoading">
                <el-table-column prop="order" label="排序" width="80" header-align="center" align="center">
                    <template v-slot="scope">
                        {{ scope.$index + 1 }}
                    </template>
                </el-table-column>
                <el-table-column prop="createDate" label="检测时间" header-align="center" align="center"
                                 show-overflow-tooltip
                                 width="150px">
                    <template v-slot="scope">
                        {{ scope.row.createDate|datetime }}
                    </template>
                </el-table-column>
                <el-table-column prop="testRecordType" label="检测类型" header-align="center" align="center"
                                 show-overflow-tooltip
                                 width="100px">
                    <template v-slot="scope">
                        {{ scope.row.testRecordType|dict('archive.testType') }}
                    </template>
                </el-table-column>
                <el-table-column prop="status" label="检测结果" header-align="center" align="center" show-overflow-tooltip
                                 width="80px">
                    <template v-slot="scope">
                        {{ scope.row.status|dict({'0': '未检测', '1': '通过', '2': '不通过'}) }}
                    </template>
                </el-table-column>

                <el-table-column prop="testName" label="检测项" header-align="center" align="center"
                                 show-overflow-tooltip/>
                <el-table-column prop="testResult" label="说明" header-align="center" align="center" show-overflow-tooltip
                                 width="220px"/>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="dialogShow = false">取 消</el-button>
            </div>
        </el-dialog>

    </table-index>
</template>
<script>

    export default {
        data() {
            return {
                loading: false,
                dialogLoading: false,
                dialogShow: false,
                data: [],
                //新格式
                fields: [
                    {prop: 'fondCode', label: '全宗号', align: 'center', width: '120'},
                    {prop: 'categoryCode', label: '分类号', align: 'center', width: '120'},
                    {prop: 'title', label: '题名', align: 'center', search: true},
                    {prop: 'year', label: '年度', width: '120'},
                    {prop: 'createDate', label: '检测开始时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: '140'},
                    {
                        prop: 'testStatus', label: '四性检测', width: 120, component: 'tag', showTypeKey: 'show',
                        mapper: {
                            '0': {label: '不通过', show: 'danger'},
                            '1': {label: '通过', show: 'success'},
                            '-1': {label: '未执行', show: 'warning'},
                            '2': {label: '重复', show: 'danger'}
                        },
                    },
                ],
                //离线接收批次信息
                offLineRecord: {},
                //是否显示入库按钮
                showRk: false,
                //是否显示编辑目录,删除 按钮
                showEditButton: false,
                editDialogShow: false,
                form: {},

                defaultParams: {}
            }
        },
        methods: {
            async $init() {

            },
            defaultSearchForm() {
                return {batchId: this.$route.query.businessId ? this.$route.query.businessId : this.$route.query.id}
            },
            //跳转到四性检测报告页面
            toTestReport(row) {
                this.$router.push({
                    path: '/testrecorditem',
                    query: {
                        batchId: row.batchId,
                        formDataId: row.formDataId,
                        recordId: row.lastTestRecordId
                    }
                })
            },
            routeDetail(row) {
                this.$router.push({
                    path: '/archive/metadataAndFileDetail',
                    query: {
                        formDataId: row.formDataId,
                        formDefinitionId: row.formDefinitionId,
                        refType: 'archive',
                        groupCode: 'default',
                        watermark: false
                    }
                })
            },
            appendParams(form) {
                this.$set(form, 'businessId', this.batchId)
            },
        },
        computed: {
            batchId() {
                return this.$route.query.id || this.$route.query.businessId
            },
            isProcess() {
                return !!this.$route.query.taskId
            }

        }
    }
</script>
