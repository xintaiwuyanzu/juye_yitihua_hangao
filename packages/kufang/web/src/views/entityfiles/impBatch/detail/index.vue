<template>
  <table-index :fields="fields"
               path="impEntityDetail/getDetail"
               :edit="false"
               :delete="false"
               :insert="false"
               back
               :defaultSearchForm="defaultSearchForm"
               title="导入记录详情"
               ref="table">
    <el-table-column type="expand" label="详情">
      <template slot-scope="{row}">
        <div :key="tag.id" v-for="tag in row.voList">
          <el-descriptions :column="3" border size="medium">
            <el-descriptions-item label="题名">{{ tag.title }}</el-descriptions-item>
            <el-descriptions-item label="档号">{{ tag.archiveCode }}</el-descriptions-item>
            <el-descriptions-item label="类型"> 卷内文件</el-descriptions-item>
          </el-descriptions>
        </div>

      </template>
    </el-table-column>

  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', search: true, align: 'center'},
        {prop: 'archiveCode', label: '档号', search: true, width: 300, align: 'center'},
        {prop: 'fondCode', label: '全宗号', width: 300, align: 'center'},
        {
          prop: 'archiveType', label: '类型', align: 'center', width: 200,
          mapper: {'wj': '文件', 'aj': '案卷', 'jn': '卷内文件'}
        },
        {
          prop: 'status', label: '状态', width: 80, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '失败', show: 'danger'},
            '1': {label: '成功', show: 'success'},
          },
        },
      ],
    }
  }
  ,
  methods: {
    defaultSearchForm() {
      return {batchId: this.$route.query.batchId, archiveType: this.$route.query.archiveType}
    }
  }
}
</script>