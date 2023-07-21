<template>
  <table-index path="manage/handoverConfig" :fields="fields" :insert="false" :edit="false" ref="table" :delete="false">
    <template v-slot:search-$btns>
      <config-add @loadData="loadData" ref="form"/>
    </template>
    <template v-slot:table-$btns="{row}">
      <el-button type="text" width="30" @click="toggle(row)">
        {{ row.status === '1' ? '暂停' : '启动' }}
      </el-button>
      <el-button type="text" width="30" @click="edit(row)"> 编辑</el-button>
    </template>
  </table-index>
</template>
<script>
  import ConfigAdd from "./configAdd";

  const datePatten = 'YYYY-MM-DD HH:mm:ss'

  export const statusMapper = {
    '1': {
      label: '运行中',
      data: {showType: 'success'}
    },
    '2': {
      label: '暂停',
      data: {showType: 'warning'}
    },
    '3': {
      label: '已办结',
      data: {showType: 'info'}
    },
  }

  export default {
    name: 'handOverConfig',
    components: {ConfigAdd},
    data() {
      return {
        fields: {
          createDate: {label: '配置年度', dateFormat: 'YYYY', width: 120, fieldType: 'date'},
          warningTime: {label: '预警时间', dateFormat: datePatten},
          handoverTime: {label: '移交时限', dateFormat: datePatten},
          allNum: {label: '到期数量', width: 120},
          sendNum: {label: '移交数量', width: 120},
          status: {label: '状态', width: 80, component: 'tag', mapper: statusMapper}
        }
      }
    },
    methods: {
      loadData() {
        this.$refs.table.reload()
      },
      async toggle(row) {
        try {
          await this.$confirm(`确定要${row.status === '1' ? '暂停' : '启动'}移交检测吗`, '提醒')
          const {data} = await this.$post('/manage/handoverConfig/toggle', {id: row.id})
          if (data.success) {
            this.$message.success('操作成功')
            await this.loadData()
          } else {
            this.$message.warning(data.message)
          }
        } catch (e) {
          console.log(e)
        }
      },
      edit(row) {
        this.$refs.form.showAdd(row)
      }
    }
  }
</script>