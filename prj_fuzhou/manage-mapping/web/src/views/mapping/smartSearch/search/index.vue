<template>
  <el-col :offset="3" :span="18" class="index_main smart-search-main">
    <div class="search">
      <div style="display: flex">
        <el-input style="min-width: 380px" placeholder="请输入要检索的内容" v-model="content"/>
        <el-button type="primary" @click="search" style="margin-left: 20px" :loading="loading">搜索</el-button>
        <el-button @click="content = ''" type="info">重置</el-button>
        <el-button @click="$router.back()" type="primary">返回</el-button>
      </div>
      <div style="position: relative">
        <el-tabs style="font-size: 11px;margin: 5px 0" type="border-card" v-model="classType">
          <el-tab-pane label="人" name="person">
            <div v-show="isShow">
              <div class="item">
                <div class="item_title">区域：</div>
                <el-checkbox-group @change="handleCheckedCitiesChange" v-model="personCheckedArea">
                  <el-checkbox :key="i" :label="i" v-for="i in areas" type="text">{{ i }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">性别：</div>
                <el-checkbox-group v-model="checkedSex">
                  <el-checkbox label="男">男</el-checkbox>
                  <el-checkbox label="女">女</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">民族：</div>
                <el-checkbox-group size="mini" v-model="checkedNations">
                  <el-checkbox :key="i" :label="i" v-for="i in nations">{{ i }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">婚姻状况：</div>
                <el-checkbox-group v-model="checkedMarry">
                  <el-checkbox label="已婚">已婚</el-checkbox>
                  <el-checkbox label="未婚">未婚</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">单位类型：</div>
                <el-checkbox-group size="mini" v-model="checkedOrganizeTypes">
                  <el-checkbox :key="i" :label="i" v-for="i in organizeTypes">{{ i }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <!--                <div style="text-align: right">
                                <el-button @click="setNull" icon="el-icon-delete" size="mini">清空条件</el-button>
                              </div>-->
            </div>
          </el-tab-pane>
          <el-tab-pane label="事" name="event">
            <div v-show="isShow">
              <div class="item">
                <div class="item_title">年代：</div>
                <el-checkbox-group v-model="eventCheckedTime">
                  <el-checkbox :key="i" :label="i" v-for="i in eventTimes">{{ i }}年代</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">分类：</div>
                <el-checkbox-group v-model="eventCheckedType">
                  <el-checkbox :key="i" :label="i" v-for="i in eventTypes">{{ i }}</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">标签：</div>
                <el-checkbox-group v-model="eventCheckedTag">
                  <el-checkbox :key="i" :label="i" v-for="i in eventTags">{{ i }}</el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="物" name="thing">
            <div v-show="isShow">
              <div class="item">
                <div class="item_title">年代：</div>
                <el-checkbox-group v-model="eventCheckedTime">
                  <el-checkbox :key="i" :label="i" v-for="i in eventTimes">{{ i }}年代</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">类型：</div>
                <el-input style="width: 80%" v-model="thingType">
                </el-input>
              </div>
              <div class="item">
                <div class="item_title">主要原料：</div>
                <el-input style="width: 80%" v-model="thingType">
                </el-input>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="地" name="land">
            <div v-show="isShow">
              <div class="item">
                <div class="item_title">行政区类别：</div>
                <el-checkbox-group v-model="xzqType">
                  <el-checkbox :key="i" :label="i" v-for="i in xzqTypes">{{ i }}年代</el-checkbox>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">地理位置:</div>
                <el-checkbox-group v-model="locationType">
                  <el-checkbox :key="i" :label="i" v-for="i in xzqTypes">{{ i }}年代</el-checkbox>
                </el-checkbox-group>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="组织" name="organize">
            <div v-show="isShow">
              <div class="item">
                <div class="item_title">成立年限：</div>
                <el-select clearable multiple style="width: 80%" v-model="organizeCheckedYears">
                  <el-option :key="index" :label="i+1980" :value="i+1980"
                             v-for="(i,index) in new Date().getFullYear()-1980"></el-option>
                </el-select>
              </div>
              <div class="item">
                <div class="item_title">法人类型：</div>
                <el-checkbox-group size="mini" v-model="checkedLegalPersons">
                  <el-checkbox-button :key="i" :label="i" v-for="i in legalPersons">{{ i }}</el-checkbox-button>
                </el-checkbox-group>
              </div>
              <div class="item">
                <div class="item_title">关系：</div>
                <el-checkbox-group size="mini" v-model="checkedRelations">
                  <el-checkbox-button :key="i" :label="i" v-for="i in relations">{{ i }}</el-checkbox-button>
                </el-checkbox-group>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
        <div style="position: absolute; right: 50px;top: 10px;">
          <i class="el-icon-caret-bottom" v-show="!isShow" @click="isShow=true"></i>
          <i class="el-icon-caret-top" v-show="isShow" @click="isShow=false"></i>
        </div>
      </div>
    </div>
    <div class="box" v-loading="loading">
      <el-empty v-if="result.length===0" class="c"/>
      <div class="c" v-else>
        <el-card :key="index" class="result_item" v-for="(i,index) in result" shadow="hover">
          <div style="display: flex;">
            <div class="itemName">
              <!--<h4 @click="toDetail(i)">{{ i.propertie.name }}</h4>
              <div class="msg">
                <span style="-webkit-text-fill-color: #3a87ad">ID: {{ i.id }}</span>
              </div>-->
              <div class="msg">
                <h4 @click="toDetail(i)">{{ i.NAME }}</h4>
                <div style="display: flex;justify-content: space-between;margin-top: 10px">
                  <div>
                          <span :key="j.prop" style="-webkit-text-fill-color: #3a87ad;margin-right: 20px"
                                v-for="j in fields"
                                v-if="j.prop != 'NAME'">
                            <span style="font-weight: bold">{{ j.label }}</span>:
                            <span style="font-size: 14px">{{ getValue(i, j) === '' ? '-' : getValue(i, j) }}</span>
                          </span>
                  </div>
                  <el-button @click="toDetail(i)" size="mini" style="float: right" type="primary">查看</el-button>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </div>
      <el-pagination layout="total,prev, pager, next"
                     :total="page.total"
                     style="text-align: right;"
                     :page-size="page.size"
                     @current-change="handleCurrentChange"/>
    </div>
  </el-col>
</template>

<script>
  import {useMenu} from "@dr/framework/src/hooks/useMenu";

  export default {
    name: "index",
    data() {
      return {
        loading: false,
        map: new Map(),
        result: [],
        fields: [],
        //跳转数据
        type: Number(this.$route.query.type),
        content: this.$route.query.content,
      classType: this.$route.query.classType,
      //面板数据
      areas: ['鼓楼区', '台江区', '仓山区', '晋安区', '马尾区', '长乐区', '闽侯县', '连江县', '罗源县', '闽清县', '永泰县', '平潭县'],//地区
      nations: ['汉族', '少数民族'],//民族
      organizeTypes: ['企业', '事业单位', '国家行政机关', '政府', '个人工商户', '其他'],//单位类型
      legalPersons: ['企业法人'],//法人类型
      relations: ['股东', '对外投资'],//关系
      eventTypes: ['历史文化', '风土人情', '民俗民风', '持续事件', '名人名胜', '当时教育', '名优特产', '其他'],//事件分类
      eventTimes: [50, 60, 70, 80, 90],//事件年代
      eventTags: ['知识青年', '上山下乡', '贫下中农再教育', '阶级斗争', '生产斗争', '革命运动', '公社插队'],//事件标签
      xzqTypes: ['省级行政区', '地级行政区', '县级行政区', '乡级行政区'], //行政区划
      locationTypes: ['沿海城市', '内陆城市', '边陲城市'], //按地理位置划分

      //检索参数
      personCheckedArea: [],
      checkedSex: [],
      checkedNations: [],
      checkedMarry: [],
      checkedOrganizeTypes: [],
      organizeCheckedArea: [],
      organizeCheckedYears: [],
      checkedLegalPersons: [],
      checkedRelations: [],
      eventCheckedType: [],
      eventCheckedTime: [],
      thingType: '',
      eventCheckedTag: [],
      xzqType: [],
      locationType: [],
      isShow: true,
      types: [{label: '人', value: 'person'},
        {label: '事', value: 'event'},
        {label: '物', value: 'thing'},
        {label: '地', value: 'land'},
        {label: '组织', value: 'organize'}],
      formDefinitionId: '',
      page: {
        index: 0,
        size: 15,
        total: 0
      }
    }
  },
  setup() {
    const {setName} = useMenu()
    setName('智能化检索', '/mapping/smartSearch/search')
  },
  methods: {
    async $init() {
      //重新初始化查询条件
      this.content = this.$route.query.content
      this.classType = this.$route.query.classType
      await this.getForms()
      await this.search()
    },
    async getForms() {
      const {data} = await this.$post('relation/getForms', {id: ''})
      if (data && data.success) {
        this.map = new Map()
        data.data.forEach(i => {
          this.map.set(i.formCode, i.id)
        })
      }
    },
    async loadListShowScheme() {
      if (this.formDefinitionId) {
        const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
          formDefinitionId: this.formDefinitionId,
          schemeType: 'list',
          formCode: 'list',
        })
        if (data.success) {
          this.fields = data.data.fields.map(item => {
            if (item.meta && item.meta.dict) {
              return Object.assign({}, {
                'prop': item.code,
                'label': item.name,
                'fieldType': 'select',
                'dictKey': item.meta.dict
              })
            } else {
              return Object.assign({}, {'prop': item.code, 'label': item.name})
            }
          })
        } else {
          this.$message.error(data.message)
        }
      }
    },
    getValue(var1, var2) {
      let arr = Object.keys(var1).map(item => ({key: item, value: var1[item]}))
      for (let i = 0; i < arr.length; i++) {
        if (arr[i].key === var2.prop) {
          return arr[i].value
        }
      }
      return ''
    },
    handleCurrentChange(v) {
      this.page.index = v - 1
      this.search()
    },
    async search() {
      let obj = {}
      if (this.classType === 'person') {
        if (this.personCheckedArea.length > 0) {
          obj['areas'] = this.personCheckedArea.toString()
        }
        if (this.checkedSex.length > 0) {
          obj['sex'] = this.checkedSex.toString()
        }
        if (this.checkedNations.length > 0) {
          obj['nations'] = this.checkedNations.toString()
        }
        if (this.checkedMarry.length > 0) {
          obj['marry'] = this.checkedMarry.toString()
        }
        if (this.checkedOrganizeTypes.length > 0) {
          obj['organizeTypes'] = this.checkedOrganizeTypes.toString()
        }
      }
      this.loading = true
      const {data} = await this.$post("search/smartSearch", {
        classType: this.classType,
        content: this.content,
        jsonStr: JSON.stringify(obj),
        ...this.page
      })
      if (data && data.success) {
        this.result = data.data.data
        this.page.index = data.data.start
        this.page.total = data.data.total
        this.page.size = data.data.size
        this.formDefinitionId = data.data.other
        await this.loadListShowScheme()
      }
      this.loading = false
    },
    handleCheckAllChange(val) {
      this.checkedCities = val ? areas : [];
      this.isIndeterminate = false;
    },
    handleCheckedCitiesChange(value) {
      let checkedCount = value.length;
      this.checkAll = checkedCount === this.areas.length;
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.areas.length;
    },
    setNull() {
      if (this.classType === 'person') {
        this.personCheckedArea = []
        this.checkedSex = []
        this.checkedNations = []
        this.checkedMarry = []
        this.checkedOrganizeTypes = []
      }
    },
    //跳到详情页面
    toDetail(row) {
      /*没找到表单表id*/
      this.$router.push({
        path: '/mapping/smartSearch/detail',
        query: {
          formDefinitionId: this.formDefinitionId,
          fromDataId: row.id,
          name: row.NAME
        }
      })
    }
  },
  watch: {
    classType() {
      this.page.total = 0
      this.page.index = 0
      this.search()
    }
  }
}
</script>

<style lang="scss">
.smart-search-main {
  height: 100%;

  .item {
    display: flex;
    line-height: 30px;
    padding: 3px 0;
  }

  .item_title {
    font-size: small;
    margin-right: 10px;
  }


  .result_item {
    margin-bottom: 5px;
  }

  .box {
    background-color: white;
    padding: 10px;
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: auto;

    .c {
      flex: 1;
      overflow: auto;
    }
  }

  .itemName {
    padding: 7px;
    width: 100%;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
  }

  .msg > span {
    color: $--color-primary;
    font-size: 15px;
  }
}
</style>