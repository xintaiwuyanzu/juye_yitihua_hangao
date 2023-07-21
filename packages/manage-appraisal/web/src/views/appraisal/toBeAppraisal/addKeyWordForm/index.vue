<template>
  <section>
    <div style="overflow:scroll;">
      <form-render label-width="80px" :fields="fields" ref="form" :model="form" style="margin-right: 5px">
      </form-render>
      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="primary" @click="saveForm">保 存</el-button>
      </div>
    </div>
  </section>
</template>
<script>
export default {
  data(){
    return{
      form:{},
      fields:{
        keyWord:{label: '关键词',  align: 'center', required: true,},
        auxiliaryResult: {label: '辅助结果',required: true,
          fieldType:'select',
          mapper:[{
            id: 'kz',
            label: '控制'
          }, {
            id: 'kf',
            label: '开放'
          }],
          on:{change:this.onChange}},
        openRange: {label: '开放范围',required: true,
          fieldType:'select',
          mapper:[],
          labelKey:'openRange'
        },
        remarks:{label: '备注',  align: 'center'},
      },
      params:{
        page: false, auxiliaryResult:'kz'
      },
    }
  },
  props:{
    formDataId:{type:String},
    formDefinitionId:{type:String},
  },
  methods:{
    saveForm(){
      this.$http.post("/appraisalRecommendKeyWord/insert",Object.assign({
        sourceType:'recommend',
        formDataId:this.formDataId,
        formDefinitionId:this.formDefinitionId,
        batchId:sessionStorage.getItem("batchId")},this.form))
          .then(({data})=>{
              if(data.success){
                this.$message.success("添加成功")
              }
          })
    },
    onChange(v){
      this.form.openRange = ''
      this.params.auxiliaryResult=v
      this.$http.post("appraisalOpenRange/page", this.params)
          .then(({data})=>{
            this.fields.openRange.mapper = data.data
          })
    }
  }
}
</script>

<style scoped>

</style>
