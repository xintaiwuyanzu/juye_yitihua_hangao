<template>
  <el-card v-loading="loading" class="cards">
    <span class="system_access">CPU使用情况、运行内存</span>
    <el-row>
      <el-progress v-for="item in data" :key="item.label" type="dashboard" :percentage="item.percentage"
                   :format="format(item)"
                   style="margin-top: 50px;margin-right: 15%;margin-left: 10px"/>

    </el-row>
  </el-card>
</template>

<script>
export default {
  name: "monitoring",
  data() {
    return {
      loading: false,
      data: [],
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

<style lang="scss" scoped>
.cards{
  padding: 16px;
}
.system_access {
  height: 80%;
  font-size: 20px;
  font-weight: 500;
  font-stretch: normal;
  line-height: 25px;
  letter-spacing: 1px;
  color: $--color-primary;
}
</style>