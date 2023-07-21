<template>
    <table-index :fields="fields" path="cqbcAlarm/" :edit="false"
                 :defaultSearchForm="defaultSearchForm"
                 :insert="false" ref="table">
        <template v-slot:table-$btns="scope">
            <el-button type="text"
                       @click="showProcess(scope.row)"
                       size="mini" width="33" v-show="scope.row.processState === '0'">处理
            </el-button>
        </template>
        <!--    <alarmdeal ref="alarmdeal" @func="loadData"/>-->
        <el-dialog :visible.sync="doExe" title="处理" width="80%">
            <el-form :model="form" ref="form" label-width="80px">
                <el-form-item label="处理结果" prop="title" style="margin-left: 8px">
                    <el-input type="textarea" v-model="form.processResult" :autosize="{ minRows: 3, maxRows: 6 }"
                              clearable/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="updateStatus">提交</el-button>
                <el-button @click="doExe=false">取消</el-button>
            </div>
        </el-dialog>
    </table-index>
</template>

<script>
    import indexMixin from '@/util/indexMixin'
    // import alarmdeal from "./alarmdeal";

    export default {
        mixins: [indexMixin],
        // components: {alarmdeal},
        data() {
            return {
                doExe: false,
                //新代码
                fields: [
                    {
                        prop: 'alarmType',
                        label: '报警类型',
                        search: true,
                        width: "150",
                        fieldType: 'select',
                        mapper: {'archive': '电子档案报警', 'disk': '存储空间报警', 'tamper': '篡改检测报警', 'adjust': '档案调整'}
                    },
                    {prop: 'alarmContent', label: '报警内容', search: true, required: true},
                    {prop: 'createDate', label: '报警时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false},
                    {
                        prop: 'processState',
                        label: '处理状态',
                        width: "80",
                        component: 'tag',
                        showTypeKey: 'show',
                        mapper: {
                            '0': {label: '未处理', show: 'danger'},
                            '1': {label: '已处理', show: 'success'}
                        },
                        fieldType: 'select',
                        edit: false,
                        search: true
                    },
                    {prop: 'processUserName', label: '处理人', width: "100"},
                    {prop: 'processDate', label: '处理时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
                    {prop: 'processResult', label: '处理结果', width: "100"},
                ],
                defaultSearchForm: {
                    processState: '',
                    alarmContent: '',
                    alarmType: '',
                    classificationId: this.$route.query.classificationId,
                    archiveId: this.$route.query.archiveId
                },
                form: {
                    id: '',
                    processResult: '',
                    processState: '1'
                }
            }
        },
        methods: {
            loadData() {
                this.$refs.table.loadData(this.defaultSearchForm);
            },
            showProcess(row) {
                if (row.alarmType === 'tamper') {
                    this.$confirm('此操作将当前被篡改的档案从备份路径恢复到长期保存库, 是否继续?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(async () => {
                        const {data} = await this.$http.post('cqbcAlarm/earchiveRestore', {archiveId: row.archiveId, alarmId:row.id})
                        if (data && data.success) {
                            this.$message.success(data.message)
                            this.$refs.table.reload()
                        } else {
                            this.$message.error(data.message)
                        }
                    })
                } else {
                    this.doExe = true
                    this.form.id = row.id
                }
            },
            async updateStatus() {
                const {data} = await this.$http.post('/cqbcAlarm/update', this.form)
                if (data && data.success) {
                    this.$message.success('处理完成!')
                    this.doExe = false
                    this.$refs.table.reload()
                } else {
                    this.$message.error(data.message)
                }
            }
        },
        watch: {
            "$route.query.classificationId"(val) {
                this.defaultSearchForm.classificationId = val
                this.$refs.table.loadData(this.defaultSearchForm);
            }
        },
    }
</script>
