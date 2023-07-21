<template>
  <iframe ref="iframe" frameBorder="0"/>
</template>
<script>
import {abstractIframeView} from "./abstractViewer";
import {encode} from 'js-base64'

export default {
  name: "pdfView",
  extends: abstractIframeView,
  methods: {
    async setUrl() {
      if (this.$refs.iframe) {
        if (this.url) {
          const fileUrl = encode(this.buildUrl())
          const url = `/pdf/web/viewer.html?file=${fileUrl}&disable=` + this.disable
          if (this.$refs.iframe && this.$refs.iframe.contentWindow) {
            this.$refs.iframe.contentWindow.location.replace(url)
          }
        }
      }
    }
  },
  computed: {
    disable() {
      let disable = '', arr = [];
      if (this.requestParams !== undefined) {
        if (this.requestParams.printStatus) {
          arr.push('print')
        }
        if (this.requestParams.downloadStatus) {
          arr.push('download')
        }
      } else {
        if (!this.hasRole('print')) {
          arr.push('print')
        }
        if (!this.hasRole('download')) {
          arr.push('download')
        }
      }
      disable = arr.join(',')
      return disable;
    }
  }
}
</script>