<template>
  <div class="index_main" v-loading="loading" style="width: 85%;margin: auto">
    <el-card style="overflow-y: auto; height: 100%;padding: 0 5%;">
      <div v-for="(item, index) in data" :key="item.id + index">
        <el-row style="height: 17px;padding-bottom: 22px;">
          <span
              class="searchTitle"
              v-html="item.TITLE"
              @click="showFile(item)"/>
        </el-row>
        <el-row>
          <el-col>
            <span class="showValueClass">档号：</span><span class="showValueClass" v-html="item.ARCHIVE_CODE"/>
            <el-divider direction="vertical"/>
            <span class="showValueClass">保管期限：</span><span class="showValueClass" v-html="item.SAVE_TERM"/>
            <el-divider direction="vertical"/>
            <span class="showValueClass">全宗：</span><span class="showValueClass" v-html="item.FOND_NAME"/>
            <el-divider direction="vertical"/>
            <span class="showValueClass">责任者：</span><span class="showValueClass" v-html="item.DUTY_PERSON"/>
            <el-divider direction="vertical"/>
            <span class="showValueClass">文件时间：</span><span class="showValueClass" v-html="item.FILETIME"/>
            <el-divider direction="vertical"/>
            <span class="showValueClass">关键词：</span><span class="showValueClass" v-html="item.KEY_WORDS"/>
          </el-col>
        </el-row>
        <el-row v-if="item.CONTENT">
          <el-col>
            <p v-html="item.CONTENT.substring(0,400)" class="searchContent"/>
          </el-col>
        </el-row>
        <el-row>
          <span class="showValueClass">标签:<el-tag class="showValueClass" v-show="item.TAG_NAME">{{
              item.TAG_NAME
            }}</el-tag></span>
        </el-row>
        <el-divider/>
      </div>
    </el-card>
    <el-pagination :page-size="page.size"
                   :current-page="page.index"
                   @current-change="index=>changePage(index)"
                   :total="page.total"
                   layout="total, prev, pager, next"/>
  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {}
  },
  props: {
    data: [],
    page: {},
    loading: Boolean,
  },
  methods: {
    changePage(index) {
      this.$emit('changePage', index)
    },
    showFile(item) {
      this.$emit('showFile', item)
    }
  }
}
</script>

<style lang="scss">
.searchTitle {
  color: #2453b3;
  font-family: MicrosoftYaHei;
  font-size: 17px;
  line-height: 57px;
  font-weight: normal;
  font-stretch: normal;
  cursor: pointer;
}

.showValueClass {
  color: #373c45;
  height: 13px;
  font-size: 15px;
  font-family: MicrosoftYaHei;
  font-weight: normal;
  font-stretch: normal;
  letter-spacing: 0px;
}

.searchContent {
  width: 1190px;
  height: 32px;
  font-family: MicrosoftYaHei;
  font-size: 14px;
  font-weight: normal;
  font-stretch: normal;
  line-height: 18px;
  letter-spacing: 0px;
  color: #5c6673;
}

.searchTag {
  width: 65px;
  height: 12px;
  font-family: MicrosoftYaHei;
  font-size: 13px;
  font-weight: normal;
  font-stretch: normal;
  line-height: 57px;
  letter-spacing: 0px;
  color: #7a8899;
}
</style>
