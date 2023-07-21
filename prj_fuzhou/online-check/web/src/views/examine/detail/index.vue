<template>
  <section>
    <nac-info :back=true title="检查单详情">
      <el-button type="success" @click="save(0)">保 存</el-button>
      <el-button type="success" @click="save(1)" v-show="sendId==='1'">保存并通知</el-button>
      <!--      <el-button type="success" @click="save(2)" v-show="sendId==='1'">保存并提交反馈</el-button>-->
      <el-button type="success" @click="gongshi" v-show="sendId==='1'">提交公示</el-button>
      <el-button type="success" @click="gongshi" v-show="sendId==='0'">打印检查报告</el-button>
      <el-button type="success" @click="showDrawer">资料库</el-button>
    </nac-info>
    <div class="index_main">
      <el-descriptions title="检查单详情" :column="3" border style="margin-top: 10px;">
        <el-descriptions-item label="检查人" :span="3" label-class-name="my-label">{{
            row.personName
          }}
        </el-descriptions-item>
        <el-descriptions-item label="检查项" :span="3" label-class-name="my-label">{{
            row.categoryName
          }}
        </el-descriptions-item>
        <el-descriptions-item label="被检查单位" :span="3" label-class-name="my-label">{{
            row.organiseName
          }}
        </el-descriptions-item>

        <el-descriptions-item label="计划检查时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.jihuaTime|datetime
          }}
        </el-descriptions-item>
        <el-descriptions-item label="实际检查时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.shijiTime|datetime
          }}
        </el-descriptions-item>
        <el-descriptions-item label="结果填写时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.jieguoTime|datetime
          }}
        </el-descriptions-item>
        <el-descriptions-item label="整改通知时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.tongzhiTime|datetime
          }}
        </el-descriptions-item>
        <el-descriptions-item label="整改到期时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.zhenggaiTime|datetime
          }}
        </el-descriptions-item>
        <el-descriptions-item label="整改完成时间" label-class-name="my-label" v-show="sendId==='1'">{{
            row.wanchengTime|datetime
          }}
        </el-descriptions-item>

      </el-descriptions>
      <el-descriptions title="室藏档案情况" :column="2" border style="margin-top: 10px;">
        <template slot="extra">
          <el-button type="primary" size="small" @click="loadArriveCount" v-show="sendId==='1'">重新获取</el-button>
        </template>
        <el-descriptions-item label="文书档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.ws }} 卷（件）。
        </el-descriptions-item>
        <el-descriptions-item label="声像档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.sx }} 卷（件）。
        </el-descriptions-item>
        <el-descriptions-item label="会计档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.kj }} 卷（件）。
        </el-descriptions-item>
        <el-descriptions-item label="专业档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.zy }} 卷（件）。
        </el-descriptions-item>
        <el-descriptions-item label="电子档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.dz }} 卷（件）。
        </el-descriptions-item>
        <el-descriptions-item label="其它档案" label-class-name="my-label">2020年度至 2021 年度，共 {{ arriveCount.qt }} 卷（件）。
        </el-descriptions-item>
      </el-descriptions>
      <div style="margin-top: 20px;">
        <div slot="header" class="clearfix">
          <strong class="title">检查项</strong>
          <el-button style="float: right" type="primary" size="mini" icon="el-icon-circle-plus" v-show="sendId==='1'"
                     @click="addBar">添加
          </el-button>
        </div>
        <el-table
            border
            :data="rows"
            style="width: 100%">
          <!--          <el-table-column prop="categoryName" label="检查项" show-overflow-tooltip></el-table-column>-->
          <el-table-column prop="categoryName" label="检查项" align="center" width="200px">
            <template slot-scope="scope">
              <span v-if="scope.row.id!=='' ">{{ scope.row.categoryName }}</span>
              <div v-else>
                <el-cascader

                    style="width: 90%"
                    v-model="scope.row.categoryId"
                    :options="options"
                    :props="props"
                    :show-all-levels="false"
                    clearable>

                </el-cascader>
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="personName" label="检查人" width="100px"/>
          <el-table-column label="检查结果" width="270">
            <template slot-scope="scope">
              <el-radio-group v-model="scope.row.categoryResult" :disabled="sendId==='0'">
                <el-radio :label="0">待检查</el-radio>
                <el-radio :label="1">通过</el-radio>
                <el-radio :label="2">不通过</el-radio>
              </el-radio-group>
            </template>
          </el-table-column>
          <el-table-column label="检查情况" width="300">
            <template slot-scope="scope">
              <el-input
                  type="textarea"
                  :rows="1"
                  :disabled="sendId==='0'"
                  placeholder="请输入内容"
                  v-model="scope.row.situation">
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="整改意见" width="300">
            <template slot-scope="scope">
              <el-input
                  type="textarea"
                  :rows="1"
                  :disabled="sendId==='0'"
                  placeholder="请输入内容"
                  v-model="scope.row.opinion">
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="整改反馈" width="300">
            <template slot-scope="scope">
              <el-input
                  type="textarea"
                  :rows="1"
                  :disabled="sendId==='0'"
                  placeholder="请输入内容"
                  v-model="scope.row.reply">
              </el-input>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="90">
            <template slot-scope="scope">
              <el-button @click="deleteRow(scope.row)" type="danger">删 除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div>
          <strong>整改意见</strong>
          <el-input
              type="textarea"
              :rows="4"
              :disabled="sendId==='0'"
              placeholder="请输入内容"
              v-model="opinion">
          </el-input>
        </div>
        <div>
          <strong>反馈消息</strong>
          <span style="float: right" v-show="sendId==='1'">
            <el-button @click="fileListDialog = true" type="success">附件</el-button>
          </span>

          <el-input
              type="textarea"
              :rows="4"
              :disabled="sendId==='0'"
              placeholder="请输入内容"
              v-model="reply">
          </el-input>
        </div>
      </div>
    </div>
    <file-list refType="check" :formDataId="pId" :deleter="false" :print="false" :upload="true" transform
               style="margin-top: 5px"
               width="50%" v-if="fileListDialog" :title="title" :uploadButton="uploadButton" :uploadText="uploadText"
               :selectionButton="selectionButton"/>
    <el-drawer
        title="资料列表"
        :visible.sync="drawer" size="50%">
      <table-render class="table-container"
                    :columns="columns"
                    :data="pdfList" style="height:80vh;display: flex;flex-direction: column">
        <el-table-column label="操作" width="120" header-align="center" align="center">
          <template v-slot="scope">
            <el-link type="success" @click="downLoad(scope.row)">下载</el-link>
          </template>
        </el-table-column>
      </table-render>
    </el-drawer>
  </section>
