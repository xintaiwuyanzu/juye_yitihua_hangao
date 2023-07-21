<template>
  <section class="multi_search_item">
    <select-async v-model="operato" placeholder="条件" style="width:70px" size="mini" :mapper="operator"/>
    <section class="search_items">
      <div class="item-container" v-for="(query,index) in querys" :key="query.id">
        <el-input v-model="query.query" placeholder="请输入查询条件" size="mini" clearable>
          <select-async v-model="query.field" slot="prepend" style="width: 120px" placeholder="字段" :mapper="fields"/>
          <select-async v-model="query.type" slot="append" style="width: 80px" placeholder="类型" :mapper="queryType"/>
        </el-input>
        <el-button icon="el-icon-plus" v-if="index===0" @click="add"/>
        <el-button size="mini" v-else icon="el-icon-minus" @click="remove(query.id)"/>
      </div>
    </section>
    <el-button type="primary" size="small" @click="search">搜 索</el-button>
  </section>
</template>
<script>
import {v4} from 'uuid'
import abstractContainerItem from "./abstractContainerItem";
import {searchOperator} from "./index";

export default {
  name: "multiSearchItem",
  extends: abstractContainerItem,
  data() {
    return {
      //操作类型
      operator: searchOperator,
      //查询类型
      queryType: {must: '精确', should: '模糊'},
      //查询条件
      fields: {TITLE: '题名', KEY_WORDS: '关键词', VINTAGES: '年度', ARCHIVE_CODE: '档号', FOND_CODE: '全宗号'},
      //查询关系
      operato: 'AND',
      querys: [
        {id: v4(), field: 'TITLE', operator: 'AND', query: '', type: 'must'},
        {id: v4(), field: 'KEY_WORDS', operator: 'AND', query: '', type: 'must'}
      ]
    }
  },
  methods: {
    /**
     * 重置查询条件
     * @param row
     */
    resetMultiQuery(row) {
      this.querys = JSON.parse(row.keyWords)
      this.operato = row.operato
    },
    search() {
      this.$emit('search')
    },
    remove(id) {
      this.querys = this.querys.filter(q => q.id !== id)
    },
    add() {
      this.querys.push({
        id: v4(),
        field: '',
        operator: 'AND',
        query: '',
        type: 'must'
      })
    },
    getQuery() {
      return {
        querysListJson: [
          {
            multiSearch: true,
            keyWords: JSON.stringify(this.querys.filter(q => !!q.field && !!q.query)),
            operato: this.operato
          }
        ]
      }
    }
  }
}
</script>