<template>
<!--  <section>-->
  <table-index title="借阅审批" path="Registration"
               pagePath="Registration/processPage"
               :fields="fields" :insert="false"
               :delete='false' :edit="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm" :page="true">
    <template v-slot:table-$btns="{row}">
      <el-button type="text" width="60" @click="queryDedail(row)">查看详情</el-button>
    </template>
    <el-table-column prop="updateDate" label="审批时间" width="160" align="center" after="createDate">
      <template v-slot="scope">
        <span v-show="scope.row.status!=='1'&&scope.row.status!=='0'">{{scope.row.updateDate|datetime}}</span>
      </template>
    </el-table-column>
  </table-index>

<!--  </section>-->
</template>

<script>
import indexMixin from "@dr/auto/lib/util/indexMixin";
export default {
  name: "index",
  mixins: [indexMixin],
  data() {
    return {
      show:false,
      form:{},
      dict: ['archivePrint','archiveDownload'],
      fields:[
        {prop: 'applicant', label: '申请人',edit: false,search:true },
        {prop: 'organizeName', label: '机构',edit: false },
        {prop: 'purpose', label: '查询目的',type:'textarea'},
        {prop: 'archiveContent', label: '借阅档案信息',type:'textarea'},
        {prop: 'keywordString', label: '关键字',search:true},
        {prop:'createDate',label: '审请时间',edit: false,dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140,},
        {prop:'updateDate',label: '审批时间',edit: false,dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140,},
        {prop: 'downloadState', label: '是否下载',edit:false,dictKey: 'archiveDownload', component: 'tag',width:80},
        {prop: 'printState', label: '是否打印',edit:false,dictKey: 'archivePrint', component: 'tag',width:80},
        {prop: 'status', label: '借阅状态',edit:false,dictKey: 'borrow.status', component: 'tag',},
      ],
      defaultSearchForm:{id:this.$route.query.id},
      strategyNameList:[],
      row:{},

    }
  },
  methods:{
    // 跳转详情页
    queryDedail(row){
      this.$router.push({path:'/ElectronicBorrowing/detail',query: {id:row.id,keywordString:row.keywordString}})
    },

  },
}
</script>