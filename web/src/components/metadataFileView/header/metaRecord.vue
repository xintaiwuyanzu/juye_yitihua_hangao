<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button type="primary" @click="showRecord">管理过程元数据记录</el-button>
    <el-drawer title="管理过程元数据记录"
               :visible.sync="drawerShow"
               direction="rtl"
               size="50%"
               custom-class="table_drawer"
               append-to-body>
      <table-index :insert="false" :edit="false" ref="table"
                   :delete="false"
                   :fields="columns" path="archiveMetadataRecord"
                   :showTitle="false"
                   :defaultSearchForm="searchForm"/>
    </el-drawer>
  </section>
</template>
<script>
import abstractArchiveDetail from "../abstractArchiveDetail";

/**
 * 管理过程元数据
 */
export default {
  name: "metaRecord",
  extends: abstractArchiveDetail,
  data() {
    return {
      drawerShow: false,
      columns: [
        {
          prop: 'ywxw', label: '业务行为', width: 100, component: 'tag', showTypeKey: 'show', mapper: {
            'ADD': {label: '手动添加', show: 'warning'},
            'OFFLINE_RECEIVE': {label: '离线归档接收', show: 'danger'},
            'OFFLINE_YIJIAO': {label: '离线移交', show: 'success'},
            'ONLINE_RECEIVE': {label: '在线归档接收', show: 'warning'},
            'EDIT': {label: '编辑', show: 'success'},
            'INGL': {label: '入库', show: 'info'},
            'OUTGL': {label: '出库', show: 'info'},
            'PILIANGXIUGAI': {label: '批量修改', show: 'info'},
            'TIAOZHENG': {label: '调整', show: 'success'},
            'JIANDING_DAOQI': {label: '到期鉴定', show: 'warning'},
            'JIANDING_KAIFANG': {label: '开放鉴定', show: 'danger'},
            'XIAOHUI': {label: '销毁', show: 'primary'},
            'YIJIAO': {label: '移交', show: 'primary'},
          }
        },
        // {prop: 'xwms', label: '行为描述', search: true},
        {prop: 'alterName', label: '修改字段', search: false, width: 80},
        {prop: 'oldValue', label: '原始值', search: false, width: 80},
        {prop: 'newValue', label: '修改值', search: false},
        {prop: 'changePersonName', label: '行为人', width: 100},
        {prop: 'createDate', label: '行为时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
      ],
    }
  },
  methods: {
    searchForm() {
      return {formDataId: this.formData.id}
    },
    /**
     * 显示弹窗
     */
    async showRecord() {
      this.drawerShow = true
      const table = this.$refs.table
      if (table) {
        await table.reload()
      }
    }
  }
}
</script>