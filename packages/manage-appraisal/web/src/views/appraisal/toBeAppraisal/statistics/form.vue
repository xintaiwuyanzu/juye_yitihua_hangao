<template>
  <el-dialog title="创建鉴定批次"
             :visible.sync="show"
             :close-on-click-modal=false
             :close-on-press-escape=false
             width="40%">
    <div style="margin-top: 10px">
      <form-render label-width="110px" :fields="fields" ref="form" :model="form"></form-render>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="info" @click="cancelDialog">取 消</el-button>
      <el-button type="primary" @click="saveForm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  data() {
    return {
      show:false,
      form:{},
      fields:{
        batchName: {label: '批次名称',required:true },
        batchRemarks: {label: '批次备注'},
      }
    }
  },
  methods: {
    cancelDialog(){
      this.form = {}
      this.show = false
    },
    saveForm(){
      this.$refs.form.validate((valid) => {
        if(valid){
          this.$emit('save',this.form)
        }
      })
    }
  }
}
</script>
