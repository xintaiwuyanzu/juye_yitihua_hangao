<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
<!--      <el-form-item label="操作人" prop="name">
        <el-input v-model="searchForm.name" style="width: 160px" placeholder="文件名称" clearable/>
      </el-form-item>-->
      <el-form-item>
<!--        <el-button type="success" @click="search" size="mini">搜 索</el-button>-->
<!--        <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>-->
<!--        <el-button type="primary" @click="check" size="mini" v-loading="loading">上 传</el-button>-->
      <!--  <el-button type="primary" @click="check('zww')" size="mini" v-loading="loading">检索政务网</el-button>
        <el-button type="primary" @click="check('jyw')" size="mini" v-loading="loading">检索局域网</el-button>-->
      </el-form-item>
    </el-form>

    <el-dialog title="文件上传" :visible.sync="imports" :modal-append-to-body='false' append-to-body>
      <el-upload style="text-align: center;margin-top: 50px"
                 class="upload-demo"
                 ref="uploadFiles"
                 action="api/files/upload"
                 :before-upload="beforeUpload"
                 :on-success="Push"
                 :on-error="error"
                 accept="*"
                 :limit="1"
                 :headers="myHeader"
                 :data="{
                          refId:'tool-download',
                          refType:'default'
                       }"
                 :on-exceed="handleExceed"
                 :auto-upload="false"
                 :file-list="fileList">
        <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="medium" type="success" @click="submitUpload">
          <i class="el-icon-upload el-icon--right"/>上传文件
        </el-button>
        <div slot="tip" class="el-upload__tip">上传文件最大500MB!</div>
      </el-upload>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@archive/core/src/util/formMixin'

export default {
  data() {
    return {
      searchForm: {},
      rules: {},
      showdata: [],
      fileList: [],
      autoClose: false,
      imports:false,
      myHeader: {
        $token: sessionStorage.getItem('$token')
      },
    }
  },
  methods: {
    search() {
      this.$emit('func', this.searchForm)
    },

    check() {
      //检索其它系统的新备份版本
      this.fileList=[]
      this.imports=true
/*      this.loading = true
      this.$http.post('/recovery/check', {sysName: b}).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.showdata = data.data
          this.autoClose = true

        }
        this.loading = false
      })*/
    },
    /**
     * 同步
     * @param row
     */
    recovery(row) {
      this.$http.post(this.apiPath() + '/recovery', {
        filePath: row.backupRecoveryPath,
        versionNum: row.versionNum,
        showPath: row.showPath,
        sysName: row.sysName,
      }).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.autoClose = false
          this.loadData()
        }
        this.loading = false
      })
    },
    beforeUpload(file) {
      file.name.replace(/.+\./, "");
    },
    error() {
      this.$message.error("上传失败!")

    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    Push(val) {
      if (val.success) {
        this.$message.success("上传成功!")
        this.imports = false
        this.drawer = false
        this.$emit('func')
      } else {
        this.$message.error(val.message)
      }
    },
    submitUpload() {
      this.$refs.uploadFiles.submit();
    },
  },
  mixins: [fromMixin]
}
</script>
