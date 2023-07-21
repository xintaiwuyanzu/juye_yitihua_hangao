<template>
  <table-index :fields="fields" path="specialTag"
               :defaultSearchForm="defaultSearchForm"
               ref="table" @editShow="editShow">
    <template v-slot:edit="form">
      <el-form-item prop="parentId" label="上一级">
        <select-async
            v-model="form.parentId"
            :mapper="parentMapper"
            valueKey='id'
            labelKey='tagName'/>
      </el-form-item>
      <el-form-item prop="leaf" label="是否有下一级">
        <el-radio-group v-model="form.leaf">
          <el-radio label="0">是</el-radio>
          <el-radio label="1">否</el-radio>
        </el-radio-group>
      </el-form-item>
    </template>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [
        {prop: 'tagName', label: '标签名', search: true, required: true},
        {prop: 'tagDescribe', label: '描述', type: 'textarea'},
        {prop: 'createPerson', label: '创建人', edit: false},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false},
        {prop: 'updatePerson', label: '修改人', edit: false},
        {prop: 'updateDate', label: '修改时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false}
      ],
      defaultSearchForm: {tagName: ''},
      parentMapper: {}
    }
  },
  methods: {
    editShow(form) {  //多选下拉框回显
      this.getParentMapper()
    },
    getParentMapper() {
      this.$http.post('/specialTag/allParent')
          .then(({data}) => {
            if (data.success) {
              this.parentMapper = data.data
            } else {
              this.$message.error(data.message)
            }
          })
    }
  }
}
</script>

<style lang="scss">
.sysMenuTree {
  height: auto !important;
  overflow: auto;

  .el-tree-node__content {
    height: auto;

    .buttons {
      float: right;
    }
  }
}
</style>