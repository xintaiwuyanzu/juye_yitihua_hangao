<template>
  <section>
    <nac-info back title="国家标准标签">
      <el-button @click="$router.push('/utilization/stdTag')" type="primary">标签管理</el-button>
      <el-button @click="visible=true" type="primary">导入标签</el-button>
    </nac-info>
    <div class="index_main">
      <el-table
          :data="tableData"
          :span-method="objectSpanMethod"
          border
          height="800px"
          style="width: 100%">
        <el-table-column
            align="center"
            prop="labelName1st"
            label="一级标签内容">
        </el-table-column>
        <el-table-column
            prop="labelName2nd"
            label="二级标签内容">
        </el-table-column>
        <el-table-column label="词库管理" align="center">
          <template slot-scope="scope">
            <el-button @click="edit(scope.row)" size="mini" type="primary">
              管理
            </el-button>
            <el-button @click="visible2=true,tag=scope.row" size="mini">
              导入词库
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-dialog
          :visible.sync="visible"
          title="导入标签及词库"
          width="50%">
        <tag-zip-template :type="1"></tag-zip-template>
        <el-upload
            :auto-upload="false"
            :before-upload="beforeUpload"
            :file-list="fileList"
            :on-success="onSuccess"
            action="/api/tagUpload/upload?tagType=std"
            class="upload-demo"
            drag
            ref="upload"
            style="text-align: center">
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传 ZIP 文件，且不超过 100MB</div>
        </el-upload>
        <span class="dialog-footer" slot="footer">
          <el-button @click="visible = false">取 消</el-button>
          <el-button @click="save" type="primary" v-loading="loading">确 定</el-button>
        </span>
      </el-dialog>

      <tag-word-import
          :import-dialog="visible2"
          :path="'/api/tagUpload/importWordTxt?id='+tag.id+'&type=std'"
          @cancel="cancel"
          @onSuccess="onSuccess2"
          @saveWord="saveWord"
          fileType="text/plain">
      </tag-word-import>
    </div>
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
        tableData: [],
        spanMap: new Map(),
        visible: false,
        fileList: [],
        visible2: false,
        tag: {},
        content: '',
        loading: false
      }
    },
    methods: {
      $init() {
        this.loadData()
      },
      //合并单元格方法
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        const key = row.labelName1st
        if (columnIndex === 0) {
          const index = this.spanMap.get(key)[0]
          const length = this.spanMap.get(key)[1]
          if (rowIndex === index) {
            return [length, 1]
          } else {
            return [0, 0]
          }
        }
      },
      async loadData() {
        const {data} = await this.$post('/stdTag/getAllStdTag')
        if (data && data.success) {
          this.tableData = data.data
          this.spanData()
        }
      },
      //获取单元格合并的数组
      spanData() {
        let map = new Map()
        //把一级标签去掉
        this.tableData = this.tableData.filter(i => {
          return i.labelName2nd !== ''
        })
        //获取每个一级标签的二级标签数量
        this.tableData.forEach((i, index) => {
          const key = i.labelName1st
          if (map.has(key)) {
            let value = map.get(key)
            value.push(i)
          } else {
            map.set(key, [i])
          }
        })
        //合并的数据  ['一级':[二级开始的index,二级数量]]
        let index = 0
        for (let key of map.keys()) {
          this.spanMap.set(key, [index, map.get(key).length])
          index = index + map.get(key).length
        }
      },
      edit(row) {
        this.$router.push({
          path: '/utilization/countryTag/editTagInfo',
          query: {
            id: row.id
          }
        })
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
      onSuccess(res, file) {
        this.$message.success('上传成功')
        this.visible = false
        this.loadData()
      },
      async save() {
        this.loading = true
        await this.$refs.upload.submit()
        this.loading = false
      },
      async saveWord(content) {
        this.tag.content = this.tag.content + '\n' + content
        const {data} = await this.$post('/stdTag/update', this.tag)
        if (data && data.success) {
          this.$message.success('导入成功！')
          this.loading = false
          this.visible2 = false
        }
      },
      cancel() {
        this.visible2 = false
      },
      onSuccess2() {
        this.$message.success('导入成功！')
        this.cancel()
      }
    },
    watch: {
      visible(b) {
        if (!b)
          this.fileList = []
      },
      visible2(b) {
        if (!b)
          this.fileList = []
      }
    }
  }
</script>

<style scoped>

</style>