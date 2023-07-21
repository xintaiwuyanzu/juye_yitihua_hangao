<template>
  <section class="file_viewer">
    <el-cascader placeholder="请选择要查看的原文" :props="{ value:'id' }" :options="fileTree"
                 filterable
                 @change="handleNodeClick"
                 ref="cascader"
                 style="margin-bottom: 6px"
                 v-model="selectFile">
      <template v-slot="{ node, data }">
        <span>{{ data.label }}</span>
        <span v-if="!node.isLeaf"> ({{ data.children.length }}) </span>
      </template>
    </el-cascader>
    <water-mark class="file_container" :watermarkOpen="watermarkStatus" :waterText="watermarkText">
      <fileContentView url="/api/fileView/getFile"
                       :printStatusProps="printStatus"
                       :downloadStatusProps = "true"
                       :requestParams="params"
                       :fileType="fileType"
                       class="file_v"
                       :fileName="this.current.label"/>
    </water-mark>
  </section>
</template>
<script>

/**
 * 找到第一个文件
 * @param files
 * @param path
 */
function loadFirstChild(files, path = []) {
  for (let i in files) {
    const file = files[i]
    if (!file.children) {
      path.push(file.id)
      return {file, path}
    } else {
      path.push(file.id)
      return loadFirstChild(file.children, path)
    }
  }
}

export default {
  name: "fileContent",
  props: {
    //文件列表
    fileTree: {type: Array, default: () => ([])},
    //高亮关键字
    keyword: {type: String},
    //预览是否带水印
    watermark: {default: 'true'},
    //watermarkStatus 为真正修改水印是否显示的字段，上面的是以前系统就存在的，传的参数是String类型，这边用这个参数完成申请借阅中水印显隐的功能。
    watermarkStatus:{type:Boolean,default:true},
    printStatus:{type:Boolean},
    downloadStatus:{type:Boolean},
    getFileId: {type: Function, default: () => ([])}
  },
  data() {
    return {
      //当前选中的文件
      selectFile: [],
      //当前选中文件
      current: {},
      watermarkText:""
    }
  },
  watch: {
    //监听文件列表变化
    fileTree() {
      this.computeFileTree()
    },
  },
  computed: {
    /**
     * 请求参数
     */
    params() {
      return {
        fileId: this.current.data,
        filePath: this.current.id,
        tools: 'print',
        //写在此处去判断是否打印是否下载 是因为新建的属性传不到pdfView页面。
        printStatus:this.printStatus,
        downloadStatus:this.downloadStatus
        // tools: this.hasRole('print') ? 'print' : 'none' //TODO 将来ofd云阅读打印要带权限。现在不好使，都带着打印，没看出来问题，国产机不显示打印按钮，win显示，不知道为啥
      }
    },
    /**
     * 文件类型
     */
    fileType() {
      if (this.current.label) {
        return this.current.label.split('.').pop()
      }
      return ''
    }
  },
  methods: {
    watermarkData(){
      this.$http.post("watermark/page",{status:"1"}).then(({data})=>{
        if(data.success){
          //获取自定义的水印
          if (data.data.data[0]!==undefined){
            this.watermarkText = data.data.data[0].title
          }

        }})
    },
    async handleNodeClick() {
      const nodes = this.$refs.cascader.getCheckedNodes(true)
      let current = {}
      if (nodes.length === 1) {
        current = nodes[0].data
      }
      this.current = current
      this.getFileId(current.id)
    },

    computeFileTree() {
      let current = {}
      this.selectFile = []
      if (this.fileTree.length > 0) {
        const {path, file} = loadFirstChild(this.fileTree, [])
        this.selectFile = path
        current = file
      }
      this.current = current
      this.getFileId(current.id)
    }
  },
  mounted() {
    this.computeFileTree()
    this.watermarkData()
  }
}
</script>
<style lang="scss">
.file_viewer {
  display: flex;
  height: 100%;
  flex-direction: column;

  .file_container {
    overflow: auto;
    display: flex;
    flex-direction: column;
    flex: 1;

    .file_v {
      flex: 1;
      overflow: auto;
    }
  }
}
</style>