<template>
  <section>
    <nac-info back></nac-info>
    <div class="index_main">
      <el-row class="container" type="flex" justify="space-between">
        <el-col :span="12">
          <el-card class="box-card" style="height: 30vh">
            <div slot="header">
              <span>鉴定批次基本信息</span>
            </div>
            <form-detail :batch-id="$route.query.batchId"></form-detail>
          </el-card>
        </el-col>
        <el-col :span="6" style="margin-left: 2px">
          <el-card class="box-card" style="height: 30vh">
            <div slot="header">
              <span>鉴定人员</span>
              <!--              <el-link  style="float: right;margin-left: 10px;color: #409EFF" @click="toConfig" size="mini" type="text">去配置</el-link>-->
              <el-button icon="el-icon-plus" @click="addPerson()" type="primary" size="mini" circle
                         style="float: right;">
              </el-button>
            </div>
            <div style="max-height: 25vh;overflow:auto;">
              <person-config ref="person" :batch-id="$route.query.batchId"></person-config>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6" style="margin-left: 2px">
          <el-card class="box-card" style="height: 30vh">
            <div slot="header">
              <span>档案鉴定进度</span>
            </div>
            <pie-of-batch-archive-status :batch-id="$route.query.batchId"></pie-of-batch-archive-status>
          </el-card>
        </el-col>
      </el-row>
      <el-row class="container" style="margin-top: 2px" type="flex" justify="space-between">
        <el-col :span="12">
          <el-card class="box-card" style="height: 48vh">
            <div slot="header">
              <span>档案全宗年度分布</span>
              <el-link style="float: right;margin-left: 10px;color: #409EFF" @click="toArchive" size="mini" type="text">
                档案列表
              </el-link>
            </div>
            <fond-and-vintages :batch-id="$route.query.batchId"></fond-and-vintages>
          </el-card>
        </el-col>
        <el-col :span="12" style="margin-left: 2px ;">
          <el-card class="box-card" style="height: 48vh">
            <div slot="header">
              <span>鉴定档案类型分布</span>
            </div>
            <histogram-of-auxiliary :batch-id="$route.query.batchId"></histogram-of-auxiliary>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
<script>
import pieOfBatchArchiveStatus from './vcharts/pieOfBatchArchiveStatus'
import fondAndVintages from './fondAndVintages'
import formDetail from './form'
import personConfig from '../config/person'
import histogramOfAuxiliary from './vcharts/histogramOfAuxiliary'

export default {
  components: {pieOfBatchArchiveStatus, fondAndVintages, formDetail, personConfig, histogramOfAuxiliary},
  data() {
    return {}
  },
  methods: {
    toConfig() {
      this.$router.push({path: "/appraisal/appraisalBatch/config", query: {batchId: this.$route.query.batchId}})
    },
    toArchive() {
      this.$router.push({
        path: "/appraisal/appraisalBatch/detail/archive",
        query: {
          batchId: this.$route.query.batchId,
          appraisalType: this.$route.query.appraisalType,
        }
      })
    },
    addPerson() {
      this.$refs.person.addPerson()
    }
  }
}
</script>
