<template>
    <section v-loading="loading">
        <nac-info :title="'位置详情'" :back="true">
        </nac-info>
        <div class="index_main card">
            <el-row>
                <el-col :span="16">
                    <el-card shadow="hover" style="height: 240px">
                        <div slot="header" style="height: auto">
                            <strong>位置基本信息</strong>
                            <!--                          <el-button-group style="float: right">
                                                          <el-button type="success" size="mini" @click="editPosition">修改</el-button>
                                                          <el-button type="danger" size="mini" @click="deletePosition">删除</el-button>
                                                          <el-button type="warning" size="mini"
                                                                     @click="updateStatus">
                                                              {{positionDetails.positionStatus|dict({1:'停 用',0:'启 用'})}}
                                                          </el-button>
                                                      </el-button-group>-->
                        </div>
                        <el-descriptions class="margin-top" :column="3" border>
                            <!--  <template slot="extra">
                                <el-button type="primary" size="small">操作</el-button>
                              </template>-->
                            <el-descriptions-item>
                                <template slot="label">
                                    库房
                                </template>
                                <el-tag>{{ positionDetails.locId|getLocOptions }}</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    区
                                </template>
                                <el-tag>{{ positionDetails.areaId|getAreas }}</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    密集架名称
                                </template>
                                <el-tag>{{ positionDetails.caseName }}</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    密集架号
                                </template>
                                <el-tag>{{ positionDetails.bookCaseNo }}</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    总节数
                                </template>
                                <el-tag>{{ positionDetails.columnNo }}节</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    总层数
                                </template>
                                <el-tag>{{ positionDetails.layer }}层</el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    最后更新时间
                                </template>
                                <el-tag><span>{{ positionDetails.updateDate|datetime }}</span></el-tag>
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    已放档案数量
                                </template>
                                <el-tag>{{ counted }}个</el-tag>
                            </el-descriptions-item>
                        </el-descriptions>
                        <!--       <el-descriptions label-width="120px" :column="5">
                                 <el-descriptions-item label="状态"><el-tag>{{positionDetails.positionStatus|dict({0:'停用中',1:'启用中'})}}</el-tag></el-descriptions-item>
                                 <el-descriptions-item label="最后更新时间">   <el-tag> <span>{{positionDetails.lastUpdate|datetime}}</span></el-tag></el-descriptions-item>
                                 <el-descriptions-item label="已投放档案数量"><el-tag>  <el-tag>{{counted}}个</el-tag></el-tag></el-descriptions-item>
                               </el-descriptions>-->
                        <!--      <el-form inline label-width="120px" class="form_detail">
                                  <el-form-item  label="档案室：">
                                      <span>{{positionDetails.locId|getLocOptions}}</span>
                                  </el-form-item>
                                  <el-form-item label="密集架名称：">
                                      <el-tag>{{positionDetails.caseName}}</el-tag>
                                  </el-form-item>
                                  <el-form-item label="密集架号：">
                                      <el-tag>{{positionDetails.bookCaseNo}}</el-tag>
                                  </el-form-item>
                                  <br>
                                  <el-form-item label="总列数：">
                                      <el-tag>{{positionDetails.columnNo}}列</el-tag>
                                  </el-form-item>
                                  <el-form-item label="总层数：">
                                      <el-tag>{{positionDetails.layer}}层</el-tag>
                                  </el-form-item>
                                  <el-form-item label="每格容量：">
                                      <el-tag>{{positionDetails.bookCount}}本</el-tag>
                                  </el-form-item>
                                  <br>
                                  <el-form-item label="密集架类型：">
                                      <span>{{positionDetails.columnNum|dict('columnNum.status')}}</span>
                                  </el-form-item>
                                 &lt;!&ndash; <el-form-item label="状态：">
                                      <el-tag>{{positionDetails.positionStatus|dict({0:'停用中',1:'启用中'})}}</el-tag>
                                  </el-form-item>&ndash;&gt;
                                  <el-form-item label="最后更新时间：">
                                      <span>{{positionDetails.lastUpdate|datetime}}</span>
                                  </el-form-item>
                                  <br>
                                  <el-form-item label="已放档案：">
                                      <el-tag>{{counted}}个</el-tag>
                                  </el-form-item>
            &lt;!&ndash;                            <el-form-item label="位置描述：">
                                      <span>{{positionDetails.caseNoTrans}}</span>
                                  </el-form-item>
                                  <el-form-item label="备注：">
                                      <span>{{positionDetails.remarks}}</span>
                                  </el-form-item>&ndash;&gt;
                              </el-form>-->
                    </el-card>
                </el-col>
                <el-col :span="8">
                    <el-card shadow="hover" style="height: 240px">
                        <br>
                        <el-col :span="16">
                            <el-row><span>密集架AB面:&nbsp;
                                <el-switch
                                        v-model="value2"
                                        active-color="#13ce66" :disabled="disabled"
                                        inactive-color="dodgerblue" @change="handleChangeone">
                                </el-switch>&nbsp;&nbsp;</span><span style="color: rebeccapurple ">
                                {{ this.value2|dict({true: 'A', false: 'B'}) }}面</span>
                            </el-row>
                            <br>
                            <el-row><span>密集架节数:&nbsp;&nbsp;
                            <el-input-number v-model="layerNo" controls-position="right" @change="handleChangeThree"
                                             :min="1" :max="parseInt(layerNos)"></el-input-number>
                            </span></el-row>
                            <br>
                            <el-row><span>密集架层数:&nbsp;&nbsp;
                            <el-input-number v-model="layerNum" controls-position="right" @change="handleChangetwo"
                                             :min="1" :max="parseInt(layer)"></el-input-number>
                            </span></el-row>
                            <br>
                        </el-col>
                        <el-col :span="8">
                            <el-row>
                                <div style="text-align: center;padding-top: 65px;">
                                    <el-form>
                                        <el-form-item class="word">
                                            <el-button round style="border: none"><span
                                                    style="font-size: 50px;text-align: center;color: dodgerblue;font-family: 'Microsoft YaHei'">{{ count }}份</span>
                                            </el-button>
                                        </el-form-item>
                                    </el-form>
                                </div>
                            </el-row>
                        </el-col>
                        <br>
                        <br>
                    </el-card>
                </el-col>
            </el-row>
            <el-row>
                <el-card v-if="!denseframeArchive" shadow="hover">
                    <div slot="header">
                        <strong>档案馆藏信息</strong>
                    </div>
                    <el-table :data="layerData" border height="350px">
                        <el-table-column label="排序" fixed align="center" width="80">
                            <template slot-scope="scope">
                                {{ (page.index - 1) * page.size + scope.$index + 1 + 15 }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="title" label="档案题名" align="center" header-align="center"
                                         show-overflow-tooltip>

                        </el-table-column>
                        <el-table-column prop="archiveCode" label="档/盒号" align="center" header-align="center"
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <el-button type="text" size="small" @click="positionDetailsJump(scope.row)">
                                    {{ scope.row.archiveCode }}
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column prop="archiveCode" label="件/盒" align="center" header-align="center"
                                         show-overflow-tooltip>
                            <template v-slot="scope">
                                {{ scope.row.archiveBox|dict({'1': "件", '2': "盒"}) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" header-align="center" width="200">
                            <template slot-scope="scope">
                                <el-link v-if="scope.row.archiveBox=='2'" type="success" size="mini"
                                         @click="getBooksBybookCase(scope.row.id)">查看详情
                                </el-link>
                                <el-divider v-if="scope.row.archiveBox=='2'" direction="vertical"></el-divider>
                                <el-link type="danger" size="mini" @click="deleteArchive(scope.row)">移除</el-link>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
                <el-card v-if="denseframeArchive" shadow="hover">
                    <div slot="header">
                        <strong>档案馆藏信息</strong>
                        <el-button v-if="denseframeArchive" style="float: right;margin-left: 10px;color: #409EFF"
                                   @click="goArchive"
                                   size="mini" type="text">返回
                        </el-button>
                    </div>
                    <el-table :data="layerDataBox" border height="350px">
                        <el-table-column label="排序" fixed align="center" width="80">
                            <template slot-scope="scope">
                                {{ (page.index - 1) * page.size + scope.$index + 1 + 15 }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="title" label="档案题名" align="center" header-align="center"
                                         show-overflow-tooltip>

                        </el-table-column>
                        <el-table-column prop="archiveCode" label="档号" align="center" header-align="center"
                                         show-overflow-tooltip>
                            <template slot-scope="scope">
                                <el-button type="text" size="small" @click="positionDetailsJump(scope.row)">
                                    {{ scope.row.archiveCode }}
                                </el-button>
                            </template>
                        </el-table-column>
                        <el-table-column prop="archiveCode" label="件" align="center" header-align="center"
                                         show-overflow-tooltip>
                            <template v-slot="scope">
                                {{ scope.row.archiveBox|dict({'1': "件", '2': "盒"}) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" header-align="center" width="200">
                            <template slot-scope="scope">
                                <el-link type="danger" size="mini" @click="deleteArchive(scope.row)">移除</el-link>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-row>
        </div>
        <position-form ref="positionForm"/>
        <el-dialog :visible.sync="dialogVisible" width="400px" title="修改档案保存状态">
            <el-form :model="form" :rules="rules" ref="form" label-width="100px" style="width: 300px">
                <el-form-item label="保存状态:" prop="saveStatus">
                    <select-dict v-model="form.saveStatus" type="archive.saveStatus"/>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveArchiveStatus" v-loading="loading">保 存</el-button>
            </div>
        </el-dialog>
    </section>
</template>
<script>
    import indexMixin from '@/util/indexMixin'
    import positionForm from './positionForm'

    let locoptions = []
    let areas = []
    export default {
        mixins: [indexMixin],
        components: {positionForm},
        data() {
            return {
                value2: true,
                dict: ['organise', 'book.circul', 'loc.type', 'book.status', 'columnNum.status', 'archive.saveStatus'],
                loading: false,
                positionDetails: {},
                bookCaseNo: '',
                layerData: [],
                layerDataBox: [],
                layer: '',
                layerNos: '',
                layerNo: 1,
                layerNum: 1,
                ABsurface: 1,
                count: 0,
                dataForm: [],
                thisbarCode: '',
                counted: 0,
                disabled: false,
                denseframeArchive: false,
                bookCaseID: '',
                dialogVisible: false,
                form: {saveStatus: ''},
                rules: {
                    saveStatus: [{required: true, message: '请选择保管状态', trigger: 'change'}]
                },
                row: {}
            }
        },
        filters: {
            getLocOptions(v) {
                if (v) {
                    const loc = locoptions.find(d => d.id === v)
                    if (loc) {
                        return loc.name
                    }
                }
            },
            getAreas(v) {
                if (v) {
                    const area = areas.find(d => d.id === v)
                    if (area) {
                        return area.areaName
                    }
                }
            },

        },
        methods: {
            //更换AB面
            handleChangeone(value) {
                if (value == true) {
                    this.ABsurface = 1
                } else {
                    this.ABsurface = 2
                }
                this.getBooksBybookCaseId()
            },

            //更换层数
            handleChangetwo(value) {
                this.layerNum = value
                this.getBooksBybookCaseId()
            },
            //更换层数
            handleChangeThree(value) {
                this.layerNo = value
                this.getBooksBybookCaseId()
            },
            goArchive() {
                this.denseframeArchive = false
            },
            //初始化，获取页面数据
            $init() {
                //this.getlibs()
                this.getlocoption()
                this.loadDataBook()
                this.getAreas()
                //this.getBooksBybookCaseNo()
            },
            getlocoption() {
                this.$http.post('/locationkufang/getLocations').then(({data}) => {
                    if (data.success) {
                        locoptions = data.data
                    } else {
                        this.$message.error(data.message)
                    }
                })
            },
            getAreas() {
                this.$http.post('kufangArea/page', {page: false})
                    .then(({data}) => {
                        if (data.success) {
                            areas = data.data
                        } else {
                            areas = []
                        }
                    })
            },
            //加载位置详情页面
            loadDataBook() {
                //this.loading = true
                let caseNo = ''
                const id = this.$route.query.id
                if (id) {
                    this.$http.post('/position/getPositionbyId', {id: id})
                        .then(({data}) => {
                            if (data.success) {
                                this.positionDetails = data.data
                                if (this.positionDetails.columnNum == '1') {
                                    this.disabled = true
                                }
                                this.$http.post('/position/getBookCountByPositionId', {id: id})
                                    .then(({data}) => {
                                        if (data.success) {
                                            this.counted = data.data
                                        }
                                    });
                                this.layer = data.data.layer
                                this.layerNos = data.data.columnNo
                                caseNo = data.data.bookCaseNo
                                this.bookCaseNo = caseNo
                                this.bookCaseID = data.data.id
                                if (this.$route.query.entityFiles) {
                                    this.value2 = this.$route.query.entityFiles.columnNum
                                    this.layerNo = parseInt(this.$route.query.entityFiles.columnNo)
                                    this.layerNum = parseInt(this.$route.query.entityFiles.layer)
                                }
                                this.getBooksBybookCaseId()
                                if (this.$route.query.entityFiles?.parentId) {//盒
                                    this.getBooksBybookCase(this.$route.query.entityFiles.parentId)
                                }
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                }
            },
            //获取该层图书列表
            getBooksBybookCaseId() {
                this.$http.post('/entityFiles/page', {
                    page: false,
                    caseId: this.bookCaseID,
                    columnNum: this.value2 ? 1 : 2,
                    columnNo: this.layerNo,
                    layer: this.layerNum
                }).then(({data}) => {
                    if (data.success) {
                        this.layerData = data.data
                        this.denseframeArchive = false
                        if (this.layerData != null) {
                            this.count = this.layerData.length
                        } else {
                            this.count = 0;
                        }
                    } else {
                        this.$message.error(data.message)
                    }
                    this.loading = false
                })
            },
            //获取盒下档案列表
            getBooksBybookCase(row) {
                this.$http.post('/entityFiles/page', {
                    parentId: row,
                    page: false,
                }).then(({data}) => {
                    if (data.success) {
                        this.layerDataBox = data.data
                        this.denseframeArchive = true
                        console.log(this.layerDataBox)
                    } else {
                        this.$message.error(data.message)
                    }
                    this.loading = false
                })
            },
            //编辑位置
            editPosition() {
                this.$refs.positionForm.editForm(this.positionDetails)
            },
            //移除
            deleteArchive(row) {
                this.$confirm('确定移除当前档案吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('/entityFiles/removeEntity', {
                        id: row.id
                    }).then(({data}) => {
                        if (data.success) {
                            this.$message.success("移除成功")
                            this.loadDataBook()
                        } else {
                            this.$message.error(data.message)
                        }
                        this.loading = false
                    })
                })
            },
            //删除位置
            deletePosition() {
                this.$confirm('确定删除这个密集架吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('/position/delPosition', {
                        positionId: this.positionDetails.id,
                        isdelete: true
                    })
                        .then(({data}) => {
                            if (data.success) {
                                this.$message.success("删除成功")
                                this.$router.back()
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                })
            },
            //更换状态
            updateStatus() {
                this.$confirm('您确定要更换状态吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$http.post('/position/updatePositionStatus', {
                        positionId: this.positionDetails.id
                    })
                        .then(({data}) => {
                            if (data.success) {
                                this.$message.success(data.message)
                                this.loadDataBook()
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                })
            },
            changeInput(v) {
                this.thisbarCode = v
            },
            //添加图书到书架
            addBook() {
                if (this.thisbarCode === '') {
                    return this.$message.error("档号不能为空")
                }
                this.dataForm.cn084 = this.thisbarCode
                this.dataForm.bookCaseNo = this.bookCaseNo
                this.dataForm.ABsurface = this.ABsurface
                this.dataForm.layerNo = this.layerNo
                this.dataForm.layerNum = this.layerNum
                this.dataForm.libId = this.positionDetails.libId
                this.dataForm.locId = this.positionDetails.locId
                this.dataForm.update = false
                if (this.count < this.positionDetails.bookCount) {
                    // this.$refs.addBook.editForm(this.dataForm)
                    this.saveForm(this.dataForm)
                } else {
                    return this.$message.error("放不下了，换个位置吧")
                }
            },
            saveForm(v) {
                this.loading = true
                const param = Object.assign(v)
                this.$http.post('/position/addPositionForBook', param)
                    .then(({data}) => {
                        if (data.success) {
                            this.$message.success(data.message)
                            this.loadDataBook()
                            this.loading = false

                        } else {
                            this.$message.error(data.message)
                            this.loading = false
                        }
                    })
            },
            addBooks() {
                this.dataForm.ABsurface = this.ABsurface
                this.dataForm.bookCaseNo = this.bookCaseNo
                this.dataForm.layerNum = this.layerNum
                this.dataForm.layerNo = this.layerNo
                this.dataForm.locId = this.positionDetails.locId
                if (this.count < this.positionDetails.bookCount) {
                    this.dataForm.num = this.positionDetails.bookCount - this.count
                    this.$refs.addBooks.editForm(this.dataForm)
                } else {
                    // this.$refs.addBooks.editForm(this.dataForm)
                    return this.$message.error("放不下了，换个位置吧")
                }
            },
            click(r) {
                this.$router.push({path: '/archive/kufang/book/detail', query: {barCode: r.barCode}})
            },
            updateArchiveStatus(row) {
                this.row = row
                this.dialogVisible = true
                this.form.saveStatus = row.saveStatus
            },
            saveArchiveStatus() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        this.$http.post("/positionArchive/updatePositionArchiveStatus", {
                            Id: this.row.id,
                            saveStatus: this.form.saveStatus
                        })
                            .then(({data}) => {
                                if (data.success) {
                                    this.$message.success("修改成功")
                                    this.dialogVisible = false
                                    this.getBooksBybookCaseId()
                                }
                            })
                    }
                })
            },
            positionDetailsJump(row) {
                this.$router.push({
                    path: '/entityfiles/entity/detail',
                    query: {id: row.id, parentId: row.parentId, box: row.archiveBox}
                })
            },
        }
    }
</script>
<style lang="scss" scoped>
    .card {
        .el-card {
            margin: 5px 2px;

            .el-form {
                .el-form-item {
                    width: 30%;
                    margin-bottom: 2px;
                }
            }

            .el-card__header, .el-card__body {
                padding: 10px;
                clear: both;
            }
        }

    }

    .bookPerson {
        .finance {
            text-align: center;
            font-size: 30px;

            .el-button {
                font-size: 30px;
                font-weight: bold;
            }
        }
    }

    .word {
        text-align: center;
        font-size: 30px;
    }

    .xxx {
        text-align: left;
        padding-left: 20px;

    }

    .el-form-item__label {
        text-align: justify;
    }

</style>
