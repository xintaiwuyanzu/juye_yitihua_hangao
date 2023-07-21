<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="名称" prop="title">
        <el-input v-model="searchForm.title" style="width: 220px" placeholder="请输入名称" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search" size="mini">搜 索</el-button>
        <el-button @click="$refs.searchForm.resetFields()" size="mini" type="info">重 置</el-button>
        <el-button type="primary" @click="editForm()" size="mini">添加</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑水印':'添加水印')" width="550px" :close-on-click-modal=true  :modal-append-to-body=false
               :destroy-on-close=true>
      <el-form :model="form" :rules="rules" ref="form" label-width="130px">
        <el-form-item label="名称" prop="title" required>
          <el-input v-model="form.title" placeholder="请输入名称" clearable/>
        </el-form-item>
        <!--        <el-form-item label="位置" prop="positions" required>-->
        <!--          <el-input v-model="form.positions" placeholder="请输入位置" clearable/>-->
        <!--        </el-form-item>-->
        <!--        <el-form-item label="水平距离" prop="widthX" required>
                  <el-input v-model="form.widthX" placeholder="请输入水平距离" clearable/>
                </el-form-item>
                <el-form-item label="垂直距离" prop="heightY" required>
                  <el-input v-model="form.heightY" placeholder="请输入垂直距离" clearable/>
                </el-form-item>-->
        <el-form-item label="水平位置初始值" prop="width0" required>
          <el-input v-model="form.width0" placeholder="请输入水平位置初始值" clearable/>
        </el-form-item>
        <el-form-item label="垂直位置初始值" prop="height0" required>
          <el-input v-model="form.height0" placeholder="请输入垂直位置初始值" clearable/>
        </el-form-item>
        <!--        <el-form-item label="水印垂直值" prop="waterMarkVertical" style="margin-top: 7px">-->
        <!--          <el-input v-model="form.waterMarkVertical" placeholder="请输入水印垂直值" clearable/>-->
        <!--        </el-form-item>-->
        <el-form-item label="倾斜角度" prop="tiltAngle" style="margin-top: 7px">
          <el-input v-model="form.tiltAngle" placeholder="请输入倾斜角度" clearable/>
        </el-form-item>
        <el-form-item label="缩放比例" prop="watermarkScale" style="margin-top: 7px">
          <el-input v-model="form.watermarkScale" placeholder="请输入缩放比例" clearable>
            <template slot="append">取值范围:0~5间的浮点型值</template>
          </el-input>
        </el-form-item>
        <el-form-item label="不透明度" prop="colorGray" style="margin-top: 7px">
          <el-input v-model="form.colorGray" placeholder="请输入不透明度" clearable>
            <template slot="append">取值范围:0~1</template>
          </el-input>
        </el-form-item>
        <!--        <el-form-item label="颜色" prop="color" style="margin-top: 7px">
                  &lt;!&ndash;          <el-input v-model="form.color" placeholder="请输入颜色" clearable/>&ndash;&gt;
                  <select-dict type="color.number" v-model="form.color"/>
                </el-form-item>-->
        <el-form-item label="颜色" prop="color" style="margin-top: 7px">
          <!--          <el-input v-model="form.color" placeholder="请输入颜色" clearable/>-->
          <div class="block">
            <el-color-picker v-model="form.color"></el-color-picker>
          </div>
        </el-form-item>
        <el-form-item label="字体大小" prop="fontSize" style="margin-top: 7px">
          <el-select v-model="form.fontSize" style="width: 80%" placeholder="请选择字体大小">
            <el-option
                v-for="item in fontData"
                :key="item.id"
                :label="item.label"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="图片地址" prop="photoUrl" required >
           <el-image
                   style="width: 100px; height: 100px"
                   :src="imgSrc"></el-image>
         </el-form-item>-->
