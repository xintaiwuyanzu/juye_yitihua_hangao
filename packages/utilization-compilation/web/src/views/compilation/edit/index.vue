<template>
  <section>
    <nac-info back :title="title">
      <el-button type="primary" size="mini" @click="goFullscreen">全屏</el-button>
      <el-button type="primary" size="mini" @click="save" v-if="showSave">保 存</el-button>
<!--      <el-button type="primary" size="mini" @click="AISave" v-if="showSave">自动编研</el-button>-->
      <start-process :businessId="taskManagement.id" @afterStartProcess="afterStartProcess"
                     v-if="showStart"/>
      <task-button :task-instance-id="taskId" :business-id="businessId" v-if="this.businessId"
                   :compilationContent="taskManagement.compilationContent"/>
    </nac-info>
    <div class="index_main" id="center">
      <split-pane :default-percent='defaultPercent' split="vertical" slot="paneL" class="leftMaterial">
        <left slot="paneL" :batchId="$route.query.id" :showSave="showSave" :readOnly="readOnly"/>
        <el-form slot="paneR" style="height: 94%" :model="taskManagement" :rules="rules" ref="form"
                 label-width="80px">
          <el-tabs v-model="activeName" type="card" class="tabs">
            <el-tab-pane label="内容管理" name="first" style="height: 100%">
              <el-form-item label="内容" prop="compilationContent" style="height: 90%" required>
                <c-k-editor-bar v-model="taskManagement.compilationContent" :value="taskManagement.compilationContent"
                                style="height: 100%" :readOnly="readOnly"/>
              </el-form-item>
            </el-tab-pane>
            <el-tab-pane label="编研历史记录" name="fourth" style="height: 100%;overflow: scroll"
                         v-if="showHistory">
              <history ref="history" :businessId="businessId"/>
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </split-pane>
    </div>
    <el-dialog title="原文预览" :visible.sync="showFile" class="fileContent" :fullscreen="showFile">
      <file-content :file-info="fileInfo"/>
    </el-dialog>
  </section>
</template>
<script>
import formMixin from "@dr/auto/lib/util/formMixin"
import StartProcess from "../process"
import taskButton from "../process/examine/taskButton"
import history from "../process/examine/history"
import material from "./material"
import splitPane from 'vue-splitpane'
import Left from "./left";

export default {
  name: "index",
  mixins: [formMixin],
  components: {Left, StartProcess, material, splitPane, taskButton, history},
  data() {
    return {
      activeName: 'first',
      //
      //编研信息
      taskManagement: {},
      fileList: [],
      title: '新建',
      optType: '',
      showFile: false,
      fileInfo: {},
      defaultPercent: 40,
    }
  },
  methods: {
    $init() {
      if (this.businessId) {
        this.taskManagement.id = this.businessId
        this.getTaskManagement(this.taskManagement.id)
      } else {
        this.taskManagement.id = this.$route.query.id
        this.getTaskManagement(this.taskManagement.id)
      }
      this.getFileList(this.taskManagement.id)
    },
    //获取上传文件列表
    async getFileList(refId) {
      const {data} = await this.$http.post('files/list', {
        refId: refId,
        refType: 'compilation',
      })
      if (data.success) {
        if (data && data.success) {
          this.fileList = data.data
        }
      }
    },
    async save() {
      try {
        const valid = await this.$refs.form.validate()
        if (valid) {
          const {data} = await this.$http.post('/compilationtask/update', Object.assign(this.taskManagement, {optType: this.optType}))
          if (data.success) {
            this.$message.success('保存成功！')
            await this.getTaskManagement(this.taskManagement.id)
          } else {
            this.$message.error("保存失败！")
          }
        } else {
          this.$message.warning('请填写完整信息！')
        }
      } catch {
        this.$message.warning('请填写完整信息！')
      }
    },
    async AISave() {
      const {data} = await this.$http.post('/compilationtask/AISave', {
        batchId: this.taskManagement.id,
        compilationTemplateId: this.taskManagement.templateId
      })
      if (data.success) {
        this.$message.success("自动编研完成！")
      } else {
        this.$message.success(data.message)
      }
      await this.getTaskManagement(this.taskManagement.id)
    },
    //根据任务id获取内容信息TODO
    async getTaskManagement(taskId) {
      const {data} = await this.$http.post('compilationtask/detail', {id: taskId})
      //如果有编研内容
      if (data.success && data.data) {
        this.taskManagement = data.data
        // 判断内容是否存在
        if (this.taskManagement.compilationContent) {
          this.title = '编辑'
          this.optType = 'edit'
        } else {
        }
      } else {
        this.optType = 'add'
      }
    },

    //启动流程按钮后的操作
    afterStartProcess() {
      this.taskManagement.status = '1'
      this.$router.back()
    },
    //全屏
    goFullscreen() {
      let fullarea = document.getElementById('center')
      if (fullarea.requestFullscreen) {
        fullarea.requestFullscreen();
      } else if (fullarea.webkitRequestFullScreen) {
        fullarea.webkitRequestFullScreen();
      } else if (fullarea.mozRequestFullScreen) {
        fullarea.mozRequestFullScreen();
      } else if (fullarea.msRequestFullscreen) {
        // IE11
        fullarea.msRequestFullscreen();
      }
    },
  },
  computed: {
    businessId() {
      return this.$route.query.businessId
    },
    //是否显示保存按钮
    showSave() {
      return this.$route.query.status === '0'
    },
    readOnly() {
      return this.taskManagement.status === '2'
    },
    //是否显示审核按钮
    showStart() {
      return this.$route.query.status === '0' && this.optType === 'edit'
    },
    //是否显示历史记录
    showHistory() {
      return this.taskId
    },
    //环节实例Id
    taskId() {
      return this.$route.query.taskId
    }
  },
}
</script>
<style lang="scss">
.tabs {
  overflow: hidden;

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

.leftMaterial {
  .splitter-pane {
    overflow: scroll;
  }
}

//原文预览样式
.fileContent {
  height: 100%;

  .el-dialog {
    height: 100%;
    width: 90%;
  }

  .el-dialog__body {
    height: 100%;
    max-height: none;
  }
}
</style>
