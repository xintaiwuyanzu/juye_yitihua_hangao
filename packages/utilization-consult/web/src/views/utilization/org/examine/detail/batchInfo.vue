<template>
  <section style="display: inline-block;margin-left: 5px">
    <el-button @click="showBatch" type="primary">登记详情</el-button>
    <el-drawer title="查档登记详情" :visible.sync="drawerShow" direction="rtl" size="50%" append-to-body>
      <form-render label-width="120px" inine :fields="renderFields" :model="batchInfo" disabled/>
    </el-drawer>
  </section>
</template>
<script>
import abstractContainerItem from "@archive/utilization_search/src/components/searchIndex/abstractContainerItem";

const renderFields = {
  auditInfo: {
    fieldType: 'object',
    header: '审核信息',
    children: {
      batchName: {label: '查档编号', required: true},
      endDate: {label: '到期时间', fieldType: 'date'}
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
  concat: {
    fieldType: 'object',
    header: '基本信息',
    children: {
      phone: {label: '联系电话', required: true},
      receiveOrgId: {label: '受理单位', required: true,fieldType:'select',url:'organise/getAllDepartment',params:{page:false},labelKey:'organiseName'},
    }
  },
  material: {
    fieldType: 'object',
    header: '证明材料',
    children: {
      fileType: {label: '材料类型', fieldType: 'checkBox', singleLine: true, dictKey: 'materials'},
      id: {label: '材料收集', fieldType: 'file', multiple: true}
    }
  },
  result: {
    fieldType: 'object',
    header: '结果说明',
    children: {
      remarks: {label: '结果说明', type: 'textarea', singleLine: true}
    }
  }
}

/**
 * 查档登记详情
 */
export default {
  extends: abstractContainerItem,
  name: 'BatchInfo',
  props: {
    /**
     * 是否显示到期时间
     */
    showEndDate: {default: false}
  },
  data() {
    return {
      renderFields,
      drawerShow: false,
      loading: false,
      batchInfo: {}
    }
  },
  methods: {
    async showBatch() {
      this.drawerShow = true
      await this.checkLoad()
    },
    async checkLoad() {
      if (!this.batchInfo.id) {
        this.loading = true
        const {data} = await this.$post('utilization/consult/detail', {id: this.$route.query.id})
        this.batchInfo = data.data
        this.loading = false
      }
    },
    async $init() {
      if (this.searchContainer) {
        await this.checkLoad()
        this.searchContainer.resetQuery({keyWords: this.batchInfo.searchDescription}, true)
      }
    }
  }
}
</script>
