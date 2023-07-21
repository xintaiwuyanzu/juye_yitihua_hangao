<template>
  <section>
    <nac-info back>
      <el-button type="primary" size="mini" @click="preview">预 览</el-button>
      <el-button type="primary" size="mini" @click="save">保 存</el-button>
    </nac-info>
    <div class="index_main">
      <el-form style="height: 100%" :model="form" :rules="rules" ref="form" label-width="100px">
        <el-tabs v-model="activeName" type="card" class="tabs">
          <el-tab-pane label="基础信息" name="first" style="height: 100%">
            <el-form-item label="模板名称:" prop="templateName" required>
              <el-input v-model="form.templateName"/>
            </el-form-item>
            <el-form-item label="模板编码:" prop="templateTypeCode">
              <el-input v-model="form.templateTypeCode"/>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="内容管理" name="second" style="height: 100%">
            <el-form-item label="内容" prop="compilationContent" style="height: 90%">
              <c-k-editor-bar v-model="form.compilationContent" :value="form.compilationContent"
                              style="height: 100%"/>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="示例" name="third" style="height: 100%">
            <el-table
                :data="tableData1"
                style="width: 100%;height: 60vh;overflow: auto"
                row-key="id"
                border
                lazy
                :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
              <el-table-column
                  prop="date"
                  label="名称"
                  width="300">
              </el-table-column>
              <el-table-column
                  prop="name"
                  label="代码示例"
                  width="220">
              </el-table-column>
              <el-table-column
                  prop="address"
                  label="备注">
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </div>
    <el-dialog :title="'【'+form.templateName+'】模板预览'" :visible.sync="dialogShow" width="90%" center="">
      <c-k-editor-bar v-model="content" :value="content"
                      style="height: 100%" :readOnly="true" :toolbar="false" disabled="disabled"/>
    </el-dialog>
  </section>
</template>

<script>
import formMixin from "@dr/auto/lib/util/formMixin";
import compilationTemplateDemo from '../compilationTemplateDemo.json'

export default {
  name: "index",
  mixins: [formMixin],
  data() {
    return {
      form: {},
      id: this.$route.query.id,
      activeName: 'first',
      tableData1: compilationTemplateDemo,
      dialogShow: false,
      content: ''
    }
  },
  watch: {
    id(v) {
      this.getDetail(v)
    }
  },
  methods: {
    $init() {
      this.getDetail(this.id)
    },
    async getDetail(v) {
      const {data} = await this.$http.post('/compilationTemplate/detail', {id: v})
      if (data.success) {
        this.form = data.data
      } else {
        this.$message.error(data.message)
      }
    },
    async save() {
      try {
        const valid = await this.$refs.form.validate()
        if (valid) {
          const {data} = await this.$http.post('/compilationTemplate/update', Object.assign(this.form))
          if (data.success) {
            this.$message.success('操作成功')
          } else {
            this.$message.error("操作失败")
          }
        } else {
          this.$message.warning('请填写基础信息！')
        }
      } catch {
        this.$message.warning('请填写基础信息！')
      }
    },
    //预览
    async preview() {
      this.dialogShow = true
      const {data} = await this.$http.post('/compilationTemplate/preview', Object.assign(this.form))
      if (data.success) {
        this.content = data.data
      } else {
        this.$message.success(data.message)
      }
    },
  }
}

</script>

<style lang="scss">
.tabs {
  height: 100%;
  overflow: scroll;

  .el-tabs__content {
    height: 100%;
  }

  .el-form-item__content {
    height: 100%;
  }

  .w-e-text {
    max-height: 100%;
  }
}
</style>