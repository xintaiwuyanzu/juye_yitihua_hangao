<template>
  <section>
    <el-card class="manageFileView" style="height: 100%;width: 100%">
      <div slot="header" class="clearfix">
        <span>个人网盘</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="showDrawer" v-if="!readOnly">添加资料
        </el-button>
      </div>
      <file-content1 :fileTree="fileTree" :keyword="''"/>
    </el-card>
    <el-drawer
        title="资料列表"
        :visible.sync="drawer" size="50%">
      <manage-file-list :batchId="batchId" ref="manageFileList" @loadFileTree="getFileTree()"/>
    </el-drawer>
  </section>
</template>
<script>
import fileContent1 from "@archive/core/src/components/metadataFileView/fileContent";

export default {
  name: "index",
  props: {batchId: {type: String}, readOnly: {type: Boolean, default: false}},
  components: {fileContent1},
  data() {
    return {
      drawer: false,
      fileTree: []
    }
  },
  methods: {
    $init() {
      this.getFileTree()
    },
    async getFileTree() {
      let refId = this.$route.query.businessId ? this.$route.query.businessId : this.batchId;
      if (refId) {
        const {data} = await this.$http.post('manageFile/fileTree', {
          refId: refId
        })
        if (data.success > 0) {
          this.fileTree = data.data
        } else {
          this.fileTree = []
        }
      } else {
        this.fileTree = []
      }
    },
    showDrawer() {
      this.drawer = true
      // this.$refs.manageFileList.getManageFileList()
    },
  }
}
</script>

<style lang="scss">
.manageFileView {
  .file_container {
    height: 100%;
  }

  .el-card__body {
    height: 100%;
    padding: 0;
  }
}
</style>