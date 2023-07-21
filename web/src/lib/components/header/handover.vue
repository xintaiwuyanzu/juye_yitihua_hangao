<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">归档<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">归档选中</el-dropdown-item>
        <el-dropdown-item command="all">归档所有</el-dropdown-item>
        <el-dropdown-item command="query">归档查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog title="移交" :visible.sync="dialogShow" append-to-body width="50%">
      <el-select placeholder="请选择全宗" v-model="targetPerson">
        <el-option v-for="fondOne in fonds" :key="fondOne.id" :label="fondOne.name" :value="fondOne.id"/>
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
      fonds: [],
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
      if (this.fonds.length === 0) {
        const {data} = await this.$post('/manage/fond/getFondAll', {page: false})
        this.fonds = data.data
      }
    },
    /**
     *
     * 执行提报操作
     * @returns {Promise<void>}
     */
    async doSend() {
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      console.log(query)
      const {data} = await this.$post('/receive/online/handover', {
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
