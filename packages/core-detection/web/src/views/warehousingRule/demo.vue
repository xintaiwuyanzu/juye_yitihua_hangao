<template>
  <table-index title="检查报告模板" :fields="fields" path="template/"
               ref="table"
               :defaultSearchForm="defaultSearchForm"
               deleteMulti>
    <template v-slot:search-$btns>
      <el-dropdown>
        <el-button type="primary">
          下载模板<i class="el-icon-arrow-down el-icon--right"></i>
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            <el-button type="text"
                       @click="expgs(scope.row)"
                       size="mini" width="45">公示模板
            </el-button>
          </el-dropdown-item>
          <el-dropdown-item>通报模板</el-dropdown-item>
          <el-dropdown-item>通知模板</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </template>
    <template v-slot:edit="form">
      <el-form-item prop="id" label="上传文件">
        <el-upload action="https://jsonplaceholder.typicode.com/posts/" multiple>
          <i class="el-icon-upload"></i>
          <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
          <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过500kb</div>
        </el-upload>
      </el-form-item>
    </template>
  </table-index>
</template>
<script>
/**
 * 待办任务列表
 */
export default {
  data() {
    return {
      //改后
      defaultSearchForm: {checkType: 'nd'},
      fields: [
        {
          prop: 'reportName',
          label: '报告名称',
          width: '150',
          search: true,
        },
        {
          prop: 'reportHeir',
          label: '上传人',
          width: '100',
          edit: false,
          insert: false
        },
        {
          prop: 'templateType',
          label: '模板类型',
          width: '140',
          search: true,
          required: true,
          fieldType: 'select',
          mapper: {
            "1": '公示模板',
            "2": '通报模板',
            "3":'通知模板'
          }
        },
        {
          prop: 'reportType',
          label: '报告类型',
          search: true,
          required: true,
          fieldType: 'select',
          mapper: {
            "1": '年度检查报告',
            "2": '日常检查报告'
          }
        }
      ],
    }
  },
  methods: {
    /**
     * 公示模板下载
     */
    expgs(row) {
      let url = "api/template/expgs?id=" + row.id
      window.open(url)
    }
  }
}
</script>
