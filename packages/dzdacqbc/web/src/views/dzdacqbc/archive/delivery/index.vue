<template>
  <table-index :fields="fields"
               path="deliveryBatchDetailItem"
               :defaultInsertForm="defaultInsertForm"
               :defaultSearchForm="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false"
               back
               ref="table"
               style="height:80vh;display: flex;flex-direction: column">
    <template v-slot:search-$btns="scope">
      <task-container :task-instance-id="deliveryId"
                      :business-id="$route.query.businessId"
                      style="display: inline-flex">
        <template v-slot:sendNext="params">
          <send-next :before-open="beforeOpenDialogOpen" @sendSaved="sendSaved">
          </send-next>
        </template>
        <template v-slot:endProcess="params">
          <end-process :before-open="beforeOpenDialogOpen" @saveEnd="saveEnd">
          </end-process>
        </template>
        <template v-slot:endProcess="params">
          <end-process :before-open="beforeOpenDialogOpen" @saveEnd="saveEnd">
            <template v-slot:sendForm="{form}">
              <el-form-item prop="isPass" label="是否通过" required>
                <el-radio-group v-model="form.isPass">
                  <el-radio v-model="form.isPass" label="2">是</el-radio>
                  <el-radio v-model="form.isPass" label="3">否</el-radio>
                </el-radio-group>
              </el-form-item>
            </template>
          </end-process>
        </template>
      </task-container>
      <!--<el-button type="primary" width="20" @click="fastDelivery()">一键审核</el-button>-->
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="goDelivery(scope)" type="text" width="20">审核</el-button>
    </template>
  </table-index>
</template>
<script>
import {endProcess, sendNext, taskContainer} from "@dr/process/src/lib";

export default {
  components: {taskContainer, sendNext, endProcess},
  name: "index",
  data() {
    return {
      fields: {
        archiveCode: {label: '档号', search: true, edit: false, align: 'center', width: 200},
        year: {label: '年度', search: false, edit: false, align: 'center', width: 100},
        title: {label: '题名', search: true, edit: false, align: 'center'},
        deliveryStatus: {
          label: '状态', search: false, edit: false, align: 'center', width: 100,
          mapper: [{
            id: '1',
            label: '已审核'
          }, {
            id: '0',
            label: '未审核'
          }],
        },
      },
      defaultSearchForm: {
        deliveryId: this.$route.query.businessId
      },
      defaultInsertForm: {},
      deliveryId: '',
      businessId: '',
      key: 0
    }
  },
  methods: {
    $init() {
      this.deliveryId = this.$route.query.taskId
      this.businessId = this.$route.query.businessId
      this.defaultSearchForm.deliveryId = this.$route.query.businessId
    },
    search() {
      this.$refs.search.show = true
    },
    async beforeOpenDialogOpen(form) {
      this.$set(form, 'isPass', '2')
      const {data} = await this.$http.post("/deliveryBatchDetailItem/checkSubmit", {
        deliveryId: this.$route.query.businessId,
        isPass: this.form.isPass
      })
      if (data.success && data.data === 0) {
        return true
      } else {
        this.$message.error("有未审核档案信息，请处理之后重试")
        return false
      }
    },
    goDelivery(scope) {
      this.$router.push({
        path: './deliverydetail', query: {
          formDefinitionId: scope.row.formDefinitionId,
          formDataId: scope.row.formDataId,
          index: scope.$index + (parseInt(this.$refs.table.data.page.index) - 1) * parseInt(this.$refs.table.data.page.size)
        }
      })
    },
    fastDelivery() {
      this.$confirm('此操作将未审核档案统一做审核通过处理, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/deliveryBatchDetailItem/fastDelivery", {deliveryId: this.businessId})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("一键审核" + data.data + "份档案。")
                this.$refs.table.loadData()
              } else {
                this.$message.error(data.message)
              }
            })
      })
    },
    sendSaved() {
      this.$router.push({path: '/process/taskTodo'})
    },
    saveEnd() {
      this.$router.push({path: '/process/taskTodo'})
    },
  },
}
</script>

<style scoped>

</style>