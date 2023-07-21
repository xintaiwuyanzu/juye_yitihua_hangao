<template>
  <table-index :delete="false" :edit="false" :fields="fields" :insert="false" path="realm_class" ref="table">
    <template v-slot:search-$btns>
      <el-button @click="reload" icon="el-icon-refresh" type="primary">刷新</el-button>
      <el-button @click="option('')" icon="el-icon-plus" type="primary">添加对象</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="option(scope.row.formId)" type="text" width="40">编辑</el-button>
      <el-button
          @click="viewData(scope.row)"
          type="text"
          width="60">
        对象数据
      </el-button>
      <el-button
          @click="viewAlta(scope.row)"
          type="text"
          v-show="false"
          width="40">
        图谱
      </el-button>
    </template>
  </table-index>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        fields: [
          {prop: 'formAlias', label: '对象名'},
          {prop: 'propertyNum', label: '属性数量'},
          {prop: 'dataNum', label: '数据数量'},
          {prop: 'relationNum', label: '关系数量'},
          {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss'}
        ]
      }
    },
    methods: {
      option(id) {
        this.$router.push({
          path: 'object/add',
          query: id === '' ? {} : {id}
        })
      },
      async reload() {
        const {data} = await this.$post('realm_class/reload')
        if (data && data.success) {
          this.$message.success('刷新成功')
          this.$refs.table.loadData()
        }
      },
      viewData(row) {
        this.$router.push({
          path: '/mapping/object/viewData',
          query: {id: row.id, formId: row.formId}
        })
      },
      viewAlta(row) {
        this.$router.push({
          path: '/mapping/tripletData/paint',
          query: {classType: row.formTable, i: 3}
        })
      }
    }
  }
</script>

<style scoped>

</style>