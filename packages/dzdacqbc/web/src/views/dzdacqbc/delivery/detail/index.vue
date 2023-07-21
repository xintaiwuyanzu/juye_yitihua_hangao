<template>
  <table-index :fields="fields" path="earchive/batchDetail/getDetail" :insert="false" :edit="false" :delete="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm" back title="档案出入库记录详情">
    <template v-slot:search-$btns>
      <el-button type="primary" size="mini" width="60"
                 v-if="$route.query.batchType=='out' && $route.query.deliveryStatus === '0'" @click="addDownLoad">
        提交出库
      </el-button>
    </template>
    <template v-slot:table-$btns="scop" v-if="$route.query.batchType=='out'">
      <el-button type="text" size="mini" width="60" @click="details(scop.row)"
                 v-if="$route.query.deliveryStatus === '0'">移除
      </el-button>
    </template>
    <el-table-column type="expand" label="详情">
      <template slot-scope="{row}">
        <div :key="tag.id" v-for="tag in row.voList">
          <el-descriptions :column="4" border size="medium">
            <el-descriptions-item label="档号">{{ tag.archiveCode }}</el-descriptions-item>
            <el-descriptions-item label="题名">
              <el-button type="text" @click="detailClick(tag)">{{ tag.title }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="年度">{{ tag.year }}</el-descriptions-item>
            <el-descriptions-item label="类型"> 卷内文件</el-descriptions-item>
          </el-descriptions>
        </div>

      </template>
    </el-table-column>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      //新代码
      fields: [
        {prop: 'fondName', label: '全宗', width: "140"},
        {prop: 'categoryName', label: '门类', width: "80"},
        {prop: 'archiveCode', label: '档号', width: "250", search: true},
        {
          prop: 'title', label: '题名', align: 'left', component: 'text',
          route: true,
          routerPath: '/dzdacqbc/archive/deliverydetail',
          queryProp: this.routeQuery
        },
        {prop: 'year', label: '年度', width: 80},
        {
          prop: 'modelType', label: '类型', width: 150,
          mapper: {'1': '案卷', '0': '文件', '4': '卷内文件'}
        }
      ]
    }
  },
  methods: {
    detailClick(row) {
      this.$router.push({
        path: '/dzdacqbc/archive/deliverydetail',
        query: {batchId: row.id, formDefinitionId: row.formDefinitionId, formDataId: row.formDataId}
      })
    },
    defaultSearchForm() {
      return {batchId: this.$route.query.batchId}
    },
    routeQuery(row) {
      return {batchId: row.id, formDefinitionId: row.formDefinitionId, formDataId: row.formDataId}
    },
    //提交到数据维护客户端
    addDownLoad() {
      this.$http.post('/earchive/batch/addDownLoad', {batchId: this.$route.query.batchId}).then(({data}) => {
        if (data.success) {
          this.$message.success(data.message)
          this.$router.back();
        } else {
          this.$message.error(data.message)
        }
      })
    },
    async details(row) {
      this.loading = true
      await this.$http.post('/earchive/batchDetail/deleteById', {
        id: row.id,
        batchId: row.batchId
      }).then(({data}) => {
        if (data.success) {
          this.$http.post('/earchive/batch/updateById', {id: row.batchId}).then(({d}) => {
            if (data.success) {
              this.$message.success(data.message)
              this.$refs.table.reload();
              this.loading = false
            }
          })
        } else {
          this.$message.error(data.message)
        }
      })
    }
  }
}
</script>
