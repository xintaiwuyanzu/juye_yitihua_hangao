<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="操作人" prop="createPersonName">
        <el-input v-model="searchForm.createPersonName" style="width: 160px" placeholder="操作人" clearable/>
      </el-form-item>
      <el-form-item label="版本号" prop="versionNum">
        <el-input v-model="searchForm.versionNum" style="width: 160px" placeholder="版本号" clearable/>
      </el-form-item>
      <el-form-item label="系统" prop="sysName">
        <el-input v-model="searchForm.sysName" style="width: 160px" placeholder="系统" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click="search" size="mini">搜 索</el-button>
        <!--<el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>-->
        <el-button type="primary" @click="check('zw')" size="mini" v-loading="loading">检索智网</el-button>
        <el-button type="primary" @click="check('zww')" size="mini" v-loading="loading">检索政务网</el-button>
        <el-button type="primary" @click="check('jyw')" size="mini" v-loading="loading">检索局域网</el-button>
      </el-form-item>
    </el-form>

    <el-dialog title="新的备份" :visible.sync="autoClose">
      <el-table :data="showdata" border>
        <el-table-column property="backupRecoveryPath" label="备份路径" width="550"></el-table-column>
        <el-table-column property="versionNum" label="版本号"></el-table-column>
        <el-table-column property="sysName" label="系统"></el-table-column>
        <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
          <template v-slot="scope">
            <!--            <el-link type="danger" @click="remove(scope.row.id)">删 除</el-link>-->
            <!--            |-->
            <!--            <el-link type="success" @click="dowload(scope.row)">下 载</el-link>-->
            <!--            |-->
            <el-link type="warning" @click="recovery(scope.row)">接收同步数据</el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@archive/core/src/util/formMixin'

export default {
  data() {
    return {
      searchForm: {},
      rules: {},
      showdata: [],
      autoClose: false,

    }
  },
  methods: {
    search() {
      this.$emit('func', this.searchForm)
    },

    check(b) {
      //检索其它系统的新备份版本

      this.loading = true
      this.$http.post('/recovery/check', {sysName: b}).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.showdata = data.data
          this.autoClose = true

        }
        this.loading = false
      })
    },
    /**
     * 同步
     * @param row
     */
    recovery(row) {
      this.$http.post(this.apiPath() + '/recovery', {
        filePath: row.backupRecoveryPath,
        versionNum: row.versionNum,
        showPath: row.showPath,
        sysName: row.sysName,
      }).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.autoClose = false
          this.loadData()
        }
        this.loading = false
      })
    }
  },
  mixins: [fromMixin]
}
</script>
