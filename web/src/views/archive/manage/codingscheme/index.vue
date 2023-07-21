  <template>
  <section>
    <nac-info title="编码方案">
      <el-form :model="searchForm" ref="form" inline>
        <el-form-item label="方案名称" prop="name">
          <el-input v-model="searchForm.name"
                    placeholder="请输入方案名称"
                    style="width: 160px"
                    clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData" size="mini">搜 索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="mini" @click="add">添 加</el-button>
          <el-button type="danger" size="mini" @click="remove">删 除</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="multipleTable"
                  :data="data"
                  border
                  height="100%"
                  @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="序号" fixed align="center" width="50">
            <template slot-scope="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="方案名称" prop="name" show-overflow-tooltip align="center"
                           header-align="center">
            <template slot-scope="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>
          <!--<el-table-column label="方案编码" prop="code" show-overflow-tooltip align="center"
                           header-align="center">
              <template slot-scope="scope">
                  {{scope.row.code}}
              </template>
          </el-table-column>-->
          <el-table-column label="起始年度" prop="startYear" show-overflow-tooltip align="center"
                           header-align="center">
            <template slot-scope="scope">
              {{ scope.row.startYear }}
            </template>
          </el-table-column>
          <el-table-column label="终止年度" prop="endYear" show-overflow-tooltip align="center"
                           header-align="center">
            <template slot-scope="scope">
              {{ scope.row.endYear }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" header-align="center" fixed="right" width="250">
            <template slot-scope="scope">
              <el-button size="mini" @click="edit(scope.row)" type="primary">编 辑</el-button>
              <el-button size="mini" @click="configTest(scope.row)" type="warning">配 置</el-button>
              <el-button size="mini" @click="setDefault(scope.row)"
                         :type="scope.row.default?'info':'success'"
                         :disabled="scope.row.default">设为默认
              </el-button>
              <!--                        <el-button size="mini" @click="delete(scope.row.id)">删 除</el-button>-->
            </template>
          </el-table-column>
        </el-table>

      </div>
      <el-pagination
          @current-change="index=>loadData(index)"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>

      <el-dialog
          :title="deployTitle"
          width="50%"
          :close-on-click-modal="false"
          :destroy-on-close="true"
          :visible.sync="dialog">
        <el-form :model="showScheme" ref="form" style="" label-width="200px">
          <el-form-item label="方案名称" prop="name" required
                        :rules="[
                      { required: true, message: '方案名称不能为空'},
                    ]">
            <el-input v-model="showScheme.name" placeholder="请输入方案名称"
                      style="width: 75%" clearable></el-input>
          </el-form-item>
          <el-form-item label="方案编码" prop="code" :rules="[
                      { required: true, message: '方案编码不能为空'},
                    ]" required>
              <el-input v-model="showScheme.code"
                        placeholder="请输入方案编码"
                        style="width: 75%" clearable></el-input>
          </el-form-item>
          <el-form-item label="顺序号" prop="order">
            <el-input v-model="showScheme.order"
                      placeholder="请输入顺序号"
                      @input="showScheme.order=showScheme.order.replace(/[^\d]/g,'')"
                      style="width: 75%" clearable></el-input>
          </el-form-item>
          <el-form-item label="起始年度" prop="startYear">
            <el-date-picker
                v-model="showScheme.startYear"
                type="year"
                placeholder="起始年度"
                style="width: 75%" :clearable="true">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="终止年度" prop="endYear">
            <el-date-picker
                v-model="showScheme.endYear"
                type="year"
                placeholder="终止年度"
                style="width: 75%" :clearable="true">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="是否自动生成" prop="isAutoGeneration">
            <el-switch
                :true-label="truelabel" :false-label="falselabel"
                v-model="showScheme.isAutoGeneration"
                active-color="#13ce66"
                inactive-color="#ff4949">
            </el-switch>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="dialog = false" class="btn-cancel">取 消</el-button>
          <el-button type="primary" @click="save" v-loading="loading" class="btn-submit">提 交</el-button>
        </div>
      </el-dialog>
    </div>
    <sort-config ref="sort"
                 :pitch-on="pitchOn"
                 :field="field"
                 @fun="fun"
                 :show-scheme-id="showSchemeId"
    ></sort-config>
  </section>
</template>

<script>
import indexMixin from "@dr/auto/lib/util/indexMixin";
import SortConfig from "./sort";

