<template>
  <section v-show="archiveCarData.show">
    <el-dropdown @command="carCommand" ref="dropdown">
      <el-tooltip :content="tipText">
        <el-button type="primary"
                   :icon="archiveCarData.archiveCar.id?'el-icon-shopping-bag-1':'el-icon-shopping-bag-2'"
                   class="car_btn"
                   circle>
        </el-button>
      </el-tooltip>
      <el-dropdown-menu slot="dropdown" class="car_menu">
        <el-dropdown-item command="add">
          <icon icon="plus-circle"/>
          新建
        </el-dropdown-item>
        <el-dropdown-item command="car">
          <icon icon="refresh-cw"/>
          切换
        </el-dropdown-item>
        <el-dropdown-item command="detail" v-if="archiveCarData.archiveCar.id">
          <icon icon="eye"/>
          预览
        </el-dropdown-item>
        <el-dropdown-item command="none">
          <icon icon="send"/>
          跳转
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>

    <el-drawer title="我的档案车"
               :visible.sync="archiveCarData.carDrawerShow==='car'"
               direction="rtl"
               size="600px"
               @close="closeDialog"
               custom-class="table_drawer"
               append-to-body>
      <table-index path="/archiveCarBatch"
                   :fields="archiveCarField"
                   @row-dblclick="switchCar"
                   ref="carTable"
                   :showTitle="false"
                   :edit="false"
                   :insert="false">
        <template v-slot:table-$btns="scope">
          <el-button @click="switchCar(scope.row)" type="text" width="20">选择</el-button>
        </template>
      </table-index>
    </el-drawer>
    <el-drawer :title="tipText+'详情'"
               :visible.sync="archiveCarData.carDrawerShow==='detail'"
               direction="rtl"
               size="880px"
               append-to-body
               @close="closeDialog"
               custom-class="table_drawer">
      <table-index path='/archiveCarDetail'
                   ref="detailTable"
                   :defaultSearchForm="defaultSearchForm"
                   :fields="detailFields"
                   :edit="false"
                   :showTitle="false" :insert="false">
        <template v-slot:table-$btns="{row}">
          <el-button type="text" @click="selectArchive(row)" width="50">选择</el-button>
        </template>
      </table-index>
    </el-drawer>
    <el-dialog title="新建档案车"
               :visible.sync="archiveCarData.carDrawerShow==='add'"
               @close="closeDialog"
               width="600px" append-to-body>
      <form-render :model="archiveCarForm" :fields="archiveCarField" label-width="120px" ref="form"/>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="saveCar">保 存</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="'添加档案到'+tipText" width="600px"
               :visible.sync="archiveCarData.carDrawerShow==='addCar'"
               @close="closeDialog"
               append-to-body>
      <el-form :model="archiveCarData.detail" label-width="120px">
        <el-form-item prop="batchType" label="档案标记">
          <select-async v-model="archiveCarData.detail.archiveTag"
                        :mapper="archiveCarData.tagSelect"
                        labelKey="name"
                        valueKey="code"
                        v-if="archiveCarData.carStatic"
                        placeholder="请选择档案标记"/>
          <el-autocomplete v-model="archiveCarData.detail.archiveTag"
                           :fetch-suggestions="tagSelect"
                           v-else
                           placeholder="请选择或者输入档案标记"/>
        </el-form-item>
        <el-form-item prop="description" label="描述">
          <el-input
              type="textarea"
              :rows="4"
              placeholder="请输入内容"
              v-model="archiveCarData.detail.description"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="info" @click="closeDialog">取 消</el-button>
        <el-button type="primary" @click="addToCar()">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>
<script>
import {useArchiveCar} from "./useArchiveCar";

export default {
  name: 'archiveCar',
  setup() {
    return useArchiveCar()
  },
  data() {
    return {
      /**
       * 添加档案车表单
       */
      archiveCarForm: {batchType: 'default'},
      archiveCarField: {
        batchName: {
          label: '档案车名称',
          required: true,
          search: true
        },
        batchType: {
          label: '档案车类型',
          fieldType: 'select',
          // mapper: this.archiveCarData.batchType,
          url: 'archiveCarBatch/archiveCarType',
          params: {withTag: false},
          labelKey: 'name',
          required: true,
          valueKey: 'type'
        }
      },
      detailFields: {
        archiveCode: {label: '档号', search: true, width: 160},
        title: {label: '题名', width: 120},
        fondName: {label: '全宗', width: 120},
        archiveTag: {
          label: '档案标记',
          width: 100,
          url: '/archiveCarBatch/archiveCarTag',
          params: this.tagParams,
          labelKey: 'name',
          valueKey: 'code'
        },
        description: {label: '描述', width: 120},
      }
    }
  },
  computed: {
    tipText() {
      return this.archiveCarData.archiveCar.id ? '当前档案车->' + this.archiveCarData.archiveCar.batchName : '档案车'
    },
    tagParams() {
      return {
        carType: this.archiveCarData.batchType,
        withDynamic: true
      }
    }
  },
  watch: {
    'archiveCarData.carDrawerShow'(showType) {
      if (showType === 'add') {
        this.archiveCarForm = {batchType: 'default'}
      } else if (showType === 'detail') {
        if (this.$refs.detailTable) {
          this.$refs.detailTable.reload()
        }
      } else if (showType === 'car') {
        if (this.$refs.carTable) {
          this.$refs.carTable.reload()
        }
      } else if (showType === 'dropdown') {
        if (this.$refs.dropdown) {
          this.$refs.dropdown.show()
          this.archiveCarData.carDrawerShow = 'none'
        }
      }
    }
  },
  methods: {
    selectArchive(row) {
      this.$root.$emit('$carSelect', row)
    },
    /**
     * 下拉菜单点击事件
     */
    carCommand(cmd) {
      this.archiveCarData.carDrawerShow = cmd
      if (cmd === 'none') {
        const path = this.archiveCarData.archiveCar.id ? '/archive/archiveCar/detail' : '/archive/archiveCar'
        this.$router.push({
          path,
          query: {
            id: this.archiveCarData.archiveCar.id,
            batchType: this.archiveCarData.archiveCar.batchType
          }
        })
      }
    },
    /**
     * 保存档案车
     * @returns {Promise<void>}
     */
    async saveCar() {
      //校验表单
      await this.$refs.form.validate()
      if(this.archiveCarForm.batchName==''|| this.archiveCarForm.batchType==''){
        this.$message.warning("请输入完整的档案车信息")
        return
      }
      const {data} = await this.$post('/archiveCarBatch/insert', this.archiveCarForm)
      if (data.success) {
        await this.switchCar(data.data)
      } else {
        this.$message.error(data.message)
      }
    },
    async switchCar(row) {
      this.archiveCarData.archiveCar = row
      this.archiveCarData.carDrawerShow = 'none'
    },
    defaultSearchForm() {
      return {batchId: this.archiveCarData.archiveCar.id}
    },
    tagSelect(txt, cb) {
      cb(this.archiveCarData.tagSelect)
    },
    closeDialog() {
      this.archiveCarData.carDrawerShow = 'none'
    }
  }
}
</script>
<style lang="scss">
.car_btn {
  i {
    font-size: 18px;
  }
}

.car_menu {
  background: $--color-primary;

  .el-dropdown-menu__item {
    .icon {
      margin-right: 4px;
    }

    font-size: 14px;
    line-height: 20px;
    text-align: center;
    color: $--color-white;
    padding: 6px 14px;
  }
}
</style>
