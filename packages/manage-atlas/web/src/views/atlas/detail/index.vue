<template>
  <section>
    <nac-info back>
    </nac-info>
    <div class="box">
      <div class="left">
        <el-tabs v-model="showtype" @tab-click="changeTab">
          <el-tab-pane label="分页" name='page'>
            <div class="top">
              <div :key="index" class="img_box" v-for="(i,index) in archives"
                   @click="seeArchiveDetail(i.formDataId,i.formDefinitionId)">
                <div style="background-color: rgba(211,211,211,0.05);height: 140px">
                  <el-image v-if="i.imgFileid"
                            :src="getSrc(i)"
                            fit="cover"
                            style="height: 140px">
                  </el-image>
                  <el-image v-else>
                    <div slot="error" class="image-slot" style="margin-top: 30px;font-size: 50px">
                      <i class="el-icon-picture-outline">
                      </i>
                    </div>
                  </el-image>
                </div>
                <div class="msg">
                  <span>标题: {{ i.title }}</span>
                </div>
                <div class="msg"><span>档号: {{ i.archiveCode }}</span></div>
              </div>
            </div>
            <el-pagination
                @current-change="index=>getImgArchives(index)"
                :current-page.sync="page.index"
                :page-size="page.size"
                layout="total, prev, pager, next"
                :total="page.total">
            </el-pagination>
          </el-tab-pane>
          <el-tab-pane label="翻页" name='turn'>
            <div class="manual-wrap active">
              <turn
                  :width="turnwidth"
                  :height="turnheight"
                  ref="turn"
                  @change="changeCurrent"
                  @tap="turnTap"
                  :data="recentlsit"
              ></turn>
            </div>
            <div style="text-align: center;margin-top: 5px">
              <el-button type="primary" @click="pageUp">上一页</el-button>
              <el-button type="primary" @click="pageDown">下一页</el-button>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
      <div class="right">
        <h4 class="h">基本信息</h4>
        <el-form :model="atlas" style="padding: 20px 20px 20px 10px">
          <el-form-item label="图册名称">
            <el-input v-model="atlas.atlasName"></el-input>
          </el-form-item>
          <el-form-item label="图册类型">
            <el-input v-model="atlas.atlasType"></el-input>
          </el-form-item>
          <el-form-item label="主题">
            <el-input v-model="atlas.atlasTitle"></el-input>
          </el-form-item>
          <el-form-item label="关键词">
            <el-select multiple filterable clearable placeholder="选择关键词" style="width: 100%" v-model="values">
              <el-option
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id"
                  v-for="item in keywords">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="图册描述">
            <el-input type="textarea" v-model="atlas.atlasMark"></el-input>
          </el-form-item>
          <div>
            <el-button @click="save" type="primary">保存</el-button>
          </div>
        </el-form>
      </div>
    </div>
  </section>
</template>

<script>
import FileViewContent from "@archive/core/src/components/fileView/content"
import indexMixin from '@/util/indexMixin'
import turn from "vue-flip-page"

