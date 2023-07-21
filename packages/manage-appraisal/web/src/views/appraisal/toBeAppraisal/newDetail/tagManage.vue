<template>
  <el-card>
    <el-tabs v-model="showPane">
      <el-tab-pane label="鉴定记录" name='one'>
        <appraisal-history :form-data-id="formData.id"
                           :form-definition-id="container.formDefinitionId"></appraisal-history>
      </el-tab-pane>
      <el-tab-pane label="关键词推荐" name='two'>
        <add-key-word-form :form-data-id="formData.id"
                           :form-definition-id="container.formDefinitionId"></add-key-word-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>
<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";
import addKeyWordForm from "../addKeyWordForm"
import appraisalHistory from "../appraisalHistory"
import doAppraisal from "../doAppraisal"


export default {
  name: "tagManage",
  extends: abstractArchiveDetail,
  components: {addKeyWordForm, appraisalHistory, doAppraisal},
  data() {
    return {
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      myChart: null,
      chartData: [],
      chartLink: [],
      showTag: true,
      showPane: 'one',
      fullscreen: false,
      key: 0
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
    $init() {
      //this.$parent.$emit('aaa', 'bbb')
      //this.container.$emit('aaa', 'bbb')
    },
    next() {
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
