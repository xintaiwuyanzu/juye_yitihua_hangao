<template>
  <el-card v-loading="loading" style="padding-top: 14px;overflow: scroll">
    <span class="h_color">门类统计
      <span class="h_color" style="color: #616265">馆藏量总数： <span style="font-size: 20px;color: #616265;">{{
          resourceStaticsNum
        }}</span></span>
    </span>
    <el-divider/>
    <div>
      <ve-histogram :data="chartData"
                    :extend="verticalChartExtend"
                    height="300px"
                    judge-width
      ></ve-histogram>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "static",
  data() {
    return {
      chartData: {
        columns: ['类型', '数量'],
        rows: []
      },
      verticalChartExtend: {
        series: {
          label: {
            show: true,
            position: 'top',
          },
          itemStyle: {
            // 添加渐变颜色
            normal: {
              color: function(params) {
                var colorList = [
                  "#70a8f2",
                  "#72d4a3",
                  "#f2cb32",
                  "#d0a0e7",
                ]; //每根柱子的颜色
                return colorList[params.dataIndex];
              }
            }
          }
        },
        xAxis: {
          axisLabel: {
            interval: 0,
            rotate: 0,
            //换行显示
            // formatter: function (value) {
            //   let result = ""; //拼接加\n返回的类目项
            //   let maxLength = 1; //每项显示文字个数
            //   let valLength = value.length; //X轴类目项的文字个数
            //   let rowNumber = Math.ceil(valLength / maxLength); //类目项需要换行的行数
            //   if (rowNumber > 1) {
            //     //如果文字大于3,
            //     for (let i = 0; i < rowNumber; i++) {
            //       let temp = ""; //每次截取的字符串
            //       let start = i * maxLength; //开始截取的位置
            //       let end = start + maxLength; //结束截取的位置
            //       temp = value.substring(start, end) + "\n";
            //       result += temp; //拼接生成最终的字符串
            //     }
            //     return result
            //   } else {
            //     return value
            //   }
            // },
          }
        },
        legend:{
          show:false,
        },
      },
      loading: false,
      resourceStatics: [],
      resourceStaticsNeW: [],
      category: [],
    }
  },
  methods: {
    async $init() {
      this.loading = true
      const {data} = await this.$post('resourceStatistics/total')
      if (data && data.success) {
        this.resourceStatics = data.data
        this.getCategory()
      }
      this.loading = false
    },
    getCategory() {
      this.$http.post('/resourceStatistics/page', {page: false}).then(({data}) => {
        if (data.success) {
          this.resourceStaticsNeW = data.data
          this.chartData.rows = [];
          this.resourceStaticsNeW.forEach(i => {
            this.chartData.rows.push({
              '类型': i.categoryName,
              '数量': i.fileArchiveNum
            })
          })
        }
      })
    }

  },
    /*getCategory(){
      this.$http.post('/resourceStatistics/page').then(({data}) => {
        if (data.success){
          console.log('data.data',data.data)
        }

      })


    }

  },
*/
  computed: {
    resourceStaticsNum() {
      let num = 0
      this.resourceStatics.forEach(i => {
        num += i.fileArchiveNum
      })
      return num
    }
  }
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