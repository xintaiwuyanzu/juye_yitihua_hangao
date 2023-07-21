<template>
  <section>
    <el-dropdown
        placement="bottom"
        trigger="click"
        @command="handleCommand">
      <el-button class="search-btn" type="primary">调整<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">调整选中</el-dropdown-item>
        <el-dropdown-item command="all">调整所有</el-dropdown-item>
        <el-dropdown-item command="query">调整查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <el-dialog width="80%" title="档案调整" :visible.sync="dialogShow" append-to-body>
      <el-form :model="form" :rules="rules" ref="form" label-width="160px" style="margin-left: 0px">
        <div v-for="(item, index) in form.item" :key="index + item.field">
          <el-row>
            <el-col :span="7">
              <el-form-item label="调整项" :prop="'item.' + index + '.field'"
                            :rules="{required: true, message: '目标字段不能为空', trigger: 'change'}">
                <el-select v-model="item.field" style="width: 200px" placeholder="字段" @change="changeField(item)">
                  <el-option v-for="(item, index) in fields"
                             :key="item.key + index"
                             :label="item.label"
                             :value="item.key">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="7">
              <el-form-item label="调整为" :prop="'item.' + index + '.newValue'">
                <el-input v-model="item.newValue" style="width: 200px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="3">
              <el-button icon="el-icon-plus" v-show="index===0" @click="addItem"
                         style="position:absolute; right: 5%; top: 4px"></el-button>
              <el-button icon="el-icon-delete" v-show="index!==0" @click="deleteItem(item, index)"
                         style="position:absolute; right: 5%; top: 4px"></el-button>
            </el-col>
          </el-row>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="save()" v-loading="loading" class="btn-submit">确 定</el-button>
      </div>

    </el-dialog>

  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

export default {
  extends: abstractComponent,
  //福州项目档案室接收暂存库批量调整功能，不带审核流程
  name: 'filing',
  data() {
    return {
      dialogShow:false,
      targetPerson: '',
      sendType: 'all',
      rules: {
        processId: [{required: true, trigger: 'blur', message: '请选择审核流程'}],
      },
      processes: [],
      ids: '',
      fields: [],
      persons: [],
      form: {
        targetPerson: '',
        remarks: '',
        item: [
          {
            field: '',
            newValue: '',
            oldValue: '',
            fieldName: '',
          },
        ],
      },
    }
  },
  methods: {
    //打开对话框，检查档案中有无已提交档案
    async handleCommand(command) {
      this.sendType = command
      const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
        formDefinitionId: this.formId,
        schemeType: 'form',
        formCode: 'form',
      })
      if (data.success) {
        data.data.fields.forEach(item => {
          this.fields.push({key: item.code, label: item.name})
        })
      }
      this.form={
        item: [
          {
            field: '',
            newValue: '',
            oldValue: '',
            fieldName: '',
          },
        ],
      }
      this.dialogShow = true
    },
    //调整档案提交审核
    save() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.loading = true
          //准备参数
          this.form.formDefinitionId = this.formId
          const query = this.eventBus.getQueryByQueryType(this.sendType)
          let fields = ''
          let newValues = ''
          let fieldNames = ''
          this.form.item.forEach(item => {
            fields += item.field + ','
            newValues += item.newValue + ','
            fieldNames += item.fieldName + ','
          })
          this.form.fieldsCode = fields
          this.form.newValues = newValues
          this.form.fieldNames = fieldNames
          this.form.formDataId = this.ids
          this.$http.post('/archiveChange/archiveChangeMulti', {
            fieldsCode: fields,
            newValues: newValues,
            fieldNames: fieldNames,
            formDataId: this.ids,
            ...query
          }).then(({data}) => {
            if (data && data.success) {
              this.dialogShow = false
              this.eventBus.$emit("loadData")
              this.$message.success("调整成功")
            } else {
              this.$message.error(data.message.replace("服务器错误", ""))
            }
          })
          this.loading = false
        } else {
          this.loading = false
        }
      })
    },

    /**
     * 显示提报dialog
     */
    async showSend() {
      this.dialogShow = true
    },
    /**
     *
     * 执行提报操作
     * 原来的调整操作
     * @returns {Promise<void>}
     */
    async doSend() {
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      this.loading = true
      const {data} = await this.$post('/batch/newBatch', {
        type: 'ARCHIVE',
        ...query
      })
      if (data.success) {
        this.$message.success('调整成功，请在管理库查看结果！')
      } else {
        this.$message.error(data.message)
      }
      this.loading = false
      this.dialogShow = false
    },

    async getIds(command) {
      let ids = ''
      if ('select' === command) {
        this.currentSelect.forEach(item => {
          ids += item.id + ','
        })
      } else if ('query' === command) {
        this.eventBus.data.forEach(item => {
          ids += item.id + ','
        })
      } else {
        const query = this.eventBus.getQueryByQueryType('all')
        await this.$http.post('/manage/formData/formDataPage', {
          fondId: query.fondId,
          categoryId: query.categoryId,
          formDefinitionId: query.formDefinitionId,
          _QUERY: query._QUERY,
          page: false,
        }).then(({data}) => {
          if (data.success) {
            data.data.data.forEach(item => {
              ids += item.id + ','
            })
          }
        })
      }
      return ids
    },

    /**
     *  获取审批流程
     */
    getProcess() {
      this.$http.post('/filingProcess/page', {page: false, processType: 'FILING'}).then(({data}) => {
        this.processes = data.data
      })
    },
    addItem() {
      this.form.item.push({
        field: '',
        newValue: '',
        oldValue: ''
      })
    },
    deleteItem(item, index) {
      this.form.item.splice(index, 1)
    },
    changeField(item) {
      // item.oldValue = this.row[item.field] ? this.row[item.field] : '暂无数据'
      item.fieldName = this.fields.find(v => v.key === item.field).label
    },
    changeTargetPerson(val) {
      const obj = this.persons.find(person => person.id === val)
      this.form.targetPersonName = obj.userName
    }

  },
  watch:{
    dialogShow(){
      if(!this.dialogShow){
        this.fields=[]
      }
    }
  }
}
</script>
