<template>
  <section>
    <el-button type="primary" @click="showDialog">上传目录</el-button>
    <el-dialog width="50%" title="上传目录"
               @close="closeForm"
               :visible.sync="dialogShow"
               :close-on-click-modal=true
               :modal-append-to-body=false
               :destroy-on-close=true
               class="dialog">
      <el-row :gutter="10" style="margin-top: 10px">
        <el-col :span="8">
          <fond-tree autoSelect @check="formCategoryCheck" ref="fondTree" :withPermission="true"
                     style="height: 400px;overflow: scroll"/>
        </el-col>
        <el-col :span="16">
          <el-form :model="form" ref="form" label-width="100px">
            <el-form-item label="数据来源" prop="sourceCode" required>
              <select-dict v-model="form.sourceCode" placeholder="请选择数据来源"
                           type="source"/>
            </el-form-item>
            <el-form-item label="导入方式" prop="impType" required>
              <el-radio-group v-model="form.impType">
                <el-radio label="1">目录+原文</el-radio>
                <el-radio label="2">包结构</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="元数据方案" prop="formDefinitionId" v-show="formDefinition.length>0" required>
              <el-select v-model="form.formDefinitionId" placeholder="请选择元数据方案">
                <el-option
                    v-for="item in formDefinition"
                    :key="item.formDefinitionId"
                    :label="item.formName"
                    :value="item.formDefinitionId">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="导入方案" prop="impSchemaId" required v-if="form.impType == '1'">
              <select-async v-model="form.impSchemaId" placeholder="选择导入方案"
                            url="/impexpscheme/page"
                            labelKey="name" valueKey="id" :params="keyMapParams"/>
            </el-form-item>
            <el-form-item label="目录附件">
              <el-upload ref="uploadFile"
                         action="api/receive/offline/newBatch"
                         accept="text/xml, application/xml,.xml,.xlsx,.xls,.dbf,.accdb"
                         :before-upload="beforeUpload"
                         :on-success="Push"
                         :data="uploadData"
                         :file-list="fileList"
                         :limit="1"
                         :auto-upload="false">
                <el-button slot="trigger" type="primary" icon="el-icon-search">选取文件</el-button>
                <div slot="tip" v-if="form.impType == '1'">支持xlsx,xls,xml,dbf,accdb类型文件</div>
                <div slot="tip" v-if="form.impType == '2'">只支持xml类型文件</div>
              </el-upload>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="routeSchema">导入方案配置
        </el-button>
        <el-button type="primary" @click="submitUpload" v-loading="loading">上 传</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
/**
 * 目录附件文件上传弹窗
 */
export default {
  data() {
    return {
      //导入导出方案查询条件
      keyMapParams: {page: false, schemeType: '1'},
      form: {
        //导入方式默认为 目录+原文
        impType: '1',
        //表单定义Id
        formDefinitionId: '',
        //导入导出方案id
        impSchemaId: '',

        sourceCode: '',
      },
      dialogShow: false,
      loading: false,
      //当前选择的全宗
      fond: {},
      fileList: [],
      //当前选择的门类
      category: {},
      //门类所有的元数据方案
      formDefinition: [],
      //提示信息
      pointOut: false
    }
  },
  computed: {
    /**
     * 附件上传额外带的参数
     */
    uploadData() {
      return {
        fondId: this.fond.id,
        fondCode: this.fond.code,
        categoryId: this.category.id,
        categoryCode: this.category.code,
        type: 'IMP',
        name: 'RECEIVE',
        ...this.form
      }
    }
  },
  methods: {
    $init() {
      this.dialogShow = false
    },
    // cancel(){
    //   this.dialogShow = false
    //   this.$refs.uploadFile.clearFiles()
    // },
    /**
     * 弹窗关闭时清空数据
     */
    closeForm() {
      this.formDefinition = []
      this.fond = {}
      this.category = {}
      this.dialogShow = false
      this.form = {}
    },
    /**
     * 全宗门类选中回调
     * @param v
     */
    async formCategoryCheck(v) {
      this.formDefinition = []
      this.$refs.fondTree.fonds.forEach(item => {
        if (this.$refs.fondTree.selectFond === item.id) {
          this.fond = item.data
        }
      })
      this.category = v.data
      await this.loadFormDefinition(v.data.id)
    },
    /**
     * 根据门类Id查询门类表单方案
     * @param categoryId
     * @returns {Promise<void>}
     */
    async loadFormDefinition(categoryId) {
      const {data} = await this.$http.post('manage/categoryconfig/page', {
        businessId: categoryId,
        page: false
      })
      if (data.success) {
        console.log(data)
        data.data.forEach(item => {
          if (item.default) {
            if (item.arcFormId != null && item.arcFormName != null && item.arcFormId.length > 0) {
              this.formDefinition.push({formDefinitionId: item.arcFormId, formName: item.arcFormName})
            }
            if (item.fileFormId != null && item.fileFormName != null && item.fileFormId.length > 0) {
              this.formDefinition.push({
                formDefinitionId: item.fileFormId,
                formName: item.fileFormName
              })
            }
            if (item.proFormId != null && item.proFormName != null && item.proFormId.length > 0) {
              this.formDefinition.push({formDefinitionId: item.proFormId, formName: item.proFormName})
            }
          }
        })
      }
    },
    //显示弹出框
    showDialog() {
      this.dialogShow = true
      // if (this.$refs.form) {
      //   this.$nextTick(() => {
      //     this.$refs.form.clearValidate()
      //   })
      // }
    },
    beforeUpload(file) {
      const FileExt = file.name.replace(/.+\./, "");
      if (['xml', 'xlsx', 'xls', 'dbf', 'accdb'].indexOf(FileExt.toLowerCase()) === -1) {
        this.$message.warning('请选择指定类型的附件');
        return false;
      }
    },
    Push() {
      this.$message.success('上传成功')
      this.$emit('loadData')
      // this.$refs.uploadFile.clearFiles()
      this.dialogShow = false
      this.fileList = []
    },
    /**
     * 提交表单
     * @returns {Promise<void>}
     */
    async submitUpload() {
      try {
        //先校验表单
        const valid = await this.$refs.form.validate()
        if (valid) {
          this.loading = true
          //在使用附件上传
          await this.$refs.uploadFile.submit();
          this.loading = false
        } else {
          this.$message.warning('请填写完整表单')
        }
      } catch {
      }
    },
    /**
     * 跳转导入导出方案
     */
    routeSchema() {
      this.dialogShow = false
      this.$router.push('/archive/manage/impscheme')
    }
  }
}

</script>
<style lang="scss">
.dialog {
  overflow: hidden;

  .el-dialog__body {
    padding: 25px 15px 0 15px;
  }
}
</style>