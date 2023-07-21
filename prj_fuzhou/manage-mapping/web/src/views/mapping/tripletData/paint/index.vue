<template>
  <section>
    <nac-info back title="关系图谱"/>
    <paint :p_classType="$route.query.classType" :p_i="i" :p_relationName="relationName" :p_search="search"></paint>
  </section>
</template>

<script>
  import Paint from "../../component/paint";

  export default {
    name: "index",
    components: {Paint},
    data() {
      return {
        //查询
        search: this.$route.query.search,
        relationName: this.$route.query.relationName,
        i: Number(this.$route.query.i)
      }
    }
  }
</script>