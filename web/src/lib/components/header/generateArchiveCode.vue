<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">生成档号<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">生成选中</el-dropdown-item>
        <el-dropdown-item command="all">生成所有</el-dropdown-item>
        <el-dropdown-item command="query">生成查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

export default {
  extends: abstractComponent,
  name: 'generateArchiveCode',
  data() {
    return {sendType: 'all',}
  },
  methods: {
    async handleCommand(command) {
      this.sendType = command
      await this.doGenerateArchiveCode()
    },
    async doGenerateArchiveCode() {
      try {
        await this.$confirm('批量生成档号, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const query = this.eventBus.getQueryByQueryType(this.sendType)
        const {data} = await this.$http.post("/archivecode/getArchiveByCategoryCodeAndYear", {
          typeCode: this.category.code,
          year: '',
          ...query
        })
        if (data.success) {
          this.$message({
            type: 'success',
            message: data.data === '' ? '操作完成！' : data.data
          });
          this.eventBus.$emit("loadData")
        }
      } catch {
        this.$message({
          type: 'info',
          message: '已取消操作'
        });
      }
    },
  },
}
</script>
