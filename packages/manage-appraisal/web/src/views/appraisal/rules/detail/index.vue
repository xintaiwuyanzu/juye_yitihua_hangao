<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="关键词:" prop="keyWord">
          <el-input v-model="searchForm.keyWord" clearable placeholder="请输入关键词"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
          <el-button type="primary" @click="$router.back()" size="mini">返 回</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main">
      <el-row style="height: 100%" justify="space-between" type="flex">
        <el-col :span="6">
          <el-card style="height: 100%;overflow: auto;">
            <div slot="header" class="clearfix">
              <span>依据细则信息</span>
            </div>
            <basis-form :basis-id="$route.query.basisId"></basis-form>
            <rules-form :riles-id="$route.query.rulesId"></rules-form>
          </el-card>
        </el-col>
        <el-col :span="8" style="margin-left: 5px">
          <el-card style="height: 100%;overflow: auto;">
            <div slot="header" class="clearfix">
              <span>关键词</span>
              <el-link style="float: right;margin-left: 10px;margin-top: 2px" @click="addKeyWord" size="mini"
                       type="primary">添加
              </el-link>
            </div>
            <key-word :rules-id="$route.query.rulesId" :basis-id="$route.query.basisId" ref="keyWord"></key-word>
          </el-card>
        </el-col>
        <el-col :span="10" style="margin-left: 5px">
          <el-card style="height: 100%;overflow: auto;">
            <div slot="header" class="clearfix">
              <span>专题</span>
              <el-link style="float: right;margin-left: 10px;margin-top: 2px" @click="addSpecial" size="mini"
                       type="primary">添加
              </el-link>
            </div>
            <special :rules-id="$route.query.rulesId" ref="special"></special>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
<script>
import basisForm from "./basisForm";
import rulesForm from "./rulesForm";
import keyWord from "./keyWord";
import special from "./special";

export default {
  components: {
    basisForm,
    rulesForm,
    keyWord,
    special
  },
  data() {
    return {
      searchForm: {keyWord: ''}
    }
  },
  methods: {
    addKeyWord() {
      this.$refs.keyWord.addKeyWord()
    },
    addSpecial() {
      this.$refs.special.addSpecial()
    },
    search() {
      this.$refs.keyWord.loadData({'keyWord': this.searchForm.keyWord})
      this.$refs.special.loadData({'specialRemarks': this.searchForm.keyWord})
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
