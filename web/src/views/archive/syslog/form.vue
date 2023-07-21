<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="操作：" style="padding-right: 5px">
        <el-input v-model="searchForm.operation" clearable placeholder="请输入操作"/>
      </el-form-item>
      <el-form-item label="操作人：" style="padding-right: 5px">
        <el-select placeholder="请选择操作人" v-model="searchForm.createPerson" filterable clearable>
          <el-option v-for="person in personData" :key="person.userCode" :label="person.userName"
                     :value="person.userCode"/>
        </el-select>
      </el-form-item>
      <el-form-item label="记录时间：" prop="value2" style="padding-right: 5px">
        <el-date-picker
            v-model="searchForm.value2"
            type="daterange"
            align="right"
            style="max-width: 240px"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions">
        </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="keyEnterSearch" size="mini">搜 索</el-button>
        <el-button @click="resetFields()"  type="info" >重 置</el-button>
        <el-button @click="exp" type="primary" size="mini">导 出</el-button>
        <el-button type="danger" v-if="this.multipleSelection.length>0" @click="remove">删 除</el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'
import {Base64} from 'js-base64'

export default {
  mixins: [fromMixin],
  props: {
    personData: Array, //这样可以指定传入的类型，如果类型不对，会警告
    multipleSelection: Array //这样可以指定传入的类型，如果类型不对，会警告
  },
  data() {
    return {
      edit: false,
      searchForm: {
        operation: '',
        createPerson: '',
        value2: [],
        createDate: 0,
        updateDate: 0,
      },
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
        }, {
          text: '不限时间',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(0);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  methods: {
    keyEnterSearch() {

      if (this.searchForm.value2.length === 0) {
        this.searchForm.createDate = 0
        this.searchForm.updateDate = 4784335053000
      } else {
        this.searchForm.createDate = Date.parse(this.searchForm.value2[0]);
        this.searchForm.updateDate = Date.parse(this.searchForm.value2[1]);
      }


      this.$emit('func', this.searchForm)
    },
    //导出日志
    exp() {
      if (this.searchForm.value2.length === 0) {
        this.searchForm.createDate = 0
        this.searchForm.updateDate = 4784335053000
      } else {
        this.searchForm.createDate = Date.parse(this.searchForm.value2[0]);
        this.searchForm.updateDate = Date.parse(this.searchForm.value2[1]);
      }
      let url = "api/fzlog/expLog?createPerson="
          + this.searchForm.createPerson
          + "&createDate=" + this.searchForm.createDate
          + "&updateDate=" + this.searchForm.updateDate
          + "&operation=" + this.searchForm.operation
      window.open(url)

    },
    resetFields(){
      this.searchForm= {
            operation: '',
            createPerson: '',
            value2: [],
            createDate: 0,
            updateDate: 0,
      }
    },
    apiPath() {
      return 'fzlog'
    },
    remove() {
      let ids = ""
      if (this.multipleSelection.length > 0) {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          ids += this.multipleSelection[i].id + ","
        }
      } else {
        return this.$message.warning('请选择要删除的数据')
      }
      let v = "fz"+new Date().getFullYear()
      this.$confirm("确认删除这些数据？", '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true
      }).then(() => {
        this.loading = true
        this.$http.post('/fzlog/deleteAll', {ids: Base64.encode(ids +v), no: new Date().getFullYear()})
            .then(({data}) => {
              if (data.success) {
                this.$message.success("删除成功")
                // todo 这里想办法把查询条件带过来，不然专门写删除图啥
                this.$emit('func', this.searchForm)
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
      })
    },


  },

}
</script>