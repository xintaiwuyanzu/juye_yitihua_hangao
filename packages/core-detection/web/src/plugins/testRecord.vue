<template>
  <table-index :fields="fields" path="/testRecord" :defaultSearchForm="defaultSearchForm"
               :delete="false" :edit="false" :insert="false" :showTitle="false"
               style="padding: 0 0;display: flex">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="30" @click="toTestReport(scope.row)">检测报告</el-button>
    </template>
  </table-index>
</template>

<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";

export default {
  name: "testRecord",
  extends: abstractArchiveDetail,
  data() {
    return {
      fields: [
        {
          prop: 'linkCode',
          label: '检测环节',
          width: 90,
          component: 'tag',
          mapper: {'gd': '归档环节', 'yj': '移交环节', 'bc': '长期保存环节', 'rg': '人工手动检测', 'qt': '其他环节'}
        },
        {
          prop: 'status',
          label: '检测结果',
          width: 80,
          component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '不通过', show: 'danger'},
            '1': {label: '通过', show: 'success'}
          },

        },

        {prop: 'createDate', label: '检测时间', dateFormat: 'YYYY-MM-DD HH:mm:ss'},
      ],
      defaultSearchForm: {formDefinitionId: this.formDefinitionId, formDataId: this.formData.id}
    }
  },
  methods: {
    //跳转到四性检测报告页面
    toTestReport(row) {
      this.$router.push({
        path: '/testrecorditem',
        query: {
          recordId: row.id
        }
      })
    },
  }
}
</script>
