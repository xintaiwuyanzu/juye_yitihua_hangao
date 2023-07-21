<template>
  <table-index :fields="fields"
               path="appraisalRules"
               :edit="true"
               :delete="true"
               :insert="true"
               :back="back"
               ref="table"
               :defaultSearchForm="defaultSearchForm"
               :defaultInsertForm="defaultInsertForm"
               title="细则管理">
    <template v-slot:edit="form">
      <el-form-item prop="auxiliaryResult" label="辅助结果" required>
        <select-dict type="kfjd" v-model="form.auxiliaryResult" :required="true"
                     @change="((val)=>{onChange(val, form)})"/>
      </el-form-item>
      <el-form-item prop="openRange" label="开放范围">
        <select-async v-model="form.openRange" :key="key" :required="true" , labelKey="openRange"
                      url="appraisalOpenRange/page" :params="params"/>
      </el-form-item>
    </template>
    <template v-slot:search-$btns>
      <el-button type="primary" @click="recommendKeyWord" size="mini">推荐关键词管理</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="20" @click="toDetail(scope.row)">详情</el-button>
    </template>
    <!--    <el-table-column/>-->
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      back: this.basisId ? true : false,
      fields: {
        rulesName: {label: '细则名称', required: true, search: true, align: 'center', width: 200},
        basisId: {
          label: '所属依据', required: true,
          fieldType: 'select',
          url: 'appraisalBasis/page',
          params: {page: false},
          labelKey: 'basis',
          search: true, align: 'center', width: 300
        },
        auxiliaryResult: {
          label: '辅助结果',
          fieldType: 'select',
          dictKey: 'kfjd',
          //on: {change: this.onChange},
          required: true,
          search: true,
          align: 'center',
          width: 80
        },
        createDate: {label: '创建时间', edit: false, search: false, dateFormat: "YYYY-MM-DD HH:mm:ss", width: 150},
        openRange: {
          label: '开放范围', required: true, autosize: true, search: false, align: 'center',
          fieldType: 'select',
          labelKey: 'openRange',
          url: 'appraisalOpenRange/page',
          params: {page: false}
        }
      },
      params: {
        page: false
      },
      key: 1,
      defaultInsertForm: {}
    }
  },
  methods: {
    $init() {
      /*this.defaultSearchForm.basisId = this.basisId
      this.defaultInsertForm.basisId = this.basisId*/
      this.defaultInsertForm = this.basisId ? {basisId: this.basisId} : {}
      this.back = this.basisId ? true : false
    },
    toDetail(row) {
      this.$router.push({path: '/appraisal/rules/detail', query: {rulesId: row.id, basisId: row.basisId}})
    },
    onChange(v, form) {
      this.params.auxiliaryResult = v
      this.key++
      this.$set(form, 'openRange', '')
    },
    recommendKeyWord() {
      this.$router.push({path: '/appraisal/rules/recommendKeyWord'})
    },
    defaultSearchForm() {
      return {basisId: this.$route.query.basisId}
    },
    /*defaultInsertForm() {
      return {basisId: this.$route.query.basisId}
    }*/
  },
  computed: {
    basisId() {
      return this.$route.query.basisId
    },
  },
  watch: {
    'defaultSearchForm.basisId'() {
      this.$refs.table.loadData(this.defaultSearchForm);
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
