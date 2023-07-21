<template>
  <table-index title="接收记录详情" :fields="fields" path="receive/onlineDetail" :defaultSearchForm="defaultSearchForm"
               :insert="false" :edit="false" :back="true" ref="table" :delete="false">
    <el-table-column prop="aa" after="digitaldigest" label="摘要算法" width="80" align="center">SM3</el-table-column>
    <template v-slot:search-$btns>
      <process-container
          v-if="!isProcess"
          v-show="showRk"
          title="提交入库申请流程"
          processType="online_filling"
          :business-id="batchId"
          btn-text="入库申请"
          ref="pro"
          @saved="notifyResult">
      </process-container>
      <task-container v-if="isProcess" :task-instance-id="$route.query.taskId">
        <template v-slot:sendNext="params">
          <send-next @sendSaved="sendSaved"/>
        </template>
        <template v-slot:endProcess="params">
          <end-process @saveEnd="saveEnd" :beforeOpen="appendParams">
          </end-process>
        </template>
      </task-container>
      <el-button type="primary" @click="addArchives">批量入库</el-button>
      <el-button type="primary" v-if="status!='3'||status!='2'" @click="batchReturn">批量退回</el-button>
      <!--      <el-button type="primary" @click="toTest">重新检测</el-button>-->
    </template>
    <template v-slot:table-$btns="scope">
    <!--  <el-button type="text" size="mini" width="60" @click="editForm(scope.row)" v-show="showEditButton">编辑目录
      </el-button>-->
      <!--      <el-button type="text" size="mini" width="30">上传</el-button>-->
      <el-button type="text" size="mini" width="60"  v-if="status!='3'||status!='2'" @click="toTestReport(scope.row)">{{ fourDectionTitle }}</el-button>
      <el-button type="text" size="mini" width="30" @click="deleteDetail(scope.row)" v-show="showEditButton">删除
      </el-button>
    </template>
    <el-dialog width="80%" title="编辑目录" :visible.sync="editDialogShow" append-to-body>
      <el-form ref="form" :model="form" label-width="100" inline v-if="formFieldList.length>0">
        <el-form-item :prop='field.code' :label='field.name' label-width="100px" :key="field.id"
                      v-for="field in formFieldList">
          <select-dict v-model="form[field.code]" :type='field.meta.dict' clearable v-if="field.meta&&field.meta.dict"
                       :style="{width: field.remarks+'px'}"/>
          <el-input v-model="form[field.code]" :style="{width: field.remarks+'px'}" v-else/>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="editDialogShow = false" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="save" class="btn-submit">保存</el-button>
      </div>
    </el-dialog>
    <el-dialog width="40%" title="批量入库"
               @close="closeForm"
               :visible.sync="dialogShow"
               append-to-body
               class="dialog"
               :close-on-click-modal="false">
      <el-row :gutter="10" style="margin-top: 10px">
        <el-col :span="24">
          <fond-tree autoSelect @check="formCategoryCheck" ref="fondTree" :withPermission="true"
                     style="height: 400px;overflow: scroll"/>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false">取 消</el-button>
        <el-button type="primary" @click="addArchive">确 定</el-button>
      </div>
    </el-dialog>
  </table-index>
</template>
<script>
import {endProcess, processContainer, sendNext, taskContainer} from '@dr/process/src/lib'

/**
 * 批次列表
 */
