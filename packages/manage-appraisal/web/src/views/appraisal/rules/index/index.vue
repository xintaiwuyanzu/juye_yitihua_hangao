<template>
  <section>
    <nac-info :back="back">
      <el-badge :value="recommendKeys" class="item">
        <el-button type="primary" @click="recommendKeyWord" size="mini">推荐关键词管理</el-button>
      </el-badge>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-row style="height: 100%" :gutter="5">
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <el-select v-model="basisId" @change="selectRules" placeholder="请选择">
                <el-option
                    v-for="item in basisData"
                    :key="item.id"
                    :label="item.code"
                    :value="item.id">
                </el-option>
              </el-select>
              <el-button icon="el-icon-plus" type="primary" size="mini" circle
                         style="float: right;"
                         @click="addRules()"></el-button>
            </div>
            <div class="text item" style="height: 69vh;">
              <div v-cloak>
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
                      <span style="margin-left: 5px" @click="handleNodeClick(item)">
                        <el-tag v-if="rulesId === item.id" type="primary">{{ item.rulesName }}</el-tag>
                        <el-tag v-else type="success">{{ item.rulesName }}</el-tag>
                      </span>
                      <span style="float: right">
                          <el-link type="primary" @click="editNode(item)">编辑</el-link>
                        </span>
                      <span style="float: right;margin-right: 10px">
                          <el-link type="primary" @click="deleteNode(item)">删除</el-link>
                      </span>
                    </div>
                  </div>
                </transition-group>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="18">
          <el-card class="box-card" style="height: 78vh;">
            <el-tabs v-model="activeName" >
              <el-tab-pane name="first" label="关键词" >
                <filter-word ref="word" :basis-id="basisId" :rules-id="rulesId" ></filter-word>
              </el-tab-pane>
              <el-tab-pane name="second">
                <label slot="label">专题</label>
                <special ref="special" :basis-id="basisId" :rules-id="rulesId"></special>
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <edit-form ref="editForm" :basis-id="basisId"  v-on:reload="reloadData"/>
  </section>
</template>

<script>
  import indexMixin from '@dr/auto/lib/util/indexMixin'
  import formMixin from "@dr/auto/lib/util/formMixin"

  import filterWord from "../filterWord";
  import special from "../special";
  import editForm from "../editForm/form";

  export default {
    mixins: [indexMixin, formMixin],
    components: {filterWord, editForm, special},
    data() {
      return {
        wordGroup: '',
        rulesId: '',
        basidId: '',
        tabColumn: '',
        recommendKeys: 0,
        basisId: this.$route.query.basisId,
      basisData:[],
      back:this.$route.query.basisId?true:false,
      activeName:'first'
    }
  },
  methods: {
    addRules() {
      this.$refs.editForm.show = true
    },
    selectBasis() {
      this.$http.post("/appraisalBasis/page", {page: false})
          .then(({data}) => {
            this.basisData = data.data
            if(!this.basisId){
              if(this.basisData.length>0){
                this.basisId = this.basisData[0].id
              }
            }
            this.selectRules()
          })
    },
    selectRules() {
      this.$http.post("/appraisalRules/page", {page: false,basisId:this.basisId})
          .then(({data}) => {
            this.wordGroup = data.data
            if(this.wordGroup[0]){
              this.handleNodeClick(this.wordGroup[0])
            }else{
              this.$refs.word.clearData()
            }
          })
    },
    $init() {
      this.selectBasis()
      this.$http.post("/appraisalRecommendKeyWord/countToDo")
          .then(({data}) => {
            this.recommendKeys = data.data
          })
    },
    handleNodeClick(node) {
      this.rulesId = node.id
      this.basidId = node.basidId
    },
    editNode(node) {
      this.rulesId = node.id
      this.basidId = node.basidId
      this.$refs.editForm.form = node
      this.$refs.editForm.show = true
    },
    reloadData() {
      this.$init()
    },
    deleteNode(row) {
      this.$confirm("确认删除？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post("/appraisalRules/delete",{id:row.id})
          .then(({data}) => {
            if (data && data.success) {
              this.selectWordGroup()
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
          this.$http.post("/appraisalWordGroup/update",v)
      })
    },
    recommendKeyWord(){
      this.$router.push({path:'/appraisal/wordGroup/recommendKeyWord'})
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
