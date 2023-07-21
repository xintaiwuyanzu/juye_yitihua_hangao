<template>
  <div>
    <el-card>
      <h3>档案鉴定数量</h3>
      <ve-histogram :data="chartData"/>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "appraisalNum",
    props: {
      params: Object
    },
    data() {
      return {
        chartData: {
          rows: [],
          columns: ['年度', '开放鉴定数量', '到期鉴定数量']
        }
      }
    },
    methods: {
      $init() {
        this.getAppraisalNum();
      },
      async getAppraisalNum() {
        const {data} = await this.$post('/appraisalStatistics/getStatistics', this.params)
        if (data) {
          this.chartData.rows = []
          data.data.forEach(i => {
            this.chartData.rows.push({
              '年度': i.vintages,
              '开放鉴定数量': i.appraisalType === '开放鉴定数量' ? i.id : 0,
              '到期鉴定数量': i.appraisalType === '到期鉴定数量' ? i.id : 0
            })
          })
        }
      }
    }
  }
</script>

<style scoped>

</style>