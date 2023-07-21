<template>
  <table-index :fields="fields" path="jdzdWeight/" :insert="true" :edit="true" :delete="true" ref="table"
               deleteMulti
               :defaultSearchForm="defaultSearchForm">
    <!--   <template v-slot:search="form">
         <el-form-item label="权重:" prop="weight" before='status'>
           <el-select filterable v-model="form.weight"  placeholder="请选择权重"
                      clearable style="width: 150px;">
             <el-option
                 v-for="item in 100"
                 :key="item"
                 :label="item"
                 :value="item">
             </el-option>
           </el-select>
         </el-form-item>
       </template>-->
    <template slot-scole='form' slot='search-$btns'>
      <el-button type="success" size="mini" width="90" @click="todoing(1)">一键新增单位权重</el-button>
      <el-button type="danger" size="mini" width="90" @click="todoing(0)">清空</el-button>
    </template>
  </table-index>
</template>

<script>

export default {
  components: {},
  data() {
    return {
      dict: [],
      //新代码
      fields: [
        // {prop: 'organiseName', label: '单位名', search: true},
        {
          prop: 'organiseId',
          label: '单位名',
          search: true,
          edit: true,
          insert: true,
          column: true,
          filterable: true,
          fieldType: 'select',
          labelKey: 'organiseName',
          required: true,
          url: '/keyword/getOrg',
          params: {}
        },
        {
          prop: 'weight', label: '权重', search: false, showTypeKey: 'show', fieldType: 'select', width: '300',
          mapper: {
            1: {label: '1', show: 'primary'},
            2: {label: '2', show: 'primary'},
            3: {label: '3', show: 'success'},
            4: {label: '4', show: 'success'},
            5: {label: '5', show: 'warning'},
            6: {label: '6', show: 'warning'},
            7: {label: '7', show: 'danger'},
            8: {label: '8', show: 'danger'},
            9: {label: '9', show: 'danger'},
            10: {label: '10', show: 'danger'},
          },
        },
        {
          prop: 'status', label: '状态', width: "300", component: 'tag', showTypeKey: 'show', fieldType: 'select',
          mapper: {
            '1': {label: '启用', show: 'primary'},
            '0': {label: '禁用', show: 'success'}
          },
        }
      ],
      defaultSearchForm: {organiseId: ''}
    }
  },
  methods: {

    $init() {
    },
    apiPath() {
      return '/jdzdWeight'
    },
    todoing(v) {
      let url = ''
      let message = ''

      if (v === 0) {
        url = this.apiPath() + '/clear'
        message = '此操作将永久删除下列表内容'
      } else {
        url = this.apiPath() + '/addAuto'
        message = '此操作将新增所有单位权重信息'
      }


      this.$confirm(message + ', 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post(url).then(({data}) => {
          if (data && data.success) {
            this.$message.success('操作成功！')
          } else {
            this.$message.error(data.message)
          }
          this.$refs.table.loadData(this.defaultSearchForm);
          this.loading = false
        })
      });


    }
  },
  computed: {
    /**
     * 映射table-index 选中数据
     * @returns {[]|*|*[]}
     */
    selection() {
      if (this.$refs.table && this.$refs.table.tableSelection) {
        return this.$refs.table.tableSelection
      } else {
        return []
      }
    }
  },
  filters: {},
}
</script>
