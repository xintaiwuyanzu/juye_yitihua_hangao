<template>
    <table-index title="批量添加详情" :fields="fields" path="receive/offline/detail" :insert="false"
                 :edit="false" :delete="false"
                 :default-search-form="defaultSearchForm" back ref="table">
        <template v-slot:search="form">
            <el-form-item label="挂接状态：" prop="hookStatus">
                <el-select v-model="form.hookStatus" clearable>
                    <el-option label="已挂接" value="1"></el-option>
                    <el-option label="未挂接" value="0"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="检测状态：" prop="testStatus">
                <el-select v-model="form.testStatus" clearable>
                    <el-option label="通过" value="1"></el-option>
                    <el-option label="不通过" value="0"></el-option>
                    <el-option label="未检测" value="-1"></el-option>
                    <el-option label="重复" value="2"></el-option>
                </el-select>
            </el-form-item>
        </template>
        <template v-slot:search-$btns>
            <process-container
                    v-if="!isProcess"
                    v-show="showRk"
                    title="提交入库申请流程"
                    processType="offline-filling"
                    :business-id="batchId"
                    btn-text="入库申请"
                    ref="pro"
                    @saved="notifyResult">
            </process-container>
            <task-container v-if="isProcess" :task-instance-id="$route.query.taskId">
                <template v-slot:sendNext="params">
                    <send-next @sendSaved="sendSaved">
                    </send-next>
                </template>
                <template v-slot:endProcess="params">
                    <end-process @saveEnd="saveEnd" :beforeOpen="appendParams">
                    </end-process>
                </template>
            </task-container>
            <el-button type="primary" @click="toTest">重新检测</el-button>
        </template>
        <template v-slot:table-$btns="scope">
            <el-button type="text" size="mini" width="50" @click="editForm(scope.row)">编辑目录
            </el-button>
            <el-button type="text" size="mini" width="50" @click="toTestReport(scope.row)">检测报告</el-button>
            <el-button type="text" size="mini" width="30" @click="deleteDetail(scope.row)">删除
            </el-button>
        </template>
        <el-table-column prop="archiveCode" after="fondCode" label="档号" show-overflow-tooltip align="center"
                         width="140">
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
        <el-dialog width="80%" title="编辑目录" :visible.sync="editDialogShow" append-to-body>
            <el-form ref="form" :model="form" label-width="100" inline v-if="formFieldList.length>0">
                <el-form-item :prop='field.code' :label='field.name' label-width="100px" :key="field.id"
                              v-for="field in formFieldList">
                    <select-dict v-model="form[field.code]" :type='field.meta.dict' clearable
                                 v-if="field.meta&&field.meta.dict"
                                 :style="{width: field.remarks+'px'}"/>
                    <el-input v-model="form[field.code]" :style="{width: field.remarks+'px'}" v-else/>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="editDialogShow = false">关 闭</el-button>
                <el-button type="primary" @click="save">保存</el-button>
            </div>
        </el-dialog>
    </table-index>
