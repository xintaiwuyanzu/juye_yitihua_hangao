<template>
  <section>
    <nac-info>
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="审批环节名称：" prop="taskName">
          <el-input v-model="searchForm.taskName" clearable placeholder="请输入审批环节名称"/>
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
      searchForm: {processType: 'FILING'},
    }
  },
  mixins: [fromMixin],
  methods: {
    $init() {
    },
    apiPath() {
      return '/filingTask'
    },
  }
}
</script>
