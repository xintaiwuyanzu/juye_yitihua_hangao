<template>
    <section>
        <el-dialog :visible.sync="edit" width="350px">
            <strong slot="title">添加档案
            </strong>
            <el-form :model="form" ref="form" label-width="120px" style="width: 300px">
                <el-form-item label="馆藏室:" required>
                    <el-tag>{{form.locId|getLocOptions}}</el-tag>
                </el-form-item>
                <el-form-item label="密集架号:" required>
                    <el-tag>{{form.bookCaseNo}}</el-tag>
                </el-form-item>
                <el-form-item label="AB面:" required>
                    <el-tag>{{form.ABsurface|dict({1:'A',2:'B'})}}面
                    </el-tag>
                </el-form-item>
                <el-form-item label="列数:" required>
                    <el-tag>{{form.layerNo}}
                    </el-tag>
                </el-form-item>
                <el-form-item label="层数:" required>
                    <el-tag>{{form.layerNum}}
                    </el-tag>
                </el-form-item>
                <el-form-item label="开始档号:" required>
                    <el-input placeholder="请输入开始档号" v-model="form.startBarCode" clearable autofocus/>
                </el-form-item>
                <el-form-item label="结束档号:" required>
                    <el-input placeholder="请输入结束档号" v-model="form.endBarCode" @keyup.enter.native="handleClick"
                              clearable autofocus/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="cancel">取 消</el-button>
                <el-button type="primary" @click="saveForm" v-loading="loading">添 加</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    import fromMixin from '@/util/formMixin'
    import indexMixin from '@/util/indexMixin'

    let locoptions = []
    export default {
        mixins: [indexMixin, fromMixin],
        data() {
            return {
                autoClose: true,
                form: [{
                    bookCaseNo: '',
                    layerNum: '',
                    startBarCode: '',
                    endBarCode: '',
                    locId: '',
                    libId: '',
                    num: ''
                }],
               dialog: false,
                    rules: {
                        startBarCode: [{validator: this.numCheck, trigger: 'blur'}],
                        endBarCode: [{validator: this.numCheck, trigger: 'blur'}],
                }
            }
        }, filters: {
            getLocOptions(v) {
                if (v) {
                    const loc = locoptions.find(d => d.id === v)
                    if (loc) {
                        return loc.name
                    }
                }
            },
        },

        methods: {
            handleClick() {
                this.$nextTick(() => {
                    this.form.startBarCode = this.form.startBarCode.replace(/[^\w]/g, '');
                    this.form.endBarCode = this.form.endBarCode.replace(/[^\w]/g, '');
                })
            },
            $init() {
                //this.getlibs()
                this.form = this.$refs.dataForm
                //this.getlocoption()

            },
            getlocoption() {
                this.$http.post('library/getLocations').then(({data}) => {
                    if (data.success) {
                        locoptions = data.data
                    } else {
                        this.$message.error(data.message)
                    }
                })
            },
            saveForm() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.loading = true
                        this.form.update = false
                        const param = Object.assign(this.form)
                        this.$http.post('/position/addPositionForBooks', param)
                            .then(({data}) => {
                                if (data.success) {
                                    this.$message.success(data.message)
                                    this.cancel()
                                    this.$parent.loadDataBook()
                                    this.loading = false

                                } else {
                                    this.$message.error(data.message)
                                    this.loading = false
                                }
                            })
                    }
                })
            },

            numCheck(rule, value, cb) {
                if (value) {
                    if (this.$check.isNum(value)) {
                        cb()
                    } else {
                        cb(new Error("手机号格式不正确！"))
                    }
                } else {
                    cb(new Error("手机号不能为空！"))
                }
            },
        }
    }
</script>
