<template>
<section>

  <nac-info back title="查档检索"/>
<!--  搜索关键字-->
  <div style="text-align: center">
    <el-input v-model="form.keywordString" @keyup.enter.native="loadData" placeHolder="请输入搜索关键字"
              clearable v-focus style="width: 500px">
      <el-button slot="append" icon="el-icon-search" @click="loadData" type="primary">搜 索</el-button>
    </el-input>
  </div>

  <el-card  style="margin-top: 20px">
    <div slot="header">
      <strong>查档结果</strong>
    </div>
    <el-container style="height: 80vh;overflow:auto;">
      <el-main v-if="this.list.length>0">
        <resultRender  v-for="(item,index) in list" :key=item.id :item="item" :index="page.size*(page.index-1)+index">
          <slot :item="item" name="btns">
            <el-button type="primary" size="mini" @click="addDetail(item)">添加</el-button>
          </slot>
        </resultRender>
      </el-main>
      <el-main v-else>
        <el-empty description="暂无数据，请重新搜索" style="margin-top: 40px"/>
      </el-main>
    </el-container>
  </el-card>

</section>
</template>

<script>
import resultRender from "./resultRender";
export default {
  components:{resultRender},
  computed: {
    // keywordString() {
    //   return this.$route.query.keywordString
    // },
  },
  data(){
    return{
      list:[],
      form:{
        keywordString:this.$route.query.keywordString
      },
      page: {
        index: 1,
        size: 50,
        total: 0
      },
      querysListJson: [{
        multiSearch: false,
        keyWords: this.$route.query.keywordString
      }]
    }
  },
  methods: {
    $init(){
      this.loadData()
    },
    addDetail(item){
      item.formId=item.id
      item.form=item.id
      this.$http.post('RegistrationDetails/insert', {...item,borrowingId:this.$route.query.id}).then(({data})=>{
        if(data.success){
          this.$message.success('添加档案详情页成功')
        }else {
          let indexs= data.message.lastIndexOf(":")
          var resolves = data.message.substring(indexs + 1, data.message.length);
          this.$message.error(resolves)
        }
      })
    },
    loadData(){
      this.querysListJson=[{
        multiSearch: false,
        keyWords: this.form.keywordString
      }]
      this.$http.post('/search/searchBykeyWords', {querysListJson:JSON.stringify(this.querysListJson),size:this.page.size}).then(({data})=>{
        if(data.success){
          this.list=data.data.data[0].data
          //console.log(this.list)
        }
      })
    },
    },
}
</script>
<style lang="scss">
.result_list {
  flex: 1;
  overflow: auto;
  padding: 0px;

.search_result_item {
  margin-bottom: 6px;


.result_title {
  display: flex;
  height: 32px;
  position: relative;

.result_text {
  font-weight: bold;
  justify-content: left;
  font-size: 18px;
  white-space: nowrap;
  overflow-x: hidden;
}

.result_btns {
  background-color: white;
  position: absolute;
  right: -15px;
  min-width: 80px
}
}
}
}
</style>