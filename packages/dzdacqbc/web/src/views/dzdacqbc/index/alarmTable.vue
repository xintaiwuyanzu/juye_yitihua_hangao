<template>
  <section>
    <table-render :columns="columns" style="height: 400px;"
                  :data="data"
                  :page="page"
                  @size-change="s=>this.page.size=s"
                  @page-current-change="p=>this.loadData({pageIndex:p-1})"
                  showPage>
      <el-table-column label="操作" width="120" header-align="center" align="center">
        <template v-slot="scope">
          <el-button type="text"
                     @click="showProcess(scope.row)"
                     size="mini" width="33" v-show="scope.row.processState === '0'">处理
          </el-button>
        </template>
      </el-table-column>
    </table-render>


    <alarmdeal ref="alarmdeal"/>
  </section>
</template>
<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import alarmdeal from "../alarm/alarmdeal";

export default {
  mixins: [indexMixin],
  components: {alarmdeal},
  data() {
    return {
      //新代码
      columns: [
        {prop: 'alarmType', label: '报警类型', width: "100",  fieldType: 'select',
          mapper: {'archive': '电子档案报警', 'disk': '存储空间报警', 'tamper': '篡改检测报警', 'adjust': '档案调整'}},
        {prop: 'alarmContent', label: '报警内容'},
        {prop: 'createDate', label: '报警时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140},
        /*{prop: 'taskName', label: '任务名称', width: "100"},*/
        {
          prop: 'processState', label: '处理状态', width: "80", component: 'tag', showTypeKey: 'show', mapper: {
            '0': {label: '未处理', show: 'danger'},
            '1': {label: '已处理', show: 'success'}
          }, fieldType: 'select', edit: false
        }
      ],
      path: 'cqbcAlarm',
      defaultSearchForm: {alarmContent: ''}
    }
  },
  methods: {
    $init() {
      this.loadData()
    },
    showProcess(row) {
      // console.log(this.defaultSearchForm)
      // this.defaultSearchForm.alarmContent = ''
      this.$refs.alarmdeal.showDig(row)
    },
    /*apiPath() {
      return '/cqbcAlarm'
    },*/
  }
}
</script>
