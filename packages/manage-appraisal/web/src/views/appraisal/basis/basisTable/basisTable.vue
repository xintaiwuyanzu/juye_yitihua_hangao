<template>
  <table-index :fields="fields"
               path="appraisalBasis"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="true"
               :delete="false"
               ref="table"
               deleteMulti
               :showTitle="false"
               style="height: 75vh;display: flex;flex-direction: column">
<!--    <template v-slot:search-$btns="scope">-->
<!--      <el-button type="primary" width="40" @click="loadAll(scope)">查看全部</el-button>-->
<!--    </template>-->
  </table-index>
</template>
<script>

export default {
  data() {
    return {
      defaultInsertForm: {},
      defaultSearchForm: {},
      fields:{
        basis:{label:'鉴定依据',edit:true},
        describe:{label:'描述',edit:true},
        parentId: {
          label: '所属类型',
          fieldType: 'select',
          required: true,
          labelKey: 'basis',
          url: 'appraisalBasis/page',
          width:200,
          params: {page: false, parentId: 'default'}
        }
      }
    }
  },
  methods: {
    loadData() {
      this.$refs.table.reload()
    },
    clearData() {
      this.$refs.table.data.data = []
    },
    loadAll() {
      this.defaultSearchForm.parentId = ''
      this.loadData()
    }
  }
}
</script>

