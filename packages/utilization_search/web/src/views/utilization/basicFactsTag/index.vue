<template>
  <section>
    <nac-info back title="基础事实标签">
      <el-button @click="tagDialog = true">导入标签</el-button>
      <el-button @click="addFirst" type="primary">添加一级标签</el-button>
      <el-button @click="addSecond" type="primary">添加二级标签</el-button>
      <el-button @click="addThree" type="primary">添加三级标签</el-button>
    </nac-info>
    <div class="index_main">
      <el-table
          :data="tags"
          :span-method="objectSpanMethod"
          border
          v-loading="loading"
          height="800px"
          style="width: 100%">
        <el-table-column
            label="一级标签"
            prop="labelName1st">
        </el-table-column>
        <el-table-column
            prop="labelName2nd"
            label="二级标签">
        </el-table-column>
        <el-table-column
            prop="labelName3rd"
            label="三级标签">
        </el-table-column>
        <el-table-column label="词库管理" align="center">
          <template slot-scope="scope">
            <el-button @click="edit(scope.row,3)" size="mini" type="primary">
              管理
            </el-button>
            <el-button @click="importWordPre(scope.row,3)" size="mini">
              导入词库
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        :title="'添加'+(type===1?'一级标签':(type===2?'二级标签':'三级标签'))"
        :visible.sync="visible"
        width="30%">
      <el-form :model="tag" label-width="110px">
        <el-form-item label="一级标签名称" prop="labelName1st" v-show="type===1">
          <el-input v-model="tag.labelName1st"></el-input>
        </el-form-item>
        <div v-show="type===2">
          <el-form-item label="一级标签名称" prop="labelName1st">
            <el-select v-model="tag.labelName1st">
              <el-option :key="key" :label="label1.get(key)" :value="key"
                         v-for="key of label1.keys()"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="二级标签名称" prop="labelName2nd">
            <el-input v-model="tag.labelName2nd"></el-input>
          </el-form-item>
        </div>
        <div v-show="type===3">
          <el-form-item label="二级标签名称" prop="labelName2nd">
            <el-select v-model="tag.labelName2nd">
              <el-option :key="key" :label="label2.get(key)" :value="key"
                         v-for="key of label2.keys()"
                         v-if="key!==null&&key!==''"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="三级标签名称" prop="labelName3rd">
            <el-input v-model="tag.labelName3rd"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <span class="dialog-footer" slot="footer">
        <el-button @click="visible = false">取 消</el-button>
        <el-button @click="save" type="primary">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
        :visible.sync="tagDialog"
        title="导入标签及词库"
        width="50%">
      <tag-zip-template :type="2"/>
      <el-upload
          :auto-upload="false"
          :before-upload="beforeUpload"
          :file-list="fileList"
          :on-success="onTagSuccess"
          action="/api/tagUpload/upload?tagType=fact"
          class="upload-demo"
          drag
          ref="upload"
          style="text-align: center">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传 ZIP 文件，且不超过 100MB</div>
      </el-upload>
      <span class="dialog-footer" slot="footer">
          <el-button @click="tagDialog = false">取 消</el-button>
          <el-button @click="submit" type="primary" v-loading="loading">确 定</el-button>
        </span>
    </el-dialog>
    <tag-word-import
        :import-dialog="importDialog"
        :path="'/api/tagUpload/importWordTxt?id='+importTag.id+'&type=fact'"
        @cancel="cancel"
        @onSuccess="onSuccess"
        @saveWord="saveWord"
        fileType="text/plain">
    </tag-word-import>
  </section>
</template>

<script>
import TagWordImport from "../../../components/tag/tagWordImport";
import TagZipTemplate from "../../../components/tag/tagZipTemplate";

