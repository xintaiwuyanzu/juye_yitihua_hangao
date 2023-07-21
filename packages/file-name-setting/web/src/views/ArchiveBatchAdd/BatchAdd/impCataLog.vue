<template>
  <section>
    <el-button type="primary" @click="showDialog">批量添加</el-button>
    <el-dialog width="50%" title="批量添加"
               @close="closeForm"
               :visible.sync="dialogShow"
               :close-on-click-modal=true
               :modal-append-to-body=false
               :destroy-on-close=true
               class="dialog">
      <el-row :gutter="10" style="margin-top: 10px">
        <el-col :span="8">
          <fond-tree autoSelect @check="formCategoryCheck" ref="fondTree" :withPermission="true"
                     style="height: 400px;overflow: scroll"/>
        </el-col>
        <el-col :span="16">
          <div style="margin-left: 120px;height: 100px;margin-top: 10px" v-if="pointOut">
            <div style="background-color: #eeeeee;
                        height: 50px;width: 18vw;
                        border-radius: 20px;">
              <div style="color: red;margin-left: 40px;padding-top: 15px">
                {{ pointOnData }}
              </div>
            </div>
          </div>
          <el-form :model="form" ref="form" label-width="100px" class="demo-dynamic" v-if="formPointOut">
            <el-form-item label="起始档号" :rules="{required: true, message: '起止档号不能为空', trigger: 'blur'}" prop="startNumber">
              <el-input v-model="form.startNumber"></el-input>
            </el-form-item>
            <el-form-item label="截至档号" :rules="{required: true, message: '截至档号不能为空', trigger: 'blur'}" prop="endNumber">
              <el-input v-model="form.endNumber"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('form')">提交</el-button>
              <el-button @click="resetForm('form')" type="success">重置</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-dialog>
  </section>
</template>
<script>
/**
 * 目录附件文件上传弹窗
 */
export default {
  data() {
    return {
      form: {
        //表单定义Id
        formDefinitionId: '',
        fondId: '',
        fondCode: '',
        categoryId: '',
        categoryCode: '',
        type: 'IMP',
        name: 'RECEIVE',
        startNumber:'',
        endNumber: '',
      },
      dialogShow: false,
      loading: false,
      //当前选择的全宗
      fond: {},
      //当前选择的门类
      category: {},
      //门类所有的元数据方案
      formDefinition: [],
      //提示信息
      pointOut: false,
      formPointOut: false,
      pointOnData: '错误',
    }
  },
  computed: {},
  methods: {
    $init() {
      this.dialogShow = false
    },
    /**
     * 弹窗关闭时清空数据
     */
    closeForm() {
      this.formDefinition = []
      this.fond = {}
      this.category = {}
      this.dialogShow = false
      this.form = {}
      this.formPointOut = false
    },
    /**
     * 全宗门类选中回调
     * @param v
     */
    async formCategoryCheck(v) {
      this.formDefinition = []
      this.$refs.fondTree.fonds.forEach(item => {
        if (this.$refs.fondTree.selectFond === item.id) {
          this.fond = item.data
        }
      })
      this.category = v.data
      await this.loadFormDefinition(v.data.id)
    },
    /**
     * 根据门类Id查询门类表单方案
     * @param categoryId
     * @returns {Promise<void>}
     */
    async loadFormDefinition(categoryId) {
      const {data} = await this.$http.post('manage/categoryconfig/page', {businessId: categoryId, page: false})
      if (data.success) {
        this.pointOut = false
        console.log(data.data[0])
        if (data.data[0] != undefined) {
          if (data.data[0].arcFormId != null || data.data[0].fileFormId != null) {
            if (data.data[0].arcFormId != null && data.data[0].arcFormId.length > 0) {
              //获取档号的生成规则
              this.fileNameSetting(data.data[0].arcFormId)
            } else {
              this.fileNameSetting(data.data[0].fileFormId)
            }

          } else {
            this.pointOnData = '未配置门类表单，请检查【档案配置-元数据配置】表单是否配置'
            this.pointOut = true
          }
        }
      }
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          //进行判断档案的相似度
          let on = this.form.startNumber
          let end = this.form.endNumber
          if(on.substring(0,on.length-4) === end.substring(0,end.length-4)){
            let start = parseInt(this.form.startNumber.substr(-4))
            let last = parseInt(this.form.endNumber.substr(-4))
            if (last>start){
              this.$http.post('/ArchiveBatchAdd/newBatch', this.form).then(({data}) => {
                if (data && data.success) {
                  this.dialogShow = false
                  this.$message.success(data.data)
                  this.$emit('loadData')
                } else {
                  this.dialogShow = false
                  this.$message.error(data.message)
                }
              })

            }else {
              this.$message.error("档号起始数不正常")
              return}
          }else {
            this.$message.error("请检查档号是否有误")
            return}
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    //中转作用 取出formdefinitionid
    async fileNameSetting(id) {

      this.loading = true
      if (id.length == 0) {
        this.loading = false
        return this.$message.error("请检查是否配置表单")
      }else {
        this.formPointOut = true
        this.form.formDefinitionId = id;
        this.form.fondId = this.fond.id
        this.form.fondCode = this.fond.code
        this.form.categoryId = this.category.id
        this.form.categoryCode = this.category.code
      }
      this.loading = false
    },
    //显示弹出框
    showDialog() {
      this.dialogShow = true
    },
  }
}

</script>
<style lang="scss">
.dialog {
  overflow: hidden;
  .el-dialog__body {
    padding: 25px 15px 0 15px;
  }
}
</style>