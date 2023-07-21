<template>
  <select-async v-model="categoryType" placeholder="请选择文书类型" :mapper="categoryOptions" labelKey="description"
                valueKey="description"/>
</template>

<script>
export default {
  name: "getCategory",
  data() {
    return {
      categoryType: '',
      categoryOptions: []
    }
  },
  methods: {
    $init() {
      this.getFonds();
    },
    //获取当前登陆人的全宗
    async getFonds() {
      const {data} = await this.$post('/sysResource/personResource', {type: 'fond'})
      await this.getCategory(data.data[0].id)
    },
    //获取门类
    async getCategory(fondId) {
      if (fondId) {
        const result = await this.$post('/member_authority/getCategoryByFondId', {fondId: fondId})
        if (result) {
          result.data.data.forEach(i => {
            if (i.description != null && i.description !== "") {
              this.categoryOptions.push(i)
            }
          })
        }
      }
    },
  },
  watch: {
    categoryType(v) {
      this.$emit("category", v)
    }
  }
}
</script>