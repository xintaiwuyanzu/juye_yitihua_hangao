<template>
  <section>
    <nac-info back title="编辑对象"/>
    <div>
      <el-card class="card c1">
        <h3 style="margin-bottom: 10px">基本信息</h3>
        <el-form :model="formDefinition" label-width="80px" ref="form">
          <el-form-item label="对象名称" required>
            <el-input :disabled="formDefinition.formName!==undefined" v-model="formDefinition.formName"></el-input>
          </el-form-item>
          <el-form-item label="英文名" required>
            <el-input :disabled="formDefinition.formTable!==undefined" v-model="formDefinition.formTable"></el-input>
          </el-form-item>
          <el-form-item label="对象类型" required>
            <el-radio-group v-model="formDefinition.formType">
              <el-radio :key="index" :label="i" v-for="(i,index) in types">{{i}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="描述">
            <el-input v-model="formDefinition.description"></el-input>
          </el-form-item>
        </el-form>
        <div style="text-align: right">
          <el-button @click="save" type="primary">保存表单</el-button>
        </div>
      </el-card>
      <el-card class="card">
        <div class="t">
          <h3>基本属性</h3>
          <div style="display:flex">
            <!--            <el-button type="danger">清空属性</el-button>-->
            <el-input clearable placeholder="输入字段名检索" style="margin: 0 10px" v-model="search">
              <el-button @click="getFields" icon="el-icon-search" slot="append">搜索</el-button>
            </el-input>
            <el-button @click="addF" type="primary">新增属性</el-button>
          </div>
        </div>
        <el-table :data="fields.concat(myFields)" :default-sort="{prop: 'order', order: 'ascending'}" border
                  height="400"
                  style="width: 100%">
          <el-table-column type="index"></el-table-column>
          <el-table-column label="属性名称" prop="label"></el-table-column>
          <el-table-column label="属性列名" prop="fieldAliasStr"></el-table-column>
          <el-table-column label="属性类型" prop="fieldTypeStr"></el-table-column>
          <el-table-column label="属性长度" prop="fieldLength"></el-table-column>
          <el-table-column label="排序" prop="order" sortable></el-table-column>
          <el-table-column align="center" label="操作" width="130">
            <template v-slot="scope">
              <el-button @click="updateF(scope.row)" type="text">编辑</el-button>
              <el-button @click="deleteF(scope.row)" type="text">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </div>

    <el-dialog :title="opt?'新增属性':'编辑属性'" :visible.sync="visible">
      <el-form :model="field" label-width="100px">
        <el-form-item label="名称" required>
          <el-input v-model="field.label"></el-input>
        </el-form-item>
        <el-form-item label="列名" required>
          <el-input v-model="field.fieldAliasStr"></el-input>
        </el-form-item>
        <el-form-item label="类型" required>
          <el-select v-model="field.fieldTypeStr">
            <el-option :key="index" :label="i" :value="i" v-for="(i,index) in columnTypes"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="默认长度" required>
          <el-input v-model="field.fieldLength"></el-input>
        </el-form-item>
        <el-form-item label="排序" required>
          <el-input v-model="field.order"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="field.description"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button @click="visible=false">取 消</el-button>
        <el-button @click="addField" type="primary">确 定</el-button>
      </span>
    </el-dialog>
  </section>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        columnTypes: [],
        types: ['人', '事', '物', '地', '组织'],
        formDefinition: {
          formType: '',
          id: this.$route.query.id
        },
        fields: [],
        myFields: [],
        visible: false,
        field: {},
        opt: true,
        //搜索
        search: ''
      }
    },
    methods: {
      //获取字段数据类型
      getColumnTypes() {
        this.$post('realm_class/getColumnTypes').then(({data}) => {
          this.columnTypes = data.data
        })
      },
      setForm(fd) {
        this.formDefinition = {
          id: fd.id,
          formType: fd.formType,
          formTable: fd.formTable,
          formName: fd.formName,
          description: fd.description,
          remarks: fd.remarks,
          formCode: fd.formCode
        }
      },
      //获取表定义数据
      async getFormMsg() {
        if (this.formDefinition.id) {
          const {data} = await this.$post('realm_class/getFormMsg', {formId: this.formDefinition.id})
          if (data && data.success) {
            this.fields = []
            const fd = data.data
            this.setForm(fd)
            this.myFields = fd.fields
          }
        }
      },
      //创建属性
      async addField() {
        this.field.formDefinitionId = this.formDefinition.id
        if (this.field.fieldTypeStr === 'STRING') {
          if (this.field.fieldLength === '') {
            this.field.fieldLength = 255
          }
        } else {
          this.field.fieldLength = 0
        }
        const {data} = await this.$post(this.opt ? 'realm_class/addField' : 'realm_class/updateField', this.field)
        if (data) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          if (data.success) {
            if (this.opt) {
              this.myFields.push(this.field)
            }
            this.visible = false
          }
        }
      },
      //创建表并定义字段
      async insert() {
        const params = {formDefinition: JSON.stringify(this.formDefinition)}
        const {data} = await this.$post('realm_class/addForm', params)
        if (data) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          if (data.success) {
            this.setForm(data.data)
            this.getFormMsg()
          }
        }
      },
      //更新表
      async update() {
        const {data} = await this.$post('realm_class/updateForm', this.formDefinition)
        if (data) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          this.getFormMsg()
        }
      },
      //保存
      async save() {
        if (this.formDefinition.id === undefined) {
          this.insert()
        } else {
          this.update()
        }
      },
      //新增属性按钮
      addF() {
        this.opt = true
        this.visible = true
        this.field = {
          fieldTypeStr: 'STRING',
          fieldLength: 255,
          order: this.fields.concat(this.myFields).length + 1
        }
      },
      //编辑属性按钮
      updateF(obj) {
        this.opt = false
        this.visible = true
        this.field = obj
      },
      //删除属性
      async deleteF(row) {
        this.$confirm('确定删除该属性吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {data} = await this.$post('realm_class/deleteField', {
            formDefinitionId: row.formDefinitionId,
            fieldCode: row.fieldCode
          })
          if (data && data.success) {
            this.$message.success("删除成功")
            this.getFormMsg()
          } else {
            this.$message.error("删除失败！")
          }
        })
      },
      //设置基础属性
      async setDefaultFields() {
        const {data} = await this.$post('realm_class/getDefaultFields', {type: this.formDefinition.formType})
        if (data && data.success) {
          this.fields = data.data
        }
      },
      //查询属性
      async getFields() {
        const {data} = await this.$post('realm_class/getFields', {
          formId: this.formDefinition.id,
          label: this.search
        })
        if (data && data.success) {
          this.myFields = data.data
        }
      }
    },
    mounted() {
      this.getColumnTypes()
      this.getFormMsg()
      if (this.$route.query.id === undefined) {
        this.formDefinition.formType = '人'
        this.setDefaultFields()
      }
    },
    watch: {
      'formDefinition.formType'(val) {
        if (this.$route.query.id === undefined) {
          this.setDefaultFields(val)
        }
      },
      'field.fieldTypeStr'(type) {
        if (type === 'STRING') {
          this.field.fieldLength = 255
        } else {
          this.field.fieldLength = 0
        }
      }
    }
  }
</script>

<style scoped>
  .t {
    display: flex;
    justify-content: space-between;
    margin-bottom: 10px;
  }

  .card {
    padding: 10px 10px 0 10px;
  }

  .c1 {
    margin-bottom: 10px;
    height: auto;
  }
</style>