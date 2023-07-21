<template>
    <section>
        <!--    <el-button type="primary" @click="showDialog">销毁</el-button>
            <el-dialog width="80%" :title="title" :visible.sync="dialogShow" :close-on-click-modal="false">
              <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
                <el-button type="primary" @click="dialogShow = false" v-loading="loading" class="btn-submit">保 存</el-button>
              </div>
            </el-dialog>-->

        <el-dropdown
                placement="bottom"
                trigger="click"
                @command="handleCommand">
            <el-button class="search-btn" type="primary">开放鉴定<i class="el-icon-arrow-down el-icon--right"/></el-button>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="currentSelect.length>0" command="select">鉴定选中</el-dropdown-item>
                <el-dropdown-item command="all">鉴定所有</el-dropdown-item>
                <el-dropdown-item command="query">鉴定查询</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>

        <el-dialog width="40%" title="档案鉴定"
                   :visible.sync="dialogShow"
                   :close-on-click-modal=false
                   :close-on-press-escape=false
                   @close="resetForms"
                   v-if="dialogShow">

            <el-form :model="form" ref="form" :rules="rules" label-width="200px">

                <el-form-item label="接收人" prop="targetPerson" style="margin-top: 7px">
                    <el-select placeholder="请选择接收人" v-model="form.targetPerson" style="width: 80%">
                        <el-option v-for="person in persons" :key="person.id" :label="person.userName"
                                   :value="person.id"/>
                    </el-select>
                </el-form-item>

                <el-form-item label="鉴定原因" prop="destoryOpinion" style="margin-top: 7px">
                    <el-input
                            type="textarea"
                            :rows="2"
                            style="width: 80%"
                            placeholder="请输入销毁原因"
                            v-model="form.destoryOpinion">
                    </el-input>
                </el-form-item>
            </el-form>

            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="resetForms" class="btn-cancel">取 消</el-button>
                <el-button type="primary" @click="preSubmit" v-loading="loading" class="btn-submit">提 交</el-button>
            </div>
        </el-dialog>
    </section>
</template>
<script>
    import abstractComponent from "./abstractComponent";

    export default {
        extends: abstractComponent,
        name: 'appraisal',
        watch: {
            'form.appraisalType': {
                handler: function (value) {
                    this.clearForm()
                    this.form.appraisalType = value
                }
            }
        },
        data() {
            return {
                dialogShow: false,
                rules: {
                    destoryOpinion: [
                        {required: true, message: '鉴定原因不能为空', trigger: 'change'}
                    ],
                    targetPerson: [
                        {required: true, message: '接收人不能为空', trigger: 'change'}
                    ]
                },
                form: {
                    appraisalType: '',
                    sourceName: '',
                    sourceCode: '',
                    sourceValue: '',
                    targetValue: '',
                    appraisalGroups: '',
                    appraisalOpinion: '',
                },
                options: [],
                targetPerson: '',
                persons: [],
                personId: [],
                sendType: "all",
            }
        },
        methods: {
            resetForms(){
                this.form = {}
                this.dialogShow = false
            },
            handleCommand(command) {
                this.sendType = command
                this.showDialog()
                this.getPerson()
            },
            //显示弹出框
            async showDialog() {
                this.dialogShow = true
                this.clearForm()
                this.destructionShow = false
                this.saveTermShow = false
                this.openScopeShow = false
                this.securityLevelShow = false
                this.worthShow = false

                this.targetPerson = ''
                if (this.persons.length === 0) {
                    const {data} = await this.$post('/person/page', {page: false})
                    this.persons = data.data.filter(item => {
                        return this.personId.indexOf(item.id) === -1
                    })
                    //this.persons =
                }
            },
            getPerson() {
                this.$http.post('/login/info')
                    .then(({data}) => {
                        if (data.success) {
                            this.personId.push(data.data.id)
                        } else {
                            this.$message.error(data.message)
                        }
                    })
            },
            clearForm() {
                this.form = {
                    appraisalType: '',
                    sourceName: '',
                    sourceCode: '',
                    sourceValue: '',
                    targetValue: '',
                    appraisalGroups: '',
                    appraisalOpinion: '',
                }
            },
            async preSubmit() {
                const query = this.eventBus.getQueryByQueryType(this.sendType)
                this.$refs.form.validate(valid => {
                    if(valid){
                        const query = this.eventBus.getQueryByQueryType(this.sendType)
                        this.$http.post('/open/addBatch', {
                            targetPersonId: this.form.targetPerson,
                            formDefinitionId: this.formId,
                            fondId: this.fond.id,
                            destoryOpinion: this.form.destoryOpinion,
                            categoryId: this.category.id,
                            type: 'KFJD',
                            ...query
                        }).then(({data}) => {
                            if (data && data.success) {
                                this.eventBus.$emit("loadData")
                                this.$message.success("操作成功")
                                this.dialogShow = false
                                this.form = {}
                            } else {
                                this.$message.error(data.message)
                            }
                        })
                    }else{

                    }
                })
            },
        },
        computed: {
            title() {
                return `销毁`
            }
        },
    }
</script>
