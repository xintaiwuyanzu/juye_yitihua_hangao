<template>
  <section style="height: 50px">
    <ve-pie :data="chartDataPie" :settings="chartSettings" :series="chartSettings.series" :legend="chartSettings.legend"  offsetY="4"  ref="chartpie"></ve-pie>
  </section>
</template>

<script>
export default {
  name: "pieOfBatchArchiveStatus",
  data(){
    return{
      chartDataPie:[],
      chartSettings:{
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series:[{
          type: 'pie',
          center:['65%', '30%'],
          radius:'80',
          data: [

          ],
          columns: ['日期', '访问用户'],
        }],
      },
    }
  },
  props:{
    batchId:''
  },
  methods:{
    $init(){
      this.$http.post('/appraisalBatch/getArchiveCountByBatch',{id:this.batchId})
          .then(({data})=>{
            if(data.success){
              this.chartSettings.series[0].data = []
              this.chartSettings.series[0].data.push({value:data.data.data.ywc,name:'已完成'})
              this.chartSettings.series[0].data.push({value:data.data.data.ylq-data.data.data.ywc,name:'正在鉴定'})
              this.chartSettings.series[0].data.push({value:data.data.data.zs-data.data.data.ylq,name:'未领取数量'})
            }
          })
    }
  }
}
</script>

<style scoped>

</style>
