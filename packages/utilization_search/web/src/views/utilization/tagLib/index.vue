<template>
  <table-index :fields="fields" path="tagLib"
               :defaultSearchForm="defaultSearchForm"
               :defaultInsertForm="defaultInsertForm"
               ref="table">
    <template v-slot:table-$btns="scope" style="width:500px">
      <el-button type="text" size="mini" @click="adopt(scope.row)" width="20">通过</el-button>
    </template>
    <template v-slot:edit="form">
      <el-form-item prop="stType" label="实体分类" v-show="ctype === 'st' ">
        <select-async v-model="form.stType" :mapper = "stMapper"/>
      </el-form-item>
      <el-form-item prop="parentId" label="上一级" v-show="ctype === 'gt' ">
        <select-async
            v-model="form.parentId"
            :mapper = "gtMapper"
            valueKey = 'id'
            labelKey = 'fullLable'/>
      </el-form-item>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fields: [
        {prop: 'tagName', label: '标签名', search: true, required: true},
        {
          prop: 'ctype', label: '类型', search: true, required: true, fieldType: 'select',
          mapper: {'gt': '固体标签', 'st': '实体标签', 'zdy': '自定义标签'},
          on: {change: this.ctypeChange}
        },
        {
          prop: 'stType',
          label: '实体分类',
          fieldType: 'select',
          mapper: {'PER': '人名', 'LOC': '地名', 'ORG': '机构名', 'TIME': '时间'}
        },
        {prop: 'tagDescribe', label: '描述', type: 'textarea'},
        {prop: 'createPersonName', label: '创建人', edit: false},
        {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false},
        {prop: 'status', component: 'tag' , label: '目前状态', mapper: {'0': '未启用','1': '启用'}, edit: false},
        /*{prop: 'updatePerson', label: '修改人', edit: false},
        {prop: 'updateDate', label: '修改时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false}*/
      ],
      defaultSearchForm: {tagName: '', tagType: ''},
      defaultInsertForm: {status: '1'},
      stMapper: {'PER': '人名', 'LOC': '地名', 'ORG': '机构名', 'TIME': '时间'},
      ctype: '',
      gtMapper: {}
    }
  },
  methods: {
    ctypeChange(v) {
      this.ctype = v
      this.$http.post('/tagLib/selectParents')
          .then(({data}) => {
            if (data.success) {
              this.gtMapper = data.data
            } else {
              this.$message.error(data.message)
            }
          })
    },
    adopt(row){
      console.log(row)
      this.$http.post('/tagLib/updateTagState',row).then(({data}) =>{
        if(data.success){
          this.$message.success(data.message)
          this.$refs.table.reload()
        }else {
          this.$message.error(data.message)
        }
      })
    }
  }
}
</script>

