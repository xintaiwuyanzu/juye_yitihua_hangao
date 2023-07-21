<template>
  <table-index title="年度检查" :fields="fields" path="zfjcCheck/" :insert="false" insertPath="zfjcCheck/add" :edit="false"
               :delete="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm"
               deleteMulti>
    <template slot-scole='form' slot='search-$btns'>
      <el-button type="primary" @click="$router.push({path:'/examine/zfjcCheck/edit', query: {checkType: 'nd'}})">添加
      </el-button>
      <el-button type="primary" @click="tongbao()" size="mini">下载检查通报</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text"
                 @click="$router.push({path: '/examine/zfjcCheck/edit', query: {id:scope.row.id,sendId: '1', infoId: null, path: 'zfjcCheck', checkType: 'nd'}})"
                 size="mini" width="40">编辑
      </el-button>
      <el-button type="text"
                 @click="addtz(scope.row)"
                 size="mini" width="45">检查通知单
      </el-button>
      <!--      <el-button type="text"
                       @clike="tongbao(scope.row)"
                       size="mini" width="45"
            >检查通报
            </el-button>-->
    </template>
  </table-index>

</template>
<script>
import indexMixin from '@/util/indexMixin'
import ConfigForm from './form'

/**
 * 待办任务列表
 */
export default {
  mixins: [indexMixin],
  components: {ConfigForm},
  data() {
    return {
      //改后
      defaultSearchForm: {checkType: 'nd'},
      fields: [
        {
          prop: 'pici',
          label: '批次',
          search: true,
          width: '150',
          edit: false,
          insert: false,
          component: 'text',
          route: true,
          routerPath: '/examine/detail',
          queryProp: ['id', 'formDefinitionId', {sendId: '1', infoId: null, path: 'zfjcCheck'}]
        },
        {prop: 'personName', label: '检查人员', width: '100', search: false, edit: false, insert: false, required: true},
        {prop: 'organiseName', label: '检查单位', width: '140', search: true, edit: false, insert: false, required: true},
        {prop: 'categoryName', label: '检查项', search: false, edit: false, insert: false , required: true},
        /*{
          prop: 'status', label: '状态', mapper: {
            '0': {label: '待审核', show: 'success'},
            '1': {label: '待检查', show: 'danger'},
            '2': {label: '办结', show: 'danger'}
          }, width: '80', fieldType: 'select', component: 'tag', edit: false, showTypeKey: 'show',
        }*/
      ],
    }
  },
  props: {
    isShow: {default: true}
  },
  methods: {
    //下载检查通知单
    addtz(row) {
      let url = "api/zfjcCheck/exptz?id=" + row.id
      window.open(url)
    },
    //下载检查通报
    tongbao() {
      if (this.selection.length === 0) {
        this.$message.error('请选择数据!')
      } else {
        let ids = ''
        for (let i = 0; i < this.selection.length; i++) {
          ids += this.selection[i].id + ','
        }
        let url = "api/zfjcCheck/exptb?ids=" + ids
        window.open(url)
      }

    },
  },
  computed: {
    /**
     * 映射table-index 选中数据
     * @returns {[]|*|*[]}
     */
    selection() {
      if (this.$refs.table && this.$refs.table.tableSelection) {
        return this.$refs.table.tableSelection
      } else {
        return []
      }
    }
  },
}
</script>
<style scoped>
.divshow {
  display: inline-block;
}
</style>
