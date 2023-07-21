<template>
  <section style="display: inline-block;margin-left: 5px">
    <add-button ref="addButtonRef" @change="queryAll($event)"></add-button>
    <el-card shadow="never" class="box-card">
      <el-table :data="tableData"  height="650px"  border
          :header-cell-style="{'text-align':'center'}"
          :cell-style="{'text-align':'center'}"
          v-loading="tableLoading"
          style="width: 100%">
        <el-table-column
            label="序号"
            type="index">
        </el-table-column>
        <el-table-column
            prop="strategyName"
            label="策略名称">
        </el-table-column>
        <el-table-column
            prop="borrowingPeriod"
            label="保管期限/天">
          <template slot-scope="scope">
            {{scope.row.borrowingPeriod+"天"}}
          </template>
        </el-table-column>
        <el-table-column
            prop="printState"
            label="是否打印">
          <template slot-scope="scope">
            {{scope.row.printState==='0'?"否":"是"}}
          </template>
        </el-table-column>
        <el-table-column
            prop="downloadState"
            label="是否下载">
          <template slot-scope="scope">
            {{scope.row.downloadState==='0'?"否":"是"}}
          </template>
        </el-table-column>
        <el-table-column
            prop="watermarkState"
            label="是否带水印">
          <template slot-scope="scope">
            {{scope.row.watermarkState==='0'?"否":"是"}}
          </template>
        </el-table-column>
        <el-table-column
            label="是否启用">
          <template slot-scope="scope">
            {{scope.row.enableOrNot==='0'?"否":"是"}}
          </template>
        </el-table-column>
        <el-table-column
            label="操作">
          <template slot-scope="scope">
            <el-link  v-if="scope.row.enableOrNot==='0'" type="primary" @click="enableOrNot(scope.row,'1')">启用</el-link>
            <el-link  v-if="scope.row.enableOrNot==='1'"type="danger" @click="enableOrNot(scope.row,'0')">禁用</el-link>
            <el-divider direction="vertical" v-if="scope.row.enableOrNot==='0'"></el-divider>
            <el-link v-if="scope.row.enableOrNot==='0'" type="danger" @click="deleteRow(scope.row)">删除</el-link>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page.sync="pageDate.index"
            :page-size="pageDate.size"
            layout="->,total, prev, pager, next, jumper"
            :total="pageDate.total"
            class="page">
      </el-pagination>
    </el-card>
  </section>
</template>

<script>
//引入子组件
import addButton from "../components/header/addButton";
export default {
  name: "index",
  //声明子组件
  components:{addButton},
  data() {
    return {
      tableLoading:false,
      borrowingForm:{},
      borrowingRules:{},
      borrowingDialogVisible:false,
      tableData: [],
      currentPageSize:0,
      pageDate:{
        index:0,
        size:0,
        total:0,
      }
    }
  },
  methods: {
    $init(){
      this.queryAll()
    },
    //查询方法  parameter需要传对象进来。
    queryAll(parameter){
      this.tableLoading =true
      parameter = Object.assign({}, parameter,{
        pageIndex:this.currentPageSize
      })
      this.$http.post("Borrowing/page",parameter).then(({data})=>{
        if (data!=null&&data.success){
          this.tableData = data.data.data
          this.pageDate.index = data.data.start / data.data.size + 1
          this.pageDate.size = data.data.size
          this.pageDate.total = data.data.total
          //页数归零，让档案页数为第一页,去掉这个归零。当前页刷新。
          this.currentPageSize = 0
        }
        this.tableLoading =false
      })
    },
    //table操作栏方法
    //是否启用策略
    enableOrNot(row,data){
      row.enableOrNot = data
      this.$http.post("Borrowing/update",row).then(({data})=>{
        if(data.success){
          this.$message.success("请求成功")
        }
      })

    },
    //删除方法
    deleteRow(row){
      this.$http.post("Borrowing/delete",row).then(({data})=>{
       // console.log(data)
        if(data.success){
          this.$message.success("删除成功")
          //可以获取当前页数，不需要返回到第一页
          this.handleCurrentChange(this.pageDate.index)
        }
      })
    },

    handleSizeChange(val) {
     // console.log(`每页 ${val} 条`);
    },
    handleCurrentChange(val) {
      let chil =  this.$refs.addButtonRef
      this.currentPageSize = val-1
      this.queryAll(chil.formInline)
      //console.log(`当前页: ${val}`);
    },

  },
}
</script>

<style scoped>
.box-card {
  height: 90%;
}
.page{
  padding-top: 10px;
  float: right;
  background-color: #ffffff;
}
</style>