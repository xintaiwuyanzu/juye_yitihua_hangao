<template>
  <section>
    <nac-info title="检测方案" back>
      <el-button type="primary" size="mini" @click="addField">添 加</el-button>
    </nac-info>
    <div class="index_main">
      <el-table class="table-container" :data="data" border height="100%">
        <el-table-column type="selection" width="40"/>
        <el-table-column prop="schemeName" label="方案名称" align="center"/>
        <el-table-column label="检测环节" align="center">
          <template slot-scope="scope">
            {{ scope.row.checkLink|dict('checkLink') }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" align="center"/>
        <el-table-column label="操作" align="center" header-align="center" width="240">
          <template slot-scope="scope">
            <el-link type="primary" size="mini" @click="editField(scope.row)">编 辑</el-link>
            <el-divider direction="vertical"></el-divider>
            <el-link type="danger" size="mini" @click="remove(scope.row.id)">删 除</el-link>
            <el-divider direction="vertical"></el-divider>
            <el-link type="primary" size="mini" @click="detail(scope.row)">查看详情</el-link>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog :title="form.id?'检测方案编辑':'检测方案添加'" :visible.sync="dialogVisible" :close-on-click-modal=false
               :destroy-on-close="true" @close="dialogVisible =false"
               :close-on-press-escape=false width="30%">
      <el-form ref="form" :model="form" label-width="110px" :rules="rules">
        <el-form-item label="检测方案名称" prop="schemeName">
          <el-input v-model="form.schemeName" placeholder="请输入显示方案名称"/>
        </el-form-item>
        <el-form-item label="检测环节" prop="checkLink">
          <select-dict type="checkLink" v-model="form.checkLink"/>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea"
                    placeholder="请输入描述"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogVisible = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="saveForm" v-loading="loading" class="btn-submit">提 交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'

export default {
  mixins: [indexMixin],
  data() {
    return {
      rules: {
        schemeName: [
          {required: true, message: '方案名称不能为空', trigger: 'blur'}
        ],
        checkLink: [
          {required: true, message: '检测环节不能为空', trigger: 'blur'},
          {required: true, message: '检测环节不能为空', trigger: 'change'}
        ],
      },
      formId: this.$route.query.formId,
      param: {
        formId: "",
      },
      data: [],
      form: {},
      dialogVisible: false,
    }
  },
  methods: {

    $init() {
      if (!this.formId) {
        this.$message.error("参数不全")
        this.$router.back()
      } else {
        this.loadData()
      }
    },
    async loadData() {
      this.loading = true
      const {data} = await this.$http.post('fournaturescheme/page', {page: false, businessId: this.formId})
      if (data && data.success) {
        this.data = data.data
      } else {
        this.data = []
      }
      this.loading = false
    },
    editField(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true;
    },
    addField() {
      this.form = {}
      this.dialogVisible = true;
    },
    async saveForm() {

      const valid = await this.$refs.form.validate()
      if (valid) {
        const params = Object.assign({businessId: this.formId}, this.form)
        delete params.fields
        delete params.meta
        this.loading = true
        const {data} = await this.$http.post(`/fournaturescheme/${this.form.id ? "update" : "insert"}`, params)
        if (data && data.success) {
          this.dialogVisible = false
          this.loadData()
          this.$message.success('保存成功！')
        } else {
          this.$message.error(data.message)
        }
      } else {
        this.$message.error("请填写完整表单！")
      }
      this.loading = false
    },
    remove(id) {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/fournaturescheme/delete", {id}).then(({data}) => {
          if (data && data.success) {
            this.loadData()
            this.$message.success('删除成功！')
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      })
    },
    detail(row) {
      this.$router.push({
        path: './fournaturescheme/item',
        query: {id: row.id, formDefinitionId: this.formId}
      })
    }
  }
}
</script>