</template>
<script>
    import {endProcess, processContainer, sendNext, taskContainer} from '@dr/process/src/lib'

    /**
     * 批次列表
     */
    export default {
        components: {processContainer, taskContainer, sendNext, endProcess},
        data() {
            return {
                loading: false,
                dialogLoading: false,
                dialogShow: false,
                data: [],
                //新格式
                fields: [
                    {prop: 'fondCode', label: '全宗号', align: 'center', width: '130'},
                    {prop: 'title', label: '题名', align: 'center', search: true},
                    // {prop: 'archiveCode', label: '档号', width: '120',align: 'center',column: true},
                    {prop: 'year', label: '年度', width: '60'},
                    {prop: 'hookStartTime', label: '挂接开始时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: '140'},
                    {prop: 'hookEndTime', label: '挂接结束时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: '140'},
                    {prop: 'hookNum', label: '挂接数量', width: '70'},
                    {
                        prop: 'hookStatus', label: '挂接状态', width: '70', component: 'tag', showTypeKey: 'show',
                        mapper: {
                            '0': {label: '未挂接', show: 'warning'},
                            '1': {label: '已挂接', show: 'success'}
                        },
                    },
                    {
                        prop: 'testStatus', label: '四性检测', width: 80, component: 'tag', showTypeKey: 'show',
                        mapper: {
                            '0': {label: '不通过', show: 'danger'},
                            '1': {label: '通过', show: 'success'},
                            '-1': {label: '未执行', show: 'warning'},
                            '2': {label: '重复', show: 'danger'}
                        },
                    },
                ],
                //是否显示入库按钮
                showRk: false,
                //是否显示编辑目录,删除 按钮
                showEditButton: false,
                editDialogShow: false,
                //先写死
                formFieldList: [
                    {
                        code: "SAVE_TERM", id: "1b8036b0-ce91-4f7b-9aea-85184d19d111", labelWidth: 100,
                        meta: {}, name: "保管期限", remarks: "200", type: "input"
                    },
                    {
                        code: "TITLE", id: "78cf137c-14a6-4a56-9bbe-0b1366c900a2", labelWidth: 100,
                        meta: {}, name: "题名", remarks: "510", type: "input"
                    },
                    {
                        code: "VINTAGES", id: "a254dfdc-394c-4009-b2d3-9f831b817eb6", labelWidth: 100,
                        meta: {}, name: "年度", remarks: "200", type: "input"
                    },
                    {
                        code: "ARCHIVE_CODE", id: "f17b89d2-ec6a-42a0-ae01-fbd86fa5f3f4", labelWidth: 100,
                        meta: {}, name: "档号", remarks: "200", type: "input"
                    },
                    {
                        code: "FILETIME", id: "27002f1c-7f4c-42a5-b349-6daae159c998", labelWidth: 100,
                        meta: {}, name: "文件形成日期", remarks: "200", type: "input"
                    },
                    {
                        code: "NOTE", id: "fc59f6fb-dfa5-45cc-9ead-1d28c2c30b57", labelWidth: 100,
                        meta: {}, name: "备注", remarks: "510", type: "input"
                    }
                ],
                form: {},
                defaultParams: {}
            }
        },
        methods: {
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
            //删除
            async deleteDetail(row) {
                await this.$confirm('此操作将彻底删除该数据无法恢复, 是否继续?', '提示', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                })
                this.loading = true
                const {data} = await this.$post('/receive/offline/detail/deleteDetail', {
                    id: row.id
                })
                if (data.success) {
                    this.$message.success('删除成功！')
                    this.$refs.table.reload()
                } else {
                    this.$message.error(data.message)
                }
                this.loading = false

            },
            //编辑目录
            async editForm(row) {
                this.form = {}
                this.defaultParams = {}
                //获取表单数据
                const {data} = await this.$http.post('manage/formData/detail?', {
                    formDefinitionId: row.formDefinitionId,
                    formDataId: row.formDataId
                })
                if (data.success) {
                    this.form = data.data
                    this.defaultParams = {
                        formDefinitionId: row.formDefinitionId,
                        categoryId: row.categoryId,
                        detailId: row.id
                    }
                    this.$router.push({
                        path: "./metadataIndex",
                        query: {
                            defaultParams: this.defaultParams,
                            formDataId: row.formDataId,
                            formDefinitionId: row.formDefinitionId,
                            refType: 'archive',
                            groupCode: 'default',
                            watermark: false,
                            formdatarow: row,
                            id: row.id
                        }
                    })
                }
            },
            //编辑目录保存
            async save() {
                const valid = await this.$refs.form.validate()
                console.log("valid", valid)
                if (valid) {
                    const url = `/receive/offline/detail/updateForm`
                    const {data} = await this.$post(url, Object.assign(this.form, this.defaultParams))
                    if (data.success) {
                        this.$message.success("保存成功！")
                        this.$refs.table.reload()
                        this.editDialogShow = false
                    }

                }
            },
            /**
             * 跳转详情页面
             * @param row
             */
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
            showDialog(row) {
                this.dialogShow = true
                this.dialogLoading = true
                this.$http.post('/testrecord/page', {page: false, formDataId: row.formDataId}).then(({data}) => {
                    if (data && data.success) {
                        this.data = data.data
                    }
                    this.dialogLoading = false
                    this.dialogShow = true
                })
            },
            /**
             *入库档案
             * @return {Promise<void>}
             */
            async notifyResult(params) {
                if (params.success) {
                    this.$message.success('提交入库申请成功！')
                    this.$router.back()
                } else {
                    this.$message.warning(params.message)
                }
            },
            async sendSaved() {
                this.$message.success('提交成功！')
                this.$router.back()
            },
            async saveEnd() {
                this.$message.success('办结成功！')
                this.$router.back()
            },
            appendParams(form) {
                this.$set(form, 'businessId', this.batchId)
            },
            /*重新检测*/
            toTest() {
                this.$http.post('/receive/offline/doTestOffline', {batchId: this.batchId}).then(({data}) => {
                    if (data && data.success) {
                        this.$message.success(data.data)
                    } else {
                        this.$message.error(data.message)
                    }
                })
            }
        },
        computed: {
            batchId() {
                return this.$route.query.id || this.$route.query.businessId
            },
            // defaultSearchForm() {
            //   return {batchId: this.$route.query.id || this.$route.query.businessId}
            // },
            //是否在流转中
            isProcess() {
                return !!this.$route.query.taskId
            }

        }
    }
</script>
