<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="记录类型：">
        <el-select v-model="searchForm.doType" placeholder="请选择记录类型" clearable>
          <el-option label="入库" value="1"></el-option>
          <el-option label="出库" value="0"></el-option>
        </el-select>
      </el-form-item>
      &nbsp; &nbsp;
      <el-form-item label="档号：" prop="archiveCode">
        <el-input v-model="searchForm.archiveCode" clearable placeholder="请输入档号"/>
      </el-form-item>
      &nbsp; &nbsp;
      <el-form-item label="记录时间：" prop="value2">
        <el-date-picker
            v-model="searchForm.value2"
            type="daterange"
            align="right"
            style="max-width: 240px"
            unlink-panels
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            :picker-options="pickerOptions"
            value-format="yyyy-MM-dd HH:mm:ss">
        </el-date-picker>
      </el-form-item>
      &nbsp; &nbsp;
      <el-form-item>
        <el-button type="primary" @click="keyEnterSearch" size="mini">搜 索</el-button>
        <el-button @click="resetFields()" type="info">重 置</el-button>
        <el-button type="primary" size="mini" @click="exp">导 出</el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  mixins: [fromMixin],
  data() {
    return {
      edit: false,
      searchForm: {
        doType: '',
        createDate: '',
        updateDate: '',
        archiveCode: '',
        value2: []
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
  mounted() {
    let date = new Date();
    date.setTime(946686600000);
    let utcDate = date.getUTCDate();
    this.value2 = [utcDate, new Date()]
  },
  methods: {
    apiPath() {
      return 'his'
    },
    $init() {
      this.keyEnterSearch()
    },
    resetFields(){
      this.searchForm={
        doType: '',
        archiveCode:'',
        value2: ''
      }
    },
    keyEnterSearch() {

      if(this.searchForm.value2 === null || this.searchForm.value2 === undefined){
        this.searchForm.createDate = ''
        this.searchForm.updateDate = ''
      }else{
        if(this.searchForm.value2[0] != undefined && this.searchForm.value2[0] != null){
          this.searchForm.createDate = Date.parse(this.searchForm.value2[0])
        }
        if(this.searchForm.value2[1] != undefined && this.searchForm.value2[1] != null){
          this.searchForm.updateDate = Date.parse(this.searchForm.value2[1])
        }
      }

     // this.searchForm.updateDate = Date.parse(this.searchForm.value2[1]);
      // this.search(this.searchForm)
      this.$emit('func', this.searchForm)
    },
    exp() {
      if (!this.searchForm.value2) {
        this.searchForm.createDate = '';
        this.searchForm.updateDate = '';
      } else {
        this.searchForm.createDate = this.searchForm.value2[0];
        this.searchForm.updateDate = this.searchForm.value2[1];
      }

      let archiveCode = ''
      if (this.searchForm.archiveCode) {
        archiveCode = this.searchForm.archiveCode;
      }
      let doType = ''
      if (this.searchForm.doType) {
        doType = this.searchForm.doType;
      }

      let url = "api/his/exp?id=1&doType=" + doType +
          "&startTime=" + this.searchForm.createDate + "&endTime=" + this.searchForm.updateDate
          + "&archiveCode=" + archiveCode;
      window.open(url)
      this.loading = false
    },

  },

}
</script>