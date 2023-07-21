<template>
  <section>
    <el-row>
      <el-col :span="24">
        <el-form-item label="题名" prop="title" required>
          <el-input v-model="management.title" clearable :disabled="detail"/>
        </el-form-item>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="8">
        <el-form-item label="责任人" prop="personLiable" required>
          <el-input v-model="management.personLiable" clearable :disabled="detail"/>
        </el-form-item>
        <el-form-item label="保管期限" prop="retentionPeriod" required>
          <el-input v-model="management.retentionPeriod" @keyup.native="keyUp" clearable :disabled="detail"/>
        </el-form-item>
        <el-form-item label="文件时间" prop="fileTime" required>
          <el-input v-model.number="management.fileTime" maxlength="8" clearable :disabled="detail"/>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="起始年度" prop="vintagesStart" required>
          <el-date-picker
              v-model="management.vintagesStart"
              format="yyyy"
              value-format="yyyy"
              type="year"
              placeholder="开始"
              clearable
              :picker-options="pickerStartAuditYear" style="width: 100%" :disabled="detail"/>
        </el-form-item>
        <el-form-item label="件号">
          <el-input v-model="management.pieceNumber" clearable :disabled="detail"/>
        </el-form-item>
        <el-form-item label="页数" prop="totalNumberOfPages">
          <el-input v-model="management.totalNumberOfPages" clearable :disabled="detail"/>
        </el-form-item>
      </el-col>
      <el-col :span="8">
        <el-form-item label="终止年度" prop="vintagesEnd" required>
          <el-date-picker
              v-model="management.vintagesEnd"
              format="yyyy"
              value-format="yyyy"
              type="year"
              placeholder="结束"
              clearable
              :picker-options="pickerEndAuditYear" style="width: 100%" :disabled="detail"/>
        </el-form-item>
        <el-form-item label="材料编号" prop="archive_code">
          <el-input v-model="management.archive_code" clearable :disabled="detail"/>
        </el-form-item>
        <el-form-item label="盒号">
          <el-input v-model="management.boxNumber" clearable :disabled="detail"/>
        </el-form-item>
      </el-col>
    </el-row>
  </section>
</template>

<script>
export default {
  name: "baseInfo",
  data() {
    return {
      // year日期选择器  开始年度和结束年度 添加限制
      pickerStartAuditYear: {
        disabledDate: time => {
          if (this.management.vintagesEnd) {
            return time.getFullYear() > this.management.vintagesEnd
          }
        }
      },
      pickerEndAuditYear: {
        disabledDate: time => {
          return time.getFullYear() < this.management.vintagesStart
        }
      },
    }
  },
  props: {management: {type: Object}, detail: {type: Boolean, default: false}},
  methods: {
    //限制输入特殊字符
    keyUp(e) {
      e.target.value = e.target.value.replace(/[`~!@#$%^&*()_\-+=<>?:"{}|,./;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]/g, '');
    },
  }
}
</script>