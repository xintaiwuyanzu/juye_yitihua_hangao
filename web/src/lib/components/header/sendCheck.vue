<template>
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
        <el-button type="primary" @click="doSend" v-loading="loading" class="btn-submit">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import abstractComponent from "../abstractComponent";

/**
 * 头部按钮
 * 提报年检
 */
export default {
  extends: abstractComponent,
  name: "search",
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
     * 显示提报dialog
     */
    async showSend() {
      this.dialogShow = true
      this.targetPerson = ''
      if (this.persons.length === 0) {
        const {data} = await this.$post('/person/page', {page: false})
        this.persons = data.data
      }
    },
    /**
     *
     * 执行提报操作
     * @returns {Promise<void>}
     */
    async doSend() {
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      const {data} = await this.$post('/archiveTask/startTask', {
        targetPersonId: this.targetPerson,
        type: 'SEND_CHECK',
        ...query
      })
      if (data.success) {
        this.$message.success('提交成功')
      } else {
        this.$message.error(data.message)
      }
      this.dialogShow = false
    }
  }
}
</script>
