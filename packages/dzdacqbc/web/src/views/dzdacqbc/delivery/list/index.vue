<template>
  <section>
    <div class="index_main" v-loading="loading">
      <config-form ref="form"></config-form>
      <div class="table-container">
        <el-table :data="data" border height="100%">
          <el-table-column label="排序" type="index" align="center"/>
          <el-table-column prop="archiveName" label="批次" align="center" show-overflow-tooltip/>
          <el-table-column prop="archiveNumber" label="出库数量" align="center" show-overflow-tooltip/>

          <el-table-column prop="operatePerson" label="操作人" align="center" show-overflow-tooltip/>
          <el-table-column prop="examinePerson" label="审核人" align="center" show-overflow-tooltip/>
          <el-table-column prop="examineState" label="出库状态" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{ scope.row.examineState | examineStateFilter(scope.row.examineState) }}
            </template>
          </el-table-column>
          <el-table-column prop="saveTarget" label="存储位置" align="center" show-overflow-tooltip/>
          <el-table-column prop="deliveryTime" label="出库时间" align="center" show-overflow-tooltip>
            <template slot-scope="scope">
              {{
                scope.row.deliveryTime ? $moment(parseInt(scope.row.deliveryTime)).format('YYYY-MM-DD HH:mm:ss') : ''
              }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right"
                           show-overflow-tooltip>
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="showDelivery(scope.row)"
                         v-if="scope.row.examineState === '1'">出 库
              </el-button>
              <el-button type="text" size="small" @click="showDetail(scope.row)">详 情</el-button>
              <el-button type="text" size="small" @click="downloadByDeliveryId(scope.row)">下载</el-button>
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
    </div>

    <el-dialog title="出库" :visible.sync="deliveryDialogVisible" width="40%" center :before-close="beforeClose">
      <el-form :form="dataForm" ref="deliveryContent" label-width="130px" :rules="rules">
        <el-row>
          <el-form-item label="存储位置:" prop="saveTarget">
            <el-select filterable v-model="dataForm.saveTarget" placeholder="请选择存储空间名称" clearable style="width: 260px;">
              <el-option
                  v-for="item in spaces"
                  :key="item.id"
                  :label="item.spaceName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>

        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="deliveryDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="delivery">出 库</el-button>
          </span>
    </el-dialog>

    <el-dialog title="档案详情" :visible.sync="detailVisible" width="80%" center>
      <el-table :data="detailData" border ref="archiveTable" height="330">
        <el-table-column label="排序" type="index" align="center" show-overflow-tooltip/>
        <el-table-column prop="fondName" label="所属全宗" align="center" show-overflow-tooltip/>
        <el-table-column prop="categoryName" label="所属门类" align="center" show-overflow-tooltip/>
        <el-table-column prop="title" label="题名" align="center" show-overflow-tooltip/>
        <el-table-column prop="archiveCode" label="档号" align="center" show-overflow-tooltip/>
        <!--                    <el-table-column prop="durationStorage" label="保管期限" align="center"/>-->
        <el-table-column prop="annual" label="年度" align="center" show-overflow-tooltip/>
        <el-table-column label="成文时间" prop="written" align="center" show-overflow-tooltip/>
        <el-table-column prop="pages" label="页数" align="center" show-overflow-tooltip/>
        <el-table-column prop="classificationName" label="所属分类" align="center" show-overflow-tooltip/>
        <el-table-column prop="warehouseName" label="入库批次" align="center" show-overflow-tooltip/>
        <el-table-column label="操作" width="100" header-align="center" align="center" fixed="right"
                         show-overflow-tooltip>
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="downloadFile(scope.row)">下载</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
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
      deliveryDialogVisible: false,
      deliveryContent: {},
      rules: {
        saveTarget: [{required: true, trigger: 'blur', message: '请选择存储地址'}]
      },
      dataForm: {},
      detailVisible: false,
      detailData: [],
      spaces: [],
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  methods: {
    $init() {

      this.loadData()
    },
    getAllSpace() {
      this.$http.post('/spaces/page', {
        page: false
      }).then(({data}) => {
        if (data.success) {
          this.spaces = data.data
        }
      })
    },
    beforeClose() {
      this.deliveryContent.saveTarget = ''
      this.deliveryDialogVisible = false
    },
    apiPath() {
      return '/delivery'
    },
    editFormChange(data) {
      this.configForm = data
      this.editForm(this.configForm)
    },
    showDelivery(row) {
      this.getAllSpace()
      this.deliveryContent = row
      this.deliveryDialogVisible = true
    },
    delivery() {
      this.deliveryDialogVisible = false
      this.$http.post(this.apiPath() + '/delivery', {
        id: this.deliveryContent.id,
        target: this.dataForm.saveTarget
      }).then(({data}) => {
        if (data.success) {
          this.$message.success("出库成功")
          this.deliveryDialogVisible = false
          this.loadData()
        }
      })
    },
    loadData(params, useSearchForm) {
      this.loading = true
      if (useSearchForm && this.$refs.form && this.$refs.form.getSearchForm) {
        params = this.$refs.form.getSearchForm(params)
      }
      this.$http.post('delivery/page', {flag: 'delivery', ...params}).then(({data}) => {
        if (data && data.success) {
          this.data = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        }
        this.loading = false
      })
    },
    showDetail(row) {
      this.$router.push({path: '/dzdacqbc/delivery/list/detail', query: {deliveryId: row.id,path:'cqbcDeliveryDetail',pagePath:'cqbcDeliveryDetail/page'}})
      // this.$http.post('cqbcDeliveryDetail/page', {deliveryId: id, page: false}).then(({data}) => {
      //   if (data && data.success) {
      //     this.detailData = data.data
      //   }
      // })
      // this.detailVisible = true
    },
    // //下载单个zip文件
    downloadFile(row) {
      this.$http.post('delivery/download', {id: row.id}).then(({data}) => {
        if (data && data.success) {
          window.open(data.data)
        }
      })

    },
    //下载目录下全部zip文件
    downloadByDeliveryId(row) {
      this.$http.post('delivery/downloadByDeliveryId', {id: row.id}).then(({data}) => {
        if (data && data.success) {

          for (let i = 0; i < data.data.length; i++) {
            window.open(data.data[i])
          }

        }
      })

    },
  },
  filters: {
    examineStateFilter(v) {
      if (v === '0') return '待审核'
      if (v === '1') return '待出库'
      if (v === '2') return '已驳回'
      if (v === '3') return '已出库'
    },
    deliveryFilter(v) {
      if (v === '1') return '未出库'
      if (v === '2') return '待审核'
      if (v === '3') return '待出库'
      if (v === '4') return '已出库'
    }
  },
}
</script>


