<template>
  <el-form>
    <el-row>
      <el-col :span="24" >
        <el-form-item  style="margin-bottom: 2px" label="批次名称：">
          {{form.batchName}}
        </el-form-item>
      </el-col>

    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" >
          <label slot="label">创&#8194;建&#8194;人：</label>
          {{form.createPersonName}}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" label="创建时间：">
          {{form.createDate|datetime}}
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" label="档案数量：">
          {{form.archiveQuantity}}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" label="批次状态：">
          {{form.status|dict({'0': "未开始",'1':'进行中','2':'已完成'})}}
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" label="起始年度：">
          {{form.startVintages}}--{{form.endVintages}}
        </el-form-item>
      </el-col>
      <el-col :span="12">
        <el-form-item  style="margin-bottom: 2px" label="鉴定类型：">
          {{form.appraisalType|dict({'KFJD': "开放鉴定",'DQJD':'销毁鉴定'})}}
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="24">
        <el-form-item  style="margin-bottom: 2px" label="包含全宗：">
          {{form.fondNames}}
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
</template>

<script>
export default {
  name: "index",
  data(){
    return{
      form:{}
    }
  },
  props:{
    batchId:''
  },
  methods:{
    $init(){
      this.$http.post('/appraisalBatch/detail',{id:this.batchId})
          .then(({data})=>{
            if(data.success){
              this.form = data.data
            }
          })
    }
  }
}
</script>

<style scoped>

</style>
