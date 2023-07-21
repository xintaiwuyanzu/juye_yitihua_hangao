<template>
  <table-index :fields="fields"
               path="appraisalKeyWord"
               pagePath="appraisalKeyWord/page2"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="true"
               :insert="false"
               :back="back"
               ref="table"
               title="关键词管理"
  >
    <template v-slot:search-$btns="scope">
      <el-button type="primary" @click="repeatKeyWord">筛选重复关键词</el-button>
    </template>
    <el-dialog title="重复关键词" :visible.sync="repeatKeyWordShow" width="50%" center>
      <table-render :columns="columns" :data="repeatKeyWordData" ref="repeatTable">
        <el-table-column label="操作" width="120" header-align="center" align="center">
          <template v-slot="scope">
            <el-button type="text"
                       @click="seachByKeyword(scope.row)"
                       size="mini" width="33">查看
            </el-button>
          </template>
        </el-table-column>
      </table-render>
    </el-dialog>
  </table-index>
</template>

<script>
export default {
  data() {
    return {
      back: this.$route.query.keyWord ? true : false,
      fields: {
        keyWord: {label: '关键词', required: true, search: true},
        basisName: {label: '所属依据', required: true},
        rulesName: {label: '所属细则', required: true},
        createDate: {label: '创建时间', required: true, edit: false, dateFormat: "YYYY-MM-DD HH:mm:ss", width: 150}
      },
      defaultSearchForm: {keyWord: this.$route.query.keyWord},
      repeatKeyWordShow: false,
      columns: {
        keyWord: {label: '关键词'},
        countNum: {label: '重复次数'}
      },
      repeatKeyWordData: []
    }
  },
  methods: {
    repeatKeyWord() {
      this.$http.post("/appraisalKeyWord/repeatKeyWord")
          .then(({data}) => {
            if (data.data.length > 0) {
              this.repeatKeyWordData = data.data
              this.repeatKeyWordShow = true
            } else {
              this.$message.info("无重复关键词!")
            }
          })
    },
    seachByKeyword(row) {
      this.defaultSearchForm = {keyWord: row.keyWord}
      this.$refs.table.reload()
      this.repeatKeyWordShow = false
    }
  }
}
</script>

<style scoped>

</style>