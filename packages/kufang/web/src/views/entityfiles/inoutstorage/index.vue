<template>
    <section>
        <nac-info/>
        <div>
            <div>
                <el-row>
                    <el-col :span="3" style="height: 50px;" :offset="9">
                        <el-button style="text-align: center;width: 100%;height: 100%;" type="success" @click="rrr=true"
                                   :disabled="rrr">
                            <span style="font-size: 20px;text-align: center;">入 库</span>
                        </el-button>
                    </el-col>
                    <el-col :span="3" style="height: 50px;">
                        <el-button style="text-align: center;width: 100%;height: 100%;" type="primary"
                                   @click="rrr=false"
                                   :disabled="!rrr">
                            <span style="font-size: 20px;text-align: center;">出 库</span>
                        </el-button>
                    </el-col>
                </el-row>
            </div>
            <div v-if="rrr">
                <el-card style="height: 950px;">
                    <el-form inline :model="kufang" ref="form" :rules="rules">

                        <div style="text-align: center;width: 100%;font-weight: bolder;font-size: 28px;">
                            档案入库
                        </div>
                        <br>
                        <br>
                        <br>
                        <div style="margin: 5px 130px ">
                            <el-form-item label="库房：" prop="proplocId">
                                <el-select v-model="kufang.locId" placeholder="请选择库房" clearable style="width: 200px;"
                                           filterable @change="getArea">
                                    <el-option v-for="item in locIds" :key="item.id" :label="item.name"
                                               :value="item.id"/>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="区：" prop="propareaId">
                                <el-select v-model="kufang.areaId" placeholder="请选择区" clearable style="width: 200px;"
                                           filterable @change="getBookCaseNo">
                                    <el-option v-for="item in areas" :key="item.id" :label="item.areaName"
                                               :value="item.id"/>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="密集架：" prop="propCaseId">
                                <el-select v-model="kufang.caseId" placeholder="请选择密集架" @change="selectLayerLie"
                                           style="width: 200px;">
                                    <el-option v-for="item in bookCaseNos" :key="item.id" :label="item.bookCaseNo"
                                               :value="item.id"/>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="AB面：">
                                <el-select v-if="abcNum==1" v-model="kufang.columnNum" placeholder="请选择AB面"
                                           style="width: 200px;">
                                    <el-option value="1" label="A面"/>
                                </el-select>
                                <el-select v-else v-model="kufang.columnNum" placeholder="请选择AB面" style="width: 200px;">
                                    <el-option value="1" label="A面"/>
                                    <el-option value="2" label="B面"/>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="列号：" prop="propColumnNo">
                                <el-select v-model="kufang.columnNo" style="width: 200px;">
                                    <el-option v-for="item in lies" :key="item" :label="item" :value="item"/>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="层号：" prop="propLayer">
                                <el-select v-model="kufang.layer" style="width: 200px;">
                                    <el-option v-for="item in layers" :key="item" :label="item" :value="item"/>
                                </el-select>
                            </el-form-item>
                            <br>

                            <br>
                            <div style="text-align: center;width: 100%;font-weight: bolder;font-size: 28px;">
                                <el-form-item label="档案：" prop="code">
                                    <el-input v-model="code" @input="saveEditForm" placeholder="请扫描实体档案二维码或输入档号"
                                              style="width: 300px;"
                                              type="textarea" :rows="5" clearable/>
                                </el-form-item>
                                <br>
                                <span class="el-icon-info" style="color: red" @click="showhidden"
                                      v-show="showhiddenspan">
                  注：扫码时请确保当前输入法为英文
                </span>
                                <br>
                                <br>
                                <el-form-item>
                                    <el-button @click="showhidden" type="danger">提示</el-button>
                                    <el-button @click="cancel" type="info">重置</el-button>
                                    <el-button type="primary" @click="updateEntity" v-loading="loading">手动保存</el-button>
                                </el-form-item>
                            </div>

                        </div>
                    </el-form>

                    <div>
                        <h4>临时记录</h4>
                        <el-table :data="hisData" stripe style="width: 100%"
                                  :default-sort="{prop: 'index', order: 'descending'}">
                            <el-table-column prop="index" label="序号" width="100" sortable/>
                            <el-table-column prop="old" label="原始位置"/>
                            <el-table-column prop="new" label="绑定位置"/>
                            <el-table-column width="200" prop="address" label="记录时间"/>
                        </el-table>
                    </div>
                </el-card>
            </div>
            <div v-else>
                <el-card style="height: 750px;">
                    <el-form inline ref="form">
                        <div style="text-align: center;width: 100%;font-weight: bolder;font-size: 28px;">
                            档案出库
                        </div>
                        <br>
                        <br>
                        <div style="text-align: center;width: 100%;font-weight: bolder;font-size: 28px;">
                            <el-form-item label="档案：" prop="code">
                                <el-input v-model="code2" @input="saveOut" placeholder="请扫描实体档案二维码或输入档号"
                                          style="width: 300px;"
                                          type="textarea"
                                          :rows="5" clearable/>
                            </el-form-item>
                            <br>
                            <span class="el-icon-info" style="color: red" @click="showhidden" v-show="showhiddenspan">
                注：扫码时请确保当前输入法为英文
              </span>
                            <br>
                            <br>
                            <el-form-item>
                                <el-button @click="showhidden" type="danger">提示</el-button>
                                <el-button @click="cancel" type="info">重置</el-button>
                                <el-button type="primary" @click="saveOutManual" v-loading="loading">手动保存</el-button>
                            </el-form-item>
                        </div>
                    </el-form>
                    <div>
                        <h4>临时记录</h4>
                        <el-table :data="hisData2" stripe style="width: 100%"
                                  :default-sort="{prop: 'index', order: 'descending'}">
                            <el-table-column prop="index" width="100" label="序号" sortable/>
                            <el-table-column prop="old" label="原始位置"/>
                            <el-table-column width="200" prop="address" label="记录时间"/>
                        </el-table>
                    </div>
                </el-card>
            </div>


        </div>
    </section>
