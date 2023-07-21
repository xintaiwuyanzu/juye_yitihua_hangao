<template>
    <section>
        <nac-info back title="任务批次详情">
          <el-button type="primary" @click="showDialog()" size="mini">导 出</el-button>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
                <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="50" align="center"/>
                    <column-index :page="page"/>
                    <el-table-column label="全宗" prop="fondName" show-overflow-tooltip align="center"/>
                    <el-table-column label="分类" prop="categoryCode" show-overflow-tooltip align="center"/>
                    <el-table-column label="机构或问题" prop="orgCode" show-overflow-tooltip align="center" min-width="120"/>
                    <el-table-column label="档号" prop="archiveCode" show-overflow-tooltip align="center"/>
                    <el-table-column label="题名" prop="title" show-overflow-tooltip align="center"/>
                    <el-table-column label="关键词" prop="keyWords" show-overflow-tooltip align="center"/>
                    <el-table-column label="年度" prop="year" show-overflow-tooltip align="center"/>
                    <el-table-column label="发起人" prop="sourcePersonName" show-overflow-tooltip align="center"/>
                    <el-table-column label="发起时间" prop="sourceDate"
                                     :formatter="(r,c,cell)=>$moment(cell).format('YYYY-MM-DD')"
                                     show-overflow-tooltip
                                     align="center"/>
                    <el-table-column label="状态" prop="status" align="center">
                        <template v-slot="scope">
                            {{ scope.row.status|dict({0: "待审核", 1: "通过", '-1': "未通过"}) }}
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <page :page="page"/>
        </div>
      <el-dialog width="50%" title="导出" :visible.sync="dialogShow" :close-on-click-modal="false">
        <el-form>
          <el-form-item label="选择导出方案">
            <el-select v-model="expSchemaId" style="width: 200px">
              <el-option v-for="item in selectData"
                         :label="item.name"
                         :value="item.id"
                         :key="item.id"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="选择导出类型">
            <select-dict v-model="mineType" type="impexp.mineType" placeholder="请选择导出类型" style="width: 200px"/>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
          <el-button type="primary" @click="onSubmit" v-loading="loading" class="btn-submit">提 交</el-button>
        </div>
      </el-dialog>
    </section>
</template>
<script>
    import indexMixin from '@/util/indexMixin'

    /**
     * 批次列表
     */
    export default {
        mixins: [indexMixin],
        data() {
            return {
              loading: false,
              searchForm: {
                formatName: "",
              },
              category: [],
              data: [],
              multipleSelection: [],
              selectData: [],
              expSchemaId: '',
              mineType: '',
              dict: ['impexp.mineType'],
              expType: 'all',
              dialogShow: false,
              fondId:'',
              categoryId:''
            }
        },
        methods: {
          handleSelectionChange(val) {
            this.multipleSelection = val;
          },
            audit(row) {
                this.$router.push({
                    path: '/archive/task/batch/detail/audit',
                    query: {
                        id: row.id,
                        batchId: row.batchId,
                        taskId: this.$route.query.taskId,
                        taskStatus: this.$route.query.taskStatus
                    }
                })
            },
            async loadData() {
                this.loading = true
                const {data} = await this.$post('/batch/batchDetailPage', {
                    id: this.$route.query.batchId,
                    batchType: this.$route.query.taskType
                })
                this.data = data.data.data
                this.page.index = data.data.start / data.data.size + 1
                this.page.size = data.data.size
                this.page.total = data.data.total
                this.loading = false
            },
            $init() {
                this.loadData()
            },
          showDialog() {
            if(this.multipleSelection.length<1){
              this.$message.warning("请先选择要导出的数据")
            }else{
              this.loading = true
              this.$http.post('/impexpscheme/page', {page: false}).then(({data}) => {
                if (data && data.success) {
                  //只展示导出的
                  this.selectData = data.data.filter(function (val) {
                    if (val.schemeType === '2') {
                      return val
                    }
                  })
                  //如果 1 则直接赋值显示
                  if (this.selectData.length === 1) {
                    this.expSchemaId = this.selectData[0].id
                  }
                  this.dialogShow = true
                } else {
                  this.$message.error(data.message)
                }
                this.loading = false
              })
            }

          },
          onSubmit(){
            this.dialogShow = true
            let json = {}
            let _QUERY = []
            let query = {}
            this.$set(json,'key','id')
            this.$set(json,'type','i')
            let ids = ''
            for (let i = 0; i < this.multipleSelection.length; i++) {
              ids += this.multipleSelection[i].formDataId + ','
            }
            json.key = 'id'
            json.type = 'i'
            json.value = ids.substring(0,ids.length-1)
            //this.$set(json,'value',ids.substring(0,ids.length-1))
            //this.$set(query,'formDefinitionId',this.multipleSelection[0].formDefinitionId)
            query.formDefinitionId = this.multipleSelection[0].formDefinitionId
            _QUERY.push(json)
            query._QUERY = JSON.stringify(_QUERY)

            this.$http.post('/batch/getCategory', {formDefinitionId:this.multipleSelection[0].formDefinitionId}).then(({data}) => {
              if (data && data.success) {
                //console.log(data.data.fondId)
                //console.log(data.data.id)
                this.fondId = data.data.fondId
                this.categoryId = data.data.id
                this.exp(query)
              } else {
                this.$message.error(data.message)
              }
            })

          },
          exp(query){
            this.$http.post('/expBatch/newBatch', {
              impSchemaId: this.expSchemaId,
              mineType: this.mineType,
              type: 'EXP',
              categoryId:this.categoryId,
              fondId:this.fondId,
              ...query}).then(({data}) => {
              if (data && data.success) {
                this.$message.success('正在导出...，请到【导出记录】查看结果')
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
              this.dialogShow = false
            })
          }
        }
    }
</script>
