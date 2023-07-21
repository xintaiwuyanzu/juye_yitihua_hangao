<template>
  <el-form label-width="100px">
    <el-form-item label="依据编码：">{{form.code}}</el-form-item>
    <el-form-item label="依据名称：">{{form.basis}}</el-form-item>
    <el-form-item label="创建时间：">{{form.createDate|datetime}}</el-form-item>
    <el-form-item label="依据详情：">{{form.description}}</el-form-item>
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
    basisId:{type:String}
  },
  methods: {
    $init(){
      this.$http.post("/appraisalBasis/detail",{id:this.basisId})
          .then(({data})=>{
            if(data.success){
              this.form = data.data
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
