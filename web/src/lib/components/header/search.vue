<template>
  <section>
    <el-popover placement="top" v-model="dialogShow" width="900">
      <form-render style="padding: 10px 20px" :fields="fields" :model="form" label-width="120px" ref="form"
                   v-loading="loading"
                   :style="'width:'+width" inline>
        <el-form-item label="">
          <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
          <el-button type="primary" @click="$refs.form.resetFields()" class="btn-cancel">重 置</el-button>
          <el-button type="primary" @click="loadData" v-loading="loading" class="btn-submit">搜 索</el-button>
        </el-form-item>
      </form-render>
      <el-button slot="reference" type="primary">查询</el-button>
    </el-popover>
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
      form: {},
      width: "600",
      fields: []
    }
  },
  watch: {
    formId() {
      this.loadListShowScheme()
    },
    dialogShow(v) {
      if (v && this.fields.length === 0) {
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
          this.fields = data.data.fields.map(item => {
            if (item.meta && item.meta.dict) {
              return Object.assign({}, {
                'prop': item.code,
                'label': item.name,
                'fieldType': 'select',
                'dictKey': item.meta.dict
              })
            } else {
              return Object.assign({}, {'prop': item.code, 'label': item.name})
            }
          })
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      }
    },
    async loadData() {
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
    this.eventBus.$emit('addQueryItem', this)
  }
}
</script>
