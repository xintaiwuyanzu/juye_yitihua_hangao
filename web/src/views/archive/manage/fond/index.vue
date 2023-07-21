<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline>
        <el-form-item label="全宗名" prop="name">
          <el-input v-model="searchForm.name" placeholder="请输入全宗名" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData" size="mini">搜索</el-button>
          <el-button type="primary" size="mini" @click="showdialog">添加</el-button>
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
        <el-table-column label="全宗名" prop="name" show-overflow-tooltip align="center"/>
        <el-table-column label="全宗号" prop="code" show-overflow-tooltip width="100" align="center"/>
        <el-table-column label="状态" width="70" align="center">
          <template slot-scope="scope">
            <el-link v-if="scope.row.enabled" type="success" size="small">
              启用
            </el-link>
            <el-link v-else type="warning" size="small">
              未启用
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template slot-scope="scope">
            <el-button type="text" @click="editForm(scope.row)">编 辑</el-button>
            <!--            <el-button type="text" @click="set(1,scope.row)">编码方案</el-button>-->
            <el-button type="text" @click="setTree(scope.row)">门类建立</el-button>
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
      <!-- 新增修改dialog -->
      <el-dialog :title="title" :visible.sync="addDialogVisible"
                 :destroy-on-close=true
                 :modal-append-to-body=false
                 :close-on-click-modal=true
                 width="50%" :close-on-press-escape=false>
        <el-form :model="addForm" ref="addForm" label-width="80px" :rules="rules">
          <el-row>
            <el-form-item label="全宗号" required prop="code">
              <el-input v-model="addForm.code"
                        placeholder="请输入全宗号"></el-input>
            </el-form-item>
            <el-form-item label="全宗名称" required prop="name">
              <el-input v-model="addForm.name"
                        placeholder="请输入全宗名称"></el-input>
            </el-form-item>
            <el-form-item label="选择机构" required prop="partyIdAyyay">
              <el-select filterable placeholder="请选择机构" v-model="addForm.partyIdAyyay" multiple @change="$forceUpdate()"
                         style="width: 100%"
                         clearable>
                <el-option v-for="organise in organises" :key="organise.id" :label="organise.organiseName"
                           :value="organise.id"/>
              </el-select>
            </el-form-item>
            <el-form-item prop="order" label="顺序号">
              <el-input v-model="addForm.order"
                        @input="addForm.order=addForm.order.replace(/[^\d]/g,'')"
                        placeholder="请输入顺序号"></el-input>
            </el-form-item>
            <el-form-item prop="fondType" label="类型" v-show="false">
              <select-dict v-model="addForm.fondType" type="archives.fondType"
                           placeholder="请选择类型"/>
            </el-form-item>
            <el-form-item prop="partyName" v-show="false" label="机构名称">
              <el-input v-model="addForm.partyName"
                        placeholder="请输入机构名称"></el-input>
            </el-form-item>
            <el-form-item prop="filePath" v-show="false" label="存储路径">
              <el-input v-model="addForm.filePath"
                        placeholder="请输入存储路径"></el-input>
            </el-form-item>
            <el-form-item prop="enabled" label="是否启用">
              <el-switch v-model="addForm.enabled" :active-value="active" style="margin-left: 20px"
                         :inactive-value="inactive" active-color="#13ce66"
                         inactive-color="#ff4949"></el-switch>
            </el-form-item>
          </el-row>
        </el-form>
        <span slot="footer" class="dialog-footer">
              <el-button type="info" @click="addDialogVisible = false">取 消</el-button>
              <el-button type="primary" @click="save" v-loading="loading">保 存</el-button>
          </span>
      </el-dialog>
      <el-dialog title="编码方案" :visible.sync="lookSchemeDialogVisiable"
                 :destroy-on-close=true
                 :modal-append-to-body=false
                 :close-on-click-modal=true
                 v-loading="lookSchemeDialogLoading" width="80%">
        <el-table :data="schemeData" border style="height: 50%">
          <column-index :page="page1"/>
          <el-table-column label="编码方案名称" fixed="left" prop="name" show-overflow-tooltip align="center">
            <template slot-scope="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>
          <el-table-column label="年度起" prop="startYear" show-overflow-tooltip align="center"
                           header-align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.startYear }}</span>
            </template>
          </el-table-column>
          <el-table-column label="年度止" prop="endYear" show-overflow-tooltip align="center"
                           header-align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.endYear }}</span>
            </template>
          </el-table-column>
          <el-table-column label="分类名称" align="center" header-align="center" fixed="right"
                           style="color: #1e89db">
            <template slot-scope="scope">
              <span style="color: #1e89db">{{ scope.row.categoryName }}</span>
            </template>
          </el-table-column>
        </el-table>
        <page :page="page1" @change="index=>set(index,fond)"/>
      </el-dialog>
    </div>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  mixins: [indexMixin, formMixin],
  data() {
    return {
      fond: {},
      title: '新增全宗',
      active: true,
      inactive: false,
      loading: false,
      lookSchemeDialogVisiable: false,
      lookSchemeDialogLoading: false,
      data: [],
      multipleSelection: [],
      schemeData: [],
      addForm: {
        code: '',
        name: '',
        fondType: '',
        partyName: '',
        filePath: '',
        defaultVal: '',
        enabled: false,
        partyIdAyyay: []
      },
      searchForm: {
        name: '',
        edit: false
      },
      addDialogVisible: false,
      optype: 'add',
      page1: {
        index: 1,
        size: 15,
        total: Number,
      },
      organises: [],
      organisesById: [],
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    timestampToTime(timestamp) {
      if (timestamp !== 0 && timestamp !== undefined) {
        return this.$moment(timestamp).format('YYYY')
      }
    },
    $init() {
      this.loadData()
      this.getOrganises()
    },
    async getOrganises() {
      const {data} = await this.$post('/organise/getAllDepartment')
      this.organises = data.data
    },
    beforeLoadData(index) {
      this.page.index = index
      this.loadData()
    },
    async loadData(params) {
      this.loading = true
      // params = Object.assign({}, this.page, {name: this.searchForm.name})
      await this.$http.post('/manage/fond/page', {name: this.searchForm.name, ...params}).then(({data}) => {
        if (data && data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        }
        this.loading = false
      })
    },
    showdialog() {
      this.reset1()
      this.optype = 'add'
      this.title = '新增全宗'
      this.addForm.enabled = true
      this.addDialogVisible = true
    },
    reset1() {
      this.addForm = {
        code: '',
        name: '',
        fondType: '',
        partyName: '',
        filePath: '',
        defaultVal: '',
        enabled: false
      }
    },
    async editForm(row) {
      this.optype = 'edit'
      this.title = '修改全宗'
      const {data} = await this.$post('/manage/fondorganise/page', {page: false, fondId: row.id})
      this.addForm = {...row, ...{partyIdAyyay: data.data.map(d => d.organiseId) || []}}
      this.addDialogVisible = true
    },
    //设置编码方案
    set(index, row) {
      // this.$router.push({path: '/archive/codingscheme', query: {id: row.id}})
      this.fond = row
      // this.$router.push({path: '/archive/manage/fond/setscheme', query: {id: row.id}})
      this.lookSchemeDialogLoading = true
      const param = Object.assign({}, {findId: this.fond.id, index: this.page1.index, size: this.page1.size})
      this.$http.post('/codingscheme/findSchemeByFondId', param)
          .then(({data}) => {
            if (data.success) {
              if (data.data) {
                this.schemeData = data.data.data
                for (const datum of this.schemeData) {
                  if (datum.startYear === 1970) {
                    datum.startYear = null
                  }
                  if (datum.endYear === 1970) {
                    datum.endYear = null
                  }
                }
                this.page1.index = Math.floor(data.data.start / data.data.size + 1)
                //this.page1.size = data.data.size
                this.page1.total = data.data.total
                this.lookSchemeDialogLoading = false
              }
              this.lookSchemeDialogVisiable = true
            } else {
              this.$message.error(data.message)
            }
          })
    },
    setTree(row) {
      this.$router.push({path: '/archive/manage/category', query: {id: row.id}})
    },
    save() {

      /*const re = /^[0-9a-zA-Z]*$/;  //判断字符串是否为数字和字母组合
      if (re.test(this.addForm.code) === false) {
        this.$message.error("全宗号只能是字母和数字的组合！")
        return
      }*/
      this.$refs.addForm.validate(valid => {
        if (valid) {
          this.addForm.businessId = "fond"
          this.addForm.partyId = this.addForm.partyIdAyyay.toString();
          let path
          if (this.optype === 'add') {
            path = '/manage/fond/insert'
          } else {
            path = '/manage/fond/update'
          }
          this.$http.post(path, this.addForm)
              .then(({data}) => {
                if (data.success) {
                  this.$message({
                    message: '操作成功！',
                    type: 'success'
                  });
                } else {
                  console.log(data)
                  this.$message.error(data.message)
                }
                this.loadData()
                this.addDialogVisible = false
              })
        }
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
      this.$confirm("此操作将删除选中数据, 是否继续？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.$http.post('/manage/fond/delete', param)
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
  },
  beforeRouteLeave (to, from, next) {
    this.addDialogVisible = false
    this.lookSchemeDialogVisiable = false
    next()
  }
}
</script>
