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
        <el-button type="success" @click="shaixuan()" size="mini" v-loading="loading">筛选同步数据</el-button>
        <el-button type="success" @click="wj()" size="mini" v-loading="loading">备份文件数据</el-button>

      </el-form-item>
    </el-form>
    <el-dialog title="筛选" :visible.sync="autoClose" width="40%">
      <el-transfer
          filterable
          :filter-method="filterMethod"
          filter-placeholder="请输入筛选条件"
          v-model="value"
          :button-texts="['到左边', '到右边']"
          :titles="['备选项', '已选项']"
          :data="data">
      </el-transfer>
      <div style="text-align: center;margin-top: 10px;">
        <el-button type="primary" @click="backup()" size="mini" v-loading="loading">发送同步数据</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@archive/core/src/util/formMixin'

export default {
  data() {
    const generateData = _ => {
      const data = [];
      const cities = ['长期保存库', '管理库', '利用库', '水印管理'];
      const pinyin = ['changqibaocunku', 'guanliku', 'liyongku', 'shuiyin'];
      cities.forEach((city, index) => {
        data.push({
          label: city,
          key: index,
          pinyin: pinyin[index]
        });
      });
      return data;
    };
    return {
      data: generateData(),
      value: [],
      filterMethod(query, item) {
        return item.pinyin.indexOf(query) > -1;
      },
      searchForm: {},
      rules: {},
      autoClose: false,
    }
  },
  methods: {
    search() {
      this.$emit('func', this.searchForm)
    },
    wj() {
      this.$http.post('/backup/wjbackup').then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.autoClose = false
          this.search()
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    backup() {
      let mo = ''
      //console.log(this.value)
      if (this.value.length > 0) {

        for (let i = 0; i < this.value.length; i++) {
         // console.log(this.data[this.value[i]])
          mo = mo + this.data[this.value[i]].pinyin + ','
        }
      } else {
        return this.$message.warning('请选择筛选条件')
      }

      //console.log(mo)
      this.loading = true
      this.$http.post('/backup/backup', {table: mo}).then(({data}) => {
        if (data && data.success) {
          this.$message.success(data.data)
          this.autoClose = false
          this.search()
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    shaixuan() {
      this.autoClose = true
    }
  },
  mixins: [fromMixin]
}
</script>
