<template>
  <section v-loading="loading">
    <el-tag type="info" effect="dark" v-if="imgs.length>0" style="position: absolute;top:2px;left: 50%;z-index: 999">
      {{ `${index + 1}/${imgs.length}` }}
    </el-tag>
    <image-view :img-src="imgs[index]" v-if="imgs.length>0"/>
    <span v-else>暂无数据</span>
    <template v-if="imgs.length>1">
        <span class="el-image-viewer__btn el-image-viewer__prev"
              v-if="!isFirst"
              @click="prev">
          <i class="el-icon-arrow-left"/>
        </span>
      <span class="el-image-viewer__btn el-image-viewer__next"
            v-if="!isLast"
            @click="next">
          <i class="el-icon-arrow-right"/>
        </span>
    </template>
  </section>
</template>
<script>
import abstractViewer from "./abstractViewer";
import tiff from 'tiff.js'
import NProgress from "nprogress";
import imageView from "../imageView";

tiff.initialize({TOTAL_MEMORY: 50 * 1024 * 1024});

/**
 * 渲染tiff类型的文件
 */
export default {
  name: "tiffView",
  extends: abstractViewer,
  components: {imageView},
  data() {
    return {
      imgs: [],
      index: 0,
      loading: false
    }
  },
  computed: {
    isFirst() {
      return this.index === 0
    },
    isLast() {
      return this.index === this.imgs.length - 1
    }
  },
  methods: {
    async setUrl() {
      this.loading = true
      this.imgs = []
      this.index = 0
      const request = this.requestMethod === 'post' ? this.$post : this.$get
      const url = this.replaceUrl()
      //异步请求数据
      const {data} = await request(url, this.requestParams, {
        responseType: 'arraybuffer',
        transformResponse(data) {
          NProgress.done()
          return {data, success: true};
        }
      })
      if (data.success) {
        const tiffData = new tiff({buffer: data.data})
        for (let i = 0, len = tiffData.countDirectory(); i < len; ++i) {
          tiffData.setDirectory(i)
          const imgs = tiffData.toDataURL()
          if (imgs) {
            this.imgs.push(imgs)
          }
        }
      }
      this.loading = false
    },
    prev() {
      if (this.index == 0) return;
      const len = this.imgs.length;
      this.index = (this.index - 1 + len) % len;
    },
    next() {
      if (this.index == this.imgs.length - 1) return;
      const len = this.imgs.length;
      this.index = (this.index + 1) % len;
    },
  }
}
</script>