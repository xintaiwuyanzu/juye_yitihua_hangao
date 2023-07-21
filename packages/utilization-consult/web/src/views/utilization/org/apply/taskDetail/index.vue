<template>
  <section class="task-detail">
    <section class="breadcrumb-container">
      <el-radio-group v-model="active">
        <el-radio-button label="detail">登记详情</el-radio-button>
        <el-radio-button label="archive">档案详情</el-radio-button>
        <el-radio-button label="history">流程意见</el-radio-button>
      </el-radio-group>
      <section class="slot">
        <section style="display: inline-block" v-show="active==='archive'">
          <el-button type="primary" :loading="loading" @click="loadNext(true)" v-show="page.index>1">上一条</el-button>
          <el-button type="primary" :loading="loading" @click="loadNext(false)" v-show="page.index<page.total">下一条
          </el-button>
          <el-button type="primary" :loading="loading" @click="addMore">追加查档</el-button>
          <el-tag type="primary" style="margin-left: 5px">{{ page.index }}/{{ page.total }}</el-tag>
          <el-button type="primary" @click="showDrawer">查看所有</el-button>
        </section>
        <task-button :task-instance-id="taskId"/>
        <el-button type="primary" @click="$router.back()">返回</el-button>
      </section>
    </section>
    <div class="index_main" v-loading="loading">
      <batch-detail :batch-instance="batchInstance" v-show="active==='detail'"/>
      <items v-show="active==='archive'" :form-data="formData" :detail="detail" :task-id="taskId"
             @next="loadNext(false)"/>
      <task-history-container :process-instance-id="$route.query.processInstanceId" v-show="active==='history'"/>
    </div>
    <item-index :batch-id="batchInstance.id" ref="detailIndex" @itemSelect="itemSelect"/>
  </section>
</template>
<script>
import BatchDetail from "./batchDetail";
import Items from "./items";
import ItemIndex from "./itemIndex";
import TaskButton from "./taskButton";
import {taskHistoryContainer} from '@dr/process/src/lib'
import {useMenu} from "@dr/framework/src/hooks/useMenu";
import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";

/**
 * 查档审核页面
 */
export default {
  name: 'taskDetail',
  components: {TaskButton, ItemIndex, Items, BatchDetail, taskHistoryContainer},
  data() {
    return {
      //环节实例Id
      taskId: this.$route.query.taskId + '',
      //加载状态
      loading: false,
      active: 'detail',
      //表单数据
      formData: {},
      //当前查档
      detail: {},
      /**
       * 查档批次
       */
      batchInstance: {},
      //分页查询数据
      page: {
        index: 0,
        total: 0
      },
      //查询表单
      searchForm: {}
    }
  },
  watch: {
    active(n) {
      if (n === 'archive' && !this.detail.id) {
        this.loadNext()
      }
    }
  },
  setup() {
    const {setName} = useMenu()
    setName('查档登记审核', '/utilization/consult/taskDetail')
    return useArchiveCar()
  },
  methods: {
    showDrawer() {
      this.$refs.detailIndex.showDrawer()
    },
    /**
     * 数据选择事件
     * @param params
     */
    async itemSelect(params) {
      this.page.index = params.index + params.page.index
      this.page.total = params.page.total
      this.detail = params.row
      this.searchForm = Object.assign({}, params.search)
      await this.loadFormData()
    },
    /**
     *加载下一个详情数据
     * @param isPre
     * @returns {Promise<void>}
     */
    async loadNext(isPre = false) {
      if (!isPre) {
        if (this.page.index >= this.page.total && this.page.total > 0) {
          this.$message.warning('已经是最后一条了')
          return
        }
      }
      let pageIndex = this.page.index - 1
      pageIndex = isPre ? pageIndex - 1 : pageIndex + 1
      if (pageIndex < 0) {
        pageIndex = 0
      }
      this.loading = true
      let {data} = await this.$post('/utilization/consult/details/page', {
        pageIndex,
        pageSize: 1,
        batchId: this.batchInstance.id,
        ...this.searchForm
      })
      data = data.data
      this.page.index = data.start + 1
      this.page.total = data.total
      this.detail = data.data[0]
      await this.loadFormData()
      this.loading = false
    },
    async loadDetail(id) {
      this.loading = true
      const {data} = await this.$post('/utilization/consult/details/detail', {id})
      this.detail = data.data
      await this.loadFormData()
      this.loading = false
    },
    async loadFormData() {
      const formData = await this.$http.post('manage/formData/detail?', {
        formDefinitionId: this.detail.formDefinitionId,
        formDataId: this.detail.formDataId
      })
      this.formData = formData.data.data
    },
    //追加查档
    addMore() {
      this.archiveCarData.archiveCar.id = row.id
      this.$router.push({
        path: `/utilization/search`,
        query: {keyword: row.userName}
      })
    },
    async $init() {
      const businessId = this.$route.query.businessId
      if (businessId) {
        const {data} = await this.$post('/utilization/consult/detail', {id: businessId})
        this.batchInstance = data.data
      } else {
        this.$message.error('查询查档详情失败')
        this.$router.back()
      }
    }
  }
}
</script>
<style lang="scss">
.task-detail {
  .metaView {
    flex: 1;
  }
}
</style>