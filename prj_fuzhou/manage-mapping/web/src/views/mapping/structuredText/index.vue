<template>
  <el-tabs @tab-click="handleClick" v-model="activeName">
    <el-tab-pane label="未梳理" name="first">
      <table-index :defaultSearchForm="search1" :delete="false" :edit="false" :fields="fields" :insert="false"
                   path="structuredText"
                   ref="table0"
                   style="height: 80vh;display: flex;flex-direction: column"
                   title="结构化原文标注">
        <template v-slot:search-$btns="scope">
          <el-button @click="addStrText" type="primary" width="20">添加</el-button>
          <el-button @click="flush" type="primary" width="20">刷新</el-button>
        </template>
        <template v-slot:table-$btns="scope">
          <el-button @click="tagging(scope.row)" type="text" width="80">
            标注
          </el-button>
        </template>
      </table-index>
    </el-tab-pane>
    <el-tab-pane label="已梳理" name="second">
      <table-index :defaultSearchForm="search2" :delete="false" :edit="false" :fields="fields" :insert="false"
                   path="structuredText"
                   ref="table1"
                   style="height: 80vh;display: flex;flex-direction: column" title="关系梳理">
        <template v-slot:search-$btns="scope">
          <el-button @click="flush" type="primary" width="20">刷新</el-button>
        </template>
        <template v-slot:table-$btns="scope">
          <el-button @click="tagging(scope.row)" type="text" width="100">
            关系梳理
          </el-button>
        </template>
      </table-index>
    </el-tab-pane>
    <el-dialog title="添加结构化原文"
               :visible.sync="digYuanWen" style="width:80%;">
      <el-form :model="form" label-width="100px" ref="form">
        <el-form-item label="上传文件">
          <div style="text-align: center;width: 40%;height: 40%">
            <el-upload
                :auto-upload="false"
                :before-upload="beforeAvatarUpload"
                :file-list="fileList"
                :on-error="uploadFalse"
                :on-success="onSuccess"
                accept=".xlsx,.xls"
                action="/api/structuredText/upload"
                class="upload-demo"
                drag
                multiple
                ref="upload">
              <i class="el-icon-upload"></i>
              <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              <div class="el-upload__tip" slot="tip">
                不超过10M，只支持xls和xlsx格式
              </div>
            </el-upload>
          </div>
          <span class="dialog-footer" slot="footer"></span>
        </el-form-item>
        <el-form-item label="门类类型">
          <select-async placeholder="请选择文书类型" v-model="form.categoryName" :mapper="categoryOptions"
                        labelKey="description" valueKey="description"/>
        </el-form-item>
        <el-form-item label="档号">
          <el-input maxlength="20" placeholder="请输入档号" style="width: 50%;" v-model="form.documentNo"
                    show-word-limit/>
        </el-form-item>
        <el-form-item label="开始年度">
          <el-input v-model="form.startYear" placeholder="请输入开始年度" maxlength="4" style="width: 50%"
                    show-word-limit/>
        </el-form-item>
        <el-form-item label="年度跨度">
          <el-input v-model="form.endYear" placeholder="请输入年度跨度" maxlength="4" style="width: 50%"
                    show-word-limit/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="digYuanWen=false">取 消</el-button>
        <el-button @click="$refs.upload.submit()" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </el-tabs>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      activeName: 'first',
      fields: {
        title: {label: '标题', search: true},
        documentNo: {label: '档号'},
        categoryName: {label: '档案门类'},
        dataNum: {label: '数据数量'},
        startYear: {label: '年度', sortable: true}
      },
      search1: {status: '0'},
      search2: {status: '1'},
      digYuanWen: false,//添加|编辑的弹窗
      form: {
        categoryName: "", startYear: "", documentNo: "",
        dataNum: "", endYear: "", title: ""
      },
      //文件
      fileList: [],
      loading: false,
      categoryOptions: []
    }
  },
  methods: {
    $init() {
      this.getFonds()
    },
    handleClick(tab, event) {
      this.$refs['table' + tab.index].loadData()
    },
    tagging(row) {
      this.$router.push({
        path: '/mapping/structuredText/excelView',
        query: {id: row.id}
      })
    },
    //获取当前登陆人的全宗
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      await this.getCategory(data.data[0].id)
    },
    //获取门类
    async getCategory(fondId) {
      if (fondId) {
        const result = await this.$post('/member_authority/getCategoryByFondId', {fondId: fondId})
        result.data.data.forEach(i => {
          if (i.description != null && i.description !== "") {
            this.categoryOptions.push(i)
          }
        })
      }
    },
    addStrText() {
      this.digYuanWen = true
      this.form = {}
    },
    onSuccess(response, file, fileList) {
      this.form.dataNum = response.data;
      this.form.title = file.name.split(".")[0]
      this.submit()
    },
    uploadFalse() {
      this.$message({
        message: '文件上传失败！',
        type: 'error'
      });
    },
    async flush() {
      const {data} = await this.$post('structuredText/loadData')
      if (data && data.success) {
        this.$message({
          type: data.success ? 'success' : 'error',
          message: data.message
        })
        this.loadData()
      }
    },
    loadData() {
      if (this.activeName === 'first') {
        this.$refs['table0'].loadData()
      } else {
        this.$refs['table1'].loadData()
      }
    },
    async submit() {
      this.loading = true
      this.form.status = '0'
      const {data} = await this.$post('/structuredText/insert', this.form)
      if (data && data.success) {
        this.$message.success('保存成功')
        this.digYuanWen = false
        this.loadData()
        this.fileList = [];
        this.loading = false
      } else {
        this.$message.error('保存失败')
      }
    },
    beforeAvatarUpload(file) {
      const extension = file.name.split(".")[1] === "xls";
      const extension2 = file.name.split(".")[1] === "xlsx";
      const isLt2M = file.size / 1024 / 1024 < 10;
      if (!extension && !extension2) {
        this.$message({
          message: '上传模板只能是 xls、xlsx格式!',
          type: 'error'
        });
      }
      if (!isLt2M) {
        this.$message({
          message: '上传模板大小不能超过 10MB!',
          type: 'error'
        });
      }
      return extension || extension2;
    }
  }
}
</script>