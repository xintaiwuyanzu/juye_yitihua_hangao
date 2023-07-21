<template>
  <section>
    <el-button v-show="show" type="warning" @click="showReceive" v-loading="loading">拆卷</el-button>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

/**
 * 头部按钮
 * 拆卷按钮
 */
export default {
  extends: abstractComponent,
  name: 'disassemble',
  data() {
    return {
      selectData: {},
      loading: false,
    }
  },
  computed: {
    show() {
      return this.childrenIndex && this.currentSelect.length > 0
    }
  },
  methods: {
    //显示弹出框
    showReceive() {
      this.loading = true
      if (this.currentSelect.length === 0) {
        this.$message.error("请至少选择1条文件进行拆卷！")
        return
      }
      let ids = ''
      this.currentSelect.forEach(item => {
        ids += item.id + ','
      })
      this.$http.post('/manage/formData/disassemble', {
        ajFormDefinitionId: this.formId,
        wjFormDefinitionId: this.childrenIndex.formId,
        ids: ids,
        fondId: this.childrenIndex.fond.id,
        categoryId: this.childrenIndex.category.id
      }).then(({data}) => {
        if (data.success) {
          this.$message.success('拆卷成功')
          this.loading = false
        }
      })
      this.loading = false
    }
  }
}
</script>
