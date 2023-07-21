<template>
    <table-index :fields="fields"
                 path="appraisalBatchTask"
                 :defaultInsertForm="defaultInsertForm"
                 :defaultSearchForm="defaultSearchForm"
                 :edit="false"
                 :delete="false"
                 :insert="false"
                 back
                 ref="table"
                 deleteMulti
                 style="height: 70vh;display: flex;flex-direction: column">
      <template v-slot:search-$btns="scope">
        <el-button v-if="'true'===$route.query.createPerson" type="primary" width="20"  @click="applyTask(scope)" >领取任务</el-button>
        <el-button v-else type="primary" width="20"  @click="distributionTask(scope)" >分配任务</el-button>
      </template>
      <template v-slot:table-$btns="scope">
        <el-button type="text" width="20"  @click="detail(scope)" >进入</el-button>
      </template>
      <distribution ref="distribution"  v-on:reload="reload" :batch-id="$route.query.batchId" ></distribution>
    </table-index>
</template>

<script>

import distribution from './distribution'

const result = {'kz':'控制','kf':'开放'}
export default {
  components:{distribution},
  data(){
    return{
      fields: {
        batchTaskCode: {label: '鉴定任务编号', search: true,align: 'center'},
        createDate: {label: '领取时间', search: false,dateFormat:'YYYY-MM-DD hh:mm:ss', edit:false},
        auxiliaryResult: {label: '机器鉴定结果', search: true,fieldType:'select',
          mapper:[]
        , edit:false},
        currentPersonName: {label: '当前操作人', search: false, edit:false, width:200},
        receiveTaskName: {label: '任务领取人', search: false, edit:false, width:200},
        batchTaskQuantity: {label: '任务数量', search: false, edit:false, width:200},
        status: {label: '任务状态', search: true,edit:false,fieldType:'select',component: 'tag',
          width:200,
          mapper:[{
            id: '2',
            label: '已完成'
          }, {
            id: '1',
            label: '审批中'
          }, {
            id: '0',
            label: '进行中'
          }, {
            id: '-1',
            label: '未开始'
          }]}
      },
      defaultSearchForm:{batchId:this.$route.query.batchId,createPerson:this.$route.query.createPerson},
      defaultInsertForm:{},
      mapper:[]
    }
  },
  methods:{
    $init(){
      this.$http.post("/appraisalOpenRange/page",{page:false})
          .then(({data})=>{
            data.data.forEach(v=>{
              this.fields.auxiliaryResult.mapper.push({id:v.id,label:result[v.auxiliaryResult]+'--'+v.openRange})
            })
            this.fields.auxiliaryResult.mapper.push({id:'null',label:'无机器鉴定结果'})
          })
    },
    detail(scope){
      sessionStorage.setItem('taskId',scope.row.id)
      this.$router.push({path:"/appraisal/appraisalTask/task/archive",
        query:{
          taskId:scope.row.id,
          createPerson:this.$route.query.createPerson
      }})
    },
    applyTask(){
      this.$refs.distribution.fields.personId.show = false
      this.$refs.distribution.show = true
    },
    reload(){
      this.$refs.table.loadData(this.defaultSearchForm)
    },
    distributionTask(){
      this.$refs.distribution.show = true
    }
  }
}
</script>
