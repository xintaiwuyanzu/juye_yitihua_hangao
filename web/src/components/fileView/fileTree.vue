<template>
  <section style="height: 100%">
    <div class="table-container" style="height: 100%">
      <el-tree :data="pdfTree" @node-click="handleNodeClick"/>
    </div>
  </section>
</template>

<script>
  import indexMixin from '@/util/indexMixin'

  export default {
    name: "fileTree",
    mixins: [indexMixin],
    props: {
      //业务外键Id
      refId: {type: String},
      refType: {default: 'default'},
      groupCode: {default: 'default'},
      keyword: {type: String},
      //预览是否带水印
      watermark: {default: true},
  },
  data() {
    return {
      pdfTree: [],
      current: {},
    }
  },
  watch: {
    refId() {
      this.loadData()
    }
  },
  methods: {
    $init() {
      this.loadData();
    },
    deletePdf(row) {
      this.$confirm('此操作将进行删除信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('files/delete/' + row.id)
            .then(({data}) => {
              if (data.success) {
                this.loadData()
                this.$message.success("删除成功")
                this.$emit("uploadYuanwen")
              }
            })
      })
    },
    fileToPdf(row) {
      this.loading = true
      this.$http.post('ofd/fileToOfd/', {fileId: row.id})
          .then(({data}) => {
            if (data && data.success) {
              this.$message.success(data.data)
              this.loadData()
            }
            this.loading = false
          })
    },
    async loadData() {
      this.loading = true
      const {data} = await this.$http.post('fileView/fileTree', {
        refId: this.refId,
        refType: this.refType,
        groupCode: this.groupCode
      })
      this.loading = false
      if (data.success) {
        this.loading = false
        if (data && data.success) {
          this.pdfTree = data.data
          if (this.pdfTree.length > 0 && this.pdfTree[0].children == null) {
            await this.handleNodeClick(this.pdfTree[0])
          }
          this.$forceUpdate()
        }
      }
      this.loading = false
    },
    async handleNodeClick(v) {
      this.current = v
      //子节点 点击获取文件
      if (v.children == null) {
        this.$emit("showFile", v)
      } else {
        //文件夹
      }
    },
  }
}
</script>

<style lang="scss" scoped>
  ::v-deep .el-tree-node.is-current > .el-tree-node__content {
    background: #f0f4ff;
    color: #db1717;
    font-weight: bold;
  }
</style>