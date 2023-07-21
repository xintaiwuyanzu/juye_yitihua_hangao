<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="记录名称：" prop="filingName">
          <el-input v-model="searchForm.filingName" clearable placeholder="请输入记录名称"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
          <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
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
    }
  },
  mixins: [fromMixin],
  methods: {
    $init() {
    },
    apiPath() {
      return '/filing'
    },
  }
}
</script>
