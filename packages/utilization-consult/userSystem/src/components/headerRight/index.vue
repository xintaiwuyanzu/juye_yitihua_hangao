<template>
  <section>
    <full-screen/>
    <theme-select/>
    <el-tooltip content="退出登录" placement="bottom">
      <section class="top_icon" @click="logout">
        <icon icon="delete"/>
      </section>
    </el-tooltip>
  </section>
</template>
<script>
import fullScreen from '@dr/framework/src/components/headerRight/fullScreen'
import themeSelect from '@dr/framework/src/components/headerRight/themeSelect'
import {useUser} from "../../useUser";

export default {
  name: "headerRight",
  components: {fullScreen, themeSelect},
  setup() {
    return useUser();
  },
}
</script>