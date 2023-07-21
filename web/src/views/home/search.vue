<template>
  <el-tabs :stretch="true" style="text-align: center;" tab-position="left" type="border-card"
           v-model="activeName"
           v-loading="loading">
    <!--    左侧标题-->
    <!--    <el-tab-pane label="" name="first">-->
    <img src="../../../public/images/logo.png" style="margin-top: 1%;margin-bottom: 4px">
    <home-search @search="search"/>
<!--    <el-divider/>
    <div style="overflow-y: scroll;">
      <el-form :model="searchFirst" label-width="100" style="font-size: 16px" v-if="isDag==='dag'">
        <el-form-item>
          <el-checkbox-group v-model="searchFirst.type">
            <el-checkbox :key="index" :label="i.code" v-for="(i,index) in conditionFirst.types" style="width: 15%">
              <span style="width: 10%">{{ i.name }}</span>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <el-form :model="searchFirst" label-width="100" style="font-size: 16px" v-else>
        <el-form-item>
          <el-checkbox-group v-model="searchFirst.type">
            <el-checkbox :key="index" :label="i.code" v-for="(i,index) in conditionFirst.types" style="width: 15%">
              <span style="width: 10%">{{ i.name }}</span>
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
    </div>-->
    <!--    </el-tab-pane>-->
    <!--    <el-tab-pane label="智能化检索" name="two">
          <smart-search :search="search" class="smartSearch"/>
        </el-tab-pane>-->
  </el-tabs>
</template>
<script>
import smartSearch from '@archive/manage-mapping/src/views/mapping/smartSearch'

export default {
  name: "search",
  components: {smartSearch},
  data() {
    return {
      loading: false,
      isDag: '',
      activeName: 'first',
      //条目检索表单
      searchFirst: {
        type: [],
      },
      //条件
      conditionFirst: {
        types: [],
      },
    }
  },
  methods: {
    search(keyword) {
      if (keyword.length < 1) {
        this.$message.warning("请输入查询条件")
      } else {
        // let search = {} TODO 具体参数
        switch (this.activeName) {
          case "first":
            // search = {
            //   type: this.searchFirst.type.toString(),
            //   source: this.searchFirst.source.toString(),
            // }
            break
          case 'two':
            break
          default:
            break
        }
        let categrorys = this.searchFirst.type.join(',');
        if (this.isDag === 'dag') {
          this.$router.push({path: '/utilization/search', query: {keyword: keyword, fondCode: categrorys}})
        }else {
          this.$router.push({path: '/utilization/search', query: {keyword: keyword, category: categrorys}})
        }
      }
    },
    async $init() {
      await this.getOrganiseType()
      if (this.isDag === 'dag') {
        await this.getFonds()
      } else {
        await this.getCategorys()
      }
    },
    async getOrganiseType() {
      const {data} = await this.$post('/manage/handover/organiseType')
      if (data.success && data.data) {
        this.isDag = data.data.organiseType
      }
    },
    async getCategorys() {
      this.loading = true
      const {data} = await this.$post('search/getCategorys',)
      if (data && data.success) {
        this.conditionFirst.types = data.data.sort((a, b) => {
          return a.order - b.order
        })
      }
      this.loading = false
    },
    async getFonds() {
      this.loading = true
      const {data} = await this.$post('search/getFonds')
      if (data && data.success) {
        this.conditionFirst.types = data.data.sort((a, b) => {
          return a.order - b.order
        })
      }
      this.loading = false
    }
  }
}
</script>

<style lang="scss">
.index_search {
  .smartSearch {
    margin-top: -80px;
  }
}
</style>