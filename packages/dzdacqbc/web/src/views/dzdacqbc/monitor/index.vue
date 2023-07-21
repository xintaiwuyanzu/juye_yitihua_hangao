<template>
  <section>
    <nac-info/>
    <div class="index_main" v-loading="loading">
      <el-row>
        <el-progress v-for="item in data" :key="item.label" type="dashboard" :percentage="item.percentage"
                     :format="format(item)"
                     style="margin: auto;"/>
      </el-row>
    </div>
  </section>
</template>
<script>

export default {
  data() {
    return {
      data: [],
      loading: false
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    async loadData() {
      const {data} = await this.$post('/earchive/spaces/getMonitorData')
      if (data.success) {
        this.data = data.data.map(d => {
          return {
            ...d,
            percentage: parseFloat(parseFloat(d.value * 100 / d.max).toFixed(2))
          }
        })
      }
    },
    format(item) {
      return () => {
        return item.label + `\n(${item.percentage}%)`
      }
    }
  }
}
</script>


