<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="题名">
        <el-input style="width:150px" v-model="searchForm.title" clearable/>
      </el-form-item>
      <el-form-item label="档号">
        <el-input style="width:150px" v-model="searchForm.archiveCode" clearable/>
      </el-form-item>
      <!--        <el-form-item label="件盒">-->
      <!--            <el-select style="width:100px" v-model="searchForm.archiveBox" filterable clearable>-->
      <!--                <el-option value="1" label="件"/>-->
      <!--                <el-option value="2" label="盒"/>-->
      <!--            </el-select>-->
      <!--        </el-form-item>-->
      <el-form-item>
        <el-button type="primary" @click="searchF" size="mini">搜 索</el-button>
        <el-button type="primary" @click="add()" size="mini">添 加</el-button>
        <el-button @click="reset()" type="info">重 置</el-button>
        <!--                <el-button type="primary" v-if="this.multipleSelection.length>0" @click="boxedit" size="mini">入 盒-->
        <!--                </el-button>-->
        <!--        <el-button type="primary" size="mini" @click="maketype">类型设置</el-button>-->
        <el-button type="primary" size="mini" @click="upDa()">批量设置位置信息
        </el-button>
        <el-button type="primary" size="mini" @click="modelImp">模板下载</el-button>
        <el-button type="primary" size="mini" @click="imp">档案导入</el-button>
        <el-button type="primary" size="mini" @click="erweima">导出二维码</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'添加')+'实体档案'" width="60%">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px" :inline="false">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>档案信息</span>
          </div>
          <div v-show="isShowDA">
            <el-form-item label="题名" prop="title" @input.native="handleClick" required>
              <el-input v-model="form.title" clearable/>
            </el-form-item>
            <el-form-item label="档号" prop="archiveCode" required>
              <el-input v-model="form.archiveCode" placeholder="请输入档号" clearable/>
            </el-form-item>
            <el-form-item label="档案分类">
              <el-select
                  v-model="parentType" :disabled="true"
                  filterable
                  default-first-option
                  placeholder="请选择类型" @change="getPropertys">
                <el-option
                    v-for="item in archiveTypes"
                    :key="item.id"
                    :label="item.archiveTypeName"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="类型" prop="archiveType">
              <el-select
                  v-model="archiveType" :disabled="true"
                  filterable
                  default-first-option
                  placeholder="请选择类型" @change="getPropertys">
                <el-option
                    v-for="item in archiveTypes"
                    :key="item.id"
                    :label="item.archiveTypeName"
                    :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="件盒" prop="archiveBox" required>
              <el-select v-model="form.archiveBox" placeholder="请选择件盒">
                <el-option value="1" label="件"/>
