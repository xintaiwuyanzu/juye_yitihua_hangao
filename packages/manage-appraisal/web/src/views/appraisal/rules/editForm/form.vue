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
          rulesName:{label: '规则名称', required: true, style:{width:'500px'}},
          auxiliaryResult:{
            label: '辅助鉴定结果',
            required: true,
            fieldType:'select',
            style:{width:'500px'},
            dictKey:'kfjd'
          },
          priority:{label: '优先级', required: true,type:'number', style:{width:'500px'}},
          openRange:{label: '开放范围', required: true, style:{width:'500px'}}

      },
      show:false,
      form:{},
    }
  },
  props:{
    basisId:{type: String}
  },
  methods: {
    appraisalTypeChang(){
      this.fields.auxiliaryResult.show = false
      if(this.form.appraisalType=='KFJD'){
         this.fields.auxiliaryResult.dictKey = 'openLevel'
      }else{
        this.fields.auxiliaryResult.dictKey = 'dqjd'
      }
      this.fields.auxiliaryResult.show = true
    },
    cancelDialog(){
      this.show = false
      this.form = {}
    },
    saveForm(){
      this.loading = true
      this.$refs.form.validate((valid) => {
        if(valid){
          let url = "/appraisalRules/insert"
          if(this.form.id){
            url = "/appraisalRules/update"
          }
          this.form.basisId = this.basisId
          this.$http.post(url,this.form)
            .then(({data}) => {
              this.loading = false
              this.$emit('reload')
              this.form = {}
              this.show = false
            })
        }
      })
    },
    changeBasisParentId(){
      //this.fields.basisChildrenId.params.parentId = this.form.basisParentId
      this.form.basisChildrenId = ''
      this.$http.post("/appraisalBasis/page",{page:false,parentId:this.form.basisParentId})
          .then(({data})=>{
            this.fields.basisChildrenId.mapper = data.data
          })
    }
  }
}
</script>

