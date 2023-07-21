<template>
  <table-index path="/manage/handover" :fields="fields" :edit="false" :delete="false" :insert="false"
               :checkAble="true"
               ref="table">
  </table-index>
</template>

<script>
/**
 * 待移交批次
 */
export default {
  name: "handOver",
  data() {
    return {
      fields: {
        batchName: {
          label: '批次名称', search: true, route: true, component: 'text',
          routerPath: '/manage/handover/edit',
        },
        transferPersonName: {label: '创建人', width: 120},
        createDate: {label: '创建时间', width: 100, dateFormat: true},
        detailNum: {label: '档案数量', width: 100},
        sequence: {label: '载体起止顺序号'},
        batchType: {
          label: '记录类型',
          width: 100,
          component: 'tag',
          mapper: {
            delay: {label: '延期'},
            handOver: {label: '移交'}
          }
        },
        status: {
          label: '记录状态',
          width: 140,
          component: 'tag',
          mapper: {
            10: {label: '编辑中'},
            20: {label: '内部审核中'},
            21: {label: '审核不通过'},
            25: {label: '档案馆待接收'},
            30: {label: '档案馆审核中'},
            31: {label: '档案馆审核不通过'},
            40: {label: '已办结'}
          }
        }
      },
    }
  },
  methods: {}
}
</script>