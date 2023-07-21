<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table ref="table" :data="data" :stripe="true" :border="true">
        <el-table-column type="selection" width="40"/>
        <el-table-column type="index" label="序号" fixed align="center" header-align="center" width="55">
          <template v-slot="scope">
            {{ (page.index - 1) * page.size + scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="kerWord" label="检索词" header-align="center" align="center" show-overflow-tooltip>
          <template v-slot="scope">
            {{ keyWordShow(scope.row) }}
          </template>
        </el-table-column>
        <el-table-column prop="multiSearch" label="检索类型" header-align="center" align="center" show-overflow-tooltip
                         width="150">
          <template v-slot="scope">
            <span v-if="scope.row.secondRetrieval">二次检索</span>
            <span v-else="scope.row.multiSearch">高级搜索</span>
            <span v-else>全文检索</span>
          </template>
        </el-table-column>
        <el-table-column prop="operate" label="检索关系" header-align="center" align="center" show-overflow-tooltip
                         width="150">
          <template v-slot="scope">
            <span v-if="scope.row.operate==='AND'">并且</span>
            <span v-if="scope.row.operate==='OR'">或者</span>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="创建时间" header-align="center" align="center"
                         dateFormat="YYYY-MM-DD HH:mm:ss" show-overflow-tooltip width="150"/>
        <el-table-column
            fixed="right"
            label="操作"
            header-align="center"
            align="center"
            width="180">
          <template v-slot="scope">
            <el-link type="success" @click="searchArchive(scope.row)">检索</el-link>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination style="text-align:right;"
                     @current-change="index=>loadData(index)"
                     :current-page.sync="page.index"
                     :page-size="page.size"
                     layout="total, prev, pager, next,jumper,->"
                     :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'

export default {
  mixins: [indexMixin],
  data() {
    return {
      searchForm: {},
      options: [],
      addForm: {},
      optype: 'add',
      id: '',
      title: '',
      fields: [
        {key: 'TITLE', label: '题名'},
        {key: 'KEY_WORDS', label: '关键词'},
        {key: 'VINTAGES', label: '年度'},
        {key: 'ARCHIVE_CODE', label: '档号'},
        {key: 'FOND_CODE', label: '全宗号'}
      ],
    }
  },
  methods: {
    $init() {
      this.loadData(1)
    },
    loadData(index) {
      const params = Object.assign({}, this.page, {
        pageIndex: index - 1,
        pageSize: this.page.size
      })
      this.$http.post('/searchHistory/page', params).then(({data}) => {
        if (data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    keyWordShow(row) {
      //二次检索与高级检索 检索条件 拼接
      //二次检索与高级检索 检索条件 拼接
      let queryStr = ""
      if (row.secondRetrieval) {
        let querysList = JSON.parse(row.querysListJson)

        for (let j = 0; j < querysList.length; j++) {
          if (j > 0) {
            queryStr = queryStr + "AND"
          }
          queryStr = queryStr + "(" + this.queryToStr(querysList[j]) + ")"
        }
      } else {
        queryStr = this.queryToStr(row)
      }
      return queryStr
    },
    queryToStr(row) {
      if (row.multiSearch) {
        let keyword = ""
        let kerWord = JSON.parse(row.keyWords)
        for (let i = 0; i < kerWord.length; i++) {
          if (i > 0) {
            keyword = keyword + row.operato
          }
          for (const argumentsKey in this.fields) {
            if (kerWord[i].field === this.fields[argumentsKey].key) {
              keyword = keyword + "(" + this.fields[argumentsKey].label + "=" + kerWord[i].query + ","
            }
          }
          if (kerWord[i].type === "must") {
            keyword = keyword + "精确查询" + ")"
          } else {
            keyword = keyword + "模糊查询" + ")"
          }
        }
        return keyword
      } else {
        return row.keyWords
      }
    },
    searchArchive(row) {
      this.$router.push({
        path: '/utilization/search',
        query: {keyword: row.keyWords}
      })
    }
  }
}
</script>
