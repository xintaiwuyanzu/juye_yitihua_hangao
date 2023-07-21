<template>
  <el-card>
    <el-tabs v-model="showPane">
      <el-tab-pane label="回复" name='one'>
        <businessGuidance v-on:next="next" v-on:reload="reload" :showStatus="showStatus" :gid="gid" :form-data-id="formData.id" :form-definition-id="container.formDefinitionId" :key="key" ></businessGuidance>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";
import businessGuidance from "./businessGuidance"


export default {
  name: "tagManage",
  props:{
    gid:{type:String },
    showStatus:{type:String}
  },
  extends: abstractArchiveDetail,
  components: {businessGuidance},
  data() {
    return {
      showPane: 'one',
      key:0,

    };
  },
  watch: {
    formData() {
      this.key++
    }
  },
  mounted() {
    window.addEventListener("resize", this.resizeTheChart);
  },
  methods: {
    reload(){
      this.container.$emit("reload")
    },
    next(){
      this.container.$emit('next')
    }
  }
}
</script>

<style lang="scss">
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin: 5px;
  vertical-align: bottom;
}

.btns-konw {
  display: flex;
  align-items: center;
  float: right;
  margin: 10px;
}

.chartsPane {
  background-color: white;
}
</style>
