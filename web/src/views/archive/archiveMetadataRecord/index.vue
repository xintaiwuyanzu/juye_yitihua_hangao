<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="form"></config-form>
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column label="时间" prop="createDate" dateFormat="YYYY-MM-DD HH:mm:ss" align="center"/>
          <el-table-column label="操作人" prop="changePersonName" align="center"/>
          <el-table-column label="档号" prop="archiveCode" align="center"/>
          <el-table-column label="类型" prop="changeType" align="center">
            <template slot-scope="scope">
              {{ scope.row.changeType|dict('archive.metadataChangeType') }}
            </template>
          </el-table-column>
          <el-table-column label="操作" header-align="center" align="center" fixed="right">
            <template slot-scope="scope">
              <el-link type="primary" @click="detail(scope.row)">详 情</el-link>
              <el-divider direction="vertical"/>
              <el-link type="primary" @click="remove(scope.row.id)">删 除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="index=>loadData({pageIndex:index-1})"
          :current-page.sync="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
      <el-dialog title="详情" :visible.sync="dialogVisible" width="60%" v-if="this.type=='EDIT'">
        <el-row>
          <el-col :span="12">
            <span style="color: red; font-size: 16px; font-weight: 400;">原数据</span>
            <div class="content-column" v-for="(val, key) in oldData" :key="key" v-show="show(key)">
              <span class="content-title">{{ key | fieldCodeFilter }}：</span>
              <el-tooltip class="item" effect="dark" :content="val" placement="top">
                <span class="content-words" :style="{'color':val === newData[key]?'black':'red'}">
                  {{ val | valueFilter }}
                </span>
              </el-tooltip>
            </div>
          </el-col>
          <el-col :span="12">
            <span style="color: green; font-size: 16px; font-weight: 400;">新数据</span>
            <div class="content-column" v-for="(val, key) in newData" :key="key" v-show="show(key)">
              <span class="content-title">{{ key | fieldCodeFilter }}：</span>
              <el-tooltip class="item" effect="dark" :content="val" placement="top">
                <span class="content-words" :style="{'color':val === oldData[key]?'black':'green'}">
                  {{ val | valueFilter }}
                </span>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </el-dialog>
      <el-dialog title="详情" :visible.sync="dialogVisible" width="30%" v-else>
        <el-row>
          <el-col :span="12">
            <div class="content-column" v-for="(val, key) in newData" :key="key" v-show="show(key)">
              <span class="content-title">{{ key | fieldCodeFilter }}：</span>
              <el-tooltip class="item" effect="dark" :content="val" placement="top">
                <span class="content-words" :style="{'color':val === oldData[key]?'black':'green'}">
                  {{ val | valueFilter }}
                </span>
              </el-tooltip>
            </div>
          </el-col>
        </el-row>
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from '@/util/indexMixin'

export default {
  data() {
    return {
      data: [],
      configForm: {},
      type: '',
      oldData: {},
      newData: {},
      dialogVisible: false,
      dict: ['archive.metadataChangeType'],
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    show(val) {
      return val !== 'id' && val !== 'status_info' && val !== 'createDate' && val !== 'updateDate'
          && val !== 'createPerson' && val !== 'updatePerson' && val !== 'ORGANISEID'
    },
    $init() {
      this.loadData()
    },
    apiPath() {
      return '/archiveMetadataRecord'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
    loadData(params, useSearchForm) {
      this.loading = true
      if (useSearchForm && this.$refs.form && this.$refs.form.getSearchForm) {
        params = this.$refs.form.getSearchForm(params)
      }
      this.$http.post(this.apiPath() + '/page', params).then(({data}) => {
        if (data && data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
          if (this.$refs.form) {
            this.$refs.form.searchForm = Object.assign(this.$refs.form.searchForm, {pageIndex: this.page.index - 1})
          }
        }
        this.loading = false
      })
    },
    remove(id) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$http.post(this.apiPath() + '/delete', {Id: id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success('删除成功！')
                this.loadData()
              } else {
                this.$message.error(data.message)
                this.loading = false
              }
            })
      })
    },
    detail(row) {
      this.type = row.changeType
      this.oldData = {}
      this.newData = {}
      if (row.oldStringFormData !== null) {
        this.oldData = JSON.parse(row.oldStringFormData)
      }
      if (row.newStringFormData !== null) {
        this.newData = JSON.parse(row.newStringFormData)
      }
      this.dialogVisible = true
    },
  },
  filters: {
    valueFilter(value) {
      if (!value) return ''
      if (value.length > 15) {
        return value.slice(0, 15) + '...'
      }
      return value
    },
    fieldCodeFilter(v) {
      if (v === 'FOND_CODE') return '全宗号'
      if (v === 'ARCHIVE_CODE') return '档号'
      if (v === 'TITLE') return '题名'
      if (v === 'KEY_WORDS') return '关键词'
      if (v === 'VINTAGES') return '年度'
      if (v === 'FILETIME') return '文件时间'
      if (v === 'SAVE_TERM') return '保管期限'
      if (v === 'DUTY_PERSON') return '责任人'
      if (v === 'AJDH') return '案卷档号'
      if (v === 'MLDM') return '门类代码'
      if (v === 'CATALOGUE_CODE') return '目录号'
      if (v === 'OPEN_SCOPE') return '开放范围'
      if (v === 'WJLX') return '文件类型'
      if (v === 'AJH') return '案卷号'
      if (v === 'CATE_GORY_CODE') return '门类代码'
      if (v === 'FILECODE') return '文号'
      return v
    },
  }
}
</script>

<style type="text/css">
.content-words {
  text-align: left;
  font-size: 16px;
  font-weight: 400;
}

.content-title {
  font-size: 16px;
  font-weight: 400;
}

.content-column {
  margin-top: 10px;
}

</style>
