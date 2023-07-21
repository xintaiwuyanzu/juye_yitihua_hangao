<template>
  <section class="fond_type">
    <nac-info back/>
    <div class="index_main">
      <el-row style="height: 100%" :gutter="20">
        <el-col :span="4" style="height: 100%">
          <el-card shadow="hover" style="width: 100%;overflow-y: scroll;height: 100%">
            <el-tree :current-node-key="managementTypeId" :data="managementTypes" :expand-on-click-node="false"
                     :filter-node-method="filterNode" @node-click="handleNodeClick"
                     label="name"
                     node-key="id"
                     class="fond_material_tree"
                     ref="tree"
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
          <el-card shadow="hover" style="height:100%;">
            <table-index :showTitle="false" :fields="fields" checkAble
                         path="management" :default-insert-form="defaultInsertForm" :insert="false" :edit="false"
                         :delete="false"
                         :default-search-form="defaultSearchForm" ref="table">
              <el-table-column prop="title" label="题名" align="center" show-overflow-tooltip>
                <template slot-scope="{row}">
                  <el-button type="text"
                             @click="$router.push({path: '/archive/management/edit', query: {managementTypeCode: managementTypeCode,id:row.id,optType:'detail'}})">
                    {{ row.title }}
                  </el-button>
                </template>
              </el-table-column>
              <template slot-scole='form' slot='search-$btns'>
                <el-button type="primary" @click="comparative">对比查阅</el-button>
              </template>
            </table-index>
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
      dict: ['archive.compilationStatus'],
      //全宗卷大类和分类树
      managementTypes: [],
      approveProcess: {},
      //
      fields: [
        {
          prop: 'fileTime',
          label: '文件时间',
          width: 140,
          search: true,
          required: true
        },
        {prop: 'vintagesStart', label: '起始年度', width: '70', required: false},
        {prop: 'vintagesEnd', label: '终止年度', width: '70', required: false},
        {prop: 'title', label: '题名', search: true, required: false},
        {prop: 'archive_code', label: '文件材料编号', width: '160', required: false},
        {prop: 'retentionPeriod', label: '保管期限', width: '70', required: false},
        {
          prop: 'status', label: '状态', mapper: {
            '0': {label: '待提交', show: 'success'},
            '1': {label: '审批中', show: 'danger'},
            '2': {label: '通过', show: 'danger'},
            '3': {label: '未通过', show: 'danger'}
          }, width: '80', fieldType: 'select', edit: false
        }],
      defaultInsertForm: {fondId: this.$route.query.fondId, status: '1', managementTypeCode: this.managementTypeCode},
      managementTypeId: '',
      //分类code，查询需要
      managementTypeCode: ''
    }
  },
  methods: {
    async defaultSearchForm() {
      if (!this.managementTypeId) {
        await this.getManagementTypes()
      }
      return {
        fondId: this.$route.query.fondId,
        managementTypeCode: this.managementTypeCode,
        status: '1',
      }
    },
    //获取所有全宗卷大类分类树
    async getManagementTypes() {
      await this.$http.post('/managementType/getManagementTypeAndDossierTree').then(({data}) => {
        if (data && data.success) {
          this.managementTypes = data.data
          let managementType = this.managementTypes[0]
          if (managementType.children) {
            this.managementTypeId = managementType.children[0].id
            this.managementTypeCode = managementType.children[0].description
            this.$nextTick(() => {
              this.$refs.tree.setCurrentKey(managementType.children[0].id)
            })
          }
        }
      })
    },
    selectFond() {
      this.$refs.table.reload()
    },
    keyUp(e) {
      e.target.value = e.target.value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '');
    },
    //对比查询
    comparative() {
      if (this.selection.length == 2) {
        this.$router.push({
          path: '/archive/management/sea/detail',
          query: {
            managementOneId: this.selection[0].id,
            managementTwoId: this.selection[1].id,
          }
        })
      } else {
        this.$message.warning("请选择两条全宗卷信息")
      }
    },
    //过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    handleNodeClick(v) {
      this.managementTypeId = v.id
      this.managementTypeCode = v.description
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(v.id)
      })
      this.$refs.table.reload()
    },
  },
  computed: {
    /**
     * 映射table-index 选中数据
     * @returns {[]|*|*[]}
     */
    selection() {
      if (this.$refs.table && this.$refs.table.tableSelection) {
        return this.$refs.table.tableSelection
      } else {
        return []
      }
    }
  }
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
