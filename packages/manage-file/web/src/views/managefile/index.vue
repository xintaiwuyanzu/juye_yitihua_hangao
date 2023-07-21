<template>
  <section>
    <nac-info>
      <el-button type="primary" size="mini" @click="getConfig" style="margin-bottom: 10px" v-if="upload">
        {{ uploadText }}
      </el-button>
    </nac-info>
    <div class="index_main" id="center">
      <list-content ref="loadData"
                    :btn-preview="btnPreview"
                    :ref-id="refId"
                    :ref-type="refType"
                    :keyword="keyword"
                    :deleter="deleter" :print="print" :transform="transform" :watermark="watermark"/>
      <upload-form ref="upload" @func="getConfigScheme"
                   :formDataId="refId"
                   :ref-type="refType"
                   :selectionButton="selectionButton"
                   :uploadButton="uploadButton"/>
    </div>
  </section>
</template>
<script>
import listContent from '@archive/core/src/components/fileList/content'
import UploadForm from '@archive/core/src/components/fileList/upload'
import {useUser} from "@dr/framework/src/hooks/userUser";

export default {
  name: "index",
  components: {listContent, UploadForm},
  setup() {
    return useUser()
  },
  data() {
    return {
      //列表相关参数
      btnPreview: '预览',
      refId: this.user.id,
      refType: 'manageFile',
      keyword: '',
      deleter: true,
      print: false,
      transform: false,
      watermark: false,
      //按钮参数
      explain: '',
      selectionButton: '选择本地资料',
      uploadButton: '开始上传',
      uploadText: '资料上传',
      upload: true
    }
  },
  methods: {
    getConfig() {
      setTimeout(() => {   //设置延迟执行
        this.$refs.upload.getConfigScheme()
      }, 1000)
    },
    getConfigScheme() {
      if (!this.refId) {
        this.$message.error("请选择一项信息!")
      } else {
        this.$refs.loadData.loadData()
        this.fileList = []
      }
    },
  }
}
</script>