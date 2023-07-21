<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="库房名称" prop="kuFangMingCheng" style="margin-left: 8px">
        <el-input v-model="searchForm.kuFangMingCheng" style="width: 150px" placeholder="请输入库房名称" clearable/>
      </el-form-item>
      <el-form-item label="消毒时间" style="margin-left: 8px">
        <el-date-picker
            v-model="valueTime"
            type="daterange"
            align="right"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="search" size="mini">搜 索</el-button>
        <el-button type="primary" @click="editForm()" size="mini">添加</el-button>
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
        <el-form-item label="任务名称" prop="taskName" required style="margin-top: 7px">
          <el-input v-model="form.taskName" placeholder="请输入任务名称" clearable/>
        </el-form-item>
        <el-form-item label="申请单位" prop="unit" required style="margin-top: 7px">
          <el-input v-model="form.unit" placeholder="请输入申请单位" clearable/>
        </el-form-item>
        <el-form-item label="记录人" prop="userName" required style="margin-top: 7px">
          <el-input v-model="form.userName" placeholder="请输入记录人姓名" clearable/>
        </el-form-item>
        <el-form-item label="题名" prop="title" required style="margin-top: 7px">
          <el-input v-model="form.title" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="档号" prop="archiveCode" required style="margin-top: 7px">
          <el-input v-model="form.archiveCode" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="复制用途" prop="copyPurpose" required style="margin-top: 7px">
          <el-input v-model="form.copyPurpose" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="仿真要求" prop="copyRequire" required style="margin-top: 7px">
          <el-input v-model="form.copyRequire" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="处理情况" prop="situation" required style="margin-top: 7px">
          <el-input v-model="form.situation" placeholder="" clearable/>
        </el-form-item>
        <el-form-item label="备注" prop="mark" style="margin-top: 7px">
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
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      defaultForm: {
        key: '',
        value: '',
        order: '',
        status: 1,
        description: ''
      },
      datas: []
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
      let path = '/fangZhen'
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
      console.log(this.valueTime)
      if (this.valueTime != null && this.valueTime.length > 0) {
        this.searchForm.startDate = this.valueTime[0].getTime()
        this.searchForm.endDate = this.valueTime[1].getTime()
      } else {
        this.searchForm.startDate = ""
        this.searchForm.endDate = ""
      }
      this.$emit('func', this.searchForm)
    },
    exp() {
      if (this.valueTime != null && this.valueTime.length > 0) {
        this.searchForm.startDate = this.valueTime[0].getTime()
        this.searchForm.endDate = this.valueTime[1].getTime()
      } else {
        this.searchForm.startDate = ""
        this.searchForm.endDate = ""
      }
      let url = 'api/fangZhen/exp?startDate=' + this.searchForm.startDate +
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
