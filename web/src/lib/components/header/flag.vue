<template>
  <section>
    <el-button v-show="show" type="primary" @click="showDialog">立卷</el-button>
  </section>
</template>
<script>
import abstractColumnComponent from "../abstractColumnComponent";

/**
 * 头部按钮
 * 立卷按钮
 */
export default {
  extends: abstractColumnComponent,
  name: 'flag',
  computed: {
    //按钮是否显示
    show() {
      //选择两条数据，并且有父的列表页面才行
      return this.currentSelect.length > 1 && this.parentIndex
    }
  },
  data() {
    return {selectData: {}}
  },
  methods: {
    //显示弹出框
    showDialog() {
      if (this.currentSelect.length < 2) {
        this.$message.error("请至少选择2条文件进行立卷！")
        return
      }
      this.selectData.formDefinitionId = this.parentIndex.formId
      this.selectData.FOND_CODE = this.fond.code
      this.selectData.CATE_GORY_CODE = this.parentIndex.category.code
      this.parentIndex.$emit('edit', this.selectData)
    }
  },
}
</script>
