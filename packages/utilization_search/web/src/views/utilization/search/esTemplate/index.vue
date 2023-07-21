<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <div style="float: right">
          <el-form-item>
            <el-button type="primary" size="mini" v-on:click="add">添 加</el-button>
          </el-form-item>
        </div>
      </el-form>
    </nac-info>
    <div class="archive" v-loading="loading">
      <el-card shadow="hover" style="flex:1">
        <el-table
            ref="table"
            :data="data"
            :stripe="true"
            :border="true"
            style="width: 100%" @selection-change="handleTableSelect">
          <el-table-column
              type="selection"
              width="40"/>
          <el-table-column
              type="index"
              label="序号"
              fixed
              align="center"
              header-align="center"
              width="55">
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column
              prop="templateType"
              label="模板类型"
              header-align="center"
              align="center"
              show-overflow-tooltip
          >
            <template v-slot="scope">
              <span v-if="scope.row.templateType === '1'">索引模板</span>
              <span v-if="scope.row.templateType === '2'">查询模板</span>
            </template>
          </el-table-column>
          <el-table-column
              prop="templateName"
              label="模板名称"
              header-align="center"
              align="center"
              show-overflow-tooltip
              width="100"
          >
          </el-table-column>
          <el-table-column
              prop="description"
              label="描述"
              header-align="center"
              align="center"
              show-overflow-tooltip
              width="150"
          >
          </el-table-column>
          <el-table-column
              prop="createDate"
              label="创建时间"
              header-align="center"
              align="center"
              dateFormat="YYYY-MM-DD HH:mm:ss"
              show-overflow-tooltip
              width="150"
          >
            <template v-slot="scope">
              {{ scope.row.createDate|datetime }}
            </template>
          </el-table-column>
          <el-table-column label="创建人" prop="createPerson" show-overflow-tooltip align="center"
                           header-align="center" width="150">
            <template v-slot="scope">
              {{ dataFilter1(scope.row.createPerson) }}
            </template>
          </el-table-column>
          <el-table-column
              prop="updateDate"
              label="修改时间"
              header-align="center"
              align="center"
              dateFormat="YYYY-MM-DD HH:mm:ss"
              show-overflow-tooltip
              width="150"
          >
            <template v-slot="scope">
              {{ scope.row.updateDate|datetime }}
            </template>
          </el-table-column>
          <el-table-column label="修改人" prop="updatePerson" show-overflow-tooltip align="center"
                           header-align="center" width="150">
            <template v-slot="scope">
              {{ dataFilter1(scope.row.updatePerson) }}
            </template>
          </el-table-column>
          <el-table-column
              fixed="right"
              label="操作"
              header-align="center"
              align="center"
              width="200">
            <template v-slot="scope">
              <span> <el-link type="primary" @click="update(scope.row)">编 辑</el-link>
              | </span>
              <el-link type="success" @click="preview(scope.row)">重建</el-link>
              <span>
              | </span>
              <el-link type="primary" @click="deleteTemplate(scope.row)">删除模板</el-link>
              <span>
              | </span>
              <el-link type="danger" @click="remove(scope.row.id)">删除</el-link>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination style="text-align:right;"
                       @current-change="index=>loadData(index)"
                       :current-page.sync="page.index"
                       :page-size="page.size"
                       layout="total, prev, pager, next,jumper,->"
                       :total="page.total">
        </el-pagination>
      </el-card>
      <el-dialog :title="title" width="80%"
                 :close-on-click-modal=false
                 :destroy-on-close="true"
                 :close-on-press-escape=false
                 :visible.sync="sourceDialog">
        <el-form :model="addForm" :rules="rules" label-width="80px" ref="sourceForm">
          <el-form-item label="模板类型" prop="templateType" label-width="80px" required>
            <el-select v-model="addForm.templateType">
              <el-option label="索引模板" value="1"></el-option>
              <el-option label="查询模板" value="2"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="模板名称" prop="templateName" required>
            <el-input v-model="addForm.templateName" maxlength="100"/>
          </el-form-item>
          <el-form-item label="描述" prop="description" required>
            <el-input v-model="addForm.description" maxlength="100"/>
          </el-form-item>
          <el-form-item label="内容" prop="templateContent">
            <div style="height: 40vh">
              <el-input type="textarea" v-model="addForm.templateContent"
                        :rows="15"
              ></el-input>
            </div>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" size="mini" @click="sourceDialog=false">取 消</el-button>
          <el-button type="primary" size="mini" @click="save">保 存</el-button>
        </div>
      </el-dialog>
    </div>
  </section>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  name: "index.vue",
  mixins: [indexMixin, formMixin],
  data() {
    return {
      searchForm: {
      },
      options: [],
      addForm: {
      },
      sourceDialog: false,
      optype: 'add',
      id: '',
      title: ''
    }
  },
  methods: {
    $init() {
      this.dataFilter()
      this.loadData(1)
    },
    loadData(index) {
      const params = Object.assign({}, this.page, {
        pageIndex: index - 1,
        pageSize: this.page.size
      })
      this.$http.post('/esTemplate/page', params).then(({data}) => {
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
    add() {
      this.addForm = Object.assign({})
      /*this.addForm.compilationContent = ''*/
      this.addForm.status = '0'
      if (this.$refs.sourceForm) {
        this.$nextTick(() => {
          this.$refs.sourceForm.clearValidate()
        })
        //this.$refs.sourceForm.clearValidate()
      }
      this.sourceDialog = true
      this.title = '添加'
      this.optype = 'add'
    },
    dataFilter() {
      this.$http.post('/person/page', {page: false}).then(({data}) => {
        if (data.success) {
          this.options = data.data
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    dataFilter1(row) {
      for (const argumentsKey in this.options) {
        if (row === this.options[argumentsKey].id) {
          return this.options[argumentsKey].userName
        }
      }
    },
    update(row) {
      if (this.$refs.sourceForm) {
        //this.$refs.sourceForm.clearValidate()
        this.$nextTick(() => {
          this.$refs.sourceForm.clearValidate()
        })
      }
      this.sourceDialog = true
      this.addForm = Object.assign({}, row)
      this.optype = 'edit'
      this.title = '编辑'
    },
    remove(id) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$http.post('/esTemplate/delete', {Id: id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success('删除成功！')
                this.loadData(1)
              } else {
                this.$message.error(data.message)
                this.loading = false
              }
            })
      })
    },
    save() {
      this.$refs.sourceForm.validate(valid => {
        if (valid) {
          let url
          if (this.optype === 'add') {
            url = '/esTemplate/insert'
          } else {
            url = '/esTemplate/update'
          }
          if (this.addForm.compilationContent) {
            if (this.addForm.templateContent.length > 100000) {
              return this.$message.error('内容超过100000,请修改!')
            }
          }
          this.$http.post(url, this.addForm).then(
              ({data}) => {
                if (data.success) {
                  this.$message.success('操作成功！')
                  this.sourceDialog = false
                  this.loadData(1)
                } else {
                  this.$message.error(data.message)
                }
                this.addForm = {}
              })
        }
      })
    },
    preview(row) {
      this.$http.post('/esTemplate/rebuildTemplate', {id: row.id}).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功！')
            } else {
              this.$message.error('创建模板失败!')
            }
          })
    },
    deleteTemplate(row){
      this.$http.post('/esTemplate/deleteTemplate', {id: row.id}).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功！')
            } else {
              this.$message.error('')
            }
          })
    },
    dateFormatter(r, c, cell) {
      if (cell) {
        return this.$moment(cell).format('YYYY-MM-DD HH:mm:ss')
      }
      return '-'
    },
    searchIndexTemplate(){

    }
  },
  beforeRouteLeave (to, from, next) {
    this.sourceDialog = false
    next()
  }
}
</script>

<style lang="scss">
.typeName {
  width: 100%;
  color: #2d79f0;
  font-size: 18px;
  line-height: 25px;
}

.archive {
  flex-direction: row;
  display: flex;
  flex: 1;
  margin: 8px;
  overflow: auto;

  .el-card {
    height: calc(100% - 2px);
    overflow: auto;

    .el-card__body {
      padding: 0px;
      height: 100%;
      display: flex;
      flex-direction: column;

      > section {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: auto;

        .table-container {
          flex: 1;
          overflow: auto;
        }
      }
    }
  }

  .formListContainer {
    > section {
      flex: 1
    }
  }

  .centered {
    text-align: center;
  }

  .table1 table td {
    border: 1px solid #000;
    text-align: center;
    border-collapse: collapse;
    padding: 10px 30px;
  }

  .table1 table th {
    border: 1px solid #000;
    text-align: center;
    border-collapse: collapse;
    padding: 10px 30px;
  }

}
</style>
