<template>
  <section>
    <nac-info :back=true title="业务指导详情">
      <el-button type="primary" @click="submit">提交</el-button>
      <el-button @click="openWebimPc" type="success">在线聊天
            </el-button>
      <el-button @click="openWebimPc" type="success">在线聊天
      </el-button>
    </nac-info>
    <div class="index_main">
      <!-- 回复 -->
      <strong style="margin: 10px 0">回复</strong>
      <div style="margin-top: 10px;">
        <el-input
            type="textarea"
            :rows="4"
            placeholder="请输入内容"
            v-model="message">
        </el-input>
      </div>
      <!--   历史记录   -->
      <div style="margin-top: 10px;">
        <el-tooltip :content="recordShow==false?'收起历史记录':'展开历史记录'" placement="top"><strong
            @click="recordShow = !recordShow" style="cursor:pointer">历史记录<i class="el-icon-info"/></strong>
        </el-tooltip>
        <div style="border:2px solid #ededed;margin-top: 10px;height: 200px;overflow: auto;" :hidden="recordShow">
          <div v-for="item in records" class="record" :key="item.id">
            <span v-if="user.id===item.createPerson" class="currentUserName">{{ item.createUserName }}</span>
            <span v-else class="userName">{{ item.createUserName }}</span>
            [{{ $moment(item.createDate).format('YYYY-MM-DD HH:mm:ss') }}]
            <br/>
            <div class="message">{{ item.message }}</div>
          </div>
        </div>
      </div>
      <!-- 档案列表 -->
      <div style="margin-top: 10px;">
        <div slot="header" class="clearfix">
          <strong>档案列表</strong>
        </div>
        <div style="border:2px solid #ededed;margin-top: 10px;">
          <table-index :showTitle="false" :fields="fields" path="businessGuidanceBatchDetail"
                       :default-search-form="defaultSearchForm" :edit="false" :insert="false" :delete="false"
                       ref="table">
          </table-index>
        </div>
      </div>
      <!--在线聊天-->
      <word-pro-dialog ref="wordProDialog" @loadFinish="loadFinish" :modal="false"/>
    </div>
  </section>
</template>

<script>
import {useUser} from "@dr/framework/src/hooks/userUser";
import wordProDialog from "../../wordPro/wordProDialog";

export default {
  name: "index",
  components: {wordProDialog},
  setup() {
    return useUser()
  },
  data() {
    return {
      fields: [
        {
          prop: 'title',
          label: '题名',
          search: true,
          align: 'left',
          component: 'text',
          route: true,
          routerPath: '/archive/metadataAndFileDetail',
          queryProp: ['id', 'formDataId', 'formDefinitionId', {
            refType: 'archive',
            groupCode: 'default',
            watermark: false
          }]
        },
        {prop: 'archiveCode', label: '档号', width: 200},
        {prop: 'fondCode', label: '全宗号', width: 100},
        {prop: 'categoryCode', label: '分类号', width: 140},
        {prop: 'year', label: '年度', width: 80},
      ],
      defaultSearchForm: {batchId: this.$route.query.id},
      message: '',
      recordShow: false,
      records: []
    }
  },
  async mounted() {
    await this.$refs.wordProDialog.getPerson()
    await this.$refs.wordProDialog.load()
    window.addEventListener('message', (e) => {
      if (e.data.type === 'onNewMsg') {
        this.newmsgcount = e.data.msgs
      }
    })
  },
  methods: {
    $init() {
      this.getRecords()
    },
    /*提交*/
    async submit() {
      if (this.message == null || this.message === '') {
        this.$message.warning('请填写内容！')
        return;
      }
      const {data} = await this.$http.post('/businessGuidanceRecord/insert', {
        message: this.message,
        businessId: this.defaultSearchForm.batchId,
        type: 'reply'
      })
      if (data && data.success) {
        this.$message.success('提交成功！')
        await this.getRecords()
      } else {
        this.$message.warning(data.message)
      }
    },
    /*获取历史记录*/
    async getRecords() {
      const {data} = await this.$http.post('/businessGuidanceRecord/page', {
        page: false,
        businessId: this.defaultSearchForm.batchId
      })
      if (data && data.success) {
        this.records = data.data
      } else {
        this.$message.warning(data.message)
      }
    },
    /*在线聊天*/
    openWebimPc() {
      this.$refs.wordProDialog.openUi()
      // window.open('http://192.168.1.144:8002/webim/pc','_blank')
    },
    loadFinish() {
      //this.loading = false
      this.$refs.wordProDialog.loadingColse()
    }
  }
}
</script>

<style lang="scss" scoped>
.currentUserName {
  //background: #f0f4ff;
  color: $--color-success;
  font-weight: bold;
}

.userName {
  //background: #f0f4ff;
  color: $--color-primary-dark-1;
  font-weight: bold;
}

.message {
  padding: 10px 20px 0 20px;
  color: #606266;
}

.record {
  padding: 10px;
}
</style>