<template>
  <section>
    <nac-info title="查看报表统计" back>
      <search-component @search="search" style="display: inline;"/>
    </nac-info>
    <div class="table-container">
      <ve-histogram :data="chartData" style="margin-top: 30px"/>
      <ve-histogram :data="chartData1" style="margin-top: 30px"/>
    </div>
  </section>
</template>

<script>
import SearchComponent from "../searchComponent";

export default {
  name: "fileByClass",
  components: {SearchComponent},
  data() {
    return {
      resourceStatistics: {
        //全宗code
        fondCode: '',
        categoryCode: '',
        //年度
        vintages: ''
      },
      //报表数据
      chartData: {
        rows: [],
        columns: []
      },
      chartData1: {
        rows: [],
        columns: []
      }
    }
  },
  methods: {
    $init() {
      this.getListBySome(this.resourceStatistics)
    },

    //根据全宗code和年度统计
    async getListBySome(r) {
      const {data} = await this.$post('/resourceStatistics/page', Object.assign({page: false}, r))
      if (data) {
        this.chartData.columns = ['门类', '案卷目录数量', '文件目录数量', '案卷原文数量', '文件原文数量', '案卷原文大小（KB）', '文件原文大小 (KB)'];
        this.chartData1.columns = ['门类', '案卷原文大小（KB）', '文件原文大小 (KB)'];
        this.chartData.rows = [];
        this.chartData1.rows = [];
        data.data.forEach(i => {
          this.chartData.rows.push({
            '门类': i.categoryName,
            '案卷目录数量': i.arcArchiveNum,
            '文件目录数量': i.fileArchiveNum,
            '案卷原文数量': i.arcFileNum,
            '文件原文数量': i.fileFileNum,
          })
          this.chartData1.rows.push({
            '门类': i.categoryName,
            '案卷原文大小（KB）': i.arcFileSize,
            '文件原文大小 (KB)': i.fileFileSize
          })
        });
      }
    },
    search(r) {
      this.getListBySome(r)
    }
  }
}
</script>