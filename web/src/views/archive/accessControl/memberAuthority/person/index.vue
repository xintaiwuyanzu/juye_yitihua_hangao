<template>
  <div>
    <el-table  ref="multipleTable" :data="personList.filter(data => !search1 || data.userName.toLowerCase().includes(search1.toLowerCase()))"
              tooltip-effect="dark"
              class="tableStyle"
               max-height="250"
              @selection-change="handlePersonSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="userName" label="人员名称" width="120"></el-table-column>
      <el-table-column>
        <template slot="header" slot-scope="scope">
          <el-input v-model="search1" size="mini" placeholder="输入机构人员名称"/>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="reverseSelect(personList)">反选</el-button>
      <el-button @click="toggleSelection">取消选择</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "index",
  props:{personList:Array},
  data(){
    return{
      search1:'',//机构人员搜索
      loading: false
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
    //人员更改时
    handlePersonSelectionChange(val) {
      this.$emit("personNameAndId",val)
    },
    toggleRowSelectionPerson(val){
      this.personList.forEach(i=>{
        if (i.id===val){
          this.$refs.multipleTable.toggleRowSelection(i, true);

        }
      })
    }
  }
}
</script>

<style>
.tableStyle{
  margin:10px;width:415px;overflow: auto;border: 1px solid gainsboro
}
</style>