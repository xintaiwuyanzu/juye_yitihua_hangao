<template>
  <table-index title="编研任务" :fields="fields" path="compilationtask" :edit="false" :delete="false"
               :default-insert-form="defaultInsertForm" :defaultSearchForm="defaultSearchForm" ref="table">
    <template v-slot:table-$btns="{row}">
      <el-button type="text" @click="$refs.table.showEdit(row)" size="mini" width="30" v-show="row.status==='0'">编辑
      </el-button>
      <el-button type="text" @click="deleteOne(row)" size="mini" width="30" v-show="row.status==='0'">删除</el-button>
      <!--      <el-button type="text" @click="routeSearch(row)" width="60" v-show="row.status==='0'">搜索素材</el-button>-->
      <el-button type="text" @click="publish(row,'1')" width="30" v-show="row.status==='2'&&row.publishStatus==='0'">
        发布
      </el-button>
      <el-button type="text" @click="publish(row,'0')" width="30" v-show="row.status==='2'&&row.publishStatus==='1'">
        下线
      </el-button>
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
          align: 'left',
          routerPath: '/compilation/edit',
          queryProp: ['id', 'compilationTitle', 'status']
        },
        /*{
          prop: 'taskType',
          label: '类型',
          fieldType: 'select',
          mapper: {'DSJ': '大事记', 'QZJS': '全宗介绍'}
        },*/
        {
          prop: 'templateId', label: '编研模板', required: false, autosize: true, search: false, align: 'center',
          fieldType: 'select',
          labelKey: 'templateName',
          url: 'compilationTemplate/page',
          params: {page: false, businessType: 'compilation'}
        },
        {prop: 'compilationAbstract', label: '摘要', type: 'textarea', width: 140},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false},
        {
          prop: 'status', label: '审批状态',
          component: 'tag',
          showTypeKey: 'show',
          mapper: {
            '0': {label: '待提交', show: 'info'},
            '1': {label: '审批中', show: 'danger'},
            '2': {label: '通过', show: 'success'},
            '3': {label: '未通过', show: 'warning'}
          }, width: '80', fieldType: 'select', edit: false
        },
        {
          prop: 'publishStatus', label: '发布状态',
          component: 'tag',
          showTypeKey: 'show',
          mapper: {
            '0': {label: '未发布', show: 'info'},
            '1': {label: '已发布', show: 'danger'},
          }, width: '80', fieldType: 'select', edit: false
        },
        {
          prop: 'compilationDescribe', label: '描述', type: 'textarea', width: 140
        }
      ],
      defaultInsertForm: {status: '0', publishStatus: '0'},
      defaultSearchForm: {type: 'task', status: '2'}//只查询未通过之外的数据
    }
  },
  setup() {
    return useArchiveCar()
  }
  ,
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
    }
    ,
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
    }
    ,
    async deleteOne(row) {
      await this.$refs.table.remove(row.id)
      await this.$refs.table.reload()
    }
    ,
    async publish(row, publishStatus) {
      try {
        const v = await this.$confirm("确认发布？", '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        })
        if (v) {
          const {data} = await this.$http.post('/compilationtask/publish', {id: row.id, publishStatus: publishStatus})
          if (data.success) {
            this.$message.success("发布成功");
            await this.$refs.table.reload()
          } else {
            this.$message.error(data.message)
          }
        }
      } catch {
      }
    }
  }
}
</script>