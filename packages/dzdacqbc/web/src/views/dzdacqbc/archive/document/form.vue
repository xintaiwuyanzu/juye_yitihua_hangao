<template>
    <section>
        <nac-info title="档案信息">
            <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
                <el-form-item label="题名:" prop="title">
                    <el-input v-model="searchForm.title" clearable placeholder="请输入题名"/>
                </el-form-item>
                <el-form-item label="全宗名:" prop="totalName">
                    <el-input v-model="searchForm.totalName" clearable placeholder="请输入全宗名"/>
                </el-form-item>
                <el-form-item label="年度:" prop="annual">
                    <el-input v-model="searchForm.annual" clearable placeholder="请输入年度"/>
                </el-form-item>
                <el-form-item prop="fileId">
                    <el-input v-model="searchForm.fileId" v-if="false"/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" size="mini">搜 索</el-button>
                    <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
    </section>
</template>

<script>
    import fromMixin from '@/util/formMixin'

    export default {
        data() {
            return {
                form: {},
            }
        },
        mixins: [fromMixin],
        methods: {
            $init() {

            },
            apiPath() {
                return '/cqbcarchive'
            },
            getId(id) {
                this.searchForm.fileId = id
            }
        },
    }
</script>
