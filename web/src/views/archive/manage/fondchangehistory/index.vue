<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline>
        <el-form-item label="全宗名" prop="name">
          <el-input v-model="searchForm.name" style="width: 160px" placeholder="请输入全宗名" clearable/>
        </el-form-item>
        <el-form-item label="新全宗名" prop="newName" style="margin-left: 8px">
          <el-input v-model="searchForm.newName" style="width: 160px" placeholder="请输入新全宗名" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData" size="mini">搜索</el-button>
          <el-button type="danger" size="mini" @click="remove">删 除</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-table border height="100%" class="table-container"
                ref="multipleTable" :data="data"
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <column-index :page="page"/>
        <el-table-column label="全宗名" prop="fondName" show-overflow-tooltip align="center"/>
        <el-table-column label="新全宗名" prop="newFondName" show-overflow-tooltip align="center"/>
        <el-table-column label="全宗号" prop="fondId" show-overflow-tooltip align="center"/>
        <el-table-column label="新全宗号" prop="newFondId" show-overflow-tooltip align="center"/>
        <el-table-column label="排序" prop="order" show-overflow-tooltip width="70" align="center"/>
        <el-table-column label="新排序" prop="newOrder" show-overflow-tooltip width="70" align="center"/>
        <el-table-column label="提交人" prop="createPerson" show-overflow-tooltip align="center"/>
        <el-table-column label="提交时间" prop="createDate"
                         dateFormat="YYYY-MM-DD HH-mm-ss"
                         show-overflow-tooltip
                         align="center"/>
        <el-table-column label="状态" prop="status" align="center">
          <template slot-scope="scope">
            <span v-if='scope.row.status==="新增"' style="color: green">新增</span>
            <span v-if='scope.row.status==="删除"' style="color: red">删除</span>
            <span v-if='scope.row.status==="更新"' style="color: #2d79f0">更新</span>
          </template>
        </el-table-column>
      </el-table>
      <!--      <page :page="page" @change="pageIndex=>loadData({pageIndex})"/>-->
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1},this.searchForm)"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'

export default {
  mixins: [indexMixin],
  data() {
    return {
      loading: false,
      data: [],
      multipleSelection: [],
      searchForm: {
        name: '',
        edit: false
      },
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    $init() {
      this.loadData()
    },
    beforeLoadData(index) {
      this.page.index = index
      this.loadData()
    },
    loadData(params) {
      this.loading = true
      // params = Object.assign({}, this.page, {
      //   fondName: this.searchForm.name,
      //   newFondName: this.searchForm.newName
      // })
      this.$http.post('/manage/fondchangehistory/page', {
        fondName: this.searchForm.name,
        newFondName: this.searchForm.newName, ...params
      }).then(({data}) => {
        if (data && data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        }
        this.loading = false
      })
    },
    getSearchForm(param) {
      if (this.searchForm.datePick === null || this.searchForm.datePick === undefined) {
        this.searchForm.datePick = [0, 0]
      }
      const date = this.searchForm.datePick.length > 0 ? {
        startTime: this.searchForm.datePick[0],
        endTime: this.searchForm.datePick[1]
      } : {}
      return Object.assign({}, this.searchForm, date, param)
    },
    remove() {
      if (this.multipleSelection.length === 0) {
        this.$message.error("请至少选择一条数据")
        return
      }
      let ids = ''
      for (let i = 0; i < this.multipleSelection.length; i++) {
        ids = ids + this.multipleSelection[i].id + ","
      }
      const param = Object.assign({}, {ids: ids})
      this.$confirm("确认删除？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post('/manage/fondchangehistory/delete', param)
            .then(({data}) => {
              if (data.success) {
                this.$message({
                  message: '操作成功！',
                  type: 'success'
                });
              } else {
                this.$message.error(data.message)
              }
              this.loadData()
            })
      }).catch(() => {

      });
    }
  }
}
</script>
