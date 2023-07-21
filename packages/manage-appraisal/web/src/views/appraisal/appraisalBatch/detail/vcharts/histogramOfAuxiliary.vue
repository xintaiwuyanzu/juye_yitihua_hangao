<template>
  <section>
    <ve-histogram :data="chartData"/>
  </section>
</template>
<script>
export default {
  data() {
    return {
      chartData: {
        rows: [],
        columns: ['门类名称', '数量']
      },
    }
  },
  props: {
    batchId: ''
  },
  methods: {
    $init() {
      this.$http.post('/appraisalBatch/getCategoryCountByBatch', {id: this.batchId})
          .then(({data}) => {
            if (data.success) {
              this.chartData.rows = [];
              data.data.forEach(
                  data => {
                    this.chartData.rows.push({
                      '门类名称': data.categoryName,
                      '数量': data.count,
                    })
                  }
              )
            }
          })
    },
  }
}
</script>
<style>

</style>
