<template>
  <table-index :fields="fields" path="publicInformation/" pagePath="publicInformation/myInfo" :insert="false"
               :edit="false"
               :delete="false" ref="table"
               :defaultSearchForm="defaultSearchForm">
    <!--        <template slot-scole='form' slot='search-$btns'>-->
    <!--          <el-button type="success" size="mini" width="90" @click="todoing(1)">一键新增单位权重</el-button>-->
    <!--          <el-button type="danger" size="mini" width="90" @click="todoing(0)">清空</el-button>-->
    <!--        </template>-->
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="120" @click="yidu(scope.row)"
                 v-show="scope.row.status==='0'&&scope.row.toId===person.id  ">已读
      </el-button>
      <el-button type="text" size="mini" width="180" @click="fankuishow(scope.row)">调整反馈</el-button>
    </template>
    <!--    调整反馈 -->
    <el-dialog
        title="调整单"
        :visible.sync="fankuiShow"
        width="30%"
        center>
      <el-descriptions title="通知单详情" :column="1">
        <el-descriptions-item label="发送人">{{ row.sendName }}</el-descriptions-item>
        <el-descriptions-item label="接收人">{{ row.toName }}</el-descriptions-item>
        <el-descriptions-item label="消息内容">{{ row.infoContent }}</el-descriptions-item>
      </el-descriptions>
      <el-input
          type="textarea"
          :rows="5"
          placeholder="请输入"
          v-model="reply">
      </el-input>
      <span slot="footer" class="dialog-footer">
       <el-button @click="fankuiShow=false" type="danger">取 消</el-button>
       <el-button type="primary" @click="fankui">保 存</el-button>
      </span>
    </el-dialog>
  </table-index>
</template>

<script>
export default {
  components: {},
  data() {
    return {
      dict: [],
      reply: '',//回复
      fankuiShow: false,
      person: {},//当前登录人
      row: {},
      //新代码
      fields: [
        {prop: 'sendName', label: '发送人', search: false},
        {prop: 'sendUnitName', label: '发送人单位', search: false},
        {prop: 'toName', label: '接收人', search: false},
        {prop: 'toUnitName', label: '接收人单位', search: false},

        {prop: 'infoContent', label: '信息内容', search: false},
        {
          prop: 'infoType', label: '信息类型', fieldType: 'select',
          mapper: {
            'adjust': {label: '调整通知', show: 'primary'},
            'check': {label: '检查通知', show: 'success'}
          },
        },
        {
          prop: 'checkType', label: '检查类型', fieldType: 'select',
          mapper: {
            'zfjcCheck': {label: '执法检查', show: 'primary'},
            'ndjcCheck': {label: '年度检查', show: 'success'}
          },
        },
        {prop: 'sendTime', label: '通知日期', edit: false, dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 170},
        {
          prop: 'status', label: '状态', width: "100", component: 'tag', showTypeKey: 'show', fieldType: 'select',
          mapper: {
            '1': {label: '已读', show: 'primary'},
            '0': {label: '未读', show: 'warning'}
          },
        }
      ],
      defaultSearchForm: {infoType: ''}
    }
  },
  methods: {

    $init() {
      this.$http.post('/login/info').then(({data}) => {
        if (data && data.success) {
          this.person = data.data
        }
      })
    },
    apiPath() {
      return '/publicInformation'
    },
    todoing(v) {
      let url = ''
      let message = ''

      if (v === 0) {
        url = this.apiPath() + '/clear'
        message = '此操作将永久删除下列表内容'
      } else {
        url = this.apiPath() + '/addAuto'
        message = '此操作将新增所有单位权重信息'
      }


      this.$confirm(message + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(url).then(({data}) => {
          if (data && data.success) {
            this.$message.success('操作成功！')
          } else {
            this.$message.error(data.message)
          }
          this.$refs.table.loadData(this.defaultSearchForm);
          this.loading = false
        })
      });


    },
    /**
     * 调整反馈
     * */
    fankuishow(row) {

      this.$router.push({
        path: '/examine/detail',
        query: {id: row.refId, sendId: "0", infoId: row.id, path: row.checkType}
      })


      //
      //
      // this.row = row
      // this.fankuiShow = true
    },
    fankui() {
      this.$http.post('/publicInformation/fankui', {id: this.row.id, reply: this.reply}).then(({data}) => {
        if (data && data.success) {
          this.$message.success('保存成功')
          this.fankuiShow = false
          this.$refs.table.loadData(this.defaultSearchForm);
        }
      })
    },
    yidu(row) {
      row.status = '1'
      this.$http.post('/publicInformation/update', row).then(({data}) => {
        if (data && data.success) {
          this.fankuiShow = false
          this.$message.success('请求成功')
          this.$refs.table.loadData(this.defaultSearchForm);
        }
      })
    }

  },
  computed: {
    /**
     * 映射table-index 选中数据
     * @returns {[]|*|*[]}
     */
    selection() {
      if (this.$refs.table && this.$refs.table.tableSelection) {
        return this.$refs.table.tableSelection
      } else {
        return []
      }
    }
  }
}
</script>
