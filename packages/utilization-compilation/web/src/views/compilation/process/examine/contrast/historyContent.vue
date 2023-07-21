<template>
  <section style="height: 100%">
    <el-card style="height: 100%;overflow: scroll">
      <div slot="header" class="clearfix">
        <span v-if="type==='current'">当前版本</span>
        <span v-else-if="type==='history'">选中版本【{{
            managementHistory.createPersonName + '--' +
            $moment(managementHistory.createDate).format('YYYY-MM-DD HH:mm:ss')
          }}】</span>
      </div>
      <div v-html="managementHistory.compilationContent" class="table1">{{
          managementHistory.compilationContent
        }}
      </div>
    </el-card>
  </section>
</template>

<script>
export default {
  name: "historyContent",
  data() {
    return {
      managementHistory: {},
      fileList: []
    }
  },
  props: {
    managementHistoryId: {type: String},
    type: {type: String}
  },
  watch: {
    managementHistoryId() {
      this.getManagementHistory()
    }
  },
  methods: {
    $init() {
      this.getManagementHistory()
      // this.getFileList()
    },
    //获取信息详情
    async getManagementHistory() {
      if (this.type === 'history') {
        const {data} = await this.$http.post('/compilationTaskHistory/detail', {id: this.managementHistoryId})
        if (data && data.success) {
          this.managementHistory = data.data
        }
      } else if (this.type === 'current') {
        const {data} = await this.$http.post('/compilationtask/detail', {id: this.managementHistoryId})
        if (data && data.success) {
          this.managementHistory = data.data
        }
      }
    },
  }
}
</script>