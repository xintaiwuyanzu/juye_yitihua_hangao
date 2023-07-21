<template>
    <table-index :edit="false" :fields="fields" :insert="false" path="reportGenerate">
      <template v-slot:table-$btns="scope">
        <el-button @click="downloadReport(scope.row)" type="text" width="30">下载</el-button>
      </template>
    </table-index>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        fields: {
          personName: {label: '操作人'},
          optionName: {label: '操作内容'},
          organizeName: {label: '操作人所在机构'},
          fondName: {label: '报表全宗', search: true},
          reportAnnual: {label: '报表年度', search: true},
          createDate: {label: '生成时间', dateFormat: 'YYYY-MM-DD HH:mm:ss'}
        },
      }
    },
    methods: {
      downloadReport(row) {
        window.open('/api/reportGenerate/downloadReport?id=' + row.id)
      }
    }
  }
</script>

<style scoped>

</style>