<template>

  <div>
    <el-tooltip content="即时通讯" placement="bottom" effect="light">

      <el-button circle type="primary"
                 @click="openWebimPc" icon="el-icon-s-comment">
        <!--        这里放的是未读信息数-->
        {{ newmsgcount > 99 ? "99" : newmsgcount }}
      </el-button>

    </el-tooltip>
    <word-pro-dialog ref="wordProDialog" @loadFinish="loadFinish" :modal="false"></word-pro-dialog>
  </div>
</template>
<script>
import wordProDialog from "./wordProDialog";

export default {
  components: {wordProDialog},
  data() {
    return {
      newmsgcount: 0
    }
  },
  mounted() {
    this.$refs.wordProDialog.getPerson()
    this.$refs.wordProDialog.load()
    window.addEventListener('message', (e) => {
      if (e.data.type === 'onNewMsg') {
        this.newmsgcount = e.data.msgs
      }
    })
  },
  methods: {
    openWebimPc() {
      this.$refs.wordProDialog.openUi()
    },
    loadFinish() {
      //this.loading = false
      this.$refs.wordProDialog.loadingColse()
    }
  }
}
</script>
