<template>
  <section class="search_main">
    <!--    <nac-info title="全文检索" :showTitle="false"/>-->
    <section class="search_title" v-if="initStyle">
      <h2 class="text-center">
        <i class="el-icon-search"/>
        档案检索—{{ multiSearch|searchTitle }}
      </h2>
    </section>
    <section class="search_form">
      <multi-search-item v-if="multiSearch" ref="search" @search="doSearch"/>
      <key-word-search ref="search" @search="doSearch" v-else/>
      <el-button type="primary" @click="multiSearch=!multiSearch" class="mSearch" style="margin-left: 10px">
        {{ !multiSearch|searchTitle }}
      </el-button>
      <el-button type="primary" @click="doSearch(true)">二次检索</el-button>
      <!--      <el-button @click="smartSearch" type="primary">智能化检索</el-button>-->
      <slot/>
    </section>
    <el-container class="search_result" :class="{'backGroundImg' : initStyle}" v-loading="loading">
      <el-aside class="result_agg" v-if="!initStyle && aggs">
        <agg-count :badge-data="badgeData" :tag-data="tagData"></agg-count>
      </el-aside>
      <el-main class="result_list" v-if="data.length>0">
        <result-render v-for="(item,index) in data" :key=item.id :item="item" :index="page.size*(page.index-1)+index">
          <slot :item="item" name="btns"/>
        </result-render>
        <page class="search_page" :page="page" v-if="data&&data.length>0"
              @current-change="index=>handleCurrentChange(index)"/>
      </el-main>
      <el-main class="result_list" v-else-if="keyWord">
        <el-empty description="暂无数据，请重新搜索" style="margin-top: 40px"/>
      </el-main>
      <!--      <el-main class="backGroundImg" v-else  style="margin-top: 300px">
              &lt;!&ndash;        <el-empty description="暂无数据，请重新搜索" style="margin-top: 40px"/>&ndash;&gt;
            </el-main>-->
    </el-container>
  </section>
</template>
<script>
import MultiSearchItem from "../multiSearchItem";
import KeyWordSearch from "../keyWordSearch";
import ResultRender from "../resultRender";
import AggCount from "../aggCount";
import {useMenu} from "@dr/framework/src/hooks/useMenu";

/**
 * 查询页面基础功能组件
 */
