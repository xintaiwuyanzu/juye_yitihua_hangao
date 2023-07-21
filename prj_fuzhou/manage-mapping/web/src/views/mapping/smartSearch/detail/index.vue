<template>
  <section>
    <nac-info :back=true title="图谱详情"/>
    <div class="smartSearch_detail_index index_main">
      <el-card class="base-info-detail">
        <el-tabs v-model="activeName">
          <el-tab-pane label="基础信息" name="first">
            <el-descriptions :column="1" border size="medium" style="margin:5px" v-loading="loading">
              <el-descriptions-item :key="info.label" :label="info.label" v-for="info in fields">
                {{ getValue(info) }}
              </el-descriptions-item>
            </el-descriptions>
          </el-tab-pane>
          <el-tab-pane label="档案数据" name="second">
            <div v-loading="archiveLoading">
              <el-empty v-if="archives.length===0"/>
              <el-card v-else :key="index" style="margin-bottom: 10px;padding: 10px" v-for="(i,index) in archives">
                <el-link @click="toViewArchive(i)"><h3 style="margin-bottom: 6px">{{ i.title }}</h3></el-link>
                <br>
                <span>档号：{{ i.archiveCode }}</span>
              </el-card>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
      <div style="width: 10px"/>
      <el-card class="card_charts" v-show="activeName==='first'">
        <div slot="header">
          <strong>关系图谱</strong>
        </div>
        <paint :name="name"/>
      </el-card>
    </div>
  </section>
</template>

<script>
import Paint from "./paint";

export default {
  name: "index",
  components: {Paint},
  data() {
    return {
      fields: [],
      basicInfo: {},
      loading: true,
      activeName: 'first',
      archives: [],
      archiveLoading: false
    }
  },
  methods: {
    async $init() {
      this.loading = true
      await this.loadShowScheme()
      await this.getBaseInfo()
      this.loading = false
    },
    async getBaseInfo() {
      const {data} = await this.$http.post('/search/detail', {
        formDefinitionId: this.$route.query.formDefinitionId,
        formDataId: this.$route.query.fromDataId
      })
      if (data && data.success) {
        this.basicInfo = data.data
      }
    },
    async loadShowScheme() {
      if (this.$route.query.formDefinitionId) {
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: this.$route.query.formDefinitionId,
          schemeType: 'detail',
          formCode: 'detail'
        })
        if (data.success) {
          this.fields = data.data.fields.map(item => {
            if (item.meta && item.meta.dict) {
              return Object.assign({}, {
                'prop': item.code,
                'label': item.name,
                'fieldType': 'select',
                'dictKey': item.meta.dict
              })
            } else {
              return Object.assign({}, {'prop': item.code, 'label': item.name})
            }
          })
        } else {
          this.$message.error(data.message)
        }
      }
    },
    getValue(val) {
      let arr = Object.keys(this.basicInfo).map(item => ({key: item, value: this.basicInfo[item]}))
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].key === val.prop) {
          return arr[i].value
        }
      }
      return ''
    },
    async getArchivesByKeyword() {
      this.archiveLoading = true
      const {data} = await this.$post('tagLibArchive/getArchivesByKeyword', {keyword: this.$route.query.name})
      if (data && data.success) {
        this.archives = data.data
      }
      this.archiveLoading = false
    },
    toViewArchive(row) {
      this.$router.push({
        path: '/archive/metadataAndFileDetail',
        query: {
          formDataId: row.formDataId,
          formDefinitionId: row.formDefinitionId,
          refType: 'archive',
          groupCode: 'default',
          watermark: false
        }
      })
    }
  },
  watch: {
    activeName(val) {
      if (val === 'second') {
        if (this.archives.length === 0) {
          this.getArchivesByKeyword()
        }
      }
    }
  },
  computed: {
    name() {
      return this.$route.query.name
    }
  }
}
</script>
<style lang="scss">
.smartSearch_detail_index {
  flex-direction: row !important;

  .el-card__body {
    flex: 1;
    overflow: auto;
  }

  .base-info-detail {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .card_charts {
    flex: 2;
    display: flex;
    flex-direction: column;
  }
}
</style>