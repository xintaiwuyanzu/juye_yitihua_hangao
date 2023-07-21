<template>
  <el-transfer
      ref="transfer"
      filterable
      :filter-method="filterMethod"
      filter-placeholder="请输入全宗"
      @left-check-change="leftCopeChane"
      @right-check-change="rightCopeChane"
      @change="handleChange"
      v-model="rights"
      :titles="['选择全宗', '授权全宗']"
      :data="lefts">

    <el-button class="transfer-footer" slot="left-footer" size="small" @click="disableBtn('left')">不选
    </el-button>
    <el-button class="transfer-footer" slot="left-footer" size="small" @click="undisableLeftBtn">不选恢复
    </el-button>
    <el-button class="transfer-footer" slot="left-footer" size="small" @click="resetBtn">重置</el-button>

    <el-button class="transfer-footer" slot="right-footer" size="small" @click="disableBtn('right')">设为不选
    </el-button>
    <el-button class="transfer-footer" slot="right-footer" size="small" @click="undisableLeftBtn">不选恢复
    </el-button>
  </el-transfer>
</template>

<script>
export default {
  name: "index",
  props: {
    transferData: {type: Array}, rightValue: {type: Array}
  },
  data() {
    return {
      leftCheckedList: [],//穿梭框左边选中的对象
      rightCheckedList: [],//穿梭框右边选中的对象
      transferList: [],//用于存放transferData改变后的值
      lefts: [],
      rights: []
    }
  },
  methods: {
    //设为不可选
    disableBtn(val) {
      let CheckedList = []
      let checked = null
      if (val === 'left') {
        CheckedList = this.leftCheckedList
        checked = 'leftPanel'
      } else {
        CheckedList = this.rightCheckedList
        checked = 'rightPanel'
      }
      CheckedList.forEach((j) => {

        this.transferList = this.transferData.filter(i => {
          if (i.key === j) {
            i.disabled = true
          }
          return i
        })
        this.$emit("disableBtn", this.transferList)
      })
      this.$refs.transfer.$refs[checked].checked = [];
    },
    //不可选恢复
    undisableLeftBtn() {
      this.transferList = this.transferData.filter(i => {
        if (i.disabled) {
          i.disabled = false
        }
        return i
      })
      this.$emit("undisableLeftBtn", this.transferList)
    },
    //重置
    resetBtn() {
      this.$emit("resetBtn")
    },
    filterMethod(query, item) {
      return item.label.indexOf(query) > -1;
    },
    //左边选项的id
    leftCopeChane(selecedOption) {
      this.leftCheckedList = selecedOption;
    },
    rightCopeChane(selecedOption) {
      console.log(selecedOption)
      this.rightCheckedList = selecedOption
      this.$emit("getFondId", this.rightCheckedList)
    },
    handleChange(value, direction) {
      if (direction == "right") {
        this.rightCopeChane(value)
      }
    }
  },
  watch: {
    rightValue(val) {
      console.log(val)
    }
  },
  mounted() {
    this.lefts = this.transferData
    this.rights = this.rightValue
  }
}
</script>

<style>
.el-transfer-panel {
  width: 300px;
}
</style>