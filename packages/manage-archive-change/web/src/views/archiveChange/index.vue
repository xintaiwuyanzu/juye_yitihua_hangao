<template>
  <metadata-file-view :title="formData.ARCHIVE_CODE" :formDataId="formDataId"
                      :formDefinitionId="formDefinitionId" refType="archive"
                      group-code="default"
                      :form-data="formData"
                      :watermark="true">
    <el-button type="primary" @click="$router.back()" style="float: right">返回</el-button>
    <start-process @validate="validate" :form="form"
                   ref="startProcess" v-if="startBtn" @afterStartProcess="afterStartProcess"/>
    <task-button :business-id="businessId" :taskInstanceId="taskId" v-if="this.businessId"/>
<!--    <el-button type="primary" @click="back()">返回</el-button>-->
    <el-card slot="detailTop">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" style="margin-left: -48px;margin-top: 10px">
        <div v-for="(item, index) in form.item" :key="index + item.field">
          <el-row>
            <el-col :span="12">
              <el-form-item label="调整项" :prop="'item.' + index + '.field'" required>
                <el-select v-model="item.field" style="width: 130px" placeholder="字段" @change="changeField(item)"
                           :disabled="ifExamine">
                  <el-option v-for="(item, index) in fields"
                             :key="item.key + index"
                             :label="item.label"
                             :value="item.key"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-form-item label="原始值" :prop="'item.' + index + '.oldValue'">
                <el-input v-model="item.oldValue" style="width: 120px" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="调整为" :prop="'item.' + index + '.newValue'">
                <el-input v-model="item.newValue" style="width: 150px" :disabled="ifExamine"/>
              </el-form-item>
            </el-col>
            <el-col :span="11">
              <el-button icon="el-icon-plus" v-show="index===0" @click="addItem"
                         style="position:absolute; right: 5%;" v-if="!ifExamine"/>
              <el-button icon="el-icon-delete" v-show="index!==0" @click="deleteItem(item, index)"
                         style="position:absolute; right: 5%; " v-if="!ifExamine"/>
            </el-col>
          </el-row>
        </div>
        <el-row>
          <el-col :span="24">
            <el-form-item label="调整说明:" prop="errorDescription" required>
              <el-input type="textarea" v-model="form.errorDescription" placeholder="请输入调整说明" clearable
                        :disabled="ifExamine"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </metadata-file-view>
</template>

