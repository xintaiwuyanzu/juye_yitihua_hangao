<template>
  <el-card>
    <matching-key-word :form-data-id="formData.id" :form-definition-id="container.formDefinitionId" :key="key"
                       v-on:checkedKeyWord="checkedKeyWord"></matching-key-word>
  </el-card>
</template>
<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";
import matchingKeyWord from "../views/appraisal/toBeAppraisal/matchingKeyWord"

export default {
  name: "tagManage",
  extends: abstractArchiveDetail,
  components: {matchingKeyWord},
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
      key: 0,
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
      this.$parent.$emit('aaa', 'bbb')
      this.container.$emit('aaa', 'bbb')
    },
    checkedKeyWord(row) {
      this.keyWord = row.keyWord
    },
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
