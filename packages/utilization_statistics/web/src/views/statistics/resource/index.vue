<template>
  <section>
    <nac-info>
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="全宗" style="margin-left: 10px">
          <el-select v-model="form.id" placeholder="请选择" clearable>
            <el-option
                v-for="item in orgData"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="日期">
          <el-date-picker
                  v-model="form.year1"
                  type="year"
                  :picker-options="pickerOption1"
                  placeholder="请选择开始年份"
                  clearable>
          </el-date-picker>
        </el-form-item>
        <el-form-item label="至">
          <el-date-picker
                  v-model="form.year2"
                  type="year"
                  :picker-options="pickerOption2"
                  placeholder="请选择结束年份"
                  clearable>
          </el-date-picker>
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
          <div class="table-container">
            <el-card class="container-card">
              <el-table ref="applyTable"
                        :data="data" border
                        highlight-current-row
                        v-loading="loading">
                <column-index :page="page"/>
                <el-table-column label="档案门类" prop="lx" show-overflow-tooltip align="center">
                  <template slot-scope="scope">
                    {{
                      scope.row.lx|dict({
                        'Document': "文书门类",
                        'PERSONNEL': "人事门类",
                        'Accountant': "会计门类",
                        'OTHER': "默认分类"
                      })
                    }}
                  </template>
                </el-table-column>
                <el-table-column label="件盒数量" prop="jh" align="center"></el-table-column>
                <el-table-column label="原文数量" prop="yw" align="center"></el-table-column>
                <el-table-column label="文件数量" prop="wj" align="center"></el-table-column>
                <el-table-column label="案卷数量" prop="aj" align="center"></el-table-column>
              </el-table>
            </el-card>
            <el-card class="container-card">
              <ve-histogram :data="utilizeArchiveData" height="550px" width="100%" :settings="chartSettings"
                            :loading="loading" :extend="extend_color"/>
            </el-card>

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
      orgData: [],
      form: {},
      category: [],
      year1: '',
      year2: '',
      pickerOption1:{},
      pickerOption2:{
        disabledDate:(time)=>{
            return time.getTime() < new Date(this.form.year1).getTime();
        }
      },
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
        labelMap: {
          lx: '类型',
          jh: '件盒',
          yw: '原文',
          wj: '文件',
          aj: '案卷'
        },
      },
      utilizeArchiveData: {
        columns: ['lx','jh','yw','wj','aj'],
        rows: []
      },
    }
  },
  methods: {
    $init() {
      this.loadData()
      this.getOrganise()
    },
    loadData(){
      this.loadData1()
    },
    async loadData1() {
      let startTime = ''
      let endTime = ''
      if (this.form.year1 != undefined && this.form.year1 != null && this.form.year2 != undefined && this.form.year2 != null) {
        startTime = this.getTime(this.form.year1)
        endTime = this.getTime(this.form.year2)
      }
      const {data} = await this.$post('/statistics/resource',{
        startTime: startTime,
        endTime: endTime,
        id: this.form.id,
        flag: 0
      })
      // this.utilizeArchiveData.rows = data.data
      this.data = data.data
      this.utilizeArchiveData.rows = data.data

    },

    async download(){
      let startTime = ''
      let endTime = ''
      if (this.form.year1 != undefined && this.form.year1 != null && this.form.year2 != undefined && this.form.year2 != null) {
        startTime = this.getTime(this.form.year1)
        endTime = this.getTime(this.form.year2)
      }
      const {data} = await this.$post('/export/exportExcel',{
        exportType: 'dazy',
        startTime: startTime,
        endTime: endTime,
        fontId: this.form.id
      })
      window.open(data.data, '_blank')
    },
    getOrganise(){
      this.$http.post('/statistics/getFond').then(({data}) => {
        if (data && data.success) {
          this.orgData = data.data
        }
      })
    },
    getTime(time){
      var date = new Date(time)
      var y = date.getFullYear()
      var m = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1)
      var d = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate())
      return y + '-' + m + '-'+ d
    },

  },

}
</script>
<style lang="scss">
.container-card{
  margin: 5px;
}
</style>

