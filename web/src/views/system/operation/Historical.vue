<template>
  <el-card v-loading="loading" class="cards">
    <span class="system_access">历史操作总量
      <span style="float: right;font-size: 13px;cursor:pointer" @click="$router.push('/archive/syslog')">更多 ></span>
    </span>
    <ve-histogram :data="chartData" :extend="extend" :legend-visible="false"/>
  </el-card>
</template>

<script>
export default {
  name: "historical",
  data() {
    return {
      loading: false,
      extend: {
        yAxis: {
          //是否显示y轴线条
          axisLine: {
            show: true,
          },
          // 纵坐标网格线设置，同理横坐标
          splitLine: {
            show: true,
          },
          // 线条位置
          position: "left",
        },
        xAxis: {
          axisLine: {
            show: true,
          },
        },
        series: {
          label: { show: true, position: "top" },
          itemStyle: {
            // 添加渐变颜色
            normal: {
              color: function(params) {
                var colorList = [
                  "#70A8F2",
                  "#ADD150",
                  "#F2CB32",
                  "#F59056",
                ]; //每根柱子的颜色
                return colorList[params.dataIndex];
              }
            }
          }
        },
      },
      chartData: {
        columns: ["data", "number"],
        rows: [],
      },
    }
  },
  methods: {
    $init(){
      this.history()
    },
    history(){
      this.$http.post("fzlog/operation").then(({data})=>{
        /*console.log("Object.keys(data.data)",Object.keys(data.data).length)
        console.log("Object.values(data.data)",Object.values(data.data))*/
        if (data.success) {
          this.chartData.rows = [];
          for (let prop in data.data){
            this.chartData.rows.push({
              "data":prop,
              "number":data.data[prop]
            });
          }
        }
      })
    }
  },
  computed: {

  }
}
</script>

<style lang="scss" scoped>
.cards{
  padding: 16px;
}
.system_access {
  //padding-bottom: 10px;
  height: 80%;
  font-size: 20px;
  font-weight: 500;
  font-stretch: normal;
  line-height: 25px;
  letter-spacing: 1px;
  color: $--color-primary;
}
</style>