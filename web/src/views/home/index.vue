<template>
  <section class="index_home">
    <skip-button></skip-button>
    <div class="index_container">
      <search class="index_search"/>
      <archive-car class="index_car"/>
    </div>
    <div class="index_container">
      <statics class="index_statics"/>
      <annual-report class="index_car"/>
    </div>
  </section>
</template>
<script>
import {decode} from 'js-base64'
import search from './search'
import statics from './static'
import skipButton from "./skipButton";
import archiveCar from "./archiveCar";
import annualReport from "./annualReport";

export default {
  components: {
    search,
    archiveCar,
    statics,
    skipButton,
    annualReport,
  },
  mounted() {
    let params = localStorage.getItem("params")
    if (!params) {
      params = this.$route.query.params
    }
    //const params = localStorage.getItem("params")
    if (params) {
      localStorage.removeItem('params')
      try {
        const routeParams = JSON.parse(decode(params))
        const {$path, ...query} = routeParams
        this.$nextTick(() => {
          setTimeout(()=>{
            this.$router.push({path: $path, query})
          },1000)
        })
      } catch (e) {
        this.$message.warning('解析单点登录参数失败，自动路由失败')
      }
    }
  }
}
</script>
<style lang="scss">
.el-button--warning:hover,  .el-button--warning:focus{
  background-color: rgba(250,174,24,0.8) !important;
  border-color: rgba(250,174,24,0.8) !important;
  color: white;
}
.index_home {
  display: flex;
  //padding: 10px;
  //overflow: auto;

  .index_container {
    display: flex;
    flex: 1;
    flex-direction: row;
    //overflow: auto;

    .h_color {
      color: $--color-primary;
    }
    .index_search {
      margin-bottom: 18px;
      flex: 1;
    }
    .index_car {
      width: 33%;
      margin-left: 18px;
      margin-bottom: 18px;
    }

    .index_statics {
      flex: 1;
      margin-bottom: 18px;
      padding-bottom: -30px !important;
    }

    .el-card__body {
      flex: 1;
      flex-direction: column;
      display: flex;
      overflow: auto;
    }
  }
}
</style>