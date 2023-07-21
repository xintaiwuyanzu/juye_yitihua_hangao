<template>
  <section>
    <nac-info title="操作日志">
      <data-form ref="form" v-if="showSearch" @func="loadData" :person-data="todata" :multiple-selection="multipleSelection"/>
    </nac-info>
    <div class="index_main">
      <div class="table-container">
        <el-table ref="multipleTable"
                  :data="data"
                  v-loading="loading"
                  border
                  height="100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="序号" fixed align="center" width="50">
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="操作" prop="operation" show-overflow-tooltip align="center"
                           header-align="center">
          </el-table-column>
          <el-table-column label="操作人" prop="createPerson" show-overflow-tooltip align="center"
                           header-align="center" width="200">
            <template v-slot="scope">
              {{ scope.row.createPerson|getPersonName(scope.row.createPerson) }}
            </template>
          </el-table-column>
          <el-table-column label="操作时间" prop="createDate" width="200"
                           dateFormat="YYYY-MM-DD HH-mm-ss"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column label="ip地址" prop="ip" width="150" show-overflow-tooltip align="center"
                           header-align="center">
          </el-table-column>
<!--          <el-table-column label="操作" align="center" header-align="center" fixed="right" width="80px">
            <template v-slot="scope">
              <el-link size="mini" @click="remove(scope.row.id)" type="danger">删 除</el-link>
            </template>
          </el-table-column>-->
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1},$refs.form.getSearchForm(),true)"
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
import indexMixin1 from '@archive/core/src/util/indexMixin';
import DataForm from "./form"


let personData = []
export default {

  mixins: [indexMixin,indexMixin1],
  components: {DataForm},
  data() {
    return {
      loading: false,
      todata: [],
      multipleSelection: []
    }
  },
  filters: {
    getPersonName(v) {
      if (v) {
        let p = personData.find(d => d.userCode === v)
        if (p) {
          return p.userName
        }
      }
    },
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    $init() {
      this.persons()
      this.loadData()
    },
    apiPath() {
      return 'fzlog'
    },
    persons() {
      this.$http.post('/archiveOrganisePerson/getPersonsByOrganise', {
        page: false
      }).then(({data}) => {
        if (data && data.success) {
          personData = data.data
          this.todata = data.data
          //console.log(personData)
        } else {
          personData = []
        }
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
        this.$http.post('/fzlog/delete', {id: id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("删除成功")
                // todo 这里想办法把查询条件带过来，不然专门写删除图啥
                this.loadData()
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
      })
    },
  }
}
</script>
