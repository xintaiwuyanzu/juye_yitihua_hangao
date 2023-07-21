<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="年检名称" prop="fondName">
        <el-input v-model="searchForm.name" prefix-icon="el-icon-search" placeholder="请输入" style="width: 160px"
                  clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchF" size="small">搜 索</el-button>
        <el-button type="primary" size="small"  v-on:click="onAdd">添加</el-button>
        <el-button type="success" size="small"  v-on:click="check">提交</el-button>
        <!--        <el-button   size="small" v-on:click="exp">导出打印</el-button>-->
        <el-button type="danger" size="small"  v-on:click="ondelete">删除</el-button>
      </el-form-item>
<!--      <div style="float: right;margin-top:3.5px">
        <el-button type="primary" size="small"  v-on:click="onAdd">添加</el-button>
        <el-button type="success" size="small"  v-on:click="check">提交</el-button>
&lt;!&ndash;        <el-button   size="small" v-on:click="exp">导出打印</el-button>&ndash;&gt;
        <el-button type="danger" size="small"  v-on:click="ondelete">删除</el-button>
      </div>-->
    </el-form>
  </section>
</template>

<script>
import fromMixin from '@/util/formMixin'
import lib from '@dr/framework/src/components/login/util'

export default {
  mixins: [fromMixin],
  data() {
    return {
      id:lib.getToken(),
      searchForm: {
        name: '',
      },

      addForm: {
        columnShow: true
      },
      multipleSelection: [],
      backDialogVisible: false,
      sourceDialog: false,
      delMsg: '请至少选择一条记录',
    }
  },
  methods: {
    searchF() {
      this.$emit('func', this.searchForm)
    },
    ondelete() {
      this.multipleSelection = this.$parent.$parent.multipleSelection
      if (this.multipleSelection.length < 1) {
        this.$message.warning(this.delMsg)
      } else {
        this.$confirm('此操作将删除选中选, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = ''
          for (let i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ','
          }
          this.$http.post('/onlineas/deletes', {
            aIds: ids
          }).then(
              ({data}) => {
                if (data.success) {
                  this.$message.success('操作成功！')
                  this.searchF()
                } else {
                  this.$message.error(data.message)
                }
              })
        })
      }
    },
    check() {
      this.multipleSelection = this.$parent.$parent.multipleSelection
        let ids = ''
        for (let i = 0; i < this.multipleSelection.length; i++) {
            if (this.multipleSelection[i].status=='未审核'){
                ids += this.multipleSelection[i].id + ','
            }
        }
      if (this.multipleSelection.length < 1||ids.length<1) {
        this.$message.warning("请选择至少一条未提交审核数据")
      } else {
        this.$confirm('此操作将上报选中项, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http.post('/onlineas/checks', {
            aIds: ids
          }).then(
              ({data}) => {
                if (data.success) {
                  this.$message.success('操作成功！')
                  this.searchF()
                } else {
                  this.$message.error(data.message)
                }
              })
        })
      }
    },
    onAdd() {
      this.$router.push({path: '/onlineAS/addDetail',query: {id:"",optype:"add"} })
    },
    exp(){
      this.multipleSelection = this.$parent.$parent.multipleSelection
      if (this.multipleSelection.length < 1) {
        this.$message.warning(this.delMsg)
      } else {
        this.$confirm('此操作将导出选中项, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let ids = ''
          for (let i = 0; i < this.multipleSelection.length; i++) {
            ids += this.multipleSelection[i].id + ','
          }
          // let url = "api/annals/printLabel?aIds="+ids
          // window.open(url)
          this.$http.post('annals/printLabel',
              {aIds: ids},
              {timeout: 1000 * 60 * 1}).then(
              ({data}) => {
                if (data.success) {
                  this.$message.success('操作成功！')
                } else {
                  //this.$message.error(data.message)
                }
              })
          this.searchF()
        })
      }
    },

  }
}
</script>

<style scoped>

</style>
