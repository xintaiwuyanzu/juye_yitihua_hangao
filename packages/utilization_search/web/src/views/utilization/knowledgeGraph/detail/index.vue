<template>
  <section>
    <nac-info>
      <span style="float: right">
        <select-async v-model="modeValue" :mapper="modeMapper" v-on:change="changeMode"></select-async>
        <el-button type="primary" @click="savegdata">保存</el-button>
        <el-button @click="$router.back()" type="primary">返回</el-button>
      </span>
    </nac-info>
    <el-card style="height: 85vh;overflow:scroll;flex-direction: column">

      <div id="container" style="height: 100%;">
      </div>

    </el-card>

  </section>
</template>

<script>
import G6 from '@antv/g6';

export default {
  name: "index",
  data() {
    return {
      myGraph: null,
      chartData: [],
      chartLink: [],
      form: {},
      data: {nodes: [], edges: []},  //关系图 节点,线 数据
      modeValue: 'default',
      modeMapper: {'default': '默认', 'addEdge': '加关系', 'removeEdge': '删除关联'},
      addedCount: 0,
      id: this.$route.query.id,
      //用于判断数据是否是根据关键词取出 根据关键词取出节点无坐标信息
      isgetkeyword: true
    }
  },
  mounted() {
  },
  methods: {
    $init() {
      this.$http.post('/knowledgeGraph/detail', {id: this.id}).then(({data}) => {
        if (data.success) {
          if (data.data.graphData === null || data.data.graphData === '') {
            let cData = []
            let cLink = []
            this.$http.post('/knowledgeGraph/getGraphDataByKw', {id: this.id}).then(({data}) => {
              if (data.success) {
                for (let i = 0; i < data.data.length; i++) {
                  if (cData.findIndex(item => item.id === data.data[i].id) == -1) {
                    cData = cData.concat({
                      id: data.data[i].id, name: data.data[i].tagName, category: '标签',
                      color: '#2e6a9b', label: data.data[i].tagName, style: {fill: '#2e6a9b'}
                    })
                  }
                  if (cData.findIndex(item => item.id === data.data[i].formDataId) == -1) {
                    cData = cData.concat({
                      id: data.data[i].formDataId,
                      name: data.data[i].archiveCode,
                      category: '档案',
                      title: data.data[i].title,
                      formDefinitionId: data.data[i].formDefinitionId,
                      color: '#cec344',
                      label: data.data[i].archiveCode, style: {fill: '#cec344'}
                    })
                  }
                  cLink = cLink.concat({value: "", source: data.data[i].id, target: data.data[i].formDataId, id: i})
                }
                this.chartData = cData
                this.chartLink = cLink
                this.isgetkeyword = true
                this.getArchiveCarNode()
                this.data = {nodes: this.chartData, edges: this.chartLink}
                setTimeout(() => {
                  this.initGraph()
                }, 100)
              } else {
              }
            })
          } else {
            this.data = JSON.parse(data.data.graphData)
            this.isgetkeyword = false
            this.getArchiveCarNode()
            setTimeout(() => {
              this.initGraph()
            }, 100)
          }
        } else {
        }
      })
    },
    getArchiveCarNode() {
      this.$http.post('/archiveCarDetail/page', {batchId: this.id}).then(({data}) => {
        if (data.success) {
          for (let i = 0; i < data.data.data.length; i++) {
            this.chartData = this.chartData.concat({
              id: data.data.data[i].formDataId,
              name: data.data.data[i].archiveCode,
              category: '档案',
              title: data.data.data[i].title,
              formDefinitionId: data.data.data[i].formDefinitionId,
              color: '#cec344',
              label: data.data.data[i].archiveCode, style: {fill: '#cec344'}
            })
          }
          this.data.nodes = this.chartData
        }
      })
    },
    initGraph() {
      let dom = document.getElementById("container");

      let tooltip = new G6.Tooltip({
        offsetX: 10,
        offsetY: 10,
        fixToNode: [1, 0.5],
        itemTypes: ['node', 'edge'],
        getContent: (e) => {
          const outDiv = document.createElement('div');
          outDiv.style.width = 'fit-content';
          outDiv.style.height = 'fit-content';
          const model = e.item.getModel();
          if (e.item.getType() === 'node') {
            if (model.category === '标签') {
              outDiv.innerHTML = `${model.category}<br/>${model.name}`;
            } else {
              outDiv.innerHTML = `${model.category}<br/>档号: ${model.name}<br/>题名: ${model.title}`;
            }

          } else {
            const source = e.item.getSource();
            const target = e.item.getTarget();
            outDiv.innerHTML = `来源：${source.getModel().name}<br/>去向：${target.getModel().name}`;
          }
          return outDiv;
        },
      });

      let width = dom.scrollWidth;
      this.myGraph = new G6.Graph({
        container: 'container',
        width,
        height: 700,
        plugins: [tooltip],
        modes: {
          default: ['drag-node', 'activate-relations'],
          addNode: ['click-add-node', 'click-select'],
          addEdge: ['click-add-edge', 'click-select'],
          removeEdge: ['click-remove-edge', 'click-select']
        },
        nodeStateStyles: {
          selected: {
            stroke: '#666',
            lineWidth: 2,
            fill: 'steelblue',
          },
          active: {
            //stroke: '#666',
            lineWidth: 2,
            fill: 'steelblue',
          }
        },
        defaultNode: {
          size: 50,
        },
        defaultEdge: {
          style: {
            endArrow: true, //线带箭头
          }
        }
      })

      this.myGraph.data(this.data)
      this.myGraph.render()


      if (this.isgetkeyword) {
        this.myGraph.updateLayout({
          type: 'force',
          linkDistance: 200,
          preventOverlap: true,
        });
      }


      let graph = this.myGraph
      let then = this

      let aa = this.addedCount
      //添加节点模式
      G6.registerBehavior('click-add-node', {
        getEvents() {
          return {
            'canvas:click': 'onClick',
          };
        },
        onClick(ev) {
          graph.addItem('node', {
            x: ev.canvasX,
            y: ev.canvasY,
            id: `node-${aa}`,
          });
          aa++;
        },
      });
      //加边 模式
      G6.registerBehavior('click-add-edge', {
        getEvents() {
          return {
            'node:click': 'onClick',
            mousemove: 'onMousemove',
            'edge:click': 'onEdgeClick',
          };
        },
        onClick(ev) {
          const node = ev.item;
          const point = {x: ev.x, y: ev.y};
          const model = node.getModel();
          if (self.addingEdge && self.edge) {
            graph.updateItem(self.edge, {
              target: model.id,
            });

            self.edge = null;
            self.addingEdge = false;
          } else {
            self.edge = graph.addItem('edge', {
              source: model.id,
              target: model.id,
            });
            self.addingEdge = true;
          }
        },
        onMousemove(ev) {
          const self = this;
          const point = {x: ev.x, y: ev.y};
          if (self.addingEdge && self.edge) {
            graph.updateItem(self.edge, {
              target: point,
            });
          }
        },
        onEdgeClick(ev) {
          const self = this;
          const currentEdge = ev.item;
          if (self.addingEdge && self.edge === currentEdge) {
            graph.removeItem(self.edge);
            self.edge = null;
            self.addingEdge = false;
          }
        },
      });
      //删除
      G6.registerBehavior('click-remove-edge', {
        getEvents() {
          return {
            'edge:click': 'onEdgeClick',
          };
        },
        onEdgeClick(ev) {
          const self = this;
          const currentEdge = ev.item;
          graph.removeItem(currentEdge);
          self.edge = null;
        },
      });

    },
    changeMode(v) {
      this.myGraph.setMode(v)
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
    savegdata() {
      let graphData = JSON.stringify(this.myGraph.save())
      this.$http.post('/knowledgeGraph/updategraphData', {id: this.id, graphData: graphData}).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功！')
            } else {
              this.$message.error(data.message)
            }
            this.addForm = {}
          })
    }
  }
}
</script>

<style scoped>

</style>