export default {
  name: "searchContainer",
  components: {ResultRender, KeyWordSearch, MultiSearchItem, AggCount},
  props: {
    //可以传查询参数进来
    keyWord: {type: String},
    aggs: {type: Boolean, default: false},
    category: {type: String},  //可以传门类编码参数进来,多个门类编码用','分割
    fondCode: {type: String},  //可以传门类编码参数进来,多个门类编码用','分割
  },
  setup() {
    const {setName} = useMenu()
    setName('全文检索', '/utilization/search')
  },
  data() {
    return {
      //初始样式
      initStyle: true,
      multiSearch: false,
      //加载状态
      loading: false,
      //查询到的数据
      data: [],
      //这个是啥
      badgeData: [],
      tagData: [],
      //分页数据
      page: {
        index: 1,
        size: 50,
        total: 0
      },
      //所有子查询组件
      queryItems: [],
      //查询历史
      queryList: []
    }
  },
  provide() {
    return {
      searchContainer: this
    }
  },
  created() {
    //监听所有子组件
    this.$on('addQuery', item => {
      if (item) {
        this.queryItems.push(item)
      }
    })
    this.$on('removeQuery', item => {
      if (item) {
        this.queryItems.splice(this.queryItems.indexOf(item), 1)
      }
    })
  },
  methods: {
    $init() {
      if (this.keyWord) {
        this.$nextTick(() => {
          this.resetQuery({keyWords: this.keyWord}, true)
        })
      }
    },
    /**
     * 重置查询条件
     * @param history
     */
    resetQuery(history, search) {
      //判断查询类型 二次,高级,全文
      if (history) {
        if (history.secondRetrieval) { //二次
          let querys = JSON.parse(history.querysListJson)
          if (querys.length > 0) {
            this.queryList = querys.splice(querys.length - 1) //查询历史
            this.rQuery(querys[querys.length - 1]) //最后一次查询 回显
            if (search) {
              setTimeout(() => {
                this.doSearch(true)
              }, 40)
            }
          }
        } else {
          this.rQuery(history)
          if (search) {
            //如果接着需要执行查询方法，则直接执行
            setTimeout(() => {
              this.doSearch()
            }, 40)
          }
        }
      }
    },
    rQuery(history) {
      this.multiSearch = history.multiSearch
      //检索历史回显时无法获取$refs,要等待子组件加载完成
      setTimeout(() => {
        if (history.multiSearch) { //高级
          if (this.$refs.search.resetMultiQuery) {
            this.$refs.search.resetMultiQuery(history)
          }
        } else {
          if (this.$refs.search.resetQuery) {
            this.$refs.search.resetQuery(history)
          }
        }
      }, 20)

    },
    /**
     * 真正执行查询方法
     * @returns {Promise<void>}
     */
    async doSearch(isSecond = false) {
      this.initStyle = false
      this.loading = true
      let requestData = {querysListJson: []}
      //遍历所有子组件，寻找查询方法
      this.queryItems.forEach(i => {
        if (i.getQuery) {
          const query = i.getQuery()
          if (query) {
            Object.keys(query).forEach(k => {
              if (k === 'querysListJson') {
                requestData.querysListJson = requestData.querysListJson.concat(query[k])
              } else {
                requestData[k] = query[k]
              }
            })
          }
        }
      })
      if (isSecond) {
        requestData.querysListJson = this.queryList.concat(requestData.querysListJson)
        requestData.secondRetrieval = true
      }
      this.queryList = requestData.querysListJson
      requestData.querysListJson = JSON.stringify(requestData.querysListJson)
      requestData.index = this.page.index == undefined ? 0 : this.page.index - 1
      //判断检索条件是否为空
      for (let i = 0; i < this.queryList.length; i++) {
        if (this.queryList[i].multiSearch) {
          if (this.queryList[i].keyWords == "[]") {
            this.$message.warning("请输入查询条件")
            this.loading = false
            return
          }
        } else {
          if (this.queryList[i].keyWords.split(" ").join("").length == 0) {
            this.$message.warning("请输入查询条件")
            this.loading = false
            return
          }
        }
      }
      if (this.queryList.length === 1) {
        const kwd = this.queryList[0].keyWords
        if (kwd !== this.$route.query.keyword) {
          this.$router.replace({query: {keyword: this.queryList[0].keyWords}})
        }
      }
      requestData.size = 50
      if (this.category) {  //这个数据主要是首页传过来的门类编码
        requestData.category = this.category
      }
      if (this.fondCode) {  //这个数据主要是首页传过来的全宗编码
        requestData.fondCode = this.fondCode
      }
      const {data} = await this.$post('/search/searchBykeyWords', requestData)
      if (data.success) {
        if (this.data = data.data.data != null) {
          this.data = data.data.data[0].data
          this.badgeData = data.data.data[0].count
          this.tagData = data.data.data[0].tagCount
        }
        this.page.total = data.data.total
        this.page.index = data.data.start + 1
      } else {
        this.data = []
      }
      this.loading = false
    }
    ,
    handleCurrentChange(val) {
      this.page.index = val
      this.doSearch()
    },
    smartSearch() {
      this.$router.push({
        path: '/mapping/smartSearch/search',
        query: {content: this.$refs.search.keyWord, classType: 'person'}
      })
    }
  },
  filters: {
    //搜索标题映射
    searchTitle(multiSearch) {
      return multiSearch ? '高级检索' : '全文检索'
    }
  }
}
</script>
<style lang="scss">
.el-input-group__append button.el-button {
  background: #0168B3;
  color: #FFFFFF;
}

.el-input-group--append .el-input__inner {
  height: 34px;
}

.el-button {
  margin-left: 10px;
}

.search_main {

  .search_title {
    margin-bottom: 25px;
    margin-top: 65px;
    font-size: 17.5px;
    font-weight: 500;

    .multi-container {
      max-height: 100px;
      //overflow: auto;
      display: inline-block;
    }

    .text-center {
      text-align: center;
    }
  }

  .search_form {
    text-align: center;
    align-content: center;

    .multi_search_item {
      display: inline-block;

      .search_items {
        display: inline-block
      }
    }
  }

  .backGroundImg {
    background-image: url('./hyym.png');
  }

  .search_result {
    flex: 1;
    overflow: auto;
    background-repeat: no-repeat;
    background-position: center center;
    padding: 10px 20px;
    display: flex;
    //flex-direction: column;

    .result_list {
      flex: 1;
      overflow: auto;
      padding: 0px;

      .search_result_item {
        margin-bottom: 6px;


        .result_title {
          display: flex;
          height: 32px;
          position: relative;

          .result_text {
            font-weight: bold;
            justify-content: left;
            font-size: 18px;
            white-space: nowrap;
            overflow-x: hidden;
          }

          .result_btns {
            background-color: white;
            position: absolute;
            right: -15px;
            min-width: 80px
          }
        }
      }
    }

    .result_agg {
      padding-right: 20px;

      .result_agg_item {
        margin-bottom: 6px;

        .result_agg_row {
          min-height: 30px;
          margin: 6px;
          padding-left: 10px;
        }

        .el-collapse-item__header {
          font-weight: bold;
          padding-left: 10px;
        }
      }
    }

    .el-pagination {
      text-align: right;
      margin: 10px 0px;
    }
  }


  .item-container {
    width: 100%;
    display: flex;
    margin-bottom: 4px;
  }

  .search_page {
    position: fixed;
    left: 50%;
    bottom: 0;
  }
}
</style>
