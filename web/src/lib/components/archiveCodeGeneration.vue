<template>
  <section>
    <el-button type="primary" @click="generation">档号生成</el-button>
  </section>
</template>
<script>
import abstractColumnComponent from "./abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  data() {
    return {
      data: {},
      archvieCode: ''
    }
  },
  methods: {
    //显示弹出框
    generation() {
    },
  },
}
</script>
