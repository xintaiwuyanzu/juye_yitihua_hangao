<template>
  <table-index :fields="fields" path="earchive/batch" :insert="false" :edit="false" :delete="false" ref="table"
               :defaultSearchForm="defaultSearchForm">
    <template v-slot:table-$btns="{row}">
      <el-button type="text" @click="getClientUrl" size="mini" width="30" v-show="row.deliveryStatus==='1'">下载
      </el-button>
    </template>
  </table-index>
</template>
<script>
import {encode} from 'js-base64';
import {useUser} from "@dr/framework/src/hooks/userUser";
import util from "@dr/framework/src/components/login/util";

export default {
  setup() {
    return useUser();
  },
  data() {
    return {
      //新代码
      fields: [
        {
          prop: 'batchName',
          label: '批次名称',
          align: 'left',
          component: 'text',
          search: true,
          route: true,
          routerPath: '/dzdacqbc/delivery/detail',
          queryProp: this.routeQuery
        },
        {
          prop: 'batchType',
          label: '批次类型',
          width: "80",
          search: true,
          mapper: {in: '入库', out: '出库'},
          fieldType: 'select',
          component: 'tag'
        },
        {prop: 'startDate', label: '开始时间', width: "140", dateFormat: 'YYYY-MM-DD HH:mm:ss'},
        {prop: 'detailNum', label: '档案数量', width: "80"},
        {
          prop: 'deliveryStatus', label: '出库状态', width: "80",
          mapper: {
            '0': {label: '待出库', show: ''},
            '1': {label: '已出库', show: 'success'}
          },
          component: 'tag'
        },
        // {prop: 'deliveryPerson', label: '出库人', width: "120"},
        {prop: 'deliveryTime', label: '出库时间', width: "140"},
        {prop: 'beizhu', label: '备注'}
      ],
      defaultSearchForm: {flag: "delivery", categoryId: ''}
    }
  },
  methods: {
    routeQuery(row) {
      return {batchId: row.id, batchType: row.batchType, deliveryStatus: row.deliveryStatus}
    },
    async getClientUrl() {
      const {data} = await this.$post('/receive/offline/getClientUrl')
      if (data.success) {
        this.openDataClient(data.data)
      }
    },
    openDataClient(val) {
      let path = encode(val + '/dataoperationtools.html/#/login?token=' + util.getCookie('dauth') + '&userId=' + this.user.id + '&menuPath=/dataClient/outRecord')
      if (process.env.NODE_ENV === 'production') {
        val = location.origin
        path = encode(val + '/dataoperationtools/index.html#/login?token=' + util.getCookie('dauth') + '&userId=' + this.user.id + '&menuPath=/dataClient/outRecord')
      }
      window.open('dc://openUrl?url=' + path + '&store=true')
    }
  }
}
</script>
