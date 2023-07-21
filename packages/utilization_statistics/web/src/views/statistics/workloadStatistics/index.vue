<template>
  <section>
    <nac-info>
        <!--<el-form :inline="true" class="demo-form-inline">
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

            <el-form-item label="机构" style="margin-left: 10px">
                <el-select v-model="form.orgId" placeholder="请选择" clearable>
                    <el-option
                            v-for="item in orgData"
                            :key="item.id"
                            :label="item.organiseName"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item style="margin-left: 10px">
                <el-button type="primary" @click="loadData">查询</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="download">生成报告</el-button>
            </el-form-item>
        </el-form>-->
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable"
                  :data="data" border
                  highlight-current-row
                  v-loading="loading">
          <column-index :page="page"/>
          <el-table-column label="类型" prop="lx" show-overflow-tooltip align="center">

          </el-table-column>
          <el-table-column label="数量" prop="sl" align="center"></el-table-column>
        </el-table>
          <ve-histogram :data="utilizeArchiveData" height="750px" width="100%" :settings="chartSettings"
                        :loading="loading" :extend="extend_color"/>
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
      },
      pickerOption1:{},
      pickerOption2:{
        disabledDate:(time)=>{
            return time.getTime() < new Date(this.form.year1).getTime();
        }
      },
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
          radius: '100',
          Axis:{
              xAxis:{
                  labels:{
                      x:45
                  }
              }
          },
      },
      utilizeArchiveData: {
          columns: ['类型','数量'],
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
          this.loadData2()
      },
    async loadData1() {
        /*let startTime = ''
        let endTime = ''
        if (this.form.year1 != undefined && this.form.year1 != null && this.form.year2 != undefined && this.form.year2 != null) {
            startTime = this.getTime(this.form.year1)
            endTime = this.getTime(this.form.year2)
        }*/
      const {data} = await this.$post('/statistics/workloadStatistics',{
          /*startTime: startTime,
          endTime: endTime,
          orgId: this.form.orgId,*/
          flag:0
      })
      this.data = data.data
    },
      async loadData2() {
          /*let startTime = ''
          let endTime = ''
          if (this.form.year1 != undefined && this.form.year1 != null && this.form.year2 != undefined && this.form.year2 != null) {
              startTime = this.getTime(this.form.year1)
              endTime = this.getTime(this.form.year2)
          }*/
          const {data} = await this.$post('/statistics/workloadStatistics',{
              /*startTime: startTime,
              endTime: endTime,
              orgId: this.form.orgId,*/
              flag:1
          })
          this.utilizeArchiveData.rows = data.data
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