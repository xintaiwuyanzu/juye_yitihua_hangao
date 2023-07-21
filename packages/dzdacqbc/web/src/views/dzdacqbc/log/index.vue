<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="操作人:" prop="operatePerson">
          <el-input v-model="searchForm.operatePerson" clearable placeholder="请输入操作人名称"/>
        </el-form-item>
        <el-form-item label="操作时间:" prop="operateTime">
          <el-date-picker v-model="searchForm.operateTime" type="datetimerange" range-separator="至"
                  start-placeholder="开始时间" end-placeholder="结束时间" value-format="yyyy-MM-dd HH:mm:ss" @change="changeTime">
          </el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData(1, searchForm)" size="mini">搜 索</el-button>
          <el-button @click="resetSearch" size="mini" type="info">重 置</el-button>
          <el-button type="primary" @click="expLog" size="mini">导 出</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main">
      <div class="table-container">
        <el-table ref="multipleTable"
                  :data="data"
                  v-loading="loading"
                  border
                  height="100%">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="序号" fixed align="center" width="50">
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="content" show-overflow-tooltip align="center" header-align="center">
          </el-table-column>
          <el-table-column label="操作人" prop="operatePerson" show-overflow-tooltip align="center" header-align="center" />
          <!--          <el-table-column label="操作时间" prop="createDate" show-overflow-tooltip align="center"-->
          <!--                           header-align="center">-->
          <!--            <template v-slot="scope">-->
          <!--              {{ scope.row.createDate|datetime }}-->
          <!--            </template>-->
          <!--          </el-table-column>-->
          <el-table-column label="操作时间" prop="createDate" dateFormat="YYYY-MM-DD HH:mm:ss" show-overflow-tooltip align="center"/>
          <el-table-column label="ip地址" prop="ip" show-overflow-tooltip align="center" header-align="center" />
<!--          <el-table-column label="操作" align="center" header-align="center" fixed="right" width="80px">
            <template v-slot="scope">
              <el-button size="mini" @click="remove(scope.row.id)" type="text">删 除</el-button>
            </template>
          </el-table-column>-->
        </el-table>
      </div>
      <el-pagination
              @current-change="index=>loadData(index, searchForm)"
              :current-page.sync="page.index"
              :page-size="page.size"
              layout="total, prev, pager, next"
              :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>

<script>
  import indexMixin from "@dr/auto/lib/util/indexMixin";

  export default {
    mixins: [indexMixin],
    data() {
      return {
        page: {
          index: 1,
          size: 15,
          total: 0
        },
        loading: false,
        data: [],
        searchForm: {
          operatePerson: '',
          operateTime: [],
          createPerson:'',
          updatePerson:''
        },
      }
    },
    methods: {
      $init() {
        this.loadData(1, this.searchForm)
      },
      loadData(index, searchForm) {
        this.loading = true
        if (index || index === 0) {
          this.page.index = index
        }
        const param = Object.assign({
          page: true,
          pageIndex: this.page.index - 1,
          pageSize: this.page.size,
        }, searchForm)
        this.$http.post('/cqbclog/page', param)
                .then(({data}) => {
                  if (data.success) {
                    this.data = data.data.data
                    this.page.index = data.data.start / data.data.size + 1
                    this.page.size = data.data.size
                    this.page.total = data.data.total
                  } else {
                    this.$message.error(data.message)
                  }
                  this.loading = false
                })
      },
      remove(id) {
        this.$confirm("确认删除？", '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }).then(() => {
          this.loading = true
          this.$http.post('/cqbclog/delete', {id: id})
                  .then(({data}) => {
                    if (data.success) {
                      this.$message.success("删除成功")
                      this.loadData()
                    } else {
                      this.$message.error(data.message)
                    }
                    this.loading = false
                  })
        })
      },
      timestampToTime(timestamp) {
        if (timestamp !== 0 && timestamp !== undefined) {
          return this.$moment(timestamp).format('yyyy-MM-dd HH:mm:ss')
        }
      },
      expLog() {
        let url = 'api/cqbclog/expLog?operatePerson='+ this.searchForm.operatePerson + '&createPerson='
                + this.searchForm.createPerson + '&updatePerson=' + this.searchForm.updatePerson
        window.open(url)
      },
      changeTime() {
        if (this.searchForm.operateTime) {
          this.searchForm.createPerson = this.searchForm.operateTime[0]
          this.searchForm.updatePerson = this.searchForm.operateTime[1]
        } else {
          this.searchForm.createPerson = null
          this.searchForm.updatePerson = null
        }
      },
      resetSearch() {
        this.$refs.searchForm.resetFields()
        this.searchForm.createPerson = null
        this.searchForm.updatePerson = null
      }
    }
  }
</script>
