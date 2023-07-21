<template>
  <section class="fond_type">
    <nac-info/>
    <div class="index_main">
      <el-row style="height: 100%" :gutter="20">
        <el-col :span="4" style="height: 100%">
          <el-card style="height: 100%">
            <el-input
                placeholder="输入关键字进行过滤"
                v-model="filterText" clearable>
            </el-input>
            <el-tree :current-node-key="managementTypeId" :data="managementTypes" :filter-node-method="filterNode"
                     @node-click="handleNodeClick" label="name"
                     node-key="id"
                     ref="tree"
                     class="fond_material_tree"
                     default-expand-all>
              <template #default="scope">
                <div class="custom-node">
                  <span>{{ scope.node.label }}</span>
                </div>
              </template>
            </el-tree>
          </el-card>
        </el-col>
        <el-col :span="20" style="height: 100%">
          <el-card style="height: 100%">
            <table-index @editShow="toEdit" :showTitle="false" :fields="fields" path="dossierType"
                         :default-search-form="defaultSearchForm" ref="table"/>
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
      dragging: null,//正在拖拽的 元素
      //全宗卷大类
      managementTypes: [],
      //
      fields: [
        {prop: 'name', label: '卷内材料类型', search: true, required: true},
        {prop: 'code', label: '类型编号', width: '100', search: true, required: true},
        {prop: 'saveTerm', label: '保管期限', width: '100', required: true},
        {
          prop: 'createDate',
          label: '添加时间',
          dateFormat: 'YYYY-MM-DD HH:mm:ss',
          width: 140,
          edit: false
        }
      ],
      managementTypeId: '',
      filterText: '',
    }
  },
  methods: {
    toEdit(form) {
      form.businessId = this.managementTypeId
    },
    async defaultSearchForm() {
      if (!this.managementTypeId) {
        await this.getManagementType()
      }
      return {businessId: this.managementTypeId}
    },
    handleDragOver(e) {
      e.dataTransfer.dropEffect = 'move'// e.dataTransfer.dropEffect="move";//在dragenter中针对放置目标来设置!
    },
    handleNodeClick(v) {
      this.managementTypeId = v.id
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(v.id)
      })
      this.$refs.table.reload()
    },
    //获取全宗卷大类
    async getManagementType() {
      const {data} = await this.$http.post('/managementType/getManagementTypeTree')
      if (data && data.success) {
        this.managementTypes = data.data
        if (this.managementTypes.length > 0) {
          this.managementTypeId = this.managementTypes[0].id
          this.$nextTick(() => {
            this.$refs.tree.setCurrentKey(this.managementTypes[0].id)
          })
        }
      }
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


  .fond_material_tree {
    .is-current {
      .el-tree-node__content {
        color: $--color-white;
        background-color: $--color-primary-dark-1;
      }
    }

    .node {
      font-size: 14px !important;
    }
  }
}
</style>