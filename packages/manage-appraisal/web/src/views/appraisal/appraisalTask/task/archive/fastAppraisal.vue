<template>
  <el-dialog title="一键审核结果" :visible.sync="show" width="40%">
    <form-render label-width="110px" :fields="fields" ref="form" :model="form">
      <el-form-item>
        <span style="color: red" slot="label">说&#12288;明：</span>
        <span style="color: red">此操作将未鉴定档案将采用选择的鉴定结果 </span>
      </el-form-item>
    </form-render>
    <div slot="footer" class="dialog-footer" style="text-align: right">
      <el-button type="primary" @click="saveForm">保 存</el-button>
    </div>
  </el-dialog>
</template>

<script>
const result = {'kz': '控制', 'kf': '开放'}
export default {
  name: "fastAppraisal",
  data() {
    return {
      form: {},
      show: false,
      appraisalType: '',
      fields: {
        auxiliaryResult: {
          label: '快速鉴定结果', search: true, fieldType: 'select',
          mapper: [],
          required: true,
          on: {change: this.personResultChange}
        },
        delayYear: {
          label: '延长年限',
          required: true,
          type: 'number',
          show: false
        }
      }
    }
  },
  watch: {
    appraisalType() {
      if (this.appraisalType === 'KFJD') {
        this.$http.post("/appraisalOpenRange/page", {page: false})
            .then(({data}) => {
              this.fields.auxiliaryResult.mapper = []
              data.data.forEach(v => {
                this.fields.auxiliaryResult.mapper.push({
                  id: v.id,
                  label: result[v.auxiliaryResult] + "--" + v.openRange
                })
              })
              this.fields.auxiliaryResult.mapper.push({id: 'machine', label: '以机器鉴定结果为准'})
            })
      } else {
        this.fields.auxiliaryResult.mapper = [{id: 'xh', label: '销毁'}, {id: 'yq', label: '延期'}]
      }
    }
  },
  methods: {
    saveForm() {
      //this.$emit('saveForm', {auxiliaryResult: this.form.auxiliaryResult})
      this.$emit('saveForm', this.form)
      this.show = false
    },
    personResultChange() {
      if (this.form.auxiliaryResult == 'yq') {
        this.fields.delayYear.show = true
      } else {
        this.fields.delayYear.show = false
      }
    }
  }
}
</script>

<style scoped>

</style>
