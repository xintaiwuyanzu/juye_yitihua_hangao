<template>
  <process-container
      :btn-text="TName"
      :before-open="loadCar"
      :process-type="carInfo.batchType"
      :before-save="appendParams"
      :business-id="batchId"
      @saved="notifyResult">
    <template v-slot:form="{form}">
      <el-form-item prop="type" label="选择标记" required>
        <select-async v-model="form.type" :mapper="typeMapper" placeholder="请选择要发送的档案标记"/>
      </el-form-item>
      <el-form-item prop="endDate" label="到期时间" required>
        <el-date-picker v-model="form.endDate" placeholder="请选择到期时间" value-format="timestamp"/>
      </el-form-item>
    </template>
  </process-container>
</template>
<script>
import {processContainer} from '@dr/process/src/lib'

/**
 * 启动流程按钮
 */
export default {
  name: "guidanceApplication",
  components: {processContainer},
  props: {
    /**
     * 档案车Id
     */
    batchId: {type: String}
  },
  data() {
    return {

      TName:'业务指导申请',
      typeMapper: {
        all: '所有数据'
      },
      /**
       * 档案车详情
       */
      carInfo: {},
      /**
       * 是否加载过标记数据
       */
      tagLoaded: false
    }
  },
  methods: {
    /**
     * 追加启动数据
     * @param form
     */
    appendParams(form) {
      this.$set(form, 'batchId', this.batchId)
    },
    /**
     *提示返回消息
     * @param data
     */
    notifyResult(data) {
      if (data.success) {
        this.$message.success('启动成功')


      } else {
        this.$message.error(data.message)
      }
    },
    /**
     * 加载档案车详情
     * @returns {Promise<void>}
     */
    async loadCar(form) {
      this.$set(form, 'type', 'all')
      this.$set(form, 'endDate', this.$moment().add(7, 'd').toDate().getTime())
      this.$set(form, '')
      if (!this.carInfo.id) {
        const {data} = await this.$post('/archiveCarBatch/detail', {id: this.batchId})
        if (data.success) {
          this.carInfo = data.data
        }
      }

      //加载档案车标记类型
      if (!this.tagLoaded) {
        const {data} = await this.$post('/archiveCarBatch/archiveCarTag', {
          carType: this.carInfo.batchType,
          withDynamic: true
        })
        data.data.forEach(d => this.typeMapper[d.code] = d.name)
        this.tagLoaded = true
      }
    }
  }
}
</script>