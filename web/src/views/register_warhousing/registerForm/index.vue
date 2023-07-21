<template>
  <section>
    <nac-info title="入库登记表" back>
    </nac-info>
    <div style="width: 899px;height: 900px; margin: 0px auto;">
      <el-row>
        <el-col :span="24"
                style="text-align: center;font-size: 20px;line-height: 80px;color: #606266; font-weight: bold;">
          电子档案入库登记
        </el-col>
      </el-row>
      <div class="tableClass">
        <el-row type="flex">
          <el-col class="line title1" :span="12">操作人</el-col>
          <el-col class="line" :span="12">{{record.person_name}}</el-col>
        </el-row>
        <el-row type="flex">
          <el-col class="line title1" :span="12">入库时间</el-col>
          <el-col class="line" :span="12">{{ record.createDate|datetime }}</el-col>
        </el-row>
        <el-row type="flex">
          <el-col class="line title1" :span="12">数量</el-col>
          <el-col class="line" :span="12">{{ record.quantity }}</el-col>
        </el-row>
        <el-row type="flex">
          <el-col class="line title1" :span="12">备注</el-col>
          <el-col class="line" :span="12">{{ record.remarks }}</el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="6">题名</el-col>
          <el-col class="line title1" :span="6">档号</el-col>
          <el-col class="line title1" :span="4">年度</el-col>
          <el-col class="line title1" :span="4">文件形成日期</el-col>
          <el-col class="line title1" :span="4">保管期限</el-col>
        </el-row>
        <el-row v-for="item in recordList"  :key="item.id"  >
          <el-row type="flex">
            <el-col class="line title2" :span="6">{{item.timing}}</el-col>
            <el-col class="line title2" :span="6">{{item.archiveCode}}</el-col>
            <el-col class="line title2" :span="4">{{item.nd}}</el-col>
            <el-col class="line title2" :span="4">{{item.wjxcrq}}</el-col>
            <el-col class="line title2" :span="4">{{item.bgqx}}</el-col>
          </el-row>
        </el-row>
      </div>

<!--      <el-row style="text-align: center;margin-top: 20px">
          <el-button @click="dayin" type="primary" >打 印</el-button>
      </el-row>-->
    </div>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      loading: false,
      batchData:{},
      recordList: {},
      record:{},
      report:{},
    }
  },
  methods: {
    $init() {
      this.record = this.$route.query.row
      this.getDetailData()
      this.getReport()
    },
    async getDetailData(){
      let batchId=this.record.id
      const {data} = await this.$http.post('registerWarehousingDetails/page',{bId:batchId})
      this.recordList = data.data.data
    },
    getReport(){
      this.$http.post('receive/offline/getReport', {id: this.id})
          .then(({data}) => {
            if (data.success) {
              this.report = data.data
            }
          })
    },
    // 一会放开
    dayin() {
      this.$http.post("/registerWarehousing/download", {id:this.record.id}).then((data) => {
        if (data.data.success) {
          window.open("api/registerWarehousing/registerPDF")
        }
      })
    },
  },
  // computed: {
  //   id() {
  //     return this.$route.query.batchId
  //   },
  // }
}
</script>

<style scoped>
.tableClass{
  padding: 10px;
  background: #fff;
}
.line {
  line-height: 35px;
  padding: 5px;
  padding-left: 12px;
  border: 1px solid #d0e5fd;
  text-align: left;
  color: #606266;
}

.m {
  text-align: left;
  height: 77px;
  line-height: 70px;
}

.title1 {
  line-height: 35px;
  border: 1px solid #d0e5fd;
  text-align: left;
  font-size: 16px;
  background: #ecf2ff;
  color: #606266;
}
.title2 {
  line-height: 35px;
  border: 1px solid #d0e5fd;
  text-align: left;
  font-size: 16px;
  color: #606266;
}

.l-right {
  float: right;
  text-align: right;
  color: #b3bac7;
  font-size: 14px;
  line-height: 70px;
  margin-right: 10px;
}

.sh {
  height: 77px;
  line-height: 70px;
}

.no {
  font-size: 28px;
  text-align: center;
}

</style>