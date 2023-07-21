<template>
  <section>
    <el-link type="primary" @click="show">管理过程元数据</el-link>
    <el-dialog width="90%" :title="title" :visible.sync="dialogShow" append-to-body>
      <el-table :data="tableData" border style="width: 100%">
        <!--        <el-table-column prop="fondName" label="全宗名称" width="180" align="center"/>
                <el-table-column prop="categoryName" label="分类名" width="180" align="center"/>
                <el-table-column prop="archiveCode" label="档号" width="180" align="center"/>-->
        <el-table-column prop="createDate" label="行为时间" width="140" dateFormat="YYYY-MM-DD HH:mm:ss" align="center"/>
        <el-table-column prop="changePersonName" label="行为人" width="180" align="center"/>
        <el-table-column prop="changeType" label="业务行为" width="80" show-overflow-tooltip align="center">
          <template slot-scope="scope">
            {{ scope.row.changeType|dict('archive.metadataChangeType') }}
          </template>
        </el-table-column>
        <el-table-column prop="xwms" label="行为描述" show-overflow-tooltip align="center"/>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: "metaData",
  computed: {
    title() {
      return '管理过程元数据'
    }
  },
  data() {
    return {
      dialogShow: false,
      tableData: {},
      loading1: false,
      dict: ['archive.metadataChangeType'],
    }
  },
  methods: {
    async show() {
      await this.getRecords()
      this.dialogShow = true
    },
    async getRecords() {
      const {data} = await this.$post('/archiveMetadataRecord/page', {
        page: false,
        formDefinitionId: this.formId,
        formDataId: this.row.id,
      })
      if (data.success) {
        this.tableData = data.data
      }
    }
  }
}
</script>