export default {
  name: "index",
  components: {FileViewContent, turn},
  mixins: [indexMixin],
  data() {
    return {
      width: '80px',
      atlas: {},
      keywords: [],
      values: [],
      img: {},
      visible: false,
      imgScans: [],
      archives: [],
      imgType: ['PNG', 'JPG', 'JPEG', 'TIF'],
      turnwidth: 375,
      turnheight: 667,
      recentlsit: [],
      currentIndex: 0,
      showtype: 'page'
    }
  },
  /*mounted() {
    this.onTurn();
  },*/
  methods: {
    $init() {
      this.getTag()
      /*this.imgs.forEach(i => {
        this.imgScans.push(i.src)
      })*/
      this.$post('atlas/detail', {id: this.$route.query.id}).then(({data}) => {
        if (data && data.success) {
          this.atlas = data.data
          this.values = this.atlas.atlasKeyWord.split(',')
        }
      })
      this.getImgArchives(1)

    },
    changeCurrent(index) {
      this.currentIndex = index
    },
    turnTap() {

    },
    pageUp() {
      if (this.currentIndex == 0) {
        this.$message.info("当前为第一页!")
      } else {

        this.$refs["turn"].toPage(this.currentIndex - 1)
      }
    },
    pageDown() {
      if (this.currentIndex == this.recentlsit.length - 1) {
        this.$message.info("当前为最后一页!")
      } else {
        this.$refs["turn"].toPage(this.currentIndex + 1)
      }
    },
    getImgArchives(index) {
      const params = Object.assign({atlasId: this.$route.query.id}, this.page, {
        pageIndex: index - 1,
        pageSize: this.page.size
      })
      //根据关键词查档案
      this.$post('/atlas/getImgArchives', params).then(({data}) => {
        if (data && data.success) {
          this.archives = data.data.data
          this.page.index = data.data.start / data.data.size + 1
          this.page.size = data.data.size
          this.page.total = data.data.total
        } else {
          this.$message.error(data.message)
        }
      })
    },
    getSrc(row) {
      return '/api/fileView/getFile?fileId=' + row.imgFileid + '&filePath=' + row.imgFilePath
    },
    getaaa(row) {
      return 'background:url(' + this.getSrc(row) + ') no-repeat 100% 100%'
    },
    getTag() {
      this.$http.post('/tagLib/selectTagList').then(({data}) => {
        if (data.success) {
          this.keywords = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    seeArchiveDetail(formDataId, formDefinitionId) {
      this.$router.push({
        path: '/archive/metadataAndFileDetail',
        query: {
          formDataId: formDataId,
          formDefinitionId: formDefinitionId,
          refType: 'archive',
          groupCode: 'default',
          watermark: false
        }
      })
    },
    changeTab() {
      this.recentlsit = []
      for (let i = 0; i < this.archives.length; i++) {
        this.recentlsit = this.recentlsit.concat({
          'picture_image': '/api/fileView/getFile?fileId=' + this.archives[i].imgFileid + '&filePath=' + this.archives[i].imgFilePath
        })
        /*this.recentlsit = this.recentlsit.concat('/api/fileView/getFile?fileId=' + this.archives[i].imgFileid + '&filePath=' + this.archives[i].imgFilePath
        )*/
      }
    },
    save() {
      this.atlas.atlasKeyWord = this.values.toString()
      this.$post('/atlas/update', this.atlas).then(({data}) => {
        if (data && data.success) {
          this.$message({type: 'success', message: '保存成功'})
          this.getImgArchives(1)
        }
      })
    }
  }
}
</script>

<style scoped>
.box {
  width: 100%;
  height: 100%;
  background-color: white;
  display: flex;
  overflow-y: hidden;
}

.left {
  width: 80%;
  height: 100%;
  display: block;
}

.top {
  width: 100%;
  padding: 10px 3px;
  overflow-y: scroll;
}

.right {
  width: 20%;
  height: 100%;
  text-align: center;
  border-left: 1px solid gainsboro;
  overflow-y: scroll;
}

.h {
  background-color: gainsboro;
  line-height: 40px;
}

.img_box {
  width: 210px;
  text-align: center;
  border: 1px solid lightgrey;
  margin-bottom: 10px;
  font-size: 13px;
}

.top {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-around;
}

.icon {
  cursor: pointer;
}

.msg {
  display: flex;
  padding: 2px 5px;
  font-size: 12px;
}

.manual-wrap {
  position: relative;
  z-index: 0;
  width: 100%;
  height: 100%;
  opacity: 0;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-pack: center;
  -ms-flex-pack: center;
  justify-content: center;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  -webkit-transform: scale(0.95);
  transform: scale(0.95);
  -webkit-transition: opacity ease 0.5s;
  transition: opacity ease 0.5s;
  -moz-user-select: none;
  -webkit-user-select: none;
  -ms-user-select: none;
  user-select: none;
  -o-user-select: none;
}

.manual-wrap.active {
  -webkit-transform: scale(1);
  transform: scale(1);
  opacity: 1;
  /*background-color: #f3f1f1;*/
}

.flipbook {
  width: 90vw;
  height: 90vh;
}
</style>