<template>
  <section>
    <nac-info >
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="年度">
          <el-date-picker
                  style="float: right;"
                  v-model="year"
                  type="year"
                  placeholder="请选择年度"
                  clearable>
          </el-date-picker>
        </el-form-item>
        <!--<el-form-item label="分类" style="margin-left: 10px">
          <el-select v-model="form.category" style="width: 150px;float: right;margin-right: 20px"
                     placeholder="请选择" clearable>
            <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.name"
                    :value="item.code">
            </el-option>
          </el-select>
        </el-form-item>-->

        <el-form-item style="margin-left: 10px">
          <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
       <!-- <el-form-item>
          <el-button type="primary" @click="download">生成报告</el-button>
        </el-form-item>-->
      </el-form>
    </nac-info>
    <div class="index_main" v-loading="loading">
      <div class="table-container">
        <el-table ref="applyTable"
                  :data="data" border
                  highlight-current-row
                  v-loading="loading">
          <column-index :page="page"/>
          <el-table-column label="导出类型" prop="type" show-overflow-tooltip align="center">
            <template slot-scope="scope">
              {{
                scope.row.type|dict({
                  'dazy': "档案资源统计",
                  'lyqk': "利用情况统计",
                  'fltj': "分类统计",
                  'OTHER': "默认分类"
                })
              }}
            </template>
          </el-table-column>
          <el-table-column label="导出人" prop="exportUser" align="center"></el-table-column>
          <el-table-column label="导出时间" prop="createDate"
                           dateFormat="YYYY-MM-DD HH:mm:ss"
                           align="center"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-link type="primary" @click="download(scope.row)">下载</el-link>
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

  </section>
</template>
<script>
import indexMixin from '@archive/core/src/util/indexMixin'
import "echarts/lib/component/title";
import "echarts/lib/component/toolbox";

/**
 * 批次列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      fondid: '',
      loading: false,
      data: [],
      page: {
        index: 1,
        size: 15,
        total: 0
      },
      options: [{
        value: 'XXGK',
        label: '信息公开'
      },{
        value: 'ws',
        label: '文书'
      }],
      form: {},
      category: [],
      year: '',
      tableData: [],
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    async loadData(row) {
      let index = row == undefined ? 0 : row
      if (this.year != '' && this.year != null) {
        this.form.year = new Date(this.year).getFullYear()
      }
      const {data} = await this.$post('/reportform/getReportForm',{
        vintages: this.form.year,
        index: index,
        size: this.page.size
      })
      this.data = data.data.data
      this.page.index = data.data.start + 1
      this.page.size = data.data.size
      this.page.total = data.data.total
      this.form.year = ''
    },
    async download(row){
      window.open(row.downloadPath, '_blank')
    },
    handleCurrentChange(val) {
      this.loadData(val - 1)
    }
  },

}
</script>

