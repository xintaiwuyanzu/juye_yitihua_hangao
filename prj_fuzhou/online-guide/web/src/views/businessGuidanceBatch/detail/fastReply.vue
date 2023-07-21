<template>
  <el-dialog title="一键指导回复" :visible.sync="show" width="40%">
    <form-render label-width="110px" :fields="fields" ref="form" :model="form">
      <el-form-item >
        <span style="color: red" slot="label">说&#12288;明：</span>
        <span style="color: red">此操作将未回复档案将采用回复的指导结果 </span>
      </el-form-item>
    </form-render>
    <div slot="footer" class="dialog-footer" style="text-align: right">
      <el-button type="primary" @click="saveForm">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
const result = {'kz':'控制','kf':'开放'}
export default {
  name: "fastAppraisal",
  data() {
    return {
      form:{},
      show:false,
      fields: {
        result:{label: '快速指导结果', search: true, type:'textarea'}
      }
    }
  },
  methods:{
    saveForm(){
      console.log('执行')
      console.log(this.form.result)
      this.$emit('confirmReply',this.form.result)
      this.show = false
    },
    setShow(){
      this.show=true
    }
  }
}
</script>

<style scoped>

</style>
