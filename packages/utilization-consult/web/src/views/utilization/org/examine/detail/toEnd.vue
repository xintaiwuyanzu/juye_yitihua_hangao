<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button @click="toSuccess" type="primary">查档完成</el-button>
    <el-dialog :visible.sync="show" title="查档完成说明" width="40%" >
      <form-render label-width="100px" :fields="fields" ref="form" :model="form"></form-render>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="show = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="saveForm">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>

export default {
  data() {
    return{
      show:false,
      form:{},
      fields:{
        remarks:{label:'结果说明',required: true,type: 'textarea',singleLine: true}
      }
    }
  },
  methods: {
    toSuccess(){
      this.show = true
    },
    saveForm(){
      this.$http.post("/utilization/consult/toEnd",{id:this.$route.query.id,remarks:this.form.remarks})
          .then(({data})=>{
            if(data.success){
              this.$message.success("查档已完成")
              this.$router.back()
            }
          })
    }
  }
}
</script>
