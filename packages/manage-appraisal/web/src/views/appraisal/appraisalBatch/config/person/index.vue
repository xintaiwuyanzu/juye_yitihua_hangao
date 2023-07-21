<template>
  <section>
    <table-render :columns="columns" :data="data" ref="table" style="height: 22vh">
      <el-table-column label="操作" width="60" header-align="center" align="center">
        <template v-slot="scope">
          <el-button type="text" @click="remove(scope.row)">移除</el-button>
        </template>
      </el-table-column>
    </table-render>
    <person-table ref="personTable" v-on:reload="loadData" :batch-id="batchId" :user-ids="userIds"></person-table>
  </section>
</template>
<script>
import personTable from './personTable'

export default {
  components: {
    personTable
  },
  name: "index",
  data() {
    return {
      columns: {
        personName: {label: '用户姓名'},
        personCode: {label: '用户编码'},
        personPhone: {label: '联系电话'}
      },
      data: [],
      userIds: []
    }
  },
  props: {
    batchId: ''
  },
  methods: {
    async $init() {
      await this.loadData()
    },
    async loadData() {
      const {data} = await this.$http.post("/appraisalBatchPerson/page", {batchId: this.batchId})
      this.data = data.data.data
      this.userIds = []
      this.data.forEach(v => {
        this.userIds.push(v.personId)
      })
    },
    remove(row) {
      this.$http.post("/appraisalBatchPerson/delete", {id: row.id})
          .then(({data}) => {
            if (data.success) {
              this.$message.success("移除成功")
              this.loadData()
            }
          })
    },
    addPerson() {
      this.$refs.personTable.show = true
      this.$refs.personTable.loadData()
    }
  }
}
</script>
