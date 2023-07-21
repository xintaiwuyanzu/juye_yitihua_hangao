<template>
  <section>
    <nac-info back title="查看领域对象"/>
    <div class="box">
      <div style="padding: 10px 0 20px 10px">
        <div class="b">
          <div class="t">{{realm.realmType}}</div>
          <div style="padding: 5px 10px">
            <div style="padding-bottom: 12px;line-height: 30px">
              <span class="title">{{realm.realmName}}</span>
              <el-tag :color="realm.status===1?'success':'info'" :type="realm.status===1?'success':'primary'">
                {{realm.status===1?'已发布':'未发布'}}
              </el-tag>
            </div>
            <div style="color: grey;font-size: 17px">行政部门: {{realm.realmArea}}</div>
            <div style="color: #0164AC;line-height: 30px">
              <i class="el-icon-price-tag" style="transform:rotate(45deg)"></i>
              {{realm.realmArea}}
            </div>
            <div style="font-size: 15px;color: grey">{{realm.realmMark}}</div>
          </div>
        </div>
      </div>
      <div>
        <div>
          <el-button @click="visible=true" style="float: right;margin-bottom: 5px" type="primary">添加对象</el-button>
          <el-table :data="realmClasses" border stripe>
            <el-table-column align="center" type="index" width="120"></el-table-column>
            <el-table-column align="center" label="对象" prop="formAlias"></el-table-column>
            <el-table-column align="center" label="节点" prop="realmNode">
              <template v-slot="scope">
                {{map.get(realm.id)}}
              </template>
            </el-table-column>
            <el-table-column align="center" date-format label="创建时间" prop="createDate"></el-table-column>
            <el-table-column align="center" label="操作" width="150">
              <template v-slot="scope">
                <el-button @click="releaseClass(scope.row)" style="color: red" type="text">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <el-dialog :visible.sync="visible" title="添加对象" width="700px">
        <el-form :model="realmClass" label-width="120px">
          <el-form-item label="对象" required>
            <el-select v-model="realmClass.id">
              <el-option :key="index" :label="i.formAlias" :value="i.id"
                         v-for="(i,index) in allRealmClasses"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="节点" required>
            <el-select v-model="realmClass.realmNode">
              <el-option :key="index" :label="i" :value="i" v-for="(i,index) in nodes"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span class="dialog-footer" slot="footer">
            <el-button @click="visible = false">取 消</el-button>
            <el-button @click="saveForm" type="primary">保 存</el-button>
          </span>
      </el-dialog>
    </div>
  </section>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        realm: {},
        realms: [],
        realmClasses: [],//该领域的对象
        allRealmClasses: [],//未分配领域的对象
        visible: false,
        realmClass: {},
        nodes: ['档案', '部门'],
        map: new Map()
      }
    },
    methods: {
      $init() {
        this.getDetail()
      },
      //获取领域详细信息
      getDetail() {
        this.$post('realm/detail', {id: this.$route.query.id}).then(({data}) => {
          this.realm = data.data
          this.nodes = this.realm.realmNodes.split(',')
          this.getRealmClass()
        })
      },
      //获取领域下的对象
      getRealmClass() {
        this.$post('realm_class/getAllClass').then(({data}) => {
          this.realmClasses = []
          this.allRealmClasses = []
          data.data.forEach(i => {
            //下拉框数据
            this.allRealmClasses.push(i)
            if (i.realmId !== null && i.realmId !== '') {
              if (i.realmId.split(',').indexOf(this.realm.id) !== -1) {
                const json = JSON.parse(i.realmNode)
                for (let k of Object.keys(json)) {
                  this.map.set(k, json[k]);
                }
                //保存当前领域的对象
                this.realmClasses.push(i)
              }
            }
          })
        })
      },
      async saveForm() {
        this.realmClass.realmId = this.realm.id
        const {data} = await this.$post('realm_class/update', this.realmClass)
        if (data && data.success) {
          this.$message({
            type: 'success',
            message: '添加成功'
          })
          this.visible = false
          this.getRealmClass()
        }
      },
      async releaseClass(row) {
        try {
          await this.$confirm('确定释放该对象吗？', '警告')
          const {data} = await this.$post('realm_class/releaseClass', {id: row.id, realmId: this.realm.id})
          if (data && data.success) {
            this.$message.success('删除成功！')
            this.getRealmClass()
          } else {
            this.$message.error('删除失败！')
          }
        } catch (e) {
        }
      }
    }
  }
</script>

<style scoped>
  .box {
    height: 100%;
    background-color: white;
    padding: 10px;
  }

  .t {
    width: 80px;
    height: 80px;
    line-height: 80px;
    background-color: #0273C7;
    color: white;
    font-size: 20px;
    text-align: center;
    border-radius: 80px;
  }

  .b {
    display: flex;
  }

  .title {
    font-size: 20px;
    font-weight: 400;
    margin-right: 10px;
  }
</style>