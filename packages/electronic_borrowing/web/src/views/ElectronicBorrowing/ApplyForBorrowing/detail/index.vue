<template>
<section>
  <table-index title="借阅详情"  path="RegistrationDetails" pagePath="RegistrationDetails/page" :fields="fields" :insert="false" ref="table" back :edit="false" :delete="false" :defaultSearchForm="defaultSearchForm">
    <el-table-column prop="archive_CODE" label="档号" width="160" align="center">
      <template v-slot="scope">
        <span v-html="scope.row.archive_CODE"></span>
      </template>
    </el-table-column>
      <el-table-column prop="title" label="题名" width="160" align="center" after="archive_CODE">
          <template v-slot="scope">
              <span v-html="scope.row.title"></span>
          </template>
      </el-table-column>
    <template v-slot:table-$btns="{row}">
        <el-button type="text" size="mini"  @click="queryArch(row)" width="60">
<!--          <a :href="href" style="text-decoration: none;font-size: 14px;color: #0168b3;font-weight: 500">查档</a>-->
          查档
        </el-button>
    </template>
  </table-index>
</section>
</template>

<script>
import qs from 'qs'
export default {
  name: "index",
  data() {
    return {
      fields:[
        {prop: 'archive_CODE', label: '档号',edit: true,search:true },
        {prop: 'title', label: '题名'},
        {prop: 'fond_CODE', label: '全宗'},
        {prop: 'cate_GORY_CODE', label: '门类',},
        {prop: 'vintages', label: '年度'},
      ],
      defaultSearchForm:{borrowingId:this.$route.query.id},
      dataList: {},
      href:'',
    }
  },

  methods:{
    $init(){
      this.getlist()
    },
    //获取申请人信息
    getlist(){
      if(this.$route.query.id){
        this.$http.post('Registration/processPage',{id:this.$route.query.id}).then(({data})=>{
          if(data.success){
            this.dataList=data.data.data[0]
          }
        })
      }
    },
    //查档
   async queryArch(row){
       const query = {
         formDataId: row.formId,
         formDefinitionId: row.formDefinitionId,
         refType: 'archive',
         groupCode: 'default',
         watermark: this.$route.query.watermarkState==='0'?false:true,
         //需要修改值， 是否的时候 为true
         printStatus: this.$route.query.printState==='0'?true:false,
         downloadState:this.$route.query.downloadState==='0'?true:false
       }

       // this.href='#/archive/metadataAndFileDetail?' + qs.stringify(query)
       // this.href='#/ElectronicBorrowing/metadataAndFile?' + qs.stringify(query)

      this.$router.push({path:'/ElectronicBorrowing/metadataAndFile',query:{...query}})
    },
  },

}
</script>
<style scoped>
::v-deep .el-table--border {
  height: 75vh !important;
}
</style>