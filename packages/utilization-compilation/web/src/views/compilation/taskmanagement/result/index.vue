<template>
  <table-index title="编研成果管理" :fields="fields" path="compilationtask" :edit="false" :delete="false" :insert="false"
               :default-insert-form="defaultInsertForm" :defaultSearchForm="defaultSearchForm" ref="table">
    <template v-slot:table-$btns="{row}">
      <el-button type="text" @click="publish(row,'1','发布')" width="20"
                 v-show="row.status==='2'&&row.publishStatus==='0'">
        发布
      </el-button>
      <el-button type="text" @click="publish(row,'0','下线')" width="20"
                 v-show="row.status==='2'&&row.publishStatus==='1'">
        下线
      </el-button>
      <el-button type="text" @click="htmlToPdf(row)" width="15">
        导出
      </el-button>
<!--      <el-button type="text" @click="push(row)" width="90">
        推送至全宗卷
      </el-button>-->
    </template>
  </table-index>
</template>
<script>
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";

export default {
  data() {
    return {
      fields: [
        {
          prop: 'compilationTitle',
          label: '编研主题',
          required: true,
          search: true,
          route: true,
          component: 'text',
          routerPath: '/compilation/edit',
          align: 'left',
          queryProp: ['id', 'compilationTitle']
        },
        // {prop: 'templateId', label: '编研模板'},
        {prop: 'compilationAbstract', label: '摘要', type: 'textarea', width: 200},
        {prop: 'publishDate', label: '发布（下线）时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false},
        {
          prop: 'publishStatus', label: '发布状态',
          component: 'tag',
          showTypeKey: 'show',
          mapper: {
            '0': {label: '未发布', show: 'info'},
            '1': {label: '已发布', show: 'danger'},
          }, width: '80', fieldType: 'select', edit: false
        },
        {prop: 'compilationDescribe', label: '描述', type: 'textarea', width: 140}
      ],
      defaultInsertForm: {status: '0', publishStatus: '0'},
      defaultSearchForm: {type: 'result', status: '2'}//只查询通过的数据
    }
  },
  setup() {
    return useArchiveCar()
  },
  methods: {
    /**
     * 跳转查档页面
     * @param row
     */
    routeSearch(row) {
      this.archiveCarData.archiveCar.id = row.id
      this.$router.push({
        path: `/utilization/search`,
        query: {keyword: row.compilationTitle}
      })
    },
    /**
     * 跳转编研页面
     * @param row
     */
    start(row) {
      this.$router.push({
        path: `/compilation/edit`,
        query: {
          id: row.id,
          compilationTitle: row.compilationTitle,
          templateId: row.templateId,
          status: row.status
        }
      })
    },
    async publish(row, publishStatus, active) {
      try {
        const v = await this.$confirm("确认" + active + "?", '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        })
        if (v) {
          const {data} = await this.$http.post('/compilationtask/publish', {id: row.id, publishStatus: publishStatus})
          if (data.success) {
            this.$message.success(active + "成功");
            await this.$refs.table.reload()
          } else {
            this.$message.error(data.message)
          }
        }
      } catch {
      }
    },
    async htmlToPdf(row) {
      window.open(`api/compilationtask/htmlToPdf?id=` + row.id)
    },
    async push(row) {
      const {data} = await this.$http.post('/compilationtask/push', {id: row.id})
      if (data.success) {
        this.$message.success("推送成功！");
      } else {
        this.$message.error(data.message)
      }
    }
  }
}
</script>