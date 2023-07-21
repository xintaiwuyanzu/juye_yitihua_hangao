<template>
  <section style="height: 100%">
    <div class="table-container" style="height: 100%">
      <el-table :data="pdfList" border height="100%" v-loading="loading" element-loading-text="加载中...">
        <el-table-column label="排序" align="center" width="50">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column label="文件名称" prop="name" show-overflow-tooltip align="center" header-align="center"/>
        <el-table-column label="文件类型" prop="suffix" width="100px" align="center" header-align="center">
        </el-table-column>
        <el-table-column label="上传日期" prop="saveDate" width="100px" align="center"
                         header-align="center">
          <template slot-scope="scope">
            {{ scope.row.saveDate|date }}
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="description" width="100px" show-overflow-tooltip align="center"
                         header-align="center"/>
        <el-table-column label="操作" align="center" header-align="center" width="180">
          <template slot-scope="scope">
            <el-link type="success" v-if="print" @click="toPrint(scope.row)">打 印</el-link>
            <el-divider v-if="print" direction="vertical"/>
            <el-link type="success" @click="downLoad(scope.row)">{{ btnPreview }}</el-link>
            <el-divider v-if="deleter" direction="vertical"/>
            <el-link type="danger" v-if="deleter" @click="deletePdf(scope.row)">删 除</el-link>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog :close-on-click-modal=true
                 :append-to-body="true"
                 :modal="true"
                 :modal-append-to-body=false
                 :destroy-on-close=true
                 :fullscreen="true"
                 :visible.sync="preview"
                 class="fileContent"
                 customClass="customWidth"
                 :title="fileInfo.name">
        <fileContentView :fileName="fileInfo.name" :fileType="fileInfo.suffix" :url="src"
                         style="height: 100%;width: 100%"/>
      </el-dialog>
    </div>
  </section>
</template>

<script>
import indexMixin from '@/util/indexMixin'

export default {
  mixins: [indexMixin],
  props: {
    //业务外键Id
    refId: {type: String},
    refType: {default: 'default'},
    groupCode: {default: 'default'},
    //是否显示打印按钮
    print: {default: false},
    //是否显示删除按钮
    deleter: {default: true},
    //是否显示转换按钮
    transform: {default: true},
    keyword: {type: String},
    btnPreview: {default: '预览'},
    //预览是否带水印
    watermark: {default: true},
  },
  data() {
    return {
      src: '',
      preview: false,
      pdfList: [],
      fileInfo: {}
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
      this.preview=false
    },
    /**
     * 在线预览文件
     * @param row
     * @returns {Promise<void>}
     */
    async downLoad(row) {
      this.fileInfo = row
      if ('doc'==row.suffix||'docx'==row.suffix){
        this.src='/api/fileView/getFile/?fileId='+row.id+'&filePath='+row.id
      }else {
        this.src = `/api/files/downLoad/${row.id}?download=false`
      }
      this.preview = true
    },
    toPrint(row) {
      this.downLoad(row)
      this.$emit("toPrint", row)
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
      if (this.refId) {
        const {data} = await this.$http.post('files/list', {
          refId: this.refId,
          refType: this.refType,
          groupCode: this.groupCode
        })
        this.loading = false
        if (data.success) {
          this.loading = false
          if (data && data.success) {
            this.pdfList = data.data
            this.$forceUpdate()
          }
        }
      }
      this.loading = false
    },
    handleTableSelect(val) {
      this.prefiling = val
    },
    doTransform(row) {
      return ['XLSX', 'XLS', 'DOC', 'DOCX', 'TXT', 'PPT', 'PPTX', 'PNG', 'JPG', 'JPEG', 'TIF', 'PDF'].indexOf(row.suffix.toUpperCase()) != -1
    }
  }
}
</script>
<style lang="scss">
.fileContent {
  height: 100%;
}
</style>