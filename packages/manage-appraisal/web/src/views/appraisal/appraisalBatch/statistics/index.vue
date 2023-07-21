<template>
  <section>
    <nac-info back>
      <el-select v-model="fondId" @change="loadData">
        <el-option v-for="f in fond" :key="f.id" :label="f.name" :value="f.id"/>
      </el-select>
    </nac-info>
    <div class="index_main">
      <table-render :columns="columns" :data="data" height="auto" v-if="data.length>0">
        <!--        <el-table-column prop="vintages" label="年份" align="center" >-->
        <!--          <template v-slot="scope">-->
        <!--            {{ scope.row.vintages }}-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column prop="kz" label="控制" align="center" >-->
        <!--          <template  v-slot="scope">-->
        <!--            <span v-if="scope.row.kz===0">{{ scope.row.kz}} </span>-->
        <!--            <el-button type="text" v-else @click="toArchive('kz','',scope.row.vintages)">{{ scope.row.kz}} </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column prop="kf" label="开放" align="center" >-->
        <!--          <template v-slot="scope">-->
        <!--            <span v-if="scope.row.kf===0">{{ scope.row.kf}} </span>-->
        <!--            <el-button type="text" v-else @click="toArchive('kf','',scope.row.vintages)">{{ scope.row.kf}} </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column prop="xh" label="销毁" align="center" >-->
        <!--          <template v-slot="scope">-->
        <!--            <span v-if="scope.row.xh===0">{{ scope.row.xh}} </span>-->
        <!--            <el-button type="text" v-else @click="toArchive('xh','',scope.row.vintages)">{{ scope.row.xh}} </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <!--        <el-table-column prop="yq" label="延期" align="center" >-->
        <!--          <template v-slot="scope">-->
        <!--            <span v-if="scope.row.yq===0">{{ scope.row.yq}} </span>-->
        <!--            <el-button type="text" v-else @click="toArchive('yq','',scope.row.vintages)">{{ scope.row.yq}} </el-button>-->
        <!--          </template>-->
        <!--        </el-table-column>-->
        <el-table-column prop="yz" label="与机器鉴定结果一致" align="center">
          <template v-slot="scope">
            <span v-if="scope.row.yz===0">{{ scope.row.yz }} </span>
            <el-button type="text" v-else @click="toArchive('','1',scope.row.vintages)">{{ scope.row.yz }}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="byz" label="与机器鉴定结果不一致" align="center">
          <template v-slot="scope">
            <span v-if="scope.row.byz===0">{{ scope.row.byz }} </span>
            <el-button type="text" v-else @click="toArchive('','0',scope.row.vintages)">{{ scope.row.byz }}</el-button>
          </template>
        </el-table-column>
      </table-render>
    </div>
  </section>

</template>

<script>
export default {
  name: "index",
  data() {
    return {
      data: [],
      fondId: '',
      fond: [],
      columns: {}
    }
  },
  methods: {
    async $init() {
      this.columns.vintages = {label: '年份'}
      if ("kfjd" === this.$route.query.type) {
        const {data} = await this.$http.post("/appraisalOpenRange/page", {page: false})
        if (data.success) {
          this.columns = {}
          data.data.forEach(v => {
            this.columns[v.id] = {
              label: v.openRange,
              route: true,
              routerPath: '/appraisal/appraisalBatch/detail/archive',
              queryProp: [{status: 2}, {personResult: v.id}, {batchId: this.$route.query.batchId}, {vintages: v.vintages}],
              component: 'text'
            }
          })
        }
      } else {
        this.columns.xh = {
          label: '销毁',
          route: true,
          routerPath: '/appraisal/appraisalBatch/detail/archive',
          // queryProp:[{status:2},{personResult:'xh'},{batchId:this.$route.query.batchId},{vintages:v.vintages}],
          component: 'text'
        }
        this.columns.yq = {
          label: '延期',
          route: true,
          routerPath: '/appraisal/appraisalBatch/detail/archive',
          //  queryProp:[{status:2},{personResult:'yq'},{batchId:this.$route.query.batchId},{vintages:v.vintages}],
          component: 'text'
        }
      }
      this.loadData()
      this.$http.post("/appraisalBatch/getFondByBatchId", {batchId: this.$route.query.batchId})
          .then(({data}) => {
            this.fond = data.data
            this.fond.push({name: '全部全宗', id: ''})
          })
    },
    loadData() {
      this.$http.post("/appraisalBatch/statisticsFinishByBatchId", {
        batchId: this.$route.query.batchId,
        fondId: this.fondId
      })
          .then(({data}) => {
            this.data = data.data

          })
    },
    toArchive(personResult, isEqual, vintages) {
      this.$router.push({
        path: '/appraisal/appraisalBatch/detail/archive',
        query: {status: 2, personResult, isEqual, batchId: this.$route.query.batchId, vintages}
      })
    }
  }
}
</script>

<style scoped>

</style>
