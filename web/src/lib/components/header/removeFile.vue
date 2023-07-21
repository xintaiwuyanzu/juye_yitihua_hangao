<template>
  <section>
    <el-button v-show="show" type="warning" @click="removeFile">移卷</el-button>
  </section>
</template>
<script>
  import abstractColumnComponent from "../abstractColumnComponent";

  /**
 * 头部按钮
 * 移卷按钮
 */
export default {
  extends: abstractColumnComponent,
  name: 'removeFile',
  computed: {
    //按钮是否显示
    show() {
      //选择一条数据，并且有父的列表页面才行
      return this.currentSelect.length > 0 && this.parentIndex
    }
  },
  data() {
    return {
      selectData: {},
    }
  },
  methods: {
    removeFile() {
      this.selectData.ids = ''
      this.currentSelect.forEach(item => {
        this.selectData.ids += item.id + ','
      })
      this.$http.post('/manage/formData/removeFile', {
        formDefinitionId: this.formId,
        ids: this.selectData.ids,
        fondId: this.fond.id,
        categoryId: this.category.id
      }).then(({data}) => {
        if (data.success) {
          this.eventBus.$emit("loadData")
          this.$message.success('移卷成功')
          this.dialogVisible = false
        } else {
          this.$message.error(data.message)
        }
      })
    },
  },
}
</script>
