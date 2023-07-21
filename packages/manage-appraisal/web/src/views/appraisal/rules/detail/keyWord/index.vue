<template>
  <section>
    <el-table :data="data" :border="true">
      <el-table-column type="index" label="序号" align="center" header-align="center" width="55">
        <template v-slot="scope">
          {{ (page.pageIndex - 1) * page.size + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="keyWord" label="关键词" align="center"></el-table-column>
      <el-table-column label="操作" width="120" header-align="center" align="center">
        <template v-slot="scope">
          <el-link type="primary" @click="edit(scope.row)">编辑</el-link>
          <el-divider direction="vertical"></el-divider>
          <el-link type="danger" @click="remove(scope.row)">删除</el-link>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        @current-change="index=>currentChange(index)"
        :current-page="page.pageIndex"
        :page-size="page.size"
        layout="total, prev, pager, next"
        :total="page.total"
    ></el-pagination>
    <edit-form ref="editForm" v-on:reload="$init"></edit-form>
  </section>
</template>
<script>

import editForm from './form'

export default {
  components: {
    editForm
  },
  props: {
    rulesId: {type: String},
    basisId: {type: String}
  },
  data() {
    return {
      data: [],
      page: {
        pageIndex: 1,
        size: 15,
        total: 0
      },
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    loadData(param) {
      console.log(param)
      if (this.page.pageIndex < 1) {
        this.page.pageIndex = 0
      } else {
        this.page.pageIndex = this.page.pageIndex - 1
      }
      this.$http.post("/appraisalKeyWord/page", Object.assign(this.page, param, {rulesId: this.rulesId}))
          .then(({data}) => {
            if (data.success) {
              this.data = data.data.data
              this.page.pageIndex = (data.data.start / data.data.size) + 1
              this.page.size = data.data.size
              this.page.total = data.data.total
            }
          })
    },
    async remove(row) {
      await this.$confirm('次操作将会删除选中数据，确定要删除吗？', '提示')
      this.$http.post("/appraisalKeyWord/delete", {id: row.id})
          .then(({data}) => {
            if (data.success) {
              this.$message.success("删除成功")
              this.$init()
            } else {
              this.$message.error(data.message)
            }
          })
    },
    addKeyWord() {
      this.$refs.editForm.form = {rulesId: this.rulesId, basisId: this.basisId}
      this.$refs.editForm.dialogVisible = true
    },
    edit(row) {
      this.$refs.editForm.form = row
      this.$refs.editForm.dialogVisible = true
    },
    currentChange(index) {
      this.page.pageIndex = index
      this.loadData()
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
