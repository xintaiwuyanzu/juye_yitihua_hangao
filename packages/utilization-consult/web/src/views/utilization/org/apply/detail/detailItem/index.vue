<template>
  <metadata-file-view :title="`档案详情(${page.index}/${page.total})`"
                      v-loading="loading"
                      refType="archive"
                      :formDefinitionId="detail.formDefinitionId"
                      :form-data="formData">
    <el-button type="primary" @click="loadNext(true)">上一个</el-button>
    <el-button type="primary" @click="loadNext(false)">下一个</el-button>
    <el-button type="primary">打印原文</el-button>
  </metadata-file-view>
</template>
<script>

/**
 *查档审核详情
 */
export default {
  data() {
    return {
      loading: false,
      //表单数据
      formData: {},
      /**
       * 分页数据
       */
      page: {
        index: 0,
        total: 0
      },
      //当前查档
      detail: {},
    }
  },

  computed: {
    batchId() {
      return this.$route.query.id || this.$route.query.batchId
    }
  },
  methods: {
    /**
     *加载下一个详情数据
     * @param isPre
     * @returns {Promise<void>}
     */
    async loadNext(isPre = false) {
      let pageIndex = this.page.index - 1
      pageIndex = isPre ? pageIndex - 1 : pageIndex + 1
      if (pageIndex < 0 && isPre) {
        this.$message.warning("已经是第一个了")
        return
      }

      if (pageIndex === this.page.total && !isPre) {
        this.$message.warning("已经是最后一个了")
        return
      }
      if (pageIndex < 0) {
        pageIndex = 0
      }
      this.loading = true
      let {data} = await this.$post('/utilization/consult/details/page', {
        pageIndex,
        pageSize: 1,
        batchId: this.batchId
      })
      data = data.data
      this.page.index = data.start + 1
      this.page.total = data.total
      this.detail = data.data[0]
      await this.loadFormData()
      this.loading = false
    },
    /**
     * 查询查询查档详情
     * @param id
     * @returns {Promise<void>}
     */
    async loadDetail(id) {
      this.loading = true
      const {data} = await this.$post('/utilization/consult/details/detail', {id})
      this.detail = data.data
      await this.loadFormData()
      this.loading = false
    },
    async loadFormData() {
      const formData = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.detail.formDefinitionId,
        formDataId: this.detail.formDataId
      })
      this.formData = formData.data.data
    },
    async $init() {
      const query = this.$route.query
      this.page.index = parseInt(query.index) + 1
      this.page.total = parseInt(query.total)
      await this.loadDetail(query.detailId)
    }
  }
}
</script>