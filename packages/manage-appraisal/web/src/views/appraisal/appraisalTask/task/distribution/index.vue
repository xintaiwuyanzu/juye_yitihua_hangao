<template>
  <el-dialog title="任务领取"
             :visible.sync="show"
             :close-on-click-modal=false
             :close-on-press-escape=false
             width="20%">
    <form-render
        :model="form"
        ref="form"
        :fields="fields"
        label-width="120px"
    >
    </form-render>
    <div style="width: 100%;text-align: right">
      <el-button @click="show=false">取消</el-button>
      <el-button type="primary" @click="applyTask">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
const result = {'kz': '控制', 'kf': '开放'}
export default {
  data() {
    return {
      show: false,
      form: {},
      fields: {
        batchTaskQuantity: {
          label: '领取鉴定数量', required: true,
          fieldType: 'select',
          mapper: [{
            id: '5',
            label: '5份'
          }, {
            id: '100',
            label: '100份'
          }, {
            id: '200',
            label: '200份'
          }, {
            id: '500',
            label: '500份'
          }, {
            id: '0',
            label: '全部'
          }]
        },
        personId: {
          label: '鉴定操作人员', required: true,
          url: 'appraisalBatchPerson/page',
          params: {batchId: this.batchId, page: false},
          fieldType: 'select',
          labelKey: 'personName',
          valueKey: 'personId',
        },
        auxiliaryResult: {
          label: '机器鉴定结果',
          fieldType: 'select',
          mapper: []
        }
      },
      batch: {}
    }
  },
  props: {
    batchId: '',
  },
  methods: {
    $init() {
      this.$http.post("/appraisalBatch/detail", {id: this.batchId})
          .then(({data}) => {
            this.batch = data.data
            if (this.batch.appraisalType === 'KFJD') {
              this.loadAutype()
            } else {
              this.fields.auxiliaryResult.show = false
            }
          })
    },
    loadAutype() {
      this.$http.post("/appraisalOpenRange/page", {page: false})
          .then(({data}) => {
            this.fields.auxiliaryResult.mapper = []
            data.data.forEach(v => {
              this.fields.auxiliaryResult.mapper.push({id: v.id, label: result[v.auxiliaryResult] + '--' + v.openRange})
            })
            this.fields.auxiliaryResult.mapper.push({id: 'null', label: '无机器鉴定结果'})
          })
    },
    applyTask() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$http.post("/appraisalBatchTask/createAppraisalBatchTask", Object.assign({batchId: this.batchId}, this.form))
              .then(({data}) => {
                if (data.success) {
                  this.$message.success("领取成功")
                  this.show = false
                  this.$emit('reload')
                } else {
                  this.$message.error(data.message)
                }
              })
        }
      })
    }
  }
}
</script>
