<template>
  <table-index :defaultSearchForm="defaultSearchForm" :delete="false" :edit="false" :fields="fields" :insert="false"
               back
               pagePath="realm_class/getFormData" path="realm_class" ref="table"
               title="对象数据"></table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      fields: [],
      defaultSearchForm: {formId: this.$route.query.formId},
      type: ''
    }
  },
  methods: {
    $init() {
      /*this.getClass()*/
      this.loadListShowScheme()
    },
    async getClass() {
      const {data} = await this.$post('realm_class/detail', {id: this.$route.query.id})
      if (data && data.success) {
        this.type = data.data.formType
      }
    },
    async loadListShowScheme() {
      if (this.$route.query.formId) {
        this.loading = true
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: this.$route.query.formId,
          schemeType: 'list',
          formCode: 'list'
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
  },
  /*watch: {
    type(val) {
      if (val === '人') {
        this.fields.push(
            {prop: 'NAME', label: '姓名'},
            {prop: 'COUNTRY', label: '国籍'},
            {prop: 'IDCARD_NO', label: '身份证号码'},
            {prop: 'NATION', label: '民族'},
            {prop: 'SEX', label: '性别'},
        )
      } else if (val === '事') {
        this.fields.push(
            {prop: 'NAME', label: '名称'}
        )
      } else if (val === '地') {
        this.fields.push(
            {prop: 'NAME', label: '名称'}
        )
      } else if (val === '物') {
        this.fields.push(
            {prop: 'NAME', label: '名称'}
        )
      } else if (val === '组织') {
        this.fields.push(
            {prop: 'NAME', label: '机构名称'},
            {prop: 'ORG_ADDR', label: '地址'},
            {prop: 'ORG_TIME', label: '成立时间'}
        )
      }
    }
  }*/
}
</script>

<style scoped>

</style>