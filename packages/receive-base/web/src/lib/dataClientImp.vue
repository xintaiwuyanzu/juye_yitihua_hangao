<template>
  <section>
    <el-button type="primary" @click="showDialog">客户端导入</el-button>
    <el-dialog width="50%" title="客户端导入" :visible.sync="dialogShow" :close-on-click-modal="false">
      <el-form label-width="100px">
        <el-form-item label="导入类型">
          <el-select v-model="expSchemaId" style="width: 200px" placeholder="选择导入类型">
            <el-option v-for="item in selectData"
                       :label="item.groupName"
                       :value="item.groupId"
                       :key="item.groupId"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-upload style="text-align: center;margin-top: 50px"
                     ref="uploadFile"
                     action="api/dataBatch/newBatch"
                     accept="text/xml, application/xml,.zip"
                     :before-upload="beforeUpload"
                     :on-success="Push"
                     :data="{
                         formDefinitionId:this.formId,
                         fondId:this.fond.id,
                         categoryId:this.category.id,
                         type:'IMP',
                         categoryCode:this.category.code,
                         impSchemaId:this.expSchemaId,
                         name:this.eventBus.defaultForm.status_info,
                     }"
                     :limit="1"
                     :on-exceed="handleExceed"
                     :auto-upload="false">
            <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">选取文件</el-button>
            <div style="margin-bottom: 20px">可上传zip文件</div>
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
  name: 'impZipList',
  data() {
    return {selectData: [], expSchemaId: '', sourceCode: ''}
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
      if (['zip'].indexOf(FileExt.toLowerCase()) === -1) {
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
      this.$message.success('上传成功，请在导入记录中查看导入详情')
      this.dialogShow = false
      this.expSchemaId = ''
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
