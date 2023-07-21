<template>
    <section>
        <nac-info>
            <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
                <el-form-item label="记录名称:">
                    <el-input v-model="searchForm.record_name" placeholder="请输入记录名称" clearable/>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="search" size="mini">搜 索</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
                <el-table ref="applyTable" :data="data" border height="100%" highlight-current-row>
                    <column-index :page="page"/>
                    <el-table-column label="记录名称" prop="record_name" show-overflow-tooltip>
                        <!-- <el-link type="danger" @click="remove(scope.row.id)">删 除</el-link>-->
                        <!--<el-divider direction="vertical"/>-->
                        <template v-slot="scope">
                            <el-link type="primary" @click="detail(scope.row.id)">{{ scope.row.record_name }}</el-link>
                        </template>
                    </el-table-column>

                    <el-table-column label="开始时间" prop="createDate"
                                     dateFormat="YYYY-MM-DD HH:mm:ss"
                                     show-overflow-tooltip/>
                    <el-table-column label="结束时间" prop="updateDate"
                                     dateFormat="YYYY-MM-DD HH:mm:ss"
                                     show-overflow-tooltip/>

                    <el-table-column label="类型" prop="reason" show-overflow-tooltip/>

                    <el-table-column label="数量" prop="quantity" show-overflow-tooltip/>

                    <el-table-column label="状态" align="center" prop="type"
                                     width="60"
                                     show-overflow-tooltip>
                        <template slot-scope="scope">
                            {{scope.row.stateType!='成功'?'失败':'成功'}}<!--调用getChangeType方法-->
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" header-align="center" align="center" width="160">
                        <template v-slot="scope">
                            <span v-if="scope.row.status==1">
                                <el-link @click="download(scope.row)" type="primary">
                                预 览
                            </el-link>
                          <el-divider direction="vertical"/>
                            </span>

                            <!-- <el-link type="danger" @click="remove(scope.row.id)">删 除</el-link>-->
                            <!--<el-divider direction="vertical"/>-->
                            <el-link type="primary" @click="detail(scope.row.id)">详 情</el-link>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <!--      <page :page="page"/>-->
            <el-pagination
                    @current-change="index=>loadData({pageIndex:index-1},this.searchForm)"
                    :current-page.sync="page.index"
                    :page-size="page.size"
                    layout="total, prev, pager, next"
                    :total="page.total">
            </el-pagination>
        </div>
    </section>
</template>
<script>
    import indexMixin from '@/util/indexMixin'

    /**
     * 批次详情列表
     */
    export default {
        mixins: [indexMixin],
        data() {
            return {
                xiazai: true,
                loading: false,
                searchForm: {
                    record_name: "",
                },
                data: [],
                stateMapper: {null: '失败', undefined: '失败'}
            }
        },
        methods: {
            $init() {
                this.loadData()
            },
            dateFormatter(r, c, cell) {
                if (cell) {
                    return this.$moment(cell).format('YYYY-MM-DD HH:mm:ss')
                }
                return '-'
            },
            async loadData(params) {
                this.loading = true
                const {data} = await this.$post('/registerWarehousing/page', {...params})
                this.data = data.data.data
                this.page.index = data.data.start / data.data.size + 1
                this.page.size = data.data.size
                this.page.total = data.data.total
                this.loading = false
            },
            search() {
                this.loadData(this.searchForm)
            },
            getChangeType(e) {
                for (var i = 0; i < this.typeList.length; i++) {
                    if (this.typeList[i].dictValue == e) { //dictValue，dictLabel保持和上面定义一致
                        return this.typeList[i].dictLabel;
                    }
                }
            },
            download(row) {
                // console.log("row",row)
                this.$router.push({path: "/register_warhousing/registerForm", query: {row: row}}).catch(err => {
                    return err
                })
            },
            detail(id) {
                this.$router.push({path: '/register_warhousing/details', query: {batchId: id}})
            },

            remove(id) {
                this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let ids = []
                    if (id === true) {
                        ids = [...this.selectIds]
                    } else if (id) {
                        ids = [id]
                    }
                    if (ids.length > 0) {
                        this.loading = true
                        this.$http.post('/expBatch/delete', {id: ids.join(','), type: "EXP"})
                            .then(({data}) => {
                                if (data.success) {
                                    this.$message.success('删除成功！')
                                    this.selectIds = []
                                    this.loadData()
                                } else {
                                    this.$message.error(data.message)
                                    this.loading = false
                                }
                            })
                    } else {
                        this.$message.warning('请选择要删除的数据列！')
                    }
                })

            },
        }
    }
</script>
