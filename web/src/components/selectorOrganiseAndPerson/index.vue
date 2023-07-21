<template>
  <section>
    <div>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="机构">
          <el-select placeholder="请选择机构" v-model="form.targetOrganiseId" @change="persons" clearable filterable>
            <el-option v-for="organise in organiselist" :key="organise.id" :label="organise.organiseName"
                       :value="organise.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="人员">
          <el-select placeholder="请选择接收人" v-model="form.targetPerson" clearable filterable>
            <el-option v-for="person in personlist" :key="person.id" :label="person.userName" :value="person.id"/>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="hand" v-loading="loading" class="btn-submit">提 交</el-button>
      </div>
    </div>
  </section>
</template>
<script>
export default {
  name: "selectOrganiseAndPerson",
  props: {
    multi: {type: Boolean, default: false},
    // dialogShow: {type: Boolean, default: false},
    row: Array,//当前选中行
    multipleSelection: Array //这样可以指定传入的类型，如果类型不对，会警告
  },
  data() {
    return {
      loading: false,
      dialogShow: false,
      form: {
        targetPerson: '',
        targetOrganiseId: '',
      },
      organiselist: [],
      personlist: [],
    }
  },
  methods: {
    //移交选中
    handleCommand() {
      this.dialogShow = true
    },
    //移交
    async hand() {
      //todo  low代码
      if (this.multi) {
        //多选
        for (let i = 0; i < this.multipleSelection.length; i++) {
          let arr = this.multipleSelection[i]
          arr.organiseId = this.form.targetOrganiseId
          arr.personId = this.form.targetPerson
          //移交 todo 简单处理 把机构和责任人换了
          const {data} = await this.$post('/handOver/update', Object.assign(arr))
          if (data.success) {
            //移交成功 加记录
            await this.addhis()
          }
          this.loading = false
        }
      } else {
        //非多选
        this.row.organiseId = this.form.targetOrganiseId
        this.row.personId = this.form.targetPerson
        //移交 todo 简单处理 把机构和责任人换了
        const {data} = await this.$post('/handOver/update', Object.assign(this.row))
        console.log(data.success)
        if (data.success) {
          //移交成功 加记录
          await this.addhis()
        }
        this.loading = false
      }
    },
    async addhis() {
      await this.$post('/overHistory/addhis', {
        title: this.row.title,
        archiveCode: this.row.archiveCode,
        targetPerson: this.form.targetPerson
      })
      this.$message.success('移交成功')
      this.dialogShow = false
      this.$emit('func')
    },
    persons(oId) {
      this.form.targetPerson = ''
      this.$http.post('/person/page', {
        page: false,
        defaultOrganiseId: oId
      }).then(({data}) => {
        if (data && data.success) {
          this.personlist = data.data
        }
      })
    },
    organises() {
      this.$http.post('/organise/getAllDepartment', {}).then(({data}) => {
        if (data && data.success) {
          this.organiselist = data.data
        }
      })
    },
  },
  mounted() {
    this.organises()
  }
}
</script>
