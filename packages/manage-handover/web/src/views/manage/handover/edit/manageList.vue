<template>
  <table-render :columns="columns" :data="data" v-loading="loading" :page="{index:0,size:100}"/>
</template>

<script>
/**
 * 要移交的全宗卷列表
 */
export default {
  name: "manageList",
  props: {
    batchId: {type: String}
  },
  data() {
    return {
      loading: false,
      data: [],
      columns: {
        title: {label: '题名'},
        archiveCode: {label: '文件材料编号'},
        year: {label: '年度', width: 80}
      }
    }
  },
  methods: {
    async $init() {
      if (this.batchId) {
        this.loading = true
        const {data} = await this.$post('/manage/handover/details/page', {
          page: false,
          batchId: this.batchId
        })
        if (data.success) {
          this.data = data.data
        }
        this.loading = false
      }
    }
  }
}
</script>