<template>
  <section>
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>档案素材库</span>
      </div>
      <el-collapse v-model="activeName" accordion v-for="(item,index) in materialList" :key="index"
                   @change="handleChange(item.formDataId)">
        <el-collapse-item :title="'【'+(index+1)+'】'+item.title+'【'+item.archiveCode+'】'" :name="item.id"
                          class="materialTitle">
          <div v-for="(file,index) in fileList" :key="index" style="float: left;">
            <el-link @click="preViewer(file)" v-if="fileId===file.id" type="primary">{{ file.name }}</el-link>
            <el-link @click="preViewer(file)" v-else>{{ file.name }}</el-link>
            <el-divider direction="vertical"/>
          </div>
          <!--          <file-content-new :fileInfo="fileInfo" :url="src" style="width: 100%; height:88vh;float: left;"/>-->
          <file-content-view :url="src" :fileType="fileInfo.suffix"/>
        </el-collapse-item>
      </el-collapse>
    </el-card>
  </section>
</template>
<script>
export default {
  name: "material",
  props: {batchId: {type: String}},
  data() {
    return {
      activeName: '1',
      materialList: [],
      fileList: [],
      src: '',
      fileId: '',
      fileInfo: {}
    }
  },
  methods: {
    $init() {
      if (this.$route.query.businessId) {
        this.getMaterialList(this.$route.query.businessId)
      } else {
        this.getMaterialList(this.$route.query.id)
      }
    },
    //根据内容id获取编研档案车列表(素材库)
    async getMaterialList(batchId) {
      const {data} = await this.$http.post('archiveCarDetail/page', {batchId: batchId, page: false})
      if (data.success) {
        this.materialList = data.data
      } else {
        this.$message.error(data.message)
      }
    },
    //获取档案素材库列表
    async getFileList(id) {
      const {data} = await this.$http.post('files/list', {
        refId: id,
        refType: 'archive'
      })
      if (data && data.success) {
        this.fileList = data.data
        if (this.fileList.length > 0) {
          this.fileId = this.fileList[0].id
          await this.preViewer(this.fileList[0])
        }
      }
    },
    handleChange(val) {
      this.fileList = []
      this.getFileList(val)
    },
    //预览原文
    async preViewer(fileInfo) {
      this.fileId = fileInfo.id
      this.fileInfo = fileInfo
      this.src = `api/files/downLoad/${fileInfo.id}?download=false`
    }
  }
}
</script>

<style lang="scss">
.materialTitle {
  height: 100%;
  //width: 220px;
  //max-width: 220px;
  overflow: hidden;
  white-space: nowrap;
  //text-overflow: ellipsis;

  .el-collapse-item__header {
    color: $--color-primary;
  }
}
</style>