<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="操作人" prop="createPersonName">
        <el-input v-model="searchForm.createPersonName" style="width: 160px" placeholder="操作人" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="search" size="mini">搜 索</el-button>
        <!--<el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>-->
        <el-button type="primary" @click="backup()" size="mini" v-loading="loading">备 份</el-button>
      </el-form-item>
    </el-form>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'

export default {
  data() {
    return {
      searchForm: {},
      rules: {},
      autoClose: true,
    }
  },
  methods: {
    search() {
      this.$emit('func', this.searchForm)
    },
    backup() {
      this.loading = true
      this.$http.post('/backuprecovery/backup').then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.search()
        }
        this.loading = false
      })
    }
  },
  mixins: [fromMixin]
}
</script>
