<template>
  <table-index path="utilization/consult/user" :fields="columns" :edit="false" :delete="false" :insert="false"
               title="外部人员查档记录">
    <el-table-column align-="center" label="查档编号" prop="batchName">
      <template v-slot="{row}">
        <el-button type="text" @click="routeDetail(row)">{{ row.batchName }}</el-button>
      </template>
    </el-table-column>
    <template v-slot:table-$btns="{row}">
      <el-button type="text" @click="routeDetail(row)" width="40">详情</el-button>
    </template>
  </table-index>
</template>
<script>
/**
 * 查档列表页面
 */
export default {
  name: "consultIndex",
  data() {
    return {
      columns: {
        batchName: {label: '查档编号', search: true,},
        userName: {label: '姓名', search: true, width: 120},
        phone: {label: '联系电话', width: 100},
        idNo: {label: '有效证件号', width: 120},
        searchDescription: {label: '查阅内容', dictKey: 'content', width: 140},
        useWay: {label: '利用情形', dictKey: 'inquire', width: 140},
        useFor: {label: '利用目的', dictKey: 'utilize', width: 140},
        detailNum: {label: '查档数量', width: 100}
      }
    }
  },
  methods: {
    /**
     * 跳转详情页面
     * @param row
     */
    routeDetail(row) {
      this.$router.push({
        path: `detail`,
        query: {id: row.id}
      })
    }
  }
}
</script>
