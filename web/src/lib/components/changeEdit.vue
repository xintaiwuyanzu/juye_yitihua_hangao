<template>
  <section>
    <el-link type="primary"
             @click="$router.push({path: '/archiveChange', query: {formDataId: row.id,formDefinitionId:formId,errorType:'1',status:'2',errorSource:'管理库',fondId:fond.id,categoryId:category.id}})">
      调整
    </el-link>
  </section>
</template>
<script>
  import abstractColumnComponent from "./abstractColumnComponent";

  /**
   * 列表按钮
   * 档案馆调整按钮，需要走调整审核流程
   */
  export default {
    extends: abstractColumnComponent,
    name: "changeEdit",
  }
</script>
