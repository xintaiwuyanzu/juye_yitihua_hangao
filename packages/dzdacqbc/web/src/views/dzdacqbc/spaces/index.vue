<template>
  <table-index :fields="fields" path="/earchive/spaces" ref="table" :back="$route.query.back"
               :defaultSearchForm="searchForm" back>
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="60" @click="storageDetails(scope.row)">存储档案</el-button>
      <el-button
          @click="setDefault(scope.row)" :type="scope.row.isDefault === 'true'?'info':'success'"
          size="mini" width="90" :disabled="scope.row.isDefault === 'true'">设为默认
      </el-button>
    </template>
    <template v-slot:edit="form">
      <el-form-item label="挂载目录:" prop="catalogue" required>
        <el-input v-model="form.catalogue" placeholder="请输入挂载目录" clearable
                  style="width: 260px;" @blur="changeCatalogue(form)">
        </el-input>
      </el-form-item>
      <el-form-item label="容量:" prop="capacity" required>
        <el-input v-model="form.capacity" clearable style="width: 260px;text-align: center">
          <template slot="append">G</template>
        </el-input>
      </el-form-item>
    </template>
    <el-table-column prop="capacity" label="容量" width="180" align="center">
      <template v-slot="scope">
        <el-progress :text-inside="true" :stroke-width="20" :percentage="scope.row.percent"
                     class="space-progress"
                     :status="scope.row.percent <= 50? 'success' :
                             scope.row.percent <= 70? 'warning' : 'exception'" :format="setItemText(scope.row)"/>
      </template>
    </el-table-column>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fields: [
        {
          prop: 'spaceName',
          label: '存储空间名称',
          search: true,
          required: true
        },
        {prop: 'catalogue', label: '挂载目录', width: "120", search: true, required: true},
        {prop: 'capacity', label: '容量（G）', width: "80", edit: true, disabled: true},
        {prop: 'backPath', label: '备份目录', width: "120", required: true},
        {
          prop: 'spacesType',
          label: '类型',
          width: "100",
          fieldType: 'radio',
          mapper: {'1': '服务器', '2': '移动硬盘'},
          required: true
        },
        {prop: 'endTime', label: '到期时间', width: '120', fieldType: 'date', dateFormat: true},
        {prop: 'latestDetectDate', label: '上次检测时间', width: '120', fieldType: 'date', dateFormat: true},
        {prop: 'remarks', label: '备注', width: "130"},
      ],
      searchForm: {id: this.$route.query.id},
      id: ''
    }
  },
  watch: {
    "$route.query.id"(val) {
      this.searchForm.id = val
      this.$refs.table.loadData(this.searchForm);
    }
  },
  methods: {
    setItemText(row) {
      return () => {
        return parseFloat(row.usedSpace).toFixed(2) + 'G/' + parseFloat(row.capacity).toFixed(2) + 'G \xa0\xa0|\xa0\xa0 ' + row.percent + '%'
      }
    },
    setDefault(row) {
      this.loading = true;
      row.isDefault = 'true';
      this.$http.post('/earchive/spaces/updateDefault', row).then(({data}) => {
        if (data.success) {
          this.$message.success("设置默认成功");
          this.$refs.table.loadData(this.defaultSearchForm);
          this.loading = false
        } else {
          this.$message.error(data.message)
        }
      })
      this.loading = false
    },
    async changeCatalogue(v) {
      const {data} = await this.$http.post('/earchive/spaces/getSpace', {catalogue: v.catalogue})
      if (data.data === '0') {
        this.$message.warning('请输入正确的路径')
      } else {
        this.$set(v, 'capacity', data.data)
      }
    },
    async storageDetails(row) {
      this.$router.push({
        path: './archive',
        query: {spaceId: row.id}
      })
    }
  }
}
</script>
<style lang="scss">
.space-progress {
  .el-progress-bar__outer {
    background-color: $--color-info;
  }
}
</style>