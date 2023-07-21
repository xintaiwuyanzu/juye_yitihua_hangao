<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">导出<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">导出选中</el-dropdown-item>
        <el-dropdown-item command="all">导出所有</el-dropdown-item>
        <el-dropdown-item command="query">导出查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
    <el-dialog width="50%" title="导出" :visible.sync="dialogShow" :close-on-click-modal="false">
      <el-form>
        <el-form-item label="选择导出方案" required>
          <el-select v-model="expSchemaId" style="width: 200px">
            <el-option v-for="item in selectData"
                       :label="item.name"
                       :value="item.id"
                       :key="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="选择导出类型" required>
          <select-dict v-model="mineType" type="impexp.mineType" placeholder="请选择导出类型" style="width: 200px"/>
        </el-form-item>
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
      expType: 'all'
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
      this.$http.post('/impexpscheme/page', {page: false, schemeType: '2'}).then(({data}) => {
        if (data && data.success) {
          //只展示导出的
          this.selectData = data.data
          this.dialogShow = true
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    onSubmit() {
      const query = this.eventBus.getQueryByQueryType(this.expType)
      if (this.expSchemaId == null || this.expSchemaId === '') {
        this.$message.warning('导出方案不能为空')
        return;
      }
      if (this.mineType == null || this.mineType === '') {
        this.$message.warning('导出类型不能为空')
        return;
      }
      this.$http.post('/expBatch/newBatch', {
        impSchemaId: this.expSchemaId,
        mineType: this.mineType,
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
      })
    }
  },
}
</script>
