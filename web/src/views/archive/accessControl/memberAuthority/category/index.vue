<template>
  <div>
    <el-table
        ref="multipleTable"
        :data="typeOptions.filter(data => !search || data.description.toLowerCase().includes(search.toLowerCase()))"
        tooltip-effect="light"
        class="tableStyle"
        max-height="250"
        @selection-change="handleTypeSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="description" label="门类" width="120"></el-table-column>
      <el-table-column>
        <template slot="header" slot-scope="scope">
          <el-input v-model="search" size="mini" placeholder="输入门类名称"/>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="reverseSelect(typeOptions)">反选</el-button>
      <el-button @click="toggleSelection">取消选择</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  props:{typeOptions:Array},
  data(){
    return{
      search:'',//门类搜索
    }
  },
  methods:{
    //表格按钮取消选择
    toggleSelection() {
      this.$refs.multipleTable.clearSelection();
    },
    // 反选
    reverseSelect(rows) {
      rows.forEach(row => {
        this.$refs.multipleTable.toggleRowSelection(row);
      })
    },
    //门类更改时
    handleTypeSelectionChange(val) {
      this.$emit("categoryName",val)
    },
    toggleRowSelectioncategoryName(val){
      this.typeOptions.forEach(i=>{
        if (i.description===val){
          this.$refs.multipleTable.toggleRowSelection(i, true);
        }

      })
    }
  },

}
</script>

<style>
</style>