<!--                <el-option value="2" label="盒"/>-->
              </el-select>
            </el-form-item>
            <div style="display: inline-block" v-for="(item,index) in Propertys"
                 v-show="!form.id&&openTypes"
                 :key="'label-'+index"
                 :label="item.propertyName"
                 :value="item.propertyName+index">
              <el-form-item :label=item.propertyName required>
                <el-input v-model="Propertys[index].status" :placeholder="'请输入'+item.propertyName"
                          clearable/>
              </el-form-item>
            </div>
            <div style="display: inline-block" v-for="(item,index) in Propertys" v-show="form.id&&openTypes"
                 :key="'name-'+index"
                 :label="item.propertyId"
                 :value="item.propertyId+index">
              <el-form-item :label=item.propertyId required>
                <el-input v-model="Propertys[index].pValue" :placeholder="'请输入'+item.propertyId"
                          clearable/>
              </el-form-item>
            </div>

          </div>
        </el-card>


        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>位置信息</span>
            <!--            <el-button style="float: right; padding: 3px 0" type="text" v-if="isShowWZ" @click="isShowWZ=!isShowWZ">隐 藏
                        </el-button>
                        <el-button style="float: right; padding: 3px 0" type="text" v-else @click="isShowWZ=!isShowWZ">显 示
                        </el-button>-->
          </div>
          <div v-show="isShowWZ">
            <el-form-item label="库房" prop="locId" required>
              <el-select v-model="form.locId" placeholder="请选择库房" clearable
                         @change="getArea(form)"
                         filterable>
                <el-option v-for="item in locIds" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="区号" v-if="form.locId" prop="areaId" required>
              <el-select v-model="form.areaId" placeholder="请选择区号" clearable
                         filterable @change="getBookCaseNoList(form)">
                <el-option v-for="item in areas" :key="item.id" :label="item.areaName"
                           :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="密集架" v-if="form.areaId && form.locId" prop="caseId" required>
              <el-select v-model="form.caseId" placeholder="请选择密集架" clearable
                         filterable @change="selectLayerLie(form)">
                <el-option v-for="item in bookCaseNos" :key="item.id" :label="item.caseName"
                           :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="AB面" prop="columnNum" v-if="form.caseId" required>
              <el-select v-if="abcNum==1" v-model="form.columnNum" placeholder="请选择AB面">
                <el-option value="1" label="A面"/>
              </el-select>
              <el-select v-else v-model="form.columnNum" placeholder="请选择AB面">
                <el-option value="1" label="A面"/>
                <el-option value="2" label="B面"/>
              </el-select>
            </el-form-item>
            <el-form-item label="列号" prop="columnNo" required>
              <el-select v-model="form.columnNo">
                <el-option v-for="item in lies" :key="item" :label="item" :value="item"/>
              </el-select>
            </el-form-item>
            <el-form-item label="层号" prop="layer" required>
              <el-select v-model="form.layer">
                <el-option v-for="item in layers" :key="item" :label="item" :value="item"/>
              </el-select>
            </el-form-item>
          </div>
        </el-card>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel" type="info">取 消</el-button>
        <el-button type="primary" @click="insertForm" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="editBox" title="档案入盒" width="40%">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px" :inline="false">
        <el-form-item label="盒" prop="boxName" required>
          <el-autocomplete
              class="inline-input"
              v-model="boxForm.boxName"
              value-key="title"
              :fetch-suggestions="querySearch"
              placeholder="请输入内容"
              @select="handleSelect"
          ></el-autocomplete>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelBox">取 消</el-button>
        <el-button type="primary" @click="addBox" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>

    <el-dialog :visible.sync="showCard">
      <el-form :model="form" :rules="rules" ref="form" label-width="90px" :inline="false">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>位置信息</span>
          </div>
          <div v-show="isShowWZ">

            <el-form-item label="库房" prop="locId" required>
              <el-select v-model="form.locId" placeholder="请选择库房" clearable
                         @change="getArea(form)"
                         filterable>
                <el-option v-for="item in locIds" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="区号" v-if="form.locId" prop="areaId" required>
              <el-select v-model="form.areaId" placeholder="请选择区号" clearable
                         filterable @change="getBookCaseNoList(form)">
                <el-option v-for="item in areas" :key="item.id" :label="item.areaName"
                           :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="密集架" v-if="form.areaId && form.locId" prop="caseId" required>
              <el-select v-model="form.caseId" placeholder="请选择密集架" clearable
                         filterable @change="selectLayerLie(form)">
                <el-option v-for="item in bookCaseNos" :key="item.id" :label="item.caseName"
                           :value="item.id"/>
              </el-select>
            </el-form-item>
            <el-form-item label="AB面" prop="columnNum" v-if="form.caseId" required>
              <el-select v-if="abcNum==1" v-model="form.columnNum" placeholder="请选择AB面">
                <el-option value="1" label="A面"/>
              </el-select>
              <el-select v-else v-model="form.columnNum" placeholder="请选择AB面">
                <el-option value="1" label="A面"/>
                <el-option value="2" label="B面"/>
              </el-select>
            </el-form-item>
            <el-form-item label="节号" prop="columnNo" required>
              <el-select v-model="form.columnNo">
                <el-option v-for="item in lies" :key="item" :label="item" :value="item"/>
              </el-select>
            </el-form-item>
            <el-form-item label="层号" prop="layer" required>
              <el-select v-model="form.layer">
                <el-option v-for="item in layers" :key="item" :label="item" :value="item"/>
              </el-select>
            </el-form-item>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancelba" type="info">取 消</el-button>
        <el-button type="primary" @click="changes" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>


    <el-dialog :visible.sync="showJN" :title="(form.id?'编辑':'添加')+'卷内文件档案'" width="60%">
      <el-form :model="jnForm" :rules="rules" ref="jnForm" label-width="90px" :inline="false">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>档案信息</span>
          </div>
          <div>
            <el-form-item label="题名" prop="title" @input.native="handleClick" required>
              <el-input v-model="jnForm.title" clearable/>
            </el-form-item>
            <el-form-item label="档号" prop="archiveCode" required>
              <el-input v-model="jnForm.archiveCode" placeholder="请输入档号" clearable/>
            </el-form-item>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="jncancel" type="info">取 消</el-button>
        <el-button type="primary" @click="jnInsert" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from "@dr/auto/lib/util/formMixin";

