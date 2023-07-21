<template>
  <section>
    <nac-info>
      <div style="float: right">
        <el-date-picker
            v-model="value2"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions">
        </el-date-picker>
      </div>
    </nac-info>
    <div>
      <div style="text-align: center;margin: 50px">
        <div>
          <el-link @click="type='histogram'" :class="{'active':type==='histogram'}">柱状图</el-link>
          <el-divider direction="vertical"/>
          <el-link @click="type='line'" :class="{'active':type==='line'}">折线图</el-link>
        </div>
      </div>
      <el-divider/>
      <ve-line :data="chartData" v-if="type==='line'"></ve-line>
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
      value2: ''
    }
  },
  created() {
    this.getStaticsByYear();
  },
  methods: {
    getStaticsByYear() {
      this.$post('/utilization/consult/staticsByYear', {startDate: null, endDate: null}).then(({data}) => {
        if (data) {
          //保存数据
          this.chartData.rows = [];
          console.log(data.data)
          for (let key in data.data) {
            console.log(data.data[key].useFor);
            this.chartData.rows.push({
              '类型': data.data[key].useFor === 'notarization' ? '办理公证' : '其他',
              '数量': data.data[key].count,
            })
          }
        }
      })
    },

  },
  watch: {},
  mounted() {
  }
}
</script>

<style scoped>
.active {
  color: red;
}
</style>