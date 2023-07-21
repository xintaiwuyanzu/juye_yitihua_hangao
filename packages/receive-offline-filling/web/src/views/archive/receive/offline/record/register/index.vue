<template>
    <section>
        <nac-info title="电子档案登记" back>
        </nac-info>
        <div style="width: 899px;height: 900px; margin: 10px auto;">
            <el-row>
                <el-col :span="24"
                        style="text-align: center;font-size: 20px;line-height: 80px;color: #606266; font-weight: bold;">
                    电子档案登记
                </el-col>
            </el-row>
          <div class="tableClass">
            <el-row type="flex">
              <el-col class="line title1" :span="4">全宗名称</el-col>
              <el-col class="line" :span="8">{{record.fondName }}</el-col>
              <el-col class="line title1" :span="4">分类名称</el-col>
              <el-col class="line" :span="8">{{ record.categoryName }}</el-col>
            </el-row>
            <el-row type="flex">
              <el-col class="line title1" :span="4">接收数据总数</el-col>
              <el-col class="line" :span="8">{{ report.detailNum }}</el-col>
              <el-col class="line title1" :span="4">已挂接数量</el-col>
              <el-col class="line" :span="8">{{ report.hookSucessNum}}</el-col>
            </el-row>
            <el-row type="flex">
              <el-col class="line title1" :span="4">未挂接数量</el-col>
              <el-col class="line" :span="8">{{ report.hookFalseNum }}</el-col>
              <el-col class="line title1" :span="4">四性检测通过数量</el-col>
              <el-col class="line" :span="8">{{ report.fourSexTestSucessNum }}</el-col>
            </el-row>
            <el-row type="flex">
              <el-col class="line title1" :span="4">四性检测不通过数量</el-col>
              <el-col class="line" :span="20">{{ report.fourSexTestFalseNum }}</el-col>
            </el-row>
            <el-row>
              <el-col class="line title1" :span="4">档号</el-col>
              <el-col class="line title1" :span="8">题名</el-col>
              <el-col class="line title1" :span="4">年度</el-col>
              <el-col class="line title1" :span="4">挂接状态</el-col>
              <el-col class="line title1" :span="4">四性检测</el-col>
            </el-row>
            <el-row v-for="item in recordList"  :key="item.id"  >
              <el-row type="flex">
                <el-col class="line title2" :span="4">{{item.archiveCode}}</el-col>
                <el-col class="line title2" :span="8">{{item.title}}</el-col>
                <el-col class="line title2" :span="4">{{item.year}}</el-col>
                <el-col class="line title2" :span="4">{{item.hookStatus}}</el-col>
                <el-col class="line title2" :span="4">{{item.testStatus}}</el-col>
              </el-row>
            </el-row>
          </div>

            <el-row style="text-align: center;margin-top: 20px">
                <span>
                    <el-button @click="dayin" type="primary" >打 印</el-button>
                </span>
            </el-row>
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
                recordList:[],
                record:{},
                report:{}
            }
        },
        methods: {
            $init() {
                this.getBatch()
                this.getDetailData()
                this.getReport()
            },
            getBatch(){
                this.$http.post('receive/offline/detail', {id: this.batchId})
                    .then(({data}) => {
                        if (data.success) {
                            this.batchData = data.data
                        }
                    })
            },
            getDetailData() {
                this.$http.post('receive/offline/detail/page', {batchId: this.batchId})
                    .then(({data}) => {
                        if (data.success) {
                            this.recordList = data.data.data
                            this.recordList.map(val => {
                                let hookStr = val.hookStatus === '0' ? '未挂接' : '已挂接'
                                val.hookStatus = hookStr
                                let testStr = val.testStatus === '0' ? '不通过' : ( val.testStatus === '1'?'通过':'未执行')
                                val.testStatus = testStr
                            })
                            //console.log(this.recordList)
                            this.record = this.recordList[0]
                        }
                    })
            },
            getReport(){
                this.$http.post('receive/offline/getReport', {batchId: this.batchId})
                    .then(({data}) => {
                        if (data.success) {
                            this.report = data.data
                        }
                    })
            },
            dayin() {
                this.$http.post("/receive/offline/expElecFile", {batchId: this.batchId}).then((data) => {
                    console.log(data.data)
                    if (data.data.success) {
                        window.open("api/receive/offline/registerPDF")
                    }
                })
            },
        },
        computed: {
            batchId() {
                return this.$route.query.batchId
            },

        }
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