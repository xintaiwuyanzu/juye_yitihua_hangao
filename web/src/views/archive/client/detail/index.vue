<template>
  <table-index :fields="fields" path="dataBatchFileDetail" :insert="false" :delete="false" :edit="false"
               :default-search-form="defaultInsertForm" back/>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'batchName', label: '批次名称', required: true, search: true, width: 100},
        {prop: 'fileName', label: '文件名称'},
        {prop: 'fileSize', label: '文件大小'},
        {prop: 'fileHash', label: '文件哈希', required: true, search: true, align: 'left'},
        {prop: 'filePath', label: '文件存储路径'},
      ],
      defaultInsertForm: {batchId: this.$route.query.id}
    }
  }
}
</script>