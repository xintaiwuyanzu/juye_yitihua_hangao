<template>
  <section>
    <el-link type="warning" @click="onTest">四性检测</el-link>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'fourSexTest',
  methods: {
    async onTest() {
      try {
        await this.$confirm('此操作将检测选中项, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        this.loading = true
        const defaultParams = {
          formDefinitionId: this.formId,
          id: this.row.id,
          fondCode: this.row.FOND_CODE,
          categoryCode: this.row.CATE_GORY_CODE,
          archiveCode: this.row.ARCHIVE_CODE,
          title: this.row.TITLE,
        }
        const url = `/testrecord/startTest`
        const {data} = await this.$post(url, Object.assign(defaultParams))
        if (data.success) {
          this.$message.success("检测完成！")
        } else {
          this.$message.error(data.message)
        }
      } catch {
        this.loading = false
      }
      this.loading = false
    },
  },
}
</script>
