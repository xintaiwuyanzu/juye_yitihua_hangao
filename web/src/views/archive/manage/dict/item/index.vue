<template>
    <section>
        <nac-info title="字典项配置">
            <el-form :model="searchForm" ref="form" inline >
                <el-form-item label="字典项名称" prop="name">
                    <el-input v-model="searchForm.name"
                              placeholder="请输入字典项名称"
                              style="width: 300px"
                              clearable></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="loadData" size="mini">搜 索</el-button>
                </el-form-item>
                <el-form-item style="float: right">
                    <el-button type="primary" size="mini" @click="add">添 加</el-button>
                    <!--                    <el-button type="danger" size="mini" @click="remove">删 除</el-button>-->
                    <el-button type="success" size="mini" @click="$router.back()">返 回</el-button>
                </el-form-item>
            </el-form>
        </nac-info>
        <div class="index_main" v-loading="loading">
            <div class="table-container">
                <el-table ref="multipleTable"
                          :data="data"
                          border
                          height="100%"
                          @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="50" align="center"/>
                    <el-table-column label="序号" fixed align="center" width="50">
                        <template slot-scope="scope">
                            {{ (page.index - 1) * page.size + scope.$index + 1 }}
                        </template>
                    </el-table-column>
                    <el-table-column label="字典项名称" prop="name" show-overflow-tooltip align="center"
                                     header-align="center">
                        <template slot-scope="scope">
                            {{ scope.row.name }}
                        </template>
                    </el-table-column>
                    <el-table-column label="字典项编码" prop="code" show-overflow-tooltip align="center"
                                     header-align="center">
                        <template slot-scope="scope">
                            {{ scope.row.code }}
                        </template>
                    </el-table-column>
                    <el-table-column label="备注" prop="remark" show-overflow-tooltip align="center"
                                     header-align="center">
                    </el-table-column>
                    <el-table-column label="操作" align="center" header-align="center" fixed="right" width="160">
                        <template slot-scope="scope">
                            <el-button size="mini" @click="edit(scope.row)" type="primary">编 辑</el-button>
                            <el-button size="mini" type="danger" @click="remove(scope.row.id)">删 除</el-button>
                        </template>
                    </el-table-column>
                </el-table>

            </div>
            <el-pagination
                    @current-change="index=>loadData(index)"
                    :current-page.sync="page.index"
                    :page-size="page.size"
                    layout="total, prev, pager, next"
                    :total="page.total">
            </el-pagination>

            <el-dialog
                    :title="deployTitle"
                    width="50%"
                    :close-on-click-modal="false"
                    :visible.sync="dialog">
                <el-form :model="showScheme" :rules="rules" ref="form" style="" label-width="200px">
                    <el-form-item label="字典项名称" prop="name" required>
                        <el-input v-model="showScheme.name" placeholder="请输入字典项名称"
                                  style="width: 75%" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="字典项编码" prop="code" required>
                        <el-input v-model="showScheme.code"
                                  placeholder="请输入字典项编码"
                                  style="width: 75%" clearable></el-input>
                    </el-form-item>
                    <el-form-item label="排序" prop="order">
                        <el-input v-model="showScheme.order"
                                  @input="showScheme.order=showScheme.order.replace(/[^\d]/g,'')"
                                  placeholder="请输入备注"
                                  style="width: 75%"></el-input>
                    </el-form-item>
                    <el-form-item label="备注" prop="remark">
                        <el-input v-model="showScheme.remark"
                                  placeholder="请输入备注"
                                  type="textarea"
                                  style="width: 75%"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                  <el-button type="info" @click="dialog = false" class="btn-cancel">取 消</el-button>
                  <el-button type="primary" @click="save" v-loading="loading" class="btn-submit">提 交</el-button>
                </div>
            </el-dialog>
        </div>
    </section>
</template>

<script>
import indexMixin from "@dr/auto/lib/util/indexMixin";

export default {
    mixins: indexMixin,
    name: "index",
    data() {
        return {
            searchForm: {
                name: ''
            },
            data: [],
            dialog: false,
            dialogVisible: false,
            showScheme: {},
            multipleSelection: [],
            page: {
                index: 1,
                size: 15,
                total: Number
            },
            deployTitle: '',
            dictGroupId: '',
            showSchemeId: '',
        }
    },
    methods: {
        $init() {
            this.dictGroupId = this.$route.query.id
            this.loadData()
        },
        loadData(index) {
            // this.loading = true
            if (index) {
                this.page.index = index
            }
            const param = Object.assign({}, this.page, {
                name: this.searchForm.name,
                businessId: this.dictGroupId
            })
            this.$http.post('/manage/dictitem/page', param)
                    .then(({data}) => {
                        if (data.success) {
                            this.data = data.data.data
                            this.page.index = data.data.start / data.data.size + 1
                            this.page.size = data.data.size
                            this.page.total = data.data.total
                        } else {
                            this.$message.error(data.message)
                        }
                        this.loading = false
                    })
        },
        add() {
            this.deployTitle = '添加'
            this.optype = 'add'
            this.dialog = true
            this.showScheme = {}
        },
        edit(row) {
            this.deployTitle = '编辑'
            this.optype = 'edit'
            this.showScheme = Object.assign({}, row)
            this.dialog = true
        },

        save() {
            this.$refs.form.validate(valid => {
                if (valid) {
                    let path
                  if (this.optype === 'add') {
                        path = '/manage/dictitem/insert'
                    } else {
                        path = '/manage/dictitem/update'
                    }
                    this.showScheme.businessId = this.dictGroupId
                    this.loading = true
                    this.$http.post(path, this.showScheme)
                            .then(({data}) => {
                                if (data.success) {
                                    this.$message.success("保存成功")
                                    this.dialog = false
                                    this.loadData()
                                } else {
                                    this.$message.error(data.message)
                                }
                                this.loading = false
                            })
                }
            })
        },
        remove(id) {
            this.$confirm("确认删除？", '提示', {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
                dangerouslyUseHTMLString: true
            }).then(() => {
                this.$http.post('/manage/dictitem/delete', {id: id})
                        .then(({data}) => {
                            if (data.success) {
                                this.$message.success("删除成功")
                                this.loadData()
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
            })
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        timestampToTime(timestamp) {
            if (timestamp !== 0 && timestamp !== undefined) {
                return this.$moment(timestamp).format('YYYY')
            }
        },
    }
}
</script>
