<template>
  <div style="display: flex;flex-direction: row;height: 100%;">
    <el-card style="overflow: auto;" shadow="hover">
      <fond-tree :fondId="fondId" autoSelect @check="check" ref="fondTree" :withPermission="true"></fond-tree>
    </el-card>
    <el-card style="height: 99.5%;flex: 1;overflow: auto" shadow="hover">
      <el-tabs @tab-click="handleClick" v-model="activeName" v-show="archiveType === '1'">
        <el-tab-pane label="案卷级档案" name="first">
          <table-index title="档案管理" :fields="fields" path="/earchive" :insert="false" :edit="false"
                       @row-click="clickRow"
                       :delete="false"
                       :back="true"
                       ref="table0"
                       :defaultSearchForm="defaultSearchForm">
            <template v-slot:table-$btns="scope">
              <el-button type="text" size="mini" width="50" @click="addToCar(scope.row)">档案出库</el-button>
              <el-button type="text" size="mini" width="50" @click="details(scope.row)">异常详情</el-button>
              <!-- <el-button type="text" size="mini" width="60" @click="storageDetails(scope.row)">存储空间</el-button>-->
            </template>
          </table-index>
        </el-tab-pane>
        <el-tab-pane label="卷内文件档案" name="second">
          <table-index title="档案管理" :fields="fields" path="/earchive" :insert="false" :edit="false"
                       :delete="false"
                       :back="true"
                       ref="table1"
                       :defaultSearchForm="defaultSearchForm1">
            <template v-slot:table-$btns="scope">
              <el-button type="text" size="mini" width="50" @click="addToCar(scope.row)">档案出库</el-button>
              <el-button type="text" size="mini" width="50" @click="details(scope.row)">异常详情</el-button>
              <!--          <el-button type="text" size="mini" width="60" @click="storageDetails(scope.row)">存储空间</el-button>-->
            </template>
          </table-index>
        </el-tab-pane>
      </el-tabs>
      <table-index title="档案管理" :fields="fields" path="/earchive" :insert="false" :edit="false" :delete="false"
                   v-show="archiveType === '0'"
                   :back="true"
                   ref="table3"
                   :defaultSearchForm="defaultSearchForm">
        <template v-slot:table-$btns="scope">
          <el-button type="text" size="mini" width="50" @click="addToCar(scope.row)">档案出库</el-button>
          <el-button type="text" size="mini" width="50" @click="details(scope.row)">异常详情</el-button>
          <!-- <el-button type="text" size="mini" width="60" @click="storageDetails(scope.row)">存储空间</el-button>-->
        </template>
      </table-index>
    </el-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      fields: [
        {prop: 'fondCode', label: '全宗', width: "80", search: false},
        {prop: 'categoryCode', label: '门类', width: "60", search: false},
        {prop: 'archiveCode', label: '档号', search: true, required: true},
        {prop: 'title', label: '题名', search: true, align: 'center'},
        {prop: 'year', label: '年度', width: "80", edit: true, disabled: true},
        {prop: 'fileTime', label: '形成日期'},
        {prop: 'inDate', label: '入库时间', width: "90", dateFormat: true},
        {prop: 'outCount', label: '出库次数', width: "70"},
        /*{prop: 'lastTestDate', label: '最后检测时间', width: '100', dateFormat: true},
        {prop: 'testingNum', label: '检测次数', width: '70'},
        {
          prop: 'archiveStatus', label: '档案状态', width: '70', mapper: {
            '0': {label: '正常', show: 'success'},
            '1': {label: '异常', show: 'danger'}
          }
        },*/
        {prop: 'lastOutDate', label: '上次出库时间', dateFormat: true},
      ],
      defaultSearchForm: {
        flag: "archive", categoryId: '', fondId: '', title: '', ajdh: '', archiveCode: '',
        spaceId: this.$route.query.spaceId, classificationId: this.$route.query.classificationId
      },
      defaultSearchForm1: {
        flag: "archive", categoryId: '', fondId: '', title: '', ajdh: '', archiveCode: '',
        spaceId: this.$route.query.spaceId, classificationId: this.$route.query.classificationId
      },
      fondId: '',
      spaceId: '',
      classificationId: '',
      archiveType: '',
      activeName: 'first',
      tabIndex: '0'
    }
  },
  watch: {
    "$route.query.spaceId"(val) {
      this.defaultSearchForm.spaceId = val
      this.$refs.table.loadData();
    }
  },
  methods: {
    clickRow(row, column, event) {
      this.defaultSearchForm1.ajdh = row.archiveCode
    },
    $init() {
      // this.check()
    },
    loadData1() {
      if (this.archiveType === '' || this.archiveType === '0') {
        this.$refs.table3.reload(this.defaultSearchForm)
      } else {
        if (this.activeName === 'first') {
          this.$refs['table0'].reload(this.defaultSearchForm)
        } else {
          this.$refs['table1'].reload(this.defaultSearchForm1)
        }
      }
    },
    handleClick(tab) {
      this.tabIndex = tab.index
      if (tab.index === '1') {
        if (this.defaultSearchForm1.ajdh === '' || this.defaultSearchForm1.ajdh == null) {
          this.defaultSearchForm1.ajdh = 'all'
        }
      }
      this.$refs['table' + tab.index].reload()
    },
    //前置先生成批次信息
    addToCar(command) {
      this.$http.post('/earchive/batch/addToBatch', command).then(({data}) => {
        if (data.success) {
          this.$message.success(data.message)
        } else {
          this.$message.error(data.message)
        }
      })
    },
    async details(row) {
      this.$router.push({
        path: './alarm',
        query: {archiveId: row.id}
      })
    },
    async storageDetails(row) {
      this.$router.push({
        path: './spaces',
        query: {id: row.spaceId}
      })
    },
    check(v) {
      this.tabIndex = '0'
      this.defaultSearchForm.fondCode = this.$refs.fondTree.currentFond.code
      this.defaultSearchForm.categoryCode = v.data.code
      this.defaultSearchForm1.fondCode = this.$refs.fondTree.currentFond.code
      this.defaultSearchForm1.categoryCode = v.data.code
      this.archiveType = v.data.archiveType
      this.defaultSearchForm1.ajdh = 'all'
      if (this.archiveType === '' || this.archiveType === '0') {
        this.tabIndex = '0'
        this.$refs.table3.reload()
      } else {
        this.$refs.table0.reload()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .table_index .table_index_container .table-wrapper{
  height: 800px !important;
}
</style>
