<template>
  <section class="businessBatchBody">
    <nac-info title="指导业务管理"/>
    <div class="index_main">
      <el-radio-group v-model="activeName" @change="activeChange">
        <el-radio-button label="0">待指导业务</el-radio-button>
        <el-radio-button label="1">指导中业务</el-radio-button>
        <el-radio-button label="2">已指导业务</el-radio-button>
      </el-radio-group>
      <table-index ref="table" :showTitle="false" name="first" :fields="fields" path="/businessGuidanceBatch"
                   :insert="false"
                   class="tableIndex"
                   :edit="false" :delete="false" :default-search-form="defaultSearchForm">
        <template v-slot:table-$btns="scope" v-if="activeName != '2'">
            <el-button v-show="activeName === '0'|| activeName === '1'" type="text" @click="categorizing(scope.row)" size="mini"  width="30" >
                归类
            </el-button>
        </template>
        <template v-slot:search-$btns="scope">
          <el-button @click="addClassifyShow = true" type="primary" width="60">添加分类</el-button>
        </template>
      </table-index>

      <!--归类-->
      <el-dialog :visible.sync="classifiShow" width="600px" title="指导归类">
        <el-form :model="searchForm" ref="searchForm" label-width="60px" class="searchForm">
          <el-form-item label="类别" prop="cType">
            <el-select v-model="searchForm.cType" @change="loadFields" style="width:400px" placeholder="请选择归类类别">
              <el-option
                  v-for="item in typeOptions"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="问题" prop="cProblem">
            <el-select v-model="searchForm.cProblem" @change="loadProblem()" style="width:400px" placeholder="请选择问题">
              <el-option
                  v-for="item in problemOptions"
                  :key="item.id"
                  :label="item.cProblem"
                  :value="item.cProblem">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="回答" prop="cResult">
            <el-input type="textarea" v-model="searchForm.cResult" style="width:400px" :rows="4" disabled  placeholder="回答" />
          </el-form-item>
          <el-form-item style="margin-bottom: -10px">
            <el-button style="float:right" type="primary" @click="settlement()">办结</el-button>
            <el-button style="float:right;margin-right: 25px" @click="cancelClassify('1')">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>


      <!-- 添加分类-->
      <el-dialog :visible.sync="addClassifyShow" width="800px" title="问题归类">
        <el-form :model="addForm" ref="addForm"  label-width="60px" >
          <el-form-item label="类别" prop="cType">
            <el-autocomplete v-model="addForm.cType" value-key="cType" placeholder="请输入归类类别"
                             :fetch-suggestions="querySearch" />
          </el-form-item>
          <el-form-item label="问题" prop="cProblem">
            <el-input type="textarea" v-model="addForm.cProblem"  placeholder="请输入问题" :rows="2" />
          </el-form-item>
          <el-form-item label="回答" prop="cResult">
            <el-input type="textarea" v-model="addForm.cResult"  placeholder="请输入回答" :rows="4" />
          </el-form-item>
          <el-form-item style="margin-bottom: -10px">
            <el-button style="float:right" type="primary" @click="addClassify()" >保存</el-button>
            <el-button style="float:right;margin-right: 25px" @click="cancelClassify('2')" >取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
    </div>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {
          prop: 'batchName',
          label: '业务名称',
          width: 330,
          component: 'text',
          route: true,
          search: true,
          routerPath: '/businessGuidanceBatch/detail',
          queryProp: ['id','gid','status']
        },
        {prop: 'classifyType', label: '指导内容'},
        {
          prop: 'status', label: '状态', width: "80", component: 'tag', showTypeKey: 'show', mapper: {
            '0': {label: '待指导', show: 'warning'},
            '1': {label: '指导中', show: 'warning'},
            '2': {label: '已指导', show: 'success'}
          }, fieldType: 'select', edit: false
        },
        {prop: 'createUserName', label: '发起人'},
        {prop: 'createOrgName', label: '发起单位'},
        {prop: 'receiveUserName', label: '接收人'},
        {prop: 'receiveOrgName', label: '接收单位'},
      ],
      activeName: '0',
      classifiShow: false,
      addClassifyShow: false,
      personId:'',
      //归类批次Id
      classifiBatchId:'',
      addForm:{
        cType:'',
        cProblem:'',
        cResult:'',
      },
      searchForm:{
        //归类类型 类型是唯一的
        cType:'',
        cProblem:'',
        cResult:'',
      },
      groupId:'',
      classifyId:'',
      typeOptions:[],
      problemOptions:[],
      restaurants:[],

    }
  },
  methods: {
    $init(){
      this.getPerson()
      this.$http.post('/businessGuidanceCategoryDictionary/selectType').then(({data})=>{
        if (data.success){
          this.restaurants = data.data
        }
      })
    },
    getPerson() {
      this.$http.post('/login/info')
        .then(({data}) => {
          if (data.success) {
            this.personId = data.data.id
          } else {
            this.$message.error(data.message)
          }
        })
    },
    async defaultSearchForm() {
      return {
        status: this.activeName,
      }
    },
    querySearch(queryString, cb) {
      var restaurants = this.restaurants;
      var results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    //归类
    categorizing(row){
      this.$http.post('/businessGuidanceCategoryDictionary/queryType').then(({data})=>{
        if (data.success){
          this.typeOptions = data.data
        }
      })
      //这里应该查询归类字典
      this.groupId = row.gid
      this.batchId = row.id

      this.classifiShow = true
    },
    loadFields(){
      this.searchForm.cProblem = ''
      this.searchForm.cResult = ''
      this.$http.post('/businessGuidanceCategoryDictionary/queryProblem',{
        type:this.searchForm.cType
      }).then(({data})=>{
          if(data.success){
            this.problemOptions = data.data
          }
      })
    },
    cancelClassify(val){
      if(val === '1'){
        this.classifiShow = false
        this.batchId = ''
        this.classifyId = ''
        this.searchForm.cType = ''
        this.searchForm.cProblem = ''
        this.searchForm.cResult = ''

      }else if(val === '2'){
        this.addClassifyShow = false
        this.addForm = []
      }

    },
    loadProblem(){
      this.problemOptions.forEach(value => {
        if(value.cProblem === this.searchForm.cProblem){
            this.searchForm.cResult = value.cResult
          this.classifyId = value.id
        }
      })
    },
    //添加分类
    addClassify(){
      this.$http.post('/businessGuidanceCategoryDictionary/insertDictionary', {
        cType:this.addForm.cType,
        cProblem:this.addForm.cProblem,
        cResult:this.addForm.cResult
      }).then(({data})=>{
          if(data.success){
              this.$message.success("完成")
              this.addClassifyShow = false
              this.addForm = []
          }else {
            this.$message.error(data.message)
          }
      })
    },
     settlement(){
       this.$confirm('确认结办?', '提示', {
         confirmButtonText: '确定',
         cancelButtonText: '取消',
         type: 'warning'
       }).then(() => {
         this.$message({
           message: '正在同步聊天记录',
           type: 'success'
         });
         this.addClassification()
       }).catch(() => {
         this.$message({
           type: 'info',
           message: '已取消'
         });
       });
    },
    //归类
    addClassification(){
      this.$http.post('/businessGuidanceCategoryDictionary/settlement',{
        batchId:this.batchId,
        classifyId:this.classifyId,
        classifyType:this.searchForm.cType
      }).then(({data})=>{
        if(data.success){
            this.roamingData()
        }
      })
    },
    //删除群
    delDialogue(){
      this.$http.post('/online/DeWordProGroup',{groupId:this.groupId}).then(({data})=>{
        if(data.success){
          console.log("完成");
          this.classifiShow = false
          this.cancelClassify('1')
          this.$refs.table.loadData()
        }else {
          this.$message.error(data.message)
        }
      })
    },
    //漫游保存数据
    roamingData(){
      this.$http.post('/online/getWorkProGroupMsg',{groupId:this.groupId,batchId:this.batchId,personId:this.personId}).then(({data})=>{
        if(data.success){
          //this.delDialogue()
          this.cancelClassify('1')
          this.classifiShow = false
          this.$refs.table.loadData()
        }else{
          this.$message.error(data.message)
        }
      })
    },
    activeChange() {
      this.$refs.table.reload()
    }
  },
}
</script>

<style lang="scss" scoped>
.businessBatchBody {
  .el-card__body {
    height: 100%;
    padding: 0;
  }

  .table_index {
    height: 100%;
    padding: 0;
  }
}

.tableIndex {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: auto;
  padding: 0;
  margin-top: -34px;

  .table-wrapper {
    padding: 0px;
  }
}
</style>