<template>
  <section>
    <nac-info back :title="title">
      <el-button type="primary" size="mini" @click="save" v-if="showAddBtn&&!detail">保 存</el-button>
      <el-button type="primary" size="mini" @click="getSaveTermCompilationContent" v-if="showSaveTerm">自动获取归档范围
      </el-button>
      <el-button type="primary" size="mini" @click="getYearCompilationContent" v-if="showCompilation">自动获取本年度编研情况
      </el-button>
      <start-process :businessId="management.id" v-if="showStart" @updateStatus="afterStartProcess"/>
      <task-button :task-instance-id="taskId" :business-id="businessId" v-if="this.businessId"
                   :compilationContent="management.compilationContent"/>
    </nac-info>
    <div class="index_main">
      <split-pane :default-percent='defaultPercent' split="vertical" slot="paneL" class="leftMaterial">
        <manage-file-drawer :batchId="id" slot="paneL" style="width: 100%; height: 100%"
                            :readOnly="!showAddBtn||detail"/>
        <el-form slot="paneR" style="height: 94%" :model="management" :rules="rules" ref="form" label-width="80px">
          <el-tabs v-model="activeName" type="card" class="tabs">
            <el-tab-pane label="基础信息" name="first" style="height: 100%">
              <base-info :management="management" :detail="detail"/>
            </el-tab-pane>
            <el-tab-pane label="内容管理" name="second" style="height: 100%">
              <el-form-item label="内容" prop="compilationContent" style="height: 90%" required>
                <c-k-editor-bar v-model="management.compilationContent"
                                style="height: 100%" :readOnly="detail"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="全宗卷历史记录" name="fourth" style="height: 100%;overflow: scroll"
                         v-if="management.status==='1'">
              <history ref="history" :businessId="businessId"/>
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </split-pane>
    </div>
  </section>
</template>

<script>
import baseInfo from "./baseInfo";
import history from "../process/examine/history";
import taskButton from "../process/examine/taskButton";
import StartProcess from "../process";
import formMixin from "@dr/auto/lib/util/formMixin";
import splitPane from 'vue-splitpane'
import {v4} from 'uuid'

