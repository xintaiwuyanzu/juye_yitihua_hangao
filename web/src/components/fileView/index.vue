<template>
  <section>
    <div class="index_main">
      <el-dialog :visible.sync="drawer" :title="dialogName" @close="$parent.fileListDialog = false"
                 :fullscreen="dialogFull"
                 append-to-body width="80%">
        <div style="min-height: 88vh;">
          <split-pane :min-percent='20' :resizerThickness="20" :default-percent='30' split="vertical">
            <template slot="paneL">
              <div style="height: 72vh;margin-top: 10px">
                <file-tree @showFile="showFile"
                           ref="loadData"
                           :ref-id="formDataId" :ref-type="refType"
                           :keyword="keyword"
                           :group-code="groupCode"
                           :watermark="watermark"/>
              </div>
            </template>
            <template slot="paneR">
              <file-view-content :currentNode="currentNode"/>
            </template>
          </split-pane>
        </div>
      </el-dialog>
    </div>
  </section>
</template>

<script>
import fileTree from "./fileTree";
import FileViewContent from "./content";
import splitPane from 'vue-splitpane'

export default {
  name: "index",
  components: {FileViewContent, fileTree, splitPane},
  props: {
    formDataId: {type: String},
    refType: {default: 'default'},
    groupCode: {default: 'default'},
    //dialog的标题
    title: {default: '原文预览'},
    keyword: {type: String},
    //预览是否带水印
    watermark: {default: true},
  },
  data() {
    return {
      drawer: true,
      dialogFull: true,
      showHeight: 0,
      dialogName: this.title,
      currentNode: {}
    }
  },
  methods: {
    showFile(v) {
      this.dialogName = v.label
      this.currentNode = v
    },
  },
}
</script>