<template>
  <div class="block" style="height: 42vh;overflow: auto;">
    <el-timeline v-if="data.length!==0">
      <el-timeline-item v-for="(item,index) in data" :key="index"
                        :timestamp="item.createDate|date('YYYY-MM-DD HH:mm:ss')" placement="top">
        <el-card>
          <h4 v-if="index==0">{{ item.appraisalPersonName }}提交鉴定</h4>
          <h4 v-else>{{ item.appraisalPersonName }}鉴定审批</h4>
          <p style="padding-top: 5px">一&#8194;致&#8194;性：{{
              item.isEqual|dict({
                '1': "与机器鉴定结果一致",
                '0': "与机器鉴定结果不一致"
              })
            }}</p>
          <p style="padding-top: 5px">人工鉴定：{{ transResult(item.personResult) }}</p>
          <p style="padding-top: 5px">关&#8194;键&#8194;词：{{ item.personAppraisalKeyWord }}</p>
          <p style="padding-top: 5px">鉴定依据：{{ item.personAppraisalBasis }}</p>
          <p style="padding-top: 5px">备&#12288;&#12288;注：{{ item.remarks }}</p>
        </el-card>
      </el-timeline-item>
    </el-timeline>
    <div v-else style="text-align: center;padding-top: 30px">
      <span style="font-size: 35px">无鉴定历史</span>
    </div>
  </div>
</template>
<script>
const result = {'kz': '控制', 'kf': '开放'}
export default {
  data() {
    return {
      data: [],
      resultMapper: {'xh': '销毁', 'yq': '延期'}
    }
  },
  props: {
    formDefinitionId: {type: String},
    formDataId: {type: String}
  },
  watch: {
    formDataId() {
      this.loadData()
    }
  },
  methods: {
    async loadData() {
      if (this.formDefinitionId && this.formDataId) {
        const {data} = await this.$http.post("archiveAppraisalMessage/page", {
          page: false,
          formDataId: this.formDataId,
          formDefinitionId: this.formDefinitionId,
          taskId: sessionStorage.getItem('taskId')
        })
        this.data = data.data
      }
    },
    async $init() {
      const {data} = await this.$http.post("/appraisalOpenRange/page", {page: false})
      data.data.forEach(v => {
        this.resultMapper[v.id] = result[v.auxiliaryResult] + v.openRange
      })
      await this.loadData()
    },
    transResult(r) {
      console.log(r)
      return this.resultMapper[r]
    }
  }
}
</script>
