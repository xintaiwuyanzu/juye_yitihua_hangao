<template>
    <section>
        <nac-info>
            <div style="float: right">
                <el-date-picker
                        v-model="rangeDate"
                        type="daterange"
                        align="right"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH-mm-ss"
                        :picker-options="pickerOptions">
                </el-date-picker>
                <el-button type="primary" size="mini" @click="expExcel">导 出</el-button>
            </div>
        </nac-info>
        <div>
            <div style="text-align: center;margin: 50px">
                <div>
                    <el-link @click="type='histogram'" :class="{'active':type==='histogram'}">柱状图</el-link>
                    <el-divider direction="vertical"/>
                    <el-link @click="type='pie'" :class="{'active':type==='pie'}">饼状图</el-link>
                </div>
            </div>
            <el-divider/>
            <ve-pie :data="chartData" v-if="type==='pie'"></ve-pie>
            <ve-histogram :data="chartData" v-else></ve-histogram>
        </div>
    </section>
</template>

<script>
    export default {
        data() {
            return {
                type: 'histogram',
                //柱状图数据按年度显示
                chartData: {
                    columns: ['状态', '数量'],
                    rows: []
                },
                mechanismYear: '',
                pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                rangeDate: [],
                startDate: null,
                endDate: null
            }
        },
        methods: {
            $init() {
                this.getStatics();
            },
            async getStatics() {
                const {data} = await this.$post('/Registration/getBorrowData', {
                    startDate: this.startDate,
                    endDate: this.endDate
                })
                if (data.success) {
                    //保存数据
                    this.chartData.rows = [];
                    data.data.forEach(i => {
                        this.chartData.rows.push({
                            '状态': i.name,
                            '数量': i.count
                        })
                    })
                }
            },
            async expExcel() {
                if (this.chartData.rows.length > 1) {
                    if (this.startDate == null && this.endDate == null) {
                        this.startDate = ''
                        this.endDate = ''
                    }
                    let url = 'api/Registration/expBorrowData?startDate=' + this.startDate + '&endDate=' + this.endDate
                    window.open(url)
                } else {
                    this.$message.info('数据为空！')
                }

            },
        },
        watch: {
            rangeDate(val, oldVal) {
                if (val !== null) {
                    this.startDate = val[0]
                    this.endDate = val[1]
                } else {
                    this.startDate = null
                    this.endDate = null
                }
                this.getStatics();
            }
        }
    }
</script>

<style scoped>
    .active {
        color: red;
    }
</style>