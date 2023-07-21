<template>
  <el-dialog title="创建鉴定批次"
             :visible.sync="show"
             :close-on-click-modal=false
             :close-on-press-escape=false
             width="40%">
    <div style="margin-top: 10px">
      <form-render label-width="110px" :rules="rules" :fields="fields" ref="form" :model="form">
        <el-form-item label="鉴定全宗" prop="fondCodes" required>
          <el-select filterable v-model="form.fondCodes" multiple placeholder="请选择全宗">
            <el-option v-for="item in fondAndCodes" :key="item.id"
                       :label="`${item.data.code}  ${item.label}`" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
      </form-render>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button type="info" @click="cancelDialog">取 消</el-button>
      <el-button type="primary" @click="saveForm">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>

export default {
  data() {
    return {
      show: false,
      form: {},
      fondCodes: [],
      fondAndCodes: [],
      rules: {
        startVintages: [
          {pattern: /^\d{1,4}$/, trigger: 'blur', message: "请输入正确的开始年度"},
        ],
        endVintages: [
          {pattern: /^\d{1,4}$/, trigger: 'blur', message: "请输入正确的结束年度"},
        ],
      },
      fields: {
        batchName: {label: '批次名称', required: true},
        appraisalType: {
          label: '鉴定类型',
          fieldType: 'select',
          required: true,
          mapper: [{
            id: 'KFJD',
            label: '开放鉴定'
          }, {
            id: 'DQJD',
            label: '销毁鉴定'
          }]
        },
        fondCodes: {
          required: true,
          label: '鉴定全宗',
          fieldType: 'select',
          multiple: true,
          filterable: true,
          url: 'sysResource/personResource',
          params: {type: 'fond'}
        },
        startVintages: {
          label: '开始年度',
          required: true
        },
        endVintages: {
          label: '结束年度',
          required: true
        },
        batchRemarks: {label: '批次备注'},
      }
    }
  },
  methods: {
    $init() {
      this.fondCodeQuery()
    },
    cancelDialog() {
      this.form = {}
      this.show = false
    },
    fondCodeQuery() {
      this.$http.post('sysResource/personResource', {type: 'fond'}).then(({data}) => {
        this.fondAndCodes = data.data
      })
    },
    saveForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {

          let fondArray = this.form.fondCodes.toString()
          this.form.fondCodes = fondArray
          this.$emit('add', this.form)
          this.form.fondCodes = []
        }
      })
    }
  }
}
</script>
