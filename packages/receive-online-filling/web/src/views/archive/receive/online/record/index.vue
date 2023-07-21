<template>
  <table-index :fields="fields" path="/receive/online" :defaultSearchForm="defaultSearchForm"
               :insert="false" :edit="false"
               ref="table"
               :delete="true">
    <!--    <template v-slot:table-$btns="scope">
          <el-button type="text" size="mini" width="50" @click="report(scope.row)">{{ reportTitle }}</el-button>
        </template>-->
    <!--报告弹窗-->
    <el-dialog :visible.sync="reportDialog.reportDialogVisible" :title="reportTitle" width="80%">
      <form-render style="padding: 10px 20px" :fields="reportFormFields" :model="reportForm"
                   label-width="160px" v-loading="reportDialog.loading"
                   ref="reportForm" inline>
      </form-render>
      <div slot="footer" class="dialog-footer">
        <el-button @click="reportDialog.reportDialogVisible=false">确 定</el-button>
      </div>
    </el-dialog>
    <!--TODO 预览弹窗-->
    <file-list refType="PRE_ARCHIVE" groupCode="PRE_ARCHIVE" :formDataId="formDataId" title="移交目录清单" :deleter="false"
               :upload="false"
               :btn-preview="btnPreview"
               :transform="false"
               style="margin-top: 5px"
               width="50%" v-if="fileListDialog"/>
  </table-index>
</template>
<script>
/**
 * 批次详情列表
 */
export default {
  data() {
    return {
      fileListDialog: false,
      formDataId: '',
      reportTitle: '接收报告',
      reportDialog: {
        reportDialogVisible: false,
        loading: false
      },
      reportForm: {},
      btnPreview: '下载',
      //新代码
      fields: [
        {
          prop: 'batchName',
          label: '归档批次',
          search: true,
          routerPath: '/archive/receive/online/record/detail',
          component: 'text',
          route: true,
          queryProp: (row) => ({batchId: row.id,status:row.status})
        },
        {prop: 'systemNum', label: '归档系统编码', width: 160},
        {prop: 'transferUnitPerson', label: '归档人', width: 100},
        {prop: 'startDate', label: '归档时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'endDate', label: '完成时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'detailNum', label: '归档数量', width: 80},
        {
          prop: 'status', label: '状态', width: 80,
          component: 'tag',
          mapper: {
            '0': {label: '归档中', show: ''},
            '1': {label: '已办结', show: 'success'},
            '2': {label: '归档失败', show: 'danger'},
            '3': {label: '已退回', show: 'info'},
            '4': {label: '-', show: 'info'},
            '5': {label: '入库审核中', show: 'info'},
            '6': {label: '已入库', show: 'info'}
          },
        },
        {
          prop: 'testStatus', label: '四性检测', width: 80, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '不通过', show: 'danger'},
            '1': {label: '通过', show: 'success'},
            '-1': {label: '未执行', show: 'info'},
          },
        },
      ],
      defaultSearchForm: {batchType: "PRE_ARCHIVE"},
      reportFormFields: [
        {prop: 'detailNum', label: '接收目录数据总数', ref: 'detailNum', disabled: true},
        {prop: 'metadataSucessNum', label: '接收目录数据成功数量', ref: 'metadataSucessNum', disabled: true},
        {prop: 'metadataFalseNum', label: '接收目录数据失败数量', ref: 'metadataFalseNum', disabled: true},
        {prop: 'fourSexTestSucessNum', label: '四性检测通过数量', ref: 'fourSexTestSucessNum', disabled: true},
        {prop: 'fourSexTestFalseNum', label: '四性检测未通过数量', ref: 'fourSexTestFalseNum', disabled: true}]
    }
  },
  methods: {
    async download(row) {
      this.fileListDialog = true
      this.formDataId = row.id
    },
    async report(row) {
      this.reportDialog.reportDialogVisible = true
      this.reportDialog.loading = true

      const {data} = await this.$post('/receive/online/getReport', {
        batchId: row.id
      })
      if (data.success) {
        this.reportForm = data.data
        this.reportForm.detailNum = row.detailNum
      }

      this.reportDialog.loading = false
    }
  },
  computed: {
    metadataFalseNum() {
      return this.reportForm.detailNum - this.reportForm.metadataSucessNum
    }
  }
}
</script>
