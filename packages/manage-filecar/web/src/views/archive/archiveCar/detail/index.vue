<template>
  <table-index title="档案车详情" :fields="fields" path="archiveCarDetail"
               :defaultSearchForm="defaultSearchForm"
               :defaultInsertForm="defaultSearchForm"
               :edit="false"
               :insert="false"
               ref="table"
               back>
    <template v-slot:search-$btns>
      <el-button type="primary" @click="$router.push('/archive/archiveCar')">返回档案车</el-button>
      <start-process v-if="getType()" :batch-id="$route.query.id"/>
      <!--      <el-button type="primary" v-if="batchType==='utilizationConsult'">添加查档登记</el-button>-->
      <!--      <guidance-application v-if="batchType==='businessGuidance'" :batch-id="$route.query.id"/>-->
      <!-- TODO 福州项目先关掉，其他项目可以应用 -->
      <!--<el-button type="primary" v-if="batchType==='businessGuidance'"
                       @click="guidanceApplicationDialog.guidanceApplicationDialogVisible=true">业务指导申请
            </el-button>-->
    </template>
    <el-table-column prop="title" label="题名" align="left" show-overflow-tooltip>
      <template slot-scope="{row}">
        <el-button type="text" size="mini" width="90" @click="routeArchiveDetail(row)"> {{ row.title }}</el-button>
      </template>
    </el-table-column>
    <!--业务指导申请弹窗-->
    <el-dialog title="业务指导申请" :visible.sync="guidanceApplicationDialog.guidanceApplicationDialogVisible" width="80%">
      <form-render style="padding: 10px 0" :fields="guidanceApplicationFields" :model="guidanceApplicationForm"
                   label-width="100px" v-loading="guidanceApplicationDialog.loading"
                   ref="guidanceApplicationForm" inline>
      </form-render>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="guidanceApplication">确 定
        </el-button>
      </div>
    </el-dialog>
  </table-index>
</template>
<script>
import StartProcess from "./startProcess";

/**
 * 档案车内档案列表详情
 */
export default {
  components: {StartProcess},
  data() {
    return {
      fields: [
        {prop: 'title', label: '题名', search: true, align: 'left'},
        {prop: 'archiveCode', label: '档号', width: 150},
        {prop: 'fondCode', label: '全宗号', width: 140},
        {prop: 'categoryCode', label: '分类号', width: 140},
        {prop: 'year', label: '年度', width: 80},
        /*{
          prop: 'archiveTag',
          label: '档案标记',
          url: '/archiveCarBatch/archiveCarTag',
          labelKey: 'name',
          valueKey: 'code',
          width: 120
        }*/
      ],
      defaultSearchForm: {batchId: this.$route.query.id},
      guidanceApplicationDialog: {
        guidanceApplicationDialogVisible: false,
        loading: false
      },
      guidanceApplicationForm: {description: ''},
      guidanceApplicationFields: [
        {
          prop: 'description',
          label: '描述',
          type: 'textarea',
          required: true,
          clearable: true,
          singleLine: true
        },
      ]
    }
  },
  methods: {
    routeArchiveDetail(row) {
      if (row.status==="1"){
        this.$router.push({
          path: '/archive/archiveCar/archiveDetail',
          query: {id: row.id,formDefinitionId:row.formDefinitionId}
        })
      }else if (this.$route.query.batchType!=='utilizationConsult'){
        this.$router.push({
          path: '/archive/archiveCar/archiveDetail',
          query: {id: row.id,formDefinitionId:row.formDefinitionId}
        })
      }else {
        this.$message.warning("请审核完成后查看")
      }
    },
    getType(){
      let button=false
      if (this.$route.query.batchType==="utilizationConsult" && this.$refs.table.data.data.length){
        if (this.$route.query.status=="0"){
          for (let i = 0; i < this.$refs.table.data.data.length; i++) {
            if (this.$refs.table.data.data[i].status!="1"){
              button=true
            }
          }
        }else if (this.$route.query.status==undefined){
          button=true
        }

      }
      return button
    },
    async guidanceApplication() {
      const data = await this.$refs.guidanceApplicationForm.submit('/businessGuidanceBatch/batchApply', {archiveCarId: this.$route.query.id})
      if (data && data.success) {
        this.$message.success("提交成功！")
        this.guidanceApplicationDialog.guidanceApplicationDialogVisible = false
      } else {
        this.$message.warning(data.message)
      }
    }
  },
  computed: {
    batchType() {
      return this.$route.query.batchType
    }
  }
}
</script>