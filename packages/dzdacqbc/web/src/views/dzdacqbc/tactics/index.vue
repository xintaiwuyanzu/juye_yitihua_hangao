<template>
  <table-index :fields="fields" path="/earchive/tactics" ref="table">
  </table-index>
</template>
<script>
export default {
  data() {
    return {
      fields: {
        tacticsName: {label: '策略名称', search: true},
        globalDetection: {label: '全局检测周期(天)', width: 120,},
        sampleDetection: {label: '抽样检测周期(天)', width: 120,},
        sampleProportion: {label: '抽样检测比例(%)', width: 120,},
        remindBackups: {label: '提醒离线备份周期(天)', width: 140},
        alarmValue: {label: '存储报警阈值(%)', width: 120},
        isDefault: {label: '是否默认', fieldType: 'radio', width: 80, mapper: {1: '是', 0: '否'}}
      }
    }
  }
}
</script>