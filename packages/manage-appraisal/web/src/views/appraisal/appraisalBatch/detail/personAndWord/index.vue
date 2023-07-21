<template>
  <el-row class="container" type="flex" justify="space-between">
    <el-col :span="14">
      <div v-for="item in wordGroup" :key="item.id" style="margin-top: 2px" class="text item">
          <span style="margin-left: 5px;" >
            <el-tag type="primary">{{ item.groupName+'---'+ (item.isApply==='0'?'不可领取':'可领取')}}</el-tag>
          </span>
      </div>
    </el-col>
    <div style="float:left;
    width:5px;
    border-right: 1px dashed black;
    padding-bottom:600px;
    margin-bottom:-600px; ">
    </div>
    <el-col :span="10">
      <div v-for="item in person" :key="item.id" style="margin-top: 2px" class="text item">
          <span style="margin-left: 5px;" >
            <el-tag type="primary">{{ item.personName }}</el-tag>
          </span>
      </div>
    </el-col>
  </el-row>
</template>

<script>
export default {
  name: "index",
  data(){
    return{
      wordGroup:[],
      person:[]
    }
  },
  props:{
    batchId:''
  },
  methods:{
    $init(){
      this.$http.post("/appraisalWordGroup/getWordGroupByBatchId",{batchId:this.batchId})
          .then(({data})=>{
            this.wordGroup = data.data
          })
      this.$http.post("/appraisalBatchPerson/page", {batchId: this.batchId})
          .then(({data})=>{
            this.person = data.data.data
          })
    }
  }
}
</script>

<style scoped>

</style>
