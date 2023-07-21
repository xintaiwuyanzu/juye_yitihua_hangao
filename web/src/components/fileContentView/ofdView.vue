<template>
  <iframe ref="iframe" frameborder="0"/>
</template>
<script>
import {abstractIframeView} from "./abstractViewer";
//todo 这个组件将来要，福州项目特有组件
export default {
  name: "ofdView",
  extends: abstractIframeView,
  methods: {
    async setUrl() {
      if (this.$refs.iframe) {
        if (this.url) {
          const request = this.requestMethod === 'post' ? this.$post : this.$get
          const url = this.replaceUrl()
          const {data} = await request(url, this.requestParams)
          if (data.success) {
            if (this.$refs.iframe && this.$refs.iframe.contentWindow && this.$refs.iframe.contentWindow.location) {
              this.$refs.iframe.contentWindow.location.replace(data.data)
            }
          }
        }
      }
    }
  }
}
</script>