<template>
  <div class="sec_warning">警告：本系统禁止处理涉密信息!!!</div>
</template>
<script>
export default {}
</script>
<style lang="scss">
.sec_warning {
  background-color: red;
  color: yellow;
  position: absolute;
  top: 10px;
  left: 44%;
  font-size: large;
  font-weight: bold;
  padding: 4px 12px;
  animation: twinkle 100ms 10 alternate;
}

@keyframes twinkle {
  0% {
    opacity: 0.9;
  }
  100% {
    opacity: 0;
  }
}
</style>