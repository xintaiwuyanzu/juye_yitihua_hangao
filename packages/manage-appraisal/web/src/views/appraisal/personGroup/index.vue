<template>
  <section>
    <nac-info>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="名称">
          <el-input v-model="searchForm.groupName" placeholder="名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadData(0)">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addGroup">添加</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="multipleTable" :data="schemeData" border height="100%">
          <el-table-column label="序号" fixed align="center" width="50">
            <template v-slot="scope">
              {{ (page.index - 1) * page.size + scope.$index + 1 }}
            </template>
          </el-table-column>
          <el-table-column label="鉴定人员组名称" prop="groupName" show-overflow-tooltip align="center"
                           header-align="center"/>
          <el-table-column label="创建日期" prop="createDate"
                           dateFormat="YYYY-MM-DD HH:mm:ss"
                           show-overflow-tooltip
                           align="center"/>
          <el-table-column label="操作" align="center" header-align="center" fixed="right" width="240">
            <template v-slot="scope">
              <el-link type="primary" @click="getGroupById(scope.row)">编 辑</el-link>
              ｜
              <el-link type="primary" @click="removeById(scope.row)">删 除</el-link>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
          @current-change="handleCurrentChange"
          :current-page="page.index"
          :page-size="page.size"
          layout="total, prev, pager, next"
          :total="page.total">
      </el-pagination>
    </div>
    <el-dialog :title="title"
               :visible.sync="dialogShow"
               @close="resetForms"
               :close-on-click-modal=false
               :close-on-press-escape=false
               v-if="dialogShow"
               width="40%">
      <div>
        <el-form ref="form" :model="form" :rules="rules" label-position="right" label-width="100px">
          <el-form-item label="鉴定组名称" style="margin-top: 7px" prop="groupName" required>
            <el-input v-model="form.groupName" placeholder="请输入名称" style="width: 250px"></el-input>
          </el-form-item>
          <el-form-item label="鉴定人员" prop="personId" required>
            <el-select v-model="form.personId" multiple placeholder="请选择筛选规则" style="width: 250px">
              <el-option
                  v-for="item in personData"
                  :key="item.id"
                  :label="item.userName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="resetForms" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="saveScheme" v-loading="loading"
                   class="btn-submit">提 交
        </el-button>
      </div>
    </el-dialog>

  </section>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin";

export default {
  mixins: [indexMixin, formMixin],
  data() {
    return {
      title: '',
      dialogShow: false,
      page: {
        index: 1,
        size: 15,
        total: 0
      },
      searchForm: {},
      form: {},
      schemeData: [],
      openevelData: [],
      personData: [],
      options: [],
      detailedShow: false
    }
  },
  methods: {
    $init() {
      this.loadData(0)
      this.getPerson()
    },
    async getPerson(){
      const {data} = await this.$http.post('appraisalPerson/getPersonByOrgId')
      if(data.success){
        this.personData = data.data
      }
    },
    handleCurrentChange(val) {
      this.loadData(val - 1)
    },
    addGroup() {
      this.title = '添加'
      this.dialogShow = true
    },
    async getGroupById(row) {
      this.title = '编辑'
      const {data} = await this.$http.post('/appraisalPerson/getGroupById', {groupId: row.id})
      this.form = data.data.group
      this.$set(this.form, 'personId', data.data.person)
      this.dialogShow = true
    },
    loadData(row) {
      let index = row === undefined ? 0 : row
      this.loading = true
      this.$http.post('/appraisalPerson/getGroup', {
        groupName: this.searchForm.groupName,
        index: index,
        size: this.page.size
      }).then(({data}) => {
        if (data && data.success) {
          this.schemeData = data.data.data
          this.page.index = data.data.start + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
          this.loading = false
        }
      })
      this.loading = false
    },
    async selectType(value) {
      const {data} = await this.$http.post('/appraisalRule/getOpenevel',{type: value})

      this.options = data.data
    },
    async getOpenevel(row) {

      const {data} = await this.$http.post('/appraisalRule/getOpenevel',{type: row})

      this.options = data.data

    },
    saveScheme() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let url
          if (this.form.id) {
            url = 'appraisalPerson/editGroup'
          } else {
            url = '/appraisalPerson/addPersonGroup'
          }
          this.$http.post(url, Object.assign({}, this.form, {personId: this.form.personId.join(",")})).then(({data}) => {
            if (data && data.success) {
              this.$message.success("操作成功")
              this.loadData(0)
              this.resetForms()
            } else {
              this.$message.error(data.message)
            }
          })
        } else {

        }
      })
    },
    async removeById(row) {
      const {data} = await this.$http.post('/appraisalPerson/removePersonGroup', {groupId: row.id})
      if (data.success) {
        this.$message.success("操作成功")
        this.loadData(0)
      } else {
        this.$message.error(data.message)
      }
    },
    resetForms() {
      this.form = {}
      this.openevelData = []
      this.dialogShow = false
      this.title = ''
    },
    async detailed(row){
      const {data} = await this.$http.post('/appraisalPerson/getGroupById',{id: row.id})
      this.openevelData = data.data
      this.detailedShow = true
    }
  }
}
</script>
