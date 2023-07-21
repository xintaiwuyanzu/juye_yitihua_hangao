<template>
  <div>
    <div class="box">
      <div @click="add" class="view add">
        <i class="el-icon-circle-plus-outline" style="font-size: 50px"></i>
        <div style="margin-top: 5px">
          <span style="font-size: 15px">创建业务领域</span>
        </div>
      </div>
      <div :key="index" class="view" v-for="(i,index) in realms">
        <div style="display: flex;height: 80%">
          <div class="type">{{i.realmType}}</div>
          <div class="content">
            <div style="line-height: 30px">
              <span style="margin-right: 5px">{{i.realmName}}</span>
              <el-tag :type="i.status==='0'?'primary':'success'" size="mini" v-if="false">
                {{i.status==='0'?'未发布':'已发布'}}
              </el-tag>
            </div>
            <div>
              <div style="color: grey;font-size: 14px">行政部门: {{i.realmArea}}</div>
              <div style="color: #0164AC;line-height: 30px;font-size: 13px">
                <i class="el-icon-price-tag" style="transform:rotate(45deg)"></i>
                {{i.realmArea}}
              </div>
              <div style="font-size: 13px;color: grey">{{i.realmMark}}</div>
            </div>
          </div>
        </div>
        <div class="btm">
          <el-badge :value="classes[i.id]">
            <el-button @click="viewRealm(i)">对象</el-button>
          </el-badge>
          <el-button @click="edit(i)" style="margin:0 10px 0 20px">编辑</el-button>
          <el-button @click="deleteRealm(i)" style="color: red" type="text">删除</el-button>
        </div>
      </div>
    </div>
    <el-dialog :before-close="cancel" :title="opt?'添加领域':'修改领域'" :visible.sync="visible">
      <el-form :label-width="width" :model="realm">
        <el-form-item label="领域名称" required>
          <el-input v-model="realm.realmName"></el-input>
        </el-form-item>
        <el-form-item label="行政部门" required>
          <el-input v-model="realm.realmArea"></el-input>
        </el-form-item>
        <el-form-item label="领域节点" required>
          <el-checkbox-group v-model="nodes">
            <el-checkbox :key="index" :label="i" v-for="(i,index) in realmNodes">{{i}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="领域类型" required>
          <el-select v-model="realm.realmType">
            <el-option :key="index" :label="i" :value="i" v-for="(i,index) in realmTypes"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="realm.realmMark"></el-input>
        </el-form-item>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button @click="save" type="primary">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        visible: false,
        realm: {},
        realms: [],
        width: '100px',
        realmNodes: ['档案', '部门'],
        realmTypes: ['人', '事'],
        nodes: ['档案', '部门'],
        opt: true,
        classes: []
      }
    },
    methods: {
      $init() {
        this.getRealms()
        this.getClassNum()
      },
      //查询领域的数量
      async getClassNum() {
        const {data} = await this.$post('realm/getClassNum')
        if (data && data.success) {
          this.classes = data.data
        }
      },
      //获取所有领域
      async getRealms() {
        const {data} = await this.$post('realm/page')
        if (data && data.success) {
          this.realms = data.data.data
        }
      },
      //查看领域
      viewRealm(row) {
        this.$router.push({
          path: '/mapping/realm/viewRealm',
          query: {
            id: row.id
          }
        })
      },
      //保存
      async save() {
        let type = 'error'
        this.realm.status = '0'
        const {data} = await this.$post(this.opt ? 'realm/insert' : 'realm/update', this.realm)
        if (data && data.success) {
          this.visible = false
          type = 'success'
          this.getRealms()
        }
        this.$message({
          type,
          message: data.message
        })
      },
      add() {
        this.opt = true
        this.visible = true
        this.realm = {}
        this.nodes = ['档案', '部门']
        this.realm.realmNodes = this.nodes.toString()
      },
      edit(obj) {
        this.opt = false
        this.visible = true
        this.realm = obj
        this.nodes = obj.realmNodes !== null ? obj.realmNodes.split(',') : ''
      },
      deleteRealm(obj) {
        this.$confirm('确定删除该领域吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$post('realm/delete', obj).then(({data}) => {
            if (data && data.success) {
              this.$message({
                type: 'success',
                message: '删除成功'
              })
              this.getRealms()
            }
          })
        })
      },
      cancel() {
        this.visible = false
        this.realm = {}
      }
    },
    watch: {
      //勾选领域节点时
      nodes(val) {
        this.realm.realmNodes = val.toString()
      }
    }
  }
</script>

<style scoped>
  .box {
    display: flex;
    flex-wrap: wrap;
  }

  .view {
    width: 250px;
    height: 180px;
    background-color: white;
    margin: 10px;
    padding: 5px 10px;
  }

  .add {
    display: flex;
    flex-direction: column;
    justify-content: center;
    text-align: center;
    color: #007fac;
    cursor: pointer;
  }

  .add:hover {
    color: #003cb4;
  }

  .type {
    width: 50px;
    height: 50px;
    line-height: 50px;
    background-color: #0164AC;
    color: white;
    text-align: center;
    border-radius: 50px;
  }

  .content {
    text-align: left;
    padding-left: 5px;
    width: 200px;
  }

  .btm {
    display: flex;
    justify-content: center;
  }

  /deep/ .el-badge__content {
    border-radius: 10px;
  }
</style>