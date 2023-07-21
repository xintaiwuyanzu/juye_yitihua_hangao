<template>
  <section>
    <el-button type="primary" @click="showDialog">数据接收</el-button>
    <el-dialog width="50%" title="数据接收" :visible.sync="dialogShow" :close-on-click-modal="false">
      <el-form label-width="120px">
        <el-form-item label="接收类型">
          <el-select v-model="expSchemaId" style="width: 200px" placeholder="选择接收类型">
            <el-option v-for="item in selectData"
                       :label="item.groupName"
                       :value="item.groupId"
                       :key="item.groupId"/>
          </el-select>
        </el-form-item>
        <el-form-item label="数据来源">
          <select-dict type="impSourceTypes" style="width: 200px" v-model="sourceCode" placeholder="请选择数据来源"/>
        </el-form-item>
        <el-form-item label="归档部门">
          <el-input
              style="width: 200px"
              placeholder="请输入归档部门名称"
              v-model="transferingUnit"
              clearable/>
        </el-form-item>
        <el-form-item label="归档部门负责人">
          <el-input
              style="width: 200px"
              placeholder="请输入归档部门负责人"
              v-model="transferingUnitPerson"
              clearable/>
        </el-form-item>
        <el-form-item>
          <el-upload style="text-align: center;margin-top: 50px"
                     ref="uploadFile"
                     action="api/receive/offline/newBatch"
                     accept="text/xml, application/xml,.xml,.xlsx,.xls,.dbf,.accdb"
                     :before-upload="beforeUpload"
                     :on-success="Push"
                     :data="{
                         formDefinitionId:this.formId,
                         fondId:this.fond.id,
                         categoryId:this.category.id,
                         fondCode:this.fond.code,
                         type:'IMP',
                         categoryCode:this.category.code,
                         impSchemaId:this.expSchemaId,
                         name:this.eventBus.defaultForm.status_info,
                         sourceCode:this.sourceCode,
                         transferingUnit:this.transferingUnit,
                         transferingUnitPerson:this.transferingUnitPerson
                     }"
                     :limit="1"
                     :on-exceed="handleExceed"
                     :auto-upload="false">
            <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">选取文件</el-button>
            <div style="margin-bottom: 20px">支持xlsx,xls,xml,dbf,accdb类型文件</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="submitUpload" v-loading="loading" class="btn-submit">上 传</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "@archive/core/src/lib/components/abstractComponent";

export default {
  extends: abstractComponent,
  name: 'impCataloglist',
  data() {
    return {selectData: [], expSchemaId: '', sourceCode: '', transferingUnit: '', transferingUnitPerson: ''}
  },
  methods: {
    //显示弹出框
    showDialog() {
      this.$http.post('/archiveConfig/keyMapGroup', {businessCode: '1'}).then(({data}) => {
        if (data && data.success) {
          //只展示导入的
          this.selectData = data.data
          //如果 1 则直接赋值显示
          if (this.selectData.length === 1) {
            this.expSchemaId = this.selectData[0].groupId
          }
          this.dialogShow = true
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
      this.dialogShow = true
    },
    beforeUpload(file) {
      const FileExt = file.name.replace(/.+\./, "");
      if (['xml', 'xlsx', 'xls', 'dbf', 'accdb'].indexOf(FileExt.toLowerCase()) === -1) {
        this.$message({
          type: 'warning',
          message: '请上传符合后缀名的附件！'
        });
        return false;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    Push() {
      this.$message({duration: '500', message: '上传成功，请在接收记录中查看', type: 'success'})
      this.dialogShow = false
    },
    submitUpload() {
      this.$refs.uploadFile.submit();
      this.progress = true
      this.timeout = setInterval(() => {
        if (this.percent <= 99) {
          const a = Math.round(Math.random() * 5 + 2)
          this.percent = a + this.percent >= 100 ? 99 : a + this.percent
        }
      }, 1000);
    }
  },
}
</script>
