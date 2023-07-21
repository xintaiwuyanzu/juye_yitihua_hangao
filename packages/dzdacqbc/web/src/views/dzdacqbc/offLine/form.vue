<template>
    <section>
        <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
            <el-form-item label="备份名称">
                <el-input style="width:150px" v-model="searchForm.strategyName" clearable/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="searchF" size="mini">搜 索</el-button>
                <el-button @click="resetFields" size="mini" type="info">重 置</el-button>
                <el-button type="primary" @click="add()" size="mini">添 加</el-button>
            </el-form-item>
        </el-form>

        <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'添加')+'离线备份信息'" width="40%">
            <el-form :model="form" :rules="rules" ref="form" label-width="120px" :inline="false">
                <el-form-item label="备份名称" prop="strategyName" @input.native="handleClick" required>
                    <el-input v-model="form.strategyName" placeholder="请输入备份名称" style="width: 90%" clearable/>
                </el-form-item>
                <el-form-item label="年度" prop="nd" required>
                    <el-date-picker
                            v-model="form.nd"
                            type="year"
                            format="yyyy"
                            value-format="yyyy"
                            placeholder="年度"
                            :clearable="true">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="介质选择" prop="mediumId" required>
                    <el-select v-model="form.mediumId">
                        <el-option :key="index" :label="item.mediumName" :value="item.id"
                                   v-for="(item,index) in mediums"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel" type="info">取 消</el-button>
                <el-button type="primary" @click="insertForm" v-loading="loading">保 存</el-button>
            </div>
        </el-dialog>


    </section>
</template>

<script>
    import fromMixin from "@dr/auto/lib/util/formMixin";

    export default {
        name: 'typeForm',
        data() {
            return {
                archiveTypes: [],
                searchForm: {
                    strategyName: '',
                    fondId: this.fondIdData,
                    clsssId: this.categoryId,
                },
                form: {
                    strategyName: '',
                    data: [],
                    fondId: this.fondIdData,
                    clsssId: this.categoryId,
                },
                autoClose: true,
                openTypes: false,
                dataSouceArchiveType: '',
                checkAll: false,
                checkedItems: [],
                isIndeterminate: true,
                fielIds: [],

                mediums: [],

            }
        },
        props: {
            total: Number,
            fondIdData: String,
            categoryId: String,
        },
        mixins: [fromMixin],
        methods: {
            $init() {
                this.getFields()
                this.getMediums()
            },
            async getMediums() {
                const {data} = await this.$http.post('eSaveMedium/page', {page: false})
                if (data.success) {

                    this.mediums = data.data

                }
            },
            resetFields() {
                this.searchForm.strategyName = ''
            },
            handleCheckAllChange(val) {
                let labels = this.fielIds.map(val => val.label)
                this.checkedItems = val ? labels : [];
                this.isIndeterminate = false;
            },
            handleCheckedItemshange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.fielIds.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.fielIds.length;

            },
            async getFields() {
                const {data} = await this.$http.post('reportGenerate/getFields', {reportType: 'sl'})
                if (data.success) {
                    this.fielIds = data.data
                }
            },
            add() {
                this.openTypes = false
                this.editForm()
            },
            handleClick() {
                this.$nextTick(() => {

                })
            },
            getTypes() {
                this.$http.post('/archiveType/page', {page: false})
                    .then(({data}) => {
                        if (data.success && data.data != null) {
                            this.archiveTypes = data.data
                            this.openTypes = true
                        }
                    })
            },
            insertForm() {
                if (this.$refs.form) {
                    this.loading = true
                    this.$refs.form.validate(valid => {
                        if (valid) {
                            let path
                            if (this.form.id) {
                                path = 'esave/offLine/update'
                            } else {
                                path = 'esave/offLine/insert'
                            }
                            this.form.fondId = this.fondIdData
                            this.form.classId = this.categoryId

                            this.$http.post(path, this.form).then(({data}) => {
                                if (data && data.success) {
                                    this.$message.success('保存成功！')
                                    if (this.autoClose) {
                                        this.cancel()
                                    }
                                    this.searchF()
                                } else {
                                    this.$message.error(data.message)
                                }
                                this.loading = false
                            })
                        } else {
                            this.loading = false
                        }
                    })
                }
            },
            searchF() {
                this.searchForm.fondId = this.fondIdData
                this.searchForm.classId = this.categoryId
                this.$emit('func', this.searchForm)
            },
            showEdit(v) {
                console.log(v)
                this.form = {...v}
                this.dataSouceArchiveType = this.form.archiveType
                this.form.classId = this.categoryId
                this.form.fondId = this.fondIdData
                this.form = {...v}
                this.edit = true
            },
            imp() {
                this.$emit("imp")
            },
        }
        ,


    }
</script>

<style scoped>
    .box-card {
        width: 100%;
    }
</style>
