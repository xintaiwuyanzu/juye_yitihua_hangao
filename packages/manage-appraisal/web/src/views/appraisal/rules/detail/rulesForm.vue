<template>
  <el-form label-width="100px">
    <el-form-item label="细则名称：">{{form.rulesName}}</el-form-item>
    <el-form-item label="创建时间：">{{form.createDate|datetime}}</el-form-item>
    <el-form-item label="辅助结果：">{{form.auxiliaryResult|dict({'kz': "控制", 'kf': "开放", 'bfkf': "部分开放", 'bfkz': "部分控制",})}}</el-form-item>
    <el-form-item label="开放范围：">{{form.openRange}}</el-form-item>
    <el-form-item label="优先级：">{{form.priority}}</el-form-item>
  </el-form>
</template>
<script>
export default {
  data() {
    return {
      form:{}
    }
  },
  props:{
    rilesId:{type:String}
  },
  methods: {
    $init(){
      this.$http.post("/appraisalRules/detail",{id:this.rilesId})
          .then(({data})=>{
            if(data.success){
              this.form = data.data
              this.getOPenRange()
            }
          })
    },
    getOPenRange(){
      this.$http.post("/appraisalOpenRange/detail",{id:this.form.openRange})
          .then(({data})=>{
            if(data.success){
              this.form.openRange = data.data.openRange
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
