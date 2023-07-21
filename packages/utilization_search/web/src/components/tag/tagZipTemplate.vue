<template>
  <section style="background-color: #dde9ff;padding: 10px;margin-bottom: 20px">
    <el-tag style="font-size: 18px;margin-bottom: 6px" type="success">zip包结构说明:</el-tag>
    <el-breadcrumb separator-class="el-icon-arrow-right" v-if="type===1">
      <el-breadcrumb-item>zip包</el-breadcrumb-item>
      <el-breadcrumb-item>一级标签（文件夹）</el-breadcrumb-item>
      <el-breadcrumb-item>二级标签（文件名称.txt）</el-breadcrumb-item>
    </el-breadcrumb>
    <el-breadcrumb separator-class="el-icon-arrow-right" v-else>
      <el-breadcrumb-item>zip包</el-breadcrumb-item>
      <el-breadcrumb-item>一级标签（文件夹）</el-breadcrumb-item>
      <el-breadcrumb-item>二级标签（文件夹）</el-breadcrumb-item>
      <el-breadcrumb-item>三级标签（文件名称.txt）</el-breadcrumb-item>
    </el-breadcrumb>
  </section>
</template>

<script>
  export default {
    name: "tagZipTemplate",
    props: {
      type: Number
    }
  }
</script>