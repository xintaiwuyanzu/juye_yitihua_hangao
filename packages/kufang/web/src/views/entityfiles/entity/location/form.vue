<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="库房名称：" prop="key" style="padding-right: 5px">
        <el-input v-model="searchForm.name" clearable/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="search" size="mini">搜 索</el-button>
        <el-button type="primary" @click="editForm()" size="mini">添加</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'添加')" width="40%">
      <el-form :model="form" :rules="rules" ref="form" label-width="110px">
        <el-form-item label="库房名称：" prop="name" required>
          <el-input v-model="form.name" clearable/>
        </el-form-item>
        <el-form-item label="楼层：" prop="locFloor" required>
          <el-input v-model="form.locFloor" clearable/>
        </el-form-item>
        <el-form-item label="库藏描述：" prop="description">
          <el-input type="textarea" v-model="form.description" :autosize="{ minRows: 3, maxRows: 6 }" clearable/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="saveForm" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import fromMixin from '@dr/auto/lib/util/formMixin'

export default {
  data() {
    return {
      searchForm: {},
      path: 'locationkufang',
      autoClose: true,
    }
  },
  methods: {},
  mixins: [fromMixin]
}
</script>