export default {
  mixins: [indexMixin],
  components: {SortConfig},
  data() {
    return {
      searchForm: {
        name: ''
      },
      truelabel: 1,
      falselabel: 0,
      data: [],
      dialog: false,
      dialogVisible: false,
      showScheme: {},
      multipleSelection: [],
      deployTitle: '',
      categoryId: '',
      categoryCode: '',
      showSchemeId: '',
      pitchOn: [],
      field: [],
    }
  },
  methods: {
    $init() {
      this.categoryId = this.$route.query.id
      this.categoryCode = this.$route.query.code
      this.fun()
      this.loadData(0)
    },
    loadData(index) {
      // this.loading = true
      if (index) {
        this.page.index = index
      }
      const param = Object.assign({}, this.page, {
        index: index,
        name: this.searchForm.name,
        businessId: this.categoryId
      })
      this.$http.post('/codingscheme/page', param)
          .then(({data}) => {
            if (data.success) {
              this.data = data.data.data
              for (const datum of this.data) {
                if (datum.startYear === 1970) {
                  datum.startYear = null
                }
                if (datum.endYear === 1970) {
                  datum.endYear = null
                }
              }
              this.page.index = data.data.start / data.data.size + 1
              this.page.size = data.data.size
              this.page.total = data.data.total
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
    changeEnabled(row) {
      if (row.isEnabled) {
        row.isEnabled = 1
      } else {
        row.isEnabled = 0
      }
      this.$http.post("/codingscheme/update", row)
          .then(({data}) => {
            if (data.success) {
              this.$message.success("修改启用状态成功")
              this.dialog = false
              this.loadData()
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
    setDefault(row) {
      this.loading = true
      row.default = true
      if (!row.startYear) {
        row.startYear = 1970
      }
      if (!row.endYear) {
        row.endYear = 1970
      }
      this.$http.post("/codingscheme/update", row)
          .then(({data}) => {
            if (data.success) {
              this.$message.success("设置默认成功")
              this.dialog = false
              this.loadData()
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
    add() {
      this.deployTitle = '添加'
      this.optype = 'add'
      this.dialog = true
      this.showScheme = {isAutoGeneration: true}
    },
    fun() {
      this.$http.post("/codingscheme/getFieldCheck", {fond: true})
          .then(({data}) => {
            if (data.success) {
              this.field = data.data
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
    edit(row) {
      this.deployTitle = '编辑'
      this.optype = 'edit'
      this.showScheme = Object.assign({}, row)
      this.dialog = true
      if (this.showScheme.startYear) {
        this.showScheme.startYear = this.getTimes(row.startYear)
      }
      if (this.showScheme.endYear) {
        this.showScheme.endYear = this.getTimes(row.endYear)
      }
    },
    getTimes(year) {
      return (year - 1970) * 365 * 24 * 3600 * 1000 + (year - 1970) / 4 * 24 * 3600 * 1000
    },
    configTest(row) {
      this.showSchemeId = row.id
      this.loading = true
      const param = Object.assign({}, {businessId: this.showSchemeId})
      this.$http.post('/codingscheme/schemeitem/page', param)
          .then(({data}) => {
            if (data.success) {
              this.pitchOn = data.data.data
              this.$http.post("/codingscheme/getFieldCheck", {fond: true})
                  .then(({data}) => {
                    if (data.success) {
                      this.field = data.data
                      if (this.pitchOn && this.pitchOn.length > 0) {
                        for (const x of this.pitchOn) {
                          for (let i = 0; i < this.field.length; i++) {
                            if (x.code === this.field[i].code) {
                              this.field.splice(i, 1)
                            }
                          }
                        }
                      } else {
                        this.pitchOn = []
                      }
                    } else {
                      this.$message.error(data.message)
                    }
                    this.dialogVisible = true
                    this.loading = false
                  })
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
    save() {
      this.$refs.form.validate(valid => {
        if (valid) {
          let path
          if (this.optype === 'add') {
            path = '/codingscheme/insert'
          } else {
            path = '/codingscheme/update'
            this.showScheme.businessId = this.categoryId
          }
          this.dataSwitch()
          this.loading = true
          if (this.showScheme.endYear === 1970 || this.showScheme.startYear <= this.showScheme.endYear) {
            this.$http.post(path, this.showScheme)
                .then(({data}) => {
                  if (data.success) {
                    this.$message.success("保存成功")
                    this.dialog = false
                    this.loadData()
                  } else {
                    this.$message.error(data.message)
                    if (this.showScheme.startYear === 1970) {
                      this.showScheme.startYear = null
                    }
                    if (this.showScheme.endYear === 1970) {
                      this.showScheme.endYear = null
                    }
                  }
                  this.loading = false
                })
          } else {
            this.$message.error("起始年度不得大于终止年度！")
            this.loading = false
          }
        }
      })
    },
    dataSwitch() {
      if (undefined !== this.showScheme.startYear) {
        this.showScheme.startYear = this.timestampToTime(this.showScheme.startYear)
      }
      if (undefined !== this.showScheme.endYear) {
        this.showScheme.endYear = this.timestampToTime(this.showScheme.endYear)
      }
      if (!this.showScheme.startYear) {
        this.showScheme.startYear = 1970
      }
      if (!this.showScheme.endYear) {
        this.showScheme.endYear = 1970
      }
    },
    remove() {
      if (this.multipleSelection.length <= 0) {
        this.$message.warning("请选择至少一项删除")
        return
      }
      let ids = ""
      for (const element of this.multipleSelection) {
        ids += element.id + ","
      }
      this.$confirm("确认删除？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post('/codingscheme/delete', {aIds: ids})
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
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    timestampToTime(timestamp) {
      if (timestamp !== 0 && timestamp !== undefined) {
        return this.$moment(timestamp).format('YYYY')
      }
    },
  }
}
</script>
