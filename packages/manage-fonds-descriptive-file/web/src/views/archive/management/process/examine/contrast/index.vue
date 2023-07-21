<template>
  <section>
    <nac-info title="历史版本对比" back/>
    <div class="index_main">
      <el-row style="height: 100%;" :gutter="10">
        <el-col :span="12" style="height: 100%">
          <history-content :managementHistoryId="$route.query.managementCurrentId" :type="'current'"/>
        </el-col>
        <el-col :span="12" style="height: 100%">
          <history-content :managementHistoryId="$route.query.managementHistoryId" :type="'history'"/>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
<script>
import historyContent from "./historyContent";

export default {
  name: "index",
  components: {historyContent}
}
</script>