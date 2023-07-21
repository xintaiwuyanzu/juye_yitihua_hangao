<template>
  <el-card class="manageFileView" slot="paneL" style="height: 100%;width: 100%">
    <div slot="header" class="clearfix">
      <el-radio-group v-model="active" style="height: 100%;width: 60%">
        <el-radio-button label="material">素材库</el-radio-button>
        <el-radio-button label="files">个人网盘</el-radio-button>
      </el-radio-group>
      <el-button style="float: right; padding: 3px 0" type="text" size="mini" @click="routeSearch"
                 v-if="showSave&&active==='material'">搜索素材
      </el-button>
      <el-button style="float: right; padding: 3px 0" type="text" @click="showDrawer"
                 v-if="showSave&&active==='files'">添加资料
      </el-button>
    </div>
    <file-content1 :fileTree="fileTree" :keyword="''" style="height: 100%;width: 100%"/>
    <el-drawer
        title="资料列表"
        :visible.sync="drawer" size="50%">
      <manage-file-list :batchId="batchId" ref="manageFileList" @loadFileTree="getFileTree()"/>
    </el-drawer>
  </el-card>
</template>
<script>
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar"
import fileContent1 from "@archive/core/src/components/metadataFileView/fileContent";

export default {
  setup() {
    return useArchiveCar()
  },
  name: "left",
  components: {fileContent1},
  props: {
    showSave: {type: Boolean, default: false},
    readOnly: {type: Boolean, default: false},
    batchId: {type: String}
  },
  data() {
    return {
      active: 'material',
      fileTree: [],
      drawer: false,
      isListen: false,
      materialList: []
    }
  },
  mounted() {
    if (!this.isListen) {
      this.$root.$on('$carSelect', this.carSelect)
      this.isListen = true
    }
  },
  beforeDestroy() {
    this.$root.$off('$carSelect', this.carSelect)
  },
  methods: {
    carSelect(car) {
      this.getMaterialFileList(car.formDataId)
    },
    $init() {
      this.archiveCarData.show = true
      if (this.active === 'material') {
        this.getMaterialList()
      } else if (this.active === 'files') {
        this.getFileTree()
      }
    },
    /**
     * 跳转查档页面
     * @param row
     */
    routeSearch() {
      this.archiveCarData.archiveCar.id = this.$route.query.id
      this.$router.push({
        path: `/utilization/search`,
        query: {keyword: this.$route.query.compilationTitle}
      })
    },
    showDrawer() {
      this.drawer = true
    },
    async getFileTree() {
      if (this.refId) {
        const {data} = await this.$http.post('manageFile/fileTree', {
          refId: this.refId
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
    async getMaterialFileList(refId) {
      this.loading = true
      const {data} = await this.$http.post('fileView/fileTree', {
        refId: refId,
        refType: 'archive',
        groupCode: 'default'
      })
      if (data.success) {
        this.loading = false
        if (data && data.success) {
          this.fileTree = data.data
        }
      }
      this.loading = false
    },
    async getMaterialList() {
      this.loading = true
      const {data} = await this.$http.post('archiveCarDetail/page', {batchId: this.refId, page: false})
      if (data.success) {
        this.loading = false
        if (data && data.success) {
          this.materialList = data.data
          if (this.materialList.length > 0) {
            await this.getMaterialFileList(this.materialList[0].formDataId)
          }
        }
      }
      this.loading = false
    }
  },
  watch: {
    active(n) {
      this.fileTree = []
      if (n === 'material') {
        this.getMaterialList()
      } else if (n === 'files') {
        this.getFileTree()
      }
    }
  },
  computed: {
    refId() {
      return this.$route.query.businessId ? this.$route.query.businessId : this.batchId
    }
  }
}
</script>

<style lang="scss" scoped>
.manageFileView {
  .file_container {
    height: 100%;
  }

  border: 0;

  .el-card__header {
    height: 100%;
    padding: 0;
  }

  .el-card__body {
    height: 100%;
    padding: 0;
  }
}
</style>