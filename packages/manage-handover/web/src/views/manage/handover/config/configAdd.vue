<template>
  <section style="display: inline-block">
    <el-button type="primary" @click="showAdd()">添加配置</el-button>
    <el-dialog :title="form.id?'编辑到期移交配置':'添加到期移交配置'" :visible.sync="dialogVisible" append-to-body  :close-on-click-modal=true  :modal-append-to-body=false
               :destroy-on-close=true>
      <form-render :model="form" :fields="fields" label-width="120px" v-loading="loading" ref="form"/>
      <section slot="footer">
        <el-button type="info" @click="dialogVisible=false" :loading="loading">取消</el-button>
        <el-button type="primary" :loading="loading" @click="save(true)">保存并检测</el-button>
        <el-button type="primary" :loading="loading" @click="save(false)">保存</el-button>
      </section>
    </el-dialog>
  </section>
</template>
<script>
/**
 * 到期意见配置信息
 */
export default {
  name: "configAdd",
  data() {
    return {
      form: {},
      fields: {
        warningTime: {fieldType: 'dateTime', label: '预警时间', required: true},
        handoverTime: {fieldType: 'dateTime', label: '移交时限', required: true}
      },
      dialogVisible: false,
      loading: false
    }
  },
  methods: {
    /**
     * 显示编辑窗口
     * @param form
     * @return {Promise<void>}
     */
    async showAdd(form) {
      this.dialogVisible = true
      if (form) {
        this.form = Object.assign({}, form)
      } else {
        this.loading = true
        const {data} = await this.$post('/manage/handoverConfig/loadThisYearConfig')
        if (data.success && data.data) {
          this.form = data.data
        } else {
          this.form = {}
        }
      }
      this.loading = false
    },
    async save(start) {
      this.loading = true
      await this.$refs.form.validate()
      const {data} = await this.$post('/manage/handoverConfig/saveStart', {
        start,
        ...this.form
      })
      if (data.success) {
        this.$message.success('提交成功')
        this.$emit('loadData')
        this.dialogVisible = false
      } else {
        this.$message.warning(data.message)
      }
      this.loading = false
    }
  },
  beforeRouteLeave (to, from, next) {
    this.dialogVisible = false
    next()
  }
}
</script>
