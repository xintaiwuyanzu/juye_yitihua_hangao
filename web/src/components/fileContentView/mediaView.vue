<template>
  <section>
    <video ref="flv" class="centeredVideo" controls autoplay/>
  </section>
</template>
<script>
/**
 * 使用flvjs预览多媒体文件
 */
import abstractViewer from "./abstractViewer";
import flvjs from 'flv.js/src/flv'

export default {
  name: "mediaView",
  extends: abstractViewer,
  beforeDestroy() {
    this.unload()
  },
  methods: {
    unload() {
      if (this.flvPlayer) {
        this.flvPlayer.unload();
        this.flvPlayer.detachMediaElement();
        this.flvPlayer.destroy();
        this.flvPlayer = null
      }
    },
    setUrl() {
      this.unload()
      if (this.url) {
        this.flvPlayer = flvjs.createPlayer({
          type: 'mp4',
          url: this.buildUrl()
        })
        this.flvPlayer.attachMediaElement(this.$refs.flv)
        this.flvPlayer.load()
      }
    }
  }
}
</script>
<style lang="scss">
.centeredVideo {
  display: block;
  width: 100%;
  height: 100%;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: auto;
}
</style>
