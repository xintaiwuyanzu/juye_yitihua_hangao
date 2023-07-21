<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="关键字" prop="title" style="margin-left: 8px">
          <el-input v-model="searchForm.title" style="width: 300px" placeholder="多个关键字以空格分割" clearable/>
        </el-form-item>
        <!--        <el-form-item label="题名" prop="title" style="margin-left: 8px">
                  <el-input v-model="searchForm.title" style="width: 150px" placeholder="请输入题名" clearable/>
                </el-form-item>
                <el-form-item label="档号" prop="archiveCode" style="margin-left: 8px">
                  <el-input v-model="searchForm.archiveCode" style="width: 150px" placeholder="请输入档号" clearable/>
                </el-form-item>
                <el-form-item label="年度" prop="year" style="margin-left: 8px">
                  <el-input v-model="searchForm.year" style="width: 150px" placeholder="请输入年度" clearable/>
                </el-form-item>-->
        <el-form-item>
          <!--          <el-button type="text" @click="categorySearch">选择分类</el-button>-->
          <el-button type="primary" @click="_search(1,'first')" size="mini">搜 索</el-button>
        </el-form-item>
        <el-form-item>
          <!--          <el-button type="text" @click="categorySearch">选择分类</el-button>-->
          <el-button type="success" @click="_search(1,'second')" size="mini">二次搜索</el-button>
        </el-form-item>
      </el-form>
      <el-drawer title="请选择全宗分类" :visible.sync="drawer"
                 class="drawer"
                 @close="_search"
                 direction="rtl" :modal="false" :modal-append-to-body="false">
        <el-input
            placeholder="输入关键字进行过滤"
            clearable
            v-model="filterText">
        </el-input>
        <!--全宗门类选择树-->
        <el-tree :filter-node-method="filterNode" :load="loadNode"
                 node-key="data.code"
                 lazy show-checkbox :props="props" ref="fondTree"/>
      </el-drawer>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="table" :data="data" class="searchTable" stripe border height="100%">
          <el-table-column prop="order" label="排序" width="50" header-align="center" align="center"
                           show-overflow-tooltip>
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column prop="archiveCode" label="档号" header-align="center"
                           width="150px" show-overflow-tooltip/>
          <el-table-column prop="year" label="年度" align="center"
                           width="60px" show-overflow-tooltip/>
          <el-table-column prop="title" label="题名" show-overflow-tooltip header-align="center" width="400px"/>
          <el-table-column prop="saveTerm" label="保管期限" show-overflow-tooltip align="center"/>
          <el-table-column prop="fileTime" label="形成日期" show-overflow-tooltip align="center"/>
          <el-table-column label="操作" width="100px" align="center">
            <template v-slot="scope">
              <el-button type="text" @click="showFile(scope.row)">原文</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination :page-size="page.size"
                     :current-page="page.index"
                     @current-change="changePage"
                     :total="page.total"
                     layout="total, prev, pager, next"/>
    </div>
    <file-list refType="archive" :formDataId="formDataId" :deleter="false" :print="false" :upload="false" transform
               style="margin-top: 5px"
               width="50%" v-if="fileListDialog"/>
  </section>
</template>
<script>

const selectChildren = (arr, strArr) => {
  arr.forEach(n => {
    if (n.children) {
      selectChildren(n.children, strArr)
    } else {
      if (strArr.indexOf(n.data.code) < 0) {
        strArr.push(n.data.code)
      }
    }
  })
}

export default {
  data() {
    return {
      title: '全库检索',
      loading: false,
      searchForm: {},
      fondList: [],
      data: [],
      fonds: [],
      filterText: '',
      props: {
        isLeaf: this.isLeaf
      },
      row: {},
      drawer: false,
      page: {
        index: 1,
        size: 15,
        total: 0
      },
      //原文查看
      fileListDialog: false,
      formDataId: '',
      fondCode: '',
      categoryCode: ''
    }
  },
  props: {
    heard: {default: true},
  },
  watch: {
    filterText(val) {
      this.$refs.fondTree.filter(val);
    }
  },
  methods: {
    async _search(index, searchType) {
      // let title = this.search
      // if (!title) {
      //   this.$message.warning("请输入查询条件")
      //   return
      // }
      this.loading = true
      if (!index) {
        index = 1
      }
      //全宗门类参数
      let fond = ''
      let cateGory = null
      if (this.$refs.fondTree) {
        //如果全宗门类树渲染并选择了，则查询选中的数据
        const checkedNodes = this.$refs.fondTree.getCheckedNodes(false);
        //如果没有选中，则按照全部的全宗查询
        if (checkedNodes.length === 0) {
          fond = this.fonds.map(f => f.data.code).join(',')
        } else {
          const fonds = []
          const category = []
          checkedNodes.forEach(n => {
            if (n.parentId === 'default') {
              //节点是全宗
              fonds.push('\'' + n.data.code + '\'')
            }
          })
          //获取最下级
          selectChildren(checkedNodes.filter(n => n.parentId !== 'default'), category)
          fond = fonds.join(',')
          cateGory = category.join(',')
        }
      } else {
        fond = this.fonds.map(f => f.data.code).join(',')
      }
      const params = Object.assign(
          {
            index,
            // fondCode: fond,
            // categoryCode: cateGory,
            size: this.page.size,
            searchType
          }, this.searchForm)
      const {data} = await this.$post('/dbsearch/searchByKeyword', params, {timeout: 10000})
      if (data.success) {
        this.data = data.data.data
        this.page.total = data.data.total
        this.page.size = data.data.size
        this.page.index = parseInt('' + data.data.start)
      } else {
        this.$message.error(data.message)
      }
      this.loading = false

    },
    changePage(index) {
      this._search(index)
    },
    categorySearch() {
      this.drawer = true
    },
    async loadFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      this.fonds = data.data
    },
    loadNode(node, resolve) {
      if (node.level === 0) {
        resolve(this.fonds)
      } else if (node.level === 1) {
        this.$post('/sysResource/personResource', {type: 'category', group: node.data.id})
            .then(d => {
              resolve(d.data.data)
            })
      } else {
        resolve(node.data.children)
      }
    },
    isLeaf(data, node) {
      if (node.level === 1) {
        return false
      }
      return !data.children
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    $init() {
      this.loadFonds()
      if (this.$route.query.title) {
        // this.search = this.$route.query.title
        this._search()
      }
    },
    showFile(row) {
      this.formDataId = row.id
      this.fileListDialog = true
    }
  }
}
</script>
<style lang="scss">
.search_header {
  .slot {
    display: flex;
    align-items: center;
  }

  .search_input {
    .el-input-group__append {
      padding: 0px !important;

      .el-button {
        margin: 0px !important;
      }
    }
  }
}

.searchTable {
  .cell {
    p {
      mark {
        margin: 0px 2px 0px 0px;
      }
    }
  }

  .content {
    p {
      max-height: 200px;
      overflow: auto;
    }
  }
}

.drawer {
  .el-drawer__body {
    overflow: auto;
    padding: 0px 10px;
  }
}
</style>
