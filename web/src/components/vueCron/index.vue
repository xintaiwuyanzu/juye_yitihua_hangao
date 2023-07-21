<style lang="scss" scoped>
    #changeContab {
        .language {
            position: absolute;
            right: 25px;
            z-index: 1;
        }

        .el-tabs {
            box-shadow: none;
        }

        .tabBody {
            .el-row {
                margin: 10px 0;

                .long {
                    .el-select {
                        width: 350px;
                    }
                }

                .el-input-number {
                    width: 110px;
                }
            }
        }

        .bottom {
            width: 100%;
            text-align: center;
            margin-top: 5px;
            position: relative;

            .value {
                font-size: 18px;
                vertical-align: middle;
            }
        }
    }
</style>
<template>
    <div id="changeContab">
        <!--<el-button class="language" type="text" @click="i18n=(i18n==='en'?'cn':'en')">{{i18n}}</el-button>-->
        <el-tabs type="border-card">
            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Seconds.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="second.cronEvery" label="3">{{text.Seconds.specific}}
                            <el-select size="small" @change="display" filterable v-model="second.specificSpecific">
                                <el-option v-for="val in 60" v-bind:key="val" v-bind:value="val-1">{{val-1}}</el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Minutes.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="minute.cronEvery" label="3">{{text.Minutes.specific}}
                            <el-select size="small" @change="display" filterable v-model="minute.specificSpecific">
                                <el-option v-for="val in 60" v-bind:key="val" v-bind:value="val-1">{{val-1}}</el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Hours.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="hour.cronEvery" label="3">{{text.Hours.specific}}
                            <el-select size="small" @change="display" filterable v-model="hour.specificSpecific">
                                <el-option v-for="val in 24" v-bind:key="val" v-bind:value="val-1">{{val-1}}</el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Day.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="day.cronEvery" label="5">{{text.Day.specificDay}}
                            <el-select size="small" @change="display" filterable v-model="day.specificSpecific">
                                <el-option v-for="val in 31" v-bind:key="val" v-bind:value="val">{{val}}</el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Month.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="month.cronEvery" label="3">{{text.Month.specific}}
                            <el-select size="small" @change="display" filterable v-model="month.specificSpecific">
                                <el-option v-for="val in 12" v-bind:key="val" v-bind:label="val"
                                           v-bind:value="val"></el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>
<!--            <el-tab-pane>
                <span slot="label"><i class="el-icon-date"></i> {{text.Year.name}}</span>
                <div class="tabBody">
                    <el-row>
                        <el-radio class="long" v-model="year.cronEvery" label="3">{{text.Year.specific}}
                            <el-select size="small" @change="display" filterable v-model="year.specificSpecific">
                                <el-option v-for="val in 1000" v-bind:key="val" v-bind:label="1800+val"
                                           v-bind:value="1800+val"></el-option>
                            </el-select>
                        </el-radio>
                    </el-row>
                </div>
            </el-tab-pane>-->
        </el-tabs>
        <div class="bottom">
            <span class="value">{{this.cron}}</span>
           <!-- <el-link type="primary" @click="change">{{text.Save}}</el-link>-->
            <!--<el-button type="primary" @click="close">{{text.Close}}</el-button>-->
        </div>
    </div>
</template>
<script>
    import Language from './language'

    export default {
        name: 'vueCron',
        props: ['warning', 'handover'],
        data() {
            return {
                second: {
                    cronEvery: '3',
                    incrementStart: '3',
                    incrementIncrement: '5',
                    rangeStart: '',
                    rangeEnd: '',
                },
                minute: {
                    cronEvery: '3',
                    incrementStart: '3',
                    incrementIncrement: '5',
                    rangeStart: '',
                    rangeEnd: '',
                },
                hour: {
                    cronEvery: '3',
                    incrementStart: '3',
                    incrementIncrement: '5',
                    rangeStart: '',
                    rangeEnd: '',
                },
                day: {
                    cronEvery: '5',
                    incrementStart: '1',
                    incrementIncrement: '1',
                    rangeStart: '',
                    rangeEnd: '',
                    cronLastSpecificDomDay: 1,
                    cronDaysBeforeEomMinus: '',
                    cronDaysNearestWeekday: '',
                },
                week: {
                    cronEvery: '',
                    incrementStart: '1',
                    incrementIncrement: '1',
                    cronNthDayDay: 1,
                    cronNthDayNth: '1',
                },
                month: {
                    cronEvery: '3',
                    incrementStart: '3',
                    incrementIncrement: '5',
                    rangeStart: '',
                    rangeEnd: '',
                },
                year: {
                    cronEvery: '3',
                    incrementStart: '2017',
                    incrementIncrement: '1',
                    rangeStart: '',
                    rangeEnd: '',
                },
                output: {
                    second: '',
                    minute: '',
                    hour: '',
                    day: '',
                    month: '',
                    // Week: '',
                    year: '',
                }
            }
        },
        watch: {
            data() {
                this.rest(this.$data);
            }
        },
        computed: {
            text() {
                return Language
            },
            secondsText() {
                if (this.second.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.second.specificSpecific
                }
            },
            minutesText() {
                if (this.minute.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.minute.specificSpecific
                }
            },
            hoursText() {
                if (this.hour.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.hour.specificSpecific
                }
            },
            daysText() {
                if (this.day.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.day.specificSpecific
                }
            },
            weeksText() {
                if (this.week.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.week.specificSpecific
                }
            },
            monthsText() {
                if (this.month.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.month.specificSpecific
                }
            },
            yearsText() {
                if (this.year.specificSpecific === undefined) {
                    return '*'
                } else {
                    return this.year.specificSpecific
                }
            },
            cron() {
                return `${this.secondsText} ${this.minutesText} ${this.hoursText} ${this.daysText} ${this.monthsText} ${this.yearsText}`
            },
        },
        methods: {
            getValue() {
                return this.cron;
            },
            change() {
                this.$emit('change', this.cron);
            },
            display(){
                this.$emit('change', this.cron);
            },
            close() {
                this.$emit('close')
            },
            $init() {

                if (this.warning !== undefined && this.warning !== '') {
                    let warning = this.warning.split(" ")
                    this.$set(this.second, 'specificSpecific', warning[0])
                    this.$set(this.minute, 'specificSpecific', warning[1])
                    this.$set(this.hour, 'specificSpecific', warning[2])
                    this.$set(this.day, 'specificSpecific', warning[3])
                    this.$set(this.month, 'specificSpecific', warning[4])
                    this.$set(this.year, 'specificSpecific', warning[5])
                }

                if (this.handover !== undefined && this.handover !== '') {
                    let handover = this.handover.split(" ")
                    this.$set(this.second, 'specificSpecific', handover[0])
                    this.$set(this.minute, 'specificSpecific', handover[1])
                    this.$set(this.hour, 'specificSpecific', handover[2])
                    this.$set(this.day, 'specificSpecific', handover[3])
                    this.$set(this.month, 'specificSpecific', handover[4])
                    this.$set(this.year, 'specificSpecific', handover[5])
                }
            },
            rest(data) {
                for (let i in data) {
                    if (data[i] instanceof Object) {
                        this.rest(data[i])
                    } else {
                        switch (typeof data[i]) {
                            case 'object':
                                data[i] = [];
                                break;
                            case 'string':
                                data[i] = '';
                                break;
                        }
                    }
                }
            }
        },
        mounted() {
        }
    }</script>
