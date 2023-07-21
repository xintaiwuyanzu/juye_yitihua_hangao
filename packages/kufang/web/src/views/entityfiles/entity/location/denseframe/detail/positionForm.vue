<template>
    <section>
        <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'添加')+'位置信息'" width="400px">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px" inline="false">

                <el-form-item label="密集架编号" prop="bookCaseNo">
                    <el-input v-model="form.bookCaseNo" placeholder="请输入密集架号" disabled clearable/>
                </el-form-item>
                <el-form-item label="密集架名" prop="caseName">
                    <el-input v-model="form.caseName" placeholder="请输入密集架名" clearable/>
                </el-form-item>
                <!--<el-form-item label="EPC" prop="epcOrder">
                    <el-input v-model="form.epcOrder" placeholder="请输入epc" clearable/>
                </el-form-item>
                <el-form-item label="磁条号" prop="bookCaseId">
                    <el-input v-model="form.bookCaseId" placeholder="请输入书柜" clearable/>
                </el-form-item>-->
                <el-form-item label="密集架类型" prop="columnNum">
                    <select-dict v-model="form.columnNum" type="columnNum.status" disabled/>
                </el-form-item>
                <el-form-item label="总列数：" prop="columnNo">
                    <el-input v-model="form.columnNo" placeholder="请输入列数" disabled clearable/>
                </el-form-item>
                <el-form-item label="总层数" prop="layer">
                    <el-input v-model="form.layer" placeholder="请输入层数" disabled clearable/>
                </el-form-item>
                <el-form-item label="每层档案数量" prop="bookCount">
                    <el-input v-model="form.bookCount" placeholder="请输入档案数量" clearable disabled/>
                </el-form-item>
                <br>
                <el-form-item label="位置描述" prop="caseNoTrans">
                    <el-input type="textarea" v-model="form.caseNoTrans" placeholder="请输入位置描述" clearable
                              style="width: 220px"/>
                </el-form-item>
                <br>
                <el-form-item label="备注信息" prop="remarks">
                    <el-input type="textarea" v-model="form.remarks" placeholder="请输入备注信息" style="width: 220px"
                              clearable/>
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
    import fromMixin from '@/util/formMixin'

    export default {
        data() {
            return {
                libs: [],
                locOptions: [],
                rules: {
                    key: [
                        {validator: this.validateKey, trigger: 'blur'}
                    ]
                },
                autoClose: true,
            }
        },

        mixins: [fromMixin],
        methods: {
            $init() {
            },

            getLoc(v) {
                this.$http.post('/location/getLocation', {libId: v})
                    .then(({data}) => {
                        this.searchForm.locId = ''
                        if (data.success) {
                            this.locOptions = data.data
                        } else {
                            this.locOptions = []
                        }
                    })
            },
            insertForm() {
                if (this.$refs.form) {
                    this.loading = true
                    this.$refs.form.validate(valid => {
                        if (valid) {
                            let path = ''
                            if (this.form.id) {
                                path = 'position/updatePosition'
                                this.$http.post(path, this.form).then(({data}) => {
                                    if (data && data.success) {
                                        this.$message.success('保存成功！')
                                        if (this.autoClose) {
                                            this.cancel()
                                            this.$parent.loadDataBook()
                                        }
                                    } else {
                                        this.$message.error(data.message)
                                    }
                                    this.loading = false
                                })
                            } else {
                                path = 'position/addPosition'
                                this.$http.post(path, this.form).then(({data}) => {
                                    if (data && data.success) {
                                        this.$message.success('保存成功！')
                                        if (this.autoClose) {
                                            this.cancel()
                                            this.$parent.loadDataBook()
                                        }
                                    } else {
                                        this.$message.error(data.message)
                                    }
                                    this.loading = false
                                })
                            }

                        } else {
                            this.loading = false
                        }
                    })
                }
            }
        }

    }
</script>

<style scoped>

</style>
