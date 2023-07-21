<template>
  <el-collapse>
    <el-collapse-item name="1" class="result_agg_item">
      <template slot="title">
        <span class="collapse-title">
          标签
        </span>
      </template>
      <el-row v-for="item in tagData" :key="item.key" @click.native="selectDetail(item, 'tagName')"
              class="result_agg_row">
        <span style="cursor:pointer"> {{ item.key }} ({{ item.value }})</span>
      </el-row>
    </el-collapse-item>
    <el-collapse-item name="2" class="result_agg_item">
      <template slot="title">
        <span class="collapse-title">
          门类
        </span>
      </template>
      <el-row v-for="item in badgeData" :key="item.key" @click.native="selectDetail(item, 'categoryName')"
              class="result_agg_row">
        <span style="cursor:pointer"> {{ item.key }} ({{ item.value }})</span>
      </el-row>
    </el-collapse-item>
  </el-collapse>
</template>

<script>
import abstractContainerItem from "./abstractContainerItem";

export default {
  name: "aggCount",
  extends: abstractContainerItem,
  props: {
    tagData: {type: Array},
    badgeData: {type: Array}
  },
  data() {
    return {
      tagName: '',
      categoryName: ''
    }
  },
  methods: {
    selectDetail(row, type) {
      this.categoryName = ''
      this.tagName = ''
      if (type === "categoryName") {
        this.categoryName = row.key
      } else {
        this.tagName = row.key
      }
      this.searchContainer.doSearch()
    },
    getQuery() {
      if (!this.categoryName.split(" ").join("").length == 0) {
        let cgName = this.categoryName
        this.categoryName = ''
        return {
          categoryName: cgName
        }
      }
      if (!this.tagName.split(" ").join("").length == 0) {
        let tName = this.tagName
        this.tagName = ''
        return {
          tagName: tName
        }
      }
    }
  }
}
</script>