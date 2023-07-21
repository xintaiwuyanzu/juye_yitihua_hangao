<template>
  <table-index path="/manage/handover/details/detailData"
               class="handOverDetails"
               :fields="fields"
               :showTitle="false"
               :default-search-form="defaultSearchForm"
               :edit="false"
               :delete="false"
               :insert="false">
    <el-table-column type="expand" label="详情">
      <template slot-scope="{row}">
        <div :key="tag.id" v-for="tag in row.voList">
          <el-descriptions :column="3" border size="medium">
            <el-descriptions-item label="档号">{{ tag.archiveCode }}</el-descriptions-item>
            <el-descriptions-item label="题名">
              <el-button type="text" @click="detailClick(tag)">{{ tag.title }}</el-button>
            </el-descriptions-item>
            <el-descriptions-item label="类型"> 卷内文件</el-descriptions-item>
          </el-descriptions>
        </div>

      </template>
    </el-table-column>
  </table-index>
</template>
<script>
/**
 * 移交档案目录列表
 */
export default {
  name: "detailList",
  props: {
    handoverId: {type: String}
  },
  data() {
    return {
      fields: {
        fondName: {label: '全宗', width: 120},
        archiveCode: {label: '档号', width: 250},
        // title: {label: '题名', component: 'text', routerPath: '/manage/handover/detail/archiveDetail', route: true},
        title: {
          label: '题名',
          component: 'text',
          routerPath: '/archive/metadataAndFileDetail',
          route: true,
          queryProp: this.routeQuery
        },
        year: {label: '年度', width: 80},
        detailType: {label: '类型', width: 120, mapper: {1: '案卷', 0: '文件', 4: '卷内文件'}},
      }
    }
  },
  methods: {
    defaultSearchForm() {
      return {handoverId: this.handoverId}
    },
    routeQuery(row) {
      return {formDefinitionId: row.formDefinitionId, formDataId: row.formDataId, refType: 'archive'}
    },
    detailClick(row) {
      this.$router.push({
        path: '/archive/metadataAndFileDetail',
        query: {formDefinitionId: row.formDefinitionId, formDataId: row.formDataId, refType: 'archive'}
      })
    },
  }
}
</script>