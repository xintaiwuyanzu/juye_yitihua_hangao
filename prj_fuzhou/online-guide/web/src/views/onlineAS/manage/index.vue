<template>
  <section>
    <nac-info>
      <config-form ref="form" @func="getMsgFromForm"></config-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table
            ref="table"
            :data="data"
            :stripe="true"
            :border="true"
            :fixed="false"
            height="100%"
            style="width: 100%"
            @selection-change="handleSelectionChange">
          <el-table-column
              type="selection"
              width="40">
          </el-table-column>
          <el-table-column
              type="index"
              label="序号"
              align="center"
              header-align="center"
              sortable
              width="55">
          </el-table-column>
          <el-table-column
              prop="nianjianName"
              label="年检名称"
              header-align="center"
              sortable
              align="center">
          </el-table-column>
          <el-table-column
              prop="unitPerson"
              label="填表人"
              width="200"
              header-align="center"
              sortable
              align="center">
          </el-table-column>
          <el-table-column
              prop="nian"
              label="年份"
              header-align="center"
              sortable
              width="200"
              align="center">
          </el-table-column>
          <el-table-column
              prop="status"
              label="状态"
              width="200"
              header-align="center"
              sortable
              align="center">
          </el-table-column>
          <el-table-column
              label="操作"
              align="center"
              header-align="center"
              width="100">
            <template slot-scope="scope">
              <el-button
                      v-if="scope.row.status=='待审核'"
                  @click.native.prevent="change1(scope.row)"
                  type="text"
                  size="mini">审 核
              </el-button>
              <el-button
                      v-else
                  @click.native.prevent="change1(scope.row,'0')"
                  type="text"
                  size="mini">查 看
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({index},true)"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next,jumper,->"
          :total="page.total">
      </el-pagination>
    </div>
  </section>
</template>

<script>
import indexMixin from '@/util/indexMixin'
import ConfigForm from './form'
import fromMixin from '@/util/formMixin'
import lib from '@dr/framework/src/components/login/util'

export default {
  name: "index.vue",
  mixins: [indexMixin, fromMixin],
  components: {
    ConfigForm
  },
  data() {
    return {
      id:lib.getToken(),
      multipleSelection: [],
      searchForm: {
        name: '',
        edit: true
      },
      addForm: {
        columnShow: true
      },
      delMsg: '请至少选择一条记录',
      page: {index: 0, size: 15},
      backDialogVisible: false,
      sourceDialog: false,
      historyDialogVisible: false,
      optype: 'add',
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    loadData(params) {
      this.loading = true
      params = Object.assign({}, this.page, {
        name: this.searchForm.name,
        status: '未审核',
        index: this.page.index
      })
      this.$http.post('/onlineas/page', params).then(({data}) => {
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    change(row) {
      this.optype = 'edit'
      this.$router.push({path: '/onlineAS/addDetail', query: {id: row.id}})
    },
    change1(row,optype) {
        if (optype){
            this.optype = optype
        }else {
            this.optype = 'edit'
        }
        this.$router.push({path: '/onlineAS/check', query: {id: row.id, optype: this.optype}})
    },
    getMsgFromForm(searchForm) {
      this.searchForm = searchForm
      this.loadData(this.searchForm)
    },
  }
}
</script>

<style scoped>
.label-span {
  width: 120px;
  line-height: 28px;
  float: left;
  text-align: right;
}

.delete-btn {
  float: right;
}

.footer-btn {
  width: 100%;
  margin-top: 20px;
  text-align: center;
}

.add-row {
  height: 32px;
  margin-bottom: 12px;
}

</style>
