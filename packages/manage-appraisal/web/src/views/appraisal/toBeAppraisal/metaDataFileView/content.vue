<template>
  <div class="file_container">
    <iframe v-if="fileType==='ofdType'" ref="iframe" frameborder="0"
            id="content"
            :src="src" style="height: 100%;width: 100%"/>
    <el-image v-else-if="fileType==='imgType'"
              :src="`/api/fileView/getFile?fileId=${currentNode.data}&filePath=${currentNode.id}`"
              slot="append" style="max-width: 100%"/>
    <ace v-else-if="fileType==='xmlType'" style="height: 100%;width: 100%"
         :fileId="`${currentNode.data}`" :filePath="`${currentNode.id}`"/>
    <el-empty v-else description="该文件暂不支持预览" style="height: 100%"/>
  </div>
</template>
<script>
export default {
  name: "fileViewContent",
  props: {
    currentNode: {type:Object}
  },
  data() {
    return {
      fileType: '',
      src: '',
    }
  },
  watch: {
    currentNode(v) {
      if(v.label){
        let type = v.label.substr(v.label.lastIndexOf(".")+1,v.label.length).toUpperCase()
        let imgType = ['PNG', 'JPG', 'JPEG', 'TIF']
        let ofdType = ['OFD', 'PDF']
        let xmlType = ['XML', 'TXT', 'VUE', 'JS', 'CONF', 'JAVA', 'CSS', 'SCSS']
        if (imgType.indexOf(type) != -1) {
          this.fileType = "imgType"
        } else if (ofdType.indexOf(type) != -1) {
          this.fileType = "ofdType"
          this.getFile(v)
        } else if (xmlType.indexOf(type) != -1) {
          this.fileType = "xmlType"
        } else {
          this.fileType = ''
        }
      }else{
        this.fileType = ''
      }
    }
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






