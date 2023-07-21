<template>
  <div class="text item" style="height: 69vh;">
    <transition-group tag="div">
      <div v-for="item in wordGroup"
           :key="item.id"
           ref="list"
           style="margin-top: 10px;"
           draggable="true"
           @dragstart="handleDragStart($event, item)"
           @dragover.prevent="handleDragOver($event, item)"
           @dragenter="handleDragEnter($event, item)"
           @dragend="handleDragEnd($event, item)">
        <div style="display: inline;">
          <span style="margin-left: 5px" >
            <el-tag type="primary">{{ item.groupName+'---'+ (item.isApply==='0'?'不可领取任务':'可以领取任务')}}</el-tag>
          </span>
          <span style="float: right;margin-right: 10px">
            <el-link type="primary" v-if="item.isApply==='0'" @click="changeApply(item,'1')">改为可以领取任务</el-link>
            <el-link type="primary" v-else @click="changeApply(item,'0')">改为不可领取任务</el-link>
            <el-link type="primary" @click="deleteNode(item)" style="margin-left: 5px" >删除</el-link>
          </span>
        </div>
      </div>
    </transition-group>
    <edit-form :batch-id="batchId" ref="editForm" v-on:loadData="loadData" ></edit-form>
  </div>
</template>

<script>
import editForm from './form'
export default {
  components:{
    editForm
  },
  name: "index",
  data(){
    return{
      wordGroup:[],
      dragging:''
    }
  },
  props:{
    batchId:''
  },
  methods:{
    $init(){
      this.loadData()
    },
    loadData(){
      this.$http.post("/appraisalWordGroup/getWordGroupByBatchId",{batchId:this.batchId})
          .then(({data})=>{
            this.wordGroup = data.data
          })
    },
    deleteNode(row){
      this.$confirm("确认删除？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post("/appraisalBatchWordGroup/delete",{id:row.id})
            .then(({data}) => {
              if (data && data.success) {
                this.loadData()
              }
            })
      })
    },
    handleDragStart(e, item) {
      this.dragging = item;
    },
    handleDragEnd(e, item) {
      this.dragging = null
    },
    //首先把div变成可以放置的元素，即重写dragenter/dragover
    handleDragOver(e) {
      e.dataTransfer.dropEffect = 'move'// e.dataTransfer.dropEffect="move";//在dragenter中针对放置目标来设置!
    },
    handleDragEnter(e, item){
      e.dataTransfer.effectAllowed = "move"//为需要移动的元素设置dragstart事件
      if (item === this.dragging) {
        return
      }
      const newItems = [...this.wordGroup]
      const src = newItems.indexOf(this.dragging)
      const dst = newItems.indexOf(item)
      newItems.splice(dst, 0, ...newItems.splice(src, 1))
      this.wordGroup = newItems;
      let index = 1
      this.wordGroup.forEach(v=>{
        v.priority = index++
        this.$http.post("/appraisalBatchWordGroup/updatePriority",v)
      })
    },
    edit(){
      this.$refs.editForm.show = true
      this.$refs.editForm.loadData()
    },
    changeApply(item,isapply){
      item.isApply = isapply
      this.$http.post("/appraisalBatchWordGroup/updatePriority",item)
    }

  }
}
</script>

<style scoped>

</style>
