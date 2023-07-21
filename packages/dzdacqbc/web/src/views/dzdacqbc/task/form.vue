<template>
    <section>
        <nac-info title="任务管理">
            <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
                <el-form-item label="任务名称：" prop="taskName">
                    <el-input v-model="searchForm.taskName" clearable placeholder="请输入任务名称"/>
                </el-form-item>
              <el-form-item label="任务类型：" prop="taskType">
                <el-select v-model="searchForm.taskType" clearable placeholder="请选择任务类型">
                  <el-option label="全局检测" value="全局检测" />
                  <el-option label="抽样检测" value="抽样检测" />
                </el-select>
              </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" size="mini">搜 索</el-button>
                    <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
                    <el-button type="info" @click="$router.go(-1)" size="mini" v-show="$route.query.back">返 回</el-button>
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
                searchForm: {
                    taskName: '',
                    taskType: '',
                },
            }
        },
        mixins: [fromMixin],
        methods: {
            $init() {

            },
            apiPath() {
                return '/cqbctask'
            },
        }
    }
</script>
