<template>
  <table-index :fields="fields"
               path="appraisalRecommendKeyWord"
               :edit="false"
               :delete="false"
               :insert="false"
               :defaultSearchForm="defaultSearchForm"
               back
               ref="table">
    <template v-slot:table-$btns="scope">
      <el-button  @click="deleteOne(scope.row)" v-show="scope.row.status==0"  type="text" width="50">删除</el-button>
    </template>
  </table-index>
</template>

<script>

import {useUser} from "@dr/framework/src/hooks/userUser";

export default {
  name: "index",
  data(){
    return{
      fields:{
        keyWord:{label:'关键词'},
        auxiliaryResult:{label: '辅助鉴定结果',fieldType:'select',dictKey:'kfjd',width: 150},
        openRange:{label: '开放范围',fieldType:'select',url:'appraisalOpenRange/page',params:{page:false},labelKey:'openRange'},
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
      row:{},
      defaultSearchForm:{'batchId':sessionStorage.getItem('batchId'),createPerson: this.user.id}
    }
  },
  setup() {
    return useUser()
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
    doAdopt(workGroupId){
      this.row.status = 1
      this.row.workGroupId = workGroupId
      this.$http.post("/appraisalRecommendKeyWord/adopt",this.row)
          .then(({data})=>{
            if(data.success){
              this.$message.success("处理成功")
            }
          })
    },
    deleteOne(row){
      this.$confirm("确认删除？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post("/appraisalRecommendKeyWord/delete", {id:row.id})
            .then(({data})=>{
              if(data.success){
                this.$message.success("删除成功")
                this.$refs.table.reload()
              }
            })
      })
    }
  }

}
</script>

<style scoped>

</style>
