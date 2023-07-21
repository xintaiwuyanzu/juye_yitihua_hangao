<template>
  <section>
    <el-form>
      <el-form-item label-width="80px" label="鉴定状态">
        <span v-if="form.status=='1'" style="color:#67C23A;">已鉴定</span>
        <span v-else style="color:#F56C6C; ">未鉴定</span>
      </el-form-item>
      <el-form-item label-width="135px" label="是否提醒下一环节">
        <span v-if="form.sign=='1'" style="color:#67C23A;">是</span>
        <span v-else style="color:#F56C6C; ">否</span>
      </el-form-item>
      <el-form-item v-if="form.appraisalType=='KFJD'" label-width="80px" label="辅助结果">
        {{ form.auxiliaryResult == 'null' ? '无机器鉴定结果' : transResult() }}
      </el-form-item>
    </el-form>
    <form-render label-width="80px" :fields="fields" ref="form" :model="form"/>
    <div slot="footer" class="dialog-footer" style="text-align: right">
      <el-button type="primary" @click="saveForm">保存</el-button>
      <el-button type="primary" @click="saveForm('next')">保存并下一份</el-button>
    </div>
  </section>
</template>
<script>

const result = {'kz': '控制', 'kf': '开放'}
export default {
  data() {
    return {
      form: {},
      fields: {
        personResult: {
          label: '确认结果',
          fieldType: 'select',
          mapper: [{id: 'xh', label: '销毁'}, {id: 'yq', label: '延期'}],
          required: true,
          on: {change: this.personResultChange}
        },
        delayYear: {
          label: '延长年限',
          required: true,
          type: 'number',
          show: false
        },
        nextSign: {
          label: '是否提醒下一环节',
          fieldType: 'radio',
          labelWidth: '135px',
          mapper: [{id: '0', label: '否'}, {id: '1', label: '是'}],
          required: true,
          on: {change: this.nextSignChange}
        },
        personAppraisalBasis: {
          label: '鉴定依据', fieldType: 'select',
          url: '/appraisalBasis/page',
          params: {page: false},
          labelKey: 'description',
          show: false
        },
        personAppraisalKeyWord: {label: '关键词', show: false},
        remarks: {label: '说明', show: false},
      },
    }
  },
  props: {
    formDefinitionId: {type: String},
    formDataId: {type: String}
  },
  methods: {
    transResult() {
      let one = this.fields.personResult.mapper.find(v => v.id === this.form.auxiliaryResult)
      if (one) {
        return one.label
      }
    },
    async $init() {
      const {data} = await this.$http.post("/archiveAppraisalTask/getAppraisalOne", {
        formDefinitionId: this.formDefinitionId,
        formDataId: this.formDataId,
        taskId: sessionStorage.getItem('taskId')
      })
      if (data.success && data.data) {
        this.form = data.data
        if (!this.form.nextSign) {
          this.form.nextSign = '0'
        }
        if (this.form.appraisalType === 'KFJD' && this.form.personResult == null) {
          this.form.personResult = this.form.auxiliaryResult
        }
        await this.loadResult()
      }
    },
    async loadResult() {
      this.nextSignChange()
      if (this.form && 'KFJD' === this.form.appraisalType) {
        const {data} = await this.$http.post("/appraisalOpenRange/page", {page: false})
        if (data.success) {
          this.fields.personResult.mapper = []
          data.data.forEach(v => {
            this.fields.personResult.mapper.push({id: v.id, label: result[v.auxiliaryResult] + '--' + v.openRange})
          })
        }
      } else {
        this.personResultChange()
        this.fields.personResult.mapper = [{id: 'xh', label: '销毁'}, {id: 'yq', label: '延期'}]
        this.fields.personResult.label = '鉴定结果'
      }
    },
    saveForm(type) {
      this.$refs.form.validate((valid) => {
        if (valid) {
          this.form.status = 1
          this.$http.post("/archiveAppraisalTask/update", this.form)
              .then(({data}) => {
                if (data.success) {
                  if ('next' == type) {
                    this.$emit("next")
                  } else {
                    this.$emit("reload")
                  }
                }
              })
        }
      })
    },
    personResultChange() {
      if (this.form.personResult == 'yq') {
        this.fields.delayYear.show = true
      } else {
        this.fields.delayYear.show = false
      }
    },
    nextSignChange() {
      if (this.form.nextSign == '1') {
        this.fields.remarks.show = true
      } else {
        this.fields.remarks.show = false
      }
    }
  }
}
</script>

