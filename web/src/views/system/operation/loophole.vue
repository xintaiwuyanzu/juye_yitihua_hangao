<template>
  <el-card v-loading="loading" class="cards">
    <span class="system_access">存储空间
      <span style="float: right;font-size: 13px;cursor:pointer" @click="$router.push('/dzdacqbc/spaces')">更多 ></span>
    </span>
    <div class="system_content">
      <el-row  class="row" v-for="item in access">
        <el-col>
          <span class="label">{{item.spaceName}}</span>
        </el-col>
        <el-col>
          <el-progress :text-inside="true" :stroke-width="20" :percentage="item.percent"
                       class="space-progress"
                       :status="item.percent <= 50? 'success' :
                             item.percent <= 70? 'warning' : 'exception'" :format="setItemText(item)"/>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>
export default {
  name: "loophole",
  data() {
    return {
      loading: false,
      access: []
    }
  },
  methods: {
    $init() {
      this.$http.post('/earchive/spaces/page').then(({data}) => {
        if (data && data.success) {
          this.access = data.data.data
        }else{
          return ''
        }
      })
    },
    setItemText(item) {
      return () => {
        return parseFloat(item.usedSpace).toFixed(2) + 'G/' + parseFloat(item.capacity).toFixed(2) + 'G \xa0\xa0|\xa0\xa0 ' + item.percent + '%'
      }
    },
  },
  computed: {

  }
}
</script>

<style lang="scss" scoped>
.label{
  font-size: 15px;
  font-weight: normal;
  font-stretch: normal;
  line-height: 32px;
  color: #606266;
}
.system_content{
  margin-top: 7%;
  .row{
    padding: 10px 0;
  }
}
.cards{
  padding: 16px;
}
.system_access {
  //padding-bottom: 10px;
  height: 80%;
  font-size: 20px;
  font-weight: 500;
  font-stretch: normal;
  line-height: 25px;
  letter-spacing: 1px;
  color: $--color-primary;
}
</style>