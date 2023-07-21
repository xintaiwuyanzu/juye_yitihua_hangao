<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">退回至业务系统<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">退回选中</el-dropdown-item>
        <el-dropdown-item command="all">退回所有</el-dropdown-item>
        <el-dropdown-item command="query">退回查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog title="退回" :visible.sync="dialogShow" append-to-body width="50%">
      <el-form :model="form" ref="form" label-width="80px" :rules="rules">
        <el-form-item label="说明" prop="returnReason" required>
          <el-input type="textarea" v-model="form.returnReason" clearable
                    style="width: 260px;"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="send" v-loading="loading" class="btn-submit">提交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "@archive/core/src/lib/components/abstractComponent";
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  mixins: [formMixin],
  extends: abstractComponent,
  name: 'filereturn',
  data() {
    return {
      persons: [],
      sendType: 'all',
      form: {}
    }
  },
  methods: {
    async handleCommand(command) {
      this.sendType = command
      this.dialogShow = true
    },
    async send() {
      try {
        const valid = await this.$refs.form.validate()
        if (valid) {
          await this.$confirm('确定要退回选中的数据吗？', '提醒')
          //增加 退回记录
          const query = this.eventBus.getQueryByQueryType(this.sendType)
          const {data} = await this.$http.post('/return/fileReturn', {returnReason: this.form.returnReason, ...query})
          if (data.success) {
            this.$message.success(data.data)
          } else {
            this.$message.success(data.message)
          }
        }
        this.dialogShow = false
      } catch {
      }
    },
    closeDialog() {
      this.dialogShow = false
      this.form.returnReason = ''
    }
  }
}
</script>
