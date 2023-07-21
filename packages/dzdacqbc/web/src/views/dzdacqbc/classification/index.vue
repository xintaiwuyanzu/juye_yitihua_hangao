<template>
  <table-index :fields="fields" path="classification" ref="table" :edit="false">
    <template v-slot:edit="form">
      <el-form-item label="选择全宗:" prop="fondId">
        <el-select filterable clearable v-model="form.fondId" placeholder="请选择全宗" @change="changeFond(form)">
          <el-option v-for="item in fonds" :key="item.id"
                     :label="`${item.data.code}  ${item.label}`" :value="item.id">
          </el-option>
        </el-select>


        <!--        <select-async  :mapper="fonds" v-model="form.fondId" placeholder="请选择全宗" clearable-->
        <!--                      @change="changeFond(form)"  filterable  labelKey="label" />-->
      </el-form-item>
      <el-form-item label="选择门类:" prop="classId">
<!--        <select-async :mapper="categories" labelKey="name" v-model="form.classId" placeholder="请选择门类" clearable-->
<!--                      filterable/>-->

        <el-cascader
            v-model="form.classId"
            :options="categories"
            :props="{ expandTrigger: 'hover',value: 'id',emitPath:false}"
            ></el-cascader>
      </el-form-item>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button type="text" size="mini" width="80" @click="editRow(scope.row)">编辑</el-button>
      <el-button type="text" size="mini" width="80" @click="switchDetails(scope.row)">异常详情</el-button>
      <el-button
          @click="setDefault(scope.row)" :type="scope.row.isDefault === 'true'?'info':'success'"
          size="mini" width="80" :disabled="scope.row.isDefault === 'true'">设为默认
      </el-button>
    </template>
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fondParams: {type: 'fond'},
      fields: [
        {prop: 'classificationName', label: '分类名称', search: true, required: true},
        {prop: 'fondName', label: '全宗名称', search: true, required: true, edit: false, insert: false},
        {prop: 'className', label: '门类名称', width: 200, search: true, required: true, edit: false, insert: false},
        {prop: 'annual', label: '年度', width: 80},
        {
          prop: 'tacticsId',
          label: '检测策略',
          required: true,
          width: 120,
          fieldType: 'select',
          url: '/earchive/tactics/page',
          labelKey: 'tacticsName',
          params: {page: false}
        },
        {
          prop: 'spacesId', label: '存储空间', width: 120,
          fieldType: 'select',
          url: '/earchive/spaces/page',
          required: true,
          labelKey: 'spaceName',
          params: {page: false}
        },
        {prop: 'lastTestTime', label: '最后检测时间', width: 120, dateFormat: true, edit: false, insert: false},
        {
          prop: 'problemNum',
          label: '异常数量',
          width: 80, edit: false, insert: false
        }
      ],
      categories: [],
      fonds: [],
    }
  },
  methods: {

    $init() {
      this.a()
    },
    async a() {
      const {data} = await this.$http.post('/sysResource/personResource', {type: 'fond'})
      if (data.success) {
        this.fonds = data.data
      } else {
        this.$message.error(data.message)
      }
    },


    async setDefault(row) {
      this.loading = true;
      row.isDefault = 'true';
      const {data} = await this.$http.post('/classification/updateDefault', row)
      if (data.success) {
        this.$message.success("设置默认成功");
        this.$refs.table.reload();
        this.loading = false
      } else {
        this.$message.error(data.message)
      }
      this.loading = false
    },
    async changeFond(form) {
      this.$set(form, 'classId', '')
      form.categoryId = ''
      this.categories = []
      await this.editShow(form, false)
    },
    async switchDetails(row) {
      this.$router.push({
        path: './alarm',
        query: {classificationId: row.id}
      })
    },
    async editShow(form, v) {
      if (form.fondId) {
        const {data} = await this.$http.post('/sysResource/personResource', {type: 'category', group: form.fondId})
        if (data.success) {
          this.categories = data.data
          if (v) {
            this.$refs.table.showEdit(form)
          }
        } else {
          this.$message.error(data.message)
        }
      } else {
        this.$set(form, 'classId', '')
      }

      // if (form.fondId) {
      //   const {data} = await this.$http.post('/manage/category/pageByBusinessId', {businessId: form.fondId})
      //   if (data.success) {
      //     this.categories = data.data
      //     if (v){
      //       this.$refs.table.showEdit(form)
      //     }
      //   } else {
      //     this.$message.error(data.message)
      //   }
      // } else {
      //   this.$set(form, 'classId', '')
      // }
    },
    //编辑
    async editRow(row) {
      await this.editShow(row, true)
    },
  }
}
</script>