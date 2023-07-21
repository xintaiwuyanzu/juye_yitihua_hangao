<template>
  <table-index :fields="fields" path="dataBatch" :insert="false" :edit="false" :delete="false">
    <template v-slot:table-$btns="scope">
      <el-button type="text"
                 @click="hook(scope.row.id)"
                 size="mini" width="45">挂接
      </el-button>
    </template>
    <!--  挂接弹窗  -->
    <el-dialog :visible.sync="hooktDialog.reportDialogVisible" title="挂接" width="80%">
      <section class="archiveLibIndex" style="height: 500px">
        <el-card class="fondTree" shadow="hover">
          <fond-tree :showHide='true' @check="changeFond" ref="fondTree"/>
        </el-card>
        <el-card class="tables">
          <el-form :model="form" ref="form" label-width="80px">
          </el-form>
        </el-card>
      </section>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hooktDialog.reportDialogVisible=false">取消</el-button>
      </div>
    </el-dialog>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    const statusMapper = {
      '0': {
        label: '未上传',
        show: 'info'
      },
      '1': {
        label: '正在上传',
        show: ''
      },
      '2': {
        label: '上传成功',
        show: 'success'
      },
      '3': {
        label: '上传失败',
        show: 'danger'
      },
      '4': {
        label: '校验文件',
        show: 'warning'
      }
    }
    return {
      fields: [
        {
          prop: 'batchName', label: '记录批次', required: true, search: true, width: 160,
          component: 'text',
          route: true,
          routerPath: 'client/detail',
        },
        {prop: 'clientPath', label: '客户端文件夹', required: true, search: true, align: 'left'},
        {prop: 'createDate', label: '开始时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'updateDate', label: '结束时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {
          prop: 'status',
          label: '状态',
          required: true,
          mapper: statusMapper,
          showTypeKey: 'show',
          component: 'tag',
          width: 90
        },
      ],
      hooktDialog: {
        reportDialogVisible: false
      },
      form: {},
    }
  },
  methods: {
    async $init() {
    },
    hook() {
      this.hooktDialog.reportDialogVisible = true
    },
    changeFond(v) {
      console.log('v===', v)
      //根据分类查询表单

    }
  }
}
</script>