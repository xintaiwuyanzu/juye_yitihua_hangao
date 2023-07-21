<template>
  <table-index path="donetask" :fields="fields" :edit="false" :insert="false" :delete="false"
               :defaultSearchForm="searchForm">
    <el-table-column align-="center" label="任务描述" prop="title" header-align="center">
      <template v-slot="{row}">
        <el-button type="text">{{ row.title }}</el-button>
      </template>
    </el-table-column>
    <template slot="table-$btns" slot-scope="{row}">
      <el-button type="text" width="60" @click="()=>showHistory(row.id)">流转历史</el-button>
    </template>
  </table-index>
</template>
<script>
import abstractProcess from "@dr/process/src/lib/abstractProcess";

/**
 * 待办任务首页
 */
export default {
  extends: abstractProcess,
  data() {
    return {
      searchForm: {withProcessVariables: false,createPerson:'admin'},
      fields: {
        type: {
          label: '任务类型',
          url: '/processDefinition/processType',
          labelKey: 'name',
          valueKey: 'type',
          width: 120,
          requestMethod: 'get'
        },
        title: {label: '任务描述', search: true},
      /*  ownerName: {label: '任务发起人', search: true, width: 100},*/
        createPersonName: {label: '发送人', width: 100},
        createDate: {dateFormat: true, label: '发送时间', width: 100},
        name: {label: '当前环节名称', width: 140}
      }
    }
  },
  methods: {
    /**
     * 跳转详情页
     * @param row
     */
    showDetail(row) {
      let path = row.formUrl
      if (!path) {
        path = this.$route.path + "/detail"
        this.$message.warning('未配置详情页面，跳转默认页面')
      }
      const processVariables = row.processVariables ? row.processVariables : {}
      this.$router.push({
        path: path,
        query: {
          //流程实例Id
          processInstanceId: row.processInstanceId,
          //环节实例Id
          taskId: row.id,
          //批次Id
          batchId: processVariables.batchId,
          //业务外键
          businessId: processVariables.$businessId
        }
      })
    }
  }
}
</script>