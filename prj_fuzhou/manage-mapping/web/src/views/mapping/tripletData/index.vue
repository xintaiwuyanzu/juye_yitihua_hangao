<template>
  <section>
    <nac-info title="关系总览">
      <span>源对象</span>
      <el-input class="e_input" clearable style="width: 150px" v-model="searchForm.sourceFormName"></el-input>
      <span>关系</span>
      <el-input class="e_input" clearable style="width: 150px" v-model="searchForm.relationName"></el-input>
      <span>目标对象</span>
      <el-input class="e_input" clearable style="width: 150px" v-model="searchForm.targetFormName"></el-input>
      <el-button @click="loadData" type="primary">搜索</el-button>
      <el-button @click="searchForm={sourceFormName:'',targetFormName:'',relationName:''}" type="info">重置</el-button>
      <el-button @click="selectCheck" type="primary">查看图谱</el-button>
    </nac-info>
    <table-render :columns="columns" :data="data" :defaultSearchForm="searchForm" class="index_main"/>
  </section>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        columns: [
          {prop: 'sourceFormName', label: '源对象'},
          {prop: 'relationName', label: '关系'},
          {prop: 'targetFormName', label: '目标对象'},
          {prop: 'status', label: '关系数量'},
          {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss'}
        ],
        searchForm: {sourceFormName: '', targetFormName: '', relationName: ''},
        path: '/tripletData',
        data: [],
      }
    },
    methods: {
      $init() {
        this.loadData()
      },
      async loadData() {
        const {data} = await this.$post('/tripletData/page', Object.assign(this.searchForm, {page: false}))
        if (data && data.success) {
          this.data = data.data
        }
      },
      selectCheck() {
        this.$router.push({
          path: '/mapping/tripletData/paint',
          query: {
            search: this.searchForm,
            i: 0
          }
        })
      }
    }
  }
</script>

<style scope>
  .e_input {
    margin: 0 5px
  }
</style>