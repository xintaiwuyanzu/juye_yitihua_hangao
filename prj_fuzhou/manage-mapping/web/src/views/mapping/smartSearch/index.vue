<template>
  <section title="智能化检索">
    <div class="index_main">
      <el-row style="margin: 0">
        <el-col :offset="6" :span="12">
          <div class="search">
            <section class="search_title">
              <h2 class="text-center">
                <i class="el-icon-search"/>
                智能化检索
              </h2>
            </section>
            <div>
              <el-radio-group size="medium" v-model="classType" style="margin-bottom: 10px;">
                <el-radio-button label="全部"></el-radio-button>
                <el-radio-button :key="i.value" :label="i.value" v-for="(i,index) in types">
                  {{ i.label }}
                </el-radio-button>
              </el-radio-group>
            </div>
            <div style="display: flex">
              <el-input style="min-width: 380px" placeholder="请输入要检索的内容" v-model="content">
                <!--              <el-button @click="search" icon="el-icon-search" slot="append">搜索</el-button>-->
              </el-input>
              <el-button @click="searchData" style="margin-left: 20px" type="primary">搜索</el-button>
              <el-button type="info" @click="cancel">重置</el-button>
            </div>
            <div style="margin-top: 10px">
              <div>
                <span style="font-size: small">热搜:</span>
                <el-descriptions :column="3" class="hotSeach_row">
                  <el-descriptions-item :label="index+1" :key="item.id" v-for="(item,index) in hotTop6"
                                        labelStyle="background-color: transparent">
                    <el-link @click="hotSearch(item.content,item.classType)">{{ item.content }}</el-link>

                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      content: '',
      b: true,
      classType: 'person',
      result: [],
      forms: [],
      types: [{label: '人', value: 'person'},
        {label: '事', value: 'event'},
        {label: '物', value: 'thing'},
        {label: '地', value: 'land'},
        {label: '组织', value: 'organize'}],
      hotTop6: []
    }
  },
  methods: {
    $init() {
      this.getHotTop6()
      this.getForms()
    },
    async getHotTop6() {
      const {data} = await this.$post('smartSearchHistory/hopTop6')
      if (data && data.success) {
        this.hotTop6 = data.data
      }
    },
    async getForms() {
      const {data} = await this.$post('relation/getForms', {id: ''})
      if (data && data.success) {
        this.forms = data.data
      }
    },
    hotSearch(hotContent) {
      this.content = hotContent
      this.searchData()
    },
    searchData() {
      this.$router.push({
        path: '/mapping/smartSearch/search',
        query: {
          content: this.content,
          classType: this.classType
        }
      })
    },
    cancel() {
      this.classType = 'person'
      this.content = ''
    }
  }
}
</script>

<style lang="scss">
.search_title {
  margin-bottom: 25px;
  margin-top: 65px;
  font-size: 17.5px;
  font-weight: 500;

  .multi-container {
    max-height: 100px;
    display: inline-block;
  }

  .text-center {
    text-align: center;
  }
}

.search {
  margin: 1vh 0;
}

.hotSeach_row {
  margin-top: 15px;
  font-size: small;
}

.hotSeach_row.el-descriptions {
  .el-descriptions__body {
    background-color: transparent
  }
}

.hotSeach_col {
  /*width: 100%; 一定要设置宽度，或者元素内含的百分比*/
  overflow: hidden; /*溢出的部分隐藏*/
  white-space: nowrap; /*文本不换行*/
  text-overflow: ellipsis; /*ellipsis:文本溢出显示省略号（...）；clip：不显示省略标记（...），而是简单的裁切*/
  margin-right: 15px;
}

.hotSeach_order {
  margin: 0 5px;
}

.hotSeach_order_top3 {
  margin: 0 5px;
  color: red;
}

</style>