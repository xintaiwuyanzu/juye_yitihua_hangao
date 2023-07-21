<template>
<!--  <el-button type="primary" v-if="currentSelect.length>0" @click="send">移交</el-button>-->

  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">移交<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">移交选中</el-dropdown-item>
        <el-dropdown-item command="all">移交所有</el-dropdown-item>
        <el-dropdown-item command="query">移交查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog title="移交" :visible.sync="dialogShow" append-to-body width="50%">
      <el-select placeholder="请选择接收人" v-model="targetPerson">
        <el-option v-for="person in persons" :key="person.id" :label="person.userName" :value="person.id"/>
      </el-select>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="send" v-loading="loading" class="btn-submit">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "./abstractComponent";

export default {
  extends: abstractComponent,
  name: 'send',
  data() {
    return {
      targetPerson: '',
      persons: [],
      sendType: 'all'
    }
  },
  methods: {
    handleCommand(command) {
      this.sendType = command
      this.showSend()
    },
    /**
     * 显示dialog
     */
    async showSend() {
      this.dialogShow = true
      this.targetPerson = ''
      if (this.persons.length === 0) {
        const {data} = await this.$post('/person/page', {page: false})
        this.persons = data.data
      }
    },
    async send() {
      try {
        await this.$confirm('确定要移交选中的数据吗？', '提醒')
        //todo 档案移交更改状态及机构信息 , 移交流程缺失
        this.eventBus.$emit('changeStatus', {ids: this.currentSelect.map(c => c.id).join(","), status: 'PRE'})
        //增加 移交记录

      } catch {
      }
    },

  }
}
</script>
