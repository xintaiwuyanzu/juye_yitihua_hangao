<template>
  <section>
    <nac-info>
      <data-forms v-show="showDataForm" ref="DataForm" :fondIdData="fondIdData"
                  :total="datatotal.length" @func="myLoadData"
                  v-on:imp="impArchive" v-on:modelImp="myModelImp" v-on:getSelects="myGetSelects"/>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <el-row style="overflow: hidden">
        <el-col :span="3">
          <el-card shadow="hover">
            <div slot="header">
              <strong>实体档案分类</strong>
            </div>
            <!--                        <el-tree :data="typeData" @node-click="check" ref="typeTree" default-expand-all></el-tree>-->
            <kufangfondTree :fondId="fondId" autoSelect @check="check" ref="fondTree"
                            :withPermission="true"></kufangfondTree>

          </el-card>
        </el-col>
        <span v-show="showDataForm == false">请选择左侧实体档案分类树</span>
        <el-col :span="21" v-show="showDataForm" style="height: 100%">
          <el-card shadow="hover">
            <div class="table-container">
              <el-tabs @tab-click="handleClick1" v-model="activeName" v-show="ajlx === 'aj'">
                <el-tab-pane label="案卷级档案" name="first">
                  <table-index title="档案管理" :fields="fields" path="/entityFiles" :insert="false"
                               @row-click="clickRow"
                               :edit="false"
                               :delete="false"
                               ref="table0" :checkAble="true"
                               style="height: 800px;display: flex;flex-direction: column;padding: 0;overflow: auto"
                               :defaultSearchForm="defaultSearchForm">
                    <template v-slot:table-$btns="scope">
                      <el-link type="success" size="mini" @click="detail(scope.row)" width="50">
                        查看详情
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="primary" size="mini" @click="editForm(scope.row)" width="50">
                        编辑
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="warning" size="mini" @click="addJN(scope.row)" width="70">
                        添加卷内文件
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="primary" size="mini" @click="open(scope.row)" width="70">
                        开架
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="primary" size="mini" @click="off(scope.row)" width="70">
                        合架
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="danger" size="mini" @click="del(scope.row)" width="50">删除
                      </el-link>
                    </template>
                  </table-index>
                </el-tab-pane>
                <el-tab-pane label="卷内文件档案" name="second">
                  <table-index title="档案管理" :fields="fields" path="/entityFiles" :insert="false"
                               :edit="false"
                               :delete="false"
                               ref="table1"
                               style="height: 800px;display: flex;flex-direction: column;padding: 0;overflow: auto"
                               :defaultSearchForm="defaultSearchForm1">
                    <template v-slot:table-$btns="scope">
                      <el-link type="primary" size="mini" @click="editJN(scope.row)" width="50">编辑
                      </el-link>
                      <el-divider direction="vertical"></el-divider>
                      <el-link type="danger" size="mini" @click="del(scope.row)" width="50">删除
                      </el-link>
                    </template>
                  </table-index>
                </el-tab-pane>
              </el-tabs>
              <!-- table所在位置 -->
              <table-index title="档案管理" :fields="fields" path="/entityFiles" :insert="false" :edit="false"
                           :delete="false"
                           v-show="ajlx === 'wj' || ajlx === 'root'"
                           ref="table3" :checkAble="true"
                           style="height: 800px;display: flex;flex-direction: column;padding: 0;overflow: auto"
                           :defaultSearchForm="defaultSearchForm">
                <template v-slot:table-$btns="scope">
                  <el-link type="success" size="mini" @click="detail(scope.row)" width="50">查看详情
                  </el-link>
                  <el-divider direction="vertical"></el-divider>
                  <el-link type="primary" size="mini" @click="editForm(scope.row)" width="50">编辑
                  </el-link>
                  <el-divider direction="vertical"></el-divider>
                  <el-link type="danger" size="mini" @click="del(scope.row)" width="50">删除</el-link>
                </template>
              </table-index>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <imp-forms ref="imp" v-on:reload="reload" :key="key"></imp-forms>
    <model-imp ref="modelImp"></model-imp>
  </section>
</template>

<script>
import indexMixin from "@dr/auto/lib/util/indexMixin";
import DataForms from './form'
import impForms from './impForm'
import modelImp from './modelImp'
import kufangfondTree from '../kufangfondTree'

