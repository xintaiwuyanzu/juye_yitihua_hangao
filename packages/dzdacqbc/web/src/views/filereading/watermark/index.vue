<template>
  <section>
    <nac-info>
      <config-form @func="loadData" ref="form" :font-data="todata" :organise-id="organiseId" :img-Src="imgSrc"/>
    </nac-info>
    <el-row>
      <el-col :span="5">
        <el-card shadow="hover">
          <div slot="header">
            <strong>部门单位</strong>
          </div>
          <div style="height:68.5vh;overflow:auto">
            <el-tree class="sysMenuTree"
                     :data="menuData"
                     default-expand-all
                     @node-click="click"
                     ref="menuTree"
                     height="68vh">
              <div slot-scope="{ node, data }" style="flex: 1;margin: 2px; ">
                <span v-if="organiseId==data.data.id" style=" color: red;font-family: 等线">{{ data.label }}</span>
                <span v-if="organiseId!=data.data.id" style=" color: #409EFF;font-family: 等线">{{ data.label }}</span>
              </div>
            </el-tree>
          </div>
        </el-card>
      </el-col>
      <el-col :span="19">
        <div class="index_main" v-loading="loading">
          <div class="table-container">
            <strong></strong>
            <el-table :data="data" border height="68vh">
              <el-table-column prop="order" label="排序" width="50" header-align="center" align="center">
                <template v-slot="scope">
                  {{ (page.index - 1) * page.size + scope.$index + 1 }}
                </template>
              </el-table-column>
              <el-table-column prop="title" label="水印名称" align="center" show-overflow-tooltip/>
              <!--          <el-table-column prop="positions" label="位置" header-align="center" show-overflow-tooltip/>-->
              <!--              <el-table-column prop="widthX" label="水平距离" header-align="center" show-overflow-tooltip/>
                            <el-table-column prop="heightY" label="垂直距离" header-align="center" show-overflow-tooltip/>-->
              <el-table-column prop="width0" label="水平初值" align="center" width="80"/>
              <el-table-column prop="height0" label="垂直初值" align="center" width="80"/>
              <!--          <el-table-column prop="waterMarkVertical" label="水印垂直值" header-align="center" show-overflow-tooltip/>-->
              <el-table-column prop="tiltAngle" label="倾斜角度" align="center" width="80"/>
              <el-table-column prop="colorGray" label="不透明度" align="center" width="80"/>
              <el-table-column prop="color" label="颜色" align="center" width="80">
                <template v-slot="scope">
                  {{ scope.row.color|dict('color.number') }}
                </template>
              </el-table-column>
              <el-table-column prop="fontSize" label="字体大小" align="center" width="80">
                <template v-slot="scope">
                  {{ scope.row.fontSize|fontSizeLabel }}
                </template>
              </el-table-column>
              <!-- <el-table-column prop="photoUrl" label="图片地址" header-align="center" show-overflow-tooltip/>-->
              <!-- <el-table-column prop="photoWidth" label="图片宽" header-align="center" show-overflow-tooltip/>
               <el-table-column prop="photoHeight" label="图片高" header-align="center" show-overflow-tooltip/>-->
              <!--              <el-table-column prop="width1" label="图片水平位置初始值" header-align="center" show-overflow-tooltip/>
                            <el-table-column prop="height1" label="图片垂直位置初始值" header-align="center" show-overflow-tooltip/>-->
              <el-table-column prop="status" label="状态" align="center" width="80">
                <template v-slot="scope">
                  <el-tag :type="scope.row.status==='1'?'success':'info'">
                    {{ scope.row.status|dict({'1': '默认', '0': '非默认'}) }}
                  </el-tag>
                </template>
              </el-table-column>
              <!--          <el-table-column prop="remark" label="备注" header-align="center" show-overflow-tooltip/>-->
              <el-table-column label="操作" width="200" header-align="center" align="center" fixed="right">
                <template v-slot="scope">
                  <el-link type="primary" size="small" @click="editForm(scope.row)">编 辑</el-link>
                  <el-divider direction="vertical"></el-divider>
                  <!--                  <el-link type="success" size="small" @click="show(scope.row)">预 览</el-link>
                                    <el-divider direction="vertical"></el-divider>
                                    <el-link type="success" size="small" @click="showFile(scope.row)">模板</el-link>
                                    <el-divider direction="vertical"></el-divider>-->
                  <el-link type="success" size="small" v-if="scope.row.status==='0'"
                           @click="moren(scope.row,'1')">设为默认
                  </el-link>
                  <el-link type="success" size="small" v-else @click="moren(scope.row,'0')">取消默认
                  </el-link>
                  <el-divider direction="vertical"></el-divider>
                  <el-link type="danger" size="small" @click="remove(scope.row.id)">删 除</el-link>
                </template>
              </el-table-column>
            </el-table>
          </div>
          <file-list refType="archive" :formDataId="formDataId" style="margin-top: 5px" width="50%"
                     v-if="fileListDialog"
                     :transform="false" :upload="true" :deleter="true"/>
          <el-pagination
              @current-change="index=>loadData({pageIndex:index-1})"
              :current-page.sync="page.index"
              :page-size="page.size"
              layout="total, prev, pager, next"
              :total="page.total">
          </el-pagination>
        </div>
      </el-col>
    </el-row>
    <el-dialog
        :close-on-click-modal=true  :modal-append-to-body=false
        :destroy-on-close=true
        :center="true"
        :fullscreen="true"
        :visible.sync="preview"
        customClass="customWidth">
      <div>
        <iframe :src="src" frameborder="0"
                style="width: 100%; height:88vh"></iframe>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import ConfigForm from './form'
