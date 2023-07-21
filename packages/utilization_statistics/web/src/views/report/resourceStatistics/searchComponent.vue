<template>
  <section>
    <el-select v-model="resourceStatistics.fondCode" filterable placeholder="请选择全宗" style="width: 200px" clearable>
      <el-option
          v-for="i in fonds"
          :key="i.id"
          :label="`${i.data.code} ${ i.label}`"
          :value="i.data.code">
      </el-option>
    </el-select>
    <el-input v-model="resourceStatistics.vintages" placeholder="输入年度查询" style="width: 200px" clearable/>
    <el-button type="primary" @click="search">查询</el-button>
    <el-button @click="updateData" type="primary" v-if="updateBtn">更新数据</el-button>
  </section>
</template>

<script>
export default {
  name: "searchComponent",
  props: {
    updateBtn: false
  },
  data() {
    return {
      //全宗数据
      fonds: [],
      resourceStatistics: {
        //全宗code
        fondCode: '',
        categoryCode: '',
        //年度
        vintages: ''
      }
    }
  },
  methods: {
    //获取全宗下拉框
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'});
      this.fonds = data.data
    },
    //手动更新数据库数据
    async updateData() {
      const {data} = await this.$http.post('/resourceStatistics/count')
      if (data.success) {
        this.$message.success('正在更新...')
      } else {
        this.$message.success(data.message)
      }
    },
    $init() {
      this.getFonds()
    },
    search() {
      this.$emit('search', this.resourceStatistics)
    }
  }
}
</script>