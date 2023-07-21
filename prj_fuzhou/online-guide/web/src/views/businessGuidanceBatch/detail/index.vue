<template>
  <table-index title="详情列表" :showTitle="false" :fields="fields" path="businessGuidanceBatchDetail"
               :default-search-form="defaultSearchForm" :edit="false" :insert="false" :delete="false"
               :checkAble="true" ref="table" back>
    <template v-slot:table-$btns="scope">
      <el-button  @click="goDetail(scope)"  type="text" width="20">查看</el-button>
    </template>
    <template v-slot:search-$btns="scope">
      <el-dropdown trigger="click"
                   @command="fastReply">
        <el-button type="primary">
          一键回复<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-if=" tableSelection.length>0" command="select">回复选中</el-dropdown-item>
          <el-dropdown-item command="search">回复查询</el-dropdown-item>
          <el-dropdown-item command="all">回复全部</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>
    <fast-reply :key="key" @confirmReply="confirmReply" ref="fastReply" ></fast-reply>
  </table-index>
</template>

<script>
import {useUser} from "@dr/framework/src/hooks/userUser";
import wordProDialog from "../../wordPro/wordProDialog";
import fastReply from "./fastReply";

export default {
  name: "index",
  components: {wordProDialog,fastReply},
  setup() {
    return useUser()
  },
  data() {
    return {
      fastReplyCommand:'',
      fields: [
        {
          prop: 'title',
          label: '题名',
          search: true,
          align: 'left',
          // component: 'text',
          // route: true,
          // routerPath: '/businessGuidanceBatch/metadataAndFileDetail',
          // queryProp: ['id', 'formDataId', 'formDefinitionId', {
          //   refType: 'archive',
          //   groupCode: 'default',
          //   watermark: false,
          //   headerExcludes: ['errorCorrection'],
          //   detailExcludes: 'errorCorrection',
          //   batchId:this.$route.query.id}]
        },
        {
          prop: 'status', label: '状态', width: "80", component: 'tag', showTypeKey: 'show', mapper: {
            '0': {label: '待指导', show: 'warning'},
            '1': {label: '指导中', show: 'warning'},
            '2': {label: '已指导', show: 'success'}
          }, fieldType: 'select', edit: false
        },
        {prop: 'archiveCode', label: '档号', width: 200},
        {prop: 'fondCode', label: '全宗号', width: 100},
        {prop: 'categoryCode', label: '分类号', width: 140},
        {prop: 'year', label: '年度', width: 80},
      ],
      defaultSearchForm: {batchId: this.$route.query.id},
      key:0
    }
  },
  methods: {
    goDetail(scope){
      this.$router.push({path: '/businessGuidanceBatch/metadataAndFileDetail', query: {
          id:scope.row.id,
          formDefinitionId:scope.row.formDefinitionId,
          formDataId:scope.row.formDataId,
          gid:this.$route.query.gid,
          showStatus:this.$route.query.status,
          refType: 'archive',
          groupCode: 'default',
          watermark: false,
          headerExcludes: ['errorCorrection'],
          detailExcludes: 'errorCorrection',
          batchId:this.$route.query.id,
          index:scope.$index+(parseInt(this.$refs.table.data.page.index)-1)*parseInt(this.$refs.table.data.page.size)
        }})
    },
    fastReply(command){
      console.log(command)
      let retun=true
      this.$refs.table.tableSelection.forEach(tiem=>{
        if (tiem.status!='0'){
          this.$message.error('请选择待指导业务')
          retun=false
        }
      })
      if (retun){
        // console.log(this.tableSelection)
        this.fastReplyCommand = command
        //this.$refs.fastReply.show = true
        this.$refs.fastReply.setShow()
      }

    },
    confirmReply(result){
      console.log(result)
      if(this.fastReplyCommand=='all'){
        this.$http.post("/businessGuidanceBatchDetail/fastReply",{batchId:this.$route.query.id,result:result})
            .then(({data})=>{
              if(data.success){
                this.$message.success("一键指导"+data.data+"份档案。")
                this.key++
                this.$refs.table.loadData()
              }else{
                this.$message.error(data.message)
              }
            })
      }
      if(this.fastReplyCommand=='select'){
        const ids = []
        this.$refs.table.tableSelection.forEach(v=>{
          ids.push(v.id)
        })
        this.$http.post("/businessGuidanceBatchDetail/fastReplyByIds",{batchId:this.$route.query.id,result:result,ids:ids.join(',')})
            .then(({data})=>{
              if(data.success){
                this.$message.success("一键回复"+data.data+"份档案。")
                this.key++
                this.$refs.table.loadData()
              }else{
                this.$message.error(data.message)
              }
            })
      }
      if(this.fastReplyCommand=='search'){
        this.$http.post("/businessGuidanceBatchDetail/fastReplyBySearch",Object.assign({batchID:this.$route.query.id,result:result},this.$refs.table.searchFormModel) )
            .then(({data})=>{
              if(data.success){
                this.$message.success("一键回复"+data.data+"份档案。")
                this.key++
                this.$refs.table.loadData()
              }else{
                this.$message.error(data.message)
              }
            })
      }
    }

  },
  computed:{
    tableSelection(){
      return this.$refs.table.tableSelection
    },
  },
}
</script>
<style lang="scss" scoped>
</style>
