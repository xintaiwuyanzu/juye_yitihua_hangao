<template>
    <table-index :fields="fields" path="/strategy" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm" back>
        <template v-slot:table-$btns="scope">
            <el-button type="text" size="mini" width="80" @click="detail(scope.row)">备份批次详情</el-button>
        </template>
        <template v-slot:edit="form">
            <el-form-item label="选择全宗:" prop="fondId">
                <select-async url='/sysResource/personResource' v-model="form.fondId" placeholder="请选择全宗" clearable
                              style="width: 260px;" :params=fondParams filterable @change="changeFond(form)"/>
            </el-form-item>
            <el-form-item label="选择门类:" prop="classId">
                <select-async :mapper="categories" labelKey="name" v-model="form.classId" placeholder="请选择门类" clearable
                              filterable/>
            </el-form-item>
        </template>

    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {
                        prop: 'strategyName',
                        label: '策略名称',
                        search: true,
                        required: true
                    },
                    // {
                    //     prop: 'strategyType',
                    //     label: '策略类型',
                    //     width: "130",
                    //     fieldType: 'radio',
                    //     mapper: {'1': '全量备份', '2': '增量备份'},
                    //     required: true
                    // },
                    {prop: 'fond_name', label: '全宗名称', width: 180, edit: false},
                    {prop: 'className', label: '分类名称', width: 180, edit: false},
                    {
                        prop: 'spacesId', label: '存储空间', width: 180,
                        fieldType: 'select',
                        url: '/earchive/spaces/page',
                        required: true,
                        labelKey: 'spaceName',
                        params: {page: false}
                    },

                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 140, edit: false},
                ],
                searchForm: {id: this.$route.query.id},
                id: '',
                categories: []
            }
        },
        watch: {},
        methods: {
            detail(row) {
                this.$router.push({
                    path: '/dzdacqbc/backBatch',
                    query: {strategyId: row.id}
                })
            },
            async changeFond(form) {
                this.$set(form, 'classId', '')
                form.categoryId = ''
                this.categories = []
                await this.editShow(form)
            },
            async editShow(form) {
                if (form.fondId) {
                    // const {data} = await this.$http.post('/manage/category/pageByBusinessId', {businessId: form.fondId})
                    const {data} = await this.$http.post('/strategy/getCateByBind', {fondId: form.fondId})
                    if (data.success) {
                        this.categories = data.data
                    } else {
                        this.$message.error(data.message)
                    }
                } else {
                    this.$set(form, 'classId', '')
                }
            }
        }
    }
</script>