<template>
  <el-dialog title="编辑词库"
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
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      show:false,
      form:{},
      fields:{
        wordGroupIds: {label: '包含词库组', fieldType:'checkBox',column:false,
          url:'/appraisalWordGroup/page',
          params:{page:false},
          labelKey:'groupName',  align: 'center'}
      }
    }
  },
  props:{
    batchId:''
  },
  methods:{
    cancelDialog(){
      this.show = false
      this.form = {}
    },
    saveForm(){
      this.$http.post("/appraisalBatchWordGroup/updateAppraisalBatchWordGroup",{batchId:this.batchId,wordGroupIds:this.form.wordGroupIds.join(',')})
          .then(({data})=>{
            if(data.success){
              this.$emit('loadData')
              this.show = false
            }
          })
    },
    loadData(){
      this.$http.post("/appraisalBatchWordGroup/getWordGroupIdsByBatchId",{batchId:this.batchId})
          .then(({data})=>{
            if(data.success){
              this.form.wordGroupIds=data.data
            }
          })
    }
  }
}
</script>

<style scoped>

</style>
