<template>
  <el-dialog
      :title="form.id?'编辑关键词':'新增关键词'"
      :visible.sync="dialogVisible"
      width="30%">
    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item >
          <span style="color: red" slot="label">说&#12288;明：</span>
          <span style="color: red"> 多个关键词用英文逗号隔开为且关系，例：关键词1，关键词2 </span>
      </el-form-item>
      <el-form-item label="关键词：" prop="keyWord" required>
        <el-input style="min-height: 30px" autosize v-model="form.keyWord" type="textarea"></el-input>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="saveForm(false)">保 存</el-button>
    <el-button type="primary" @click="saveForm(true)">保存并继续</el-button>
  </span>
  </el-dialog>
</template>
<script>

export default {
  data() {
    return {
      dialogVisible:false,
      form:{}
    }
  },
  methods: {
    saveForm(isGoOn){
      this.$refs.form.validate((valid) => {
        if(valid){
          let url = 'insert'
          if(this.form.id){
            url = 'update'
          }
          this.$http.post("/appraisalKeyWord/"+url,this.form)
              .then(({data})=>{
                if(data.success){
                  this.$message.success("保存成功")
                  this.$emit("reload")
                  if(isGoOn){
                    this.form.keyWord = ''
                    this.form.id = ''
                  }else{
                    this.dialogVisible = false
                  }
                }else {
                  this.$emit("reload")
                  this.$message.error(data.message)
                }
              })
        }
      })
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
