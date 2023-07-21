<template>
  <section class="sectionH">
    <el-row>
      <el-col :span="12">
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item>您当前的位置</el-breadcrumb-item>
          <el-breadcrumb-item>借阅策略</el-breadcrumb-item>
        </el-breadcrumb>
      </el-col>
      <el-col :span="12">
        <section class="myFloat">
          <el-form :inline="true" :model="formInline" ref="formInline">
            <el-form-item label="策略名称"  prop="strategyName">
              <el-input v-model="formInline.strategyName" placeholder="请输入策略名称" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="onSubmit">搜索</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="add">添加策略</el-button>
            </el-form-item>
          </el-form>
        </section>
      </el-col>
    </el-row>


    <el-dialog title="添加策略" :visible.sync="borrowingDialogVisible" width="40%" center>
      <el-form :model="borrowingForm" ref="borrowingFormRef" :rules="borrowingRules" label-width="160px">

        <el-form-item label="策略名称" prop="strategyName">
          <el-input v-model="borrowingForm.strategyName"></el-input>
        </el-form-item>

        <el-form-item label="保管期限" prop="borrowingPeriod" >
          <el-input placeholder="请输入内容" v-model="borrowingForm.borrowingPeriod" clearable>
            <template slot="append">天</template>
          </el-input>
        </el-form-item>
        <el-form-item label="是否打印" prop="printState">
          <el-radio-group v-model="borrowingForm.printState">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="是否下载" prop="downloadState">
          <el-radio-group v-model="borrowingForm.downloadState">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="是否显示水印" prop="watermarkState">
          <el-radio-group v-model="borrowingForm.watermarkState">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type='info' @click="resetForm('borrowingFormRef')">取 消</el-button>
        <el-button type="primary" @click="borrowingPeriod('0')">保 存</el-button>
      </div>
    </el-dialog>

  </section>

</template>

<script>
export default {
  name: "addButton",
  data() {
    return {
      borrowingForm:{
        strategyName:"",
        borrowingPeriod:"7",
        printState:"0",
        downloadState:"0",
        watermarkState:"0",
        status:"",
      },
      borrowingRules:{
        strategyName: [
          { required: true, message: '请输入策略名称', trigger: 'blur' },
        ],
        borrowingPeriod: [
          { required: true, message: '请输入保管期限', trigger: 'blur' },
        ],
      },
      borrowingDialogVisible:false,
      formInline: {
        strategyName: '',
      }
    }
  },
  methods: {
    onSubmit() {
      // 向父组件传递了两个值
      this.$emit('change', this.formInline);
    },
    add() {
      this.borrowingDialogVisible = true
      //console.log('submit!');
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.borrowingDialogVisible = false
    },
    //保存策略方法 并调用父组件方法刷新表单。
    borrowingPeriod(status){
      this.$refs.borrowingFormRef.validate((valid) => {
        this.borrowingForm.status = status+""
        if (valid) {
          this.$http.post("Borrowing/insert",this.borrowingForm).then(({data})=>{
            if (data.success){
              this.$emit('change');
              this.$message.success("添加成功")
              this.borrowingDialogVisible = false
            }
          })
        } else {
          return false;
        }
      })
    }
  }
}
</script>

<style scoped>
.sectionH {
  height: 5%;
}
.myFloat {
  float: right;
}
.btn1 {
  right: 10px;
  top: 10px;
  bottom:10px;
}
.el-breadcrumb{
  padding-top: 10px !important;
}
</style>