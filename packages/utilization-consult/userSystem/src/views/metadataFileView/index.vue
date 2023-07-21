<template>
  <section class="metaView">
    <nac-info :title="title+dialogName" back v-if="showHeader">
      <slot/>
    </nac-info>
    <section class="index_main">
      <split-pane :default-percent='20' split="vertical">
        <file-tree @showFile="showFile"
                   slot="paneL"
                   ref="loadData"
                   :ref-id="formDataId" :ref-type="refType"
                   :keyword="keyword"
                   :group-code="groupCode"
                   :watermark="watermark"/>
        <file-view-content slot="paneR" :currentNode="currentNode"/>
      </split-pane>
    </section>
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
    defaultPercent: {type: Number, default: 60}
  },
  data() {
    return {
      drawer: true,
      dialogFull: true,
      showHeight: 0,
      dialogName: '',
      currentNode: {},
    }
  },
  methods: {
    showFile(v) {
      this.dialogName = '【当前查看文件名称】：' + v.label
      this.currentNode = v
    },
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