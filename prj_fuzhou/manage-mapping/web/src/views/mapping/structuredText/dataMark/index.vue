<template>
  <section>
    <nac-info back title="元数据标注">
      <span v-show="structuredText.status==='0'">
        <el-button :disabled="currentObjId===''" @click="submitMarkResult" type="primary">保存标注结果</el-button>
        <el-button @click="finishMark" type="primary">标注完成</el-button>
      </span>
      <span v-show="structuredText.status==='1'">
        <el-button :disabled="c.indexOf('source')===-1||c.indexOf('target')===-1" @click="submitRelationResult"
                   type="primary">保存关系</el-button>
      </span>
    </nac-info>
    <el-card>
      <div class="left">
        <el-table :data="fields" border size="medium" stripe style="width: 43%;height: 750px;overflow-y: scroll">
          <el-table-column label="含义" prop="label">
            <template slot-scope="scope">
              <span>{{scope.row.label}}</span>
              <el-tag style="margin-left: 5px" v-if="markMap.has(scope.row.label)&&structuredText.status==='1'">
                {{'对象'+markMap.get(scope.row.label)}}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column
              align="center">
            <template slot="header" slot-scope="scope" v-if="structuredText.status==='0'">
              <span>当前操作对象：</span>
              <el-select v-model="currentObjId">
                <el-option :key="index" :label="i.formName" :value="i.id" v-for="(i,index) in classes"></el-option>
              </el-select>
            </template>
            <template slot-scope="scope">
              <el-select clearable v-if="structuredText.status==='0'" v-model="properties[scope.$index]">
                <el-option :key="index" :label="i.label" :value="i.fieldCode"
                           v-for="(i,index) in map.get(currentObjId)"
                           v-show="properties.indexOf(i.fieldCode)===-1"></el-option>
              </el-select>
              <el-select clearable v-if="structuredText.status==='1'" v-model="c[scope.$index]">
                <el-option label="源对象" v-show="c.indexOf('source')===-1" value="source"></el-option>
                <el-option label="目标对象" v-show="c.indexOf('target')===-1" value="target"></el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
        <div class="r">
          <el-card class="right_card">
            <div v-if="structuredText.status==='1'">
              <h4>
                <el-divider direction="vertical"/>
                选择梳理关系
              </h4>
              <div class="case">
                <el-select class="i" clearable placeholder="源对象" style="width: 200px" v-model="objIds.sourceId">
                  <el-option :key="index" :label="i.formName" :value="i.id" v-for="(i,index) in classes"></el-option>
                </el-select>
                <el-select class="i" clearable placeholder="关系" style="width: 150px" v-model="objRelation.relation">
                  <el-option :key="index" :label="i.relationName" :value="i.id"
                             v-for="(i,index) in relations"></el-option>
                </el-select>
                <el-select class="i" clearable placeholder="目标对象" style="width: 150px" v-model="objIds.targetId">
                  <el-option :key="index" :label="i.formName" :value="i.id" v-for="(i,index) in classes"></el-option>
                </el-select>
              </div>
            </div>
            <div>
              <h4>
                <el-divider direction="vertical"/>
                过滤条件
              </h4>
              <div :key="ii" class="case" v-for="(i,ii) in caseData">
                <el-select class="i" clearable filterable placeholder="源属性" style="width: 200px" v-model="i.name">
                  <el-option :key="index" :label="i.label" :value="i.label"
                             v-for="(i,index) in fields"></el-option>
                </el-select>
                <el-select class="i" placeholder="条件" style="width: 100px" v-model="i.when">
                  <el-option :key="index" :label="i" :value="i" v-for="(i,index) in conditions"></el-option>
                </el-select>
                <el-input placeholder="目标属性" style="width: 200px" v-model="i.value"></el-input>
                <div>
                  <i @click="addCase" class="el-icon-plus"
                     style="font-size: 20px;cursor: pointer;margin: 5px 0 10px 10px;"
                     v-if="ii===0"></i>
                  <i @click="deleteCase(ii)" class="el-icon-minus"
                     style="font-size: 20px;cursor: pointer;margin: 5px 0 10px 10px;"
                     v-else></i>
                </div>
              </div>
            </div>
            <el-divider/>
            <div v-if="structuredText.status==='1'">
              <h4 style="margin:20px 0 10px 0">
                <el-divider direction="vertical"/>
                已梳理关系
              </h4>
              <el-table :data="tripleData" border height="500" stripe>
                <el-table-column label="源对象" prop="sourceName">
                  <template slot-scope="scope">
                    <el-tag>对象{{markMap.get(scope.row.sourceField)}}</el-tag>
                    <span style="margin-left: 5px">{{scope.row.sourceName}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="目标对象" prop="targetName">
                  <template slot-scope="scope">
                    <el-tag>对象{{markMap.get(scope.row.targetField)}}</el-tag>
                    <span style="margin-left: 5px">{{scope.row.targetName}}</span>
                  </template>
                </el-table-column>
                <el-table-column label="关系" prop="relationName"></el-table-column>
              </el-table>
            </div>
          </el-card>
        </div>
      </div>
    </el-card>
  </section>
</template>

<script>

  export default {
    name: "dataMark",
    data() {
      return {
        id: this.$route.query.id,
        type: this.$route.query.markType,
        fields: [],
        structuredText: {},
        conditions: ['>', '<', '=', 'like'],
        classes: [],
        relations: [],
        map: new Map(),
        //当前标注的对象
        currentObjId: '',
        //筛选条件
        caseData: [
          {name: '', when: '', value: ''}
        ],
        //源对象和目标对象定位
        c: [],
        //属性映射定位
        properties: [],
        //源、目标、关系
        objRelation: {source: '', target: '', relation: ''},
        //关系梳理对象
        objIds: {baseId: this.$route.query.id, sourceId: '', targetId: ''},
        //已梳理关系
        tripleData: [],
        //对象索引标记
        markMap: new Map()
      }
    },
    methods: {
      $init() {
        this.getTitle()
        this.getClasses()
      },
      //数据来源信息（structuredText）
      async detail() {
        const {data} = await this.$post('structuredText/detail', {id: this.id})
        if (data && data.success) {
          this.structuredText = data.data
          const result = this.structuredText.markResult
          if (result !== '') {
            for (let key in JSON.parse(result)) {
              this.markMap.set(key, JSON.parse(result)[key])
            }
          }
        }
      },
      async getTitle() {
        const {data} = await this.$post('structuredText/excelTitle', {id: this.id, type: this.type})
        if (data && data.success) {
          data.data.forEach(i => {
            this.fields.push({fieldCode: "", label: i,});
          })
          this.detail()
        }
      },
      async getClasses() {
        const {data} = await this.$post('realm_class/getFormMsg', {id: ''})
        if (data && data.success) {
          this.classes = data.data
          this.map = new Map()
          data.data.forEach(i => {
            this.map.set(i.id, i.fields)
          })
        }
      },
      async getRelations() {
        this.relations = []
        const source = this.objIds.sourceId
        const target = this.objIds.targetId
        if (source && target) {
          const {data} = await this.$post('relation/getRelations', {source, target})
          if (data && data.success) {
            this.relations = data.data
          }
        }
      },
      addCase() {
        this.caseData.push({name: '', when: '', value: ''})
      },
      deleteCase(index) {
        this.caseData.splice(index, 1)
      },
      async submitMarkResult() {
        let properties = []
        this.properties.forEach((i, index) => {
          if (i !== '') {
            properties.push({from: this.fields[index].label, to: i})
          }
        })
        let result = {
          condition: this.caseData,
          properties,
          objIds: {baseId: this.id, sourceId: this.currentObjId, targetId: ''}
        }
        const {data} = await this.$post("structuredText/findExcelText", {
          result: JSON.stringify(result),
          id: this.id,
          type: this.type
        })
        if (data && data.success) {
          if (data.data > 0) {
            this.$message({type: 'success', message: '标注成功!'})
            this.currentObjId = ''
            this.properties = []
            this.caseData = [{name: '', when: '', value: ''}]
          } else {
            this.$message({type: 'error', message: '无数据可梳理!'})
          }
        }
      },
      //完成数据标注
      async finishMark() {
        this.$confirm('数据标注完成将进入关系梳理，不再重新标记数据?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          let obj = this.structuredText
          obj.status = '1'
          const {data} = await this.$post('structuredText/update', obj)
          if (data && data.success) {
            this.$message({
              type: 'success',
              message: '元数据标注完成!'
            });
            this.$router.back()
          }
        }).catch(() => {
          this.$message({
            type: 'warning',
            message: '可继续数据标注'
          });
        });
      },
      async submitRelationResult() {
        this.c.forEach((i, index) => {
          if (i !== '') {
            if (i === 'source') {
              this.objRelation.source = this.fields[index].label
            }
            if (i === 'target') {
              this.objRelation.target = this.fields[index].label
            }
          }
        })
        let result = {
          relation: this.objRelation,
          condition: this.caseData,
          objIds: this.objIds
        }
        const {data} = await this.$post('structuredText/saveRelationResult', {
          result: JSON.stringify(result),
          type: this.type
        })
        if (data && data.success) {
          if (data.data > 0) {
            this.$message({
              type: 'success',
              message: '关系数据梳理成功'
            })
            this.c = []
            this.objRelation = {source: '', target: '', relation: ''}
            this.objIds = {baseId: this.$route.query.formId, sourceId: '', targetId: ''}
            this.caseData = [{name: '', when: '', value: ''}]
            this.getTripleData()
          } else {
            this.$message({
              type: 'error',
              message: '关系梳理失败，找不到数据！'
            })
          }
        }
      },
      //获取已梳理关系
      async getTripleData() {
        const {data} = await this.$post('tripletData/getMarkedRelations', {baseFormId: this.$route.query.id})
        if (data && data.success) {
          this.tripleData = data.data
          this.detail()
        }
      }
    },
    watch: {
      'objIds.sourceId'() {
        this.getRelations()
      },
      'objIds.targetId'() {
        this.getRelations()
      },
      'structuredText.status'(val) {
        if (val === '1') {
          this.getTripleData()
        }
      }
    }
  }
</script>

<style scoped>
  .left {
    display: flex;
    justify-content: space-between;
  }

  .r {
    width: 55%;
    height: 780px
  }

  .case {
    display: flex;
    padding-left: 20px;
    margin: 10px 0 20px 0;
  }

  .i {
    margin-right: 10px;
  }

  .right_card {
    margin-bottom: 10px;
    padding: 10px;
    height: 100%
  }
</style>