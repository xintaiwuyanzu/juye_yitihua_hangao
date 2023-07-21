<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="属性：">
        <el-input v-model="searchForm.propertyName" clearable placeholder="请输入属性"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchF" size="mini">搜 索</el-button>
        <el-button @click="resetFields">重 置</el-button>
        <el-button type="primary" size="mini" @click="add">新增</el-button>
        <el-button type="success" size="mini" @click="back">返回</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'修改':'新增')+'类别属性'" width="900px" :close-on-click-modal="false">
      <el-form borde="1px" :model="form" :rules="rules" ref="form" label-width="130px" style="margin-right: 30px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="属性：" prop="propertyName">
              <el-input v-model="form.propertyName" placeholder="请输入属性" clearable>
              </el-input>
            </el-form-item>
          </el-col>
          <!--          <el-col :span="12">-->
          <!--            <el-form-item label="属性：" prop="attributeName" >-->
          <!--              <el-input v-model="form.attributeName" placeholder="请输入属性" clearable></el-input>-->
          <!--            </el-form-item>-->
          <!--          </el-col>-->

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="save('1')" v-if="!form.id" v-loading="loading">保存并继续</el-button>
        <el-button type="primary" @click="save('2')" v-if="!form.id" v-loading="loading">保存并关闭</el-button>
        <el-button type="primary" @click="save('2')" v-if="form.id" v-loading="loading">保存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  mixins: [fromMixin],
  props: {
    /**
     * pId
     */
    pid: {type: String, default: ''},

  },
  data() {
    return {
      edit: false,
      searchForm: {
        propertyName: ''
      },
      rules: {
        propertyName: [{required: true, trigger: 'blur', message: '属性不能为空'}],
        // archiveType: [{required: true, trigger: 'blur', message: '档案类别不能为空'}],
      },
    }
  },
  methods: {
    back(){
      this.$router.go(-1)
    },
    editForm(row) {
      this.form = row
      this.edit = true
    },
    add() {
      this.form = {}
      this.edit = true
    },
    searchF() {
      this.$emit('func', this.searchForm)
    },
    apiPath() {
      return 'property'
    },
    save(v) {
      this.loading = true
      this.$refs.form.validate(valid => {
        if (valid) {
          let path = this.apiPath()
          if (this.form.id) {
            path = path + '/update'
          } else {
            this.form.archiveTypeId = this.pid
            path = path + '/insert'
          }
          this.$http.post(path, this.form).then(({data}) => {
            if (data && data.success) {
              this.form = data.data
              this.$message.success('保存成功！')
              if (v === '2') {
                this.edit = false
              } else {
                this.form = {}
              }
              this.searchF()
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
        } else {
          this.loading = false
        }
      })

    },
    resetFields() {
      this.searchForm = []
    }

  },

}
</script>
