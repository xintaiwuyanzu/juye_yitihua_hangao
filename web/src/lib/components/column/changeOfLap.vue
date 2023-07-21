<template>
  <section>
    <el-link @click="showDialog">调卷</el-link>
    <el-dialog width="50%" :destroy-on-close="true" title="调卷" :visible.sync="dialogShow" :close-on-click-modal="false"
               append-to-body>
      <el-form :model="form" class="searchForm" label-width="150px" ref="form" :rules="rules">
        <el-row>
          <el-col :span="12">
            <el-form-item label="调卷人姓名：" prop="applyPerson">
              <el-input v-model="form.applyPerson" clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调卷人单位：" prop="applyUnit">
              <el-input v-model="form.applyUnit" clearable/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调卷人身份证号：" prop="applyIdentityId">
              <el-input v-model="form.applyIdentityId" clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调卷人手机号码：">
              <el-input v-model="form.applyPhone" clearable/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调卷时间：">
              <el-input v-model="form.applyDate" clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="调卷人类型：">
              <el-input v-model="form.useType" clearable/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="调卷用途：">
              <el-input v-model="form.applyusage" clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="档案内容：">
              <el-input v-model="form.applycontent" clearable/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="onSubmit" v-loading="loading" class="btn-submit">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'changeOfLap',
  data() {
    return {
      form: {},
      applyPerson: '',
      applyUnit: '',
      applyIdentityId: '',
      applyPhone: '',
      applyusage: '',
      applycontent: '',
      applyDate: '',
      useType: '',
      rules: {
        applyPerson: [
          {required: true, message: '调卷人姓名不能为空', trigger: 'blur'}
        ],
        applyIdentityId: [
          {required: true, message: '调卷人身份证号不能为空', trigger: 'blur'}
        ],
        applyUnit: [
          {required: true, message: '调卷人单位不能为空', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    showDialog() {
      this.dialogShow = true
    },
    onSubmit() {
      //this.loading = true
      // this.$http.post('/changeoflap/addLap', {
      //   fomeDataId: this.formId,
      //   danghaoId: this.row.id,
      //   changeOfLapTitle: this.row.TITLE,
      //   applyPerson: this.applyPerson,
      //   applyUnit: this.applyUnit,
      //   applyIdentityId: this.applyIdentityId,
      //   applyPhone: this.applyPhone,
      //   applyusage: this.applyusage,
      //   applycontent: this.applycontent,
      //   applyDate: this.applyDate,
      //   useType: this.useType
      // }).then(({data}) => {
      //   if (data && data.success) {
      //     this.$message.success('操作成功！')
      //   } else {
      //     this.$message.error(data.message)
      //   }
      //   this.loading = false
      //   this.dialogShow = false
      // })
    }
  },
}
</script>
