<template>
  <section v-if="childrenIndex">
    <el-link size="mini" type="primary" @click="exp">卷内目录</el-link>
    <form ref="printForm" action='/api/mulu/expMuluForjnj' method="post" target="_blank" style="display: none">
      <input name="fondId" :value="query.fondId"/>
      <input name="categoryId" :value="query.categoryId"/>
      <input name="formDefinitionId" :value="childrenIndex.formId"/>
      <input name="_QUERY" :value="query._QUERY"/>
      <input name="archiveCode" :value="row.ARCHIVE_CODE"/>

    </form>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'expJuanNeiMuLu',
  data() {
    return {
      expType: 'all',
      query: {},
      array:[]
    }
  },
  methods: {
    exp() {
      this.query = this.eventBus.getQueryByQueryType(this.expType)
      //const archive_code = {"key":"ARCHIVE_CODE","type":"s","value":this.row.ARCHIVE_CODE}
      this.$nextTick(() => {
        this.$refs.printForm.submit()
      })
    }
  },
}
</script>
