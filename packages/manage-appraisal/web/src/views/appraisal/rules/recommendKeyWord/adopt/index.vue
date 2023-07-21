<template>
  <el-dialog title="选择添加进的依据细则"
             :visible.sync="show"
             :close-on-click-modal=false
             :close-on-press-escape=false
             width="20%">
    <div style="margin-top: 10px">
      <form-render label-width="80px" :fields="fields" ref="form" :model="form"></form-render>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="saveForm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "index",
  data(){
    return{
      show:false,
      form:{},
      fields:{
        basis:{label:'所属依据',
          url:'appraisalBasis/page',
          params:{page:false},
          fieldType:'select',
          labelKey:'basis',
          required:true,
          on:{change:this.onChange}
        },
        rule:{label:'所属细则',
          url:'appraisalRules/page',
          params:{page:false},
          fieldType:'select',
          labelKey:'rulesName',
          required:true
        }
      }
    }
  },
  methods:{
    saveForm(){
      this.$refs.form.validate(valid => {
        if(valid){
          this.$emit('savaForm',this.form.rule)
          this.show = false
        }
      })
    },
    onChange(){
      this.fields.rule.params.basisId = this.form.basis
    }
  }
}
</script>

<style scoped>

</style>
