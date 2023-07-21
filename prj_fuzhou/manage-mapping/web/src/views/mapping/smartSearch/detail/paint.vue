<template>
  <section class="paint-container" v-loading="loading">
    <div class="canvas-wrapper" ref="g6Wrapper"/>
    <select-async :mapper="dataMapper" v-model="pageSize" style="width: 120px;position: static;"/>
  </section>
</template>

<script>
import G6 from '@antv/g6';
import {paintConfig} from './paintConfig'

/**
 * 图谱显示组件
 *
 * 1、根据name动态加载图谱数据
 * 2、分页功能
 */
export default {
  name: "paint",
  props: {
    name: {type: String}
  },
  data() {
    return {
      loading: false,
      //是否有数据
      hasData: false,
      //总共多少数据
      total: 0,
      //当分页大小
      pageSize: 25,
      dataMapper: {25: 25, 50: 50, 100: 100, 200: 200, 300: 300, 500: 500},
      _graph: null,
      _menu: null
    }
  },
  methods: {
    //初始化容器组件
    initDoms() {
      //初始化菜单
      // 实例化 Menu 插件
      this._menu = new G6.Menu({
        itemType: ['node', 'edge', 'combo'],
        getContent() {
          return `<section class="context-menu">
                    <!--四个按钮-->
                    <div class="it" data-id="hide">隐藏该元素</div>
                    <div class="it"  data-id="expand">显示已隐藏点/边</div>
                    <div class="it"  data-id="show">只显示该节点数据</div>
                    <div class="it" data-id="show">检索</div>
                  </section>`
        },
        handleMenuClick: this.menuClick
      });
      //初始化画板
      this._graph = new G6.Graph({
        container: this.$refs.g6Wrapper,
        plugins: [this._menu],
        ...paintConfig
      });
      //绑定监听事件
      if (this.name) {
        this.loadData()
      }
    },
    //加载数据
    async loadData() {
      this.loading = true
      const {data} = await this.$post("neo4j/getRelationsByGraph", {name: this.name, pageSize: this.pageSize})
      if (data && data.success) {
        const nodes = []
        const edges = []
        data.data.forEach(({start, end, relation}) => {
          nodes.push({
            id: start.id,
            label: start.name,
            style: {fill: '#3af194'},//节点边框样式
            labelCfg: {style: {fill: '#000', lineWidth: 800, fontSize: 13}},//文本填充样式
          })
          nodes.push({
            id: end.id,
            label: end.name,
            style: {fill: '#3af194'},//节点边框样式
            labelCfg: {style: {fill: '#000', lineWidth: 800, fontSize: 13}},//文本填充样式
          })
          edges.push({source: start.id, target: end.id, label: relation})
        })
        this.hasData = true
        this._graph.changeData({edges, nodes})
      } else {
        this.clearData()
      }
      this.loading = false
    }
    ,
    menuClick(target, item, graph) {
      const opt = target.dataset.id
      const content = item._cfg.model.label
      switch (opt) {
        case 'hide':
          graph.hideItem(item);
          break
        case 'expand':
          let arr = []
          graph.getEdges().forEach(i => {
            const id = item._cfg.id
            if (id !== i._cfg.source._cfg.id && id !== i._cfg.target._cfg.id) {
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
    },
    //参数为空的时候，需要清空页面
    clearData() {
      this.total = 0
      this.hasData = false
    },
    //销毁数据和容器
    destroyDoms() {
      if (this._graph) {
        this._graph.clear()
        this._graph.destroy()
        this._graph = null
      }
    }
  },
  mounted() {
    this.initDoms()
  },
  beforeDestroy() {
    this.destroyDoms()
  },
  watch: {
    name(v, o) {
      if (v && v !== o) {
        this.loadData()
      } else {
        this.clearData()
      }
    },
    pageSize() {
      this.loadData()
    }
  }
}
</script>
<style lang="scss">
.paint-container {
  height: 100%;
  display: flex;
  flex-direction: column;

  .canvas-wrapper {
    flex: 1
  }

  .context-menu {
    .it {
      cursor: pointer;
      line-height: 35px;
      text-align: center;
      background-color: white;
      padding: 0 15px;
      color: $--color-primary;
      font-weight: bold;
    }
  }
}
</style>