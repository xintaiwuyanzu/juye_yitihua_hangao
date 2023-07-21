<template>
  <el-tabs @tab-click="handleClick" v-model="activeName">
    <el-tab-pane label="未梳理" name="first">
      <table-index :defaultSearchForm="search1" :delete="false" :edit="false" :fields="fields" :insert="false"
                   path="dataResource"
                   ref="table0"
                   style="height: 80vh;display: flex;flex-direction: column" titl="元数据标注">
        <template v-slot:search-$btns="scope">
          <el-button @click="flush" type="primary" width="50">刷新</el-button>
        </template>
        <template v-slot:table-$btns="scope">
          <el-button @click="mark(scope.row)" type="text" width="100">标注</el-button>
        </template>
      </table-index>
    </el-tab-pane>
    <el-tab-pane label="已梳理" name="second">
      <table-index :defaultSearchForm="search2" :delete="false" :edit="false" :fields="fields" :insert="false"
                   path="dataResource"
                   ref="table1"
                   style="height: 80vh;display: flex;flex-direction: column" title="关系梳理">
        <template v-slot:search-$btns="scope">
          <el-button @click="flush" type="primary" width="50">刷新</el-button>
        </template>
        <template v-slot:table-$btns="scope">
          <el-button @click="mark(scope.row)" type="text" width="100">梳理关系</el-button>
        </template>
      </table-index>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
  import dataMark from "./dataMark/index";

  export default {
    name: "index",
    components: {
      dataMark
    },
    data() {
      return {
        activeName: 'first',
        visible: false,
        fields: {
          formName: {label: '表单名称', search: true},
          formCode: {label: '表单编码'},
          dataNum: {label: '数据数量'},
          createDate: {label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss'}
        },
        search1: {status: '0'},
        search2: {status: '1'},
        formId: ''
      }
    },
    methods: {
      async flush() {
        const {data} = await this.$post('dataResource/loadData')
        if (data) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          this.loadData()
        }
      },
      loadData() {
        if (this.activeName === 'first') {
          this.$refs['table0'].loadData()
        } else {
          this.$refs['table1'].loadData()
        }
      },
      mark(row) {
        this.$router.push({
          path: '/mapping/dataResource/dataMark',
          query: {formId: row.formId}
        })
      },
      handleClick(tab) {
        this.$refs['table' + tab.index].loadData()
      }
    }
  }
</script>

<style scoped>

</style>