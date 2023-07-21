<template>
  <section >
    <table-render
        style="height: 40vh"
        :columns="columns"
        :data="data">
    </table-render>
  </section>
</template>

<script>
export default {
  data(){
    return{
      columns:{fondCode:{label:'全宗编码'}},
      data:[]
    }
  },
  props:{
    batchId:''
  },
  methods:{
    $init(){
      this.$http.post("/appraisalBatch/statisticsByBatchId",{
        batchId: this.batchId
      }).then(({data})=>{
        if (data.success){
          let filed = data.data.filedList
          filed.forEach(v=>{
            this.columns[v] = {label:v}
          })
          this.data = data.data.countList
        }
      })
    },
    createBatch(){
      this.$refs.batchForm.show = true
    },
    saveBatch(form){
      this.$http.post("/appraisalBatch/insert",Object.assign(form,{
        fondCodes: this.$route.query.fondCodes,
        appraisalType: this.$route.query.appraisalType,
        startVintages: this.$route.query.startVintages,
        endVintages: this.$route.query.endVintages
      })).then((data)=>{
        if(data.success){
          this.$refs.batchForm.show = false
          this.$message.success("创建成功")
          this.$router.back()
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
