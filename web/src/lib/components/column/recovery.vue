<template>
  <section>
    <el-link type="success" @click="open" :modal="false">恢复</el-link>
    <!--                          暂留                    -->
    <!--    <el-dialog width="40%" title="提示" :visible.sync="dialogShow" append-to-body>-->
    <!--      &lt;!&ndash;      <span> 此操作将选中项恢复至原档案库, 是否继续?</span>&ndash;&gt;-->
    <!--      <div slot="footer" class="dialog-footer">-->
    <!--        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>-->
    <!--        <el-button type="primary" @click="recovery('RECEIVE')" v-loading="loading" class="btn-submit">恢复到接收库</el-button>-->
    <!--        <el-button type="primary" @click="recovery('MANAGE')" v-loading="loading" class="btn-submit">恢复到管理库</el-button>-->
    <!--      </div>-->
    <!--    </el-dialog>-->
  </section>
</template>

<script>
import abstractColumnComponent from "../abstractColumnComponent";

export default {
  extends: abstractColumnComponent,
  name: 'recovery',
  methods: {
    recovery(status) {
      this.eventBus.$emit('changeStatus', {ids: this.row.id, status, row: this.row})
      this.dialogShow = false
    },
    open() {
      this.$confirm('确定恢复到【接收暂存库】?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //默认恢复到接收库
        this.recovery('RECEIVE')
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消恢复'
        });
      });
    }
  }
}
</script>
