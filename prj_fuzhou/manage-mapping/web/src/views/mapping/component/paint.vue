<template>
  <div>
    <div id="mountNode" ref="mountNode"
         style="width: 100%;height: 800px;overflow: hidden;background-color: white" v-loading="loading"></div>
    <div v-show="false">
      <div id="node-menus">
        <div class="it" id="hide">隐藏该元素</div>
        <div class="it" id="show">显示已隐藏点/边</div>
        <div class="it" id="expand">只显示该节点数据</div>
        <div class="it" id="toSearch">检索</div>
      </div>
    </div>
    <div class="colorTip" v-show="visible">
      <div :key="index" style="display:flex;margin: 4px 0" v-for="(key,index) in colorMap.keys()">
        <div
            :style="{backgroundColor: colorMap.get(key),width:'30px',height:'15px',borderRadius:'5px',lineHeight:'14px',marginRight:'3px'}"></div>
        <div style="margin-top: 2px">{{ key }}</div>
      </div>
    </div>
    <div class="message" style="top: 200px" v-if="$route.query.name===undefined">
      <el-input v-model="pageSize">
        <el-button @click="judge" icon="el-icon-search" slot="append"></el-button>
      </el-input>
      <el-alert
          title="页面最多展示300条数据"
          type="warning"
          v-if="pageSize>=300">
      </el-alert>
    </div>
    <div class="message" v-show="visible">
      <el-card style="background-color: rgba(250, 250, 250, 0.8);">
        <div>
          <i :class="show?'el-icon-caret-top':'el-icon-caret-bottom'" @click="show=!show"
             style="float: right;margin-bottom: 5px;color: #0164AC;cursor: pointer"></i>
        </div>
        <el-collapse-transition>
          <div v-show="show">
            <h3 style="margin: 10px 0">基本信息</h3>
            <el-form :model="formData" label-width="70px">
              <el-form-item label="ID：">
                <el-input v-model="formData.id"/>
              </el-form-item>
              <el-form-item label="名称：">
                <el-input v-model="formData.NAME"/>
              </el-form-item>
              <el-form-item label="外文名：">
                <el-input v-model="formData.FOREIGN_NAME"/>
              </el-form-item>
              <el-form-item label="别名：">
                <el-input v-model="formData.ALIAS"/>
              </el-form-item>
              <el-form-item label="描述：">
                <el-input v-model="formData.REMARKS"/>
              </el-form-item>
            </el-form>
          </div>
        </el-collapse-transition>
      </el-card>
    </div>
  </div>
</template>
<script>
import G6 from '@antv/g6';

