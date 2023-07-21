<template>
  <div>
    <el-table
        :data="tableData"
        style="width: 100%"
        max-height="460"
        row-key="id"
        border>
      <el-table-column
          label="关键词">
        <template v-slot="scope">
          <el-button type="text" @click="checkedKeyWord(scope)">{{ scope.row.keywords }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="所属依据">
        <template v-slot="scope">
          <el-button type="text">{{ showBasis(scope.row.basisId) }}</el-button>
        </template>
      </el-table-column>
      <el-table-column
          label="出现位置"
          width="80">
        <template v-slot="scope">
          {{ scope.row.filed|dict({'title': "题名", 'content': "原文"}) }}
        </template>
      </el-table-column>
      <el-table-column
          prop="frequency"
          label="出现次数"
          width="80">
      </el-table-column>
      <el-table-column label="操作" width="80">
        <template v-slot="scope">
          <el-button @click="basisIdDetail(scope.row)" type="text" size="small">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-drawer title="依据内容"
               :visible.sync="drawerVisible"
               direction="rtl"
               size="500px"
               custom-class="table_drawer"
               append-to-body>
      <div>
        <basis-form :basis-id="basisId"></basis-form>
        <rules-form :riles-id="rulesId"></rules-form>
      </div>

    </el-drawer>
  </div>
</template>
<script>
import basisForm from "../../rules/detail/basisForm";
import rulesForm from "../../rules/detail/rulesForm";

export default {
  components: {basisForm, rulesForm},
  data() {
    return {
      tableData: [],
      basisData: [],
      drawerVisible: false,
      basisId: '',
      rulesId: ''
    }
  },
  props: {
    formDefinitionId: {type: String},
    formDataId: {type: String}
  },
  methods: {
    $init() {
      this.getAppraisalBasis()
      this.$http.post("/matchingKeyWord/page", {
        formDefinitionId: this.formDefinitionId,
        formDataId: this.formDataId,
        page: false
      })
          .then(({data}) => {
            this.tableData = data.data
            if (this.tableData.length > 0) {
              this.$emit('checkedKeyWord', this.tableData[0].keywords)
            } else {
              this.$emit('checkedKeyWord', '')
            }
          })
    },
    getAppraisalBasis() {
      this.$http.post("/appraisalBasis/page", {
        page: false
      }).then(({data}) => {
        this.basisData = data.data
      })
    },
    showBasis(id) {
      for (let i = 0; i < this.basisData.length; i++) {
        if (this.basisData[i].id == id) {
          return this.basisData[i].basis
        }
      }
    },
    checkedKeyWord(scope) {
      this.$emit('checkedKeyWord', scope.row)
    },
    basisIdDetail(row) {
      this.basisId = row.basisId
      this.rulesId = row.rulesId
      this.drawerVisible = true
    }
  },
}
</script>
