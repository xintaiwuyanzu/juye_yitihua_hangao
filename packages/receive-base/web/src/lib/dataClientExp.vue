<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">客户端导出<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">导出选中</el-dropdown-item>
        <el-dropdown-item command="all">导出所有</el-dropdown-item>
        <el-dropdown-item command="query">导出查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog width="60%" title="导出" :visible.sync="dialogShow" :close-on-click-modal="false">
      <el-form label-width="260px" :rules="rules" ref="impexp" :model="impexp">
        <el-row>
          <el-col :span="10">
            <el-form-item label="导出数量:" prop="number">
              <el-tag>{{ impexp.number }}条</el-tag>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="字节数:" prop="byteNumber">
              <el-tag>{{ impexp.byteNumber }}Byte</el-tag>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="选择导出方案:" prop="expSchemaId">
              <el-select v-model="impexp.expSchemaId" style="width: 200px" clearable>
                <el-option v-for="item in selectData"
                           :label="item.groupName"
                           :value="item.groupId"
                           :key="item.groupId"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="任务执行时间:" prop="taskTime">
              <el-input v-model="impexp.taskTime" placegolder="请输入任务执行时间" type="text"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="10">
            <el-form-item label="组盘大小:" prop="diskSize">
              <el-input v-model="impexp.diskSize" placegolder="请输入组盘大小" type="number"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="10">

          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="onSubmit" v-loading="loading" class="btn-submit">提 交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "@archive/core/src/lib/components/abstractComponent";

export default {
  extends: abstractComponent,
  name: 'expFileList',
  data() {
    return {
      selectData: [],
      expSchemaId: '',
      mineType: '',
      dict: ['impexp.mineType'],
      expType: 'all',
      impexp: {},
      rules: {
        number: [{required: false, trigger: 'blur'}],
        newPwd: [{required: false, trigger: 'blur'}],
        expSchemaId: [{required: true, message: '请选择导出方式', trigger: 'change'}],
        taskTime: [{required: false, trigger: 'blur'}],
        diskSize: [{required: false, trigger: 'blur'}],
      }
    }
  },
  methods: {
    $init() {
    },
    handleCommand(command) {
      this.expType = command
      this.showDialog()
    },
    //显示弹出框
    showDialog() {
      this.loading = true
      const query = this.eventBus.getQueryByQueryType(this.expType)
      this.$http.post('/dataBatch/getSelectCount', {...query}).then(data => {
        this.impexp.number = data.data.data
      })
      this.$http.post('/archiveConfig/keyMapGroup', {businessCode: '2'}).then(({data}) => {
        if (data && data.success) {
          //只展示导出的
          this.selectData = data.data
          //如果 1 则直接赋值显示
          if (this.selectData.length === 1) {
            this.impexp.expSchemaId = this.selectData[0].groupId
          }
          this.dialogShow = true
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    onSubmit() {
      this.$refs.impexp.validate(valid => {
        if (valid) {
          const query = this.eventBus.getQueryByQueryType(this.expType)
          this.$http.post('/dataBatch/newBatch', {
            taskTime: this.impexp.taskTime,
            diskSize: this.impexp.diskSize,
            expSchemaId: this.expSchemaId,
            type: 'EXP',
            ...query
          }).then(({data}) => {
            if (data && data.success) {
              this.$message({duration: '500', message: '正在导出...，请到【导出记录】查看结果', type: 'success'})
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
            this.dialogShow = false
            this.expSchemaId = ''
            this.impexp = {}
          })
        }
      })
    }
  },
}
</script>
