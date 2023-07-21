<template>
  <el-card shadow="hover" class="search_result_item">
    <section class="result_title">
      <el-tooltip>
        <span slot="content" v-html="item.TITLE"/>
        <a class="el-link el-link-default result_text" @click="getlist">
<!--        <a class="el-link el-link-default result_text" :href="href">-->
          {{ index + 1 }}:
          <span v-html="item.TITLE" style="margin-left: 10px"/>
        </a>
      </el-tooltip>
      <section class="result_btns">
        <slot/>
      </section>
    </section>
    <el-divider class="search-divider"/>
    <el-descriptions :column="3" border size="medium" style="margin-top: 4px">
      <el-descriptions-item label="档号">
        <span v-html="item.ARCHIVE_CODE"/>
      </el-descriptions-item>
      <el-descriptions-item label="文件时间">
        <span v-html="item.FILETIME"/>
      </el-descriptions-item>
      <el-descriptions-item label="责任者">
        <span v-html="item.DUTY_PERSON"/>
      </el-descriptions-item>
      <el-descriptions-item label="原文内容" :span="3" labelClassName="search-label" v-if="item.CONTENT">
        <span v-html="item.CONTENT.length>100?item.CONTENT.slice (0, 80)+'...':item.CONTENT"/>
      </el-descriptions-item>
      <el-descriptions-item label="标签" :span="3" v-if="item.tag&&item.tag.length>0" labelClassName="search-label">
        <el-tag :title="tags.name"
                v-for="tags in item.tag.slice(0,10)" :key="tags.id"
                style="margin: 0px 5px;border: 0;max-width: 100px;text-overflow: ellipsis;white-space: nowrap;overflow: hidden"
                :color="getColor(tags.type)"
                effect="dark">
          {{ tags.name }}
        </el-tag>
      </el-descriptions-item>
    </el-descriptions>
    <el-image v-show="showImg" :src="src" style="height: 140px;min-width:140px;margin: 10px" fit="cover">
      <div slot="error" class="el-image__error">
        暂无图片
      </div>
    </el-image>


    <el-dialog :visible.sync="show" width="90%" center>
      <strong slot="title" v-html="item.TITLE"></strong>
      <Metadata :query="query"></Metadata>
    </el-dialog>
  </el-card>

</template>
<script>
import qs from 'qs'
import Metadata from '../../components/metadataAndFileDetail'


/**
 * 查询结果渲染页面
 */
export default {
  name: "resultRender",
  props: {
    item: {
      type: Object,
      default() {
        return {}
      }
    },
    index: Number
  },
  components:{Metadata},
  data() {
    return {
      show:false,
      tagColor: [{
        type: "PER",
        tag: "人名",
        color: "#67ccaa"
      }, {
        type: "LOC",
        tag: "地名",
        color: "#ff9899"
      }, {
        type: "ORG",
        tag: "机构名",
        color: "#c7aee7"
      }, {
        type: "TIME",
        tag: "时间",
        color: "#4dd9e6"
      }],
      showImg: false,
      src: '',
      query:{
        watermark:true
      }
    }
  },
  // computed: {
  //   href() {
  //     const query = {
  //       formDataId: this.item.id,
  //       formDefinitionId: this.item.formDefinitionId,
  //       refType: 'archive',
  //       groupCode: 'default',
  //       watermark: true
  //     }
  //     return '#/archive/metadataAndFileDetail?' + qs.stringify(query)
  //   }
  // },
  methods: {
    $init() {
      //判断是否图片档案
      if (this.item.CATE_GORY_CODE == 'ZP') {
        //取第一个图片原文
        this.showImg = true
        this.$http.post('files/list', {
          refId: this.item.id,
          refType: 'archive',
          groupCode: 'default'
        }).then(({data}) => {
          if (data.success) {
            if (data && data.success) {
              this.src = '/api/fileView/getFile?fileId=' + data.data[0].id + '&filePath=' + data.data[0].id
            }
          }
        })
      }
    },

    getlist(){
      this.show=true
      this.query.formDataId=this.item.id
      this.query.formDefinitionId=this.item.formDefinitionId
      this.query.refType= 'archive'
      this.query.groupCode='default'

    },

    /*
    * 根据词性,获取标签颜色*/
    getColor(type) {
      for (let i = 0; i < this.tagColor.length; i++) {
        if (this.tagColor[i].type === type) {
          return this.tagColor[i].color
        }
      }
    },
  }
}
</script>
<style lang="scss">
.search_result_item:hover {
  border: solid $--color-primary;
}

.search_result_item {
  border: solid white;

  .el-card__body {
    padding: 4px 8px;
  }

  .search-divider {
    margin: 6px 0px;
  }

  .search-label {
    min-width: 80px;
  }

  .el-descriptions-item__cell {
    padding: 4px !important;
  }
}

</style>