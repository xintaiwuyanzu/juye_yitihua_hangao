<template>
  <section class="fond_type">
    <nac-info/>
    <div class="index_main">
      <el-row style="height: 100%" :gutter="20">
        <el-col :span="5" style="height: 100%">
          <el-card shadow="hover" style="width: 100%;height: 100%;overflow-y: scroll;">
            <el-select v-model="fondId" filterable placeholder="请选择全宗" @change="selectFond" style="width: 100%">
              <el-option
                  v-for="item in fonds"
                  :key="item.id"
                  :label="item.label"
                  :value="item.id">
              </el-option>
            </el-select>
            <el-tree :current-node-key="managementTypeId" :data="managementTypes" :expand-on-click-node="false"
                     :filter-node-method="filterNode" @node-click="handleNodeClick"
                     label="name"
                     node-key="id"
                     class="fond_material_tree"
                     ref="tree"
                     default-expand-all>
              <template #default="scope">
                <div class="custom-node">
                  <el-tooltip :content="scope.node.label" class="item" effect="dark" placement="top"><span>{{ scope.node.label }}</span>
                  </el-tooltip>
                </div>
              </template>
            </el-tree>
          </el-card>
        </el-col>
        <el-col :span="19" style="height: 100%">
          <el-card shadow="hover" style="height:100%">
            <table-index :showTitle="false" :fields="fields"
                         path="management" :insert="false" :edit="false"
                         :delete="false"
                         :default-search-form="defaultSearchForm" ref="table" style="height: 100%">
              <el-table-column prop="title" label="题名" align="center" show-overflow-tooltip>
                <template slot-scope="{row}">
                  <el-button type="text"
                             @click="$router.push({path: '/archive/management/edit',query: {optType:'edit',managementTypeCode: managementTypeCode,id:row.id}})">
                    {{ row.title }}
                  </el-button>
                </template>
              </el-table-column>
              <template slot-scole='form' slot='search-$btns'>
                <el-button type="primary" @click="toAdd">添加</el-button>
              </template>
              <template v-slot:table-$btns="scope">
                <el-button
                    type="text"
                    @click="$router.push({path: '/archive/management/edit',query: {optType:'edit',managementTypeCode: managementTypeCode,id:scope.row.id}})"
                    size="mini" width="100" v-show="scope.row.status==='0'">修改
                </el-button>
                <delete-process type="text" :managementId="scope.row.id" :status="scope.row.status"
                                v-show="scope.row.status==='0'||scope.row.status==='2'"
                                @loadData="loadData" @click="deleteById(scope.row)"
                                @deleteById="deleteById(scope.row)"/>
              </template>
            </table-index>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
  import StartProcess from "./process";
  import DeleteProcess from "./process/deleteProcess"

  export default {
    components: {StartProcess, DeleteProcess},
    data() {
      return {
        dict: ['archive.compilationStatus'],
        //所有全宗
        fonds: [],
        //所有全宗卷大类
        managementTypes: [],
        fondId: '',
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
        // {prop: 'fond_code', label: '全宗号', width: '60', search: true, required: false},
        // {prop: 'managementTypeName', label: '材料分类', search: true, required: false},
        {prop: 'title', label: '题名', search: true, required: false},
        {prop: 'archive_code', label: '文件材料编号', width: '160', required: false},
        {prop: 'retentionPeriod', label: '保管期限', width: '70', required: false},
        {
          prop: 'status', label: '状态', component: 'tag', showTypeKey: 'show', mapper: {
            '0': {label: '待提交', show: 'warning'},
            '1': {label: '审批中', show: 'info'},
            '2': {label: '通过', show: 'success'},
            '3': {label: '未通过', show: 'danger'}
          }, width: '80', fieldType: 'select', edit: false
        }],
      //树节点，分类id
      managementTypeId: '',
      //分类code，查询需要
      managementTypeCode: ''
    }
  },
  methods: {
    async defaultSearchForm() {
      if (!this.fondId) {
        await this.getFonds()
      }
      return {
        fondId: this.fondId,
        managementTypeCode: this.managementTypeCode
      }
    },
    //获取全宗列表，默认取第一个全宗
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      this.fonds = data.data
      this.fondId = this.fonds[0].id
      if (!this.managementTypeId) {
        await this.getManagementTypes()
      }
    },
    //获取所有全宗卷大类分类树
    async getManagementTypes() {
      const {data} = await this.$http.post('/managementType/getManagementTypeAndDossierTree')
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
    },
    selectFond() {
      this.$refs.table.reload()
    },
    //跳转到添加页面
    toAdd() {
      if (this.fondId == '' || this.fondId == null) {
        this.$message.warning("当前用户无所属全宗！")
        return
      }
      this.$router.push({
        path: '/archive/management/edit',
        query: {optType: 'add', managementTypeCode: this.managementTypeCode, fondId: this.fondId}
      })
    },
    //点击树节点
    handleNodeClick(v) {
      this.managementTypeId = v.id
      this.managementTypeCode = v.description
      this.$nextTick(() => {
        this.$refs.tree.setCurrentKey(v.id)
      })
      this.$refs.table.reload()
    },
    //过滤
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    keyUp(e) {
      e.target.value = e.target.value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '');
    },
    loadData() {
      this.$refs.table.reload()
    },
    deleteById(row) {
      try {
        this.$confirm("确认删除？", '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
          dangerouslyUseHTMLString: true
        }).then(() => {
          this.loading = true
          this.$http.post('/management/delete', {id: row.id})
              .then(({data}) => {
                if (data.success) {
                  this.$message.success("删除成功")
                  this.loadData()
                } else {
                  this.$message.error(data.message)
                }
                this.loading = false
              })
        })
      } catch {
        this.loading = false
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
