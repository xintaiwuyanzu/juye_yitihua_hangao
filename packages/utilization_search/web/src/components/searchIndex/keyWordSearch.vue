<template>
  <el-autocomplete class="search_input" style="min-width:380px"
                   v-model="keyWord"
                   placeholder="请输入任意文字搜索"
                   value-key="keyWords"
                   :fetch-suggestions="querySearchHistory"
                   @select="search"
                   @keyup.enter.native="search"
                   @input="changeStyle('block', '.el-autocomplete-suggestion')"
                   @keyup="changeStyle('block', '.el-autocomplete-suggestion')"
                   clearable>
    <el-button slot="append" icon="el-icon-search" @click="search" class="search">搜 索</el-button>
  </el-autocomplete>
</template>
<script>/**
 * 全文检索功能
 */
import abstractContainerItem from "./abstractContainerItem";

export default {
  name: "keyWordSearch",
  extends: abstractContainerItem,
  data() {
    return {
      keyWord: ''
    }
  },
  methods: {
    /**
     * 重置查询条件
     * @param row
     */
    resetQuery(row) {
      this.keyWord = row.keyWords
    },
    /**
     * 执行查询方法
     */
    search() {
      this.$emit('search')
    },
    /**
     * 查询搜索历史
     * @param keyWord
     * @param cb
     * @returns {Promise<void>}
     */
    async querySearchHistory(keyWord, cb) {
      let result = []
      if (keyWord) {
        const {data} = await this.$post('/searchHistory/getSearchHistory', {keyWord})
        if (data.success) {
          result = data.data
        }
      }
      cb(result)
    },
    /**
     * 获取子组件查询方法
     * @returns {{querysListJson: [{keyWords: default.methods.keyWords}]}}
     */
    getQuery() {
      this.changeStyle("none", ".el-autocomplete-suggestion")
      return {
        querysListJson: [{
          multiSearch: false,
          keyWords: this.keyWord
        }]
      }
    },
    //根据传进来的状态改变建议输入框的状态（展开|隐藏）
    changeStyle(status, className) {
      let dom = document.querySelectorAll(className);
      if (dom.length > 0) {    //详情返回再点搜索时 dom.length 为 0
        dom[0].style.display = status;
      }
    },
  }
}
</script>
<style lang="scss">
.search{
  background: $--color-primary-dark-1 !important;
}
</style>