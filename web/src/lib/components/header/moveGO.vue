<template>
  <!--移动档案-->
  <section>
    <el-dropdown placement="bottom" trigger="click" @command="handleCommand">
      <el-button class="search-btn" type="primary" v-show="!this.parentIndex">移动<i class="el-icon-arrow-down el-icon--right"/></el-button>
      <el-dropdown-menu slot="dropdown">
        <el-dropdown-item v-if="currentSelect.length>0" command="select">移动选中</el-dropdown-item>
        <el-dropdown-item command="all">移动所有</el-dropdown-item>
        <el-dropdown-item command="query">移动查询</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <form ref="printForm" :action='action' method="post" target="_blank" style="display: none">
      <input name="fondId" :value="query.fondId"/>
      <input name="categoryId" :value="query.categoryId"/>
      <input name="formDefinitionId" :value="query.formDefinitionId"/>
      <input name="_QUERY" :value="query._QUERY"/>
      <input name="status" value="save"/>
    </form>
    <el-dialog width="40%" title="移动"
               :visible.sync="dialogShow"
               :close-on-click-modal=false
               :close-on-press-escape=false
               v-if="dialogShow">
      <div style="height: 60vh;overflow: auto">
        <category-tree style="flex:1"
                       ref="tree"
                       :fond-id="fond.id"
                       :show-check="false"
                       @check="getCategory"
                       :with-permission="false"/>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="resetForms">取 消</el-button>
        <el-button type="primary" @click="preSubmit" v-loading="loading">提 交</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

export default {
  extends: abstractComponent,
  name: 'filing',
  data() {
    return {
      targetPerson: '',
      dialogShow: false,
      newCategoryId: '',
      sendType: 'all',
      query: {},
      action: '',
      archiveType:'',
      oldCategoryId:''
    }
  },
  methods: {
    $init(){
      this.archiveType = this.category.archiveType
      this.oldCategoryId = this.category.id
    },
    resetForms() {
      this.newCategoryId = ''
      this.dialogShow = false
    },
    async preSubmit() {
      if (this.newCategoryId == '') {
        this.$message.warning('请选择门类')
        return
      }
      if (this.newCategoryId == this.oldCategoryId) {
        this.$message.warning('当前目录下已存在该文件')
        return
      }
      const query = this.eventBus.getQueryByQueryType(this.sendType)
      //todo 这里需要区分案卷  卷内件  件盒

      this.query = query
      const {data} = await this.$post('/manage/formData/updateCategoryByQuery', {
        ...query,
        status: 'other',
        formDefinitionId: query.formDefinitionId,
        newCategoryId: this.newCategoryId,
        oldCategoryId: this.oldCategoryId
      })
      if (data.success) {
        this.eventBus.$emit("loadData")
        this.$message.success('移动成功，请在对应门类查看结果！')
        this.newCategoryId = ''
      }
      this.dialogShow = false
    },
    getCategory(v) {
      let cateType = v.data.archiveType
      if(this.archiveType !== cateType){
        this.$message.warning("注意：只能移动相同类型的门类数据！")
        this.newCategoryId = ''
        return
      }else{
        //todo
        this.newCategoryId = v.id
      }

    },
    async handleCommand(command) {
      this.dialogShow = true
      this.sendType = command
      /*      this.sendType = command
            const query = this.eventBus.getQueryByQueryType(this.sendType)
            //todo 这里需要区分案卷  卷内件  件盒

            this.query = query


            const {data} = await this.$post('/manage/formData/updateStatusByQuery', {...query,status:'other',formDefinitionId:query.formDefinitionId})

            if(data.success){
              this.eventBus.$emit("loadData")
              this.$message.success('保存成功，请在管理库查看结果！')
            }*/

    },
    async showSend() {
      this.dialogShow = true
    },
    /**
     *
     * 执行提报操作
     * @returns {Promise<void>}
     */
    async doSend() {
      let ids = ''
      this.currentSelect.forEach(v => {
        ids = v.id + ","
      })
      ids = ids.substring(0, ids.length - 1)
      const {data} = await this.$post('/manage/formData/updateStatus', {
        ids,
        status: 'save',
        formDefinitionId: this.formId
      })
      if (data.success) {
        this.$message.success('保存成功，请在保存库查看结果！')
      }
    }
  }
}
</script>
