<template>
  <section style="height: 50px">
<!--    <ve-pie :data="chartDataPie" :settings="chartSettings" :series="chartSettings.series" :legend="chartSettings.legend"  offsetY="4"  ref="chartpie"></ve-pie>-->
    <ve-histogram :data="chartData" :settings="chartSettings" :series="chartSettings.series" :legend="chartSettings.legend"  offsetY="4"  ref="chartpie"></ve-histogram>
  </section>
</template>

<script>
export default {
  name: "statisticalReport",
  data(){
    return{
      chartData:{
        columns: ['日期', '销售额'],
        rows: [
          { '日期': '1月1日', '销售额': 123 },
          { '日期': '1月2日', '销售额': 1223 },
          { '日期': '1月3日', '销售额': 2123 },
          { '日期': '1月4日', '销售额': 4123 },
          { '日期': '1月5日', '销售额': 3123 },
          { '日期': '1月6日', '销售额': 7123 }
        ]
      },
      chartSettings:{
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series:[]
        //     [{
        //   type: 'pie',
        //   center:['65%', '30%'],
        //   radius:'80',
        //   data: [
        //
        //   ],
        //   columns: ['日期', '访问用户'],
        // }],
      },
    }
  },
  props:{
    batchId:''
  },
  methods:{
    // $init(){
    //   this.$http.post('/appraisalBatch/getArchiveCountByBatch',{id:this.batchId})
    //       .then(({data})=>{
    //         if(data.success){
    //           this.chartSettings.series[0].data = []
    //           this.chartSettings.series[0].data.push({value:data.data.data.ywc,name:'已完成'})
    //           this.chartSettings.series[0].data.push({value:data.data.data.ylq-data.data.data.ywc,name:'正在鉴定'})
    //           this.chartSettings.series[0].data.push({value:data.data.data.zs-data.data.data.ylq,name:'未领取数量'})
    //         }
    //       })
    // }
  }
}
</script>

<style scoped>

</style>
