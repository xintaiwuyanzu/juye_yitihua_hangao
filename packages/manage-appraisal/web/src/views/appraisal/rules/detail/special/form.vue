<template>
  <el-dialog
      :title="form.id?'编辑专题':'新增专题'"
      :visible.sync="dialogVisible"
      width="60%">
    <el-form style="margin-top: 10px" ref="form" :model="form" label-width="100px">
      <el-form-item label="专题名称：" prop="specialName" required>
        <el-input style="min-height: 30px" v-model="form.specialName"></el-input>
      </el-form-item>
      <el-form-item label="专题标签:" prop="specialTagIds" required>
        <el-select v-model="form.specialTagIds" placeholder="请选择标签" multiple filterable clearable @change="selectTag">
          <el-option
              v-for="(item,index) in tags"
              :key="item.id"
              :label="item.tagName"
              :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <el-row v-for="c in condition" :key="c.field" style="margin-top: 5px" justify="space-between" type="flex">
      <el-col :span="3">
        <el-select v-model="c.field" placeholder="请选择过滤字段">
          <el-option
              v-for="item in fields"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="3" style="margin-left: 2px">
        <el-select v-model="c.relation" placeholder="请选择过滤条件">
          <el-option
              v-for="item in relations"
              :key="item.id"
              :label="item.name"
              :value="item.id">
          </el-option>
        </el-select>
      </el-col>
      <el-col v-if="c.relation=='between'" :span="8" style="margin-left: 2px">
        <el-input v-model="c.value1" type="number"></el-input>
      </el-col>
      <el-col v-if="c.relation=='between'" :span="1" style="margin-left: 2px">
        <span style="margin-left: 15px;padding-top: 20px">___</span>
      </el-col>
      <el-col v-if="c.relation=='between'" :span="8" style="margin-left: 2px">
        <el-input v-model="c.value2" type="number"></el-input>
      </el-col>
      <el-col v-if="c.relation!='between'" :span="17" style="margin-left: 2px">
        <el-input v-model="c.value1"></el-input>
      </el-col>
      <el-col :span="1" style="margin-left: 2px">
        <el-button size="mini" style="margin-top: 2px" @click="remove(c.id)" type="danger">移除</el-button>
      </el-col>
    </el-row>
    <el-form style="margin-top: 10px" :model="temp" label-width="100px">
      <el-form-item>
        <span style="color: red" slot="label">说&#12288;&#12288;明：</span>
        <span style="color: red"> 当匹配关系为大于、小于时，匹配的字段需要是数字类型值，否则无法进行匹配，多个过滤条件之间为且关系 </span>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="addCondition">添加条件</el-button>
    <el-button type="primary" @click="saveForm(false)">保 存</el-button>
    <el-button type="primary" @click="saveForm(true)">保存并继续</el-button>
  </span>
  </el-dialog>
</template>
<script>
import {v4} from 'uuid'

export default {
  data() {
    return {
      dialogVisible: false,
      form: {},
      temp: {},
      condition: [],
      fields: [
        {id: 'TITLE', name: '标题'},
        {id: 'ARCHIVE_CODE', name: '档号'},
        {id: 'FOND_CODE', name: '全宗号'},
        {id: 'VINTAGES', name: '年份'},
        {id: 'content', name: '原文'},
      ],
      relations: [
        {id: 'greaterThan', name: '大于'},
        {id: 'lessThan', name: '小于'},
        {id: 'like', name: '相似'},
        {id: 'equal', name: '相等'},
        {id: 'notEqual', name: '不相等'},
        {id: 'startWith', name: '以关键词开头'},
        {id: 'endWith', name: '以关键词结尾'}
        // {id: 'between', name: '在指定范围内'}


      ],
      tags: [],
      specialTagNames: ''
    }
  },
  methods: {
    $init() {
      this.getTag()
    },
    //获取专题标签数据
    getTag() {
      this.$http.post('/specialTag/page', {page: false}).then(({data}) => {
        if (data.success) {
          this.tags = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    selectTag(v) {
      this.specialTagNames = ''
      let arrspecialTagNames = []
      for (let i = 0; i < v.length; i++) {
        this.tags.find((item) => { //这里的options就是数据源
          if (item.id == v[i]) {
            arrspecialTagNames.push(item.tagName)
          }
        });
      }
      this.specialTagNames = arrspecialTagNames.join(',')
    }
    ,
    saveForm(isGoOn) {
      this.$refs.form.validate((valid) => {
        if (valid) {
          let conditionString = ''
          if (this.condition.length == 0) {
            this.$message.error("必须添加一个过滤条件才可保存，请检查后重试")
            return
          }
          this.condition.forEach(c => {
            if (c.field == '' || c.relation == '' || c.value1 == '') {
              valid = false
            }
            if (c.relation == 'between' && c.value2 == '') {
              valid = false
            }
            conditionString += c.field + '@' + c.relation + '@' + c.value1 + '@' + c.value2 + '#'
          })
          if (!valid) {
            this.$message.error("所有条件都必须填写完成之后才能保存，请检查后重试")
            return
          }
          let url = 'insert'
          if (this.form.id) {
            url = 'update'
          }
          this.form.specialRemarks = conditionString
          this.form.specialTagNames = this.specialTagNames
          this.form.specialTagIds = this.form.specialTagIds.join(',')
          this.$http.post("/appraisalSpecial/" + url, this.form)
              .then(({data}) => {
                if (data.success) {
                  this.$message.success("保存成功")
                  this.$emit("reload")
                  if (isGoOn) {
                    this.condition = []
                    this.form.specialName = ''
                    this.form.id = ''
                  } else {
                    this.dialogVisible = false
                    this.$emit("reload")
                  }
                } else {
                  this.$emit("reload")
                  this.$message.error(data.message)
                }
              })
        }
      })
    }
    ,
    addCondition() {
      let c = {
        id: v4(),
        field: '',
        relation: '',
        value1: '',
        value2: ''
      }
      this.condition.push(c)
    }
    ,
    remove(id) {
      this.condition = this.condition.filter(v => v.id != id)
    }
    ,
    editForm(form) {
      this.form = form
      this.$http.post("/appraisalSpecialDetail/page", {specialId: form.id, page: false})
          .then(({data}) => {
            if (data.success) {
              this.condition = data.data
            }
          })
    }
  }
}
</script>
<style>
. is-leaf.el-tree-node_expand-icon {
  display: none;
}
</style>
