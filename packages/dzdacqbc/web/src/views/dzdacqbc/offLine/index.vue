<template>
    <section>
        <nac-info>
            <data-form v-show="showDataForm" ref="DataForm" :fondIdData="fondIdData" :classCode="classCode"
                       :categoryId="categoryId"
                       :total="page.total" @func="myLoadData" :multiple-selection="multipleSelection"
                       v-on:imp="impArchive"
                       :updatesBatch="updatesBatch"/>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <el-row style="overflow: hidden">
                <el-col :span="4">
                    <el-card shadow="hover">
                        <div slot="header">
                            <strong>全宗分类</strong>
                        </div>
                        <fond-tree :fondId="fondId" autoSelect @check="check" ref="fondTree" :withPermission="true"
                                   style="height: 100%"/>
                    </el-card>
                </el-col>
                <span v-show="showDataForm == false">请选择左侧全宗门类树或者指定的分类没有配置元数据</span>
                <el-col :span="20" v-show="showDataForm" style="height: 100%">
                    <el-card shadow="hover">
                        <div class="table-container">
                            <el-table ref="table" :data="data" border height="660" highlight-current-row
                                      @selection-change="handleSelectionChange">

                                <el-table-column label="排序" fixed align="center" width="50">
                                    <template v-slot="scope">
                                        {{ (page.index - 1) * page.size + scope.$index + 1 }}
                                    </template>
                                </el-table-column>
                                <el-table-column label="备份名称" prop="strategyName" show-overflow-tooltip align="center"
                                />
                                <el-table-column label="年度" prop="nd" show-overflow-tooltip width="80"
                                                 align="center">
                                </el-table-column>
                                <el-table-column label="介质名称" prop="mediumName" show-overflow-tooltip
                                                 align="center">
                                </el-table-column>
                                <el-table-column label="备份状态" prop="status" header-align="center" align="center"
                                                 show-overflow-tooltip
                                                 width="100px">
                                    <template v-slot="scope">
                                        {{ scope.row.status|dict({'0': "未开始",'1': "已结束"}) }}
                                    </template>
                                </el-table-column>
                                <el-table-column label="备份数量" prop="strategyCount" show-overflow-tooltip width="80"
                                                 align="center">
                                </el-table-column>
                                <el-table-column prop="createDate" label="创建时间" header-align="center" align="center"
                                                 show-overflow-tooltip
                                                 width="150px">
                                    <template v-slot="scope">
                                        {{ scope.row.createDate|datetime }}
                                    </template>
                                </el-table-column>

                                <el-table-column label="操作" align="center" header-align="center" width="250"
                                                 fixed="right">
                                    <template v-slot="scope">
                                        <el-link type="success" size="mini" @click="findList(scope.row)">查询数据</el-link>
                                        <el-divider direction="vertical"></el-divider>
                                        <el-link type="success" size="mini" v-if="scope.row.status === '0'"
                                                 @click="sendData(scope.row)">上传数据
                                        </el-link>
                                        <el-link type="success" size="mini" v-if="scope.row.status === '1'"
                                                 @click="downData(scope.row)">下载数据
                                        </el-link>
                                        <el-divider direction="vertical" v-if="scope.row.status === '0'"></el-divider>
                                        <el-link type="primary" size="mini" @click="editForm(scope.row)" v-if="scope.row.status === '0'">编辑</el-link>
                                        <el-divider direction="vertical"></el-divider>
                                        <el-link type="danger" size="mini" @click="del(scope.row)">删除</el-link>
                                    </template>
                                </el-table-column>

                            </el-table>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
            <el-pagination
                    @current-change="index=>loadData({pageIndex:index-1,fondId:this.fondIdData,categoryId:this.categoryId},true)"
                    R
                    :current-page.sync="page.index"
                    :page-size="page.size"
                    layout="total, prev, pager, next"
                    :total="page.total">
            </el-pagination>
        </div>

    </section>
</template>

<script>
    import indexMixin from "@dr/auto/lib/util/indexMixin";
    import DataForm from './form';
    import util from "@dr/framework/src/components/login/util";
    import {encode} from 'js-base64';
    import {useUser} from "@dr/framework/src/hooks/userUser";

    export default {
        mixins: [indexMixin],
        components: {DataForm},
        setup() {
            return useUser();
        },
        data() {
            let fondId = this.$route.query.id
            if (!fondId) {
                fondId = this.id
            }
            return {
                fondId,
                fondIdData: '',
                classCode: '',
                row: [],
                multipleSelection: [],
                key: 0,
                updatesBatch: [],
                showDataForm: false,
                categoryId: '',
                checkFond:{}
            }
        },
        filters: {},
        methods: {
            $init() {
            },

            apiPath() {
                return "/esave/offLine"
            },
            async sendData(row) {
                const {data} = await this.$post('esave/offLine/sendDataToClient', {
                    offLineId: row.id
                })
                if (data.data === 1) {
                    this.$message.success('上传客户端成功！')
                    this.check(this.checkFond)
                } else {
                    this.$message.error('上传客户端失败！')
                }
                this.loading = false
            },
            downData(row){
                let url = 'http://localhost:8081'
                let path = encode(url + '/dataoperationtools.html/#/login?token=' + util.getCookie('dauth') + '&userId=' + this.user.id + '&menuPath=/dataClient/outRecord')
                if (process.env.NODE_ENV === 'production') {
                    url = location.origin
                    path = encode(url + '/dataoperationtools/index.html#/login?token=' + util.getCookie('dauth') + '&userId=' + this.user.id + '&menuPath=/dataClient/outRecord')
                }
                window.open('dc://openUrl?url=' + path + '&store=true')
            },
            findList(row) {
                this.$router.push({
                    path: '/dzdacqbc/offLine/detail',
                    query: {batchId: row.id}
                })

            },
            check(row) {
                this.checkFond = row
                this.loadData({fondId: row.data.fondId, classId: row.data.id})
                this.showDataForm = true
                this.fondIdData = row.data.fondId
                this.classCode = row.data.classCode
                this.categoryId = row.data.id
            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
                val.forEach(i => {
                    this.updatesBatch.push(i.id)
                })
            },
            del(row) {
                this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('esave/offLine/delete', {id: row.id})
                        .then(({data}) => {
                            if (data.success) {
                                this.$message.success('删除成功')
                                this.loadData({fondId: row.fondId, classId: row.classId})
                            } else {
                                this.$message.error('删除失败')
                            }
                        })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            editForm(row) {
                this.$refs.DataForm.showEdit(row)
            },
            impArchive() {
                this.$refs.imp.show = true
                this.$refs.imp.classCode = this.classCode
                this.$refs.imp.fondId = this.fondIdData
            },
            myLoadData(val) {
                val.fondId = this.fondIdData
                val.categoryId = this.categoryId
                this.loadData(val)
            }
        },
        mounted() {
        }
    }
</script>

<style scoped>

    .el-row {
        flex: 1;
    }

    .el-col {
        height: 100%;
        display: flex;
    }

    .el-card {
        flex: 1;
        overflow: auto;
    }
</style>
