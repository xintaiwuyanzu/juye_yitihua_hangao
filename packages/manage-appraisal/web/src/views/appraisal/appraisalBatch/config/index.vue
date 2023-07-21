<template>
  <section>
    <nac-info back></nac-info>
    <div class="index_main" >
      <el-row style="height: 100%" :gutter="5">
        <el-col :span="10">
          <el-card class="box-card" style="height: 79vh;">
            <div slot="header" class="clearfix">
              <span style="padding-top: 12px">关键词库</span>
              <el-button icon="el-icon-plus" type="primary" size="mini" circle
                         style="float: right;"
                         @click="addWordGroup()"></el-button>
            </div>
            <work-group :batch-id="$route.query.batchId" ref="wordGroup"></work-group>
          </el-card>
        </el-col>
        <el-col :span="14">
          <el-card class="box-card" style="height: 79vh;">
            <div slot="header" style="height: 30px;" class="clearfix">
              <el-button icon="el-icon-plus" @click="addPerson()" type="primary" size="mini" circle style="float: right;">
              </el-button>
            </div>
            <person ref="person" :batch-id="$route.query.batchId" style="height: 600px"/>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>

<script>
import workGroup from './workGroup'
import person from './person'

export default {
  components: {workGroup, person},
  data() {
    return {
      fields: {
        batchName: {label: '名称', search: true, align: 'center'}
      },
    }
  },
  methods: {
    addWordGroup() {
      this.$refs.wordGroup.edit()
    },
    addPerson(){
      this.$refs.person.addPerson()
    }
  }
}
</script>
