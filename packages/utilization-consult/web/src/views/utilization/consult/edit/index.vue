<template>
  <section>
    <nac-info back title="查档登记">
      <el-button type="primary" v-if="hasRole(consultRole)" @click="startF">刷卡</el-button>
<!--      <el-button type="primary" v-if="!hasRole(consultRole)" @click="checkSave(true)">保存并申请</el-button>-->
      <el-button type="primary" @click="checkSave(true)">保存并查档</el-button>
    </nac-info>
    <div class="index_main">
      <form-render :fields="fields" :model="form" :inline="true" label-width="120px" ref="form" :rules="rules"/>
    </div>
  </section>
</template>
<script>
/**
 * 查档登记页面
 */
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";
import {consultRole, nationGroup} from '../constants'

export default {
  data() {
    return {
      webSocket: {},
      consultRole,
      fields: renderFields,
      form: {
        ethnic: '汉族',
        idType: 'identityCard',
        fileType: [],
        status:0
      },
      rules:{
        phone:[{pattern:/^[1][3,4,5,7,8][0-9]{9}$/,message:'请输入正确的手机号',trigger:'blur'}],
        // idNo:[{pattern:/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/,message:'请输入正确的证件号码',trigger:'blur'}]
      },
    }
  },
  setup() {
    return useArchiveCar()
  },
  methods: {
    //捷宇高拍仪读取身份证  win需要安装插件
    startF(){
      // 地址
      this.webSocket = new WebSocket('ws://localhost:1818');
      this.webSocket.onopen = (e) => {
        this.socketOnOpen()
      }
      this.send()
      this.webSocket.onmessage = (e) => {
        this.websocketonmessage(e)
      }
    },
    send(){
      setTimeout(() => {
        console.log(this.webSocket.readyState)
        if (this.webSocket.readyState === 1) {
          this.webSocket.send('ReadCard(1001,C:\\Doccamera\\)')
          console.log(this.webSocket)
        }
      },1000)
    },
    socketOnOpen(e){
      console.log('开始')
    },
    websocketonmessage(e){
      this.onMessage(e)

    },
    onMessage(event){
      if (event.data.indexOf('BeginReadCard')>=0) {
        this.ShowLog(event.data.replace('BeginReadCard','').replace('EndReadCard',''));
      }
    },
     ShowLog(logMsg){
      const msg = JSON.parse(logMsg)
       if(msg.result === 1){

         this.form.ethnic = msg.Folk
         this.form.idNo = msg.IdNo
         this.form.userName = msg.Name
         this.form.birthday = new Date(msg.BirthDate.substr(0,4)+'-'+msg.BirthDate.substr(4,2)+'-'+msg.BirthDate.substr(6,2)).getTime()
         this.form.sex = msg.Sex === "男"? '1':'2'
         this.form.valid = new Date(msg.Valid.split('-')[1].replaceAll('.','-')).getTime()
         this.form.idType = '1'
         this.form.agency = msg.Agency
       }else {
         this.$message.warning('请重新放入证件')
       }
       this.webSocket.close()
    },
    async checkSave(routeSearch = false) {
      if(routeSearch){
        this.form.status=1
      }
      const result = await this.$refs.form.submit('/utilization/consult/insert')
      if (result.success) {
        this.$message.success('保存成功')
        if (routeSearch) {
          this.archiveCarData.archiveCar.id = result.data.id
          await this.$router.push({path: '/utilization/search', query: {keyword: result.data.userName}})
        }
      } else {
        this.$message.error(result.message)
      }
    }
  }
}
export const renderFields = {
  userInfo: {
    fieldType: 'object',
    header: '用户基本信息',
    //role: consultRole,
    children: {
      userName: {label: '姓名', required: true},
      birthday: {label: '出生日期', fieldType: 'date', required: true},
      ethnic: {label: '民族', fieldType: 'select', mapper: nationGroup, filterable: true},
      sex: {label: '性别', fieldType: 'radio', dictKey: 'gender'},
      idNo: {label: '有效证件号', required: true, newLine: true},
      valid: {label: '有效期限', fieldType: 'date', required: true},
      agency: {label: '签发机关'},
      idType: {label: '证件类型', fieldType: 'radio', singleLine: true, dictKey: 'certificate'}
    }
  },
  concat: {
    fieldType: 'object',
    header: '联系信息',
    //role: consultRole,
    children: {
      phone: {label: '联系电话', required: true},
      address: {label: '住址'}
    }
  },
  useInfo: {
    fieldType: 'object',
    header: '利用信息',
    children: {
      searchDescription: {label: '查阅内容', fieldType: 'radio', dictKey: 'content', filterable: true, required: true},
      useWay: {label: '利用情形', fieldType: 'radio', dictKey: 'inquire', filterable: true, required: true},
      useFor: {label: '利用目的', fieldType: 'radio', dictKey: 'utilize', filterable: true, required: true},
      other: {label: '其他', type: 'textarea', singleLine: true}
    }
  },
  material: {
    fieldType: 'object',
    header: '证明材料',
    children: {
      fileType: {label: '材料类型', fieldType: 'checkBox', singleLine: true, dictKey: 'materials'},
      id: {label: '材料收集', fieldType: 'file', multiple: true}
    }
  }
}
</script>
