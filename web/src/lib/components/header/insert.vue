<template>
  <section>
    <el-button v-show="show" type="warning" @click="showDialog">插卷</el-button>
    <el-dialog title="插卷" :visible.sync="dialogVisible" width="50%">
      <el-form :model="insertForm" :rules="rules" ref="form" label-width="160px">
        <el-form-item label="案卷档号:" prop="ajArchiveCode">
          <el-col :span="8">
            <el-input placeholder="请输入案卷档号" v-model="insertForm.ajArchiveCode" @input="$forceUpdate()"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button type="primary" @click="insertWj">确 定</el-button>
                </span>
    </el-dialog>
  </section>
</template>
<script>
  import abstractColumnComponent from "../abstractColumnComponent";

  /**
 * 头部按钮
 * 插卷按钮
 */
export default {
  extends: abstractColumnComponent,
  name: 'insert',
  computed: {
    //按钮是否显示
    show() {
      //选择一条数据，并且有父的列表页面才行
      return this.currentSelect.length > 0 && this.parentIndex
    }
  },
  data() {
    return {
      selectData: {},
      insertForm: {},
      dialogVisible: false,
      rules: {
        ajArchiveCode: [{required: true, message: '请输入案卷档号', trigger: 'blur'}]
      },
    }
  },
  methods: {
    //显示弹出框
    showDialog() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      //检查是否有已组卷的档案
      let flag = this.currentSelect.some(item => {
        if (item.AJDH) {
          return true
        }
      })
      if (flag) {
        this.$message.warning("所选档案文件中含有已组卷的档案，请重新选择")
        return;
      }
      this.dialogVisible = true
    },
    insertWj() {
      this.selectData.ids = ''
      this.currentSelect.forEach(item => {
        this.selectData.ids += item.id + ','
      })
      this.$http.post('/manage/formData/insertFile', {
        ajFormDefinitionId: this.parentIndex.formId,
        wjFormDefinitionId: this.formId,
        ajArchiveCode: this.insertForm.ajArchiveCode,
        ids: this.selectData.ids,
        fondId: this.fond.id,
        categoryId: this.category.id
      }).then(({data}) => {
        if (data.success) {
          this.eventBus.$emit("loadData")
          this.$message.success('插卷成功')
          this.dialogVisible = false
        } else {
          this.$message.error(data.message)
        }
      })
    }
  },
}
</script>