export default {
  components: {processContainer, taskContainer, sendNext, endProcess},
  data() {
    const batchId = this.batchId
    return {
      loading: false,
      editAdd:false,
      dialogShow:false,
      //是否在流转中
      /*isProcess: !!this.$route.query.taskId,*/
      /*batchId,*/
      fourDectionTitle: '检测报告',
      //当前选择的全宗
      fond: {},
      //当前选择的门类
      category: {},
      //门类所有的元数据方案
      formDefinition: [],
      //新代码
      fields: [
        {
          prop: 'title',
          label: '题名',
          search: true,
          component: 'text',
          route: true,
          routerPath: '/archive/receive/online/record/detail/item',
          queryProp: 'id'
        },
        {prop: 'archiveCode', label: '电子文件号/档号', search: true, width: 150},
       /* {prop: 'digitaldigest', label: '数字摘要', width: 120},*/
        /*{prop: 'systemName', label: '系统名称', width: 180},
        {prop: 'systemNum', label: '系统编号', width: 100},*/
        {prop: 'status', label: '归档状态', width: 100, dictKey: 'archive.prearchiveStatus', component: 'tag'},
        {
          prop: 'testStatus', label: '四性检测', width: 80, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '不通过', show: 'danger'},
            '1': {label: '通过', show: 'success'}
          },
        },
        {prop: 'totalCount', label: '检测项', width: 80},
        {prop: 'successCount', label: '成功项', width: 80}
      ],
      /*defaultSearchForm: {batchId},*/
      //是否显示入库按钮
      showRk: false,
      //是否显示编辑目录,删除 按钮
      showEditButton: false,
      //是否显示编辑目录弹窗
      editDialogShow: false,
      //先写死
      formFieldList: [
        {
          code: "SAVE_TERM", id: "1b8036b0-ce91-4f7b-9aea-85184d19d111", labelWidth: 100,
          meta: {}, name: "保管期限", remarks: "200", type: "input"
        },
        {
          code: "TITLE", id: "78cf137c-14a6-4a56-9bbe-0b1366c900a2", labelWidth: 100,
          meta: {}, name: "题名", remarks: "510", type: "input"
        },
        {
          code: "VINTAGES", id: "a254dfdc-394c-4009-b2d3-9f831b817eb6", labelWidth: 100,
          meta: {}, name: "年度", remarks: "200", type: "input"
        },
        {
          code: "ARCHIVE_CODE", id: "f17b89d2-ec6a-42a0-ae01-fbd86fa5f3f4", labelWidth: 100,
          meta: {}, name: "档号", remarks: "200", type: "input"
        },
        {
          code: "FILETIME", id: "27002f1c-7f4c-42a5-b349-6daae159c998", labelWidth: 100,
          meta: {}, name: "文件形成日期", remarks: "200", type: "input"
        },
        {
          code: "NOTE", id: "fc59f6fb-dfa5-45cc-9ead-1d28c2c30b57", labelWidth: 100,
          meta: {}, name: "备注", remarks: "510", type: "input"
        }
      ],
      form: {},
      defaultParams: {},
    }
  },
  methods: {
    closeForm() {
      this.formDefinition = []
      this.fond = {}
      this.category = {}
      this.dialogShow = false
      this.form = {}
    },
    async $init() {
      this.$http.post('receive/online/detail', {id: this.batchId})
          .then(({data}) => {
            if (data.success) {
              //判断四性检测全部通过才可显示入库按钮
              if (data.data.testStatus === '1' && (data.data.status != 5 && data.data.status != 6)) {
                this.showRk = true
              }
              if (data.data.status != 5 && data.data.status != 6) {
                this.showEditButton = true
              }
            } else {
            }
          })
    },
    //跳转到四性检测报告页面
    toTestReport(row) {
      this.$router.push({
        path: '/archive/receive/online/record/detail/report',
        query: {
          businessId: row.id,
        }
      })
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
        data.data.forEach(item => {
          if (item.default) {
            if (item.arcFormId != null && item.arcFormName != null && item.arcFormId.length > 0) {
              this.formDefinition.push({formDefinitionId: item.arcFormId, formName: item.arcFormName})
            }
            if (item.fileFormId != null && item.fileFormName != null && item.fileFormId.length > 0) {
              this.formDefinition.push({formDefinitionId: item.fileFormId, formName: item.fileFormName})
            }
            if (item.proFormId != null && item.proFormName != null && item.proFormId.length > 0) {
              this.formDefinition.push({formDefinitionId: item.proFormId, formName: item.proFormName})
            }
          }
        })
      }
    },
    cancelBox(){
      this.editAdd=false
    },
    //删除
    async deleteDetail(row) {
      await this.$confirm('此操作将彻底删除该数据无法恢复, 是否继续?', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      })
      this.loading = true
      const {data} = await this.$post('/receive/onlineDetail/deleteDetail', {
        id: row.id
      })
      if (data.success) {
        this.$message.success('删除成功！')
        this.$refs.table.reload()
      } else {
        this.$message.error(data.message)
      }
      this.loading = false

    },
    //编辑目录
    async editForm(row) {
      this.form = {}
      this.defaultParams = {}
      //获取表单数据
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: row.formDefinitionId,
        formDataId: row.formDataId
      })
      if (data.success) {
        this.form = data.data
        this.defaultParams = {
          formDefinitionId: row.formDefinitionId, categoryId: row.categoryId, detailId: row.id
        }
        this.editDialogShow = true
      }
    },
    //编辑目录保存
    async save() {
      const valid = await this.$refs.form.validate()
      if (valid) {
        const url = `/receive/onlineDetail/updateForm`
        const {data} = await this.$post(url, Object.assign(this.form, this.defaultParams))
        if (data.success) {
          this.$message.success("保存成功！")
          this.$refs.table.reload()
          this.editDialogShow = false
        }

      }
    },
    /**
     * 批量退回
     * @returns {Promise<void>}
     */
    async batchReturn() {
      try {
        const msg = await this.$prompt('请填写退回意见', '退回确认')
        /*this.$refs.table.data.loading = true
        setTimeout(() => {
          console.log(msg)
          this.$message.success('退回成功！')
          this.$refs.table.data.loading = false
        }, 1000)*/
        const {data} = await this.$http.post('/receive/online/batch', {
          id: this.routeBatchId
        })
        if (data.success) {
          this.$message.success(data.message);
        } else {
          this.$message.error(data.message);
        }
      } catch (e) {
      }
    },
    addArchives(){
      this.dialogShow=true
/*      const {data} =this.$http.post('/receive/online/addArchives', {
        batchId: this.batchId
      })
      if (data.success) {
        this.$message.success(data.message);
      } else {
        this.$message.error(data.message);
      }*/
    },
    addArchive(){
          this.$http.post('/receive/online/addArchives', {
        batchId: this.batchId,
        categoryId:this.category.id,
        fondId:this.fond.id
      }).then(({data})=>{
            if (data.success) {
              this.dialogShow=false
              this.$message.success(data.message);
            } else {
              this.dialogShow=false
              this.$message.error(data.message);
            }
      })
    },
    /**
     *入库档案
     * @return {Promise<void>}
     */
    async notifyResult(params) {
      if (params.success) {
        this.$message.success('提交入库申请成功！')
        this.$router.back()
      } else {
        this.$message.warning(params.message)
      }
    },
    async sendSaved() {
      this.$message.success('提交成功！')
      this.$router.back()
    },
    async saveEnd() {
      this.$message.success('办结成功！')
      this.$router.back()
    },
    appendParams(form) {
      this.$set(form, 'businessId', this.batchId)
    },
    toTest() {

    }
  },
  computed: {
    batchId() {
      return this.$route.query.batchId || this.$route.query.businessId
    },
    status(){
      return this.$route.query.status
    },
    defaultSearchForm() {
      return {batchId: this.$route.query.batchId || this.$route.query.businessId}
    },
    routeBatchId() {
      return this.$route.query.batchId
    },
    businessId() {
      return this.$route.query.businessId
    },
    isProcess() {
      return !!this.$route.query.taskId
    }
  }
}
</script>
