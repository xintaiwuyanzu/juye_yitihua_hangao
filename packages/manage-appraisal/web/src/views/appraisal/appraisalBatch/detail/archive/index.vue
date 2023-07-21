<template>
  <table-index :fields="fields"
               path="archiveAppraisalTask"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               back
               ref="table"
               style="height:80vh;display: flex;flex-direction: column">
    <template v-slot:table-$btns="scope">
      <el-button  @click="goDetail(scope)"  type="text" width="20">查看</el-button>
    </template>
  </table-index>
</template>
<script>
const result = {'kz':'控制','kf':'开放'}
export default {
  data(){
    return{
      fields: {
        fondCode: {label: '全宗编码', search: false, edit:false,  align: 'center', width:80},
        categoryCode: {label: '门类编码', search: false, edit:false, align: 'center', width:150},
        archiveCode: {label: '档号', search: true, edit:false, align: 'center', width:200},
        title: {label: '题名', search: true, edit:false, align: 'center'},
        vintages: {label: '年份', search: true, edit:false, align: 'center', width:70},
        filetime: {label: '文件时间', search: false, edit:false, align: 'center', width:100},
        status: {label: '鉴定状态', search: false, edit:false, align: 'center', width:70,fieldType:'select',component: 'tag',
          mapper:[{
            id: '0',
            label: '待鉴定'
          },{
            id: '1',
            label: '鉴定中'
          },{
            id: '2',
            label: '完成'
          }],},
        auxiliaryResult: {label: '辅助鉴定结果', component: 'tag', search: true,fieldType:'select',
          width:200,
          mapper:[
            {id:'null',label:'无匹配结果'}
          ],
          align: 'center'},
        personResult: {label: '人工鉴定结果', component: 'tag', search: true,fieldType:'select',
          width:200,
          mapper:[
            {id:'null',label:'无匹配结果'}
          ],
          align: 'center'},
      },
      defaultSearchForm:{
        batchId:this.$route.query.batchId,
        status:this.$route.query.status,
        personResult:this.$route.query.personResult,
        isEqual:this.$route.query.isEqual,
        vintages:this.$route.query.vintages
      },
      defaultInsertForm:{}
    }
  },
  methods:{
    $init(){
      this.$http.post("/appraisalOpenRange/page",{page:false})
          .then(({data})=>{
            data.data.forEach(v=>{
              this.fields.auxiliaryResult.mapper.push({id:v.id,label:result[v.auxiliaryResult]+v.openRange})
              this.fields.personResult.mapper.push({id:v.id,label:result[v.auxiliaryResult]+v.openRange})
            })
          })
    },
    search(){
      this.$refs.search.show = true
    },
    resLoadSearch(param){
      this.defaultSearchForm = param
      this.$refs.table.loadData(this.defaultSearchForm)
    },
    goDetail(scope){
      this.$router.push({path: '/appraisal/toBeAppraisal/newDetail', query: {
          toBeAppraisalId:scope.row.id,
          formDefinitionId:scope.row.formDefinitionId,
          formDataId:scope.row.formDataId,
          isDoAppraisal:false,
          index:scope.$index
        }})
    }
  }
}
</script>
