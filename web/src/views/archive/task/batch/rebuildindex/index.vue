<template>
  <table-index path="rebuildIndexRecord"
               ref="table"
               :fields="fields"
               :edit="false"
               :insert="false"
               :delete="true">
    <template v-slot:search="form">
      <el-form-item label="开始时间范围：" prop="startTime">
        <el-date-picker
            v-model="form.startTime"
            type="date"
            value-format="timestamp"
            placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item prop="endTime">
        <el-date-picker
            v-model="form.endTime"
            type="date"
            value-format="timestamp"
            placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
    </template>
  </table-index>
</template>
<script>

/**
 * 批次详情列表
 */
export default {
  data() {
    return {
      fields: {
        formCode: {label: '表单编码', search: false},
        formName: {label: '表单名称', search: true},
        rebuildType: {
          label: '重建类型', width: 100, component: 'tag', showTypeKey: 'show',
          fieldType: 'select', search: false,
          mapper: {
            '0': {label: '复制重建'},
            '1': {label: '删除重建'},
          },
        },
        startTime: {dateFormat: 'YYYY-MM-DD HH:mm:ss', label: '开始时间', width: 150},
        endTime: {dateFormat: 'YYYY-MM-DD HH:mm:ss', label: '结束时间', width: 150},
        createPersonName: {label: '操作人', width: 200, search: true},
        status: {
          label: '状态', width: 80, component: 'tag', showTypeKey: 'show',
          mapper: {
            '0': {label: '失败', show: 'danger'},
            '1': {label: '成功', show: 'success'},
          },
        },
      },
    }
  },
}
</script>
