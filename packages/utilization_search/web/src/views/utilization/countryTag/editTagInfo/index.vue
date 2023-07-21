<template>
  <section>
    <nac-info back title="标签管理">
      <el-button @click="save" type="primary" v-loading="loading">保存</el-button>
    </nac-info>
    <el-descriptions :column="3" border direction="vertical" size="medium" style="margin-bottom: 10px" title="标签信息">
      <el-descriptions-item label="一级标签">
        <el-input v-model="tag.labelName1st"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="二级标签">
        <el-input v-model="tag.labelName2nd"></el-input>
      </el-descriptions-item>
      <el-descriptions-item label="排序">
        <el-input v-model="tag.orderInfo"></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <h4 style="margin-bottom: 10px">词库内容</h4>
    <el-input :rows="32" style="width: 100%" type="textarea"
              v-model="tag.content"/>
  </section>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        tag: {},
        loading: false
      }
    },
    methods: {
      $init() {
        this.getTag()
      },
      async getTag() {
        const {data} = await this.$post('/stdTag/detail', {id: this.$route.query.id})
        if (data && data.success) {
          this.tag = data.data
        }
      },
      async save() {
        this.loading = true
        const {data} = await this.$post('/stdTag/update', this.tag)
        if (data && data.success) {
          this.$message.success('修改成功！')
        }
        this.loading = false
      }
    }
  }
</script>

<style scoped>
  /deep/ .ck-editor-wrapper .document-editor__editable-container .ck-editor__editable {
    padding: 10px;
  }
</style>