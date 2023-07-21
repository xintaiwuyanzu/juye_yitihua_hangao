<template>
  <section title="档案管理">
    <nac-info title="查看报表统计" back>
      <search-component @search="search" style="display: inline;"/>
    </nac-info>
    <div>
      <!--在组件加key，当查询条件变化时，不需要在子组件监听改变就可以重新查询-->
      <div style="display: flex">
        <appraisal-num class="form" :params="params" :key="new Date().getTime()+1"/>
        <offline-archive-num class="form" :params="params" :key="new Date().getTime()+2"/>
      </div>
      <div style="display: flex">
        <online-archive-num class="form" :params="params" :key="new Date().getTime()+3"/>
        <transfer-num class="form" :params="params" :key="new Date().getTime()+4"/>
      </div>
    </div>
  </section>
</template>

<script>
  import SearchComponent from "../resourceStatistics/searchComponent";
  import AppraisalNum from "./components/appraisalNum";
  import OfflineArchiveNum from "./components/offlineArchiveNum";
  import OnlineArchiveNum from "./components/onlineArchiveNum";
  import TransferNum from "./components/transferNum";
  export default {
    name: "index",
    components: {TransferNum, OfflineArchiveNum, OnlineArchiveNum, AppraisalNum, SearchComponent},
    data(){
      return{
        params:{
          fondCode:'',
          //年度
          vintages:'',
          //类型
          appraisalType:''
        }
      }
    },
    methods:{
      search(val){
        this.params = {
          fondCode:val.fondCode,
          vintages:val.vintages,
          appraisalType:''
        }
      }
    }
  }
</script>

<style scoped>
  .form{
    width: 50%;
    text-align: center;
    padding: 10px;
  }
</style>