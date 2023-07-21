<template>
  <table-render class="table-container"
                :columns="columns"
                :data="pdfList" style="height:80vh;display: flex;flex-direction: column">
    <el-table-column label="操作" width="120" header-align="center" align="center">
      <template v-slot="scope">
        <el-link type="success" @click="addManageFile(scope.row)">添加到资料库</el-link>
      </template>
    </el-table-column>
  </table-render>
</template>

<script>
import {useUser} from "@dr/framework/src/hooks/userUser";

export default {
  setup() {
    return useUser()
  },
  name: "index",
  props: {batchId: {type: String}, readOnly: {type: Boolean, default: false}},
  data() {
    return {
      columns: [
        {prop: 'name', label: '文件名称'},
        {prop: 'suffix', label: '文件类型', width: '100'},
        {prop: 'saveDate', label: '上传日期', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'description', label: '备注', width: '100'},
      ],
      pdfList: []
    }
  },
  methods: {
    $init() {
      this.getManageFileList()
    },
    //获取table列内容
    async getManageFileList() {
      this.fileDialog = true
      this.loading = true
      const {data} = await this.$http.post('files/list', {
        refId: this.user.id,
        refType: 'manageFile',
      })
      if (data.success) {
        if (data && data.success) {
          this.pdfList = data.data
          this.$forceUpdate()
        }
      }
      this.loading = false
    },
    //添加到当前资料库
    async addManageFile(row) {
      const {data} = await this.$http.post('manageFile/insert', {
        businessId: this.batchId,
        fileInfoId: row.id,
      })
      if (data && data.success && data.data.id !== null) {
        this.$message.success('添加成功！')
        this.$emit('loadFileTree')
      } else if (data && data.success && data.data.id === null) {
        this.$message.success('重复添加！')
      } else {
        this.$message.success(data.message)
      }
    }
  }
}
</script>

<style scoped>

</style>