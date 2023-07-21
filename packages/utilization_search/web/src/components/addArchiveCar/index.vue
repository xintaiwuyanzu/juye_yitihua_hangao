<template>
  <el-dropdown type="primary" @command="doAdd">
    <el-tooltip content="添加档案车" placement="top">
      <el-button type="primary" icon="el-icon-plus" @click="doAdd" circle style="padding: 9px"/>
    </el-tooltip>
    <el-dropdown-menu>
      <el-tooltip content="添加档案车并标记">
        <el-dropdown-item icon="el-icon-s-flag" command="tag"/>
      </el-tooltip>
    </el-dropdown-menu>
  </el-dropdown>
</template>
<script>

//import {useArchiveCar} from "../../../../../manage-filecar/web/src/components/archiveCar/useArchiveCar";

import {useArchiveCar} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";

/**
 * 添加档案车操作按钮
 */
export default {
  name: "addArchiveCar",
  props: {
    /**
     * 表单数据Id
     */
    formDataId: {type: String},
    /**
     * 表单定义Id
     */
    formDefinitionId: {type: String}
  },
  // setup() {
  //   return useArchiveCar()
  // },

  setup() {
    return useArchiveCar()
  },

  methods: {
    $init() {
      this.archiveCarData.show = true
    },
    // change() {
    //   this.archiveCarData.show = false
    // },
    async doAdd(tag) {
      this.archiveCarData.detail.formDefinitionId = this.formDefinitionId
      this.archiveCarData.detail.formDataId = this.formDataId
      await this.addToCar(tag === 'tag')
    }
  }
}
</script>