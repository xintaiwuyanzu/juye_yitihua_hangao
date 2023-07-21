<template>
  <table-index title="库房详情"
               :fields="fields"
               path="roomDetails"
               :insert="false" :edit="false" :delete="false"
               :defaultSearchForm="defaultSearchForm"
               ref="table"
               back>
    <template v-slot:search-$btns="scope">
      <el-button type="primary" @click="details(scope)" size="mini" width="80">同步数据</el-button>
    </template>
  </table-index>

</template>
<script>
export default {
  data() {
    return {
      fields: [
          {
            label: '档案类型',
            prop:'archivesName',
            align: 'center',
            component: 'text',
          },
          {
            label: '数量',
            prop:'archivesNum',
            align: 'center',
            width:100
          },
      ],
      defaultSearchForm: {kufangId:this.$route.query.id}
    }
  },
  methods: {
    details(scope){
      this.$http.post('/roomDetails/updateDetails',{kufangId : this.$route.query.id}).then(({data}) => {
        if (data.success) {
          this.$message.success(data.message)
          this.$refs.table.reload()
        } else {
          this.$message.error(data.message)
        }
      })
    }
  }
}
</script>
