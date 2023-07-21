<template>
  <section>
    <nac-info title="非结构化原文"></nac-info>
    <div style="display: flex;flex-direction: row;height: 100%;">
      <el-card style="overflow: auto;" shadow="hover">
        <fond-tree :fondId="fondId" autoSelect @check="check" ref="fondTree" :withPermission="true"></fond-tree>
      </el-card>
      <div class="index_main" style="padding-left: 10px">
        <el-tabs @tab-click="handleClick" v-model="activeName">
          <el-tab-pane label="待入库" name="first">
            <table-index :showTitle="false" :fields="fields" :defaultSearchForm="defaultSearchForm"
                         style="height:70vh;display: flex;flex-direction: column"
                         path="unStructuredText" ref="table0" :insert="false" :edit="false">
              <template v-slot:search-$btns="scope">
                <el-button type="primary" width="20" @click="BatchWarehousing">批量入库</el-button>
                <el-button type="primary" width="20" @click="add">添加</el-button>
                <el-button type="primary" width="20" @click="flush">刷新</el-button>
                <el-dialog
                    title="添加非结构化原文"
                    :visible.sync="dialogVisible"
                    width="80%">
                  <el-form label-width="80px" :model="form">
                    <el-form-item label="全宗">
                      <el-input v-model="form.fondName"></el-input>
                    </el-form-item>
                    <el-form-item label="门类">
                      <el-input v-model="form.categoryName"></el-input>
                    </el-form-item>
                    <el-form-item label="年度">
                      <el-input v-model="form.ND"></el-input>
                    </el-form-item>
                  </el-form>
                  <span slot="footer" class="dialog-footer">
                    <el-button @click="dialogVisible = false">取 消</el-button>
                    <el-button @click="submit" type="primary">确 定</el-button>
                  </span>
                </el-dialog>
              </template>
              <template v-slot:table-$btns="scope">
                <el-button @click="detail(scope.row.fondId,true)" type="text" width="20" style="float: left">标注
                </el-button>
              </template>
            </table-index>
          </el-tab-pane>
          <el-tab-pane label="已入库" name="second">
            <table-index :defaultSearchForm="defaultSearchForm" :fields="fields" :showTitle="false"
                         path="unStructuredText"
                         ref="table1" :insert="false" :edit="false">
              <template v-slot:search-$btns="scope">
                <el-button type="primary" width="20" @click="flush">刷新</el-button>
              </template>
              <template v-slot:table-$btns="scope">
                <el-button @click="detail(scope.row.fondId,false)" type="text" width="20" style="float: left">查看
                </el-button>
              </template>
            </table-index>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </section>
</template>

<script>
  import formMixin from "@dr/auto/lib/util/formMixin";

  export default {
    name: "index",
    mixins: [formMixin],
    data() {
      return {
        fondId: '',
        fields: [
          {
            prop: 'fondName', label: '全宗', search: false,
            align: 'center'
          },
          {
            prop: 'categoryName', label: '门类', search: false,
            align: 'center'
          },
          {prop: 'nd', label: '年度', search: false},
          {prop: 'archiveCode', label: '档号', search: false},
          {prop: 'timing', label: '题名', search: true},
          {prop: 'relationNum', label: '三元组数', search: false},
          {
            prop: 'status', label: '入库状态', mapper: {
              '0': {label: '待入库', show: ''},
              '1': {label: '已入库', show: 'success'}
            },
            component: 'tag'
          },

        ],
        dialogVisible: false,
        form: {},
        activeName: 'first',
        defaultSearchForm: {fondCode: '', categoryCode: '', status: '0'},
        tableName: 'table0'
      }
    },
    methods: {
      $init() {
      },
      async submit() {
        const {data} = await this.$post('/unStructuredText/insert', Object.assign(this.form, {status: '0'}))
        if (data && data.success) {
          this.$refs[this.tableName].loadData();
        }
        this.dialogVisible = false

      },
      check(v) {
        this.defaultSearchForm.fondCode = this.$refs.fondTree.currentFond.code
        // console.log(this.$refs.fondTree.currentFond.code)
        // console.log(v.data.code)
        this.defaultSearchForm.categoryCode = v.data.code
        this.$refs[this.tableName].reload()
      },
      add() {
        this.dialogVisible = true
      },
      async flush() {
        const {data} = await this.$post('/unStructuredText/flush')
        if (data && data.success) {
          this.$message.success('刷新成功')
          this.$refs[this.tableName].loadData();
        } else {
          this.$message.success('刷新失败')

        }

      },
      handleClick(tab) {
        this.defaultSearchForm.status = tab.index
        this.tableName = 'table' + tab.index
        this.$refs[this.tableName].loadData()
      },
      BatchWarehousing() {

      },
      detail(fondId, edit) {
        this.$router.push({
          path: "/mapping/UnStructuredText/tagging",
          query: {fondId: fondId, edit: edit}
        })
      }
    }
  }


</script>