<!--        <el-form-item label="图片地址" prop="photoUrl" required>
          &lt;!&ndash;          <el-input v-model="form.photoUrl" placeholder="请输入图片地址" clearable/>&ndash;&gt;

          <div>
            <el-upload
                class="upload-demo"
                action="api/files/upload"
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :on-exceed="masterFileMax"
                accept=".png,.jpg"
                :limit="1"
                :on-success="Push"
                :data="{
                          refId:this.uuid,

                       }"
                :file-list="fileList"
                list-type="picture">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，最多一个文件且不超过500kb</div>
            </el-upload>
          </div>
          &lt;!&ndash; <div v-else>
             <el-input v-model="form.photoUrl" placeholder="请输入图片地址" :input="searchData(form.photoUrl)" clearable/>
           </div>&ndash;&gt;

        </el-form-item>-->
        <!--<el-form-item label="图片宽" prop="photoWidth" required>
          <el-input v-model="form.photoWidth" placeholder="请输入图片宽" clearable/>
        </el-form-item>
        <el-form-item label="图片高" prop="photoHeight" required>
          <el-input v-model="form.photoHeight" placeholder="请输入图片高" clearable/>
        </el-form-item>-->
        <!--        <el-form-item label="图片水平位置初始值" prop="width1" required>
                  <el-input v-model="form.width1" placeholder="请输入图片水平位置初始值" clearable/>
                </el-form-item>
                <el-form-item label="图片垂直位置初始值" prop="height1" required>
                  <el-input v-model="form.height1" placeholder="请输入图片垂直位置初始值" clearable/>
                </el-form-item>-->
        <el-form-item label="备注" prop="remark" style="margin-top: 7px">
          <el-input v-model="form.remark" placeholder="请输入备注" clearable/>
        </el-form-item>
        <!-- <el-form-item label="备注" prop="remark" style="margin-top: 7px">
         <img :src="imgUrl.data" frameborder="0"
                 style="width: 100%; height:88vh"/></el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="cancel" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="save" v-loading="loading" class="btn-submit">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@archive/core/src/util/formMixin'
import {v4} from 'uuid'

export default {
  props: {
    fontData: Array, //这样可以指定传入的类型，如果类型不对，会警告
    organiseId: String,
    imgSrc: String
  },
  data() {
    return {
      form: {},
      fileList: [
        {url: ''}
      ],
      searchForm: {title: '', organiseCode: ''},
      rules: {
        title: [
          {required: true, message: '请输入名称', trigger: 'blur'}
        ]
      },
      autoClose: true,
      defaultForm: {
        key: '',
        value: '',
        order: '',
        status: 1,
        description: ''
      },
      uuid: ''
    }
  },
  methods: {
    $init(){
      this.edit=false
    },
    imgUrl(value) {
      //console.log(value)
      //this.imgUrl = this.$http.post(`/files/downLoad/${value}?download=false`);
      this.fileList[0].url = `/api/files/downLoad/${value}?download=false`
      //this.$set(this.fileList[0],'url',`/files/downLoad/${value}?download=false`)
      //this.fileList.push()
      //console.log(this.fileList)
    },
    save() {
      // this.form.photoUrl = this.uuid TODO 有问题
      if (!this.form.id) {
        this.form.organiseCode = this.organiseId
      }
      this.saveForm()
    },
    search() {
      this.searchForm.organiseCode = this.organiseId
      this.$emit('func', this.searchForm)
    },
    handleRemove(file, fileList) {

    },
    handlePreview(file) {

      this.form.watermarkUrl = file.url;
    },
    masterFileMax(files, fileList) {

      this.$message.warning(`请最多上传1个文件。`);
    },
    Push(response, file, fileList) {

    },
    /*    searchData(photoUrl){

          console.log('http://localhost:8080/api/files/downLoad/'+photoUrl+'?download=false')
          this.fileList[0].url='http://localhost:8080/api/files/downLoad/'+photoUrl+'?download=false';
          console.log(photoUrl)
        },*/
  },
  mounted() {
    this.form.id ? (this.uuid = this.form.id) : (this.uuid = v4())
  },
  mixins: [fromMixin],
  watch: {
    'form.id': (oldVal) => {
      /* this.imgUrl =this.$http.post(`files/downLoad/${this.imgSrc}?download=false`);
      console.log('执行')
      console.log(this.imgUrl)*/
      //console.log(this.fileList)
      //this.$set(this.fileList[0],'url',this.imgSrc)
      //this.fileList.push({url:this.imgSrc})
    }
  },
  beforeRouteLeave (to, from, next) {
    this.edit = false
    next()
  }
}
</script>
