<template>
  <table-index :fields="fields"
               path="appraisalSpecial"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="true"
               :delete="false"
               ref="table"
               deleteMulti
               :showTitle="false"
               style="height: 70vh;display: flex;flex-direction: column">
    <template v-slot:search-$btns="scope">
      <el-button type="primary" width="40" @click="loadData(scope)">查看全部</el-button>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      defaultInsertForm: {rulesId:this.rulesId,basisId:this.basisId},
      defaultSearchForm: {rulesId:this.rulesId},
      fields:{
        specialName: {label: '专题名称', search: false, align: 'center'},
        specialRemarks: {label: '内容', search: false,edit:false, align: 'center'}
      }
    }
  },
  props:{
    rulesId:{type:String},
    basisId:{type:String}
  },
  watch:{
    rulesId:function (){
      this.defaultSearchForm.rulesId = this.rulesId
      this.defaultInsertForm.rulesId = this.rulesId
      this.defaultInsertForm.basisId = this.basisId
      this.$refs.table.reload()
    }
  },
  methods: {
    loadData() {
      this.$refs.table.reload()
    }
  }
}
</script>

