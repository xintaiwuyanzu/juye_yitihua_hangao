<template>
  <table-render :columns="columns" :data="data" ref="table">

  </table-render>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      columns: {
        userName: {label: '利用对象'},
        utilizeNum: {label: '利用次数'},
        useFor: {label: '利用目的', dictKey: 'utilize'},
        useWay: {label: '利用情形', dictKey: 'inquire'}
      },
      data: []
    }
  },
  props: {
    formDefinitionId: {type: String},
    formDataId: {type: String}
  },
  methods: {
    async $init() {
      await this.loadData()
    },
    async loadData() {
      const {data} = await this.$http.post("/utilization/consult/details/selectHistoryByForm",
          {formDefinitionId: this.formDefinitionId, formDataId: this.formDataId})
      this.data = data.data
    }
  }
}
</script>

<style scoped>

</style>