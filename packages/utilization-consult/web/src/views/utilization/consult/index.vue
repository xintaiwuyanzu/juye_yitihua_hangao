<template>
  <table-index path="utilization/consult" :fields="columns" :edit="false" :delete="false" :insert="false"
               :defaultSearchForm="defaultSearchForm"
               title="查档登记">
    <el-table-column align="center" label="查档编号" prop="batchName">
      <template v-slot="{row}">
        <el-button type="text" @click="routeDetail(row)">{{ row.batchName }}</el-button>
      </template>
    </el-table-column>
    <template v-slot:search-$btns>
      <el-button type="primary" @click="routeAdd">查档登记</el-button>
      <el-button type="primary" v-if="hasRole('aaa')">推送原文</el-button>
    </template>
    <template v-slot:table-$btns="{row}">
      <el-button v-show="row.status!='2'" type="text" @click="routeSearch(row)" width="40">查档</el-button>
      <el-button type="text" @click="routeDetail(row)" width="40">详情</el-button>
    </template>
  </table-index>
</template>
<script>
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";
import {consultRole} from "./constants";

/**
 * 查档列表页面
 */
export default {
  name: "consultIndex",
  data() {
    return {
      columns: {
        batchName: {label: '查档编号', search: true},
        userName: {
          label: '姓名', search: true, width: 120
          //, role: consultRole
        },
        phone: {
          label: '联系电话', width: 100
          //, role: consultRole
        },
        idNo: {
          label: '有效证件号', width: 120
          //, role: consultRole
        },
        searchDescription: {label: '查阅内容', dictKey: 'content', width: 140},
        useWay: {label: '利用情形', dictKey: 'inquire', width: 140},
        useFor: {label: '利用目的', dictKey: 'utilize', width: 140},
        detailNum: {label: '查档数量', width: 100},
        other: {label: '其他'},
        status: {
          label: '状态', width: 80, fieldType: 'select', component: 'tag',
          mapper: [{
            id: '0',
            label: '待查档'
          }, {
            id: '1',
            label: '正在查档'
          }, {
            id: '2',
            label: '已完成'
          }],
        }
      },
      defaultSearchForm: {sourceType: 'person'}
    }
  },
  setup() {
    return useArchiveCar()
  },
  methods: {
    routeAdd() {
      this.$router.push(`${this.$route.path}/edit`)
    },
    /**
     * 跳转查档页面
     * @param row
     */
    routeSearch(row) {
      this.archiveCarData.archiveCar.id = row.id
      this.$router.push({
        path: `/utilization/search`,
        query: {keyword: row.userName}
      })
    },
    /**
     * 跳转详情页面
     * @param row
     */
    routeDetail(row) {
      this.$router.push({
        path: `${this.$route.path}/detail`,
        query: {id: row.id,isHidden:row.status}
      })
    }
  }
}
</script>
