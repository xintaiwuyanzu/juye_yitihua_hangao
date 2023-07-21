<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button @click="drawer = true" type="primary">
      门类
    </el-button>
    <el-drawer
        title="门类"
        :visible.sync="drawer"
        direction="ttb"
        :with-header="false">
      <el-row>
        <el-col :span="24" v-if="badgeData.length > 0">
          <div style="margin-top: 50px">
            <div>
              <el-row>
                <el-col>
                  <span style="margin-left: 30px">门类</span>
                </el-col>
              </el-row>
            </div>
            <div style="margin-top: 10px">
              <el-row>
                <el-col>
                  <span v-for="item in badgeData" :key="item.value" style="margin-left: 30px">
                    <el-badge :value="item.value" :max="10000">
                    <el-button @click="secondary(item)" size="small">{{ item.key }}</el-button>
                  </el-badge>
                  </span>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-drawer>
  </section>
</template>
<script>

import abstractContainerItem from "./abstractContainerItem";

export default {
  name: "searchByCategory",
  extends: abstractContainerItem,
  data() {
    return {
      drawer: false,
      categoryName: ''
    }
  },
  computed: {
    badgeData() {
      return this.searchContainer.badgeData || []
    }
  },
  methods: {
    secondary(row) {
      this.categoryName = row.key
      this.drawer = false
      this.searchContainer.doSearch()
    },
    search() {
      this.$emit('search')
    },
    getQuery() {
      if(!this.categoryName.split(" ").join("").length == 0){
        return {
          categoryName: this.categoryName
        }
      }
    }
  }
}
</script>