<template>
  <el-dialog title="搜索条件"
             :visible.sync="show"
             v-loading="loading">
    <el-row style="height: 400px;margin-top: 5px">
      <el-col :span="6">
        <fond-tree ref="fondTree" @check="formCategoryCheck"></fond-tree>
      </el-col>
      <el-col :span="17" :offset="1">
        <form-render label-width="110px" :fields="fields" ref="form" :model="form"></form-render>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button type="info" @click="cancelDialog" class="btn-cancel">取 消</el-button>
      <el-button type="primary" @click="confirmSearch">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import indexMixin from '@dr/auto/lib/util/indexMixin'
import formMixin from "@dr/auto/lib/util/formMixin"

export default {
  mixins: [indexMixin, formMixin],
  data() {
    return {
      show: false,
      fields: {
        fondName: {label: '全宗', disabled: true},
        categoryName: {label: '门类', disabled: true},
        vintages: {label: '年度'},
        archiveCode: {label: '档号'},
        title: {label: '题名'},
        appraisalType: {
          label: '鉴定类型',
          fieldType: 'select',
          mapper: [{
            id: 'KFJD',
            label: '开放鉴定'
          }, {
            id: 'DQJD',
            label: '销毁鉴定'
          }]
        }
      },
      fondCode: '',
      categoryCode: ''
    }
  },

  methods: {
    formCategoryCheck(node) {
      this.form.fondName = this.$refs.fondTree.currentFond.name
      this.fondCode = this.$refs.fondTree.currentFond.code
      this.form.categoryName = node.label
      this.categoryCode = node.data.code
    },
    cancelDialog() {
      this.show = false
      this.form = {}
    },
    confirmSearch() {
      this.form.fondCode = this.fondCode
      this.form.categoryCode = this.categoryCode
      if (this.form.appraisalType == 'KFJD') {
        this.form.isOpen = '1'
        this.form.isExpire = ''
      } else if (this.form.appraisalType == 'DQJD') {
        this.form.isOpen = ''
        this.form.isExpire = '1'
      } else {
        this.form.isExpire = ''
        this.form.isOpen = ''
      }
      this.$emit('search', this.form)
      this.cancelDialog()
    }
  }
}
</script>

