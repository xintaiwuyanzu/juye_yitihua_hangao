<template>
  <div v-show="showPage" class="aa">
    <el-dialog :visible.sync="dialogVisible" width="60%" :modal="false"
               v-el-drag-dialog :close-on-click-modal="false" top="5vh">
      <iframe v-show="url" :src="url"></iframe>
      <!--      <div v-else v-loading="true" style="height: 100%;"></div>-->
    </el-dialog>
  </div>
</template>

<script>
import {encode} from 'js-base64'
import elDragDialog from './el-drag-dialog'
import indexMixin from '@/util/indexMixin'
import fromMixin from '@/util/formMixin'

export default {
  name: 'wordProDialog',
  mixins: [indexMixin, fromMixin],
  components: {},
  props: {
    batchId: {type: String}
  },
  data() {
    return {
      dialogVisible: false,
      url: null,
      loading: false,
      account: null,
      showPage: false,
      params:{
        $path:'/archive/metadataAndFileDetail',
        formDataId:'',
        formDefinitionId:'',
        refType:'archive',
        groupCode:'default',
        watermark:'false'
      },
      personId: '',
      token:'',
      gid: '',
      pw:'',
    }
  },
  directives: {elDragDialog},
  methods: {
    openUi(formDefinitionId,formDataId,title) {
      this.createToken()
      this.params.formDefinitionId = formDefinitionId
      this.params.formDataId = formDataId
      this.createGroup(title)
    },
    openSte(gid) {
      this.$http.post('/online/getWorkProToken').then(({data})=>{
        this.token = data.data.data.token
        this.loadNull(gid)
        this.showPage = true
      })

    },
    createGroup(title) {
      this.$http.post('/online/getWorkProGroup', {batchId: this.batchId}).then(({data}) => {
        if (data.success) {
          this.gid = data.data.data.gid;

          let windowPath = window.document.location.href;
          // 获取当前页面主机地址之后的目录，如：/admin/index
          let pathName =  this.$route.path;
          let pos = windowPath.indexOf(pathName);
          // 获取主机地址，如：http://localhost:8080
          let localhostPath = windowPath.substring(0, pos);

          const text = localhostPath+'/home?params='+ encode(JSON.stringify(this.params))
          this.sendMessage(data.data.data.gid, text, data.data.data.name,title)
          this.load()
          this.showPage = true
          this.dialogVisible = true
        }
      })
    },
    sendMessage(val, text, name,title) {
      this.$http.post('/online/getWorkProSend', {groupId: val, text: text, name: name,title:title}).then(({data}) => {
      })
    },
    load() {
      this.showPage = false
      this.dialogVisible = true
      this.url = null
      this.$http.post('/online/getWorkProOpenChat', {GroupId: this.gid}).then(({data}) => {
        if (data && data.success) {
          if (this.account === null) {
            this.getPerson()
          }
          //let baseUrl = data.data.data.base_url+'/webim/pc/#/chat/dialogue'
          //this.url = `${baseUrl}?token=${data.data.data.token}&account=${this.account}`
          this.getByIdPassword()
          let flag = true
          //TODO 写死密码
          //let password = '!QAZ2wsx'
          let type = 'group'
          //let baseUrl = 'http://192.168.1.144:8002/webim/pc/#/chat/dialogue'
          //写死参数
          this.url = `${data.data}?account=${this.account}&token=${this.token}&id=${this.gid}&type=${type}&hide_addressbook=${flag}`
          this.token = ''
        } else {
          this.dialogVisible = false
          this.$message.error(data.msg)
        }
      }).finally(() => {
        //this.$emit('loadFinish')
        this.loadingColse()
      })
    },
    getByIdPassword(){
      this.$http.post('/queryUser/getUser',{personId:this.personId}).then(({data})=>{
        if(data.success){
          this.pw = data.data
        }
      })
    },
    loadNull(gid) {
      this.showPage = false
      this.url = null
      this.$http.post('/online/getWorkProOpenChat', {GroupId: gid}).then(({data}) => {
        if (data && data.success) {
          if(this.account===null){
            this.getPerson(gid,data.data)
          }else {
            this.show(gid,data.data)
          }
          //let baseUrl = data.data.data.base_url+'/webim/pc/#/chat/dialogue'
          //this.url = `${baseUrl}?token=${data.data.data.token}&account=${this.account}`
        } else {
          this.dialogVisible = false
          this.$message.error(data.msg)
        }
      }).finally(() => {
        //this.$emit('loadFinish')
        this.loadingColse()
      })
    },
    loadingColse() {
      this.loading = false
    },
    createToken(){
      this.$http.post('/online/getWorkProToken').then(({data})=>{
          this.token = data.data.data.token
      })
    },
    show(gid,val){
      this.dialogVisible = true
      let flag = true
      this.getByIdPassword()
      //let password = '!QAZ2wsx'
      let type = 'group'
      //let baseUrl = 'http://192.168.1.144:8002/webim/pc/#/chat/dialogue'
      //写死参数
      if(this.account !== null && this.token !== ''){
          this.url = `${val}?account=${this.account}&token=${this.token}&id=${gid}&type=${type}&hide_addressbook=${flag}`
      }
      this.token = ''
    },
    getPerson(gid,val) {
      this.$http.post('/login/info')
        .then(({data}) => {
          if (data.success) {
            this.account = data.data.userCode
            this.personId = data.data.id
            if(gid!=null && gid != ''){
              this.show(gid,val)
            }

          } else {
            this.$message.error(data.message)
          }
        })
    },
  }
}
</script>

