<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="档号：" prop="archiveCode">
          <el-input v-model="searchForm.archiveCode" clearable placeholder="请输入档号"></el-input>
        </el-form-item>
        <el-form-item label=" 类型：" prop="changeType">
          <el-select v-model="searchForm.changeType" clearable placeholder="请选择变更类型">
            <el-option v-for="item in options"
                       :label="item.name"
                       :value="item.type"
                       :key="item.type"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label=" 时间段：" prop="dateTime">
          <el-date-picker v-model="searchForm.dateTime" type="datetimerange"
                          range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                          @change="changeDateTime" value-format="yyyy-MM-dd HH:mm:ss"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
          <el-button @click="resetForm" size="mini">重 置</el-button>
        </el-form-item>
      </el-form>
    </nac-info>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  data() {
    return {
      form: {},
      person: {},
      options: [
        {name: '接收', type: 'RECEIVE'},
        {name: '添加', type: 'ADD'},
        {name: '编辑', type: 'EDIT'},
        {name: '调整', type: 'TIAOZHENG'},
        {name: '入库', type: 'INGL'},
        {name: '进入回收站', type: 'RECYCLE'},
        {name: '恢复', type: 'RECOVERY'},
        {name: '鉴定', type: 'JIANDING'},
      ],
      searchForm: {
        archiveCode: '',
        changeType: '',
        dateTime: [],
      }
    }
  },
  mixins: [fromMixin],
  methods: {
    $init() {
    },
    apiPath() {
      return '/archiveMetadataRecord'
    },
    changeDateTime() {
      this.searchForm.createPerson = this.searchForm.dateTime[0]
      this.searchForm.updatePerson = this.searchForm.dateTime[1]
    },
    resetForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm.updatePerson = ''
      this.searchForm.createPerson = ''
    }
  }
}
</script>
