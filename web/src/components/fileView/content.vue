<template>
  <section style="height: 100%">
    <div class="table-container" style="height: 100%">
      <iframe v-if="fileType==='ofdType'" ref="iframe"
              id="content"
              :src="src"
              style="width: 100%; min-height: 88vh"/>
      <el-image v-else-if="fileType==='imgType'"
                :src="`/api/fileView/getFile?fileId=${currentNode.data}&filePath=${currentNode.id}`"
                slot="append" style="max-width: 100%"/>
      <iframe v-else-if="fileType==='xmlType'" ref="iframe"
              :src="`/api/fileView/getFile?fileId=${currentNode.data}&filePath=${currentNode.id}`"
              style="width: 100%; height: 685px"/>
      <el-empty v-else description="该文件暂不支持预览"/>
    </div>
  </section>
</template>

<script>
export default {
  name: "fileViewContent",
  props: {
    currentNode: Object
  },
  data() {
    return {
      fileType: '',
      src: '',
    }
  },
  watch: {
    currentNode(v) {
      let imgType = ['PNG', 'JPG', 'JPEG', 'TIF']
      let ofdType = ['OFD', 'PDF']
      //let xmlType = ['XML']
      if (imgType.indexOf(this.getFileType(v.label)) != -1) {
        this.fileType = "imgType"
      } else if (ofdType.indexOf(this.getFileType(v.label)) != -1) {
        this.fileType = "ofdType"
        this.getFile(v)
      } /*else if (xmlType.indexOf(this.getFileType(v.label)) != -1) {
        this.fileType = "xmlType"
      }*/ else {
        this.fileType = ''
      }
    },
  },
  methods: {
    getFileType(fileType) {
      return fileType.substring(fileType.lastIndexOf('.') + 1).toUpperCase()
    },
    //调用云阅读方式，单独处理
    async getFile(v) {
      const {data} = await this.$http.post('fileView/getFile', {
        fileId: v.data,
        filePath: v.id,
      })
      if (data.success) {
        this.src = data.data
      }
    }
  }
}
</script>






