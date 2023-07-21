<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="库房名称" prop="kuFangMingCheng" style="margin-left: 8px">
        <el-input v-model="searchForm.kuFangMingCheng" style="width: 150px" placeholder="请输入库房名称" clearable/>
      </el-form-item>
      <el-form-item label="记录日期" style="margin-left: 8px">
        <el-date-picker
            v-model="valueTime"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="search" size="mini">搜 索</el-button>
        <el-button type="primary" @click="editForm()" size="mini">添加</el-button>
        <el-button type="info" @click="resetFields()" size="mini">重 置</el-button>
        <el-button type="primary" @click="exp()" size="mini">导出</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'添加')" width="50%" :close-on-click-modal="false"
               @close="cancle">
      <el-form :model="form" :rules="rules" ref="form" label-width="150px">
        <el-form-item label="库房名称" prop="kuFangMingCheng" required>
          <el-select v-model="form.kuFangMingCheng"
                     placeholder="请选择库房" style="width: 100%"
                     clearable>
            <el-option :label="item.name" :value="item.name" v-for="item in kufangList" :key="item.id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="防火情况" prop="fangHuo" required style="margin-top: 7px">
          <el-input v-model="form.fangHuo" placeholder="请输入防火情况" clearable/>
        </el-form-item>
        <el-form-item label="防盗情况" prop="fangDao" required style="margin-top: 7px">
          <el-input v-model="form.fangDao" placeholder="请输入防盗情况" clearable/>
        </el-form-item>
        <el-form-item label="记录人" prop="userName" required style="margin-top: 7px">
          <el-input v-model="form.userName" placeholder="请输入记录人姓名" clearable/>
        </el-form-item>
        <el-form-item label="采取措施" prop="mark" style="margin-top: 7px">
          <el-input v-model="form.mark" type="textarea" placeholder="请输入措施" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancle">取 消</el-button>
        <el-button type="primary" @click="saveForm" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  data() {
    return {
      searchForm: {kuFangMingCheng: ''},
      rules: {},
      valueTime: [],
      kufangList: [],
      autoClose: true,
      defaultForm: {
        key: '',
        value: '',
        order: '',
        status: 1,
        description: ''
      },
      datas: [],
    }
  },
  methods: {

    cancle() {
      this.edit = false
      this.$emit('func')
      this.form = {}
    },

    // 获取当前登录人
    getPerson() {
      this.$http.post('/Registration/personData').then(({data}) => {
        if (data.success) {
          this.form = data.data

        }
      })
    },

    // 获取当前登录人
    getPerson1() {
      this.$http.post('/Registration/personData').then(({data}) => {
        if (data.success) {
          this.form.userName = data.data.userName
        }
      })
    },


    editForm(row) {
      this.datas = row;
      if (row == undefined) {
        this.getPerson();
        this.edit = true;
      }
      if (row.id) {
        this.getPerson1();
        console.log(row)
        this.form = row;
        this.form.userName = row.personName
        this.edit = true;
      }
    },

    saveForm() {
      let path = '/anQuan'
      if (this.datas !== undefined) {
        path = path + '/update'
      } else {
        path = path + '/insert'
      }
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$http.post(path, this.form)
              .then(({data}) => {
                if (data.success) {
                  this.kufangList = data.data
                  this.$init();
                  this.$emit('func')
                } else {
                  this.$message.error(data.message)
                }
              })
          this.edit = false;
        }
      })
    },

    search() {
      if (this.valueTime.length > 0) {
        this.searchForm.startDate = this.valueTime[0].getTime()
        this.searchForm.endDate = this.valueTime[1].getTime()
      } else {
        this.searchForm.startDate = ""
        this.searchForm.endDate = ""
      }
      this.$emit('func', this.searchForm)
    },
    resetFields() {
      this.searchForm.kuFangMingCheng = ''
      this.valueTime = []
    },
    exp() {
      if (this.valueTime.length > 0) {
        this.searchForm.startDate = this.valueTime[0].getTime()
        this.searchForm.endDate = this.valueTime[1].getTime()
      } else {
        this.searchForm.startDate = ""
        this.searchForm.endDate = ""
      }
      let url = 'api/anQuan/exp?startDate=' + this.searchForm.startDate +
          '&endDate=' + this.searchForm.endDate + '&kuFangMingCheng=' + this.searchForm.kuFangMingCheng
      window.open(url)
    },
    $init() {
      this.$http.post('/locationkufang/page', {page: false})
          .then(({data}) => {
            if (data.success) {
              this.kufangList = data.data
            } else {
              this.$message.error(data.message)
            }
          })
    }
  },
  mixins: [fromMixin]
}
</script>
