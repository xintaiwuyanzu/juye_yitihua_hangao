<template>
    <section>
        <el-link type="primary" @click="showDialog">档案防护</el-link>
        <el-dialog width="50%" title="选择防护项"  :visible.sync="dialogShow" align="center" :close-on-click-modal="false" append-to-body>
            <el-form class="searchForm" v-model="form" style="margin-top: 10px" >
                <el-form-item >
                    <el-checkbox-group v-model="form.handleName">
                        <el-checkbox label="档案除尘" value="档案除尘">档案除尘</el-checkbox>
                        <el-checkbox label="档案消毒" value="档案消毒">档案消毒</el-checkbox>
                        <el-checkbox label="档案裱糊" value="档案裱糊">档案裱糊</el-checkbox>
                        <el-checkbox label="档案修复" value="档案修复">档案修复</el-checkbox>
                        <el-checkbox label="档案仿真" value="档案仿真">档案仿真</el-checkbox>
                    </el-checkbox-group>
                    <!--<el-select v-model="form.handleName" multiple  placeholder="请选择">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>-->
                </el-form-item>
                <!--<el-form-item label = "开始时间：">
                    <el-date-picker
                            v-model="form.startTime"
                            type="date"
                            placeholder="选择开始时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label = "结束时间：">
                    <el-date-picker
                            v-model="form.endTime"
                            type="date"
                            placeholder="选择结束时间">
                    </el-date-picker>
                </el-form-item>-->
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
                <el-button type="primary" @click="onSubmit" v-loading="loading" class="btn-submit">保 存</el-button>
            </div>
        </el-dialog>
    </section>
</template>

<script>
    import abstractColumnComponent from "./abstractColumnComponent";
    export default {
        extends: abstractColumnComponent,
        name: "promanage",
        data(){
            return{
                form:{
                    handleName:[],
                    startTime:"",
                    endTime:"",
                },
            }
        },
        methods:{
            showDialog() {
                this.dialogShow = true
            },
            onSubmit(){
                let name = ""
                if (this.form.handleName&&this.form.handleName.length>0){
                    for (const element of this.form.handleName) {
                        name += element + ","
                    }
                }else {
                    this.$message.warning("请选择防护项")
                    return
                }
                let param = Object.assign({},this.form,{
                    handleName:name,
                    startTime:0,
                    endTime:0,
                    businessId:this.row.id,
                    formId:this.formId,
                    title:this.row.TITLE,
                    archiveCode:this.row.ARCHIVE_CODE,
                })
                this.$http.post("/promanage/insert",param).then(({data}) => {
                    if (data.success){
                        this.$message.success("操作完成")
                        this.dialogShow = false
                    }
                })
            }
        },
    }
</script>