import indexMixin from "@dr/auto/lib/util/indexMixin";
// import fondTree from "@/components/fondTree";

let fontSizes = []
export default {
  data() {
    return {
      organiseId: '',
      menuData: [],
      src: '',
      refId: '',
      fonds: [],
      compilationType: {},
      pdfData: [],
      watermark: {},
      todata: [],
      dict: ['color.number'],
      preview: false,
      selectFond: '',
      imgSrc: '',
      fileListDialog: false,
      formDataId: ''
    }
  },
  components: {ConfigForm},
  mixins: [indexMixin],
  filters: {
    fontSizeLabel(v) {
      const type = fontSizes.find(p => p.id === v)
      return type ? type.label : v
    },
  },
  methods: {
    async editForm(row) {
      if (this.$refs.form) {
        this.$refs.form.editForm(row)
        // this.getImg(row.photoUrl)
        const {data} = await this.$http.post('files/list', {
          refId: row.photoUrl,
        })

        //console.log(data.data[0].id)
        //this.imgSrc = await this.$http.post(`files/downLoad/${data.data[0].id}?download=false`);
        this.imgSrc = data.data[0].id
        // let url = {}
        //this.$set(url,'url',this.imgSrc)
        // this.$refs.form.fileList.push(Object.assign('',{url:this.imgSrc}))
        this.$refs.form.imgUrl(this.imgSrc)
        //this.$set(.form.fileList[0],'url',this.imgSrc)
      }
    },
    async getImg(id) {
      const {data} = await this.$http.post('files/list', {
        refId: id,
      })

      this.imgSrc = await this.$http.post(`files/downLoad/${data.data[0].id}?download=false`);
    },
    $init() {
      this.loadLibRoot();
      this.todata = fontSizes
    },
    remove(id) {
      this.$confirm('此操作将删除选中数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('watermark/delete', {id: id})
            .then(({data}) => {
              if (data.success) {
                this.$message.success('删除成功！')
                this.selectIds = []
                this.loadData({organiseCode: this.organiseId})
              } else {
                this.$message.error(data.message)
                this.loading = false
              }
            })
      })
    },
    click(data) {
      this.organiseId = data.data.id
      this.loadData({organiseCode: this.organiseId})
    },
    async show(row) {
      const {data} = await this.$http.post('files/list', {
        refId: row.id,
        refType: 'archive'
      })
      if (data && data.success && data.data.length > 0) {
        await this.openFile(data.data[0].id);
      }
    },
    async openFile(mobId) {
      const {data} = await this.$http.post('/fileView/getFile', {fileId: mobId, filePath: mobId, watermark: true})
      if (data.success) {
        window.open(data.data, '_blank')
      } else {
        this.$message.error(data.message)
      }
    },
    showFile(row) {
      this.fileListDialog = true
      this.formDataId = row.id
    },
    moren(row, v) {
      this.watermark = Object.assign({}, row)
      this.watermark.status = v
      let watermarkUrl;
      let showMessage;
      if (v === '0') {
        watermarkUrl = 'watermark/update'
        showMessage = '取消成功'
      } else {
        watermarkUrl = 'watermark/updateStatus'
        showMessage = '设置成功'
      }
      this.$http.post(watermarkUrl, this.watermark).then(({data}) => {
        if (data.data) {
          this.$message.success(showMessage)
          this.loadData({organiseCode: this.organiseId})
        } else {
          this.$message.error('已设置默认水印')
        }
        this.loading = false
      })
    },
    loadLibRoot() {
      this.loading = true
      this.$http.post('/genertion/organiseTree', {all: false})
          .then(({data}) => {
            if (data.success) {
              this.menuData = data.data ? data.data : []
            } else {
              this.$message.error(data.message)
            }
            this.loading = false
          })
    },
  },
  mounted() {

    fontSizes = [
      {id: '5', label: '5'},
      {id: '5.5', label: '5.5'},
      {id: '6.5', label: '6.5'},
      {id: '7.5', label: '7.5'},
      {id: '8', label: '8'},
      {id: '9', label: '9'},
      {id: '10', label: '10'},
      {id: '10.5', label: '10.5'},
      {id: '11', label: '11'},
      {id: '12', label: '12'},
      {id: '14', label: '14'},
      {id: '16', label: '16'},
      {id: '18', label: '18'},
      {id: '20', label: '20'},
      {id: '22', label: '22'},
      {id: '26', label: '26'},
      {id: '28', label: '28'},
      {id: '36', label: '36'},
      {id: '48', label: '48'},
      {id: '56', label: '56'},
      {id: '72', label: '72'}

    ]
  },
  beforeRouteLeave (to, from, next) {
    this.preview = false
    next()
  }
}
</script>
<style scoped>
.fondTree {
  height: 750px;
}
</style>
