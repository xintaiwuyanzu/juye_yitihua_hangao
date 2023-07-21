<template>
  <section>
    <el-button v-show="show" type="warning" @click="showDialog">组卷</el-button>
  </section>
</template>
<script>
  import abstractColumnComponent from "../abstractColumnComponent";

  /**
 * 头部按钮
 * 组卷按钮
 */
export default {
  extends: abstractColumnComponent,
  name: 'group',
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
    async showDialog() {
      if (this.currentSelect.length < 2) {
        this.$message.error("请至少选择2条文件进行组卷！")
        return
      }
      //检查是否有已组卷的档案
      let flag = this.currentSelect.some(item => {
        if (item.AJDH) {
          return true
        }
      })
      if (flag) {
        this.$message.warning("所选档案文件中含有已组卷的档案，请重新选择")
        return;
      }
      this.selectData.ids = ''
      this.currentSelect.forEach(item => {
        this.selectData.ids += item.id + ','
      })
      this.selectData.ajFormDefinitionId = this.parentIndex.formId
      this.selectData.wjFormDefinitionId = this.formId
      this.selectData.FOND_CODE = this.fond.code
      this.selectData.fondId = this.fond.id
      this.selectData.categoryId = this.category.id
      this.selectData.CATE_GORY_CODE = this.parentIndex.category.code
      this.selectData.formCode = this.parentIndex.formCode
      await this.parentIndex.$emit('group', this.selectData)
      this.eventBus.$emit("loadData")
    }
  },
}
</script>
