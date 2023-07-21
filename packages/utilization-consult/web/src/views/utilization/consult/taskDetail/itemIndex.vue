<template>
  <el-drawer title="档案详情列表"
             :visible.sync="visible"
             direction="rtl"
             size="600px"
             custom-class="table_drawer"
             append-to-body>
    <table-index path="/archiveCarDetail"
                 :defaultSearchForm="defaultSearchForm"
                 :fields="detailFields"
                 :index="true"
                 @row-dblclick="selectDetail"
                 ref="table"
                 :showTitle="false"
                 :edit="false"
                 :delete="false"
                 :insert="false"/>
  </el-drawer>
</template>
<script>
/**
 * 档案列表数据
 */
export default {
  name: "itemIndex",
  props: {
    //批次Id
    batchId: {type: String}
  },
  data() {
    return {
      visible: false,
      detailFields: {
        archiveCode: {label: '档号', search: true, width: 160},
        title: {label: '题名'},
        fondName: {label: '全宗', width: 120},
      }
    }
  },
  methods: {
    /**
     * 查询条件
     * @returns {{batchId: string}}
     */
    defaultSearchForm() {
      return {batchId: this.batchId}
    },
    /**
     * 选中详情数据
     * @param row
     */
    selectDetail(row) {
      const tableData = this.$refs.table.data
      const index = tableData.data.indexOf(row)
      const params = {
        //查询表单
        search: this.$refs.table.searchFormModel,
        //一行数据
        row,
        //位置信息
        index,
        //分页信息
        page: tableData.page
      }
      this.$emit('itemSelect', params)
      this.visible = false
    },
    showDrawer() {
      this.visible = true
    }
  }
}
</script>
