<template>
  <table-index :fields="fields" path="zfjcSpecialist/" :insert="true" :edit="false" :delete="true" ref="table"
               deleteMulti
               :defaultSearchForm="defaultSearchForm" :default-insert-form="defaultInsertForm">
    <template v-slot:edit="form" ref="form">
      <el-form-item label="人员" prop="userId" before="status" required>
        <el-cascader :show-all-levels="false" v-model="form.userId" :props="{ value:'id' }" style="width: 100%"
                     placeholder="请选择人员"
                     filterable
                     :options="persons"
                     @change="((val)=>{change(val, form)})"/>
      </el-form-item>
    </template>
  </table-index>
</template>

<script>
export default {
  data() {
    return {
      persons: [],
      //新代码
      fields: [
        {prop: 'userName', label: '用户姓名', search: true, edit: false, insert: false},
        {prop: 'defaultOrganiseName', label: '所属机构', search: true, edit: false, insert: false},
        {
          prop: 'status', label: '状态', width: "300", component: 'tag', showTypeKey: 'show', fieldType: 'radio',
          mapper: {
            '1': {label: '启用', show: 'primary'},
            '0': {label: '禁用', show: 'success'}
          },
        }
      ],
      defaultSearchForm: {organiseId: ''},
      defaultInsertForm: {status: '1'},
    }
  },
  methods: {
    $init() {
      this.getOrganisePersonTree()
    },
    change(v, form) {
      if (v && v.length > 0) {
        this.$set(form, 'userId', v[v.length - 1])
      }
    },
    getOrganisePersonTree() {
      this.$http.post('/archiveOrganisePerson/getOrganisePersonTree').then(({data}) => {
        if (data.success) {
          this.persons = data.data ? data.data : []
        }
      })
    }
  }
}
</script>