<style lang="scss" scoped>
iframe {
  border: none;
  height: 100%;
  width: 100%;
  border-radius: 4px;
}

::v-deep .el-dialog {
  background-color: unset;
  overflow: unset !important;

  .el-dialog__body {
    padding: 0;
    border-radius: 4px;
    height: 600px;
    background-color: #FFFFFF;
  }

  .el-dialog__headerbtn {
    background-color: #FFFFFF;
    border-radius: 50%;
    width: 25px;
    height: 25px;
    position: absolute;
    right: 5px;
    bottom: -10px !important;
    z-index: 9999;

    *:hover {
      color: #00a0e9;
    }
  }

  .el-dialog__header {
    border: none;
    padding: 30px 0 0 0;
  }
}

.el-dialog__wrapper {
  overflow: hidden;
}

@keyframes dialog-fade-in {
  0% {
    transform: translate3d(100%, 100%, 0) scale(0.001);
    opacity: 0;
  }
  25% {
    transform: translate3d(75%, 75%, 0) scale(0.01);
    opacity: 0.25;
  }
  50% {
    transform: translate3d(50%, 50%, 0) scale(0.01);
    opacity: 0.5;
  }
  75% {
    transform: translate3d(25%, 25%, 0) scale(0.1);
    opacity: 0.75;
  }
  100% {
    transform: translate3d(0, 0, 0) scale(1);
    opacity: 1;
  }
}

@keyframes dialog-fade-out {
  0% {
    transform: translate3d(0, 0, 0) scale(1);
    opacity: 1;
  }
  25% {
    transform: translate3d(25%, 25%, 0) scale(0.1);
    opacity: 0.75;
  }
  50% {
    transform: translate3d(50%, 50%, 0) scale(0.01);
    opacity: 0.5;
  }
  75% {
    transform: translate3d(75%, 75%, 0) scale(0.01);
    opacity: 0.25;
  }
  100% {
    transform: translate3d(100%, 100%, 0) scale(0.001);
    opacity: 0;
  }
}

.dialog-fade-enter-active {
  animation: dialog-fade-in .3s
}

.dialog-fade-leave-active {
  animation: dialog-fade-out .3s
}
</style>