export default {
  name: "index",
  components: {TagZipTemplate, TagWordImport},
  data() {
    return {
      loading: false,
      tableData: [],
      visible: false,
      type: 1,
      tags: [],
      tag: {},
      label1: new Map(),
      label2: new Map(),
      spanMap1: new Map(),
      spanMap2: new Map(),
      //导入词库
      importDialog: false,
      importTag: {},
      //导入标签
      tagDialog: false,
      fileList: [],
      loading: false
    };
  },
  methods: {
    $init() {
      this.loadData()
    },
    async loadData() {
      this.loading = true
      const {data} = await this.$post('/factTag/getAllFactTag')
      if (data && data.success) {
        this.tags = data.data
        this.tags.forEach(i => {
          this.label1.set(i.labelId1st, i.labelName1st)
          this.label2.set(i.labelId2nd, i.labelName2nd)
        })
        this.spanData()
      }
      this.loading = false
    },
    objectSpanMethod({row, column, rowIndex, columnIndex}) {
      if (columnIndex === 0) {
        const key = row.labelName1st
        const index = this.spanMap1.get(key)[0]
        const length = this.spanMap1.get(key)[1]
        if (rowIndex === index) {
          return [length, 1]
        } else {
          return [0, 0]
        }
      }
      if (columnIndex === 1) {
        const key = row.labelName2nd
        const index = this.spanMap2.get(key)[0]
        const length = this.spanMap2.get(key)[1]
        if (rowIndex === index) {
          return [length, 1]
        } else {
          return [0, 0]
        }
      }
    },
    addFirst() {
      this.visible = true
      this.type = 1
      this.tag = {}
    },
    addSecond() {
      this.visible = true
      this.type = 2
      this.tag = {}
    },
    addThree() {
      this.visible = true
      this.type = 3
      this.tag = {}
    },
    async save() {
      const {data} = await this.$post('/factTag/addFactTag', Object.assign(this.tag, {type: this.type}))
      if (data && data.success) {
        this.$message.success('添加成功')
        this.visible = false
        this.loadData()
      }
    },
    spanData() {
      let map1 = new Map()
      let map2 = new Map()
      this.tags.forEach(i => {
        const key = i.labelName1st
        if (map1.has(key)) {
          let value = map1.get(key)
          value.push(i)
        } else {
          map1.set(key, [i])
        }
      })
      this.tags.forEach(i => {
        const key2 = i.labelName2nd
        if (map2.has(key2)) {
          let value = map2.get(key2)
          value.push(i)
        } else {
          map2.set(key2, [i])
        }
      })
      //合并的数据  ['一级':[二级开始的index,二级数量]]
      let index = 0
      let index2 = 0
      for (let key of map1.keys()) {
        this.spanMap1.set(key, [index, map1.get(key).length])
        index = index + map1.get(key).length
      }
      for (let key of map2.keys()) {
        this.spanMap2.set(key, [index2, map2.get(key).length])
        index2 = index2 + map2.get(key).length
      }
    },
    edit(row, type) {
      this.$router.push({
        path: '/utilization/basicFactsTag/editTagInfo',
        query: {
          id: row.id,
          type
        }
      })
    },
    importWordPre(row, type) {
      this.importDialog = true
      this.importTag = row
      this.type = type
    },
    async saveWord(content) {
      this.importTag.content = this.importTag.content + '\n' + content
      const {data} = await this.$post('/factTag/update', this.importTag)
      if (data && data.success) {
        this.$message.success('导入成功！')
        this.cancel()
      }
    },
    cancel() {
      this.importDialog = false
    },
    onSuccess() {
      this.$message.success('导入成功！')
      this.cancel()
    },
    onTagSuccess() {
      this.$message.success('导入成功')
      this.tagDialog = false
      this.loadData()
    },
    //文件上传
    beforeUpload(file) {
      const isZIP = file.type === 'application/x-zip-compressed';
      const isLt100M = file.size / 1024 / 1024 < 100;
      if (!isZIP) {
        this.$message.error('上传文件只能是 ZIP 格式!');
      }
      if (!isLt100M) {
        this.$message.error('上传文件大小不能超过 100MB!');
      }
      return isZIP && isLt100M;
    },
    async submit() {
      this.loading = true
      await this.$refs.upload.submit()
      this.loading = false
    }
  }
}
</script>