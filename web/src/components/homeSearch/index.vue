<template>
  <div style="text-align: center;margin-bottom: 10px">
    <el-input @select="search" @keyup.enter.native="search"
              class="el-input" clearable placeholder="请输入查询条件" style="width: 70%;margin: 15px 0;"
              v-model="keyword">
      <el-button @click="search" slot="append" class="search" type="primary">搜 索</el-button>
    </el-input>
    <!--    <search-history/>-->
  </div>
</template>

<script>
import {searchHistory} from '@archive/utilization_search/src/components/searchIndex'

export default {
  name: "index",
  components: {searchHistory},
  data() {
    return {
      keyword: ''
    }
  },
  methods: {
    search() {
      this.$emit('search', this.keyword)
    }
  }
}
</script>
<style lang="scss">
.el-input--small .el-input__inner{
  height: 34px;
  line-height: 34px;
  //border-top-left-radius:4px;
  //border-bottom-left-radius:4px;
  border: 1px solid #E5E7Ed;
}
.el-input-group__append{
  //margin-top: 10px;
  height: 34px;
  border-top-right-radius:2px;
  border-bottom-right-radius:2px;
  width: 40px;
  background: $--color-primary-dark-1;
  border: none;
  color: white;
}
.search:hover, .search:focus{
  height: 32px;
  color: rgba(255,255,255,0.8) !important;
  border-color:$--color-primary-dark-1 !important;
  background-color:$--color-primary-dark-1 !important;
}
.search{
  height: 32px;
  background: $--color-primary-dark-1 !important;
  border: none;
  text-align: center;
}
</style>