export default {
  mixins: [indexMixin],
  components: {DataForms, impForms, modelImp, kufangfondTree},
  data() {
    let fondId = this.$route.query.id
    if (!fondId) {
      fondId = this.id
    }
    return {
      fields: [
        {prop: 'title', label: '题名', align: 'center', width: "190", search: false},
        {prop: 'archiveCode', label: '档号', align: 'center', search: false},
        {prop: 'fondCode', label: '全宗号', search: false, width: "80"},
        {
          prop: 'archiveType', label: '类型', align: 'center', fieldType: 'select', url: 'archiveType/page',
          params: {page: false}, labelKey: 'archiveTypeName', valueKey: 'id', width: "80"
        },
        {
          prop: 'locId', label: '库房', align: 'center', fieldType: 'select', url: 'locationkufang/page',
          params: {page: false}, labelKey: 'name', valueKey: 'id', width: "90"
        },
        {
          prop: 'areaId', label: '区', align: 'center', fieldType: 'select', url: 'kufangArea/page',
          params: {page: false}, labelKey: 'areaName', valueKey: 'id', width: "90"
        },

        {prop: 'caseNo', label: '密集架', width: "100", align: 'center'},

        {
          prop: 'columnNum', label: 'A/B面', width: '80', align: 'center', mapper: {'1': "A面", '2': "B面"}
        },
        {prop: 'columnNo', label: '列号', width: "50", align: 'center'},
        {prop: 'layer', label: '层号', width: "50", align: 'center'},
        {
          prop: 'archiveBox', label: '盒号', width: '50', align: 'center', mapper: {'1': "件", '2': "盒"}
        },
        {
          prop: 'status', label: '状态', width: '50', align: 'center', mapper: {'0': "出库", '1': "入库"}
        },
      ],
      defaultSearchForm: {
        archiveType: '',
        title: '',
        archiveCode: '',
        archiveBox: '',
        caseNo: '',
        fondId: '',
        locId: ''
      },
      defaultSearchForm1: {
        archiveType: '',
        archiveCode: '',
        archiveBox: '',
        caseNo: '',
        fondId: '',
        locId: '',
        ajdh: ''
      },
      fondId: '',
      fondIdData: '',
      classCode: '',
      row: [],
      kufang: [],
      multipleSelection: [],
      key: 0,
      batchId: '',
      updatesBatch: [],
      showDataForm: false,
      typeData: [],
      archiveType: '',
      activeName: 'first',
      ajlx: '',
      tabIndex: '0',
      mulSelect: [],
      paramTo: {},
      datatotal: '',
    }
  },

  methods: {
    $init() {
      this.batchId = this.$route.query.batchId
      this.getTypeData()
    },
    myGetSelects() {
      if (this.ajlx === 'root' || this.ajlx === 'wj') {
        this.mulSelect = this.$refs.table3.tableSelection
      } else {
        this.mulSelect = this.$refs.table0.tableSelection
      }
      if (this.mulSelect.length > 0) {
        let choseIds = this.mulSelect.map(r => r.id)
        this.$refs.DataForm.realSelect(choseIds)
      } else {
        this.$message.warning('请选择要批量处理的档案！')
      }
    },
    apiPath() {
      return "/entityFiles"
    },
    selectEnable(row, rowIndex) {
      if (row.caseId === '' || row.caseId == null) {
        return true
      }
    },
    detail(row) {
      this.$router.push({
        path: this.$route.path + '/detail',
        query: {id: row.id, box: row.archiveBox, activeName: row}
      })
    },
    clickRow(row, column, event) {
      this.defaultSearchForm1.ajdh = row.archiveCode
    },
    check(row) {
      this.ajlx = row.data.aType
      this.showDataForm = true
      this.tabIndex = '0'
      this.archiveType = row.id
      this.$refs.DataForm.showBtn(row)
      this.defaultSearchForm.archiveType = this.archiveType
      this.defaultSearchForm1.archiveType = this.archiveType
      this.defaultSearchForm1.ajdh = 'all'
      if (this.ajlx === 'root' || this.ajlx === 'wj') {
        this.$refs.table3.reload(this.defaultSearchForm)
      } else {
        this.tabIndex = '0'
        this.activeName = 'first'
        this.$refs.table0.reload(this.defaultSearchForm)
      }

    },
    handleClick1(tab) {
      this.tabIndex = tab.index
      if (tab.index === '1') {
        if (this.defaultSearchForm1.ajdh === '' || this.defaultSearchForm1.ajdh == null) {
          this.defaultSearchForm1.ajdh = 'all'
        }
        this.loadTable(this.defaultSearchForm1)
      } else {
        this.loadTable(this.paramTo)
      }
    },
    loadTable(val) {
      if (this.ajlx === 'root' || this.ajlx === 'wj') {
        this.$refs.table3.loadData(val)
      } else {
        if (this.activeName === 'first') {
          this.$refs['table0'].loadData(val)
        } else {
          this.$refs['table1'].loadData(val)
        }
      }
    },
    getTypeData() {
      this.$http.post('archiveType/getAllTypeTree', {pid: 'root'})
        .then(({data}) => {
          if (data.success) {
            this.typeData = data.data ? data.data : []
            // if (this.typeData.length > 0) {
            //     this.check(this.typeData[0])
            // }
          }
        })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    del(row) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('entityFiles/del', {id: row.id})
          .then(({data}) => {
            if (data.success) {
              this.$message.success('删除成功')
              if (this.typeData.length > 0) {
                this.loadTable()
              }
            } else {
              this.$message.error('删除失败')
            }
          })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    editForm(row) {
      this.$refs.DataForm.showEdit(row)
    },
    addJN(row) {
      if (row.status == '0') {
        this.$message.warning("该案卷已出库，不能添加卷内文件")
        return
      }
      this.$refs.DataForm.addJNWJ(row.id)
    },
    editJN(row) {
      this.$refs.DataForm.editJN(row)
    },
    open(row) {
      this.$http.post('denseRack/open', row)
        .then(({data}) => {
          if (data.success) {
            this.$message.success('开架成功')
          } else {
            this.$message.error(data.message)
          }
        })
    },
    off(row) {
      this.$http.post('denseRack/off', row)
        .then(({data}) => {
          if (data.success) {
            this.$message.success('合架成功')
          } else {
            this.$message.error('合架失败')
          }
        })
    },
    impArchive(val) {
      if (this.ajlx === '' || this.ajlx === 'root') {
        this.$message.warning('请选择档案类型！')
      } else {
        val.ajlx = this.ajlx
        this.$refs.imp.show = true
        this.$refs.imp.getType(val)
      }
    },
    myModelImp() {
      this.$refs.modelImp.show = true
    },
    reload() {
      this.loadTable()
    },
    myLoadData(val) {
      this.paramTo = val
      this.loadTable(val)
      this.datatotal = val.archiveCode
    },
  },
}
</script>

<style scoped>

.el-row {
  flex: 1;
}

.el-col {
  height: 100%;
  display: flex;
}

.el-card {
  flex: 1;
  overflow: auto;
}
</style>
