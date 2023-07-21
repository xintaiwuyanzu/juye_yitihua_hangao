<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button @click="toSuccess" type="primary"  v-if="this.$route.query.isHidden!='2'">查档完成</el-button>
  </section>
</template>
<script>

export default {
  data() {
    return{
    }
  },
  methods: {
    async toSuccess(){
      await this.$confirm('是否确定要完成查档？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/utilization/consult/toEnd", {id: this.$route.query.id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("查档已完成")
                this.$router.back()
              }
            }).catch(() => {
          this.$message.error("查档失败")
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消查档'
        });
      });
    }
  }
}
</script>
