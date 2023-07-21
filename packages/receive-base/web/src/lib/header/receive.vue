<template>
  <section>
    <el-button type="primary" @click="showDialog">接收</el-button>
    <el-dialog width="50%" title="档案接收" :visible.sync="dialogShow" :close-on-click-modal="false">
      <el-select v-model="expSchemaId" style="width: 200px" placeholder="选择导入方案">
        <el-option v-for="item in selectData"
                   :label="item.groupName"
                   :value="item.groupId"
                   :key="item.groupId"/>
      </el-select>
      <el-upload style="text-align: center;margin-top: 50px"
                 ref="uploadFile"
                 action="api/impexpbatch/preImp"
                 accept="text/xml, application/xml,.xml,.xlsx,.xls,.zip"
                 :before-upload="beforeUpload"
                 :on-success="Push"
                 :data="{formId:this.formId, categoryId:this.category.id}"
                 :limit="1"
                 :on-exceed="handleExceed"
                 :auto-upload="false">
        <el-button slot="trigger" size="medium" type="primary" icon="el-icon-search">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="medium" type="success" @click="submitUpload"><i
            class="el-icon-upload el-icon--right"/> 上传文件
        </el-button>
        <div style="margin-bottom: 20px">可上传xlsx,xls,xml文件</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="onsubmit" v-loading="loading" class="btn-submit">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "@archive/core/src/lib/components/abstractComponent";

/**
 * 头部按钮
 * 接收按钮
 */
export default {
  extends: abstractComponent,
  name: "receive",
  data() {
    return {selectData: [], expSchemaId: '', batchId: ''}
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
      if (['xml', 'xlsx', 'xls', 'zip'].indexOf(FileExt.toLowerCase()) === -1) {
        this.$message({
          type: 'warning',
          message: '请上传后缀名xml的附件！'
        });
        return false;
      }
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 1 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    Push(response) {
      this.$message.success('上传成功！')
      this.batchId = response.data.batch.id
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
    },
    onsubmit() {
      if (this.batchId === '') {
        this.$message.warning('先点击【上传文件】按钮！')
        return
      }
      this.$http.post('/impexpbatch/impData', {
        id: this.batchId,
        impSchemaId: this.expSchemaId
      }).then(({data}) => {
        if (data && data.success) {
          this.$message.success('操作成功！')
          this.eventBus.$emit("loadData")
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
