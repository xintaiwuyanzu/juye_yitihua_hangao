<template>
    <table-index :fields="fields" path="manage/form/" pagePath="manage/form/findFormPage"
                 title="元数据管理"
                 deletePath="manage/form/deleteForm" updatePath="manage/form/updateForm"
                 insertPath="manage/form/addForm"
                 :editFilterFields="editFilterFields">
        <template v-slot:table-$btns="scope">
            <el-button type="text"
                       @click="$router.push({path: '/archive/manage/form/field', query: {formId: scope.row.id,formName:scope.row.formName}})"
                       size="mini" width="45">元数据项
            </el-button>
            <el-button type="text"
                       @click="$router.push({path: '/archive/manage/form/display', query: {formId: scope.row.id,formName:scope.row.formName}})"
                       size="mini" width="60">显示方案
            </el-button>
            <el-button type="text"
                       @click="$router.push({path: '/archive/manage/form/fournature', query: {formId: scope.row.id}})"
                       size="mini" width="60">元数据检测项
            </el-button>
            <el-button type="text"
                       @click="createTable(scope.row.id)"
                       size="mini" width="75">生成表结构
            </el-button>
            <el-button type="text" @click="showRebuild(scope.row)" size="mini" width="100">重建es索引</el-button>
            <el-button type="text" @click="fileNameSetting(scope.row)" size="mini" width="100">档号配置规则</el-button>
        </template>
        <template v-slot:edit="form">
            <el-form-item label="编码(英文小写)" prop="formCode" required>
                <el-input v-model="form.formCode" placeholder="请输入英文小写"
                          @blur="formCodeChange(form.formCode)">
                    <template slot="append"></template>
                </el-input>
            </el-form-item>
        </template>
        <el-dialog :visible.sync="showRebuildDialogVisible" :title="'编码'+(formDefinition.formCode)+': 选择重建类型'"
                   width="200">
            <div style="margin: 10px auto">
                <el-button type="primary" @click="rebuildIndex(formDefinition.id,'1')" size="mini">复制重建</el-button>
                <el-button type="primary" @click="rebuildIndex(formDefinition.id,'2')" size="mini">删除重建</el-button>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="showRebuildDialogVisible=false">取消</el-button>
            </div>
        </el-dialog>
        <form-name-setting
                :addOrUpdateVisible="dialogFileName"
                @close="getClose"
                :field="field"
                :pitchOn="pitchOn"
                :showSchemeId="showSchemeId">
        </form-name-setting>
    </table-index>
</template>
<script>
    import formNameSetting from "./formNameSetting"

    export default {
        components: {formNameSetting},
        data() {
            return {
                fields: [
                    {prop: 'formName', label: '名称', search: true, required: true},
                    {prop: 'formCode', label: '编码', search: true, required: true},
                    {prop: 'formType', label: '类型', dictKey: 'archiveTypes', fieldType: 'select', required: true},
                    {
                        prop: 'default', label: '默认版本', mapper: {
                            true: {label: '是', show: 'success'},
                            false: {label: '否', show: 'danger'}
                        }, fieldType: 'select', edit: false
                    },
                    {prop: 'version', label: '版本', edit: false},
                    {prop: 'description', type: 'textarea', label: '描述'}
                ],
                editFilterFields: ['fields'],
                showRebuildDialogVisible: false,
                formDefinition: {formCode: '', id: ''},
                dialogFileName: false,
                formLabelWidth: '80px',
                state: true,
                title: '',
                pitchOn: [],
                field: [],
                showSchemeId: '',
                transfer: [],
            }
        },
        methods: {
            async createTable(id) {
                try {
                    const isExist = await this.$http.post("/manage/form/tableExist", {formDefinitionId: id})
                    if (isExist.data.success) {
                        if (isExist.data.data) {
                            this.$message.success('表结构已存在!')
                        } else {
                            await this.$confirm('生成表结构, 是否继续?', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            })
                            const {data} = await this.$http.post("/manage/form/createTable", {formDefinitionId: id})
                            if (data.success) {
                                this.$message.success("操作完成！")
                            } else {
                                this.$message.error(data.message)
                            }
                        }
                    } else {
                        this.$message.error(isExist.data.message)
                    }
                } catch {
                    this.$message({
                        type: 'info',
                        message: '已取消操作！'
                    });
                }
            },
            //小写字母校验
            formCodeChange(v) {
                if (v) {
                    const reg = /^[a-z]+$/;
                    //如果不是小写字母
                    if (!reg.test(v)) {
                        this.$message.warning('编码请输入小写字母')
                    }
                }

            },
            async rebuildIndex(id, type) {
                const {data} = await this.$http.post("/esTemplate/rebuildIndex", {
                    formDefinitionId: id,
                    rebuildType: type
                })
                if (data.success) {
                    this.$message.success("操作完成！")
                } else {
                    this.$message.error(data.message)
                }
                this.showRebuildDialogVisible = false
            },
            showRebuild(row) {
                this.formDefinition.formCode = row.formCode
                this.formDefinition.id = row.id
                this.showRebuildDialogVisible = true
            },
            fileNameSetting(row) {
                this.loading = true
                this.showSchemeId = row.id
                //清空处理，防止多余数据传入
                this.field = []
                const param = Object.assign({}, {businessId: this.showSchemeId})
                this.$http.post('/codingscheme/schemeitem/page', param).then(({data}) => {
                    if (data.success) {
                        this.pitchOn = data.data.data
                        this.$http.post('/manage/form/findFieldList', {formDefinitionId: row.id}).then(({data}) => {
                            if (data.success) {
                                this.transfer = data.data
                                //将获取的元数据的 fieldCode，label 换成code,name. 用于字段名称匹配，减少后续转换工作。
                                for (const fieldOne of this.transfer) {
                                    let obj = new Object()
                                    obj.code = fieldOne.fieldCode
                                    obj.name = fieldOne.label
                                    this.field.push(obj)
                                }
                                if (this.pitchOn && this.pitchOn.length > 0) {
                                    for (const pitch of this.pitchOn) {
                                        for (let i = 0; i < this.field.length; i++) {
                                            if (pitch.code === this.field[i].code) {
                                                this.field.splice(i, 1)
                                            }
                                        }
                                    }
                                    console.log(this.field)
                                } else {
                                    this.pitchOn = []
                                }
                                this.dialogFileName = true
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                    }
                })
            },
            getClose(val) {
                this.dialogFileName = val;
            },
        }
    }
</script>
