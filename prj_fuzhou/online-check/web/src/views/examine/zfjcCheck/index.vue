<template>
  <table-index title="执法检查" :fields="fields" path="zfjcCheck/" :insert="false" insertPath="zfjcCheck/add" :edit="false"
               :delete="false"
               ref="table"
               :defaultSearchForm="defaultSearchForm"
               deleteMulti>
    <template slot-scole='form' slot='search-$btns'>
      <el-button type="primary" @click="$router.push({path:'/examine/zfjcCheck/edit', query: {checkType: 'zf'}})">添加</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text"
                 @click="$router.push({path: '/examine/zfjcCheck/edit', query: {id:scope.row.id,sendId: '1', infoId: null, path: 'zfjcCheck', checkType: 'zf'}})"
                 size="mini" width="40">编辑
      </el-button>
      <el-button type="text"
                 @click="exptz(scope.row)"
                 size="mini" width="45">检查通知单
      </el-button>
    </template>
  </table-index>
</template>
<script>
import indexMixin from '@/util/indexMixin'

/**
 * 待办任务列表
 */
export default {
  mixins: [indexMixin],
  data() {
    return {
      //多选框
      multipleSelection: [],
      dict: ['zfjc.status'],
      title: '执法检查',
      loading: false,
      detailShow: false,
      applyShow: false,
      resultShow: false,
      tongzhiShow: false,
      row: {},
      rows: [],
      reply: '',//整改回复
      opinion: '',//整改意见
      applyContent: '',//审批意见
      radio: 0,
      checkType: 'zf',
      //新代码
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
        {prop: 'personName', label: '检查人员', width: '100', search: false, edit: false, insert: false , required: true},
        {prop: 'organiseName', label: '检查单位', width: '140', search: true, edit: false, insert: false , required: true},
        {prop: 'categoryName', label: '检查项', search: false, edit: false, insert: false , required: true},
        /*{
          prop: 'status', label: '状态', mapper: {
            '0': {label: '待审核', show: 'success'},
            '1': {label: '待检查', show: 'danger'},
            '2': {label: '办结', show: 'danger'}
          }, width: '80', fieldType: 'select', component: 'tag', edit: false, showTypeKey: 'show',
        }*/
      ],
      defaultSearchForm: {checkType: 'zf'}
    }
  },
  props: {
    isShow: {default: true}
  },
  methods: {
    /**
     * 多选框
     * */
    //办结
    banjie(row) {
      //办结
      this.$http.post('/zfjcCheck/banjie', {id: row.id}).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.message)
        }
        this.cancel()
        // this.loadData(this.page.index)
      })
    },
    /**
     * 详情
     */
    detailSHow(row) {
      this.row = row
      this.detailShow = true
    },
    /**
     * 结果填报
     * @param row
     */
    resultSHow(row) {
      this.$router.push({path: '/examine/detail', query: {id: row.id, sendId: "1", infoId: null, path: 'zfjcCheck'}})
    },
    /**
     * 编辑
     */
    edit(form) {
      this.$refs.ConfigForm.editFormBefore(form)
    },
    /**
     * 审核弹窗
     * @param row
     */
    apply(row) {
      this.row = row
      this.applyShow = true
    },
    /**
     * 通知
     * @param row
     */
    tongzhi(row) {
      this.row = row
      this.tongzhiShow = true
    },
    /**
     * 审核
     * @param v
     */
    tongyi(v) {
      //暂时修改状态即可  不加任务表
      this.row.status = v
      this.row.applyContent = this.applyContent
      let message = ''
      if (v === '3') {
        message = '驳回成功'
      } else {
        message = '通过成功'
      }
      this.$http.post('/zfjcCheck/update', this.row).then(({data}) => {
        if (data && data.success) {
          this.$message.success(message)
        }
        this.cancel()
        this.applyContent = ''
        this.loadData(this.page.index)
      })
    },
    /**
     * 通知调整
     */
    tognzhitiaozheng() {
      //TODO 把不通过的条目发给 被检查单位  并生成可下载的文件
    },
    cancel() {
      this.detailShow = false
      this.applyShow = false
      this.tongzhiShow = false
      this.resultShow = false
    },
    //通知单
    exptz(row) {
      // let a = ''
      // for (let i = 0; i < this.multipleSelection.length; i++) {
      //   a += this.multipleSelection[i].id + ','
      // }
      let url = "api/zfjcCheck/exptz?id=" + row.id
      window.open(url)
    }
  }
}
</script>