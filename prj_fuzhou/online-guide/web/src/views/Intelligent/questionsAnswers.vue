<template>
  <div v-show="showPage" >
    <el-dialog :visible.sync="dialogVisible" :modal="false"
               v-el-drag-dialog :close-on-click-modal="false" top="8vh">
      <iframe v-show="goUrl" :src="goUrl" name="iframe"></iframe>
      <!--      <div v-else v-loading="true" style="height: 100%;"></div>-->
<!--      <iframe v-show="url" :src="url"></iframe>-->
<!--      <form id="idFrame" v-show="false" :action="goUrl" target="iframe"></form>-->
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from "../wordPro/el-drag-dialog";
import indexMixin from '@/util/indexMixin'
import util from '@dr/framework/src/components/login/util'
import fromMixin from '@/util/formMixin'
import {Loading} from "element-ui";

export default {
  name: 'questionsAnswers',
  directives: {elDragDialog},
  mixins: [indexMixin, fromMixin],
  components: {},
  data() {
    return {
      dialogVisible: false,
      url: null,
      goUrl:null,
      showPage: false,
    }
  },
  methods:{

    signIn(val){
      this.dialogVisible = false

      this.goUrl = val+'?token='+util.getToken()

      //如果不行用post
      // setTimeout(()=>{
      //   document.getElementById("idFrame").submit();
      // },1000)
      this.dialogVisible = true
      this.showPage = true
    },
    getUrl(){
      this.$http.post('/intelligent/getUrl').then(({data})=>{
        if(data.success){
          this.signIn(data.data)
        }
      })
    },
    openWebPage(){
      this.getUrl()
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
  left: 700px;
  width: 500px;
  .el-dialog__body {
    padding: 0;
    border-radius: 4px;
    height: 600px;
    background-color: #FFFFFF;
    //position:absolute;
    //right:0
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
    padding: 20px 0 0 0;
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
