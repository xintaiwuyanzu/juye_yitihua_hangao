<template>
  <table-index :fields="fields" path="knowledgeGraph"
               :defaultSearchForm="defaultSearchForm"
               ref="table" @editShow="editShow">
    <template v-slot:edit="form">
      <el-form-item prop="keywords" label="关键词" required>
        <el-select v-model="form.keywords" placeholder="请选择标签" multiple  filterable clearable >
          <el-option
              v-for="(item,index) in tags"
              :key="item.id"
              :label="item.tagName"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text" width="33" size="mini" @click="goTupu(scope.row.id)">图谱</el-button>
    </template>
  </table-index>
</template>

<script>
export default {
  data() {
    return {
      fields: [
        {prop: 'themeName', label: '主题', search: true, required: true},
        {prop: 'remark', label: '备注'},
        {prop: 'createPerson', label: '创建人', edit: false},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false},
        {prop: 'updatePerson', label: '修改人', edit: false},
        {prop: 'updateDate', label: '修改时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false}
      ],
      defaultSearchForm: {themeName: ''},
      tags: []
    }
  },
  methods:{
    $init() {
      this.getTag()
    },
    //跳转到知识图谱的页面
    goTupu(id){
      this.$router.push({
        path: '/utilization/knowledgeGraph/detail',
        query: {id: id}
      })
    },
    getTag(){
      this.$http.post('/tagLib/selectTagList').then(({data}) => {
        if (data.success) {
          this.tags = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    editShow(form){  //多选下拉框回显
      var keywordsSet = []
      if (form.keywords != '') {
        var arrKw = form.keywords.split(',')
        this.$set(form, 'keywords', arrKw)
      }
    }
  }
}
</script>

<style scoped>

</style>