<template>
  <section>
    <el-link type="primary" @click="showDialog">装盒</el-link>
    <el-dialog width="50%" title="装盒状态" :visible.sync="dialogShow" :close-on-click-modal="false" append-to-body>
      <div slot="footer" class="dialog-footer">
        <el-form label-width="100px">
          <el-form-item label="所在盒号">
            <el-input style="width: 90%" :disabled="true"/>
          </el-form-item>
          <el-form-item label="存放位置">
            <el-input style="width: 90%" :disabled="true"/>
          </el-form-item>
        </el-form>
        <el-button type="info" @click="dialogShow = false" class="btn-cancel">取 消</el-button>
        <el-button type="primary" @click="dialogShow = false" v-loading="loading" class="btn-submit">确 定</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import abstractComponent from "../abstractComponent";

export default {
  extends: abstractComponent,
  name: 'box',
  methods: {
    //显示弹出框
    showDialog() {
      this.dialogShow = true
    },
  },
}
</script>
