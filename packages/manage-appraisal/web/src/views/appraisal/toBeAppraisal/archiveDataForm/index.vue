<template>
    <div style="height: 20px;">
    <el-form  label-position="right" ref="formData" v-model="formData" label-width="100px" size="mini">
      <el-row>
        <el-col :span="24">
          <el-form-item style="margin-bottom: 2px">
            <label slot="label">题&#12288;&#12288;名：</label>
            <span>{{formData.TITLE}}</span>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item  style="margin-bottom: 2px">
            <label slot="label">档&#12288;&#12288;号：</label>
            {{formData.ARCHIVE_CODE}}
          </el-form-item>
        </el-col>

      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="文件编号：" style="margin-bottom: 2px">
            {{ formData.FILECODE }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="保管期限：" style="margin-bottom: 2px">
            {{ formData.SAVE_TERM }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item  style="margin-bottom: 2px">
            <label slot="label">年&#12288;&#12288;份：</label>
            {{ formData.VINTAGES }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item  style="margin-bottom: 2px">
            <label slot="label">密&#12288;&#12288;级：</label>
            {{ formData.S_LEVEL }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item  style="margin-bottom: 2px">
            <label slot="label">责&#8194;任&#8194;者：</label>
            {{ formData.DUTY_PERSON }}
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件时间：" style="margin-bottom: 2px">
            {{ formData.FILETIME }}
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item style="margin-bottom: 2px">
            <label slot="label">备&#12288;&#12288;注：</label>
            <span>{{ formData.NOTE }}</span>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    </div>
</template>

<script>

export default {
  data() {
    return {
    }
  },
  props:{
    formDefinitionId:'',
    formDataId:'',
    formData: {}
  }
}
</script>