export default {
  name: "paint",
  props: {
    //查询
    p_search: [Object, Array, String],
    p_relationName: String,
    p_i: Number,
    p_classType: String
  },
  data() {
    return {
      pageSize: 25,
      graph: null,
      data: {
        nodes: [],
        edges: []
      },
      hiddenItemIds: [],
      colorMap: new Map(),
      visible: false,
      formData: {},
      search: this.p_search,
      i: Number(this.p_i),
      show: false,

      loading: true
    }
  },
  methods: {
    $init() {
      this.judge()
    },
    judge() {
      this.data = {nodes: [], edges: []}
      if (this.pageSize === 0 || this.pageSize === '') this.pageSize = 25
      if (this.i === 0) {
        this.setColor()
      } else {
        this.getAtlasDataByGraph()
      }
    },
    //从图数据库查，不区分对象类型
    async getAtlasDataByGraph() {
      let vo = {
        name: this.name,
        relationName: this.$route.query.relationName,
        classType: this.p_classType,
        type: 0,
        pageSize: this.pageSize
      }
      const {data} = await this.$post("neo4j/getRelationsByGraph", vo)
      if (data && data.success) {
        let map = new Map()
        data.data.forEach(i => {
          this.data.edges.push({source: i.start.id, target: i.end.id, label: i.relation})
          map.set(i.start.id, i.start.name)
          map.set(i.end.id, i.end.name)
        })
        this.data.nodes = []
        for (let key of map.keys()) {
          //放入节点
          this.data.nodes.push({
            id: key,
            label: map.get(key),
            style: {fill: '#3af194'},//节点边框样式
            labelCfg: {style: {fill: '#000', lineWidth: 800, fontSize: 13}},//文本填充样式
          })
        }
        data.data.forEach(i => {
          this.data.edges.forEach(e => {
            if (e.source !== i.start.id && e.target !== i.end.id) {
              if (e.source === i.end.id && e.target === i.start.id) {
                e.type = 'quadratic'  //避免线的文本重叠
              }
            }
          })
        })
        this.init()
      }
    },
    //从mysql查，适用于关系总览
    async getAtlasDataByMysql() {
      const d = this.search
      d.status = this.i
      d.order = this.pageSize
      const {data} = await this.$post("tripletData/getAll", d)
      if (data && data.success) {
        this.data.edges = []
        let map = new Map()
        data.data.data.forEach(i => {
          //放入线
          this.data.edges.push({source: i.sourceId, target: i.targetId, label: i.relationName})
          if (i.sourceName !== '' && i.targetName !== '' && i.sourceName !== i.targetName) {
            map.set(i.sourceId, [i.sourceFormId, i.sourceFormName])//sourceFormName
            map.set(i.targetId, [i.targetFormId, i.targetFormName])//
          }
        })
        this.data.nodes = []
        for (let key of map.keys()) {
          //放入节点
          this.data.nodes.push({
            id: key,
            label: map.get(key)[1],
            name: map.get(key)[0],
            style: {fill: '#3af194', stroke: '#3af194'},//节点边框样式  this.colorMap.get(map.get(key)[1])
            labelCfg: {style: {fill: '#000', lineWidth: 800, fontSize: 13}},//文本填充样式
          })
        }
        data.data.data.forEach(i => {
          this.data.edges.forEach(e => {
            if (e.source !== i.sourceId && e.target !== i.targetId) {
              if (e.source === i.targetId && e.target === i.sourceId) {
                e.type = 'quadratic'  //避免线的文本重叠
              }
            }
          })
        })
        this.init()
      }
    },

    //根据对象类型设置颜色
    async setColor() {
      const {data} = await this.$post('relation/getForms', {id: ''})
      //图的颜色
      const color = ['#08c542', '#ED7C30', '#58ff70', '#FF8096', '#bea9b9', '#7eafc4', '#4472C5']
      if (data && data.success) {
        data.data.forEach((i, index) => {
          this.colorMap.set(i.formName, color[index])
        })
        if (this.colorMap.size > 0) {
          this.$forceUpdate()
        }
        this.getAtlasDataByMysql()
      }
    },

    init() {
      //1.判断this.graph是否存在，若存在就销毁
      if (this.graph) {
        this.graph.clear();
        this.graph.destroy();
      }
      const that = this
      // 实例化 Menu 插件
      const menu = new G6.Menu({
        itemType: ['node', 'edge', 'combo'],
        getContent() {
          return document.getElementById('node-menus')
        },
        handleMenuClick(target, item, graph) {
          const opt = target.id
          const content = item._cfg.model.label
          switch (opt) {
            case 'hide':
              that.hiddenItemIds.push(item)
              graph.hideItem(item);
              break
            case 'expand':
              let arr = []
              graph.getEdges().forEach(i => {
                const id = item._cfg.id
                if (id !== i._cfg.source._cfg.id && id !== i._cfg.target._cfg.id) {
                  that.hiddenItemIds.push(i)
                  graph.hideItem(i);
                } else {
                  arr.push(i._cfg.target._cfg.id, i._cfg.source._cfg.id)
                }
              })
              graph.getNodes().forEach(i => {
                if (arr.indexOf(i._cfg.id) === -1) {
                  graph.hideItem(i)
                }
              })
              break;
            case 'show':
              graph.getNodes().forEach((node) => {
                if (!node.isVisible()) graph.showItem(node);
              });
              graph.getEdges().forEach((edge) => {
                if (!edge.isVisible()) graph.showItem(edge);
              });
              that.hiddenItemIds = [];
              break
            case 'toSearch':
              that.$router.push({
                path: '/mapping/smartSearch/search',
                query: {
                  content,
                  classType: 'person'
                }
              })
              break
            default:
              break;
          }
        }
      });
      // 创建 G6 图实例
      this.graph = new G6.Graph({
        container: 'mountNode', // 指定图画布的容器 id
        fitCenter: true,
        //线
        defaultEdge: {
          style: {
            stroke: '#0164AC',
            endArrow: true,//末尾箭头
          },
        },
        //节点
        defaultNode: {
          size: 50,
          style: {
            cursor: 'pointer',
          }
        },
        //节点配置
        modes: {
          default: [
            "drag-canvas",//允许拖拽画布
            {
              type: "zoom-canvas",//放缩画布
              enableOptimize: true //开启性能优化
            },
            'drag-node',//拖拽节点
            // 'activate-relations',//高亮
            'click-select',//点击选中节点
          ]
        },
        //插件
        plugins: [menu], // 配置 Menu 插件
        //节点布局
        //布局
        layout: {
          // Object，可选，布局的方法及其配置项，默认为 random 布局。
          type: 'gForce', // 指定为力导向布局
          preventOverlap: true, // 防止节点重叠
          linkDistance: 150, // 指定边距离为100
          gpuEnabled: true          // 可选，开启 GPU 并行计算，G6 4.0 支持
        },
      });
      //事件
      this.graph.on('node:click', (ev) => {
        const shape = ev.target;
        const node = ev.item;
        // that.selectOne(node._cfg.id, node._cfg.model.name)
      });
      // 读取数据
      this.graph.data(this.data);
      // 渲染图
      this.graph.render();
      //显示颜色提示框
      // this.visible = true
      this.loading = false
    },
    //查询当前选中节点
    async selectOne(formDataId, formId) {
      const {data} = await this.$post('search/getDataByFormId', {formDataId, formId})
      if (data && data.success) {
        this.formData = data.data
      }
    }
  },
  computed: {
    name() {
      return this.$route.query.name
    }
  }
}
</script>

<style scoped>
.it {
  cursor: pointer;
  line-height: 35px;
  text-align: center;
  background-color: white;
  padding: 0 15px;
  color: #0164AC;
  font-weight: bold;
}

.it:hover {
  color: black;
  background-color: rgb(245, 245, 245);
}

.colorTip {
  padding: 5px 20px;
  position: absolute;
  background-color: rgba(250, 250, 250, 0.8);
  right: 2vw;
  left: unset;
  top: unset;
  bottom: 20px;
  font-size: 12px;
}

.message {
  position: absolute;
  left: unset;
  right: 2vw;
  top: 240px;
  text-align: center;
}

/deep/ .g6-component-contextmenu {
  padding: 0;
}
</style>