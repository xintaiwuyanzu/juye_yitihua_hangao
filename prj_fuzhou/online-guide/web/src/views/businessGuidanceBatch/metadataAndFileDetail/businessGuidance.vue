<template>
  <el-card>
    <strong style="margin: 10px 0">问题</strong>
    <div style="margin-top: 10px;">
      <div style="border:2px solid #ededed;margin-top: 10px;min-height:100px;max-height: 400px;overflow: auto;">
        <div v-for="item in records" class="record" :key="item.id">
          <span v-if="user.id===item.createPerson" class="currentUserName">{{ item.createUserName }}</span>
          <span v-else class="userName">{{ item.createUserName }}</span>
          [{{ $moment(item.createDate).format('YYYY-MM-DD HH:mm:ss') }}]
          <br/>
          <div class="message">{{ item.message }}</div>
        </div>
      </div>
    </div>
    <!--指导申请-->
<!--    <div style="padding-top: 10px;" v-loading="guidance.loading" v-if="!detailId">-->
<!--      <form-render label-width="80px" :fields="guidanceApplicationFields"-->
<!--                   :model="guidanceApplicationForm"-->
<!--                   ref="guidanceApplicationForm">-->
<!--      </form-render>-->
<!--      <div slot="footer" style="text-align: right">-->
<!--        <el-button type="primary" @click="guidanceSubmit">发起申请-->
<!--        </el-button>-->
<!--      </div>-->
<!--    </div>-->
    <!--回复 v-if="user.id===askerId"-->
    <div style="padding-top: 10px;" v-if="detailId">
      <form-render label-width="80px" :fields="replyFields" ref="replyForm" :model="replyForm"/>
    <!--  <el-input
          placeholder="请输入回答"
          v-model="description"
      /><br/>-->
      <div slot="footer" style="text-align: right">
       <!-- <el-button v-show="showStatus !== '2'" @click="openWebimPc" type="success" size="medium" icon="el-icon-chat-round">在线聊天</el-button>
        <el-button @click="openWebPage" icon="el-icon-service" type="primary" size="medium">智能客服</el-button>-->
        <el-button type="primary" @click="replySubmit(false)">回 复</el-button>
<!--        <el-button type="primary" @click="replySubmit(true)">回复并下一条</el-button>-->
      </div>
    </div>
    <word-pro-dialog ref="wordProDialog" :batchId="batchId" @loadFinish="loadFinish" :modal="false"/>
    <questions-answers  ref="questionsAnswers" :modal="false" ></questions-answers>
  </el-card>
</template>

<script>
import {useUser} from "@dr/framework/src/hooks/userUser";
import wordProDialog from "../../../views/wordPro/wordProDialog";
import questionsAnswers from "../../Intelligent/questionsAnswers";
export default {
  name: "errorCorrection",
  components: {wordProDialog,questionsAnswers},
  setup() {
    return useUser();
  },
  data() {
    return {
      replyForm: {},
      guidance: {
        loading: false
      },
      description:'',
      guidanceApplicationForm: {description: ''},
      guidanceApplicationFields: {
        description: {
          label: '问题',
          align: 'center',
          type: 'textarea',
          autosize: {minRows: 4, maxRows: 8},
          width: 10,
          required: true
        }
      },
      replyFields: {
        message: {
          label: '回复内容',
          align: 'center',
          type: 'textarea',
          autosize: {minRows: 4, maxRows: 8},
          width: 10,
          required: true
        },
      },
      records: [],
      askerId: '',
      batchId:'reply',
      account:''
    };
  },
  props: {
    formDefinitionId: {type: String},
    formDataId: {type: String},
    gid: {type: String},
    showStatus: {type: String}
  },
  methods: {
    $init() {
      console.log('123',this.showStatus)
      this.getRecords()
    },
    loadFinish() {
      this.$refs.wordProDialog.loadingColse()
      //this.loading = false
    },
    openWebPage(){
      this.$refs.questionsAnswers.openWebPage()
    },
    /*获取历史记录*/
    async getRecords() {
      const {data} = await this.$http.post('/businessGuidanceRecord/page', {
        page: false,
        businessId: this.formDataId
      })
      if (data && data.success) {
        this.records = data.data
        if (this.records.length > 0) {
          this.askerId = this.records[0].sendUserId
        }
      } else {
        this.$message.warning(data.message)
      }
    },
    openWebimPc() {
       this.$refs.wordProDialog.openSte(this.gid)
      // window.open('http://192.168.1.144:8002/webim/pc','_blank')
    },
    /*发起指导申请*/
    async guidanceSubmit() {
      this.guidance.loading = true
      const data = await this.$refs.guidanceApplicationForm.submit('/businessGuidanceBatch/guidance', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId
      })
      if (data && data.success) {
        this.$message.success("提交成功！")
        await this.getRecords()
      } else {
        this.$message.warning(data.message)
      }
      this.guidance.loading = false
    },
    /*回复*/
    async replySubmit(isNext) {
      const data = await this.$refs.replyForm.submit('/businessGuidanceRecord/insert', Object.assign({
        businessId: this.formDataId,
        detailId: this.detailId,
        type: 'reply',
      }, this.replyForm))
      if (data && data.success) {
        this.$message.success('提交成功！')
        await this.getRecords()
        this.$emit("next")
      } else {
        this.$message.warning(data.message)
      }
    },
      getPerson() {
        this.$http.post('/login/info')
            .then(({data}) => {
              if (data.success) {
                this.account = data.data.userCode
                this.personId = data.data.id
              } else {
                this.$message.error(data.message)
              }
            })
      },

  },
  computed: {
    /*只能在管理库或暂存库进行发起申请，只能在业务指导中待办任务中进行回复*/
    detailId() {
      return this.$route.query.id
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
