<template>
  <section>
    <el-link type="info" @click="onDelete">删除</el-link>
  </section>
</template>
<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'recycle',
  methods: {
    async onDelete() {
      try {
        await this.$confirm('此操作将选中项移除进回收站, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        this.eventBus.$emit('changeStatus', {ids: this.row.id, status: 'TRASH', row: this.row})
      } catch (e) {
        //this.$message.error(`移除失败${e}`)
      }
    }
  }
}
</script>
