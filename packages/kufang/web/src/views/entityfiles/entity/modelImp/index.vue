<template>
    <el-dialog title="模板下载" :visible.sync="show" width="40%">
        <el-form ref="impForm" :model="form" label-width="110px" :inline="false">
            <el-form-item label="类型" prop="type" required>
                <el-select
                        v-model="form.type"
                        filterable clearable
                        default-first-option
                        placeholder="请选择类型">
                    <el-option
                            v-for="item in types"
                            :key="item.id"
                            :label="item.label"
                            :value="item.id"/>
                </el-select>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="info" @click="reset" class="btn-cancel">取 消</el-button>
            <el-button type="primary" @click="expEntity" v-loading="loading" class="btn-submit">下 载</el-button>
        </div>
    </el-dialog>
</template>
<script>
    export default {
        name: "form",
        data() {
            return {
                show: false,
                form: {
                    type: '',
                },
                loading: false,
                types: [
                    {id: 'ajwj', label: '案卷/文件'},
                    {id: 'jnwj', label: '卷内文件'}
                ]
            }
        },
        methods: {
            reset() {
                this.form.type = ''
                this.show = false
            },
            expEntity() {
                this.$refs.impForm.validate(valid => {
                    if (valid) {
                        let url = 'api/entityFiles/expEntity?type=' + this.form.type
                        window.open(url)
                        this.reset()
                    }
                })
            },
        }
    }
</script>

<style scoped>

</style>
