<template>
    <section>
        <nac-info>
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="年份">
                    <el-date-picker
                            v-model="form.year"
                            type="year"
                            placeholder="请选择年份">
                    </el-date-picker>
                </el-form-item>
                <el-form-item style="margin-left: 10px">
                    <el-button type="primary" @click="loadData">查询</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
        <div class="index_main" v-loading="loading">

            <el-row :gutter="20" style="height: 50%;">
                <el-col :span="24">
                    <el-card class="box-card" style="height: 40vh">
                        <div slot="header" style="height: 20px;" class="clearfix">
                            <span>
                                到期鉴定
                            </span>
                        </div>
                        <div class="table-container" style="height: 100%">
                            <el-table ref="applyTable"
                                      :data="dqjd" border
                                      height="100%"
                                      highlight-current-row
                                      v-loading="loading">
                                <column-index :page="page"/>
                                <el-table-column label="鉴定数量" prop="count" show-overflow-tooltip align="center"></el-table-column>
                                <el-table-column label="年度" prop="year" align="center"></el-table-column>
                            </el-table>
                        </div>
                    </el-card>
                </el-col>
            </el-row>

            <el-row :gutter="20" style="height: 50%">
                <el-col :span="24">
                    <el-card class="box-card" style="height: 40vh">
                        <div slot="header" style="height: 20px;" class="clearfix">
                            <span>
                                开放鉴定
                            </span>
                        </div>
                        <div class="table-container" style="height: 100%;">
                            <el-table ref="applyTable"
                                      :data="kfjd" border
                                      height="100%"
                                      highlight-current-row
                                      v-loading="loading">
                                <column-index :page="page"/>
                                <el-table-column label="鉴定数量" prop="count" show-overflow-tooltip align="center"></el-table-column>
                                <el-table-column label="年度" prop="year" align="center"></el-table-column>
                            </el-table>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
        </div>
    </section>
</template>
<script>
    import indexMixin from '@archive/core/src/util/indexMixin'

    /**
     * 批次列表
     */
    export default {
        mixins: [indexMixin],
        data() {
            return {
                fondid: '',
                loading: false,
                data: [],
                form: {},
                kfjd: [],
                dqjd: [],
                orgData: [],
                page: {
                    index: 1,
                    size: 15,
                    total: 0
                }
            }
        },
        methods: {
            $init() {
                this.loadData()
               // this.getOrganise()
            },
            async loadData() {
                if (this.form.year != '' && this.form.year != null) {
                    this.year = (this.form.year).getFullYear()
                }
                const {data} = await this.$post('/statistics/filemanagement',{
                    year:this.year
                })
                if(data.data.kfjd != undefined){
                    this.kfjd = data.data.kfjd
                }
                if(data.data.dqjd != undefined){
                    this.dqjd = data.data.dqjd
                }
                this.year = ''
            },
            getOrganise() {
                this.$http.post('/rule/getOrganise').then(({data}) => {
                    if (data && data.success) {
                        this.orgData = data.data
                    }
                })
            },
            getTime(time) {
                var date = new Date(time)
                var y = date.getFullYear()
                var m = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
                var d = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
                return y +'-'+ m +'-'+ d
            },
        },
    }
</script>

