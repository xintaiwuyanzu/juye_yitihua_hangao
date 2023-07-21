<template>
  <div style="width: 100%;height:auto;text-align: center">
    <el-input :autosize="{ minRows: 2, maxRows: 4}"
              type="textarea"
              ref="saveTagInput"
              v-model="opinion"
              placeholder="请输入内容">

    </el-input>
    <div style="text-align: center">
        <el-tag
            :key="tag.id"
            v-for="tag in dynamicTags"
            closable
            style="cursor: pointer;"
            :disable-transitions="false"
            @close="handleClose(tag)"
            @click="handleClick(tag)">
          {{ tag.opinion }}
        </el-tag>
      <el-button v-if="dynamicTags.length===10" @click="moreQuery">更多</el-button>
      <el-input
          class="input-new-tag"
          v-if="inputVisible"
          v-model="inputValue"
          ref="saveTagInput"
          size="small"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm">
      </el-input>
      <div v-else  style="display: inline-block">
        <el-button size="small" @click="showInput">+ 添加标签</el-button>
      </div>
    </div>
  </div>

</template>

<script>

/**
 *  意见管理
 */
export default {

  data() {
    return {
      dynamicTags: [],
      inputValue: '',
      opinion: '',
      inputVisible: false,
      pageSize: 10,
    }
  },
  methods: {
    $init() {
      this.queryOpinion()
    },
    queryOpinion() {
      this.$http.post('/opinion/page', {pageSize: this.pageSize}).then(({data}) => {
        if (data.success) {
          this.dynamicTags = data.data.data
        }
      })
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    moreQuery(){
      this.pageSize = 35
      this.queryOpinion()
    },
    handleInputConfirm() {
      if (this.inputValue != '') {
        this.$http.post('/opinion/addOpinion', {opinion: this.inputValue}).then(({data}) => {
          if (data.success) {
            this.queryOpinion()
            this.inputVisible = false
            this.inputValue = ''
          }else {
            this.$message.warning(data.message)
          }
        })
      }else {
        this.inputVisible = true
      }

    },
    handleClose(tag) {
      this.$confirm('此操作将删除选中标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('/opinion/deleteOpinion', {id: tag.id}).then(({data}) => {
          if (data.success) {
            this.queryOpinion()
          }
        })
      })
    },

    handleClick(tag) {
      this.opinion = tag.opinion
      this.$emit('func', this.opinion)
    }
  },

}
</script>

<style scoped>
.el-tag, .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
}
</style>