<template>
  <section >
    <el-row>
      <el-col :span="left" >
        <div @mouseover="focusLeft" @mouseout="focusRight" >
        <file-tree @showFile="showFile"
                   ref="loadData"
                   :ref-id="formDataId" :ref-type="refType"
                   :keyword="keyword"
                   :group-code="groupCode"
                   :watermark="watermark"/>
        </div>
      </el-col>
      <el-col :span="right" >
        <file-view-content style="height: 74vh" :currentNode="currentNode"/>
      </el-col>
    </el-row>
  </section>
</template>

<script>
import fileTree from "./fileTree"
import FileViewContent from "./content"
import splitPane from 'vue-splitpane'

export default {
  name: 'metadataFileView',
  components: {FileViewContent, fileTree, splitPane},
  props: {
    //表单定义Id
    formDefinitionId: {type: String},
    //表单实例Id
    formDataId: {type: String},
    refType: {default: 'default'},
    groupCode: {default: 'default'},
    //dialog的标题
    title: {default: '查看档案目录详情'},
    keyword: {type: String},
    //预览是否带水印
    watermark: {default: true},
    //表单数据
    formData: {type: Object, default: () => ({})},
    //是否显示标题头
    showHeader: {type: Boolean, default: true},
    //默认拆分比例
    defaultPercent: {type: Number, default: 20}
  },
  data() {
    return {
      drawer: true,
      dialogFull: true,
      showHeight: 0,
      dialogName: '',
      currentNode: {},
      left:8,
      right:16,
      file:''
    }
  },
  methods: {
    showFile(v) {
      this.file = v
      this.currentNode = v
    },
    focusLeft(){
      this.left=16
      this.right = 8
    },
    focusRight(){
      this.left=2
      this.right = 22
      this.currentNode = this.file
    }
  },
}
</script>
<style lang="scss">
.metaView {
  display: flex;

  .index_main {
    overflow: auto;
  }

  .vue-splitter-container {
    display: flex;

    .splitter-pane {
      flex: 1;
      overflow: auto;
    }
  }

  .file_container {
    height: 100%;
  }

  //元数据显示
  .meta_info {
    display: flex;
    flex-direction: column;
    padding-left: 10px;
    overflow: auto;

    .detailMain {
      flex: 1;
      overflow: auto;
      margin: 10px 0px;
    }
  }
}
</style>
