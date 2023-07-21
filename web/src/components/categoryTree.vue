<template>
  <el-tree highlight-current
           v-loading="loading"
           :data="categorys"
           accordion
           ref="tree"
           @node-click="n=>$emit('check',n)"
           default-expand-all
           node-key="id"
           :show-checkbox="showCheck">
  </el-tree>
</template>
<script>
import abstractFond from "./fondTree/abstractFond";

export default {
  extends: abstractFond,
  watch: {
    fondId() {
      this.loadCategory()
    }
  },
  data() {
    return {
      //所有门类
      categorys: [],
      //当前选中门类
      currentCategory: {}
    }
  },
  methods: {
    async loadCategory() {
      if (this.fondId) {
        this.loading = true
        const {data} = await this.$post(this.url, {type: 'category', group: this.fondId})
        if (data.success) {
          this.categorys = data.data
        }
        this.loading = false
      }
    },
    /**
     * 获取选中的节点
     * @returns {D[]}
     */
    getCheckedNodes() {
      return this.$refs.tree.getCheckedNodes();
    },
    /**
     * 获取选中的Id
     * @returns {D[]}
     */
    getCheckedKeys() {
      return this.$refs.tree.getCheckedKeys()
    },
    $init() {
      this.loadCategory()
    }
  }
}
</script>
