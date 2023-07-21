<template>
  <table-index :fields="fields" path="formTagConfigure"
               :defaultSearchForm="defaultSearchForm"
               ref="table">
    <template v-slot:edit="form">
      <el-form-item prop="formDefinitionId" label="表单" required>
        <select-async clearable
            placeholder="请选择表单"
            v-model="form.formDefinitionId"
            url="/manage/form/findFormList"
            valueKey = 'id'
            labelKey = 'formName'
                      v-on:change="formDchange"
            />
      </el-form-item>
      <el-form-item prop="fieldCode" label="字段"  required>
        <select-async clearable
            placeholder="请选择字段"
            v-model = "form.fieldCode"
            valueKey = 'fieldCode'
            labelKey = 'label'
            :mapper = "fieldMapper"
        />
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
        {prop: 'formDefinitionName', label: '表单名称', edit: false},
        {prop: 'formDefinitionCode', label: '表单编码', search: true, edit: false},
        {prop: 'fieldCode', label: '字段编码', search: true, edit: false},
        {prop: 'label', label: '字段中文名称', edit: false},
        {prop: 'stType', label: '标签实体类型', fieldType: 'select',mapper:{'PER': '人名', 'LOC': '地名', 'ORG': '机构名', 'TIME': '时间'},required: true},
        {prop: 'createPerson', label: '创建人', edit: false},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false}
      ],
      defaultSearchForm: {formDefinitionCode: '', fieldCode: ''},
      fieldMapper: {}
    }
  },
  methods: {
    formDchange(v){
      this.fieldMapper = {}
      this.$http.post('/manage/form/findFieldList', {formDefinitionId: v})
          .then(({data}) => {
            if (data.success) {
              this.fieldMapper = data.data
            } else {
              this.$message.error(data.message)
            }
          })

    }
  }
}
</script>

<style scoped>

</style>