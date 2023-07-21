<template>
  <section>
    <!--  <el-button type="primary" @click="send">归档</el-button>-->
    <el-dropdown
      placement="bottom"
      trigger="click"
      @command="handleCommand">
      <el-button class="search-btn" type="primary" v-show="!parentIndex">移至正式库<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">移动选中</el-dropdown-item>
        <el-dropdown-item command="all">移动所有</el-dropdown-item>
        <el-dropdown-item command="query">移动查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog title="移交" :visible.sync="dialogVisible" :close-on-click-modal=false
               :destroy-on-close="true" @close="dialogVisible =false"
               :close-on-press-escape=false width="30%">
      <el-form :model="form" label-width="110px">
        <el-form-item label="移交正式库">
          <el-select v-model="form.label" disabled placeholder="是"></el-select>
        </el-form-item>
        <el-form-item label="移交长期保存库">
          <el-select v-model="form.label" disabled placeholder="是"></el-select>
        </el-form-item>
        <el-form-item label="移交实体库">
          <el-select v-model="form.value" placeholder="请选择是否移交至实体库">
            <el-option label="是" value="1"></el-option>
            <el-option label="否" value="2"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实体档案类型" prop="archiveType" label-width="110px" v-if="form.value === '1'">
          <el-select v-model="form.archiveType" filterable clearable>
            <el-option
              v-for="item in archiveTypes"
              :key="item.id"
              :label="item.archiveTypeName"
              :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogVisible = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="close()" v-loading="loading" class="btn-submit">确 定</el-button>
      </div>
    </el-dialog>

  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

export default {
  extends: abstractComponent,
  name: 'filing',
  data() {
    return {
      targetPerson: '',
      sendType: 'all',
      query: {},
      action: '',
      form: {},
      dialogVisible: false,
      command: '',
      archiveTypes: [],
    }
  },
  methods: {
    $init() {
      this.$http.post('/archiveType/page', {page: false, aType: 'root', fondId: this.fond.id})
        .then(({data}) => {
          if (data.success && data.data != null) {
            this.archiveTypes = data.data
          }
        })
    },

    async close() {
      this.sendType = this.command
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      //todo 这里需要区分案卷  卷内件  件盒
      this.query = query
      if (this.form.value === '1') {
        if (this.form.archiveType == null || this.form.archiveType === '' || this.form.archiveType === undefined) {
          this.$message.warning('请选择实体档案类型！')
          this.dialogVisible = true
          return
        }
      }
      this.dialogVisible = false
      //移交状态要大写
      // const {data} = await this.$post('/manage/formData/updateStatusByQuery', {...query,status:'MANAGE',formDefinitionId:query.formDefinitionId})
      const {data} = await this.$post('/tempToFormal/batch/newBatch', {
        ...query,
        status: 'MANAGE',
        formDefinitionId: query.formDefinitionId,
        archiveType: this.form.archiveType
      })
      if (data.success) {
        // let detailNum = data.data.detailNum
        // let testTrueNum = data.data.testTrueNum
        // if (detailNum === testTrueNum) {
          this.eventBus.$emit("loadData")
          this.$message.success('保存成功，请前往查看结果！')
        // } else {
        //   this.$message.info('移至成功' + testTrueNum + '个，失败' + (detailNum - testTrueNum) + '个，请在入库检测中查看结果！')
        //   this.eventBus.$emit("loadData")
        //
        // }
      }
      else{
        this.$message.error(data.message)
      }
    },
    handleCommand(command) {
      this.dialogVisible = true
      this.command = command
    },
    /**
     * 显示提报dialog
     */
    async showSend() {
      this.dialogShow = true
    }
  }
}
</script>
<style lang="scss">
.breadcrumb-container .slot .el-form-item .el-form-item__label {
  width: 130px !important;
  padding-right: 10px !important;
}
</style>
