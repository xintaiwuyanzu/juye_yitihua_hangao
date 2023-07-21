<template>
    <section>
        <nac-info back title="实体档案详情"/>
        <div class="index_main">
            <el-row justify="space-between" type="flex">
                <el-col :span="12">
                    <el-card style="height: 30vh">
                        <div slot="header">
                            <span>档案信息</span>
                        </div>
                        <el-form :data="data">
                            <el-row>
                                <el-col :span="24">
                                    <el-form-item label="档案名称：">{{ data.title }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="档案档号：">{{ data.archiveCode }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="入库时间：">{{ data.createDate|datetime }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="档案类型：">{{ data.archiveType|getTypeName }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="状态：">{{ data.status === '1' ? '入库' : '出库' }}</el-form-item>
                                </el-col>
                            </el-row>

                        </el-form>
                    </el-card>
                </el-col>
                <el-col :span="6" style="margin-left: 2px">
                    <el-card style="height: 30vh">
                        <div slot="header">
                            <span>位置信息</span>
                            <el-link style="float: right;margin-left: 10px;color: #409EFF" @click="toArchive"
                                     size="mini" type="text">
                                详情
                            </el-link>
                        </div>
                        <el-form>
                            <el-row>
                                <el-col :span="12">
                                    <el-form-item label="库房号：">{{ locName }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="区：">{{ data.areaNo }}</el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    <el-form-item label="密集架：">{{ data.caseNo }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="AB面：">{{ data.columnNum }}</el-form-item>
                                </el-col>
                            </el-row>
                            <el-row>
                                <el-col :span="12">
                                    <el-form-item label="节号：">{{ data.columnNo }}</el-form-item>
                                </el-col>
                                <el-col :span="12">
                                    <el-form-item label="层号：">{{ data.layer }}</el-form-item>
                                </el-col>
                            </el-row>

                        </el-form>
                    </el-card>
                </el-col>
                <el-col :span="6" style="margin-left: 2px">
                    <el-card v-loading="loadingqcode" style="height: 30vh">
                        <div slot="header">
                            <span>档案二维码</span>
                        </div>
                        <div align="center">
                            <div id="qrCode" ref="qrCodeDiv" v-show="showE" style="margin-top: 20px;"/>
                            <div style="padding-top: 15px;">
                                <el-button type="primary" round @click="shuaxin">刷新</el-button>
                                <el-button type="primary" round @click="dayin">打印</el-button>
                            </div>
                        </div>
                    </el-card>
                </el-col>
            </el-row>
            <el-row style="margin-top: 5px">
                <el-card v-if="accessRecord">
                    <div slot="header">
                        <span>进出库记录</span>
                        <el-button v-if="accessRecord" style="float: right;margin-left: 10px;color: #409EFF"
                                   @click="goArchive"
                                   size="mini" type="text">返回
                        </el-button>
                    </div>
                    <el-table :data="data3" border height="40vh">
                        <el-table-column label="排序" fixed align="center" width="50" type="index"/>
                        <el-table-column label="档案名称" align="center" header-align="center"
                                         show-overflow-tooltip prop="title">
                        </el-table-column>
                        <el-table-column label="档号/盒号" align="center" header-align="center"
                                         show-overflow-tooltip prop="archiveCode">
                        </el-table-column>
                        <el-table-column label="类型" align="center"
                                         header-align="center">
                            <template v-slot="scope">
                                {{ scope.row.doType|dict({'0': '出库', '1': '入库'}) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="记录时间" width="140" align="center"
                                         header-align="center">
                            <template v-slot="scope">
                                {{ scope.row.createDate|datetime }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="personName" label="经手人" width="120" align="center"
                                         header-align="center"/>
                        <el-table-column prop="remarks" label="备 注" align="center" header-align="center"
                                         show-overflow-tooltip/>
                    </el-table>
                </el-card>
                <el-card v-if="!accessRecord">
                    <div slot="header">
                        <span>档案盒详情</span>
                        <!--<el-link v-if="accessRecord"  style="float: right;margin-left: 10px" @click="goArchive" size="mini" type="text">返回</el-link>-->
                    </div>
                    <el-table :data="data5" border height="40vh" @row-click="editCurrentApplicationApproval">
                        <el-table-column type="selection" width="50" align="center"/>
                        <el-table-column label="排序" fixed align="center" width="50" type="index"/>
                        <el-table-column label="题名" prop="title" show-overflow-tooltip align="center"/>
                        <el-table-column label="档号" prop="archiveCode" show-overflow-tooltip width="250"
                                         align="center" header-align="center"/>
                        <el-table-column label="类型" prop="archiveType" show-overflow-tooltip width="100"
                                         align="center">
                            <template v-slot="scope">
                                {{ scope.row.archiveType |getTypeName }}
                            </template>
                        </el-table-column>
                        <el-table-column label="件/盒" prop="archiveBox" show-overflow-tooltip width="50"
                                         align="center">
                            <template v-slot="scope">
                                {{ scope.row.archiveBox|dict({'1': "件", '2': "盒"}) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="状态" prop="status" align="center" width="80">
                            <template v-slot="scope">
                                {{ scope.row.status|dict({'0': "出库", '1': "入库"}) }}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" header-align="center" width="220">
                            <template v-slot="scope">
                                <el-link type="success" size="mini" @click="getHisId(scope.row.id)">查看详情</el-link>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-card>
            </el-row>
        </div>
    </section>
</template>

<script>
    import QRCode from 'qrcodejs2';

    let types = []
    export default {
        name: "index",
        data() {
            return {
                loadingqcode: false,
                value2: true,
                showE: true,
                isShow: false,
                accessRecord: true,
                rowsone: {},
                url: '',
                locName: '',
                id: '',
                caseId: '',
                ab: '',
                data: [],
                data2: [],
                data3: [],
                data5: [],
                tableData: [],
                typeData: {},
                locIds: [],
            }
        },
        filters: {
            getTypeName(v) {
                if (v) {
                    let loc = types.find(d => d.id === v)
                    if (loc) {
                        return loc.archiveTypeName
                    }
                }
            },
        },
        methods: {
            $init() {
                this.loadData()
            },
            loadData() {
                this.getLoc()
                this.getTypes()
                this.getOne()
                this.getAttebutes()
                if (this.$route.query.box == '2') {
                    this.accessRecord = false
                    this.getEntityFilesBox(this.$route.query.id)
                } else if (this.$route.query.parentId) {
                    this.accessRecord = false
                    this.getEntityFilesBox(this.$route.query.parentId)
                } else {
                    this.getHis()
                }
            },
            getTypes() {
                this.$http.post('archiveType/page', {page: false})
                    .then(({data}) => {
                        if (data.success) {
                            types = data.data
                        } else {
                            types = []
                        }
                    })
            },
            getLoc() {
                this.$http.post('locationkufang/page', {page: false})
                    .then(({data}) => {
                        if (data.success) {
                            this.locIds = data.data
                        } else {
                            this.locIds = []
                        }
                    })
            },
            show() {
                this.locIds.forEach(v => {
                    if (v.id === this.data.locId) {
                        this.locName = v.name
                    }
                })

                this.url = "id:" + this.data.id + "#档号:" + this.data.archiveCode
                    + "--类型:" + this.typeData.archiveTypeName
                    + "--位置:" + this.locName + this.data.areaNo + '区' + this.data.caseNo + this.data.columnNo + '节' + this.data.layer + '层'
                    + "--随机数:" + new Date().getTime() + "endshow"
                this.bindQRCode()
            },
            getOne() {
                this.$http.post('entityFiles/detail', {id: this.id})
                    .then(({data}) => {
                        if (data.success) {
                            this.data = data.data
                            this.caseId = data.data.caseId
                            this.getTypespage(this.data.archiveType)

                            this.getLayerDetailByIDandAB()
                        } else {
                            this.data = []
                        }
                    })
            },
            getAttebutes() {
                this.$http.post('propertyValue/page', {entityId: this.id, page: false})
                    .then(({data}) => {
                        if (data.success) {
                            this.data2 = data.data
                        } else {
                            this.data2 = []
                        }
                    })
            },
            //该档案的进出馆记录
            getHis() {
                this.$http.post('his/page', {page: false, archiveId: this.id})
                    .then(({data}) => {
                        if (data.success) {
                            this.data3 = data.data
                        } else {
                            this.data3 = []
                        }
                    })
            },
            //该档案的进出馆记录
            getHisId(id) {
                this.$http.post('his/page', {page: false, archiveId: id})
                    .then(({data}) => {
                        if (data.success) {
                            this.data3 = data.data
                            this.accessRecord = true
                        } else {
                            this.data3 = []
                        }
                    })
            },
            getEntityFilesBox(parentId) {
                this.$http.post('/entityFiles/page', {page: false, parentId: parentId})
                    .then(({data}) => {
                        if (data.success) {
                            this.data5 = data.data
                        } else {
                            this.data5 = []
                        }
                    })
            },
            editCurrentApplicationApproval(row) {
                this.$http.post('entityFiles/detail', {id: row.id})
                    .then(({data}) => {
                        if (data.success) {
                            this.data = data.data
                            this.caseId = data.data.caseId
                            this.getTypespage(this.data.archiveType)

                            this.getLayerDetailByIDandAB()
                        } else {
                            this.data = []
                        }
                    })
            },
            getTypespage(v) {
                this.loading = true
                this.$http.post('archiveType/detail', {page: false, id: v})
                    .then(({data}) => {
                        if (data.success) {
                            this.typeData = data.data
                            this.show()
                        } else {
                            this.typeData = []
                        }
                        this.loading = false
                    })
            },

            //初始化，获取页面数据


            getLayerDetailByIDandAB() {
                this.$http.post('/position/getLayerDetail', {positionId: this.caseId, ab: this.ab})
                    .then(({data}) => {
                        if (data.success) {
                            this.tableData = data.data
                            this.width = data.data[0].length * 112 + 'px'
                            this.isShow = true
                            this.tableData.forEach(value => {
                                value.forEach(val => {
                                    let css = {color: 'white', fontFamily: '楷体'}
                                    if (val.layerId === this.data.layerId) {
                                        css.backgroundColor = 'green'
                                        val.css = css
                                    } else {
                                        css.color = 'green'
                                        val.css = css
                                    }
                                    val.type = ''
                                })
                            })
                        } else {
                            this.$message.error(data.message)
                        }
                        this.loading = false
                    })
            },
            //位置信息详情跳转
            toArchive() {
                this.$router.push({
                    path: '/entityfiles/entity/location/denseframe/detail',
                    query: {id: this.data.caseId, entityFiles: this.data}
                })
            },
            goArchive() {
                this.accessRecord = false
            },
            //二维码生成
            bindQRCode() {
                //清空div
                let qr = document.getElementById('qrCode');
                qr.innerHTML = "<b></b>"

                new QRCode(this.$refs.qrCodeDiv, {
                    text: this.url,
                    width: 150,
                    height: 150,
                    colorDark: "#333333", //二维码颜色
                    colorLight: "#ffffff", //二维码背景色
                    correctLevel: QRCode.CorrectLevel.L//容错率，L/M/H
                })
            },

            cancel() {
                this.rowsone = []
            },
            //打印
            dayin() {
                let url = "api/entityFiles/erweima?id=" +
                    this.id
                window.open(url)
            },
            shuaxin() {
                this.loadingqcode = true
                this.show()
                this.loadingqcode = false
            }
        },
        mounted: function () {
            this.id = this.$route.query.id
        }

    }
</script>

<style scoped>
    .el-row {
        margin-bottom: 0px;
    }

    #leftcol .el-col {
        padding-top: 20px;
    }

</style>
