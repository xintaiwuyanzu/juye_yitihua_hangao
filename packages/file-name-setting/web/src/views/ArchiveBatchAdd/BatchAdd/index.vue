<template>
  <table-index
      :fields="fields"
      path="receive/offline"
      :insert="false"
      :edit="false"
      :delete="false"
      ref="table"
      :defaultSearchForm="defaultSearchForm">
    <template slot-scole='form' slot='search-$btns'>
      <imp-cataloglist @loadData="$refs.table.reload()" ref="imp" style="display: inline-block;"/>
    </template>
    <el-table-column fixed="right" v-slot="{row}" label="操作" align="center" width="270px">
      <el-button type="text" @click="removePer(row.id)" size="mini" width="80">删除</el-button>
      <el-button type="text" @click="report(row)" size="mini" width="60">{{ reportTitle }}</el-button>
      <el-button type="text" @click="updateHook(row.id)" size="mini" width="60"
                 v-show="row.status != 5 && row.status != 6"> 上传原文
      </el-button>
      <el-button type="text" @click="hook(row.id)" size="mini" width="60"
                 v-show="row.status != 5 && row.status != 6"> 挂接原文
      </el-button>
      <el-button type="text" @click="register(row.id)" size="mini" width="80"
                 v-show="row.testStatus == 1 && row.detailNum>0"> 电子档案登记
      </el-button>
    </el-table-column>
    <!-- 挂接报告 -->
    <el-dialog :visible.sync="dialogVisible" :title="reportTitle" width="40%"
               :close-on-click-modal=true
               :modal-append-to-body=false
               :destroy-on-close=true>
      <el-form ref="reportForm" :model="reportForm" label-width="160px">
        <el-form-item label="批量生成数据总数">
          <el-input v-model="reportForm.detailNum" disabled/>
        </el-form-item>
        <el-form-item label="已挂接数量">
          <el-input v-model="reportForm.hookSucessNum" disabled/>
        </el-form-item>
        <el-form-item label="未挂接数量">
          <el-input v-model="reportForm.hookFalseNum" disabled/>
        </el-form-item>
        <el-form-item label="四性检测通过数量">
          <el-input v-model="reportForm.fourSexTestSucessNum" disabled/>
        </el-form-item>
        <el-form-item label="四性检测不通过数量">
          <el-input v-model="reportForm.fourSexTestFalseNum" disabled/>
        </el-form-item>
        <el-form-item label="原文挂接失败数">
          <el-input v-model="reportForm.fileHookFalseNum" disabled/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible=false" type="info">取消</el-button>
      </div>
    </el-dialog>
    <!-- 挂接弹窗 -->
    <el-dialog :visible.sync="hooktDialog.reportDialogVisible" title="挂接原文" width="80%">
      <el-form :model="form" ref="form" label-width="150px">
        <el-form-item label="来源:" prop="fileLocations" required>
          <el-radio-group v-model="form.fileLocations">
            <el-radio label="CLIENT">客户端上传</el-radio>
            <el-radio label="SERVER">服务器</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="上传批次记录:" prop="clientBatchId" v-if="form.fileLocations==='CLIENT'" required>
          <select-async v-model="form.clientBatchId" placeholder="选择客户端上传批次记录" style="width: 60%" clearable
                        url="/dataBatch/page" :params="dataBatchParams" labelKey="batchName"/>
        </el-form-item>
        <el-form-item label="原文位置:" prop="filePath" v-else-if="form.fileLocations==='SERVER'" required>
          <el-autocomplete v-model="form.filePath" clearable placeholder="请输入原文位置" style="width: 60%"
                           :fetch-suggestions="querySearch"/>
        </el-form-item>
        <el-form-item label="是否删除上传文件" prop="isDeleteFile">
          <el-tooltip :content="'' + form.isDeleteFile" placement="top">
            <el-switch
                v-model="form.isDeleteFile"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-tooltip>
        </el-form-item>
        <el-form-item label="挂接方式:" prop="coverOrAdd" required>
          <select-async v-model="form.coverOrAdd" placeholder="请选择挂接方式" style="width: 60%"
                        :mapper="hookOptions"
                        valueKey="value"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="hooktDialog.reportDialogVisible=false" type="info">取消</el-button>
        <el-button type="primary" @click="doHook" v-loading="loading">开始挂接</el-button>
      </div>
    </el-dialog>
  </table-index>
