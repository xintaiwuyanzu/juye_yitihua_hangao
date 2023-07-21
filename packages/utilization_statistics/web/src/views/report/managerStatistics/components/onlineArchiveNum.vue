<template>
  <div>
    <el-card>
      <h3>档案在线归档数量</h3>
      <ve-histogram :data="chartData"/>
    </el-card>
  </div>
</template>

<script>
  export default {
    name: "onlineArchiveNum",
    props:{
      params:Object
    },
    data() {
      return {
        chartData: {
          rows: [],
          columns: ['年度', '目录数量']
        }
      }
    },
    methods: {
      $init() {
        this.getArchiveNum();
      },
      async getArchiveNum() {
        let entity = {fondCode:this.params.fondCode,time:-1};
        if(this.params.vintages!==''){
          entity.time = new Date(new Date(this.params.vintages+'-01-01 00:00:00')).getTime();
        }
        const {data} = await this.$post('/onlineStatics/getArchiveOnline', entity);
        if (data.data) {
          this.chartData.rows = [];
          for (let key in data.data) {
            this.chartData.rows.push({
              '年度': key,
              '目录数量': data.data[key]
            })
          }
        }
      }
    }
  }
</script>

<style scoped>

</style>