</template>

<script>
import {useUser} from "@dr/framework/src/hooks/userUser";
export default {
  name: "index",
  setup() {
    return useUser()
  },
  data() {
    return {
      props: {multiple: false, value: 'id',},
      options: [],
      row: [],
      rows: [],
      opinion: '',
      reply: '',
      infoId: '',
      pId: '',
      path: '',
      //附件外键
      dataId: '',
      //各类档案数量
      arriveCount: {},
      sendId: '0',

      //附件相关
      fileListDialog: false,
      title: '附件',
      uploadButton: '上传附件',
      uploadText: '上传附件',
      selectionButton: '选取附件',

      //资料库相关
      drawer: false,
      columns: [
        {prop: 'name', label: '文件名称'},
        {prop: 'suffix', label: '文件类型', width: '100'},
        {prop: 'saveDate', label: '上传日期', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        {prop: 'description', label: '备注', width: '100'},
      ],
      pdfList: [],
      fileInfo: {},

    }
  },

  mounted() {
    this.pId = this.$route.query.id
    this.infoId = this.$route.query.infoId
    this.sendId = this.$route.query.sendId
    this.path = this.$route.query.path
    this.loadDetail()
    this.loadArriveCount()
  },
  methods: {
    loadDetail() {
      this.$http.post(this.path + '/detail', {id: this.pId}).then(({data}) => {
        if (data && data.success) {
          this.row = data.data
          this.opinion = this.row.opinion
          this.reply = this.row.reply
        }
      })
      // this.resultShow = true
      this.rows = []
      this.$http.post(this.path + 'Result/page', {page: false, pId: this.pId}).then(({data}) => {
        if (data && data.success) {
          this.rows = data.data
          this.rows.sort((a, b) => {
            return Number(a.categoryName.substring(0, 3)) - Number(b.categoryName.substring(0, 3))
          })
        }
      })
      this.$http.post('/jdzdCategory/cateTree', {pId: this.pId}).then(({data}) => {
        if (data && data.success) {
          this.options = data.data
        }
      })

    },
    /**
     * 保存更新检查单
     */
    save(v) {
      let s = JSON.stringify(this.rows);
      this.$http.post(this.path + '/updateResult', {
        pId: this.row.id,
        result: s,
        param: v,
        opinion: this.opinion,
        reply: this.reply,
        infoId: this.infoId,
      }).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.message)
        }
        this.reply = ''
        this.opinion = ''
        this.loadDetail()
      })
    },

    //公示检查单
    gongshi() {
      let url = 'api/' + this.path + '/expgs?id=' + this.row.id
      window.open(url)
    },
    //添加一行
    addBar() {
      let row = {categoryName: '', categoryResult: 0, reply: '', opinion: '', pId: this.row.id, id: ''}
      this.rows.push(row)
    },
    //获取各类档案数量
    loadArriveCount() {
      this.$http.post(this.path + '/loadArriveCount').then(({data}) => {
        if (data && data.success) {
          this.arriveCount = data.data
        }
      })
    },
    //附件
    deleteRow(row) {

      let arr = this.rows;
      arr.filter(function (val, index) {
        if (val === row) {
          arr.splice(index, 1)
        }
      })
      this.rows = arr
    },
    showDrawer() {
      this.drawer = true
      this.openFile()
    },
    //获取table列内容
    async openFile() {
      this.fileDialog = true
      this.loading = true
      const {data} = await this.$http.post('files/list', {
        refId: this.user.id,
        refType: 'zfjcCheck',
      })
      if (data.success) {
        if (data && data.success) {
          this.pdfList = data.data
        }
      }
      this.loading = false
    },
    async downLoad(row){
      window.open(`api/files/downLoad/${row.id}?download=false`, "_blank")
    }
    /*indexOf(val) {
      for (var i = 0; i < this.length; i++) {
        if (this[i] == val) return i;
      }
      return -1;
    },
    remove(val) {
      var index = this.indexOf(val);
      if (index > -1) {
        this.splice(index, 1);
      }
    },*/
  }
}
</script>

<style>
.my-label {
  background: #E1F3D8;
}

.my-content {
  background: #FDE2E2;
}

.el-descriptions__header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.el-textarea.is-disabled .el-textarea__inner {
  background-color: #f5f7fa;
  border-color: #E4E7ED;
  color: #303133;
  cursor: not-allowed;
}

.el-radio__input.is-disabled + span.el-radio__label {
  color: #303133;
  cursor: not-allowed;
}

.el-radio__input.is-disabled.is-checked .el-radio__inner {
  background-color: #ff0000;
  border-color: #ff0000;
}
</style>