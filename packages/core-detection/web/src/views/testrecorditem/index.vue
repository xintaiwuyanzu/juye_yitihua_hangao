<template>
  <section>
    <nac-info :back="true"/>
    <div class="index_main">
      <table-render class="tableWordWrap"
                    :index="true"
                    :columns="columns"
                    :data="data1"
                    :span-method="objectSpanMethod"
                    showPage/>
    </div>
  </section>
</template>
<script>
export default {
  name: "index",
  data() {
    return {
      columns: [
        {prop: 'testRecordTargetType', label: '检测类型', width: 80},
        {prop: 'testRecordItems', label: '检测小项', width: 120, align: 'left'},
        {prop: 'testRecordSubstance', label: '标准要求', align: 'left', width: 200},
        {prop: 'testRecordMethod', label: '实现方式', align: 'left'},
        {
          prop: 'status', label: '检测结果', width: '80', align: 'left', component: 'tag',
          showTypeKey: 'show',
          mapper: {
            '1': {label: '通过', show: 'success'},
            '0': {label: '不通过', show: 'danger'}
          }
        },
        {prop: 'testResult', label: '检测结果说明', align: 'left'},
        {prop: 'targetCode', label: '检测对象编码'},
        {prop: 'targetName', label: '检测对象名称'},
        {prop: 'targetValue', label: '检测对象值'},
        {prop: 'createDate', label: '检测时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
      ],
      spandata1: [],
      spandata2: [],
      spandata3: [],
      spandata4: [],
      data1: [],
    }
  },
  methods: {
    async $init() {
      this.$http.post('/testRecordItem/testReport', {
        batchId: this.batchId,
        formDataId: this.formDataId,
        recordId: this.recordId
      }).then(({data}) => {
        if (data && data.success) {
          this.data1 = data.data
          this.getRowSpans()
        }
      })
    },
    getRowSpans() {
      this.spandata1 = []
      this.spandata2 = []
      this.spandata3 = []
      this.spandata4 = []
      let tabData = this.data1
      let pos1 = 0
      let pos2 = 0
      let pos3 = 0
      let pos4 = 0

      for (var i = 0; i < tabData.length; i++) {
        if (i === 0) {
          this.spandata1.push(1)
          this.spandata2.push(1)
          this.spandata3.push(1)
          this.spandata4.push(1)
        } else {
          if (tabData[i].testRecordTargetType == tabData[i - 1].testRecordTargetType) {
            // 如果testRecordTargetType 相等就累加，并且push 0
            this.spandata1[pos1] += 1
            this.spandata1.push(0)
          } else {
            // 不相等push 1,并且可修改下标指向
            this.spandata1.push(1)
            pos1 = i
          }

          if (this.data1[i].testRecordItems === this.data1[i - 1].testRecordItems) {
            // 如果testRecordItems 相等就累加，并且push 0
            this.spandata2[pos2] += 1
            this.spandata2.push(0)
          } else {
            // 不相等push 1,并且可修改下标指向
            this.spandata2.push(1)
            pos2 = i
          }

          if (this.data1[i].testRecordSubstance === this.data1[i - 1].testRecordSubstance) {
            // 如果testRecordSubstance 相等就累加，并且push 0
            this.spandata3[pos3] += 1
            this.spandata3.push(0)
          } else {
            // 不相等push 1,并且可修改下标指向
            this.spandata3.push(1)
            pos3 = i
          }

          if (this.data1[i].testRecordMethod === this.data1[i - 1].testRecordMethod) {
            this.spandata4[pos4] += 1
            this.spandata4.push(0)
          } else {
            this.spandata4.push(1)
            pos4 = i
          }
        }

      }
    },
    objectSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex == 0) {
        const _row = this.spandata1[rowIndex]
        const _col = _row > 0 ? 1 : 0
        return {
          rowspan: _row,
          colspan: _col
        }
      }
      // 合并相同列数据的行
      if (columnIndex == 1) {
        const _row = this.spandata2[rowIndex]
        const _col = _row > 0 ? 1 : 0
        return {
          rowspan: _row,
          colspan: _col
        }
      }
      // 合并相同列数据的行
      if (columnIndex == 2) {
        const _row = this.spandata3[rowIndex]
        const _col = _row > 0 ? 1 : 0
        return {
          rowspan: _row,
          colspan: _col
        }
      }
      // 合并相同列数据的行
      if (columnIndex == 3) {
        const _row = this.spandata4[rowIndex]
        const _col = _row > 0 ? 1 : 0
        return {
          rowspan: _row,
          colspan: _col
        }
      }
    },
  },
  computed: {
    batchId() {
      return this.$route.query.batchId
    },
    formDataId() {
      return this.$route.query.formDataId
    },
    recordId() {
      return this.$route.query.recordId
    }
  }
}
</script>

<style lang="scss">
.tableWordWrap .el-table .cell {
  //自动换行
  /*white-space: normal;
  word-break: break-all;*/
}
</style>