<template>
  <section>
    <nac-info>
      <div style="float: right">
<!--        <el-select filterable  v-model="organiseId"  placeholder="请选择机构" clearable>
          <el-option v-for="item in organises" :key="item.id"
                     :label="item.organiseName" :value="item.id">
          </el-option>
        </el-select>-->
        <el-select v-model="fondCode" filterable placeholder="请选择全宗" style="width: 200px" clearable>
          <el-option
              v-for="i in fonds"
              :key="i.id"
              :label="`${i.data.code} ${ i.label}`"
              :value="i.data.code">
          </el-option>
        </el-select>
        <el-date-picker
            v-model="rangeDate"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd HH-mm-ss"
            :picker-options="pickerOptions" clearable>
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
<!--
      <ve-histogram :data="chartData" v-if="type==='histogram'" :legend="legend" :tooltip="tooltip" :grid="grid" :xAxis="xAxis" :yAxis="yAxis" :series="series"></ve-histogram>
-->
      <ve-pie :data="chartData" v-if="type==='pie'"></ve-pie>
      <ve-histogram :data="chartData" v-if="type==='histogram'"></ve-histogram>
<!--      <v-chart ref="ref_chart" :option="optionColumn" style="height 400px"/>-->
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
        //全宗code
      fondCode: '',
      fonds: [],
      mechanismYear: '',
      xAxis: {
        axisLabel:{
          textStyle:{
            fontSize: "20"
          }
        },
        data: []
      },
      legend: {
        orient: 'horizontal',
        top: 'top'
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'shadow'
        }
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      yAxis: {},
      series: [
        {
          name: '已指导',
          type: 'bar',
          barGap: 0,
          label: {show: true,},
          emphasis: {
            focus: 'series'
          },
          barWidth : 55,
          data: []
        },
        {
          name: '待指导',
          type: 'bar',
          barGap: 0,
          label: {show: true,},
          emphasis: {
            focus: 'series'
          },
          barWidth : 55,
          data: []
        },
      ],
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
    $init() {
      this.getStaticsByYear();
      this.getFonds();
    },
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'});
      this.fonds = data.data
    },
     async getStaticsByYear() {
       this.xAxis.data = []
       this.series[0].data = []
       this.series[1].data = []
      await  this.$post('/businessGuidanceStatic/statusStatic', {
        startDate: this.startDate,
        endDate: this.endDate,
        fondCode: this.fondCode
      }).then(({data})=>{
        if (data.success) {
       /*   //保存数据
          let set = new Set()
          this.chartData.rows = [];

          let unclears1 = 0;
          let unclears2 = 0;
          let orgRecord = '';
          let typeRecord = '';
          data.data.forEach(i=>{

            if(i.orgName != ''){
              if(set.has(i.orgName)){//判断orgName set里在不在 如果存在走下面 不存在就添加
                if(orgRecord != i.orgName){  //判断记录的 和现在的判断orgName
                  if(typeRecord = '已指导'){ //判断上次的类型
                    console.log("已指导")
                   // this.series[1].data.push('')
                   // typeRecord = '待指导'
                  }else if(typeRecord = '待指导'){//判断上次的类型
                    //this.series[0].data.push('')
                  }

                }
              }else{
                set.add(i.orgName)
                console.log(typeRecord)
                console.log(orgRecord)
                console.log(i.orgName)
                if( i.type === '待指导' && typeRecord === ''){
                  console.log('else的已指导')
                  console.log(typeRecord)
                  this.series[0].data.push('')
                  typeRecord = '已指导'

                }else if(i.type === '已指导' && typeRecord === '已指导' && orgRecord === i.orgName){
                  console.log('else的待指导')
                  console.log(typeRecord)
                  this.series[1].data.push('')
                  typeRecord = '待指导'
                }else if(i.type === '待指导' && typeRecord === '已指导' && orgRecord != i.orgName){
                  this.series[1].data.push('')
                  console.log('第三个')
                  typeRecord = '待指导'
                }


                orgRecord = i.orgName
              }
            }
            if(i.type==='已指导' && i.orgName !=''){
              this.series[0].data.push(i.quantity)
              typeRecord = '已指导'
              console.log("下面的已指导")
              console.log(this.series[0].data)
            }else if(i.orgName !=''){
              this.series[1].data.push(i.quantity)
              console.log("下面的待指导")
              console.log(this.series[1].data)
              typeRecord = '待指导'
            }

          })
            let item = Array.from(new Set(set))
            item.forEach(s=>{
              this.xAxis.data.push(s)
            })*/
          this.chartData.rows = [];
          data.data.forEach(i => {
            this.chartData.rows.push({
              '类型': i.type,
              '数量': i.quantity
            })
          })
        }
      })

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
    },
    "fondCode": function (value) {
      //监听到数据之后,可以在这个地方做任何操作*****
      this.getStaticsByYear()
    }
  }
}
</script>

<style scoped>
.active {
  color: red;
}
</style>