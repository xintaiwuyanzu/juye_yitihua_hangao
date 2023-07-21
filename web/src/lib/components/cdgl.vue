<template>
    <section>
      <el-link type="primary" @click="repeat"> 查档 </el-link>

     <!-- <el-button type="primary" @click="repeat">查档关联</el-button>-->
      <el-dialog title="查档人员" :visible.sync="dialogShow" width="50%" append-to-body>
        <el-table :data="data" border height="450px">
          <el-table-column label="排序" type="index" align="center" width="50"/>
          <el-table-column prop="username" label="姓名" header-align="center" show-overflow-tooltip/>
          <el-table-column prop="reason" label="查档目的" width="200" header-align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.reason|dict('utilize.cdmd') }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="130" align="center" header-align="center">
            <template slot-scope="scope">
              <el-button size="text" type="primary" @click="gl(scope.row)">关联</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-dialog>
    </section>


</template>
<script>
import abstractColumnComponent from "./abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  data() {
    return {
      data:[],
      dict: ['utilize.cdmd'],
      archvieCode:''
    }
  },
  methods: {

      gl(row){
          const param = {
              qzmc:this.fond.name,
              qzh:this.fond.code,
              lbh: this.category.code,
              dh: this.row.FOND_CODE,
              nd: this.row.VINTAGES,
              bgqx: this.row.SAVE_TERM,
              tm:this.row.TITLE,
              registrationId:row.id,
              archiveId: this.row.id
          }
          this.$http.post('/registration/addUtilizeArchive',param).then(
              ({data}) => {
                  if (data.success) {
                      this.$message.success("关联成功")
                      if(row.status !=='1'){
                          this.update(row)
                      }
                  }
                  this.dialogShow = false
              })
      },

      update(row){
          row.status = '1'
          this.$http.post('registration/update', row)
      },

    //显示弹出框
    repeat() {
      this.dialogShow = true
        this.$http.post('/registration/page',{registration:'registration'}).then(
            ({data}) => {
                if (data.success) {
                    this.data = data.data.data

                }
            })
      /*setTimeout(()=>{
          this.$http.post('/registration/page',{registration:'registration'}).then(
              ({data}) => {
                  if (data.success) {
                      this.data = data.data
                  }
              })
      },200)*/



    }
  }
}
</script>
