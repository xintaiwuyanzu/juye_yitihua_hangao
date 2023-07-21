<template>
  <section>
    <nac-info back title="查档登记">
      <el-button type="primary"  @click="checkSave()">保存并申请</el-button>
    </nac-info>
    <div class="index_main">
      <form-render :fields="fields" :model="form" :inline="true" label-width="120px" ref="form"/>
    </div>
  </section>
</template>
<script>
/**
 * 查档登记页面
 */
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";
import {consultRole, nationGroup} from '../constants'

export default {
  data() {
    return {
      consultRole,
      fields: renderFields,
      form: {
        ethnic: '汉族',
        idType: 'identityCard',
        fileType: [],
        status:0,
        sourceType:'org'
      },
    }
  },
  setup() {
    return useArchiveCar()
  },
  methods: {
    async checkSave() {
      const result = await this.$refs.form.submit('/utilization/consult/insert')
      if (result.success) {
        this.$message.success('保存成功')
        await this.$router.push({path: '/utilization/org/apply'})
      } else {
        this.$message.error(result.message)
      }
    }
  }
}
export const renderFields = {
  concat: {
    fieldType: 'object',
    header: '基本信息',
    role: consultRole,
    children: {
      phone: {label: '联系电话', required: true},
      receiveOrgId: {label: '受理单位',filterable:true, required: true,fieldType:'select',url:'organise/getAllDepartment',params:{page:false},labelKey:'organiseName'},
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