export default {
  name: "index",
  mixins: [formMixin],
  components: {baseInfo, history, taskButton, StartProcess, splitPane},
  data() {
    return {
      isClear: false,
      activeName: 'first',
      //全宗卷信息
      management: {},
      fileList: [],
      title: '编辑',
      defaultPercent: 40,
      id: '',
      editType: ''
    }
  },
  methods: {
    $init() {
      this.editType = this.optType;
      if (this.editType === 'add') {
        this.initAdddForm()
        this.getRetentionPeriod()
        this.getPersonInfo()
        this.title = '全宗卷内材料添加'
      } else if (this.editType === 'edit') {
        this.getManagementById(this.$route.query.id)
        this.getFileList(this.$route.query.id)
        this.title = '全宗卷内材料编辑'
      } else if (this.editType === 'detail') {
        this.getManagementById(this.$route.query.id)
        this.getFileList(this.$route.query.id)
        this.title = '全宗卷内材料详情'
      }
      if (this.businessId) {
        //根据id获取全宗卷信息
        this.getManagementById(this.businessId)
        //获取上传文件列表
        this.getFileList(this.businessId)
      }
    },
    //获取当前登录人信息
    async getPersonInfo() {
      const {data} = await this.$http.post('/login/info')
      if (data.success) {
        if (this.management.personLiable == null) {
          this.$set(this.management, 'personLiable', data.data.userName)
        }
      } else {
        this.$message.error(data.message)
      }
    },
    //获取保管期限
    async getRetentionPeriod() {
      const {data} = await this.$http.post('/dossierType/getDossierTypeByCode', {managementTypeCode: this.$route.query.managementTypeCode})
      if (data.success) {
        if (this.management.retentionPeriod == null) {
          this.$set(this.management, 'retentionPeriod', data.data.saveTerm)
        }
      } else {
        this.$message.error(data.message)
      }
    },
    //获取上传文件列表
    async getFileList(refId) {
      const {data} = await this.$http.post('files/list', {
        refId: refId,
        refType: 'fondsdescriptivefile',
      })
      if (data.success) {
        if (data && data.success) {
          this.fileList = data.data
        }
      }
    },
    initAdddForm() {
      let myDate = new Date();
      let month = myDate.getMonth() + 1
      let day = myDate.getDate()
      if (month < 10) {
        month = '0' + month
      }
      if (day < 10) {
        day = '0' + day
      }
      //获取uuid
      this.management.id = v4()
      this.id = this.management.id
      // this.management.fileTime = myDate.getFullYear() + '' + month + day
      this.$set(this.management, 'fileTime', myDate.getFullYear() + '' + month + day)
      this.management.fondId = this.$route.query.fondId
      this.management.managementTypeCode = this.$route.query.managementTypeCode
    },
    async save() {
      try {
        const valid = await this.$refs.form.validate()
        if (valid) {
          this.management.status = '0'
          if (this.editType === 'add') {
            const {data} = await this.$http.post('/management/insert', Object.assign(this.management, {optType: this.editType}))
            if (data.success) {
              this.$message.success("添加成功")
              this.editType = 'edit'
              this.title = '全宗卷内材料编辑'
              await this.getManagementById(this.management.id)
            } else {
              this.$message.error("添加失败")
            }
          } else if (this.editType === 'edit') {
            const {data} = await this.$http.post('/management/update', Object.assign(this.management, {optType: this.editType}))
            if (data.success) {
              this.$message.success('修改成功')
            } else {
              this.$message.error("修改失败")
            }
          }
        } else {
          this.$message.warning('请填写完整信息！')
        }
      } catch {
        this.$message.warning('请填写完整信息！')
      }
    },
    //自动获取归档范围 从智能归档配置系统获取
    async getSaveTermCompilationContent() {
      const {data} = await this.$http.post('/saveTermBo/getCompilationContent', {
        fondId: this.management.fondId
      })
      if (data.success) {
        this.$message.success('操作成功！')
        this.$set(this.management, 'compilationContent', data.data)
      } else {
        this.$message.error(data.message)
      }
    },
    //自动获取本年度编研情况
    async getYearCompilationContent() {
      const {data} = await this.$http.post('/compilationtask/getCompilationTaskList', {templateCode: this.$route.query.managementTypeCode})
      if (data.success) {
        this.$message.success('自动获取完成！')
        this.$set(this.management, 'compilationContent', data.data)
      } else {
        this.$message.error(data.message)
      }
    },
    //根据id获取全宗卷信息
    async getManagementById(id) {
      this.id = id
      const {data} = await this.$http.post('/management/detail', {id: id})
      if (data.success) {
        this.management = data.data
      } else {
        this.$message.error(data.message)
      }
    },
    //启动流程按钮后的操作
    afterStartProcess() {
      this.management.status = '1'
    }
  },
  computed: {
    showStart() {
      return this.management.status === '0' || this.management.status === '3'
    },
    businessId() {
      return this.$route.query.businessId
    },
    optType() {
      return this.$route.query.optType
    },
    //环节实例Id
    taskId() {
      return this.$route.query.taskId + ''
    },
    //显示保存按钮
    showAddBtn() {
      return this.management.status !== '1' && this.management.status !== '2'
    },
    //只能查看
    detail() {
      return this.$route.query.optType === 'detail'
    },
    //是否显示“自动获取归档范围”按钮
    showSaveTerm() {
      return 'bgqxgd' === this.$route.query.managementTypeCode && this.showAddBtn
    },
    //是否显示“自动获取本年度编研情况”按钮
    showCompilation() {
      return 'dabyycbqk' === this.$route.query.managementTypeCode && this.showAddBtn
    }
  }
}
</script>

<style lang="scss">
.tabs {
  height: 100%;

  .el-tabs__content {
    height: 100%;
  }

  .el-form-item__content {
    height: 100%;
  }

  .w-e-text {
    max-height: 100%;
  }
}
</style>