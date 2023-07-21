<template>
  <iframe ref="iframe" frameborder="0"/>
</template>
<script>
import {abstractIframeView} from "./abstractViewer";
import {encode} from "js-base64";

export default {
  name: "wordView",
  extends: abstractIframeView,
  methods: {
    async setUrl() {
      if (this.$refs.iframe) {
        if (this.url) {
          const fileUrl = encode(this.buildUrl())
          const url = `/pdf/web/viewer.html?file=${fileUrl}`
          if (this.$refs.iframe && this.$refs.iframe.contentWindow) {
            this.$refs.iframe.contentWindow.location.replace(url)
          }
        }
      }
    }
  }
}
</script>