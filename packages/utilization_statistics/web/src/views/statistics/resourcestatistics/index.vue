<template>
    <section>
        <nac-info >
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="日期">
                    <el-date-picker
                            style="float: right;"
                            v-model="year"
                            type="year"
                            placeholder="选择年度"
                            clearable>
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="分类" style="margin-left: 10px">
                    <el-select v-model="form.category" style="width: 150px;float: right;margin-right: 20px"
                               placeholder="请选择" clearable>
                        <el-option
                                v-for="item in options"
                                :key="item.id"
                                :label="item.name"
                                :value="item.code">
                        </el-option>
                    </el-select>
                </el-form-item>

                <el-form-item style="margin-left: 10px">
                    <el-button type="primary" @click="loadData">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="download">生成报告</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="index_main" v-loading="loading">
                <div class="table-container">
                  <el-card class="container-card">
                    <el-table
                        :data="tableData"
                        style="width: 100%;margin-bottom: 20px;"
                        row-key="id"
                        border
                        default-expand-all
                        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
                      <el-table-column
                          prop="name"
                          label="分类">
                      </el-table-column>
                      <el-table-column
                          prop="wj"
                          label="文件数量"
                          sortable>
                      </el-table-column>
                      <el-table-column
                          prop="aj"
                          sortable
                          label="案卷数量">
                      </el-table-column>
                      <el-table-column
                          prop="yw"
                          sortable
                          label="原文数量">
                      </el-table-column>
                    </el-table>
                  </el-card>
                  <el-card class="container-card">
                    <ve-histogram :data="utilizeArchiveData" height="550px" width="100%" :settings="chartSettings"
                                  :loading="loading" :extend="extend_color"/>
                  </el-card>
                </div>
            </div>

        </div>

    </section>
</template>
<script>
    import indexMixin from '@archive/core/src/util/indexMixin'
    import "echarts/lib/component/title";
    import "echarts/lib/component/toolbox";

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
                page: {
                    index: 1,
                    size: 15,
                    total: 0
                },
                options: [],
                form: {},
                category: [],
                year: '',
                tableData: [],
                extend_color: {
                    color: [
                        '#3d9dff',
                        '#66d5ff',
                        '#ffa43b',
                        '#fa64a7',
                        '#a5d439',
                        '#4deedd',
                        '#ba98fa',
                        '#f8e32f',
                        '#be736c'
                    ],
                    xAxis: {
                        axisLabel: {
                            interval: 0, //
                            rotate: 45 // 旋转的度数

                        }
                    },
                    yAxis: {
                        axisTick: {
                            allowDecimals:false
                        }
                    }
                },
                chartSettings: {
                },
                utilizeArchiveData: {
                    columns: ['分类','文件','案卷','原文'],
                    rows: []
                },
            }
        },
        methods: {
            $init() {
                this.loadData()
                this.getAllCategory()
            },
            loadData(){
                this.loadData1()
                this.loadData2()
            },
            async loadData1() {
                if (this.year != '' && this.year != null) {
                    this.form.year = new Date(this.year).getFullYear()
                }
                const {data} = await this.$post('/statistics/resourceByCateGory', {
                    category:this.form.category,
                    year:this.form.year,
                    flag:0
                },{timeout: 20000})
                this.tableData = data.data
                this.form.year = ''
            },
            async loadData2() {
                if (this.year != '' && this.year != null) {
                    this.form.year = new Date(this.year).getFullYear()
                }
                const {data} = await this.$post('/statistics/resourceByCateGory', {
                    category:this.form.category,
                    year:this.form.year,
                    flag:1
                },{timeout: 20000})
                this.utilizeArchiveData.rows = data.data
                // this.tableData = data.data
                this.form.year = ''
            },
            getAllCategory(){
                this.$http.post('/statistics/getAllCategory').then(({data}) => {
                    if (data && data.success) {
                        this.options = data.data
                    }
                })
            },
            async download() {
                this.form.exportType = 'fltj'
                const {data} = await this.$post('/export/exportExcel',{
                    exportType: 'fltj',
                   data: JSON.stringify(this.tableData)
                })
                window.open(data.data, '_blank')
            },

        },
    }
</script>
<style lang="scss">
.container-card{
  margin: 5px;
}
</style>