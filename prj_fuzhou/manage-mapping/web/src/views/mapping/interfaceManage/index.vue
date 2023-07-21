<template>
  <table-index :fields="fields" path="interface"></table-index>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        fields: {
          interfaceName: {label: '接口名称', search: true},
          interfaceApi: {label: '接口地址'},
          interfaceType: {label: '请求类型'},
          status: {
            label: '状态',
            showTypeKey: 'show',
            mapper: {'0': {label: '正常', show: 'primary'}, '1': {label: '异常', show: 'danger'}},
            component: 'tag'
          },
          createDate: {label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', edit: false}
        }
      }
    }
  }
</script>