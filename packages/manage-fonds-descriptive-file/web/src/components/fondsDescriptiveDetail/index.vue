<template>
  <section style="height: 100%">
    <el-card style="height: 100%;overflow: scroll">
      <div slot="header" class="clearfix">
        <el-link class="control-label inline text-info" style="width: auto;"
                 @click="$router.push({path: '/archive/management/edit', query: {managementTypeCode: management.managementTypeCode,id:management.id,optType:'detail'}})">
          <h2>{{ management.title }}</h2>
        </el-link>
      </div>
      <el-descriptions :column="2" border size="medium">
        <el-descriptions-item label="年度">{{ management.vintages }}</el-descriptions-item>
        <el-descriptions-item label="年度范围">{{ management.vintagesStart }}一{{
            management.vintagesEnd
          }}
        </el-descriptions-item>
        <el-descriptions-item label="文件时间">{{ management.fileTime }}</el-descriptions-item>
        <el-descriptions-item label="件号">{{ management.pieceNumber }}</el-descriptions-item>
        <el-descriptions-item label="保管期限">{{
            management.retentionPeriod
          }}
        </el-descriptions-item>
        <el-descriptions-item label="材料分类">{{ management.managementTypeName }}<span
            v-if="management.dossierTypeName">-{{ management.dossierTypeName }}</span></el-descriptions-item>
        <el-descriptions-item label="全宗">{{ management.fondName }}</el-descriptions-item>
        <el-descriptions-item label="行政区划"> {{
            management.administrative
          }}
        </el-descriptions-item>
        <el-descriptions-item label="责任人">{{
            management.personLiable
          }}
        </el-descriptions-item>
        <el-descriptions-item label="材料编码">{{
            management.archive_code
          }}
        </el-descriptions-item>
        <el-descriptions-item label="页数">{{
            management.totalNumberOfPages
          }}
        </el-descriptions-item>
        <el-descriptions-item label="盒号">{{ management.boxNumber }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="1" border size="medium">
        <el-descriptions-item label="内容">
          <div v-html="management.compilationContent" class="table1">{{ management.compilationContent }}</div>
        </el-descriptions-item>
        <el-descriptions-item label="资料清单">
          <span v-for="item in fileList" :key="item.id">
            <el-link type="primary" style="margin-left: 5px;"
                     @click="$router.push({path: '/archive/management/edit', query: {managementTypeCode: management.managementTypeCode,id:management.id,optType:'detail'}})">
              {{ item.fileName }}
            </el-link><br/>
          </span>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      management: {},
      fileList: []
    }
  },
  props: {
    formDataId: {type: String},
  },
  watch: {
    formDataId() {
      this.getManagement()
      this.getFileList()
    }
  },
  methods: {
    $init() {
      this.getManagement()
      this.getFileList()
    },
    //获取全宗卷信息详情
    async getManagement() {
      const {data} = await this.$http.post('/management/detail', {id: this.formDataId})
      if (data && data.success) {
        this.management = data.data
      }
    },
    //获取上传文件列表
    async getFileList() {
      const {data} = await this.$http.post('manageFile/page', {
        page: false,
        businessId: this.formDataId,
      })
      if (data.success) {
        if (data && data.success) {
          this.fileList = data.data
        }
      }
    }
  }
}
</script>