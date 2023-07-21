<template>
  <div>
    <el-descriptions :column="3" border size="medium">
      <el-descriptions-item :span="3" label="题名">{{ formData.TITLE }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="档号">{{ formData.ARCHIVE_CODE }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="保管期限">{{ formData.SAVE_TERM }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="年度">{{ formData.VINTAGES }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="文件形成日期">{{ formData.FILETIME }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="责任者">{{ formData.DUTY_PERSON }}</el-descriptions-item>
      <el-descriptions-item :span="3" label="备注">{{ formData.NOTE }}</el-descriptions-item>
    </el-descriptions>
<!--    <el-button @click="openFromData" style="float: right;margin: 10px 0" type="primary">查看更多</el-button>-->
    <detail-drawer ref="drawer" :drawer="visible" :formData="formData" @cancel="cancel"></detail-drawer>
  </div>
</template>
<script>
  import abstractArchiveDetail from "../abstractArchiveDetail";
  import DetailDrawer from "./detailDrawer";

  export default {
    name: "metadataDetail",
    components: {DetailDrawer},
    extends: abstractArchiveDetail,
    props: {
      //表单数据是通过参数传进来的，TODO，需要监听参数变化动态加载显示方案
      formData: {type: Object, default: () => ({})}
    },
    data() {
      return {
        visible: false
      }
    },
    methods: {
      cancel() {
        this.visible = false
      },
      openFromData(){
        this.visible=true
        this.$refs.drawer.getFormDefinitionId()
      },
    }
  }
</script>