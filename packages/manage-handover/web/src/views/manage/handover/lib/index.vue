<template>
  <table-index path="/manage/handoverLib" :fields="fields" :edit="false" :delete="false" :insert="false"
               :checkAble="true"
               ref="table">
    <template v-slot:search-$btns>
      <el-button type="primary" @click="selectCheck(true)">移交申请</el-button>
      <!--<el-button type="primary" @click="selectCheck(false)">延期申请</el-button>-->
    </template>
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
          label: '批次名称',
          search: true,
          route: true,
          component: 'text',
          routerPath: '/manage/handover/detail',
          queryProp: ['status','id']
        },
        fondName: {label: '全宗'},
        archiveYear: {label: '档案年度', width: 100},
        createDate: {label: '创建时间', dateFormat: "YYYY-MM-DD HH:mm:ss", width: 150},
        detailNum: {label: '档案数量', width: 100},
        status: {
          width: 100,
          label: '移交状态',
          component: 'tag',
          showTypeKey: 'show',
          search: true,
          fieldType: 'select',
          mapper: {
            9: {label: '检测中', show: 'danger'},
            10: {label: '待移交', show: 'warning'},
            11: {label: '移交审核中', show: 'primary'},
            12: {label: '移交审核通过', show: 'success'},
            21: {label: '延期审核中', show: 'primary'},
            22: {label: '已延期', show: 'success'},
          }
        }
      },
    }
  },
  methods: {
    /**
     * 移交或者延期
     * @param ishand
     * @return {Promise<void>}
     */
    async selectCheck(ishand) {
      const select = this.$refs.table.tableSelection
      const name = ishand ? '移交' : '延期'
      if (select.length === 0) {
        this.$message.warning(`请选择要${name}的批次`)
        return
      }
      const canNotHand = select.filter(d => d.status !== '10')
      if (canNotHand.length > 0) {
        this.$message.warning(`选中的批次不能发起${name}申请`)
        return
      }
      const fondIds = select.reduce((set, d) => set.add(d.fondId), new Set())
      if (fondIds.size > 1) {
        this.$message.warning(`只能同时${name}一个全宗`)
        return
      }
      try {
        await this.$confirm(`确定要${name}选中的批次吗`, '提示')
        const loading = this.$loading({text: `${name}中`})
        const {data} = await this.$post('/manage/handover/newBatch', {
          libIds: select.map(d => d.id).join(','),
          fondId: Array.from(fondIds)[0],
          type: ishand ? 'handOver' : 'delay'
        })
        if (data.success) {
          this.$message.success('提交成功！')
          loading.close()
          await this.$router.push({
            path: '/manage/handover/edit',
            query: {id: data.data.id}
          })
        } else {
          this.$message.warning(data.message)
          loading.close()
        }
      } catch (e) {
        console.log(e)
      }
    }
  }
}
</script>
