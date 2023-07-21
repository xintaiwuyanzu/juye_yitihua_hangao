<template>
  <el-card :loading="loading">
    <el-tabs @tab-click="changeTab" v-model="showPane">
      <el-tab-pane label="标签" name='tag'>
      </el-tab-pane>
      <el-tab-pane label="专题标签" name='specialTag'>
      </el-tab-pane>
      <el-tab-pane label="知识图谱" name='know'>
      </el-tab-pane>
      <div style="height: 300px">
        <div v-if="showPane === 'tag'">
          <el-input
              class="input-new-tag"
              v-if="inputVisible"
              v-model="inputValue"
              ref="saveTagInput"
              size="small"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
          >
          </el-input>
          <el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添加标签</el-button>
          <el-button type="primary" size="small" @click="autotag">入库自动加标签</el-button>
          <el-tag
              :key="tag.id"
              v-for="tag in dynamicTags"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)">
            {{ tag.tagName }}
          </el-tag>
        </div>
        <div v-if="showPane === 'specialTag'">
          <el-button type="primary" size="small" @click="addSpecialTag">添加专题标签</el-button>
          <el-tag
              :key="tag.specialTagId"
              v-for="tag in specialTags"
              :disable-transitions="false"
          >
            {{ tag.specialTagName }}
          </el-tag>
        </div>
        <div v-if="showPane === 'know'" ref="tagCharts" class="chartsPane">
          <el-col :span="24">
            <div class="btns-konw">
              <div @click="handleFullScreen">
                <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
                  <i class="el-icon-rank"></i>
                </el-tooltip>
              </div>
              <el-button type="primary" size="mini" @click="reset" style="margin-left: 10px">重置</el-button>
            </div>

          </el-col>
          <el-col :span="24">
            <div ref="charts" v-bind:style="{height: changeHeight(280,200)+'px'}"
                 style="margin: auto;"></div>
          </el-col>
        </div>
      </div>
    </el-tabs>
    <el-dialog title="添加专题标签" :visible.sync="specialtagDialogVisible" width="500px" center :before-close="beforeClose">
      <el-form :form="specialTagForm" ref="deliveryContent" label-width="130px" :rules="rules">
        <el-row>
          <el-form-item label="专题标签:" prop="saveTarget">
            <el-select filterable v-model="specialTagForm.specialTagName" placeholder="请选择专题标签名称" clearable
                       style="width: 260px;">
              <el-option
                  v-for="item in allSpecialTags"
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>

        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
            <el-button @click="specialtagDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="saveSpecialTag">保存</el-button>
          </span>
    </el-dialog>
  </el-card>
</template>
<script>
import abstractArchiveDetail from "@archive/core/src/components/metadataFileView/abstractArchiveDetail";
import echarts from "echarts";

