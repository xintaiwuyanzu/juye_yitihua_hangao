<template>
  <table-index :fields="fields"
               path="appraisalBasis"
               :edit="true"
               :delete="true"
               :insert="true"
               ref="table"
  >
    <template v-slot:search="form">
      <el-form-item label="关键词：" prop="hookStatus">
        <el-input v-model="keyword" placeholder="请输入关键词"></el-input>
      </el-form-item>
    </template>
    <template v-slot:search-$btns="scope">
      <el-button type="primary" @click="searchAppraisalKeyword">关键词检索</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="30" @click="toDetail(scope.row)">细则</el-button>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fields: {
        code: {label: '依据编码', required: true, width: 100},
        basis: {label: '依据名称', required: true, search: true, width: 300},
        createDate: {label: '创建时间', required: true, edit: false, dateFormat: "YYYY-MM-DD HH:mm:ss", width: 150},
        description: {label: '依据详情', autosize: true, required: true, search: false, type: 'textarea'},
      },
      keyword: ''
    }
  },
  methods: {
    toDetail(row) {
      this.$router.push({path: '/appraisal/rules', query: {basisId: row.id}})
    },
    searchAppraisalKeyword() {
      this.$router.push({path: '/appraisal/keyword', query: {keyWord: this.keyword}})
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
