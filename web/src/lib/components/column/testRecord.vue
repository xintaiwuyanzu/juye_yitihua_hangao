<template>
  <section>
    <el-link type="warning" @click="showDialog">检测结果</el-link>
    <el-dialog width="50%" title="检测结果" :visible.sync="dialogShow" append-to-body>
      <el-table :data="data" border class="table-container">
        <el-table-column prop="order" label="排序" width="80" header-align="center" align="center">
          <template slot-scope="scope">
            {{ scope.$index + 1 }}
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="检测时间" header-align="center" align="center" show-overflow-tooltip
                         width="150px">
          <template slot-scope="scope">
            {{ scope.row.createDate|datetime }}
          </template>
        </el-table-column>
        <el-table-column prop="testRecordType" label="检测类型" header-align="center" align="center" show-overflow-tooltip
                         width="100px">
          <template slot-scope="scope">
            {{ scope.row.testRecordType|dict('archive.testType') }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="检测结果" header-align="center" align="center" show-overflow-tooltip
                         width="80px">
          <template slot-scope="scope">
            {{ scope.row.status|dict({'0': '未检测', '1': '通过', '2': '未通过'}) }}
          </template>
        </el-table-column>

        <el-table-column prop="testName" label="元数据" header-align="center" align="center" show-overflow-tooltip/>
        <el-table-column prop="testResult" label="说明" header-align="center" align="center" show-overflow-tooltip
                         width="220px"/>
      </el-table>

      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'testRecord',
  data() {
    return {
      dialogShow: false,
      data: [],
      dict: ['archive.testType'],
    }
  },
  methods: {
    showDialog() {
      this.dialogShow = true
      this.loading = true
      this.$http.post('/testrecord/page', {page: false, formDataId: this.row.id}).then(({data}) => {
        if (data && data.success) {
          this.data = data.data
        }
        this.loading = false
        this.dialogShow = true
      })
    },
  },
}
</script>
