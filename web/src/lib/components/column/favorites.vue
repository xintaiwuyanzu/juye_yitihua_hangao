<template>
  <section>
    <el-link type="warning" @click="onFavorites">收藏</el-link>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'favorites',
  methods: {
    onFavorites() {
      this.$http.post('/favorites/addFavorites', {
        formDefinitionId: this.formId,
        formDataId: this.row.id,
        danghao: this.row.ARCHIVE_CODE,
        quanzong: this.row.FOND_CODE,
        fenlei: this.row.CATE_GORY_CODE,
        niandu: this.row.YEAR,
        timing: this.row.TITLE,
        // wenhao:this.row.
      }).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('收藏成功！')
              this.eventBus.$emit("loadData")
            } else {
              this.$message.error(data.message)
            }
          })
    },
  }
}
</script>