</template>

<script>
    export default {
        name: "index",
        data() {
            return {
                kufang: {},
                areas: [],
                locIds: [],
                bookCaseNos: [],
                loading: false,
                rrr: true,
                showhiddenspan: true,
                abcNum: '',
                code: '',
                code2: '',
                codejilu1: '',
                codejilu2: '',
                code3: '',
                hisData: [],
                hisData2: [],
                outData: [],
                layers: 0,
                lies: 0,

                targetingJudgment: '',
                reasonDisData: [],
                rules: {
                    propLocId: [{required: true, trigger: 'blur', message: '库房不能为空'}],
                    propCaseId: [{required: true, trigger: 'blur', message: '列号不能为空'}],
                    propColumnNo: [{required: true, trigger: 'blur', message: '节号不能为空'}],
                    propLayer: [{required: true, trigger: 'blur', message: '层号不能为空'}]
                }
            }
        },
        methods: {
            showhidden() {
                this.showhiddenspan = !this.showhiddenspan
            },
            $init() {
                this.getLoc()
                this.reasonDis()
            },

            getLoc() {
                this.$http.post('locationkufang/page', {page: false})
                    .then(({data}) => {
                        if (data.success) {
                            this.locIds = data.data
                            // this.getBookCaseNo(this.locIds[0].id)
                            this.getArea(this.locIds[0].id)
                        } else {
                            this.locIds = []
                        }
                    })
            },
            changeStyle(status, className) {
                let dom = document.querySelectorAll(className);
                dom[0].style.display = status
            },
            handleSelect(item) {

                this.$refs.test.suggestions = [];
                //this.changeStyle("none", ".el-autocomplete-suggestion");

                this.$refs.test.focus()
                this.targetingJudgment = 'two'

            },
            reasonDis() {
                this.$http.post("entityFiles/reasonDis").then(({data}) => {
                    this.reasonDisData = data.data
                })
            },
            querySearch(queryString, cb) {
                console.log("querySearch")
                const restaurants = this.reasonDisData;
                const results = queryString ? restaurants.filter(this.createFilter(queryString)) : restaurants;
                // 调用 callback 返回建议列表的数据
                cb(results);
                if (this.targetingJudgment === 'two') {
                    this.$refs.test.suggestions = [];
                    this.targetingJudgment = ''
                }
            },

            createFilter(queryString) {
                return (restaurant) => {
                    return (restaurant.value.toLowerCase().match(queryString.toLowerCase()) === 0);
                };
            },
            detail(v) {
                this.$http.post('entityFiles/detail', {id: v})
                    .then(({data}) => {
                        if (data.success) {
                        } else {
                            this.outData = data.data
                            this.outData = []
                            this.$message.error(data.message);
                        }
                    })
            },
            async getArea(v) {
                this.kufang = {locId: v}
                this.bookCaseNos = []
                this.lies = []
                this.layers = []
                const {data} = await this.$http.post('/kufangArea/page', {kufangId: v, page: false})
                if (data.success) {
                    this.areas = data.data
                    console.log(this.areas)
                } else {
                    this.areas = []
                }
            },
            getBookCaseNo(v) {
                this.kufang = {
                    locId: this.kufang.locId,
                    areaId: v
                }
                this.bookCaseNos = []
                this.lies = []
                this.layers = []
                this.$http.post('/position/page', {locId: this.kufang.locId, areaId: this.kufang.areaId, page: false})
                    .then(({data}) => {
                        if (data.success) {
                            this.bookCaseNos = data.data
                        } else {
                            this.bookCaseNos = []
                        }
                    })

            },
            //查询 列和层
            selectLayerLie(v) {
                this.kufang = {
                    locId: this.kufang.locId,
                    areaId:this.kufang.areaId,
                    caseId: v
                }
                this.lies = []
                this.layers = []
                this.bookCaseNos.forEach(a => {
                    if (a.id === v) {
                        this.layers = parseInt(a.layer)
                        this.abcNum = a.columnNum
                        this.lies = parseInt(a.columnNo)
                    }
                })
            },

            //入库
            saveEditForm: function () {
                // console.log('保存=====', this.code)
                if (this.code.includes('#')) {
                    let arr = this.code.split('#');
                    // let id = arr[0].split(':')[0];
                    let id = arr[0];
                    if (this.codejilu1.includes(id)) {
                        return;
                    }
                    this.codejilu1 = id

                    if (this.kufang.locId == null || this.kufang.locId == '') {
                        this.$message.error("库房不能为空")
                        return
                    }
                    if (this.kufang.caseId == null || this.kufang.caseId == '') {
                        this.$message.error("列号不能为空")
                        return
                    }
                    if (this.kufang.columnNum == null || this.kufang.columnNum === '') {
                        this.$message.error("AB面不能为空")
                        return
                    }
                    if (this.kufang.columnNo == null || this.kufang.columnNo == '') {
                        this.$message.error("节号不能为空")
                        return
                    }
                    if (this.kufang.layer == null || this.kufang.layer == '') {
                        this.$message.error("层号不能为空")
                        return
                    }
                    this.getEntityFilesById(id, true, 1)

                }
            },
            //手动入库
            updateEntity() {
                if (this.kufang.locId == null || this.kufang.locId == '') {
                    this.$message.error("库房不能为空")
                    return
                }
                if (this.kufang.areaId == null || this.kufang.areaId == '') {
                    this.$message.error("区不能为空")
                    return
                }
                if (this.kufang.caseId == null || this.kufang.caseId == '') {
                    this.$message.error("密集架不能为空")
                    return
                }
                if (this.kufang.columnNum == null || this.kufang.columnNum === '') {
                    this.$message.error("AB面不能为空")
                    return
                }
                if (this.kufang.columnNo == null || this.kufang.columnNo == '') {
                    this.$message.error("列号不能为空")
                    return
                }
                if (this.kufang.layer == null || this.kufang.layer == '') {
                    this.$message.error("层号不能为空")
                    return
                }
                if (this.code == null || this.code == '') {
                    this.$message.error("档案不能为空")
                    return
                }
                this.getEntityFiles(this.code, true, 2)

            },

            getEntityFiles(v, boo, ff) {
                this.loading = true
                this.$http.post('entityFiles/getEntityFiles', {archiveCode: v})
                    .then(({data}) => {
                        if (data.success) {
                            this.outData = data.data
                            this.loading = false
                            this.getEntityFilesById(this.outData.id, boo, ff)
                        } else {
                            this.outData = []
                            this.$message.error(data.message);
                        }
                    })
                this.loading = false
            },
            getEntityFilesById(v, boo, ff) {
                this.loading = true
                this.$http.post('entityFiles/getDes', {id: v})
                    .then(({data}) => {
                        if (data.success) {
                            this.loading = false
                            if (ff === 1) {
                                //扫描入库
                                this.$http.post('/entityFiles/bindPosition', {
                                    id: this.codejilu1,
                                    locId: this.kufang.locId,
                                    areaId:this.kufang.areaId,
                                    caseId: this.kufang.caseId,
                                    caseNo: this.kufang.caseNo,
                                    columnNum: this.kufang.columnNum,
                                    columnNo: this.kufang.columnNo,
                                    layer: this.kufang.layer
                                }).then(({data}) => {
                                    if (data.success) {
                                        this.$message.success('入库成功')
                                    } else {
                                        this.$message.error(data.message)
                                    }
                                })
                            } else if (ff === 2) {
                                // 手动入库
                                this.$http.post('/entityFiles/bindPositionManual', {
                                    archiveId: this.code,
                                    locId: this.kufang.locId,
                                    areaId:this.kufang.areaId,
                                    caseId: this.kufang.caseId,
                                    caseNo: this.kufang.caseNo,
                                    columnNum: this.kufang.columnNum,
                                    columnNo: this.kufang.columnNo,
                                    layer: this.kufang.layer
                                }).then(({data}) => {
                                    if (data.success) {
                                        this.$message.success('入库成功')
                                    } else {
                                        this.$message.error(data.message)
                                    }
                                })
                            } else if (ff === 3) {
                                // 扫描出库
                                this.$http.post('/entityFiles/unbindPosition', {
                                    id: this.codejilu2,
                                }).then(({data}) => {
                                    if (data.success) {
                                        this.code2 = ''
                                        this.$message.success('出库成功')

                                    }
                                })
                            } else {
                                // 手动出库
                                this.$http.post('/entityFiles/unbindPositionManual', {
                                    archiveCode: this.code2,
                                }).then(({data}) => {
                                    if (data.success) {
                                        this.$message.success('出库成功')
                                    }
                                })
                            }


                            this.des = data.data
                            let loc = ''
                            this.locIds.forEach(v => {
                                if (v.id === this.kufang.locId) {
                                    loc = v.name
                                }
                            })
                            let caseNo = ''
                            this.bookCaseNos.forEach(v => {
                                if (v.id === this.kufang.caseId) {
                                    caseNo = v.bookCaseNo
                                }

                            })
                            let time = new Date();
                            let mytime = time.toLocaleTimeString();
                            let ab = this.kufang.columnNum === '1' ? 'A面' : 'B面'
                            let newadd = loc + caseNo + ab + this.kufang.columnNo + '节' + this.kufang.layer + '层'
                            if (!boo) {
                                newadd = ''
                            }

                            if (!boo) {
                                let a = {
                                    'index': this.hisData2.length + 1,
                                    'old': this.des,
                                    'address': mytime
                                }
                                this.hisData2.push(a)
                            } else {
                                let a = {
                                    'index': this.hisData.length + 1,
                                    'old': this.des,
                                    'new': newadd,
                                    'address': mytime
                                }
                                this.hisData.push(a)
                            }

                            this.code3 = ''
                            this.code2 = ''
                            this.code = ''
                        } else {
                            this.des = ""
                        }
                    })
                this.loading = false
            },
            cancel() {
                this.code = ''
                this.code2 = ''
                this.code3 = ''
                this.codejilu1 = ''
                this.codejilu2 = ''
                this.des = ''
                this.hisData = []
                this.hisData2 = []

            },


            // 出库原因
            // 扫描出库
            saveOut() {
                //出库

                if (this.code2.includes('#')) {
                    let arr = this.code2.split('#');
                    // let id = arr[0].split(':')[0];
                    let id = arr[0];
                    if (this.codejilu2.includes(id)) {
                        return;
                    }
                    this.codejilu2 = id
                    this.getEntityFilesById(id, false, 3)

                }
            },
            //手动出库
            saveOutManual() {
                if (this.code2 == null || this.code2 === '') {
                    this.$message.error("档案不能为空")
                    return
                }
                this.getEntityFiles(this.code2, false, 4)

            },

        },
        destroyed() {
            this.hisData = []
            this.hisData2 = []
        }
    }
</script>

<style scoped>

</style>
