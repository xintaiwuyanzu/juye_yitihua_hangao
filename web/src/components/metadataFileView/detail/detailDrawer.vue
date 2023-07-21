<template>
  <el-drawer
      :beforeClose="beforeClose"
      :visible.sync="drawer"
      direction="rtl"
      title="基本信息元数据">
    <el-descriptions :column="1" border size="medium" style="margin: 10px">
      <el-descriptions-item :key="index" :label="i.label" v-for="(i,index) in fields">
        <span v-if="i.prop!=='FOND_CODE'">{{resultFormData[i.prop]}}</span>
        <span v-else>{{resultFormData.fondName}}</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-drawer>
</template>

<script>
  export default {
    name: "detailDrawer",
    props: {
      drawer: Boolean,
      formData: {type: Object, default: () => ({})}
    },
    data() {
      return {
        fields: [],
        resultFormData: this.formData
      }
    },
    methods: {
      $init() {
        this.loadShowScheme()
      },
      async loadShowScheme() {
        if (this.$route.query.formDefinitionId) {
          const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
            formDefinitionId: this.$route.query.formDefinitionId,
            schemeType: 'list',
            formCode: 'list'
          })
          console.log(data)
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
        }
      },
      async getFondNameByCode() {
        const {data} = await this.$post('/manage/formData/getFondByCode', {fondCode: this.formData.FOND_CODE})
        if (data && data.success) {
          this.resultFormData.fondName = data.data.name
        }
      },
      beforeClose() {
        this.$emit('cancel')
      },
      getFormDefinitionId(){
        this.loadShowScheme()
      },
    },
    watch: {
      'formData.id'(val) {
        this.resultFormData = this.formData
        if (val) {
          this.getFondNameByCode()
        }
      }
    }
  }
</script>

<style scoped>

</style>