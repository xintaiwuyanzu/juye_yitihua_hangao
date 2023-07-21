<template>
  <section>
    <theme-select/>
    <Person/>
    <full-screen/>
    <el-tooltip content="返回" v-if="path !=='/home'">
      <section @click="$router.back()" class="top_icon">
        <icon icon="arrow-left" style="width: 20px"/>
      </section>
    </el-tooltip>
    <!--退出-->
    <Logout/>
  </section>
</template>
<script>
import Logout from './logout'
import Person from './person'
import Home from './home'
import themeSelect from '@dr/framework/src/components/headerRight/themeSelect'
import fullScreen from '@dr/framework/src/components/headerRight/fullScreen'
import sysSelect from '@dr/framework/src/components/headerRight/sysSelect'

export default {
  components: {Logout, Person, Home, themeSelect, fullScreen, sysSelect},
  data() {
    return {
      path:'',
    }
  },
  watch:{
    '$route':function (to,from){
      console.log(from)
      this.path=to.path
    }
  },
}
</script>
<style>
.header-right > * {
  display: inline-block;
}
</style>