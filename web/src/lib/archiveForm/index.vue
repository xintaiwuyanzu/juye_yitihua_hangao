<template>
  <el-form ref="form" :model="form" :label-width="labelWidth" inline v-if="listFields.length>0">
    <!--      <el-col :span="6">
            <el-form-item label="门类"  label-width="100px">
              <el-select v-model="code" placeholder="请选择门类" @blur="computeArchiveCode">
                <el-option v-for="item in codes"
                           :key="item.id"
                           :label="item.text"
                           :value="item.id">
                  <span style="float: left">{{ item.text }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ item.id }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>-->

    <el-form-item :prop='field.code' :label='field.name' label-width="100px" v-for="field in listFields"
                  :key="field.id">
      <select-dict v-model="form[field.code]" :type='field.meta.dict' clearable v-if="field.meta&&field.meta.dict"
                   @select="computeArchiveCode" :style="{width: field.remarks+'px'}"/>
      <el-input v-model="form[field.code]" :style="{width: field.remarks+'px'}" v-else @blur="computeArchiveCode"/>
    </el-form-item>
  </el-form>
</template>
<script>
/**
 * 档案编辑表单页面
 */
export default {
  props: {
    formDefinitionId: {type: String},
    form: {type: Object}
  },
  data() {
    return {
      labelWidth: '100',
      required: false,
      listFields: [],
      codingItems: [],
      code: 'WS',
      //机关档案分类编码
      codes: [
        {id: 'WS', text: '文书档案'},
        {id: 'ZP', text: '照片档案'},
        {id: 'SW', text: '实物档案'},
        {id: 'KJ', text: '科技档案'},
        {id: 'RS', text: '人事档案'},
        {id: 'KU', text: '会计档案'},
        {id: 'ZY', text: '专业档案'},
        {id: 'LY', text: '录音档案'},
        {id: 'LX', text: '录像档案'},
        {id: 'SJ', text: '业务数据档案'},
        {id: 'YJ', text: '公务电子右键档案'},
        {id: 'WY', text: '网页信息档案'},
        {id: 'MT', text: '社交媒体档案'},
      ]
    }
  },
  watch: {
    formDefinitionId() {
      this.loadFormShowScheme()
    }
  },
  methods: {
    async loadFormShowScheme() {
      if (this.formDefinitionId) {
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: this.formDefinitionId,
          schemeType: 'form',
          formCode: 'form'
        })
        if (data.success) {
          this.labelWidth = `${data.data.labelWidth}`
          this.listFields = data.data.fields
          this.listFields.forEach(f => {
            if (!this.form[f.code]) {
              this.$set(this.form, f.code, '')
            }
          })
          this.computeArchiveCode()
        } else {
          this.$message.error(data.message)
        }
      }
    },
    validate() {
      return this.$refs.form.validate()
    },
    //TODO 动态计算档号，这里先写死，将来能够动态配置的
    computeArchiveCode() {
      if (!this.form.id) {
        this.$nextTick(() => {
          /* const codeArr = [
             //全宗号
             this.form.FOND_CODE,
             //门类
             this.code,
             //保管期限
             this.form.SAVE_TERM,
             //目录号
             this.form.CATALOGUE_CODE,
             //案卷号
             this.form.AJH,
             //件号
             this.form.JH,
           ]*/
          // this.form.ARCHIVE_CODE = codeArr.filter(v => !!v).join('-')
        })
      }
    },
    $init() {
      this.loadFormShowScheme()
    }
  }
}
</script>
