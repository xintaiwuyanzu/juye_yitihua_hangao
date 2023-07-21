<template>
  <section>
    <el-button type="primary" @click="showAdd">添加
    </el-button>
    <el-dialog width="80%" :title="title" :visible.sync="dialogShow" append-to-body>
      <archive-form :form-definition-id="formId" :form="form" ref="form"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">关 闭</el-button>
        <el-button type="primary" @click="save(false)" v-loading="loading1" class="btn-submit">保存并继续</el-button>
        <el-button type="primary" @click="save(true)" v-loading="loading2" class="btn-submit">保存并关闭</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";
import archiveForm from "../../archiveForm";

/**
 * 头部
 * 添加按钮
 */
export default {
  extends: abstractComponent,
  name: "add",
  computed: {
    title() {
      return `${this.form.id ? '编辑档案' : '添加档案'}`
    }
  },
  components: {archiveForm},
  data() {
    return {
      form: {},
      loading1: false,
      loading2: false,
      oldFormData: {}
    }
  },
  methods: {
    createDefaultForm() {
      return {
        FOND_CODE: this.fond.code,
        CATE_GORY_CODE: this.category.code,
        YEAR: this.$moment().format('yyyy'),
      }
    },
    //显示弹出框
    showAdd() {
      this.form = this.createDefaultForm()
      this.dialogShow = true
    },
    /**
     * 编辑方法
     */
    edit(row) {
      this.oldFormData = row
      this.form = Object.assign({}, row)
      this.dialogShow = true
    },
    /**
     * 组卷方法
     */
    group(row) {
      this.form = Object.assign({}, row)
      this.dialogShow = true
    },
    //保存方法
    async save(close) {
      if (this.form.TITLE === '' || this.form.TITLE == undefined) {
        this.$message.warning("请输入题名")
        return
      }
      if(this.form.ARCHIVE_CODE === ''||this.form.ARCHIVE_CODE == undefined){
        this.$message.warning("请输入档号")
        return
      }
      const valid = await this.$refs.form.validate()
      if (valid) {
        if (!close) {
          this.loading1 = true
        } else {
          this.loading2 = true
        }
        //准备参数
        const defaultParams = {
          //这几个参数是必填的
          formDefinitionId: this.formId,
          //全宗Id
          fondId: this.fond.id,
          //分类Id
          categoryId: this.category.id,
          //状态
          status: this.eventBus.defaultForm.status_info
        }
        const url = `/manage/formData/${this.form.id ? "updateFormData" : "insertFormData"}`
        const {data} = await this.$post(url, Object.assign(defaultParams, this.form, this.eventBus.defaultForm))
        if (data.success) {
          this.eventBus.$emit("loadData")
          this.$message.success("保存成功！")
          await this.realGroup(data.data)
          if (close) {
            this.dialogShow = false
          } else {
            this.createDefaultForm()
          }
        } else {
          this.$message.warning(data.message.replace("服务器错误：", ""))
        }
      } else {
        this.$message.error('请填写完整表单')
      }
      if (!close) {
        this.loading1 = false
      } else {
        this.loading2 = false
      }
    },
    async updateIndex(defaultParams) {
      const url = `/search/${this.form.id ? "updateIndexData" : "insertIndexData"}`
      const {data} = await this.$post(url, Object.assign(defaultParams, this.form, this.eventBus.defaultForm))
      if (!data.success) {
        this.$message.warning("更新搜索引擎数据出错：")
      }
    },
    realGroup(data) {
      if (this.form.ids) {
        this.$http.post('/manage/formData/groupDocument',
            {
              ajFormDefinitionId: this.form.ajFormDefinitionId,
              wjFormDefinitionId: this.form.wjFormDefinitionId,
              ajId: data.id,
              wjIds: this.form.ids,
              fondId: this.form.formId,
              categoryId: this.form.categoryId
            })
            .then(({data}) => {
              if (data.success) {
                this.childrenIndex.$emit("loadData")
                this.$message.success('组卷成功')
              } else {
                this.$message.error("组卷失败")
              }
            })
      }
    }
  },
  mounted() {
    //监听编辑事件
    this.eventBus.$on('edit', this.edit)
    //监听组卷事件
    this.eventBus.$on('group', this.group)
  },
  beforeDestroy() {
    this.eventBus.$off('edit', this.edit)
    this.eventBus.$off('group', this.group)
  }
}
</script>