export default {
  name: 'typeForm',
  data() {
    return {
      dict: ['book.status', 'columnNum.status'],
      archiveTypes: [],
      Propertys: [],
      arr: [],
      showCard: false,
      searchForm: {
        title: '',
        archiveCode: '',
        archiveType: '',
        archiveBox: '',
        caseNo: '',
        fondId: this.fondIdData,
        classCode: this.classCode,
      },
      locIds: [],
      areas: [],
      bookCaseNos: [],
      form: {
        title: '',
        data: [],
        archiveBox:'1',
        fondId: this.fondIdData,
        archiveType: this.archiveType,
      },
      abcNum: '',
      EntityFilesData: [],
      autoClose: true,
      openAdd: false,
      editBox: false,
      openTypes: false,
      boxForm: {
        parentId: '',
      },
      input0: '',
      input1: '',
      layers: 0,
      lies: 0,
      dataSouceArchiveType: '',
      showAdd: false,
      archiveType: '',
      parentType: '',
      showJN: false,
      jnForm: {},
      multipleSelection: []

    }
  },
  props: {
    isShowDA: {default: true},
    isShowWZ: {default: true},
    total: Number,
    fondIdData: String,
    updatesBatch: Array,
  },
  mixins: [fromMixin],
  methods: {
    $init() {
      this.getLoc()
      this.selectArea()
      this.selectEntityFiles()
      this.getTypes()
    },
    reset() {
      this.searchForm.title = ''
      this.searchForm.archiveCode = ''
      this.searchForm.archiveType = ''
      // this.searchForm.archiveBox = ''
    },
    showBtn(row) {
      this.archiveType = row.id
      this.parentType = row.data.pid
      this.showAdd = true
    },

    maketype() {
      this.$router.push({path: this.$route.path + '/type'})
    },
    upDa() {
      this.$emit('getSelects')
    },
    realSelect(val) {
      this.multipleSelection = val
      this.openTypes = false
      this.showCard = true
    },
    changes() {
      this.$refs.form.validate(valid => {
        if (valid) {
          const ls = this.form
          this.$http.post("/entityFiles/batchChange", {
            ids: this.multipleSelection.join(','),
            locId: ls.locId,
            areaId: ls.areaId,
            caseId: ls.caseId,
            columnNo: ls.columnNo,
            columnNum: ls.columnNum,
            layer: ls.layer
          })
            .then(({data}) => {
              this.showCard = false
              this.form = {}
              if (data.success) {
                this.$message.success("位置信息设置成功")
                this.searchF()
              } else {
                this.$message.error(data.message)
              }
            })
        }
      })
    },
    add() {
      if (this.archiveType === '' || this.archiveType == null) {
        this.$message.warning('请先选择档案分类！')
      } else {
        this.editForm()
      }

    },
    handleClick() {
      this.$nextTick(() => {
        // this.form.bookCaseNo = this.form.bookCaseNo.replace(/[^\d]/g, '')
      })
    },
    resetFields() {
      this.searchForm = []
    },
    async getLoc() {
      const {data} = await this.$http.post('locationkufang/page', {page: false})
      this.searchForm.locId = ''
      if (data.success) {
        this.locIds = data.data
        /*if (this.locIds != null && this.locIds.length > 0) {
          this.getBookCaseNoList(this.locIds[0].id)
        }*/
      } else {
        this.locIds = []
      }
    },
    cancelBox() {
      this.editBox = false
      this.boxForm = {parentId: ''}
    },
    cancelba() {
      this.showCard = false
      this.form = {}
    },
    async getArea(form) {
      this.$set(form, 'caseId', '');
      this.$set(form, 'columnNo', '');
      this.$set(form, 'layer', '');
      const {data} = await this.$http.post('/kufangArea/page', {kufangId: form.locId, page: false})
      if (data.success) {
        this.areas = data.data
      } else {
        this.areas = []
      }
    },
    async getBookCaseNoList(form) {
      this.$set(form, 'caseId', '');
      this.$set(form, 'columnNo', '');
      this.$set(form, 'layer', '');
      const {data} = await this.$http.post('/position/page', {
        locId: form.locId,
        areaId: form.areaId,
        page: false
      })
      if (data.success) {
        this.bookCaseNos = data.data
      } else {
        this.bookCaseNos = []
      }
    },
    getTypes() {
      this.$http.post('/archiveType/page', {page: false})
          .then(({data}) => {
            if (data.success && data.data != null) {
              this.archiveTypes = data.data
              this.openTypes = true
            }
          })
    },
    async getPropertys(v) {
      this.openTypes = true
      const {data} = await this.$http.post('/property/page', {archiveTypeId: v, page: false})
      if (data.success && data.data != null) {
        this.Propertys = data.data
      }
      if (this.form.id && this.dataSouceArchiveType === v) {
        await this.getAttrValues(this.form.id)
      }
    },
    async getAttrValues(v) {
      const {data} = await this.$http.post('/propertyValue/page', {entityId: v, page: false})
      if (data.success && data.data != null) {
        this.Propertys = data.data
      }
    },
    boxedit() {
      this.editBox = true
      this.boxForm = {
        boxName: '',
        parentId: '',
      }
    },
    addBox() {
      let ids = ""
      if (this.multipleSelection.length > 0) {
        for (let i = 0; i < this.multipleSelection.length; i++) {
          if (this.multipleSelection[i].archiveBox == '2') {
            return this.$message.warning('请选择正确入盒的数据')
          }
          ids += this.multipleSelection[i].id + ","
        }
      } else {
        return this.$message.warning('请选择要入盒的数据')
      }
      this.$http.post('/entityFiles/addBox', {parentId: this.boxForm.parentId, ids}).then(({data}) => {
        if (data && data.success) {
          this.$message.success('保存成功！')
          this.searchF()
        } else {
          this.$message.error(data.message)
        }
        this.editBox = false
        this.loading = false
      })
    },
    insertForm() {
      let arr = this.Propertys
      this.arr = []

      if (this.form.id) {
        for (let i = 0; i < arr.length; i++) {
          let data = {name: arr[i].propertyId, value: arr[i].pValue, id: arr[i].id}
          this.arr.push(data)
        }
      } else {
        for (let i = 0; i < arr.length; i++) {
          let data = {name: arr[i].propertyName, value: arr[i].status}
          this.arr.push(data)
        }
      }
      this.form.propertyData = JSON.stringify(this.arr);
      this.form.archiveType = this.archiveType
      // this.archiveType = this.form.archiveType
      if (this.$refs.form) {
        this.loading = true
        this.$refs.form.validate(valid => {
          if (valid) {
            let path
            if (this.form.id) {
              path = 'entityFiles/change'
            } else {
              path = 'entityFiles/addBo'
            }
            this.form.fondId = this.fondIdData
            this.form.classCode = this.classCode

            this.$http.post(path, this.form).then(({data}) => {
              if (data && data.success) {
                this.$message.success('保存成功！')
                if (this.autoClose) {
                  this.cancel()
                }
                this.searchF(this.archiveType)
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
          } else {
            this.loading = false
          }
        })
      }
    },
    querySearch(queryString, cb) {
      var EntityFilesData = this.EntityFilesData;
      var results = queryString ? EntityFilesData.filter(this.createFilter(queryString)) : EntityFilesData;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (restaurant) => {
        return (restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
      };
    },
    handleSelect(item) {
      this.boxForm.parentId = item.id
    },
    selectArea() {
      this.$http.post('kufangArea/page', {page: false})
          .then(({data}) => {
            if (data.success) {
              this.areas = data.data
            } else {
              this.$message.error(data.message)
            }
          })
    },
    selectEntityFiles() {
      this.$http.post('entityFiles/page', {archiveBox: '2', page: false})
          .then(({data}) => {
            if (data.success) {
              this.EntityFilesData = data.data
            } else {
              this.$message.error(data.message)
            }
          })
    },
    erweima() {
      if (this.total > 0) {
        let url = "api/entityFiles/erweima?title=" +
            this.searchForm.title +
            "&archiveCode=" + this.searchForm.archiveCode
            + "&archiveType=" + this.archiveType
        window.open(url)
      } else {
        this.$message.warning('暂无数据！')
      }
    },
    searchF() {
      this.searchForm.archiveType = this.archiveType
      this.$emit('func', this.searchForm)
    },
    showEdit(v) {
      this.form = {...v}
      this.dataSouceArchiveType = this.form.archiveType
      this.getLoc()
      this.getTypes()
      this.form.classCode = this.classCode
      this.form.fondId = this.fondIdData
      this.selectLayerLie(this.form)
      // this.getPropertys(this.form.archiveType)
      this.getAttrValues(this.form.id)
      this.getBookCaseNoList(this.form)
      this.form = {...v}
      this.edit = true
    },
    addJNWJ(v) {
      this.showJN = true
      this.jnForm.ajdhId = v
    },
    editJN(v) {
      this.showJN = true
      this.jnForm = {...v}
    },
    selectLayerLie(form) {
      this.$set(form, 'columnNo', '');
      this.$set(form, 'layer', '');
      this.$set(form, 'columnNum', '');
      if (this.bookCaseNos != null && this.bookCaseNos.length > 0) {
        this.bookCaseNos.forEach(a => {
          if (a.id === form.caseId) {
            this.layers = parseInt(a.layer)
            this.abcNum = a.columnNum
            this.lies = parseInt(a.columnNo)
          }
        })
      }
    },
    imp() {
      let param = {archiveType: this.archiveType, parentType: this.parentType}
      this.$emit("imp", param)
    },
    modelImp() {
      this.$emit("modelImp")
    },
    jncancel() {
      this.jnForm.title = ''
      this.jnForm.archiveCode = ''
      this.showJN = false
    },
    jnInsert() {
      if (this.$refs.jnForm) {
        this.loading = true
        this.$refs.jnForm.validate(valid => {
          if (valid) {
            let path = ''
            if (this.jnForm.id) {
              path = 'entityFiles/editJNWJ'
            } else {
              path = 'entityFiles/addJNWJ'
            }

            this.$http.post(path, this.jnForm).then(({data}) => {
              if (data && data.success) {
                this.$message.success('保存成功！')
                this.jncancel()
                this.searchF(this.archiveType)
              } else {
                this.$message.error(data.message)
              }
              this.loading = false
            })
          } else {
            this.loading = false
          }
        })
      }
    },
  },
}
</script>

<style scoped lang="scss">
.box-card {
  width: 100%;
}

.el-input {
  width: 93% !important;
}

::v-deep .el-form-item__error {
  padding-left: 40px !important;
}
</style>
