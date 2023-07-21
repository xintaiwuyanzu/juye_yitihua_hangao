<template>
    <section>
        <nac-info title="档案管理工作统计">
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="日期">
                    <el-date-picker
                            v-model="form.year"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期">
                    </el-date-picker>
                </el-form-item>
                <el-form-item style="margin-left: 10px">
                    <el-button type="primary" @click="loadData">查询</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
                <el-table ref="applyTable"
                          :data="data" border
                          height="79vh"
                          highlight-current-row
                          v-loading="loading">
                    <column-index :page="page"/>
                    <el-table-column label="鉴定类型" prop="batchName" show-overflow-tooltip align="center">

                    </el-table-column>
                    <el-table-column label="鉴定数量" prop="count" align="center"></el-table-column>
                </el-table>
            </div>
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
                this.getOrganise()
            },
            async loadData() {
                let startTime = ''
                let endTime = ''
                if (this.form.year !== undefined) {
                    startTime = this.getTime(this.form.year[0])
                    endTime = this.getTime(this.form.year[1])
                }
                const {data} = await this.$post('/statistics/countAppraisal',{
                    startTime: startTime,
                    endTime: endTime
                })
                this.data = data.data
            },
            getOrganise() {
                this.$http.post('/rule/getOrganise').then(({data}) => {
                    if (data && data.success) {
                        this.orgData = data.data
                    }
                })
            },
            getTime(time) {
              const date = new Date(time);
              const y = date.getFullYear();
              const m = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1);
              const d = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
              return y +'-'+ m +'-'+ d
            },
        },
    }
</script>