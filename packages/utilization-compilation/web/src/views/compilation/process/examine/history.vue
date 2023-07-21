<template>
  <section>
    <el-timeline style="padding-top:20px">
      <el-timeline-item
          v-for="(activity, index) in managementHistoryList"
          :key="index"
          :timestamp="$moment(activity.createDate).format('YYYY-MM-DD HH:mm:ss')">
        <el-link @click="preViewer(activity.id)">{{ activity.createPersonName }}</el-link>
      </el-timeline-item>
    </el-timeline>
    <el-dialog
        :center="true"
        :fullscreen="true"
        :visible.sync="showHistory"
        append-to-body>
      <el-card style="margin-top: 10px" v-if="showHistory">
        <div slot="header">
          <strong>{{ managementHistory.createPersonName }}-{{
              $moment(managementHistory.createDate).format('YYYY-MM-DD HH:mm:ss')
            }}</strong>
        </div>
        <div v-html="managementHistory.compilationContent" class="table">{{
            managementHistory.compilationContent
          }}
        </div>
      </el-card>
    </el-dialog>
  </section>
</template>

<script>
export default {
  name: "history",
  props: {
    businessId: {type: String}
  },
  data() {
    return {
      //修改历史记录
      managementHistoryList: [],
      //历史记录信息
      managementHistory: {},
      //是否显示历史记录内容
      showHistory: false
    }
  },
  methods: {
    $init() {
      if (this.businessId) {
        this.getManagementHistoryList(this.businessId)
      }
    },
    //获取历史记录
    async getManagementHistoryList(compilationTaskId) {
      const {data} = await this.$http.post('compilationTaskHistory/page', {
        page: false,
        compilationTaskId: compilationTaskId
      })
      if (data && data.success) {
        this.managementHistoryList = data.data
      }
    },
    //预览历史记录
    preViewer(id) {
      this.$router.push({
        path: '/compilation/process/examine/contrast',
        query: {
          managementCurrentId: this.businessId,
          managementHistoryId: id,
        }
      })
    }
  }
}
</script>

<style scoped>

</style>