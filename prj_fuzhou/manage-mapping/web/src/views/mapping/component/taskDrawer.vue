<template>
  <el-drawer
      :before-close="close"
      :visible.sync="drawer"
      direction="rtl"
      size="40%"
      title="元数据方案">
    <div style="padding: 5px 10px">
      <el-table :data="tasks" border ref="table" width="100%">
        <div v-if="type==='1'">
          <el-table-column fixed="left" label="源对象">
            <template v-slot="scope">
              {{scope.row.params.relation.source}}
            </template>
          </el-table-column>
          <el-table-column label="关系">
            <template v-slot="scope">
              {{scope.row.params.relation.relation}}
            </template>
          </el-table-column>
          <el-table-column label="目标对象">
            <template v-slot="scope">
              {{scope.row.params.relation.target}}
            </template>
          </el-table-column>
        </div>
        <div v-else>
          <el-table-column label="梳理字段">
            <template v-slot="scope">
              梳理对象：{{scope.row.params.objIds.sourceId}}
              <div v-for="i in scope.row.params.properties">
                <el-tag style="margin: 5px">{{i.from}}</el-tag>
                <i class="el-icon-right"/>
                <el-tag>{{i.to}}</el-tag>
              </div>
            </template>
          </el-table-column>
        </div>
        <el-table-column :formatter="formatter" align="center" fixed="right" label="最后执行时间"
                         prop="endDate"></el-table-column>
        <el-table-column align="center" fixed="right" label="操作" width="200px">
          <template v-slot="scope">
            <el-switch :active-value="1" :inactive-value="0" @change="change(scope.row)" active-text="启用"
                       inactive-text="禁用" v-model="scope.row.isOpen"></el-switch>
            <el-divider direction="vertical"></el-divider>
            <el-button @click="deleteTask(scope.row)" style="color: red" type="text">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </el-drawer>
</template>

<script>
  export default {
    name: "taskDrawer",
    props: {
      drawer: Boolean,
      formId: String,
      type: String
    },
    data() {
      return {
        tasks: []
      }
    },
    methods: {
      close() {
        this.$emit('close')
      },
      async getTaskById() {
        this.tasks = []
        const {data} = await this.$post('/mapping_task/getTaskById', {formId: this.formId, type: this.type})
        if (data && data.success) {
          data.data.forEach(i => {
            i.params = JSON.parse(i.params)
            this.getClassById(i.params.objIds.sourceId).then(formType => {
              i.params.objIds.sourceId = formType
            })
            if (i.params.relation !== undefined) {
              this.relationName(i.params.relation.relation).then(relationName => {
                i.params.relation.relation = relationName
              })
            }
            this.tasks.push(i)
          })
        }
      },
      async getClassById(id) {
        const {data} = await this.$post('/realm_class/getFormMsg', {formId: id})
        if (data && data.success) {
          return data.data.formType
        }
      },
      formatter(row, column, cellValue) {
        return this.$moment(this.$moment(parseInt(cellValue))).format('YYYY-MM-DD HH:mm:ss')
      },
      async change(row) {
        const {data} = await this.$post('/mapping_task/updateIsOpen', {id: row.id, isOpen: row.isOpen})
        if (data && data.success) {
          this.$message.success('操作成功')
        }
      },
      async deleteTask(row) {
        try {
          await this.$confirm('确定删除该任务吗？', '提示', {type: 'warning'})
          const {data} = await this.$post('/mapping_task/delete', {id: row.id})
          if (data && data.success) {
            this.$message.success('操作成功')
            this.getTaskById()
          }
        } catch (e) {
        }
      },
      async relationName(id) {
        const {data} = await this.$post('/relation/detail', {id})
        if (data && data.success) {
          return data.data.relationName
        }
      }
    },
    watch: {
      drawer(b) {
        if (b) this.getTaskById()
      }
    }
  }
</script>