export default {
  name: "tagManage",
  extends: abstractArchiveDetail,
  data() {
    return {
      loading: false,
      dynamicTags: [],
      inputVisible: false,
      inputValue: '',
      myChart: null,
      chartData: [],
      chartLink: [],
      showPane: 'tag',
      fullscreen: false,
      specialTags: [],
      specialtagDialogVisible: false,
      specialTagForm: {},
      allSpecialTags: []
    };
  },
  watch: {
    formData() {
      this.loadData()
    },
    formDefinitionId() {
      this.loadData()
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    loadData() {
      if (this.formData && this.formData.id && this.formDefinitionId) {
        this.loading = true
        this.$http.post('/tagLibArchive/page',
            {
              page: false,
              formDefinitionId: this.formDefinitionId,
              formDataId: this.formData.id,
              status: '1'
            }).then(({data}) => {
          if (data.success) {
            this.dynamicTags = data.data
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      }
    },
    getEchartData(fdid, fid, tid) {
      let cData = []
      let cLink = []
      this.$http.post('/tagLibArchive/archiveKnowledgeGraph',
          {formDefinitionId: fdid, formDataId: fid, tagid: tid}).then(({data}) => {
        if (data.success) {
          for (let i = 0; i < data.data.length; i++) {
            if (cData.findIndex(item => item.id === data.data[i].id) == -1) {
              cData = cData.concat({id: data.data[i].id, name: data.data[i].tagName, category: '标签'})
            }
            if (cData.findIndex(item => item.id === data.data[i].formDataId) == -1) {
              cData = cData.concat({
                id: data.data[i].formDataId,
                name: data.data[i].archiveCode,
                category: '档案',
                title: data.data[i].title,
                formDefinitionId: data.data[i].formDefinitionId
              })
            }
            cLink = cLink.concat({value: "", source: data.data[i].id, target: data.data[i].formDataId})
          }
          this.chartData = cData
          this.chartLink = cLink
          this.initEchart()
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    //删除标签
    handleClose(tag) {
      this.$confirm('此操作将删除选中标签, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.loading = true
        this.$http.post('/tagLibArchive/delete', {Id: tag.id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success('删除成功！')
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
              } else {
                this.$message.error(data.message)
              }
            })
      })

    },
    changeTab(tab, event) {
      if (tab.$options.propsData.name === 'tag') {
        this.loadData()
      } else if (tab.$options.propsData.name === 'specialTag') {
        this.getSpecialTags()
      } else {
        this.getEchartData(this.formDefinitionId, this.formData.id, '')
      }
    },
    //知识图谱 重置
    reset() {
      this.getEchartData(this.formDefinitionId, this.formData.id, '')
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    //添加标签
    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue) {
        this.$http.post("tagLibArchive/addTag",
            {
              tagName: inputValue,
              ctype: "zdy",
              formDataId: this.formData.id,
              formDefinitionId: this.formDefinitionId
            }).then(
            ({data}) => {
              if (data.success) {
                this.$message.success('操作成功，等待通过')
                this.loadData()
              } else {
                this.$message.error(data.message)
              }
            }
        )
        this.inputVisible = false;
        this.inputValue = '';
      }

    },
    autotag() {
      this.$http.post("tagLibArchive/autoAddTag",
          {
            formDataId: this.formData.id,
            formDefinitionId: this.formDefinitionId
          }).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功！')
              this.loadData()
            } else {
              this.$message.error(data.message)
            }
          }
      )
    },
    getSpecialTags() {
      if (this.formData && this.formData.id && this.formDefinitionId) {
        this.loading = true
        this.$http.post('/specialTagArchive/page',
            {formDefinitionId: this.formDefinitionId, formDataId: this.formData.id, page: false}).then(({data}) => {
          if (data.success) {
            this.specialTags = data.data
          } else {
            this.$message.error(data.message)
          }
        })
      }
    },
    //点击 添加专题标签
    addSpecialTag() {
      this.getAllSpecialTags()
      this.specialtagDialogVisible = true

    },
    //获取全部叶节点的专题标签
    getAllSpecialTags() {
      this.$http.post('/specialTag/page', {
        leaf: 1,
        page: false
      }).then(({data}) => {
        if (data.success) {
          this.allSpecialTags = data.data
        }
      })
    },
    //
    saveSpecialTag() {
      if (this.specialTagForm.specialTagName) {
        this.$http.post("specialTagArchive/addSpecialTag",
            {
              tagId: this.specialTagForm.specialTagName,
              formDataId: this.formData.id,
              formDefinitionId: this.formDefinitionId
            }).then(
            ({data}) => {
              if (data.success) {
                this.$message.success('操作成功！')
                this.getSpecialTags()
              } else {
                this.$message.error(data.message)
              }
            }
        )
        this.specialtagDialogVisible = false;
        this.specialTagForm = {};
      }
    },
    initEchart() {
      this.myChart = echarts.init(this.$refs.charts);
      this.myChart.showLoading({
        text: '加载中，请稍候...'
      })
      let option = {
        tooltip: {
          position: ['10%', '5%'],
          formatter: (x) => {
            if (x.dataType === 'node') {
              if (x.data.category === '档案') {
                return x.data.category + '<br/>' + x.data.name + '<br/>' + x.data.title
              } else {
                return x.data.category + '<br/>' + x.data.name
              }
            } else {
              return this.getNameByid(x.data.source) + '>' + this.getNameByid(x.data.target)
            }

          }
        },
        series: [
          {
            force: {
              repulsion: 400
            },

            layout: 'circular',
            roam: true,
            lineStyle: {
              color: 'source',
              curveness: 0.3
            },
            //鼠标放上去有阴影效果
            emphasis: {
              focus: 'adjacency',
              lineStyle: {
                width: 5
              },
            },
            label: {
              show: true,
              position: 'right',
              formatter: '{b}'
            },
            edgeSymbol: ['none', 'arrow'], //连线箭头显示
            symbolSize: 20,
            type: 'graph',
            links: this.chartLink,
            data: this.chartData,
            categories: [{
              name: '标签',
              itemStyle: {
                color: '#2e6a9b'
              }
            },
              {
                name: '档案',
                itemStyle: {
                  color: '#cec344'
                }
              }]
          }
        ]
      };

      this.myChart.resize()

      this.myChart.setOption(option);
      this.myChart.hideLoading()
      //节点 点击时触发
      this.myChart.on('click', (param) => {
        if (param.dataType == 'node') {
          this.myChart.showLoading({
            text: '加载中，请稍候...'
          })
          if (param.data.category === '标签') {
            this.getEchartData('', '', param.data.id)
          } else {
            this.getEchartData(param.data.formDefinitionId, param.data.id, '')
          }

        }
      });
    },
    getNameByid(id) {
      let nodeName = ''
      for (let i = 0; i < this.chartData.length; i++) {
        if (this.chartData[i].id === id) {
          nodeName = this.chartData[i].name
        }
      }
      return nodeName
    },
    //全屏
    handleFullScreen() {
      let fullarea = this.$refs.tagCharts
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (fullarea.requestFullscreen) {
          fullarea.requestFullscreen();
        } else if (fullarea.webkitRequestFullScreen) {
          fullarea.webkitRequestFullScreen();
        } else if (fullarea.mozRequestFullScreen) {
          fullarea.mozRequestFullScreen();
        } else if (fullarea.msRequestFullscreen) {
          // IE11
          fullarea.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
    changeHeight(val1, val2) {
      let nowClientHeight = document.documentElement.clientHeight;
      if (this.fullscreen) { //全屏
        return val2 * (nowClientHeight / 936);
      } else {
        return val1 * (nowClientHeight / 936);
      }
    }
  }
}
</script>

<style lang="scss">
.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  margin-right: 10px;
  margin-bottom: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin: 5px;
  vertical-align: bottom;
}

.btns-konw {
  display: flex;
  align-items: center;
  float: right;
  margin: 10px;
}

.chartsPane {
  background-color: white;
}
</style>