<template>
  <form-render label-width="120px" inine :fields="renderFields" :model="batchInstance" disabled/>
</template>
<script>
import {renderFields} from '../edit'

/**
 * 查档车详情页面
 */
export default {
  name: "batchDetail",
  props: {
    //档案车详情
    batchInstance: {type: Object}
  },
  data() {
    return {renderFields}
  }
}
</script>