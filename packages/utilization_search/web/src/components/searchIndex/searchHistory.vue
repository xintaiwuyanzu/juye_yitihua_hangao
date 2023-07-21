<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button type="primary" @click="showTable">
      检索历史
    </el-button>
    <el-drawer title="检索历史"
               :visible.sync="drawerShow"
               direction="rtl"
               size="50%"
               custom-class="table_drawer"
               append-to-body>
      <table-index :insert="false"
                   :edit="false"
                   ref="table"
                   :fields="fields"
                   path="searchHistory"
                   @row-dblclick="rowClick"
                   :showTitle="false" style="width: 100%">
        <template v-slot:search-$btns>
<!--          <el-button type="primary" @click="$router.push('/utilization/searchHistory')">查看历史</el-button>-->
        </template>
        <el-table-column prop="keyWords" label="检索词" header-align="center" align="center" show-overflow-tooltip>
          <template v-slot="scope">
            {{ keyWordShow(scope.row) }}
          </template>
        </el-table-column>
      </table-index>
    </el-drawer>
  </section>
</template>
<script>
import abstractContainerItem from "./abstractContainerItem";
import {searchOperator} from "./index";

/**
 * 查询历史组件
 */
export default {
  name: "searchHistory",
  extends: abstractContainerItem,
  data() {
    return {
      drawerShow: false,
      fields: {
        keyWords: {label: '检索词', search: true},
        multiSearch: {label: '检索类型', width: 80, mapper: {true: '高级检索', false: '全文检索'}},
        operato: {label: '检索关系', width: 80, mapper: searchOperator},
        createDate: {label: '检索时间', width: 100, dateFormat: "YYYY-MM-DD HH:mm:ss"}
      },
      fieldDist: [
        {key: 'TITLE', label: '题名'},
        {key: 'KEY_WORDS', label: '关键词'},
        {key: 'VINTAGES', label: '年度'},
        {key: 'ARCHIVE_CODE', label: '档号'},
        {key: 'FOND_CODE', label: '全宗号'}
      ],
    }
  },
  methods: {
    showTable() {
      this.drawerShow = true
      const table = this.$refs.table
      if (table) {
        table.reload()
      }
    },
    rowClick(row) {
      //告诉容器组件重置查询条件并且查询
      this.searchContainer.resetQuery(row, true)
      this.drawerShow = false
    },
    keyWordShow(row) {
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
        let keyWords = JSON.parse(row.keyWords)
        for (let i = 0; i < keyWords.length; i++) {
          if (i > 0) {
            keyword = keyword + row.operato
          }
          for (const argumentsKey in this.fieldDist) {
            if (keyWords[i].field === this.fieldDist[argumentsKey].key) {
              keyword = keyword + "(" + this.fieldDist[argumentsKey].label + "=" + keyWords[i].query + ","
            }
          }
          if (keyWords[i].type === "must") {
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
  }
}
</script>