</template>
<script>
import impCataloglist from './impCataLog'
import util from "@dr/framework/src/components/login/util";
import {encode} from 'js-base64'
import {useUser} from "@dr/framework/src/hooks/userUser";

/**
 * 离线接收
 * 批次详情列表
 */
const hookStatusMapper = {'0': {label: '未挂接', show: 'info'}, '1': {label: '已挂接', show: 'success'}}
export default {
  components: {impCataloglist},
  setup() {
    return useUser();
  },
  data() {
    return {
      defaultSearchForm: {typologic: "BatchAdd"},
      dialogVisible: false,
      loading: false,
      secret: [],
      reportTitle: '数据报告',
      reportForm: {},
      dataBatchParams: {page: false, batchType: 0},
      hooktDialog: {
        reportDialogVisible: false
      },
      form: {isDeleteFile: false, fileLocations: 'CLIENT', filePath: '', clientBatchId: ''},
      hookOptions: [{
        value: 'COVER',
        label: '清除现有原文并挂接'
      }, {
        value: 'ADD',
        label: '追加'
      }],
      fileLocations: [{
        value: 'CLIENT',
        label: '客户端上传'
      }, {
        value: 'SERVER',
        label: '服务器'
      }],
      //新格式
      fields: [
        {
          prop: 'batchName',
          label: '批量添加批次名称',
          align: 'left',
          search: true,
          component: 'text',
          route: true,
          routerPath: '/ArchiveBatchAdd/BatchAdd/details',
          width: '180',
        },
        {
          prop: 'status', label: '目录状态', width: '80', align: 'left',
          component: 'tag',
          mapper: {'-1': '已失效', '0': '执行中', '1': '导入成功', '2': '失败', '4': '-', 5: '入库审核中', 6: '已入库'}
        },
        {prop: 'detailNum', label: '目录数量'},
        {prop: 'startDate', label: '目录接收开始时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '140'},
        {prop: 'endDate', label: '目录接收结束时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '140'},
        {
          prop: 'hookStatus',
          label: '挂接状态',
          width: '80',
          component: 'tag',
          showTypeKey: 'show',
          mapper: hookStatusMapper,
        },
        {prop: 'hookSuccessNum', label: '已挂接数量', width: 90},
        /*{prop: 'hookFalseNum', label: '未挂接数量', width: 90},*/
        {prop: 'hookStartTime', label: '原文挂接开始时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '140'},
        {prop: 'hookEndTime', label: '原文挂接结束时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: '140'},
        {
          prop: 'testStatus', label: '四性检测', width: 70, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '不通过', show: 'danger'},
            '1': {label: '通过', show: 'success'},
            '-1': {label: '未执行'},
          },
        },
      ],
      //原文位置缓存数据
      filePathMapper: []
    }
  },
  methods: {
    //接收报告
    async report(row) {
      this.dialogVisible = true
      const {data} = await this.$post('/receive/offline/getReport', {
        batchId: row.id
      })
      if (data.success) {
        this.reportForm = data.data
        this.reportForm.fileHookFalseNum = row.fileHookFalseNum
      }
    },
    //上传原文
    async updateHook(id) {
      const {data} = await this.$post('/receive/offline/getClientUrl')
      if (data.success) {
        this.openDataClient(data.data, id)
      }
    },
    openDataClient(val, id) {
      // 新的挂接方式
      let path = encode(val + '/dataoperationtools.html/#/login?token=' + util.getCookie('dauth') + '&id=' + id + '&userId=' + this.user.id + '&menuPath=/dataClient/inRecord&refId=' + id)
      if (process.env.NODE_ENV === 'production') {
        val = location.origin
        path = encode(val + '/dataoperationtools/index.html#/login?token=' + util.getCookie('dauth') + '&id=' + id + '&userId=' + this.user.id + '&menuPath=/dataClient/inRecord&refId=' + id)
      }
      window.open('dc://openUrl?url=' + path + '&store=true')
    },
    //挂接
    async hook(id) {
      this.form = {isDeleteFile: false, fileLocations: 'CLIENT', filePath: '', clientBatchId: ''}
      this.form.impBatchId = id
      this.hooktDialog.reportDialogVisible = true
    },
    async doHook() {
      this.setIntoLocal(this.form.filePath)
      try {
        const valid = await this.$refs.form.validate()
        if (valid) {
          const {data} = await this.$post('/receive/offline/startHook', {
            type: "FILE",
            ...this.form
          })
          if (data.success) {
            this.$message.success(data.data)
          } else {
            this.$message.error(data.message)
          }
          this.hooktDialog.reportDialogVisible = false
        } else {
          this.$message.error('请填写完整表单')
        }
      } catch {
        this.loading = false
      }
    },
    register(id) {
      this.$router.push({
        path: "/archive/receive/offline/record/register",
        query: {batchId: id}
      }).catch(err => {
        return err
      })
    },
    async handleChange() {
      if (this.form.fileLocations === 'CLIENT') {
        this.form.filePath = ''
      } else if (this.form.fileLocations === 'SERVER') {
        this.form.clientBatchId = ''
      }
    },
    //删除
    removePer(id) {
      this.$confirm('此操作将删除选中的数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$http.post('/receive/offline/delete', {id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success(data.message)
                this.$refs.table.reload(this.defaultSearchForm)
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
      }).catch()
    },

    setIntoLocal(filePath) {
      var noArr = []
      noArr.push({value: filePath, name: filePath})
      if (JSON.parse(localStorage.getItem('filePath'))) {
        let fileList = JSON.parse(localStorage.getItem('filePath'))  // 判断是否已有此条查询记录，若已存在，则不需再存储
        for (let f of fileList) {
          if (f.value === filePath) {
            return
          }
        }
        if (JSON.parse(localStorage.getItem('filePath')).length >= 5) {
          let arr = JSON.parse(localStorage.getItem('filePath'))
          arr.pop()
          localStorage.setItem('filePath', JSON.stringify(arr))
        }
        localStorage.setItem('filePath', JSON.stringify(noArr.concat(JSON.parse(localStorage.getItem('filePath')))))
      } else {  // 首次创建
        localStorage.setItem('filePath', JSON.stringify(noArr))
      }
    },
    querySearch(queryString, cb) {
      cb(JSON.parse(localStorage.getItem('filePath')))
    }
  },
  computed: {
    metadataFalseNum() {
      return this.reportForm.detailNum - this.reportForm.metadataSucessNum
    }
  },
  beforeRouteLeave(to, from, next) {
    this.dialogVisible = false
    this.hooktDialog.reportDialogVisible = false
    next()
  }
}
</script>
<style lang="scss">
.default .el-button--info:hover, .default .el-button--info:focus {
  background-color: rgba(140, 140, 140, 0.8);
  border-color: rgba(140, 140, 140, 0.8);
  color: white;
}

.red .el-button--info:hover, .red .el-button--info:focus {
  background-color: rgba(140, 140, 140, 0.8);
  border-color: rgba(140, 140, 140, 0.8);
  color: white;
}

.green .el-button--info:hover, .green .el-button--info:focus {
  background-color: rgba(140, 140, 140, 0.8);
  border-color: rgba(140, 140, 140, 0.8);
  color: white;
}
</style>
