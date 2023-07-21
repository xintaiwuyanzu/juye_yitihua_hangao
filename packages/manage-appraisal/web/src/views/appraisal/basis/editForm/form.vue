<template>
  <section>
    <el-dialog :title="form.id?'编辑词库':'新增词库'"
               :visible.sync="show"
               :close-on-click-modal=false
               :close-on-press-escape=false
               width="40%">
      <div style="margin-top: 10px">
        <form-render label-width="110px" :fields="fields" ref="form" :model="form"></form-render>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="cancelDialog" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="saveForm">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  mixins: [formMixin],
  data() {
    return {
      fields:{
        basis:{label:'依据类型'},
        description:{label:'描述'}
      },
      show:false,
      form:{},
    }
  },
  methods: {

    cancelDialog(){
      this.show = false
      this.form = {}
    },
    saveForm(){
      this.loading = true
      this.$refs.form.validate((valid) => {
        if(valid){
          let url = "/appraisalBasis/insert"
          if(this.form.id){
            url = "/appraisalBasis/update"
          }
          this.$http.post(url,Object.assign({parentId:'default'},this.form))
            .then(({data}) => {
              this.loading = false
              this.$emit('reload')
              this.form = {}
              this.show = false
            })
        }
      })
    }
  }
}
</script>

