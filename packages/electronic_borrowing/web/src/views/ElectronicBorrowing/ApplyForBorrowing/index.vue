<template>
  <table-index title="申请借阅" path="Registration" :fields="fields" :insert="false" ref="table" :edit="false"
               :delete="false">
    <template v-slot:search-$btns="scope">
      <el-button type="primary" size="mini" @click="add(scope.row)">添加申请</el-button>
    </template>
    <el-table-column prop="updateDate" label="审批时间" width="160" align="center" after="createDate">
      <template v-slot="scope">
        <span v-show="scope.row.status!=='1'&&scope.row.status!=='0'">{{ scope.row.updateDate|datetime }}</span>
      </template>
    </el-table-column>
    <template v-slot:table-$btns="{row}">
      <el-button type="text" width="60" @click="queryDedail(row)" v-show="row.status!=='0'">查看详情</el-button>
      <el-button type="text" width="60" @click="del(row)"
                 v-show="row.status!=='3'&&row.status!=='2'&&row.status!=='0'&&row.status!=='4'">撤销
      </el-button>
      <el-button type="text" width="60" @click="editApply(row)" v-show="row.status=='0'">编辑</el-button>
      <el-button type="text" width="60" @click="delApply(row)" v-show="row.status=='0'">删除</el-button>
    </template>
    <el-dialog :visible.sync="showApply" title="申请借阅" @close="close" width="60%">
      <el-form :model="form" ref="form" :rules="rules" label-width="140px">
        <el-form-item label="申请人姓名" prop="userName" style="display: inline-block">
          <el-input v-model="form.userName" placeHolder="请输入申请人" style="width: 100%;"></el-input>
        </el-form-item>
        <!--        <el-form-item prop="defaultOrganiseName" label="申请人单位"  style="display: inline-block">-->
        <!--          <el-input v-model="form.defaultOrganiseName" style="width: 100%;">-->
        <!--          </el-input>-->
        <!--        </el-form-item>-->
        <el-form-item prop="defaultOrganiseName" label="申请人部门" style="display: inline-block">
          <el-input v-model="form.defaultOrganiseName" style="width: 100%;">
          </el-input>
        </el-form-item>
        <el-form-item prop="mobile" label="联系方式" style="display: inline-block">
          <el-input v-model="form.mobile" placeHolder="请输入联系方式" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item prop="borrowingTime" label="申请时间" style="display: inline-block">
          <el-date-picker style="width: 87%;"
                          v-model="form.borrowingTime"
                          type="date"
                          placeholder="选择申请日期" value-format="timestamp">
          </el-date-picker>
        </el-form-item>
        <el-form-item prop="purpose" label="借阅目的" required>
          <el-input v-model="form.purpose" type="textarea" placeHolder="请输入借阅目的" style="width: 100%;">
          </el-input>
        </el-form-item>
        <el-form-item prop="archiveContent" label="借阅档案信息" required>
          <el-input v-model="form.archiveContent" type="textarea" placeHolder="请输入借阅档案信息"
                    style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item prop="keywordString" label="关键字" required>
          <el-input v-model="form.keywordString" placeHolder="请输入搜索关键字" style="width: 100%;"></el-input>
        </el-form-item>
        <el-form-item prop="printState" label="是否打印" required style="display: inline-block">
          <select-dict v-model="form.printState" type="archivePrint" placeHolder="请选择是否打印"
                       style="width: 100%;"></select-dict>
        </el-form-item>
        <el-form-item prop="downloadState" label="是否下载" required style="display: inline-block">
          <select-dict v-model="form.downloadState" type="archiveDownload" placeHolder="请选择是否下载"
                       style="width: 100%;"></select-dict>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="info" @click="close">取消</el-button>
        <el-button type="primary" @click="temporarySave">保 存</el-button>
        <el-button type="primary" @click="SaveSubmission">保存并提交</el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="showEdit" title="编辑申请" @close="close1">
      <el-form :model="form1" inline ref="form" :rules="rules" label-width="140px">
        <el-form-item prop="purpose" label="借阅目的" placeHolder="请输入借阅目的" required>
          <el-input v-model="form1.purpose" style="width: 34vw" type="textarea">
          </el-input>
        </el-form-item>
        <el-form-item prop="archiveContent" label="借阅档案信息" required>
          <el-input v-model="form1.archiveContent" style="width: 34vw" type="textarea"
                    placeHolder="请输入借阅档案信息"></el-input>
        </el-form-item>
        <el-form-item prop="keywordString" label="关键字" placeHolder="请输入搜索关键字" required>
          <el-input v-model="form1.keywordString" style="width: 16vw"></el-input>
        </el-form-item>
        <el-form-item prop="printState" label="是否打印">
          <select-dict v-model="form1.printState" style="width: 16vw" type="archivePrint"
                       placeHolder="请选择是否打印"></select-dict>
        </el-form-item>
        <el-form-item prop="downloadState" label="是否下载">
          <select-dict v-model="form1.downloadState" style="width: 16vw" type="archiveDownload"
                       placeHolder="请选择是否下载"></select-dict>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: right">
        <el-button type="info" @click="showEdit=false">取消</el-button>
        <el-button type="primary" @click="SaveSubmission1">保存并提交</el-button>
      </div>
    </el-dialog>
  </table-index>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      showEdit: false,
      form1: {},
      dict: ['archivePrint', 'archiveDownload'],
      fields: [
        {prop: 'borrowingCode', label: '申请编码', edit: false, search: true, width: 140},
        {prop: 'applicant', label: '申请人', edit: false, search: true, width: 140},
        {prop: 'purpose', label: '查询目的', type: 'textarea'},
        {prop: 'archiveContent', label: '借阅档案信息', type: 'textarea'},
        // {prop: 'reviewScope', label: '查询范围',type:'textarea'},
        //  添加当前登录人信息，添加要申请查阅的名字
        // {prop: 'name', label: '人名'},
        {prop: 'keywordString', label: '关键字', search: true},
        {prop: 'createDate', label: '申请时间', edit: false, dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140,},
        {prop: 'updateDate', label: '审批时间', edit: false, dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140,},
        {
          prop: 'downloadState',
          label: '是否下载',
          edit: false,
          dictKey: 'archiveDownload',
          component: 'tag',
          width: 80
        },
        {prop: 'printState', label: '是否打印', edit: false, dictKey: 'archivePrint', component: 'tag', width: 80},
        {prop: 'status', label: '状态', edit: false, dictKey: 'borrow.status', component: 'tag', width: 80},
      ],
      showApply: false,
      form: {},
      rules: {},
      list111: []

    }
  },
  methods: {
    $init() {
      this.$refs.table.loadData()


    },
    // 获取当前登录人
    getPerson() {
      this.$http.post('/Registration/personData').then(({data}) => {
        if (data.success) {
          this.form = data.data
        }
      })
    },

    //  申请
    add() {
      this.getPerson()
      this.showApply = true
    },
    //保存
    async temporarySave() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.form.status = '0'
        this.$http.post('Registration/insert', this.form).then(({data}) => {
          if (data.success) {
            this.$message.success('保存成功')
            this.$refs.table.loadData()
            this.close()
          } else {
            this.$message.error(data.message)
          }
        })
      }

    },
    //保存并提交
    async SaveSubmission() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        this.$http.post('Registration/insert', {status: 1, ...this.form}).then(({data}) => {
          if (data.success) {
            this.$message.success('保存并提交成功')
            this.$refs.table.loadData()
            this.close()
          } else {
            this.$message.error(data.message)
          }
        })
      }
    },
    close() {
      this.showApply = false
      this.form = {}
    },

    //  查看详情
    queryDedail(row) {
      if (row.status == '3') {
        this.$http.post('/RegistrationDetails/check', {borrowingId: row.id}).then(({data}) => {
          if (data.success) {
            this.list111 = data.data.data
            if (this.list111.length > 0) {
              this.$router.push({
                path: '/ElectronicBorrowing/ApplyForBorrowing/detail',
                query: {
                  id: row.id,
                  keywordString: row.keywordString,
                  downloadState: row.downloadState,
                  watermarkState: row.watermarkState,
                  printState: row.printState
                }
              })
            }
          } else {
            this.$message.error(data.message)
          }
        })
      } else if (row.status == '4') {
        this.$message.warning('申请被拒绝')
      } else if (row.status == '1') {
        this.$message.warning('等待管理员审批')
      } else {
        this.$message.warning('申请正在审核，暂时不能查看')
      }

    },
    //撤销
    del(row) {
      this.$confirm('确定撤销该申请吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        row.status = '0'
        this.$http.post('Registration/update', row).then(({data}) => {
          if (data.success) {
            this.$message.success('申请撤销成功')
            this.$refs.table.loadData()
          }
        })
      })
    },
    editApply(row) {
      this.form1 = row
      this.showEdit = true
    },
    //保存
    SaveSubmission1() {
      this.form1.status = 1
      this.$http.post('Registration/update', this.form1).then(({data}) => {
        if (data.success) {
          this.$message.success('修改并提交成功')
          this.$refs.table.loadData()
          this.close1()
        }
      })
    },
    close1() {
      this.showEdit = false
      this.form1 = {}
    },
    delApply(row) {
      this.$confirm('确定删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post("/Registration/delete", {id: row.id}).then(({data}) => {
          if (data && data.success) {
            this.$message.success('删除成功！')
            this.$refs.table.loadData()
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      })
    },
  },
}
</script>
