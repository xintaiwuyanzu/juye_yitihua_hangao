<template>
  <table-index :fields="fields" path="zfjcCheck/" :pagePath="pagePath" :insert="false" :edit="false" :delete="false"
               ref="table"
               :dataWrapper="dataWrapper"
               :defaultSearchForm="defaultSearchForm">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="90" @click="openFile(scope.row)">检查报告</el-button>
      <el-button type="text" size="mini" width="90" @click="publish(scope.row,'1')"
                 v-show="scope.row.publish==='0'">公
        示
      </el-button>
      <el-button type="text" size="mini" width="90" @click="publish(scope.row,'0')"
                 v-show="scope.row.publish==='1'">
        撤销公示
      </el-button>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      dict: [],
      pagePath: '/zfjcCheck/myZfjcCheck',
      //新代码
      fields: [
        {prop: 'organiseName', label: '检查单位', search: false},
        {prop: 'categoryName', label: '执法检查内容', search: false},
        {
          prop: 'checkResult', label: '检查结果', width: "300", component: 'tag', showTypeKey: 'show', fieldType: 'select',
          mapper: {
            0: {label: '待检查', show: 'success'},
            1: {label: '合格', show: 'primary'},
            2: {label: '不合格', show: 'danger'},
          },
        },
        {
          prop: 'publish', label: '公开状态', fieldType: 'select', component: 'tag', mapper: {
            '1': {label: '公开', show: 'primary'},
            '0': {label: '不公开', show: 'success'}
          },
        },
        {prop: 'status', label: '当前状态', dictKey: 'zfjc.status', fieldType: 'select', component: 'tag', required: true},
      ],
      defaultSearchForm: {organiseId: '', checkType: 'zf'}
    }
  },
  methods: {
    dataWrapper(data){
      //TODO 数据值为数字0的时候，映射有问题
      data.forEach(v=>v.checkResult=''+v.checkResult)
    },
    openFile(row) {
      let url = "api/zfjcCheck/expgs?id=" + row.id
      window.open(url)
    },
    publish(row, v) {
      let des = ''
      if (v === '1') {
        des = '公开'
      } else {
        des = '撤销公开'
      }
      this.$confirm('此操作将' + des + '该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {

        row.publish = v
        this.$http.post('zfjcCheck/update', row).then(({data}) => {
          if (data && data.success) {
            this.$message.success('操作成功！')
          } else {
            this.$message.error(data.message)
          }
          this.$refs.table.loadData(this.defaultSearchForm);
          this.loading = false
        })
      })

    }
  }
}
</script>
