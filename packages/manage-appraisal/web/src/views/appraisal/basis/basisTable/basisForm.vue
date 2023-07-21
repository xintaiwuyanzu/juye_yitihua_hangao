<template>
  <section>
    <div class="index_main">
        <el-form ref="form" :model="form" label-width="80px">
          <el-form-item >
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="包含身份证信息（身份证号码、证件号码等信息）" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item >
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="包含电话号码（手机号、电话号码等信息）" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item >
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="包含家庭住址（住址、地址等信息）" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item >
            <el-checkbox-group v-model="form.type">
              <el-checkbox label="包个人履历（出生日期、就读学校、毕业于某所学校、供职于某处单位等）" name="type"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
        </el-form>
    </div>
  </section>

</template>
<script>

export default {
  data() {
    return {
      form:{type:[]},
      defaultInsertForm: {},
      defaultSearchForm: {},
    }
  },
  methods: {
    loadData() {}
  }
}
</script>

