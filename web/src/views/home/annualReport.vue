<template>
  <el-card v-loading="loading" style="overflow: hidden">
    <span class="h_color">年报统计<span style="font-size: 30px"/>
      <el-button class="h_color" type="text" style="float: right"
                 @click="$router.push('/statistics/reportGenerateRecord')">更多 >
      </el-button>
      <el-divider/>
    </span>
      <ve-line :data="chartData"
                    :extend="verticalChartExtend"
                    :settings="chartSettings"
                    height="300px"
      ></ve-line>
  </el-card>
</template>

<script>
export default {
  name: "static",
  data() {
    this.chartSettings = {
      area: true,//是否显示为面积图
      itemStyle:{ //面积图颜色设置
        color:{
          type:'linear',
          x:0,
          y:0,
          x2:0,
          y2:1,
          colorStops:[
            {
              offset: 0,
              color: 'rgba(245,140,93,0.8)', // 0% 处的颜色
            },
            {
              offset: 1,
              color: 'rgba(245,140,93,0)' // 100% 处的颜色
            },
          ],
          globalCoord: false // 缺省为 false
        }

      },
    }
    return {
      chartData: {
        columns: ['年份', '数量'],
        rows: []
      },
      verticalChartExtend: {
        series: {
          smooth:false,
          label: {
            show: false,
            position: 'top',
          },
        },
        legend:{
          show:false,
        },
      },
      loading: false,
      category: [],
    }
  },
  methods: {
    $init() {
        this.$http.post('/reportGenerate/total').then(({data}) => {
          if(data.success){
            this.chartData.rows = [];
            this.category = data.data
            this.category.forEach(i => {
              this.chartData.rows.push({
                '年份': i.reportAnnual,
                '数量': i.id
              })
            })
          }
        })
    },
  },
}
</script>

<style lang="scss" scoped>
.index_statics {
  .t1_box {
    display: flex;
    justify-content: flex-start;
    flex-wrap: wrap;
    font-size: 16px;
    overflow-y: scroll;
    //height: 10vh;
    margin-top: 4px;
  }
}

.h_color {
  color: $--color-primary-dark-1;
  float: right;
}
</style>