<template>
  <section>
    <el-link type="success" @click="onFileList">
      <div v-html="'上传'"></div>
    </el-link>
    <file-list v-on:uploadYuanwen="updateStatusYuanwen" refType="archive" :formDataId="row.id" style="margin-top: 0px;height: 0;width: 0"
               width="50%" v-if="fileListDialog" :watermark="false"/>
    <!--    <file-view :title="title" v-if="fileListDialog" :formDataId="row.id" refType="archive" style="margin-top: 5px"
                   group-code="default"
                   :watermark="false"/>-->
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'yuanwen',
  data() {
    return {
      fileListDialog: false,
      title: '原文预览'
    }
  },
  mounted() {
    this.$on('uploadYuanwen', this.updateStatusYuanwen)
  },
  methods: {
    onFileList() {
      this.fileListDialog = true
    },
    updateStatusYuanwen() {
      let param = {'categoryId': this.category.id, 'id': this.row.id, 'formId': this.formId}
      this.$http.post('/manage/formData/updateHaveYuanwen', param).then(
          ({data}) => {
            this.loading = false
            if (data.success) {
              this.row.yw_have = data.data
            } else {
              this.$message.error("更新原文信息失败")
            }
          })
    }
  }
}
</script>