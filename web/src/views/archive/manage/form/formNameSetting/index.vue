<template>
    <div>
        <el-dialog title="选择排序字段" :visible.sync="addOrUpdateVisible"
                   width="50%" height="50%" :before-close="exit">
            <el-row style="height: 100%">
                <el-col :span="9" style="height: 50%">
                    <el-table ref="field" title="排序准备字段"
                              :data="field" height="400px" style="overflow: auto"
                              tooltip-effect="dark">
                        <el-table-column label="序号" align="center" width="50">
                            <template slot-scope="scope">
                                {{ scope.$index + 1 }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="字段" width="100vw">
                        </el-table-column>
                        <el-table-column label="操作" width="80vw">
                            <template slot-scope="scope">
                                <el-button icon="el-icon-plus" type="primary" size="mini" circle
                                           @click="add(scope.$index,scope.row)"></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
                <el-col :span="15">
                    <el-table ref="field" title="排序字段" height="400px"
                              style="overflow: auto" :data="pitchItems" tooltip-effect="dark">
                        <el-table-column label="序号" align="center" width="50px">
                            <template slot-scope="scope">
                                {{ scope.$index + 1 }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="connector" label="连接符" width="80px">
                            <template slot-scope="scope">
                                <el-select v-model="scope.row.connector">
                                    <el-option v-for="item in options"
                                               :value="item.value"
                                               :key="item.label"
                                               :label="item.value"/>
                                </el-select>
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="字段" width="80px">
                        </el-table-column>
                        <el-table-column label="操作" width="80px">
                            <template slot-scope="scope">
                                <el-button icon="el-icon-minus" type="primary" size="mini" circle
                                           @click="subtract(scope.$index,scope.row)"></el-button>
                                <el-button icon="el-icon-arrow-up" type="primary" size="mini" circle
                                           @click="up(scope.$index,scope.row)"></el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div style="margin:10px 60px;height: 100px;">
                        <div class="bg">
                            <div style="color: red;">
                                <span v-for="(item,i) in pitchOnTrue" :key="item.name" style="height: 50px">
                                  <span class="list-img red" v-if="i===pitchOnTrue.length-1">{{ item.name }}</span>
                                  <span v-else>{{ item.name }}{{ item.connector }}</span>
                                </span>
                            </div>
                        </div>
                    </div>
                </el-col>
                <div align="center" style="float: right">
                    <el-button type="info" @click="exit">取 消</el-button>
                    <el-button type="primary" @click="save" v-loading="loading">提 交</el-button>
                </div>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
    export default {
        props: ['addOrUpdateVisible', 'field', 'pitchOn', 'showSchemeId'],
        data() {
            return {
                options: [{value: '-', label: '-'}, {value: '·', label: '·'}],
                deleteItem: [],
                pitchItems: [],
                loading: false,
                transfer: {}
            }
        },
        methods: {
            //关闭窗口提示
            handleClose(done) {
                this.$confirm('确认关闭？').then(() => {
                    done();
                }).catch(() => {
                });
            },
            //添加排序字段
            add(index, row) {
                const len = this.pitchItems.length
                row.order = len + 1
                row.connector = "-"
                //将id设为null,避免已有档号规则移动过来时候携带id,无法实现删除
                row.id = null
                this.field.splice(index, 1)
                this.pitchItems.push(row)
            },
            //将选中的排序字段删除
            subtract(index, row) {
                this.pitchItems.splice(index, 1)
                this.field.push(row)
                //只获取有id的规则字段，只有有id的才用删除。
                if (row.id) {
                    this.deleteItem.push(row.id)
                }
            },
            //将点击的字段排序上移一位
            up(index, row) {
                if (index !== 0) {
                    this.pitchItems.splice(index, 1)
                    this.pitchItems.splice(index - 1, 0, row)
                }
            },
            //返回时将字段值全部放入field中
            exit() {
                this.$emit('close', false);
                this.$emit("fun")
                this.pitchItems = []
            },
            //将字段信息存入catalog对象中
            save() {
                this.loading = true
                for (let i = 0; i < this.pitchItems.length; i++) {
                    this.pitchItems[i].order = i + 1
                    this.pitchItems[i].businessId = this.showSchemeId
                }
                if (this.deleteItem.length > 0) {
                    for (const argument of this.deleteItem) {
                        this.$http.post('/codingscheme/schemeitem/delete', {id: argument}).then(({data}) => {
                            if (!data.success) {
                                //只要出错的提示
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        }).finally(() => {
                            this.deleteItem = []
                            this.saveAll()
                        })
                    }
                } else {
                    this.saveAll()
                }
            },
            saveAll() {
                for (const pitchOnElement of this.pitchItems) {
                    if (pitchOnElement.id) {
                        this.$http.post('/codingscheme/schemeitem/update', pitchOnElement).then(({data}) => {
                            if (data.success) {
                                this.loading = false
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                    } else {
                        this.$http.post('/codingscheme/schemeitem/insert', pitchOnElement).then(({data}) => {
                            if (data.success) {
                                this.loading = false
                            } else {
                                this.$message.error(data.message)
                            }
                            this.loading = false
                        })
                    }
                }
                this.exit()
            }
        },
        computed: {
            pitchOnTrue() {
                return this.pitchOn.filter(i => i.name)
            }
        },
        watch: {
            'pitchOn': {
                handler() {
                    this.pitchItems = this.pitchOn
                }
            }
        },
    }
</script>
<style lang="scss">
    .bg {
        background-color: #eeeeee;
        height: auto;
        width: 24vw;
        border-radius: 20px;
        text-align: center;
        padding: 15px;
        max-height: 70px;
        overflow: auto;
    }

    .btn-submit {
        width: 101px;
        height: 37px;
        background-blend-mode: overlay, normal;
        border-radius: 18px;
    }

    .btn-cancel {
        width: 101px;
        height: 37px;
        background-blend-mode: overlay, normal;
        border-radius: 18px;
    }

    .el-table__header, .el-table__body, .el-table__footer {
        width: 96% !important;
    }

    .el-table::before {
        height: 0;
    }

    .el-table__fixed::before, .el-table__fixed-right::before {
        height: 0;
    }
</style>
