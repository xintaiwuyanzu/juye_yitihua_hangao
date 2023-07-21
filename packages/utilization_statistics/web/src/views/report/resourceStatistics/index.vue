<template>
  <section>
    <nac-info title="资源统计">
      <search-component :updateBtn="true" @search="search" style="display: inline;"/>
      <el-button @click="generateReport" type="primary">生成统计报表</el-button>
<!--      <el-button @click="downloadExcel" type="primary">下载机构情况表</el-button>-->
      <el-button type="primary" @click="$router.push({path: '/report/resourceStatistics/veForm'})">查看报表统计</el-button>
    </nac-info>
    <table-render class="index_main"
                  :columns="columns"
                  :data="data"/>
  </section>
</template>

<script>
import SearchComponent from "./searchComponent";

export default {
  name: "index",
  components: {SearchComponent},
  data() {
    return {
      //新代码
      columns: [
        {prop: 'categoryName', label: '门类名称', search: false},
        {prop: 'arcArchiveNum', label: '案卷目录数量', search: false},
        {prop: 'fileArchiveNum', label: '文件目录数量', search: false},
        {prop: 'arcFileNum', label: '案卷原文数量', search: false},
        {prop: 'fileFileNum', label: '文件原文数量', search: false},
        {prop: 'arcFileSize', label: '案卷原文大小 (KB)', search: false},
        {prop: 'fileFileSize', label: '文件原文大小 (KB)', search: false},
      ],
      path: '/resourceStatistics',
      defaultSearchForm: {categoryCode: '', fondCode: '', vintages: ''},
      data: [],

    }
  },
  methods: {
    $init() {
      this.loadData()
      this.year()

    },

    // 获取当前年度
    async year(){
      const {data} = await this.$post('/resourceStatistics/year')
      if (data && data.success) {
        if (data.data) {
          this.defaultSearchForm.vintages = data.data
        }
      }
    },

    async search(v) {
      this.defaultSearchForm.categoryCode = v.categoryCode;
      this.defaultSearchForm.fondCode = v.fondCode;
      this.defaultSearchForm.vintages = v.vintages;
      await this.loadData()
    },
    async loadData() {
      const {data} = await this.$post('/resourceStatistics/page', Object.assign(this.defaultSearchForm, {page: false}))
      this.data = data.data
    },
    async generateReport() {
      this.defaultSearchForm.vintages=''
      const {data} = await this.$post('/resourceStatistics/generateReport', this.defaultSearchForm)
      if (data && data.success) {
        if (data.data) {
          this.$message.success('生成成功！请在报表查询中查看')
        } else {
          this.$message.error('生成失败！')
        }
      }
    },
    /*downloadExcel() {
      window.location.href = '/api/resourceStatistics/downloadExcel'
    },*/
  }
}
</script>