<script>
  import StartProcess from "@archive/manage-archive-change/src/views/archiveChange/process"
  import taskButton from "./process/taskButton";

  export default {
    name: "index",
    components: {StartProcess, taskButton},
    data() {
      return {
        form: {
          item: [
            {
              field: '',
              newValue: '',
              oldValue: '',
              fieldName: '',
            }
          ],
          businessId: this.businessId,
          formDefinitionId: this.formDefinitionId,
          formDataId: this.formDataId,
          fieldsCode: '',
          newValues: '',
          oldValues: '',
          fieldNames: '',
          errorType: this.errorType,
          errorSource: this.errorSource,
          status: this.status,
          fondId: this.fondId,
          categoryId: this.categoryId,
          errorDescription: ''
        },
        rules: {},
        processes: [],
        fields: [],
        formData: {},
        errorCorrectionStatus: '',
      }
    },
    methods: {
      back() {
        // this.$refs.archive.change()
        this.$router.back()
      },
      $init() {
        //判断是否从待办任务进入页面
        if (this.businessId && this.taskId) {
          //获取要修改的字段详情
          this.getArchiveChangeRecord(this.businessId)
        } else {
          this.errorCorrectionStatus = '0'
          //获取表单的字段显示方案
          this.getFields(this.formDefinitionId)
          //获取表单数据
          this.getFormData()
        }
      },
      //获取要修改的字段详情
      async getArchiveChangeRecord(id) {
        const {data} = await this.$http.post('/archiveChange/detailByErrorCorrectionId', {
          detailByErrorCorrectionId: id
        })
        if (data.success) {
          //获取表单的字段显示方案
          if (data.data.length > 0) {
            await this.getFields(data.data[0].formDefinitionId)
            this.form.errorDescription = data.data[0].remarks
            this.form.formDataId = data.data[0].formDataId
            this.form.formDefinitionId = data.data[0].formDefinitionId
            this.$route.query.formDefinitionId=this.form.formDefinitionId
          }
          this.form.item = []
          data.data.forEach(item => {
            this.form.item.push({
              field: item.fieldsCode,
              newValue: item.newValues,
              oldValue: item.oldValues,
              // oldValue: item.fieldNames
            })
          })
          this.getFormData()
        }
      },
      //提交审核的验证
      validate() {
        this.params()
        this.$refs.form.validate(valid => {
          if (valid) {
            this.$refs.startProcess.open()
          } else {
            this.$message.warning("请输入完整信息！")
          }
        })
      },
      //获取表单的字段显示方案
      async getFields(formDefinitionId) {
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: formDefinitionId,
          schemeType: 'form',
          formCode: 'form'
        })
        if (data && data.success) {
          data.data.fields.forEach(item => {
            this.fields.push({key: item.code, label: item.name})
          })
        }
        this.form = {errorDescription: '', item: [{field: '', newValue: '', oldValue: '', fieldName: ''}]}
        if (this.errorDescription) {
          this.form.errorDescription = this.errorDescription
        }
      },
      //准备参数
      params() {
        this.form.formDefinitionId = this.formDefinitionId
        this.form.fondId = this.fondId
        this.form.errorType = this.errorType
        this.form.errorSource = this.errorSource
        this.form.categoryId = this.categoryId
        this.form.status = this.status
        this.form.formDataId = this.formData.id
        this.form.archiveCode = this.formData.ARCHIVE_CODE
        this.form.title = this.formData.TITLE
        this.form.businessId = this.businessId
        let fields = ''
        let newValues = ''
        let oldValues = ''
        let fieldNames = ''
        this.form.item.forEach(item => {
          fields += item.field + ','
          newValues += item.newValue + ','
          oldValues += item.oldValue + ','
          fieldNames += item.fieldName + ','
        })
        this.$set(this.form, 'fieldsCode', fields)
        this.$set(this.form, 'newValues', newValues)
        this.$set(this.form, 'oldValues', oldValues)
        this.$set(this.form, 'fieldNames', fieldNames)
      },
      addItem() {
        this.form.item.push({
          field: '',
          newValue: '',
          oldValue: ''
        })
      },
      deleteItem(item, index) {
        this.form.item.splice(index, 1)
      },
      changeField(item) {
        item.oldValue = this.formData[item.field] ? this.formData[item.field] : '暂无数据'
        item.fieldName = this.fields.find(v => v.key === item.field).label
      },
      //获取表单数据
      async getFormData() {
        const {data} = await this.$http.post('/manage/formData/detail', {
          formDefinitionId: this.formDefinitionId ? this.formDefinitionId : this.form.formDefinitionId,
          formDataId: this.formDataId ? this.formDataId : this.form.formDataId
        })
        if (data.success) {
          this.formData = data.data
        }
      },
      //启动成功之后修改状态为纠错中:2
      afterStartProcess() {
        this.errorCorrectionStatus = '2'
      }
    },
    computed: {
      //是审核中
      ifExamine() {
        return this.businessId !== undefined && this.taskId != undefined
      },
      startBtn() {
        return this.ifExamine === false && (this.errorCorrectionStatus === undefined || this.errorCorrectionStatus === '0')
      },
      formDefinitionId() {
        return this.$route.query.formDefinitionId
      },
      formDataId() {
        return this.$route.query.formDataId
      },
      errorType() {
        return this.$route.query.errorType
      },
      errorSource() {
        return this.$route.query.errorSource
      },
      status() {
        return this.$route.query.status
      },
      businessId() {
        return this.$route.query.businessId //|| this.$route.query.id
      },
      taskId() {
        return this.$route.query.taskId
      },
      fondId() {
        return this.$route.query.fondId
      },
      categoryId() {
        return this.$route.query.categoryId
      },
      errorDescription() {
        return this.$route.query.errorDescription   //纠错中的调整说明
      }
    }
  }
</script>