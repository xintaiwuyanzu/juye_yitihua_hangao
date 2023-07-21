<template>
  <section>
    <nac-info back title="选择梳理格式"/>
    <el-alert
        title="请根据标题的排列方向选择梳理方式，否则会导致梳理的数据有问题！"
        type="warning">
    </el-alert>
    <div class="home" v-loading="loading">
      <div id="excelView" style="padding: 10px" v-html="myXlsx"></div>
    </div>
    <div style="text-align: center;padding: 10px;background-color: white;margin: 10px 0">
      <el-button @click="mark(1)" type="primary">横向标题</el-button>
      <el-button @click="mark(2)" type="primary">纵向标题</el-button>
    </div>
  </section>
</template>
<script>
  import {read, utils} from 'xlsx'
  import axios from "axios"

  export default {
    name: "index",
    data() {
      return {
        id: this.$route.query.id,
        fileId: this.$route.query.fileId,
        myXlsx: null,
        loading: true
      }
    },
    methods: {
      $init() {
        this.getExcel()
      },
      mark(markType) {
        this.$router.push({
          path: '/mapping/structuredText/dataMark',
          query: {id: this.id, fileId: this.fileId, markType}
        })
      },
      getExcel() {
        axios({
          method: 'get',
          url: '/api/structuredText/download',
          responseType: 'arraybuffer',
          params: {
            id: this.id
          }
        }).then(({data}) => {
          const workbook = read(new Uint8Array(data), {type: 'array'}) // 解析数据
          const worksheet = workbook.Sheets[workbook.SheetNames[0]] // workbook.SheetNames 下存的是该文件每个工作表名字,这里取出第一个工作表
          this.myXlsx = utils.sheet_to_html(worksheet) // 渲染
          this.$nextTick(function () { // DOM加载完毕后执行，解决HTMLConnection有内容但是length为0问题。
            this.setStyle4ExcelHtml()
          })
        })
      },
      // 设置Excel转成HTML后的样式
      setStyle4ExcelHtml() {
        const excelViewDOM = document.getElementById('excelView')
        if (excelViewDOM) {
          const excelViewTDNodes = excelViewDOM.getElementsByTagName('td')// 获取的是HTMLConnection
          if (excelViewTDNodes) {
            const excelViewTDArr = Array.prototype.slice.call(excelViewTDNodes)
            for (const i in excelViewTDArr) {
              const id = excelViewTDArr[i].id// 默认生成的id格式为sjs-A1、sjs-A2......
              if (id) {
                const idNum = id.replace(/[^0-9]/ig, '')// 提取id中的数字，即行号
                if (idNum && (idNum === '1' || idNum === 1)) { // 第一行标题行
                  excelViewTDArr[i].classList.add('class4Title')
                }
                if (idNum && (idNum === '2' || idNum === 2)) { // 第二行表头行
                  excelViewTDArr[i].classList.add('class4TableTh')
                }
              }
            }
          }
        }
        this.loading = false
      }
    }
  }
</script>
<style scoped>
  .home {
    width: 100%;
    height: 700px;
    background-color: white;
    overflow-y: scroll;
    overflow-x: scroll;
  }

  /deep/ table {
    max-width: 100% !important;
    border-collapse: collapse !important;
    border-spacing: 0 !important;
    text-align: center !important;
    border: 0 !important;
  }

  /deep/ table tr td {
    border: 1px solid gray;
    padding: 5px 20px;
  }

  /**整体样式 */
  /deep/ .excel-view-container {
    background-color: #FFFFFF;
  }

  /**标题样式 */
  /deep/ .class4Title {
    font-weight: bold !important;
    background-color: #eee !important;
  }

  /deep/ table tr td:first-child {
    font-weight: bold !important;
    background-color: #eee !important;
  }
</style>