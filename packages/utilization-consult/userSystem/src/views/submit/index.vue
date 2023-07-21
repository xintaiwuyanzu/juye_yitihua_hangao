<template>
  <section>
    <nac-info title="查档登记">
      <el-button type="primary">刷卡</el-button>
      <el-button type="primary" @click="checkSave()">提交申请</el-button>
    </nac-info>
    <div class="index_main">
      <form-render :fields="fields" :model="form" :inline="true" label-width="120px" ref="form">
      </form-render>
    </div>
  </section>
</template>
<script>
import nationGroup from './nationGroup.json'
import {AbstractUser} from "../../useUser";

/**
 * 查档登记页面
 */
export default {
  extends: AbstractUser,
  data() {
    return {
      fields: renderFields,
      form: {
        ethnic: '汉族',
        idType: 'identityCard',
        fileType: []
      },
    }
  },
  methods: {
    async checkSave() {
      const result = await this.$refs.form.submit('/utilization/consult/insert')
      if (result.success) {
        this.$message.success('保存成功')
      } else {
        this.$message.error(result.message)
      }
    },
    $init() {
      this.form = Object.assign(this.form, this.user)
    }
  }
}
export const renderFields = {
  userInfo: {
    fieldType: 'object',
    header: '用户基本信息',
    children: {
      userName: {label: '姓名', required: true},
      birthday: {label: '出生日期', fieldType: 'date', required: true},
      ethnic: {label: '民族', fieldType: 'select', mapper: nationGroup, filterable: true},
      sex: {label: '性别', fieldType: 'radio', dictKey: 'gender'},
      idNo: {label: '有效证件号', required: true, newLine: true},
      valid: {label: '有效期限', fieldType: 'date', required: true},
      agency: {label: '签发机关'},
      idType: {label: '证件类型', fieldType: 'radio', singleLine: true, dictKey: 'certificate'}
    }
  },
  concat: {
    fieldType: 'object',
    header: '联系信息',
    children: {
      phone: {label: '联系电话', required: true},
      address: {label: '住址'}
    }
  },
  useInfo: {
    fieldType: 'object',
    header: '利用信息',
    children: {
      searchDescription: {label: '查阅内容', fieldType: 'radio', dictKey: 'content', filterable: true, required: true},
      useWay: {label: '利用情形', fieldType: 'radio', dictKey: 'inquire', filterable: true, required: true},
      useFor: {label: '利用目的', fieldType: 'radio', dictKey: 'utilize', filterable: true, required: true},
      other: {label: '其他', type: 'textarea', singleLine: true}
    }
  },
  material: {
    fieldType: 'object',
    header: '证明材料',
    children: {
      fileType: {label: '材料类型', fieldType: 'checkBox', singleLine: true, dictKey: 'materials'},
      id: {label: '材料收集', fieldType: 'file', multiple: true}
    }
  }
}
</script>
