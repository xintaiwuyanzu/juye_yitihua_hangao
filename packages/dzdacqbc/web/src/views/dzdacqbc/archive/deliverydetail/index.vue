<template>
    <metadata-file-view :title="formData.ARCHIVE_CODE" :formDataId="$route.query.formDataId"
                        :formDefinitionId="$route.query.formDefinitionId" refType="archive"
                        group-code="default"
                        :form-data="formData"
                        @next="loadTaskQuantity('next')"
                        >
      <el-button type="primary" @click="back()">返回</el-button>
    </metadata-file-view>
</template>

<script>

    export default {
        name: "index",
        data() {
            return {
                loading: false,
                formDefinitionId: this.$route.query.formDefinitionId,
                formDataId: this.$route.query.formDataId,
                archiveIndex: this.$route.query.index,
                formData: {},
            }
        },
        methods: {
            async $init() {
                const {data} = await this.$http.post('manage/formData/detail?', {
                    formDefinitionId: this.formDefinitionId,
                    formDataId: this.formDataId
                })
                if (data.success) {
                    this.formData = data.data
                }
            },
            loadTaskQuantity(type) {
                let pageIndex = this.archiveIndex
                if ('next' == type) {
                    pageIndex = parseInt(this.archiveIndex) + 1
                } else {
                    if (this.archiveIndex == 0) {
                        this.$message.error("已是第一份，没有上一份")
                        return
                    } else {
                        pageIndex = parseInt(this.archiveIndex) - 1
                    }
                }
            },
          back() {
            this.$router.back()
          },
        },
    }
</script>