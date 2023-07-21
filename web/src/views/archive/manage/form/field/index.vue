<template>
  <section>
    <nac-info :title="$route.query.formName+'>>元数据'">
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item style="float: right">
          <el-button type="primary" size="mini" @click="addField">添 加</el-button>
          <el-button type="success" size="mini" @click="back">返 回</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <table-render class="table-container"
                    :columns="columns"
                    :data="data"
                    showPage>
        <el-table-column label="操作" width="120" header-align="center">
          <template v-slot="scope">
            <el-button type="text" size="small" @click="editField(scope.row)">编 辑</el-button>
            <el-button type="text" size="small" @click="remove(scope.row.fieldCode)">删 除</el-button>
          </template>
        </el-table-column>
      </table-render>
      <el-dialog :title="form.id?'元数据编辑':'元数据添加'" :visible.sync="dialogVisible" :close-on-click-modal=false
                 :close-on-press-escape=false
                 :destroy-on-close=true
                 width="50%">
        <form-render style="padding: 10px 20px" :fields="editFormFields" :model="form"
                     label-width="160px"
                     ref="form" inline>
        </form-render>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="dialogVisible = false" class="btn-cancel">取 消</el-button>
          <el-button type="primary" @click="saveForm" v-loading="loading" class="btn-submit">提 交</el-button>
        </div>
      </el-dialog>
    </div>
  </section>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  mixins: [indexMixin, formMixin],
  data() {
    const mapper = {'STRING': '字符串', 'LONG': '长整型', 'NUMBER': '整型', 'DATE': '日期', 'BYTES': '字节型'}
    return {
      searchForm: {
        label: ''
      },
      formId: '',
      param: {
        formId: "",
      },
      data: [],
      fieldTypes: [],
      form: {},
      dialogVisible: false,
      active: false,
      activeState: 0,
      inactive: true,
      inactiveState: 1,
      //新代码
      columns: [
        {prop: 'label', label: '元数据名称', search: true},
        {prop: 'fieldCode', label: '元数据编号', search: true},
        {prop: 'fieldLength', label: '元数据长度', width: 100},
        {prop: 'fieldType', label: '元数据类型', width: 100, mapper},
        {prop: 'fieldState', label: '状态'}
      ],
      path: 'manage/form/findFieldList',
      editFormFields: [
        {prop: 'fieldCode', label: '元数据编码', required: true},
        {prop: 'label', label: '元数据名称', required: true},
        {
          prop: 'fieldTypeStr',
          label: '元数据类型',
          mapper,
          fieldType: 'select',
          required: true
        },
        {prop: 'fieldLength', label: '元数据长度', required: true},
        {prop: 'fieldValue', label: '元数据值'},
        {prop: 'version', label: '版本号', required: true},
        {prop: 'fieldOrder', label: '顺序号'},
        {prop: 'description', label: '元数据描述'},
        {prop: 'remark', label: '备注'},
        {prop: 'fieldAliasStr', label: '别名'},

      ]
    }
  },
  methods: {
    $init() {
      this.formId = this.$route.query.formId
      this.findFieldType()
      this.loadData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    async findFieldType() {
      const {data} = await this.$http.post('manage/form/findFieldTypes');
      if (data.success) {
        this.fieldTypes = data.data
      }
      this.loading = false
    },
    loadData() {
      this.loading = true
      const param = Object.assign({}, {
        fieldName: this.searchForm.fieldName,
        formDefinitionId: this.formId
      })
      this.$http.post('manage/form/findFieldList', param).then(({data}) => {
        if (data && data.success) {
          this.data = data.data
        }
        this.loading = false
      });
    },
    editField(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true;
    },
    addField() {
      this.dialogVisible = true;
      this.form = {}
      if (this.data) {
        this.form.fieldOrder = this.data.length + 1
      } else {
        this.form.fieldOrder = 1
      }
    },
    saveForm() {
      const fieldLength = /^[0-9]*$/;  //判断长度是否为数字
      // const fieldCode = /^[0-9a-zA-Z]*$/;  //判断字符串是否为数字和字母组合
      if (this.$refs.form) {
        this.$refs.form.validate(valid => {
          if (valid) {
            if (fieldLength.test(this.form.fieldLength) === false) {
              this.$message.error("长度只能为数字！")
              return
            }
            if (fieldLength.test(this.form.fieldOrder) === false) {
              this.$message.error("顺序号只能为数字！")
              return
            }
            this.loading = true
            let path = '/manage/form'
            if (this.form.id) {
              path = path + '/updateField'
            } else {
              path = path + '/addField'
            }
            this.form.formDefinitionId = this.formId
            this.$http.post(path, this.form).then(({data}) => {
              if (data && data.success) {
                this.dialogVisible = false
                this.formId = data.data.formDefinitionId
                // this.$router.push({path: '/archive/manage/form/field', query: {formId: data.data.formDefinitionId}})
                this.loadData()
                this.$message.success('保存成功！')
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
          }
        })
      }
    },
    remove(code) {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = Object.assign({}, {formDefinitionId: this.formId, fieldCode: code})
        this.$http.post("/manage/form/deleteField", param).then(({data}) => {
          if (data && data.success) {
            this.formId = data.data.formDefinitionId
            this.loadData()
            this.$message.success('删除成功！')
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      })
    },
    back() {
      this.$router.push({path: '/archive/manage/form'})
      // this.$router.go(-1);//返回上一层
    }
  },
  beforeRouteLeave (to, from, next) {
    this.dialogVisible = false
    next()
  }
}
</script>
