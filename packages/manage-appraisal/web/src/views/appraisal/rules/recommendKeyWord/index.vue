<template>
  <table-index :fields="fields"
               path="appraisalRecommendKeyWord"
               :edit="false"
               :delete="false"
               :insert="false"
               back
               ref="table">
    <template v-slot:table-$btns="scope">
      <el-button  @click="toArchive(scope.row)" v-show="scope.row.status==0"  type="text" width="40">关联档案</el-button>
      <el-button  @click="adopt(scope.row)" v-show="scope.row.status==0"  type="text" width="20">采纳</el-button>
      <el-button  @click="noAdoption(scope.row)" v-show="scope.row.status==0"  type="text" width="80">不采纳</el-button>
    </template>
    <word-group ref="wordGroup" v-on:savaForm="doAdopt"></word-group>
  </table-index>
</template>

<script>

import wordGroup from './adopt'
export default {
  name: "index",
  components:{wordGroup},
  data(){
    return{
      fields:{
        keyWord:{label:'关键词'},
        openRange:{label: '开放范围',fieldType:'select',url:'appraisalOpenRange/page',params:{page:false},labelKey:'openRange'},
        auxiliaryResult:{label: '辅助鉴定结果',fieldType:'select',dictKey:'kfjd',width: 150},
        createPersonName:{label: '推荐人',width: 150},
        sourceType:{label: '数据来源',width: 150, fieldType:'select',mapper:[{
            id: 'recommend',
            label: '推荐'
          },{
            id: 'appraisal',
            label: '鉴定结果'
          }]},
        createDate:{label: '推荐时间',width: 150,dateFormat:'YYYY-MM-DD hh:mm:ss'},
        status:{label: '是否采纳',width: 100,fieldType:'select',mapper:[{
            id: '-1',
            label: '不采纳'
          },{
            id: '0',
            label: '未处理'
          },{
            id: '1',
            label: '已采纳'
          }]},
        remarks:{label: '备注',width: 200}
      },
      row:{}
    }
  },
  methods:{
    adopt(row){
      this.row = row
      this.$refs.wordGroup.show = true
    },
    noAdoption(row){
      row.status = -1
      this.$http.post("/appraisalRecommendKeyWord/update",row)
          .then(({data})=>{
            if(data.success){
              this.$message.success("处理成功")
            }
          })
    },
    doAdopt(ruleId){
      this.row.status = 1
      this.row.ruleId = ruleId
      this.$http.post("/appraisalRecommendKeyWord/adopt",this.row)
          .then(({data})=>{
            if(data.success){
              this.$message.success("处理成功")
            }
          })
    },
    toArchive(row){
      this.$router.push({path: '/appraisal/toBeAppraisal/newDetail', query: {
          archiveCode:'关键词推荐关联档案',
          formDefinitionId:row.formDefinitionId,
          formDataId:row.formDataId}})
    }
  }

}
</script>

<style scoped>

</style>
