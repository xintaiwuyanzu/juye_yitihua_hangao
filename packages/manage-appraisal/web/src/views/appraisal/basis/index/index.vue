<template>
  <section>
    <nac-info >
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-row style="height: 100%" :gutter="5">
        <el-col :span="6">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>鉴定规则</span>
              <el-button icon="el-icon-plus" type="primary" size="mini" circle
                         style="float: right;"
                         @click="addWordGroup()"></el-button>
            </div>
            <div class="text item" style="height: 69vh;">
              <div v-cloak>
                  <div v-for="item in data"
                       :key="item.id"
                       ref="list"
                       style="margin-top: 10px;">
                    <div style="display: inline;">
                      <span style="margin-left: 5px" @click="handleNodeClick(item)">
                        <el-tag v-if="basisId === item.id" type="primary">{{ item.basis }}</el-tag>
                        <el-tag v-else type="success">{{ item.basis }}</el-tag>
                      </span>
                      <span style="float: right">
                          <el-link type="primary" @click="editNode(item)">编辑</el-link>
                      </span>
                      <span style="float: right;margin-right: 10px">
                          <el-link type="primary" @click="deleteNode(item)">删除</el-link>
                      </span>
                    </div>
                  </div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="18">
          <el-card class="box-card" style="height: 79vh;">
            <div slot="header" class="clearfix">
              <span>鉴定规则配置项</span>
            </div>
            <basis-form ref="basis"  ></basis-form>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <edit-form ref="editForm" v-on:reload="$init"/>
  </section>
</template>

<script>
  import indexMixin from '@dr/auto/lib/util/indexMixin'
  import formMixin from "@dr/auto/lib/util/formMixin"
  import editForm from "../editForm/form";
  import basisTable from "../basisTable/basisTable";
  import basisForm from "../basisTable/basisForm";


  export default {
    mixins: [indexMixin, formMixin],
    components: {editForm, basisTable, basisForm},
    data() {
      return {
        data: [],
        basisId: ''
      }
    },
    methods: {
      addWordGroup() {
        this.$refs.editForm.show = true
      },
      $init() {
        this.$http.post("/appraisalBasis/page", {page: false, parentId: 'default'})
            .then(({data}) => {
              this.data = data.data
              this.handleNodeClick(this.data[0])
            })
      },
      handleNodeClick(node) {
        this.basisId = node.id
        this.$refs.basis.defaultInsertForm.parentId = node.id
        this.$refs.basis.defaultSearchForm.parentId = node.id
        this.$refs.basis.loadData()
      },
      deleteNode(row) {
        this.$confirm("确认删除？", '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }).then(() => {
          this.$http.post("/appraisalBasis/delete", {id: row.id})
              .then(({data}) => {
                if (data && data.success) {
                  this.$init()
                }
              })
        })
      },
      editNode(node) {
        this.wordGroupId = node.id
        this.$refs.editForm.form = node
        this.$refs.editForm.show = true
      },
    }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
