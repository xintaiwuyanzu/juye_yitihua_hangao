<template>
    <section>
        <el-dropdown
                placement="bottom"
                trigger="click"
                @command="handleCommand">
            <el-button class="search-btn" type="primary">退回至临时库<i class="el-icon-arrow-down el-icon--right"/>
            </el-button>
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-if="currentSelect.length>0" command="select">退回选中</el-dropdown-item>
                <el-dropdown-item command="all">退回所有</el-dropdown-item>
                <el-dropdown-item command="query">退回查询</el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
    </section>
</template>
<script>
    import abstractComponent from "../abstractComponent";
    import formMixin from "@dr/auto/lib/util/formMixin";

    export default {
        mixins: [formMixin],
        extends: abstractComponent,
        name: 'send',
        data() {
            return {
                persons: [],
                sendType: 'all',
                form: {}
            }
        },
        methods: {
            async handleCommand(command) {
                this.sendType = command
                //提前加校验
                const query = this.eventBus.getQueryByQueryType(this.sendType)
                const {data} = await this.$http.post('/return/fileReturnCheck', {...query})
                if (!data.success) {
                    //如果不成功 说明有档案 已经移交出去 不能退回 只能查看

                    return this.$message({
                        showClose: true,
                        dangerouslyUseHTMLString: true,
                        duration: 0,
                        message: data.message,
                        type: 'warning'
                    });
                }
                this.send()
            },
            async send() {
                try {
                    await this.$confirm('确定要退回选中的数据吗？', '提醒')
                    //增加 退回记录
                    const query = this.eventBus.getQueryByQueryType(this.sendType)
                    const {data} = await this.$http.post('/return/fileReturnRec', {status: 'RECEIVE', ...query})
                    if (data.success) {
                        this.eventBus.$emit("loadData")
                        this.$message.success('退回到接收库成功')
                    } else {
                        this.$message.success(data.message)
                    }
                } catch (e) {
                    this.$message({
                        type: 'info',
                        message: '已取消退回'
                    });
                    /* this.$message.error(`退回到接收库失败${e}`)*/
                }
            },
            closeDialog() {
            }
        }
    }
</script>
