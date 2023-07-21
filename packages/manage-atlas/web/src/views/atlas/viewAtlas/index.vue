<template>
  <section>
    <nac-info back/>
    <div class="box">
      <div class="left">
        <h4 style="margin: 10px 0 0 10px">预览</h4>
        <div class="top">
          <div :key="index" class="img_box" v-for="(i,index) in imgs">
            <div style="background-color: rgba(211,211,211,0.05);height: 140px">
              <el-image
                  :preview-src-list="imgScans"
                  :src="i.src"
                  fit="cover"
                  style="height: 140px">
              </el-image>
            </div>
            <div class="msg"><span>名称: {{i.name}}</span></div>
            <div class="msg">
              <span>尺寸: 1980*1080px</span>
              <el-divider direction="vertical"/>
              <span>摄像: {{i.author}}</span>
            </div>
            <div class="msg">
              <span>时间: {{i.time}}</span>
            </div>
            <div style="line-height: 25px">
              <i @click="img=i,visible=true" class="el-icon-edit icon"></i>
              <el-divider direction="vertical"/>
              <i @click="remove(index)" class="el-icon-delete icon" style="color: red"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="right">
        <h4 class="h">基本信息</h4>
        <el-form :model="atlas" style="padding: 20px 20px 20px 10px">
          <el-form-item label="图册名称">
            <el-input v-model="atlas.atlasName"></el-input>
          </el-form-item>
          <el-form-item label="图册类型">
            <el-input v-model="atlas.atlasType"></el-input>
          </el-form-item>
          <el-form-item label="主题">
            <el-input v-model="atlas.atlasTitle"></el-input>
          </el-form-item>
          <el-form-item label="关键词">
            <el-select multiple placeholder="选择关键词" style="width: 100%" v-model="values">
              <el-option
                  :key="item.value"
                  :label="item.label"
                  :value="item.label"
                  v-for="item in keywords">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="图册描述">
            <el-input type="textarea" v-model="atlas.atlasMark"></el-input>
          </el-form-item>
          <div>
            <el-button @click="save" type="primary">保存</el-button>
          </div>
        </el-form>
      </div>
      <div>
        <el-dialog :visible.sync="visible" title="修改图片信息">
          <el-form :model="img">
            <el-form-item :label-width="width" label="图片名称">
              <el-input v-model="img.name"></el-input>
            </el-form-item>
            <el-form-item :label-width="width" label="摄像">
              <el-input v-model="img.author"></el-input>
            </el-form-item>
            <el-form-item :label-width="width" label="时间">
              <el-input v-model="img.time"></el-input>
            </el-form-item>
          </el-form>
          <div class="dialog-footer" slot="footer">
            <el-button @click="visible=false" type="primary">保存</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </section>
</template>

<script>
  export default {
    name: "index",
    data() {
      return {
        width: '80px',
        atlas: {},
        keywords: [{
          value: '选项1',
          label: '黄金糕'
        }, {
          value: '选项2',
          label: '双皮奶'
        }, {
          value: '选项3',
          label: '蚵仔煎'
        }, {
          value: '选项4',
          label: '龙须面'
        }, {
          value: '选项5',
          label: '北京烤鸭'
        }],
        values: [],
        imgs: [
          {
            name: '图1',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/e/5d/4a731a90594a4af544c0c25941171jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图2',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/8/27/f01c15bb73e1ef3793e64e6b7bbccjpeg.jpeg',
            author: '张三'
          },
          {
            name: '图3',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/a/3f/3302e58f9a181d2509f3dc0fa68b0jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图4',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/1/34/19aa98b1fcb2781c4fba33d850549jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图5',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/0/6f/e35ff375812e6b0020b6b4e8f9583jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图6',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/9/bb/e27858e973f5d7d3904835f46abbdjpeg.jpeg',
            author: '张三'
          },
          {
            name: '图7',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/d/e6/c4d93a3805b3ce3f323f7974e6f78jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图8',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/3/28/bbf893f792f03a54408b3b7a7ebf0jpeg.jpeg',
            author: '张三'
          },
          {
            name: '图9',
            time: '2022-05-27',
            src: 'https://fuss10.elemecdn.com/2/11/6535bcfb26e4c79b48ddde44f4b6fjpeg.jpeg',
            author: '张三'
          }
        ],
        img: {},
        visible: false,
        imgScans: []
      }
    },
    methods: {
      $init() {
        this.imgs.forEach(i => {
          this.imgScans.push(i.src)
        })
        this.$post('atlas/detail', {id: this.$route.query.id}).then(({data}) => {
          if (data && data.success) {
            this.atlas = data.data
            this.values = this.atlas.atlasKeyWord.split(',')
          }
        })
      },
      remove(index) {
        this.imgs.splice(index, 1)
      },
      save() {
        this.atlas.atlasKeyWord = this.values.toString()
        this.$post('/atlas/update', this.atlas).then(({data}) => {
          if (data && data.success) {
            this.$message({type: 'success', message: '保存成功'})
          }
        })
      }
    }
  }
</script>

<style scoped>
  .box {
    width: 100%;
    height: 100%;
    background-color: white;
    display: flex;
    overflow-y: hidden;
  }

  .left {
    width: 80%;
    height: 100%;
    display: block;
  }

  .top {
    width: 100%;
    padding: 10px 3px;
    overflow-y: scroll;
  }

  .right {
    width: 20%;
    height: 100%;
    text-align: center;
    border-left: 1px solid gainsboro;
    overflow-y: scroll;
  }

  .h {
    background-color: gainsboro;
    line-height: 40px;
  }

  .img_box {
    width: 210px;
    text-align: center;
    border: 1px solid lightgrey;
    margin-bottom: 10px;
    font-size: 13px;
  }

  .top {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-around;
  }

  .icon {
    cursor: pointer;
  }

  .msg {
    display: flex;
    padding: 2px 5px;
    font-size: 12px;
  }
</style>