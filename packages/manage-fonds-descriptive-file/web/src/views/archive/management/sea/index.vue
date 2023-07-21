<template>
  <section class="fond_type">
    <nac-info/>
    <div class="index_main">
      <el-row style="height: 100%" :gutter="20">
        <el-col :span="5" style="height: 100%">
          <el-card shadow="hover" style="height: 100%;overflow-y: scroll;">
            <el-input
                placeholder="输入关键字进行过滤"
                v-model="filterText" clearable>
            </el-input>
            <el-tree :current-node-key="fondId" :data="fonds" :filter-node-method="filterNode"
                     @node-click="selectFond" class="fond_material_tree"
                     node-key="id" ref="tree"
                     default-expand-all>
              <template #default="scope">
                <div class="custom-node">
                  <span>{{ scope.node.label }}</span>
                </div>
              </template>
            </el-tree>
          </el-card>
        </el-col>
        <el-col :span="19" style="height: 100%">
          <el-card shadow="hover" style="height:100%">
            <table-render class="table-container"
                          :columns="columns"
                          :data="data">
              <el-table-column label="操作" width="120" header-align="center" align="center">
                <template v-slot="scope">
                  <el-link type="success"
                           @click="$router.push({path: '/archive/management/sea/item', query: {fondId:fondId}})">详情
                  </el-link>
                </template>
              </el-table-column>
            </table-render>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return {
      //所有全宗
      fonds: [],
      fondId: '',
      //
      columns: [
        {prop: 'name', label: '编写年度'},
        {prop: 'count', label: '数量'}
      ],
      data: [],
      filterText: '',
    }
  },
  methods: {
    async $init() {
      await this.getFonds()
      await this.loadData(this.fondId)
    },
    async loadData(fondId) {
      const {data} = await this.$post('/management/getFondsCountByYear', {fondId: fondId})
      this.data = data.data
    },

    //获取全宗列表，默认取第一个全宗
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      this.fonds = data.data
      this.fondId = this.fonds[0].id
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(this.fonds[0].id)
      })
    },
    selectFond(v) {
      this.fondId = v.id
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(v.id)
      })
      this.loadData(v.id)
    },
    keyUp(e) {
      e.target.value = e.target.value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '');
    },
    //过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  },
}
</script>
<style lang="scss">
.fond_type {

  .el-card__body {
    height: 100%;
    padding: 0;
  }

  .table_index {
    height: 100%;
    padding: 0;
  }

  .table-wrapper {
    //height: 100%;
    padding: 0;
  }

  .table_index .table-wrapper {
    //height: 92%;
    padding: 0 15px;
  }

  .breadcrumb-container {
    margin-bottom: 0;
  }

  .fond_material_tree {
    .el-tree-node__content {
      padding: 4px;
    }

    .el-tree-node__content:hover {
      background-color: #eee;
    }

    .is-current {
      .el-tree-node__content {
        color: $--color-white;
        background-color: $--color-primary-dark-1;
        font-weight: bold;
      }
    }
  }
}
</style>
