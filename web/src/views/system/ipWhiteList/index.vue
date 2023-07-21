<template>
  <!--  <section>-->
  <!--    <el-card style="height: 50vh">
        <div slot="header">
          <strong>白名单</strong>
        </div>-->
  <!--      <div  style="height: 40vh;overflow:auto;">-->
  <table-index path="IpWhiteList" :fields="columns" :edit="true" :delete="true" :insert="true"
               :defaultSearchForm="defaultSearchForm"
               title="IP登录限定" ref="table">
    <template v-slot:table-$btns="scope">
      <el-button  width="40" type="text" @click="yichu(scope.row)" style="float: left">移除</el-button>
    </template>
  </table-index>
  <!--      </div>-->
  <!--    </el-card>-->

  <!--
      <el-card  style="height: 50vh">
        <div slot="header">
          <strong>黑名单</strong>
        </div>
        <div  style="height: 40vh;overflow:auto;">
        <table-index path="IpWhiteList" :fields="columns1" :insert="false" :edit="false" :delete="true"
                     :defaultSearchForm="defaultSearchForm1"  ref="table1">
          <template v-slot:table-$btns="scope">
            <el-button  width="40" type="text" @click="yichu1(scope.row)" style="float: left">移除</el-button>
          </template>
        </table-index>
        </div>
      </el-card>
  -->


  <!--  </section>-->
</template>

<script>
export default {
  name: "index",
  data(){
    return{

      columns: {
        ip: {
          label: 'IP地址', search: true,  edit: true,align: 'center',
        },
        mac: {
          label: 'mac地址',  search: false, edit: true,align: 'center',
        },
        createPerson:{
          label:'创建人',  search: false, edit: false,align: 'center',width:100
        },
        createDate:{
          label:'创建时间',dateFormat: 'YYYY-MM-DD HH:mm:ss',  insert:false,edit: false,align: 'center',
        },
        unit: {
          label: '单位名称', search: false,  edit: true,align: 'center',
        },
        dept: {
          label: '部门名称', search: false,  edit: true,align: 'center',
        },
        username: {
          label: '使用人姓名', search: true,  edit: true,align: 'center',
        },

      },

      /*columns1: {
        ip: {
          label: 'IP地址', search: false,  edit: true,align: 'center',delete:true
        },
        mac: {
          label: 'mac地址',  search: false, edit: true,align: 'center',delete:true
        },
        createPerson:{
          label:'创建人',  search: false, edit: false,align: 'center',delete:true,width:100
        },
        createDate:{
          label:'创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', insert:false,edit: false,align: 'center',delete:true
        },
      },*/

      defaultSearchForm:{
        status:'0'
      },
      /*defaultSearchForm1:{
        status:'1'
      },*/

    }
  },

  methods:{

    async lahei(row) {
      row.status='0'
      this.$confirm('确定拉到黑名单嘛?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/IpWhiteList/update", row).then(({data}) => {
          if (data && data.success) {
            this.$message.success('拉黑成功！')
            this.$refs.table1.loadData(this.defaultSearchForm1);
            this.$refs.table.loadData(this.defaultSearchForm);
          } else {
            this.$message.error('拉黑失败! ')
          }
          this.loading = false
        })
      })
    },

    async yichu(row) {
      row.status='1'
      this.$confirm('确定移除嘛?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/IpWhiteList/update", row).then(({data}) => {
          if (data && data.success) {
            this.$message.success('移除成功！')
            /*this.$refs.table1.loadData(this.defaultSearchForm1);*/
            this.$refs.table.loadData(this.defaultSearchForm);
          } else {
            this.$message.error('移除失败! ')
          }
        })
      })
    },
  }


}
</script>

<style scoped>

</style>