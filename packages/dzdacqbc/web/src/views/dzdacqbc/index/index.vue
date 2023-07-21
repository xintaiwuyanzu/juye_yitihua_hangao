<template>
  <section>
    <nac-info/>
    <div class="index_main card table-container" style="overflow: auto">
      <el-row style="height: 48%;margin-bottom: 0">
        <el-col :span="24">
          <el-card>
            <div slot="header">
              <strong>存储空间</strong>
              <el-tag style="margin-left: 50px">{{ '档案总数：' + archiveCount }}</el-tag>
              <el-button style="float: right" type="text" @click="moreSpace">更多
                <i class="el-icon-d-arrow-right"/>
              </el-button>
            </div>
            <space-table/>
          </el-card>
        </el-col>
      </el-row>
      <el-row style="height: 48%;margin-bottom: 0" type="flex">
        <!--        <el-col :span="12">
                  <el-card>
                    <div slot="header">
                      <strong>任务列表</strong>
                      <el-button style="float: right" type="text" @click="moreTask">更多
                        <i class="el-icon-d-arrow-right"/>
                      </el-button>
                    </div>
                    <task-table/>
                  </el-card>
                </el-col>-->
        <el-col :span="24">
          <el-card style="height: 100%">
            <div slot="header">
              <strong>报警信息</strong>
              <el-button style="float: right" type="text" @click="moreAlarm">更多
                <i class="el-icon-d-arrow-right"/>
              </el-button>
            </div>
            <alarm-table/>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </section>
</template>
<script>
import alarmTable from "./alarmTable";
import spaceTable from "./spaceTable";

export default {
  components: {
    // taskTable,
    alarmTable,
    spaceTable
  },
  data() {
    return {
      reader: [],
      form: [],
      count: '',
      title: '',
      reservation: [],
      today: [],
      curPerson: {},
      archiveCount: 0,
    }
  },
  methods: {
    $init() {
      this.countArchive()
    },
    moreTask() {
      this.$router.push({path: '/dzdacqbc/task', query: {back: true}})
    },
    moreAlarm() {
      this.$router.push({path: '/dzdacqbc/alarm', query: {back: true}})
    },
    moreSpace() {
      this.$router.push({path: '/dzdacqbc/spaces', query: {back: true}})
    },
    async countArchive() {
      const {data} = await this.$http.post('/earchive/countArchive')
      this.archiveCount = data.data
    }
  }
}
</script>
<style lang="scss">
.finance {
  text-align: center;
  font-size: 20px;

  .el-button {
    font-size: 30px;
    font-weight: bold;
  }
}
</style>
