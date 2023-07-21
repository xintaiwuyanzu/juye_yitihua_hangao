<template>
  <el-card v-loading="loading" style="overflow: hidden">
    <span class="h_color">我的档案车<span style="font-size: 30px"/>
      <el-button class="h_color" type="text" style="float: right"
                 @click="$router.push('/archive/archiveCar')">更多 ></el-button>
    </span>
    <el-divider/>
    <!--    <table-render :columns="columns" :data="data" :index="false" :page="page" :show-header="status" :row-class-name="tableRowClassName"/>-->
    <el-table
        :data="data"
        stripe
        :show-header="status"
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @row-click="rowClick"
        :cell-style="cellStyle"
    >
      <el-table-column
          prop="batchName"
          label="档案车名称"
          :show-overflow-tooltip="true"
      >
      </el-table-column>
      <el-table-column
          align="right"
          prop="createDate"
          label="创建时间"
          width="140"
          :formatter='dateFormat'
      >
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script>
export default {
  name: "archiveCar",
  data() {
    return {
      loading: false,
      status: false,
      data: [],
      page: {pageSize: 5}
    }
  },
  methods: {
    //查询档案车
    async $init() {
      this.loading = true
      const {data} = await this.$post('archiveCarBatch/page', {page: true, pageSize: 5})
      if (data && data.success) {
        this.data = data.data.data
      }
      this.loading = false
    },
    tableRowClassName({ rowIndex }) {
      if ((rowIndex+1) % 2 === 0) {
        return 'success-row' //这是类名
      } else {
        return ''
      }
    },
    dateFormat (row,col){
      // 获取单元格数据
      let data = row[col.property];
      if(data){
        const dateMat= new Date(data);
        const Y = dateMat.getFullYear()+ "-";
        const M = (dateMat.getMonth() + 1)<10?'0'+(dateMat.getMonth() + 1)+ "-":(dateMat.getMonth() + 1)+ "-";
        const D = dateMat.getDate()<10?'0'+dateMat.getDate()+ " ":dateMat.getDate()+ " ";
        const H = dateMat.getHours() < 10 ? "0" + dateMat.getHours()+ ":" : dateMat.getHours()+ ":" ;
        const m = dateMat.getMinutes() < 10 ? "0" + dateMat.getMinutes()+ ":"  : dateMat.getMinutes()+ ":" ;
        const s=  dateMat.getSeconds() < 10 ? "0" + dateMat.getSeconds() : dateMat.getSeconds();
        return Y+M+D+H+m+s;
      }
    },
    rowClick(row){
      this.$router.push({path: '/archive/archiveCar/detail',query: {id:row.id, batchType:row.batchType,status:row.status}})
    },
    cellStyle() {
      // console.log(data)
      return "cursor:pointer;"
    },
  }
}
</script>

<style lang="scss">
.index_car {
  .el-card__body {
    height: 100%;
  }
}
.success-row {
  color:#128ae1 !important;
}
.el-divider--horizontal{
  margin-top: 10px;
  margin-bottom: 4px;
}
</style>