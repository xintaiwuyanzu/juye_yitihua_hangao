<template>
  <div class="topindex">
    <div class="wrap">
      <div class="line1">
        <div class="line-1 line11" @click="gotaskTodo()">
            <img src="./img/taskTodo.png" class="line-icon"/>
          <span class="line-title1">
            我的待办
            <sup class="tips" v-if="this.taskTodo > 0">{{taskTodo}}</sup>
          </span>
        </div>
        <div class="line-2 line11" @click="goguidance()">
            <img src="./img/guidance.png" class="line-icon"/>
          <span class="line-title1">
            业务指导
            <sup class="tips" v-if="this.guidance > 0">{{guidance}}</sup>
          </span>
        </div>
        <div class="line-3 line11" @click="gohandover()">
            <img src="./img/handover.png" class="line-icon"/>
          <span class="line-title1">
            档案移交与接收
            <sup class="tips" v-if="this.handover > 0">{{handover}}</sup>
          </span>
        </div>
        <div class="line-4 line11" @click="goElectronicBorrowing()">
            <img src="./img/zdtj.png" class="line-icon"/>
          <span class="line-title1">
            电子档案借阅
            <sup class="tips" v-if="this.ElectronicBorrowing > 0">{{ElectronicBorrowing}}</sup>
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "skipButton",
  data() {
    return {
      taskTodo:'',
      guidance:'',
      handover:'',
      ElectronicBorrowing:''
    }
  },
  methods:{
    $init(){
      this.tasksum()
      this.guidancesum()
      this.handoversum()
      this.ElectronicBorrowingsum()
    },
    gotaskTodo(){
      this.$router.push('/process/taskTodo')
    },
    goguidance(){
      this.$router.push('/businessGuidanceBatch')
    },
    gohandover(){
      this.$router.push('/manage/handover')
    },
    goElectronicBorrowing(){
      this.$router.push('/ElectronicBorrowing/applying')
    },
    tasksum(){
      this.$http.post('/processTaskInstance/page').then(({data}) => {
        if (data.success) {
          this.taskTodo = data.data.total
        }
      })
    },
    guidancesum(){
      this.$http.post('/businessGuidanceBatch/total').then(({data}) => {
        if (data.success) {
          this.guidance = data.data[0].id
        }
      })
    },
    handoversum(){
      this.$http.post('/manage/handover/total').then(({data}) => {
        if (data.success) {
          this.handover = data.data[0].id
        }
      })
    },
    ElectronicBorrowingsum(){
      this.$http.post('/Registration/processPage').then(({data}) => {
        if (data.success) {
          this.ElectronicBorrowing = data.data.data.length
        }
      })
    }
  },
}
</script>
<style lang="scss" scoped>

.topindex {
  width: 100%;
  margin: 0 0;
  padding: 0;
  font-family: "微软雅黑";
}

.wrap {
  //width: 30%;
  //background-color: #eeeeee;
  padding-left: 5px;
  padding-right: 5px;
  display: flex;
  flex-direction: column;
}

// 上部样式
.line1 {
  flex: 1;
  min-width: 976px;
  height: 500px;
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

// 统一样式
.topindex .wrap .line1 .line11 {
  line-height: 110px;
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  background-color: #25b862;
  border-radius: 4px;
  cursor:pointer;
}
.tips{
  vertical-align:super;
  background-color: #e20c0c;
  color: white;
  padding: 2px;
  font-size: 8px;
  height: 18px;
  border-radius: 10px;
  letter-spacing:1px;
}
.topindex .wrap .line1 .line-1 {
  height: 110px;
  background-color: #42b9f1;
  border-radius: 4px;
  margin-right: 15px;
  box-shadow: 0 3px 12px #9fe0ff;
}

.topindex .wrap .line1 .line-2 {
  height: 110px;
  background-color: #57c795;
  border-radius: 4px;
  margin-right: 15px;
  box-shadow: 0 3px 12px #a8ffd8;
  float: left;
}

.topindex .wrap .line1 .line-3 {
  height: 110px;
  background-color: #fea64f;
  border-radius: 4px;
  margin-right: 15px;
  box-shadow: 0 3px 12px #ffd2a5;
  float: left;
}

.topindex .wrap .line1 .line-4 {
  height: 110px;
  background-color: #ad8cf4;
  border-radius: 4px;
  box-shadow: 0 3px 12px #d1bcff;
  float: left;
}
.line-title1 {
  width: auto;
  font-size: 1.2rem;
  letter-spacing:2px;
  //text-shadow: 0 2px 3px #616266;
  text-align: center;
  color: #ffffff;
  margin-left: 10px;
}

.line-icon {
  display: inline-block;
  height: 2rem;
}


</style>