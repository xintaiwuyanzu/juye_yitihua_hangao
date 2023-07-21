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
        columns: ['类型', '数量'],
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
    $init(){
      this.getStaticsByYear();
    },
   async getStaticsByYear() {
      const {data} = await this.$post('/utilization/consult/staticsByYear', {
        startDate: this.startDate,
        endDate: this.endDate
      })
      if (data.success) {
        //保存数据
        this.chartData.rows = [];
        data.data.forEach(i => {
          this.chartData.rows.push({
            '类型': i.useFor,
            '数量': i.count
          })
        })
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
      this.getStaticsByYear();
    }
  }
}
</script>

<style scoped>
.active {
  color: red;
}
</style>