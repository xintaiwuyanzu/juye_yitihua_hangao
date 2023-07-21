<template>
  <table-index :delete="true" :edit="false" :fields="fields" :insert="false" path="relation" ref="table">
    <template v-slot:search-$btns>
      <el-button @click="add" icon="el-icon-plus" type="primary">新增关系</el-button>
    </template>
    <template v-slot:table-$btns="scope">
      <el-button @click="edit(scope.row)" type="text" width="40">编辑</el-button>
      <el-button @click="toAltal(scope.row)" type="text" width="40">图谱</el-button>
    </template>
    <el-dialog :before-close="cancel" :title="option?'新增关系':'修改关系'" :visible.sync="visible">
      <div class="box">
        <div class="b2">
          <div class="b3">
            <el-radio-group :disabled="!option" v-model="relation.relationType">
              <el-radio :label="1">普通关系</el-radio>
              <el-radio :label="2">复杂关系</el-radio>
            </el-radio-group>
          </div>
          <el-divider/>
          <el-form :model="relation" label-width="130px" ref="form">
            <el-form-item label="源对象" required>
              <el-select v-model="relation.sourceFormId">
                <el-option :key="index" :label="i.formName" :value="i.id" v-for="(i,index) in forms"></el-option>
              </el-select>
            </el-form-item>
            <div :key="index" v-for="(i,index) in jsonText" v-show="relation.relationType===2">
              <el-form-item label="中间对象" required>
                <el-select @change="change(i,index)" v-model="i.target">
                  <el-option :key="index" :label="i.formName" :value="i.id"
                             v-for="(i,index) in forms"></el-option>
                </el-select>
                <i @click="addObj" class="el-icon-plus add_obj" v-if="index===0"/>
              </el-form-item>
              <el-form-item label="与上个对象关系" required>
                <el-input v-model="jsonText[index].relationPre"></el-input>
              </el-form-item>
              <el-form-item label="与目标关系" required>
                <el-input v-model="jsonText[index].relation"></el-input>
              </el-form-item>
            </div>
            <el-form-item label="目标对象" required>
              <el-select v-model="relation.targetFormId">
                <el-option :key="index" :label="i.formName" :value="i.id" v-for="(i,index) in forms"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="源-目标关系" required>
              <el-input v-model="relation.relationName"></el-input>
            </el-form-item>
            <el-form-item label="是否逆向">
              <el-radio-group v-model="relation.haveReverse">
                <el-radio :label="1">是</el-radio>
                <el-radio :label="0">否</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="逆向关系名称" required v-if="relation.haveReverse===1">
              <el-input v-model="relation.reverseName"></el-input>
            </el-form-item>
            <el-form-item label="关系描述">
              <el-input type="textarea" v-model="relation.relationMark"></el-input>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <span class="dialog-footer" slot="footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button @click="save" type="primary">确 定</el-button>
      </span>
    </el-dialog>
  </table-index>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        fields: [
          {
            prop: 'relationType',
            label: '关系类型',
            showTypeKey: 'show',
            mapper: {1: {label: '普通', show: 'success'}, 2: {label: '复杂', show: 'primary'}},
            component: 'tag'
          },
          {prop: 'sourceName', label: '源对象', search: true},
          {prop: 'relationName', label: '关系', search: true},
          {prop: 'targetName', label: '目标对象', search: true},
          {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', sortable: true},
          {prop: 'relationMark', label: '描述'}
        ],
        relation: {},
        //json关系数据
        first: {source: '', relation: '', target: ''},//源对象json数据
        jsonText: [{source: '', relation: '', target: '', relationPre: ''}],//中间对象json数据
        end: {source: '', relation: '', target: ''},//目标对象json数据
        //弹框
        visible: false,
        option: true,
        forms: []
      }
    },
    methods: {
      $init() {
        this.getForms()
      },
      //获取对象数据
      async getForms() {
        const {data} = await this.$post('relation/getForms', {id: ''})
        if (data && data.success) {
          this.forms = data.data
        }
      },
      //提交
      async save() {
        //获取关系json数据
        const obj = this.jsonText[0]
        let jsonText = ''
        if (obj.source !== '' && obj.target !== '') {
          //存在中间对象
          jsonText = this.jsonText.concat(this.end)
        } else {
          //只有源对象和目标对象
          jsonText = {source: this.first.source, target: this.end.target, relation: this.relation.relationName}
        }
        this.relation.jsonText = JSON.stringify(jsonText)
        //提交
        const {data} = await this.$post(this.option ? 'relation/insert' : 'relation/update', this.relation)
        if (data) {
          this.$message({
            type: data.success ? 'success' : 'error',
            message: data.message
          })
          if (data.success) {
            this.cancel()
          }
        }
      },
      cancel() {
        this.visible = false
        this.jsonText = [{source: '', relation: '', target: '', relationPre: ''}]
        this.$refs.table.loadData()
      },
      add() {
        this.relation = {relationType: 1, haveReverse: 0}
        this.option = true
        this.visible = true
      },
      edit(obj) {
        this.relation = obj
        const jsonArray = JSON.parse(obj.jsonText)
        if (jsonArray.length > 1) {
          jsonArray.pop()
          this.jsonText = jsonArray
        }
        this.option = false
        this.visible = true
      },
      addObj() {
        //中间对象json数组加一
        this.jsonText.push({
          source: this.jsonText[this.jsonText.length - 1].target,
          relation: '',
          target: '',
          relationPre: ''
        })
      },
      change(i, index) {
        if (index > 0) {
          i.source = this.jsonText[index - 1].target
        }
      },
      //查看该关系的图谱
      toAltal(row) {
        this.$router.push({
          path: '/mapping/tripletData/paint',
          query: {
            relationName: row.relationName,
            i: 2
          }
        })
      }
    },
    watch: {
      'relation.sourceFormId'(id) {
        if (id !== undefined) {
          const v = this.forms.filter(i => {
            return i.id === id
          })
          //给json数据复制
          this.first.source = v[0].formName
          this.jsonText[0].source = v[0].formName
        }
      },
      'relation.targetFormId'(id) {
        if (id !== undefined) {
          const v = this.forms.filter(i => {
            return i.id === id
          })
          //给json数据复制
          this.end = {source: this.jsonText[this.jsonText.length - 1].target, relation: '', target: v[0].formName}
        }
      }
    }
  }
</script>

<style scoped>
  .box {
    width: 100%;
  }

  .b2 {
    padding-right: 10px;
    overflow-y: scroll;
  }

  .b3 {
    text-align: center;
  }

  .add_obj {
    font-size: 20px;
    margin-left: 10px;
    cursor: pointer;
  }
</style>