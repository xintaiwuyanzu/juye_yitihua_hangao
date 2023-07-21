<template>
  <section>
    <nac-info back>
      <el-button type="primary" @click="createBatch"> 确认创建鉴定批次</el-button>
    </nac-info>
    <div class="index_main">
      <table-render
          :columns="columns"
          :data="data">
      </table-render>
      <batch-form ref="batchForm" v-on:save="saveBatch"></batch-form>
    </div>
  </section>
</template>

<script>
import batchForm from './form'

export default {
  components: {batchForm},
  data() {
    return {
      columns: {},
      data: []
    }
  },
  methods: {
    $init() {
      this.$http.post("/archiveToBeAppraisal/statistics", {
        fondCodes: this.$route.query.fondCodes,
        appraisalType: this.$route.query.appraisalType,
        startVintages: this.$route.query.startVintages,
        endVintages: this.$route.query.endVintages
      }).then(({data}) => {
        if (data.success) {
          let filed = data.data.filedList
          this.columns.fondCode = {label: '全宗编码'}
          filed.forEach(v => {
            this.columns[v] = {label: v}
          })
          this.data = data.data.countList
        }
      })
    },
    createBatch() {
      this.$refs.batchForm.show = true
    },
    saveBatch(form) {
      this.$http.post("/appraisalBatch/insert", Object.assign(form, {
        fondCodes: this.$route.query.fondCodes,
        appraisalType: this.$route.query.appraisalType,
        startVintages: this.$route.query.startVintages,
        endVintages: this.$route.query.endVintages
      })).then(({data}) => {
        if (data.success) {
          this.$refs.batchForm.show = false
          this.$message.success("创建成功")
          this.$router.back()
        }
      })
    }
  }
}
</script>

