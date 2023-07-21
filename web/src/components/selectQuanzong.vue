<template>
  <el-select :value="id" style="width: 100%" v-model="label" :placeholder="placeholder" @change="change" clearable
             filterable>
    <el-option
        v-for="item in fonds"
        :key="item.id"
        :label="item.name"
        :value="item.id">
    </el-option>
  </el-select>
</template>

<script>

export default {
  props: {
    id: {type: String},
    placeholder: {type: String},
    quanxian: {default: false, type: Boolean},
  },
  model: {event: 'selected', prop: 'id'},
  data() {
    return {
      fonds: [],
      label: ''
    }
  },
  methods: {
    change(v) {
      if (v) {
        this.label = this.fonds.find(d => d.id === v).name
      } else {
        this.label = ''
      }
      this.$emit('selected', v)
    }
  },
  mounted() {
    let url = '/manage/fond/page'
    let page = false
    if (this.quanxian) {
      url = '/manage/fond/getFondListBypermission'
    }
    this.$http.post(url, {page: page})
        .then(({data}) => {
          if (data.success) {
            if (this.quanxian) {
              this.fonds = data.data
            } else {
              let all = {
                id: '',
                name: '全库'
              }
              this.fonds = [all]
              this.fonds = this.fonds.concat(data.data)
            }
          }
        })
  }
}
</script>
