<template>
  <section>
    <nac-info title="对比查询页面" back/>
    <div class="index_main">
      <el-row style="height: 100%;" :gutter="10">
        <el-col :span="12" style="height: 100%">
          <fonds-descriptive-detail :formDataId="formDataOneId"/>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <fonds-descriptive-detail :formDataId="formDataTwoId"/>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      formDataOneId: this.$route.query.managementOneId,
      formDataTwoId: this.$route.query.managementTwoId,
    }
  }
}
</script>

<style scoped>

</style>