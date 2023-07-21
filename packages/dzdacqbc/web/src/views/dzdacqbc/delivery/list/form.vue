<template>
  <section>
    <nac-info title="出库清单">
      <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
        <el-form-item label="批次：" prop="archiveName">
          <el-input v-model="searchForm.archiveName" clearable placeholder="请输入批次"/>
        </el-form-item>
        <el-form-item label="状态：" prop="examineState">
          <el-select  v-model="searchForm.examineState" clearable placeholder="请选择状态">
            <el-option value="1" label="待出库"></el-option>
            <el-option value="3" label="已出库"></el-option>
          </el-select>
      </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search" size="mini">搜 索</el-button>
          <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
<!--          <el-button type="info" size="small" @click="$router.back()">返回</el-button>-->
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
      return '/delivery'
    },
  }
}
</script>
