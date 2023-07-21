<template>
  <el-card>
<!--    <el-tabs v-model="showPane">
      <el-tab-pane label="纠错" name='one'>
        <div>
          <form-render label-width="80px" :fields="addfields" ref="form" :model="form"
                       style="margin-right: 5px">
          </form-render>
          <div slot="footer" style="text-align: right">
            <el-button type="primary" @click="saveForm">保 存</el-button>
          </div>
        </div>
      </el-tab-pane>
      <el-tab-pane label="纠错历史" name='two'>
        <table-index :fields="fields" path="/errorCorrection" :defaultSearchForm="defaultSearchForm"
                     :delete="false" :edit="false" :insert="false" :showTitle="false" ref="table">
        </table-index>
      </el-tab-pane>
    </el-tabs>-->
    <table-index :fields="fields" path="/errorCorrection" :defaultSearchForm="defaultSearchForm"
                 :delete="false" :edit="false" :insert="false" :showTitle="false" ref="table">
    </table-index>
  </el-card>
</template>

<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";

export default {
  name: "errorCorrection",
  extends: abstractArchiveDetail,
  data() {
    return {
      showPane: 'one',
      form: {},
      addfields: {
        errorDescription: {label: '调整说明', align: 'center'},
      },
      fields: [
        {
          prop: 'errorDescription',
          label: '调整说明',
        },
        {
          prop: 'status',
          label: '状态',
          component: 'tag',
          showTypeKey: 'show',
          fieldType: 'select',
          edit: false,
          mapper: {
            '0': {label: '待纠错', show: 'warning'},
            '1': {label: '已纠错', show: 'success'},
            '2': {label: '纠错中', show: 'info'},
            '3': {label: '未通过', show: 'danger'}
          }
        }
      ],
      defaultSearchForm: {formDataId: this.formData.id}
    };
  },
  methods: {
    saveForm() {
      this.$http.post("/errorCorrection/insert", Object.assign({
        formDataId: this.formData.id,
        formDefinitionId: this.formDefinitionId,
        title: this.formData.TITLE,
        archiveCode: this.formData.ARCHIVE_CODE,
        fondCode: this.formData.FOND_CODE,
        categoryCode: this.formData.CATE_GORY_CODE,
        errorType: '2',
        errorSource: '档案详情'
      }, this.form))
          .then(({data}) => {
            if (data.success) {
              this.$message.success("添加成功")
              this.form = {}
              this.$refs.table.loadData()
            }
          })
    },
  }
}
</script>

<style scoped>

</style>