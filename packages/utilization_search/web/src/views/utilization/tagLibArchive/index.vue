<template>
  <table-index :fields="fields" path="tagLibArchive"
               pagePath="tagLibArchive/selectTagLibCount"
               :defaultSearchForm="defaultSearchForm"
               :edit="false" :delete="false" :insert="false"
               ref="table">
    <template v-slot:table-$btns="{row}">
      <el-link type="primary" @click="detail(row)">详情</el-link>
    </template>
  </table-index>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  name: "index.vue",
  mixins: [indexMixin, formMixin],
  data() {
    return {
      fields: [
        {prop: 'tagName', label: '标签名', search: true},
        {prop: 'ctype', label: '类型', search: true,fieldType: 'select', mapper: {'gt': '固体标签', 'st': '实体标签', 'zdy': '自定义标签'}},
        {prop: 'stType', label: '实体分类',mapper:{'PER': '人名', 'LOC': '地名', 'ORG': '机构名', 'TIME': '时间'} },
        {prop: 'archiveCount', label: '应用档案数量'}
      ],
      defaultSearchForm: {tagName: '', typeName: ''},
    }
  },
  methods: {
    loadData() {
      this.$refs.table.loadData(this.defaultSearchForm);
    },
    detail(row){
      //跳转到明细页面
      this.$router.push({
        path: '/utilization/tagLibArchive/detail',
        query: {
          tagLibId: row.tagLibId
        }
      })
    },
  }
}
</script>
