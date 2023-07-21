<template>
  <section>
    <nac-info>
      <span style="float: right">
        <el-button @click="$router.back()" type="primary">返回</el-button>
      </span>
    </nac-info>
    <el-card style="height: 85vh;overflow:scroll">
      <el-card style="min-height: 15vh">
        <span style="font-weight: bold">内容:</span>
        {{ fileContent }}
      </el-card>
      <el-tabs type="border-card" style="min-height: 60vh">
        <el-tab-pane label="词性分析" @tab-click="getWord">
          <div style="height: 50vh">
            <div style="height: 100%;width:16%;float:left;border-right: 1px solid #B6B6B6;margin: 5px">
              <p style="font-weight: bold">词性类别图示</p>
              <el-tag v-for="tags in cxType"
                      :key="tags.tag"
                      :color="tags.color"
                      size="medium"
                      effect="dark"
                      style="margin-top: 10px;margin-left: 10px;border: 0;width: 70px;text-align: center">
                {{ tags.tag }}
              </el-tag>
            </div>
            <div style="height: 100%;width:81%;float:left;margin: 10px;overflow-y:auto">
              <el-tag v-for="(etags,index) in cxTag"
                      :key="index"
                      :color="getColor('cx',etags.type)"
                      size="medium"
                      effect="dark"
                      style="margin: 5px;border: 0">
                {{ etags.name }}
              </el-tag>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="实体识别" @tab-click="getWord">
          <div style="height: 50vh">
            <div style="height: 100%;width:15%;float:left;border-right: 1px solid #B6B6B6;margin: 5px">
              <span style="font-weight: bold">实体类别图示</span>
              <p v-for="tags in stType" :key="tags.tag" style="width: 100%;margin-top: 10px">
                <el-tag :color="tags.color"
                        size="medium"
                        effect="dark"
                        style="margin-top: 5px;margin-left: 10px;border: 0;width: 70px;text-align: center">
                  {{ tags.tag }}
                </el-tag>
              </p>
            </div>
            <div style="height: 100%;width:82%;float:left;margin: 10px;overflow-y:auto">
              <el-tag v-for="(etags,index) in entityTag"
                      :key="index"
                      :color="getColor('st',etags.type)"
                      size="medium"
                      effect="dark"
                      style="margin: 5px;border: 0">
                {{ etags.name }}
              </el-tag>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="关键词提取" @tab-click="getKeywords">
          <div style="height: 50vh">
            <div style="height: 100%;margin: 10px">
              <el-tag v-for="(etags,index) in keywordsTag"
                      :key="index"
                      size="medium"
                      style="margin: 5px">
                {{ etags }}
              </el-tag>
            </div>
          </div>
        </el-tab-pane>
        <el-tab-pane label="关键字提取" @tab-click="getKeysentences">
          <div style="height: 50vh">
            <div style="height: 100%;margin: 10px">
              <el-tag v-for="(etags,index) in keysentences"
                      :key="index"
                      size="medium"
                      style="margin: 5px">
                {{ etags }}
              </el-tag>
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>


  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      formData: {},
      fileContent: '',
      //实体类别
      stType: [],
      //词性类别
      cxType: [],
      allTagColor: [{
        type: "n",
        tag: "普通名词",
        color: "#488fce"
      }, {
        type: "f",
        tag: "方位名词",
        color: "#c9aaca"
      }, {
        type: "s",
        tag: "处所名词",
        color: "#c582c3"
      }, {
        type: "nw",
        tag: "作品名",
        color: "#54d0eb"
      }, {
        type: "nz",
        tag: "其他专名",
        color: "#8bc3f5"
      }, {
        type: "v",
        tag: "普通动词",
        color: "#ffcb99"
      }, {
        type: "vd",
        tag: "动副词",
        color: "#ddf22c"
      }, {
        type: "vn",
        tag: "名动词",
        color: "#e2e371"
      }, {
        type: "a",
        tag: "形容词",
        color: "#67ccaa"
      }, {
        type: "ad",
        tag: "副形词",
        color: "#e8278b"
      }, {
        type: "an",
        tag: "名形词",
        color: "#b2b84c"
      }, {
        type: "d",
        tag: "副词",
        color: "#ffcccb"
      }, {
        type: "m",
        tag: "数量词",
        color: "#fcaeaf"
      }, {
        type: "q",
        tag: "量词",
        color: "#ff9899"
      }, {
        type: "r",
        tag: "代词",
        color: "#9acccd"
      }, {
        type: "p",
        tag: "介词",
        color: "#99cc67"
      }, {
        type: "c",
        tag: "连词",
        color: "#8ea4de"
      }, {
        type: "u",
        tag: "助词",
        color: "#4dd9e6"
      }, {
        type: "xc",
        tag: "其他虚词",
        color: "#96dbf8"
      }, {
        type: "w",
        tag: "标点符号",
        color: "#d1ccd0"
      }, {
        type: "PER",
        tag: "人名",
        color: "#67ccaa"
      }, {
        type: "LOC",
        tag: "地名",
        color: "#ff9899"
      }, {
        type: "ORG",
        tag: "机构名",
        color: "#c7aee7"
      }, {
        type: "TIME",
        tag: "时间",
        color: "#4dd9e6"
      }],
      entityTag: [],
      cxTag: [],
      keywordsTag: [],
      keysentences: []

    }
  },
  methods: {
    async $init() {
      this.stType = this.allTagColor.slice(20, 24)
      this.cxType = this.allTagColor.slice(0, 19)
      this.getFileContent()
      const {data} = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.formDataId
      })
      if (data.success) {
        this.formData = data.data
      }
    },
    async getFileContent() {
      const {data} = await this.$http.post('/search/getFileContent', {
        refId: this.$route.query.formDataId
      })
      if (data.success) {
        this.fileContent = data.data
        this.getWord()
        this.getKeywords()
        this.getKeysentences()
      }
    },
    //获取 实体识别和词性分析 数据
    getWord() {
      this.$http.post('/tag/getWord', {
        text: this.getDeal_str(this.fileContent)
      }).then(({data}) => {
        if (data && data.success) {
          this.entityTag = []
          this.cxTag = []
          for (let i = 0; i < data.data.length; i++) {
            if (data.data[i].type == 'PER' || data.data[i].type == 'LOC' || data.data[i].type == 'ORG' || data.data[i].type == 'TIME') {
              this.entityTag.push(data.data[i])
            } else {
              this.cxTag.push(data.data[i])
            }
          }
        } else {
          this.$message.error(data.message)
        }
      })
    },
    //获取 关键词提取 数据
    getKeywords() {
      this.$http.post('/tag/getKeywords', {
        text: this.getDeal_str(this.fileContent)
      }).then(({data}) => {
        if (data && data.success) {
          this.keywordsTag = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    //获取 关键字提取 数据
    getKeysentences() {
      this.$http.post('/tag/getKeysentences', {
        text: this.getDeal_str(this.fileContent)
      }).then(({data}) => {
        if (data && data.success) {
          this.keysentences = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    getColor(c, type) {
      if (c === 'st') {
        for (let i = 0; i < this.stType.length; i++) {
          if (this.stType[i].type === type) {
            return this.stType[i].color
          }
        }
      } else {
        for (let i = 0; i < this.cxType.length; i++) {
          if (this.cxType[i].type === type) {
            return this.cxType[i].color
          }
        }
      }
    },
    //去除空格 和 换行
    getDeal_str(str) {
      let deal_str = str.replace(/\n/g, '')
      deal_str = str.replace(new RegExp(' ', 'gm'), '')
      return deal_str
    }
  }
}
</script>

<style scoped>

</style>