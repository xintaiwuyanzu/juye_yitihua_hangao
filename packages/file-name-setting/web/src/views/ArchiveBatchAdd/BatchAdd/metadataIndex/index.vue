<template>
  <metadata-file-view :title="$route.query.title" :formDataId="$route.query.formDataId"
                      :formDefinitionId="$route.query.formDefinitionId" :refType="$route.query.refType"
                      :group-code="$route.query.groupCode"
                      :form-data="formData"
                      :watermark="$route.query.watermark"
                      :defaultShowFormData="false"
                      :defaultShowTabs="false"
                      ref="metadataFileView">
    <el-button type="primary" @click="$router.back()">返回</el-button>
    <el-card slot="detailTop">
      <div class="editCon">编辑目录</div>
      <el-form ref="formData" :model="formData" label-width="100" inline>
        <el-form-item prop='ARCHIVE_CODE' label='全宗号'>
          <el-input v-model="formData.FOND_CODE"/>
        </el-form-item>
        <el-form-item prop='ARCHIVE_CODE' label='档号'>
          <el-input v-model="formData.ARCHIVE_CODE"/>
        </el-form-item>
        <el-form-item prop='TITLE' label='题名'>
          <el-input v-model="formData.TITLE"/>
        </el-form-item>
        <el-form-item prop='VINTAGES' label='年度'>
          <el-input v-model="formData.VINTAGES"/>
        </el-form-item>
        <el-form-item prop='SAVE_TERM' label='保存期限'>
          <el-input v-model="formData.SAVE_TERM"/>
        </el-form-item>
        <el-form-item prop='SAVE_TERM' label='文号'>
          <el-input v-model="formData.FILECODE"/>
        </el-form-item>
        <el-form-item prop='SAVE_TERM' label='责任者'>
          <el-input v-model="formData.ZRZ"/>
        </el-form-item>
        <el-form-item prop='FILETIME' label='文件形成日期'>
          <el-input v-model="formData.FILETIME"/>
        </el-form-item>
        <el-form-item prop='NOTE' label='备注'>
          <el-input v-model="formData.NOTE"/>
        </el-form-item>
      </el-form>
      <div class="footer">
        <el-button type="info" @click="this.$router.back">取 消</el-button>
        <el-button type="primary" @click="save">保 存</el-button>
      </div>
    </el-card>
  </metadata-file-view>
</template>
<script>

export default {
  props: {
    defaultParams: {type: Object}
  },
  data() {
    return {
      formdatarow: this.$route.query.formdatarow,
      rules: {},
      formData: {status_info: ''},
      currentNode: '123',
      fields: [],
    }
  },
  methods: {
    //编辑目录保存
    async save() {
      if (this.formData.status_info !== 'RECEIVE') {
        return this.$message.error("该档案已不在暂存库，不可进行编辑！")
      }
      this.formData.formDefinitionId = this.$route.query.formDefinitionId
      const url = `/receive/offline/detail/updateForm`
      const {data} = await this.$post(url, Object.assign(this.formData, this.$route.query.defaultParams))
      if (data.success) {
        this.$message.success("保存成功！")
      }
    },
    /**
     * 初始化加载表单数据
     * @returns {Promise<void>}
     */
    async $init() {
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
    },
    getCurr(param) {
      this.currentNode = param
    }
  }
}
</script>
<style lang="scss" scoped>
.editCon {
  height: 60px;
  font-size: 20px;
  font-weight: 600;
}

::v-deep .el-form-item--small .el-form-item__label {
  width: 100px !important;
}

.el-input--small {
  width: 200px;
}

.footer {
  padding-left: 90px;
  padding-top: 10px;
}
</style>