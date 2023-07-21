<template>
  <section>
    <nac-info back/>
    <el-card style="padding-top: 20px">
      <el-form :model="atlas" :rules="rules" ref="atlasForm">
        <div v-show="active===0">
          <el-form-item :label-width="width" label="图册名称" prop="atlasName">
            <el-input v-model="atlas.atlasName"></el-input>
          </el-form-item>
          <el-form-item :label-width="width" label="主题" prop="atlasTitle">
            <el-input v-model="atlas.atlasTitle"></el-input>
          </el-form-item>
          <el-form-item :label-width="width" label="关键词" required>
            <el-select multiple filterable clearable placeholder="选择关键词" style="width: 100%" v-model="values">
              <el-option
                  :key="item.id"
                  :label="item.tagName"
                  :value="item.id"
                  v-for="item in keywords">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label-width="width" label="描述">
            <el-input type="textarea" v-model="atlas.atlasMark"></el-input>
          </el-form-item>
        </div>
        <el-form-item style="display: flex;justify-content: center" v-slot="footer">
          <el-button @click="complete" type="primary">完成</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      id: '',
      active: 0,
      atlas: {atlasDirection: '2', atlasMake: '1', atlasType: '实记类'},
      width: '120px',
      keywords: [],
      values: [],
      archives: [
        {tm: '题名', dh: '档号', qzh: '全宗号', flh: '分类号', nd: '年度'}
      ],
      rules: {
        atlasName: [{required: true, trigger: 'blur', message: '请输入图册名称'}],
        atlasTitle: [{required: true, trigger: 'blur', message: '请输入图册主题'}]
      }
    }
  },
  methods: {
    $init() {
      this.getTag()
    },
    getTag() {
      this.$http.post('/tagLib/selectTagList').then(({data}) => {
        if (data.success) {
          this.keywords = data.data
        } else {
          this.$message.error(data.message)
        }
      })
    },
    complete() {
      this.atlas.atlasKeyWord = this.values.toString()
      this.$post('/atlas/insert', this.atlas).then(({data}) => {
        if (data && data.success) {
          this.$router.back()
        }
      })
    },
    next() {
      this.$refs.atlasForm.validate(valid => {
        if (valid) {
          this.active++
        }
      })
    }
  }
}
</script>

<style scoped>

</style>