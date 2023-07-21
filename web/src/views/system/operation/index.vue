<template>
  <section class="operation">
    <div class="index_container">
      <visitor class="visitor"/>
      <monitoring class="monitoring"/>
    </div>
    <div class="index_container">
      <historical class="historical"/>
      <log class="log"/>
      <loophole class="loophole"/>
    </div>
  </section>
</template>
<script>
import visitor from './visitor'
import monitoring from './monitoring'
import historical from "./Historical"
import log from "./log";
import loophole from "./loophole";

export default {
  components: {
    visitor,
    monitoring,
    historical,
    log,
    loophole
  },
  data(){
    return{
      item:[],
    }
  },
  methods:{

  },
}
</script>
<style lang="scss">

.operation {
  display: flex;

  .visitor {
    flex: 1;
    margin-bottom: 18px;
  }

  .index_container {
    display: flex;
    flex: 1;
    //flex-direction: row;
    //overflow: auto;

    .h_color {
      //color: $--color-primary;
      color: $--color-primary-dark-1;
    }

    .monitoring {
      width: 26%;
      margin-left: 18px;
      margin-bottom: 18px;
    }
    .index_massage {
      width: 33%;
      margin-bottom: 18px;
      margin-left: 18px;
    }
    .log {
      width: 33%;
      margin-bottom: 18px;
      margin-left: 18px;
    }
    .loophole {
       width: 20%;
       margin-bottom: 18px;
       margin-left: 18px;
     }
    .historical{
      margin-bottom: 18px;
      flex: 1;
    }

    .el-card__body {
      flex: 1;
      flex-direction: column;
      display: flex;
      overflow: auto;
    }
  }
}
.el-divider--horizontal{
  margin-top: 10px;
  margin-bottom: 4px;
}
</style>