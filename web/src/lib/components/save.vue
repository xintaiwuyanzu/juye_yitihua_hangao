<template>
  <!--  <el-button type="primary" @click="send">归档</el-button>-->
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">移交保存<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">保存选中</el-dropdown-item>
        <el-dropdown-item command="all">保存所有</el-dropdown-item>
        <el-dropdown-item command="query">保存查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <form ref="printForm" :action='action' method="post" target="_blank" style="display: none">
      <input name="fondId" :value="query.fondId"/>
      <input name="categoryId" :value="query.categoryId"/>
      <input name="formDefinitionId" :value="query.formDefinitionId"/>
      <input name="_QUERY" :value="query._QUERY"/>
      <input name="status" value="save"/>
    </form>
  </section>
</template>
<script>
import abstractComponent from "./abstractComponent";

export default {
  extends: abstractComponent,
  name: 'filing',
  data() {
    return {
      targetPerson: '',
      sendType: 'all',
      query: {},
      action: ''
    }
  },
  methods: {
    async handleCommand(command) {

      this.sendType = command
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      //todo 这里需要区分案卷  卷内件  件盒

      this.query = query


      const {data} = await this.$post('/manage/formData/updateStatusByQuery', {...query,status:'save',formDefinitionId:query.formDefinitionId})

      if(data.success){
        this.$message.success('保存成功，请在保存库查看结果！')
      }

    },
    /**
     * 显示提报dialog
     */
    async showSend() {
      this.dialogShow = true
    },
    /**
     *
     * 执行提报操作
     * @returns {Promise<void>}
     */
    async doSend() {
      let ids = ''
      this.currentSelect.forEach(v=>{
        ids = v.id+","
      })
      ids = ids.substring(0,ids.length-1)
      const {data} = await this.$post('/manage/formData/updateStatus', {ids,status:'save',formDefinitionId:this.formId})
      if(data.success){
        this.$message.success('保存成功，请在保存库查看结果！')
      }
    }
  }
}
</script>
