<template>
  <section>
<!--    <el-link type="success" v-if="row.YW_HAVE==='1'||row.yw_have==='1'" @click="onFileList">-->
<!--      <div v-html="'原&#12288;文'"></div>-->
<!--    </el-link>-->
<!--    <el-link type="info" v-else @click="onFileList">无原文</el-link>-->
    <el-link type="success" @click="onFileList"><div v-html="'原&#12288;文'"></div></el-link>
    <file-list v-on:uploadYuanwen="updateStatusYuanwen" refType="archive" :formDataId="row.formDataId" style="margin-top: 5px" width="50%" v-if="fileListDialog"
     :deleter="false" :upload="false"/>
  </section>
</template>

<script>
import abstractColumnComponent from "@archive/core/src/lib/components/abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'yuanwen',
  data() {
    return {
      fileListDialog: false
    }
  },
  mounted() {
    this.$on('uploadYuanwen',this.updateStatusYuanwen)
  },
  methods: {
    onFileList() {
      this.fileListDialog = true
    },
    updateStatusYuanwen(){
        let param = {'categoryId':this.category.id,'id':this.row.id,'formId':this.formId}
        this.$http.post('/manage/formData/updateHaveYuanwen', param).then(
            ({data}) => {
              this.loading = false
              if (data.success) {
                this.row.yw_have = data.data
              }else{
                this.$message.error("更新原文信息失败")
              }
            })
    }
  }
}
</script>
