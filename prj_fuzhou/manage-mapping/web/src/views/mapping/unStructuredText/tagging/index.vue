<template>
  <section>
    <nac-info :title='!this.$route.query.id?"添加非结构化原文":"编辑非结构化原文"' back>
      <el-button type="primary" width="20" @click="addVisible=true" v-if="show==='true'">添加关系</el-button>
      <el-button type="primary" width="20" @click="Warehousing" v-if="show==='true'">入库</el-button>
    </nac-info>
    <!--index_main-->
    <div class="body">
      <div class="left">
        <el-tabs v-model="activeName" type="card" class="tabs">
          <el-tab-pane label="OFD阅读" name="first" style="height: 100%">
            <c-k-editor-bar v-model="management.compilationContent"
                            style="height: 100%"/>
          </el-tab-pane>
          <el-tab-pane label="OCR文本" name="second" style="height: 100%">
            <c-k-editor-bar v-model="management.compilationContent"
                            style="height: 100%"/>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="right">
        <el-table :data="keyRelation"
                  border height="750" ref="keyRelation">
          <el-table-column property="sourceName" label="源对象" width="150"></el-table-column>
          <el-table-column property="relationName" label="关系" width="200"></el-table-column>
          <el-table-column property="targetName" label="目标对象"></el-table-column>
          <el-table-column
              v-if="show==='true'"
              fixed="right"
              label="操作"
              width="100">
            <template slot-scope="scope">
              <el-button @click="handleClick(scope.$index,scope.row)" type="text" size="small">编辑</el-button>
              <el-button
                  @click.native.prevent="deleteRow(scope.$index, keyRelation[scope.$index])"
                  type="text"
                  size="small">
                移除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog title="添加关系" :visible.sync="addVisible">
      <el-form :model="form">
        <el-form-item label="源对象" label-width="80px">
          <el-input v-model="form.sourceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="关系" label-width="80px">
          <el-input v-model="form.relationName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="目标对象" label-width="80px">
          <el-input v-model="form.targetName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addVisible = false">取 消</el-button>
        <el-button @click="save" type="primary">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="编辑功能" :visible.sync="editVisible">
      <el-form :model="relationRow">
        <el-form-item label="源对象" label-width="80px">
          <el-input v-model="relationRow.sourceName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="关系" label-width="80px">
          <el-input v-model="relationRow.relationName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="目标对象" label-width="80px">
          <el-input v-model="relationRow.targetName" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editVisible = false">取 消</el-button>
        <el-button @click="editTable" type="primary">确 定</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
  import splitPane from 'vue-splitpane'

  export default {
    name: "index",
    components: {history, splitPane},
    data() {
      return {
        activeName: 'first',
        //全宗卷信息
        management: {},
        form: {},
        FondId: '',
        editVisible: false,//编辑弹窗
        addVisible: false,//添加弹窗
        relationRow: {},//当前行对象
        tableIndex: -1,
        //关键词关系
        keyRelation: [],
        show: this.$route.query.edit
      }
    },
    methods: {
      $init() {
        this.tagData()
      },
      async tagData() {
        this.FondId = this.$route.query.fondId
        const {data} = await this.$post("fondRelation/getRelationById", {FondId: this.FondId})
        if (data && data.success) {
          this.keyRelation = []
          data.data.forEach(i => {
            this.keyRelation.push({
              id: i.id, sourceName: i.sourceName, relationName: i.relationName, targetName: i.targetName
            })
          })
        }
      },
      async save() {
        this.form.fondId = this.$route.query.fondId
        const {data} = await this.$post('fondRelation/insert', this.form)
        if (data && data.success) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          await this.tagData()
          this.addVisible = false
        }
      },
      handleClick(index, row) {
        this.tableIndex = index
        this.relationRow = JSON.parse(JSON.stringify(row))
        this.editVisible = true
      },
      deleteRow(index, rows) {
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const {data} = await this.$http.post('fondRelation/delete', {id: rows.id})
          if (data && data.success) {
            this.tagData()
            this.$message.success('删除成功')
          } else {
            this.$message.success('删除失败')
          }

        })
      },
      async editTable() {
        this.relationRow.FondId = this.$route.query.fondId
        const {data} = await this.$post("fondRelation/update", this.relationRow)
        if (data && data.success) {
          this.keyRelation[this.tableIndex] = this.relationRow
          this.$message({
            showClose: true,
            message: '修改成功',
            type: 'success'
          });
          this.tagData()
          this.editVisible = false
        }
      },
      Warehousing() {

      }
    },
  }
</script>
<style lang="scss" scoped>
  .body {
    padding: 20px;
    background-color: white;
    display: flex;
    justify-content: space-between;
  }

  .left {
    display: inline-block;
    width: 50%;
    height: auto;

    .el-tabs__content {
      height: 100%;
    }

    .el-form-item__content {
      height: 100%;
    }

    .w-e-text {
      max-height: 100%;
    }
  }

  .right {
    display: inline-block;
    float: right;
    padding: 47px 10px;
    width: 48%;
    justify-content: space-between;
  }

</style>