<template>
  <el-dialog title="选择用户"
             :visible.sync="show"
             :close-on-click-modal=false
             :close-on-press-escape=false
             :before-close="close"
             width="60%">
    <div v-show="tableShow" style="min-height:450px;margin-top: 10px;">
      <table-index
          ref="table"
          path="person"
          pagePath="appraisalBatchPerson/currentOrganisePersonsPage"
          :fields="fields"
          :edit="false"
          :delete="false"
          :insert="false"
          :showTitle="false"
          style="height: 450px;display: flex;flex-direction: column"
          :defaultSearchForm="defaultSearchForm">
        <template v-slot:table-$btns="scope">
          <el-button type="text" v-if="userIds.lastIndexOf(scope.row.id)<0"
                     @click="addPerson(scope.row)"
                     size="mini" width="60">添加
          </el-button>
        </template>
      </table-index>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      show: false,
      form: {},
      fields: {
        userName: {label: '用户姓名', search: true},
        userCode: {label: '用户编号', search: true},
        phone: {label: '手机号', search: false},
        defaultOrganiseName: {label: '所属单位', search: false}
      },
      defaultSearchForm: {pageSize: 10},
      tableShow: true,
      tableHight: 600
    }
  },
  props: {
    batchId: '',
    userIds: {type: Array}
  },
  methods: {
    cancelDialog() {
      this.show = false
      this.form = {}
    },
    loadData() {
      if (this.$refs.table) {
        this.$refs.table.reload()
      }
    },
    addPerson(row) {
      let person = {
        personId: row.id,
        batchId: this.batchId
      }
      this.$http.post("/appraisalBatchPerson/insert", person)
          .then(({data}) => {
            if (data.success) {
              this.$message.success("添加成功")
              this.userIds.push(row.id)
            }
          })
    },
    close() {
      this.$emit("reload")
      this.show = false
    }
  }
}
</script>

<style scoped>

</style>
