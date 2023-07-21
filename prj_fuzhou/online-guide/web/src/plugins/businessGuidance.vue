<template>
  <el-card>
    <strong style="margin: 10px 0">聊天记录</strong>
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
    <div style="padding-top: 10px;" v-loading="guidance.loading" v-if="!detailId">
<!--      <form-render label-width="80px" :fields="guidanceApplicationFields"
                   :model="guidanceApplicationForm"
                   ref="guidanceApplicationForm">
      </form-render>-->
      <!--  <div style="border:2px solid #ededed;margin-top: 10px;min-height:40px;max-height: 400px;overflow: auto;">
      --  <div v-for="item in records" class="record" :key="item.id">
            <span v-if="user.id===item.createPerson" class="currentUserName">{{ item.createUserName }}</span>
            <span v-else class="userName">{{ item.createUserName }}</span>
            [{{ $moment(item.createDate).format('YYYY-MM-DD HH:mm:ss') }}]
            <br/>
            <div class="message">{{ item.message }}</div>
          </div>

      </div>-->
      <el-input
          placeholder="请输入问题"
          v-model="description"
          type="textarea"
          autosize
          /><br/>
      <div slot="footer" style="text-align: right">
<!--        <el-button type="primary" @click="guidanceSubmit">发起申请-->
<!--        </el-button>-->
      <!--  <el-button @click="openWebimPc" type="success" size="medium" icon="el-icon-chat-round">在线聊天
        </el-button>
        <el-button @click="openWebPage" icon="el-icon-service" type="primary" size="medium">智能客服</el-button>-->
        <el-button @click="openWebimPc" type="primary" size="medium">提交问题</el-button>
      </div>
    </div>
    <!--回复 v-if="user.id===askerId"-->
    <div style="padding-top: 10px;" v-if="detailId">
<!--      <form-render label-width="80px" :fields="replyFields" ref="replyForm" :model="replyForm"/>-->
<!--      <div slot="footer" style="text-align: right">-->
<!--        <el-button type="primary" @click="replySubmit">回 复</el-button>-->
<!--      </div>-->
    </div>
        <word-pro-dialog ref="wordProDialog" :batchId="batchId" @loadFinish="loadFinish" :modal="false"/>
        <questions-answers  ref="questionsAnswers" :modal="false" ></questions-answers>
  </el-card>
             <!--在线聊天-->
</template>

<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail"
import {useUser} from "@dr/framework/src/hooks/userUser"
import wordProDialog from "../views/wordPro/wordProDialog";
import questionsAnswers from "../views/Intelligent/questionsAnswers";
export default {
  name: "errorCorrection",
  extends: abstractArchiveDetail,
  components: {wordProDialog,questionsAnswers},
  setup() {
    return useUser();
  },
  data() {
    return {
      newmsgcount: 0,
      replyForm: {},
      guidance: {
        loading: false
      },
      description:'',
      dialogVisible:false,
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
      batchId: '',
    };
  },
  watch: {
    formData() {
      this.getRecords()
    }
  },
  methods: {
    $init() {
      this.getRecords()
    },
    wordProGetGroup(groupId){
      this.$http.post('/online/wordProGroupGet',{groupId:groupId}).then(({data})=>{
        if(data.data === 0){

        }
      })
    },
      judgeExistence(){
      this.$http.post('/businessGuidanceBatch/judgeExistence', {
        formDefinitionId:this.$route.query.formDefinitionId,
            formDataId: this.$route.query.formDataId}).then(({data})=>{
              if(data.data === '0'){
                this.$confirm('确认发起业务指导?', '提示', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
                }).then(() => {
                this.guidanceSubmit()
                }).catch(() => {
                  this.$message({
                    type: 'info',
                    message: '已取消'
                  });
                  this.description=''
                });
              }else {
                this.$refs.wordProDialog.openSte(data.data)
              }
      })
    },
    openWebPage(){
      this.$refs.questionsAnswers.openWebPage()
    },
    openWebimPc() {
          this.judgeExistence()

      // window.open('http://192.168.1.144:8002/webim/pc','_blank')
    },
    /*获取历史记录*/
    async getRecords() {
      const {data} = await this.$http.post('/businessGuidanceRecord/page', {
        page: false,
        businessId: this.formData.id
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
    /*发起指导申请*/
    async guidanceSubmit() {
      this.guidance.loading = true
      //const data = await this.$refs.guidanceApplicationForm.submit('/businessGuidanceBatch/guidance', {
        const data = await this.$http.post('/businessGuidanceBatch/guidance', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId,
        description:this.description
      })
      if (data && data.data.success) {
        this.$message.success("提交成功！")
        this.batchId = data.data.data.batchId
        this.description=''
        await this.getRecords()
        // 调用
        //await this.$refs.wordProDialog.openUi(this.$route.query.formDefinitionId,this.$route.query.formDataId,data.data.data.title)
      } else {
        console.log(data.data)
        console.log('执行')
        this.$message.warning(data.data.message)
        this.description=''
      }
      this.guidance.loading = false
    },
    /*回复*/
    async replySubmit() {
      const data = await this.$refs.replyForm.submit('/businessGuidanceRecord/insert', Object.assign({
        businessId: this.formData.id,
        detailId: this.detailId,
        type: 'reply'
      }, this.replyForm))
      if (data && data.success) {
        this.$message.success('提交成功！')
        await this.getRecords()
      } else {
        this.$message.warning(data.message)
      }
    },

    loadFinish() {
      this.$refs.wordProDialog.loadingColse()
      //this.loading = false
    }
  },
  computed: {
    /*只能在管理库或暂存库进行发起申请，只能在业务指导中待办任务中进行回复*/
    detailId() {
      return this.$route.query.id
    }
  },
  async mounted() {
    console.log(this.$refs.wordProDialogog)
    await this.$refs.wordProDialog.getPerson()
    await this.$refs.wordProDialog.load()
    window.addEventListener('message', (e) => {
      if (e.data.type === 'onNewMsg') {
        this.newmsgcount = e.data.msgs
      }
    })
  },
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
