<template>
    <section>
            <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
                <el-form-item label="档案题名：" prop="taskName">
                    <el-input v-model="searchForm.archiveTitle" clearable placeholder="请输入档案题名"/>
                </el-form-item>
              <el-form-item label="档号：" prop="archiveCode">
                <el-input v-model="searchForm.archiveCode" clearable placeholder="请输入档号"/>
              </el-form-item>
              <el-form-item label="档案状态：" prop="isNormal">
                <el-select v-model="searchForm.isNormal" clearable placeholder="请选择档案状态">
                  <el-option label="正常" value="1"></el-option>
                  <el-option label="异常" value="0"></el-option>
                </el-select>
              </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" size="mini">搜 索</el-button>
                    <el-button @click="resetFields" size="mini">重 置</el-button>
                </el-form-item>
            </el-form>
    </section>
</template>

<script>
    import fromMixin from '@/util/formMixin'

    export default {
        data() {
            return {
              form: {},
              task: {},
              searchForm: {
                archiveTitle: '',
                archiveCode: '',
                isNormal: '',
                taskId: '',
              }
            }
        },
        mixins: [fromMixin],
        methods: {
            $init() {

            },
            apiPath() {
                return '/cqbctaskarchivedetail'
            },
          resetFields() {
            this.searchForm = {
              archiveTitle: '',
              archiveCode: '',
              isNormal: '',
              taskId: this.task.id,
            }
          },
          getTask(task) {
            this.task = task
            this.resetFields()
          }
        }
    }
</script>
