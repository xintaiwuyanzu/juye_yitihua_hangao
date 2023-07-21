<template>
<section>
  <nac-info back title="审批详情">
    <el-button type="success" size="mini"  @click="queryArch()" v-show="dataList.status!=='3'">查档</el-button>
  </nac-info>
  <el-row>
    <el-col :span="24">
      <el-card>
        <div slot="header">
          <strong>申请人信息</strong>
        </div>
        <div>
          <el-descriptions class="margin-top" :column="3" border>
            <el-descriptions-item>
              <template slot="label">
                申请人
              </template>
              <span>{{ dataList.applicant}}</span>
            </el-descriptions-item>
<!--            <el-descriptions-item>
              <template slot="label">
                申请人单位
              </template>
              <el-tag>{{dataList.duty}}</el-tag>
            </el-descriptions-item>-->
            <el-descriptions-item>
              <template slot="label">
                申请人部门
              </template>
              <el-tag>{{ dataList.organizeName }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                联系方式
              </template>
              <el-tag>{{ dataList.mobile }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item >
              <template slot="label">
                借阅目的
              </template>
              <span class="purpose">{{ dataList.purpose }}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                借阅档案信息
              </template>
              <span class="purpose">{{dataList.archiveContent }}</span>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                关键字
              </template>
              <el-tag>{{dataList.keywordString }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                是否下载
              </template>
              <el-tag>{{ dataList.downloadState|dict('archiveDownload') }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item>
              <template slot="label">
                是否打印
              </template>
              <el-tag><span>{{ dataList.printState|dict('archivePrint') }}</span></el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </el-card>
    </el-col>
  </el-row>
 <el-row>
   <el-col :span="24">
     <el-card>
       <div slot="header">
         <strong>查询档案信息</strong>
       </div>
       <div style="height: 500px;padding: 0;overflow:auto;">
         <table-index :showTitle="false" path="RegistrationDetails" :fields="fields"
                      :insert="false" ref="table" :edit="false" :search="false" :delete="false"
                      :defaultSearchForm="defaultSearchForm" class="archiveTable">
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
             <el-button type="text" size="mini"  @click="delApply(row)" width="60" v-show="dataList.status!=='3'">删除</el-button>
           </template>
         </table-index>
         <el-row style="margin-top: 30px">
           <el-col :span="24" style="text-align: center">
             <el-button type="success" width="60" @click="banjie()" v-show="dataList.status!=='3'&&dataList.status!=='4'&&registrationDetailsList.length>0">办结</el-button>
             <el-button type="success" width="60" v-show="dataList.status=='3'" disabled>已办结</el-button>
             <el-button type="warning" width="60" @click="jujue()" v-show="dataList.status!=='3'&&dataList.status!=='4'&&registrationDetailsList.length>0">不通过</el-button>
             <el-button type="warning" width="60" v-show="dataList.status=='4'" disabled>已拒绝</el-button>
           </el-col>
         </el-row>
       </div>
     </el-card>
   </el-col>
 </el-row>
  <el-dialog title="确认办结" :visible.sync="show" width="30%">
    <el-form ref="form" :model="form" label-width="100" >
      <el-form-item prop='strategyName' label='借阅策略' label-width="100px">
        <el-select clearable style="width: 60%" v-model="form.strategyName">
          <el-option v-for="item in strategyNameList" :key="item.id" :label="item.strategyName"
                     :value="item.id"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer" style="text-align: right">
      <el-button type="primary" @click="saveForm">保 存</el-button>
    </div>
  </el-dialog>
</section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      dict: ['archivePrint','archiveDownload'],
      fields:[
        {prop: 'archive_CODE', label: '档号',edit: true,search:false },
        {prop: 'fond_CODE', label: '全宗',edit: true,},
        {prop: 'cate_GORY_CODE', label: '门类',},
        {prop: 'vintages', label: '年度'},
      ],
      dataList: {},
      defaultSearchForm:{borrowingId:this.$route.query.id},
      show:false,
      form:{},
      strategyNameList:[],//获取策略列表
      registrationDetailsList:[],//获取添加的档案列表
      row:{},
    }
  },
  methods:{
    $init(){
      this.getlist()
      this.getRegistrationDetailsList()
    },

    //获取添加的档案列表
    getRegistrationDetailsList(){
      this.$http.post('/RegistrationDetails/page',{borrowingId:this.$route.query.id,page:true}).then(({data})=>{
        if(data.success){
          this.registrationDetailsList=data.data.data
        }
      })
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
   async queryArch(){
      if(this.dataList.status=='1'){
        this.dataList.status='2'
        this.dataList.updateDate=Date.now()
        this.$http.post('Registration/update',this.dataList).then(({data})=>{
          if(data.success){
            this.$message.success('正在查档中')
          }else {
            this.$message.error(data.message)
          }
        })
      }
      this.$router.push({path:'/ElectronicBorrowing/queryArchive',query:{id:this.$route.query.id,keywordString:this.$route.query.keywordString}})
    },

    //获取策略列表
    getStrategyName(){
      this.$http.post('Borrowing/page',{page:false,enableOrNot:"1"}).then(({data})=>{
        if(data.success){
          this.strategyNameList=data.data
        }
      })
    },

    getApplyPerson(){
      this.$http.post('/Registration/processPage',{id:this.$route.query.id,page:false}).then(({data})=>{
        if(data.success){
          this.row=data.data.data[0]
        }
      })
    },
    //审批办结
    banjie(){
      this.getApplyPerson()
      this.show=true
      this.getStrategyName()
    },
    //同意申请，办结
    saveForm(){
      this.row.status='3'
      this.row.updateDate=Date.now()
      this.row.strategyId=this.form.strategyName,
          this.$http.post('Registration/update',this.row).then(({data})=>{
            if(data.success){
              this.$message.success('办结成功')
              this.getlist()
              this.show=false
            }else {
              this.$message.error(data.message)
            }
          })
    },
    //  拒绝申请
  async  jujue(){
    await this.getApplyPerson()
    this.$confirm('确定拒绝该申请吗?', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(() => {
      this.row.status='4'
      this.row.updateDate=Date.now()
      this.$http.post('Registration/update',this.row).then(({data})=>{
        if(data.success){
          this.$message.success('已拒绝申请')
          this.getlist()
        }else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    })
    },

    //删除档案
    delApply(row){
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("RegistrationDetails/delete", {id:row.id}).then(({data}) => {
          if (data && data.success) {
            this.$message.success('删除成功！')
            this.getRegistrationDetailsList()
            this.$refs.table.loadData()
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      })
    },
  },
}
</script>
<style scoped>
.purpose{
  width: 180px;
  overflow:hidden;
  text-overflow:ellipsis;
  white-space:nowrap;
  display: block
}
/deep/.archiveTable .el-table__body-wrapper {
  overflow: auto;
  height: 61% !important;
  position: relative;
}
/*/deep/.archiveTable.table_index{
  height: 420px;
}*/
/deep/ .table_index .table_index_container .table-wrapper{
  height: 300px;
}
</style>