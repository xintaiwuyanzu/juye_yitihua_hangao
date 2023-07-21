<template>
  <table-index title="系统备份记录"
               :fields="fields"
               path="dataBase"
               :insert="false"
               :edit="false"
               :delete="hasRole(['DASJ_ADMIN','admin'])"
               ref="table">
    <template v-slot:search-$btns="scope">
      <el-button type="primary" @click="show">备份</el-button>
    </template>
    <template v-slot:table-$btns="{row}">
      <el-button type="text" @click="recover(row)" size="mini" width="60">{{ reportTitle }}</el-button>
    </template>
    <el-dialog
        title="备份"
        :visible.sync="dialogVisible"
        :close-on-click-modal="false"
        width="30%"
        v-loading="loading">
      <el-form :model="form" ref="form" label-width="110px">
        <el-form-item label="备份记录名:" prop="backUpName" required>
          <el-input v-model="form.backUpName" placeholder="请输入备份记录名称"></el-input>
        </el-form-item>
        <el-form-item label="备份文件位置:" prop="backUpName">
          <el-input v-model="form.fileLocation"
                    placeholder="请输入备份文件位置（不填写，默认备份在系统指定位置）"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="cancel" type="info">取 消</el-button>
        <el-button type="primary" @click="sure">确 定</el-button>
      </span>
    </el-dialog>
  </table-index>
</template>
<script>

/**
 * 离线接收
 * 批次详情列表
 */
export default {
  data() {
    return {
      dialogVisible: false,
      loading: false,
      reportTitle: '恢复',
      form: {
        backUpName: '',
        fileLocation: '',
      },
      //新格式
      fields: [
        {
          prop: 'backUpName',
          label: '记录名称',
          search: true
        },
        {
          prop: 'status', label: '状态', width: '100',
          component: 'tag',
          showTypeKey: 'show',
          mapper: {
            '0': {label: '备份中', show: 'warning'},
            '1': {label: '备份成功', show: 'success'},
            '2': {label: '备份失败', show: 'danger'},
            '3': {label: '恢复中', show: 'warning'},
            '4': {label: '恢复成功', show: 'success'},
            '5': {label: '恢复失败', show: 'danger'},
          }
        },
        {prop: 'personName', label: '备份人名称', search: true, width: 100},
        {prop: 'createDate', label: '备份时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: 140},
        {prop: 'clientIp', label: '客户端ip', width: 140},
        {prop: 'fileLocation', label: '备份文件位置', width: 140},
        {prop: 'recoverPersonName', label: '最后恢复人姓名', search: true, width: 100},
        {prop: 'fileName', label: '备份文件名称', search: true, width: 200},
      ],
    }
  },
  methods: {
    show() {
      this.dialogVisible = true
    },
    cancel() {
      this.form = {}
      this.dialogVisible = false
    },
    sure() {
      this.$refs.form.validate(v => {
        if (v) {
          this.loading = true
          this.$post('dataBase/backup', this.form).then(({data}) => {
            if (data && data.success) {
              this.$message({
                message: "正在备份，这个过程可能会有点长，请稍后刷新并查看备份记录！",
                type: "warning",
                duration: 0,
                showClose: true
              })
              this.form = {}
              this.dialogVisible = false
              this.$refs.table.reload()
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
        }
      })
    },
    recover(row) {
      this.$confirm('确定从此备份恢复吗?此操作不可逆，请谨慎选择！', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$post('dataBase/recover', row).then(({data}) => {
          if (data && data.success) {
            this.$message({
              message: "正在恢复，这个过程可能会有点长，请稍后刷新并根据状态判断是否已恢复完毕！",
              type: "warning",
              duration: 0,
              showClose: true
            })
            this.$refs.table.reload()
          } else {
            this.$message.error(data.message)
          }
        })
      })
    },
  },
}
</script>
