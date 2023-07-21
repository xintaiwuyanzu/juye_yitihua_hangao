<template>
  <search-index ref="search" :keyWord="keyWord" :aggs="true" :category="category" :fondCode="fondCode">
    <search-history/>
    <el-button style="margin-left: 5px" type="primary" @click="$router.back()" v-if="keyWord">返回
    </el-button>
    <template v-slot:btns="{item}">
      <!--      <el-button style="margin-right: 5px" type="primary" @click="toTagAnalysis(item.id,item.formDefinitionId)">词性分析</el-button>-->
      <addArchiveCar style="margin-left: 10px" :formDataId="item.id" :formDefinitionId="item.formDefinitionId"/>
    </template>
  </search-index>
</template>
<script>
import {searchByCategory, searchHistory} from "../../../components/searchIndex";

export default {
  components: {searchHistory, searchByCategory},
  computed: {
    keyWord() {
      return this.$route.query.keyword
    },
    category() {
      return this.$route.query.category
    },
    fondCode(){
      return this.$route.query.fondCode
    }
  },
  methods: {
    /*
    * 点击词性分析跳转页面*/
    toTagAnalysis(formDataId, formDefinitionId) {
      this.$router.push({
        path: '/utilization/tagAnalysis',
        query: {
          formDefinitionId: formDefinitionId,
          formDataId: formDataId
        }
      })
    }
  }
}
</script>