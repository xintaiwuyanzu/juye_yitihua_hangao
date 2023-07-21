<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary" v-show="!this.parentIndex">打印目录<i
          class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">打印选中</el-dropdown-item>
        <el-dropdown-item command="all">打印所有</el-dropdown-item>
        <el-dropdown-item command="query">打印查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <form ref="printForm" :action='action' method="post" target="_blank" style="display: none">
      <input name="fondId" :value="query.fondId"/>
      <input name="categoryId" :value="query.categoryId"/>
      <input name="formDefinitionId" :value="query.formDefinitionId"/>
      <input name="_QUERY" :value="query._QUERY"/>
    </form>
  </section>
</template>
<script>
import abstractComponent from "./abstractComponent";

export default {
  extends: abstractComponent,
  name: 'expFileMulu',
  data() {
    return {
      expType:'all',
      query: {},
      action: ''
    }
  },
  methods: {
    handleCommand(command) {
      this.expType = command
      const query = this.eventBus.getQueryByQueryType(this.expType)
      //todo 这里需要区分案卷  卷内件  件盒
      let v;
      if (this.childrenIndex) {
        //有孩子则打印案卷目录
        v = "expMuluForaj"
      } else if (this.parentIndex) {
        //有父级则打印卷内目录
        v = "expMuluForjnj"
      } else {
        //都没有则打印件盒目录
        v = "expMuluForjh"
      }
      this.query = query
      this.action = `/api/mulu/${v}`
      this.$nextTick(() => {
        this.$refs.printForm.submit()
      })
    }
  }
}
</script>
