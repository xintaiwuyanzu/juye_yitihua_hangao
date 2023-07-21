<template>
  <section>
    <el-button type="primary" @click="loadData" v-show="parentIndex" v-loading="loading">刷 新
    </el-button>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

/**
 * 头部搜索按钮
 */
export default {
  extends: abstractComponent,
  name: "search",
  data() {
    return {
      listFields: [],
      form: {},
      width: "600"
    }
  },
  watch: {
    formId() {
      this.loadListShowScheme()
    },
    dialogShow(v) {
      if (v && this.listFields.length === 0) {
        this.loadListShowScheme()
      }
    }
  },
  methods: {
    async loadListShowScheme() {
      if (this.formId) {
        this.loading = true
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: this.formId,
          schemeType: 'search',
          formCode: 'search'
        })
        if (data.success) {
          this.listFields = data.data.fields
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      }
    },
    async loadData() {
      this.parentIndex.currentRow = ''
      this.eventBus.$emit("loadData")
      this.dialogShow = false
    },
    getQuery() {
      return Object.entries(this.form)
          .filter(e => !!e[1])
          .map(([key, value]) => ({key, value, type: 'l'}))
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.width = this.eventBus.$el.offsetWidth
    })
    this.eventBus.$emit('addQueryItem', this)
  }
}
</script>
