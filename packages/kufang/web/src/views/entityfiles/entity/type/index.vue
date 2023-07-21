<template>
  <div class="index_main card person_main">
    <el-row type="flex">
      <el-col :span="5" style="max-width: 280px">
        <el-card shadow="hover">
          <div slot="header">
            <strong>部门单位</strong>
          </div>
          <el-tree class="sysMenuTree"
                   :data="fonds"
                   default-expand-all
                   @node-click="click"
                   ref="menuTree">
            <div slot-scope="{ node, data }" style="flex: 1;margin: 2px; ">
              <span v-if="organiseId==data.data.id" style=" color: red;font-family: 等线">{{ data.label }}</span>
              <span v-if="organiseId!=data.data.id" style=" color: #409EFF;font-family: 等线">{{ data.label }}</span>
            </div>
          </el-tree>
        </el-card>
      </el-col>
      <el-col :span="19">
        <el-card shadow="hover" style="position: relative">
          <div slot="header">
            <strong>实体档案分类</strong>
          </div>
          <table-index title="实体档案分类" :fields="fields"
                       path="archiveType"
                       style="overflow-x: auto;"
                       :delete="true"
                       :edit="true"
                       :insert="false" :default-search-form="defaultSearchForm"
                       ref="table">
            <template v-slot:edit="form">
              <el-form-item prop="fondId" label="全宗" before="archiveTypeCode" required>
                <el-select filterable clearable v-model="form.fondId" placeholder="请选择全宗">
                  <el-option v-for="item in fondOne" :key="item.id"
                             :label="`${item.data.code}  ${item.label}`" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </template>
            <template v-slot:search-$btns="scope">
              <el-button type="primary" @click="openInsertDialog">
                新 增
              </el-button>
            </template>
          </table-index>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>
<script>
export default {
  data() {
    return {
      menuData: [],
      fonds:[],
      fondOne:[],
      ConfigForm:{},
      organiseId: "",
      fields: {
        archiveTypeCode: {
          label: '类别编号',
          align: 'center',
          required: true,
          width: 180
        },
        archiveTypeName: {
          label: '类别名称',
          align: 'center',
          search: true,
          required: true
        },
        order: {
          label: '排序',
          align: 'center',
          search: true,
          required: true,
          width: 180
        },
        createDate: {
          label: '创建时间',
          edit: false,
          search: false,
          dateFormat: "YYYY-MM-DD HH:mm:ss",
          width: 200
        }
      },
      defaultSearchForm: {aType: 'root'},
      defaultInsertForm: {},
    }
  },
  methods: {
    $init() {
      this.fond()
      this.loadLibRoot()
    },
    openInsertDialog() {
      if(!this.fondOne.length){
        this.$message.warning("请选择全宗后再新增门类")
        return false
      }
      this.$refs.table.showEdit(this.defaultInsertForm)
    },
    fond() {
      this.$http.post("sysResource/personResource",{type:"fond"}).then(({data}) => {
        if (data.success) {
          this.fonds = data.data
          if(this.fonds.length>0){
            this.click(this.fonds[0])
          }
        }
      })

    },
    loadLibRoot() {
      this.loading = true
      this.$http.post('/organise/organiseTree', {all: true, sysId: this.sysId})
        .then(({data}) => {
          if (data.success) {
            this.menuData = data.data ? data.data : []
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      this.ConfigForm = {}
    },
    click(data) {
      this.defaultSearchForm.fondId = data.id

      this.fondOne = this.fonds.filter(item => item.id == data.id)
      this.$refs.table.reload()
    },
  }
}
</script>
<template>
  <div class="index_main card person_main">
    <el-row type="flex">
      <el-col :span="5" style="max-width: 280px;height: 86vh">
        <el-card shadow="hover">
          <div slot="header">
            <strong>部门单位</strong>
          </div>
          <el-tree class="sysMenuTree"
                   :data="fonds"
                   default-expand-all
                   @node-click="click"
                   ref="menuTree"
                   height="84vh">
            <div slot-scope="{ node, data }" style="flex: 1;margin: 2px; ">
              <span v-if="organiseId==data.data.id" style=" color: red;font-family: 等线">{{ data.label }}</span>
              <span v-if="organiseId!=data.data.id" style=" color: #409EFF;font-family: 等线">{{ data.label }}</span>
            </div>
          </el-tree>
        </el-card>
      </el-col>
      <el-col :span="19" style="height: 86vh">
        <el-card shadow="hover" style="position: relative">
          <div slot="header">
            <strong>实体档案分类</strong>
          </div>
          <table-index title="实体档案分类" :fields="fields"
                       path="archiveType"
                       style="overflow-x: auto;"
                       :delete="true"
                       :edit="true"
                       :insert="false" :default-search-form="defaultSearchForm"
                       ref="table">
            <template v-slot:edit="form">
              <el-form-item prop="fondId" label="全宗" before="archiveTypeCode" required>
                <el-select filterable clearable v-model="form.fondId" placeholder="请选择全宗">
                  <el-option v-for="item in fondOne" :key="item.id"
                             :label="`${item.data.code}  ${item.label}`" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </template>
            <template v-slot:search-$btns="scope">
              <el-button type="primary" @click="openInsertDialog">
                新 增
              </el-button>
            </template>
          </table-index>
        </el-card>
      </el-col>
    </el-row>
  </div>

</template>
<script>
export default {
  data() {
    return {
      menuData: [],
      fonds:[],
      fondOne:[],
      ConfigForm:{},
      organiseId: "",
      fields: {
        archiveTypeCode: {
          label: '类别编号',
          align: 'center',
          required: true,
          width: 180
        },
        archiveTypeName: {
          label: '类别名称',
          align: 'center',
          search: true,
          required: true
        },
        order: {
          label: '排序',
          align: 'center',
          search: true,
          required: true,
          width: 180
        },
        createDate: {
          label: '创建时间',
          edit: false,
          search: false,
          dateFormat: "YYYY-MM-DD HH:mm:ss",
          width: 200
        }
      },
      defaultSearchForm: {aType: 'root'},
      defaultInsertForm: {},
    }
  },
  methods: {
    $init() {
      this.fond()
      this.loadLibRoot()
    },
    openInsertDialog() {
      if(!this.fondOne.length){
        this.$message.warning("请选择全宗后再新增门类")
        return false
      }
      this.$refs.table.showEdit(this.defaultInsertForm)
    },
    fond() {
      this.$http.post("sysResource/personResource",{type:"fond"}).then(({data}) => {
        if (data.success) {
          this.fonds = data.data
          if(this.fonds.length>0){
            this.click(this.fonds[0])
          }
        }
      })

    },
    loadLibRoot() {
      this.loading = true
      this.$http.post('/organise/organiseTree', {all: true, sysId: this.sysId})
        .then(({data}) => {
          if (data.success) {
            this.menuData = data.data ? data.data : []
          } else {
            this.$message.error(data.message)
          }
          this.loading = false
        })
      this.ConfigForm = {}
    },
    click(data) {
      this.defaultSearchForm.fondId = data.id

      this.fondOne = this.fonds.filter(item => item.id == data.id)
      this.$refs.table.reload()
    },
  }
}
</script>
