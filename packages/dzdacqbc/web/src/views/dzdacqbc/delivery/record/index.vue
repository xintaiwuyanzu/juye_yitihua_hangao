<template>
  <table-index :fields="fields" path="delivery" :insert="false" :edit="false" :delete="false" ref="table">
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="90" @click="showExamine(scope.row)"
                 v-if="scope.row.examineState === '0' && scope.row.examinePersonId === personId">审 核
      </el-button>
    </template>
    <el-dialog title="审核" :visible.sync="examineDialogVisible" width="40%" center :before-close="beforeClose">
      <el-form :model="examineContent" ref="examineContent" :rules="rules" label-width="160px">
        <el-form-item label="批次名称:" prop="archiveName">
          <el-tag>{{ examineContent.archiveName }}</el-tag>
        </el-form-item>
        <el-form-item label="出库数量:" prop="archiveNumber">
          <el-tag>{{ examineContent.archiveNumber }}</el-tag>
        </el-form-item>
        <!--        <el-form-item label="提交人:" prop="operatePerson">
                  <el-tag>{{ examineContent.operatePerson }}</el-tag>
                </el-form-item>-->
        <el-form-item label="审核意见:" prop="suggestion" required>
          <el-input type="textarea" :rows="2" placeholder="请输入内容" v-model="examineContent.suggestion">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type='danger' @click="examine('0')">驳 回</el-button>
        <el-button type="primary" @click="examine('1')">通 过</el-button>
      </div>
    </el-dialog>
  </table-index>
</template>

<script>

export default {
  data() {
    return {
      examineDialogVisible: false,
      examineContent: {},
      personId: '',
      detailVisible: false,
      //新代码
      fields: [
        {
          prop: 'archiveName',
          label: '批次',
          align: 'center',
          search: true,
          route: true,
          component: 'text',
          routerPath: 'record/detail'
        },
        {
          prop: 'archiveNumber', label: '出库数量', align: 'center', required: true,
        },
        {prop: 'operatePerson', label: '操作人', align: 'center', required: true},
        {prop: 'suggestion', label: '审核意见', align: 'center', required: true},
        {
          prop: 'examineState', label: '审核状态', width: "100", component: 'tag', showTypeKey: 'show', mapper: {
            '0': {label: '待审核', show: 'danger'},
            '1': {label: '已通过', show: 'success'},
            '2': {label: '已驳回', show: 'warning'},
            '3': {label: '已出库', show: 'info'},
          }, fieldType: 'select', edit: false, search: true
        }
      ],
      rules: {
        personId: [{suggestion: true, message: '审核意见不能为空', trigger: 'blur'}]
      },
    }
  },
  methods: {
    $init() {
      this.getCurrentPerson()
    },
    apiPath() {
      return '/delivery'
    },
    showExamine(row) {
      this.examineContent = row
      this.examineDialogVisible = true
    },
    examine(type) {
      this.$refs.examineContent.validate(valid => {
        if (valid) {
          this.$http.post('delivery/examine', {
            type: type,
            id: this.examineContent.id,
            suggestion: this.examineContent.suggestion
          }).then(({data}) => {
            if (data.success) {
              this.$message.success(type === '1' ? '审核通过' : '驳回成功')
              this.examineDialogVisible = false
              this.$refs.table.reload()
            } else {
              this.$message.error(data.message)
            }
          })
        }
      })
    },
    getCookie(cname) {
      let name = cname + "=";
      let ca = document.cookie.split(';');
      for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') c = c.substring(1);
        if (c.indexOf(name) !== -1) {
          return c.substring(name.length, c.length);
        }
      }
      return "";
    },
    getCurrentPerson() {
      this.personId = this.getCookie('dauth')
    },
    beforeClose() {
      this.examineContent.suggestion = ''
      this.examineDialogVisible = false
    },
  },
}
</script>


