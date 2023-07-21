<template>
  <el-card v-loading="loading" class="cards">
    <el-container>
      <el-aside width="55%">
        <span class="system_access">系统访问人数</span>
        <div class="access">
          <div class="modular">
                       <span class="num">
                          {{ this.loginPerson.history_person }}
                       </span>
            <span class="subtitle">历史人数</span>
          </div>
          <div class="modular">
                       <span class="num" style="color: #20d49b">
                          {{ this.loginPerson.max_person }}
                       </span>
            <span class="subtitle">历史最大人数</span>
          </div>
          <div class="modular">
                       <span class="num" style="color: #ee9f42">
                          {{ this.loginPerson.system_day }}
                       </span>
            <span class="subtitle">系统运行天数</span>
          </div>
        </div>
      </el-aside>
      <el-main class="main">
        <span class="system_access">在线人数</span>
        <div class="system_content">
          <el-row class="row">
            <el-col>
              <span class="label">当日最多同时在线人数（人）</span>
            </el-col>
            <el-col>
              <el-progress :percentage="this.loginPerson.current_day_person"
                           :color="customColor1"
                           :stroke-width="13"
                           :format="current_day_person">
              </el-progress>
            </el-col>
          </el-row>
          <el-row class="row">
            <el-col>
              <span class="label">在线人数（人）</span>
            </el-col>
            <el-col>
              <el-progress :percentage="this.loginPerson.current_person"
                           :color="customColor2"
                           :stroke-width="13"
                           :format="current_person">
              </el-progress>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </el-card>
</template>

<script>
export default {
  name: "visitor",
  data() {
    return {
      loading: false,
      customColor1: '#2c9ceb',
      customColor2: '#6b6bee',
      customColor3: '#1fd0a4',
      loginPerson: {
        history_person:0,
        max_person:0,
        system_day:0,
        current_day_person:0,
        current_person:0,
      },
    }
  },
  methods: {
    current_day_person() {
      return `${this.loginPerson.current_day_person}/100`;
      //return `1000/1000`
    },
    current_person() {
      return `${this.loginPerson.current_person}/100`;
    },
    $init() {
      this.maxLoginNumber()
    },
    maxLoginNumber() {
      this.$http.post("LoginRecord/maxLoginNumber").then(({data}) => {
        if (data.success) {
          this.loginPerson = data.data
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  padding: 0;
  margin-left: 6%;

  .system_content {
    margin-top: 7%;

    .row {
      padding: 8px 0;
    }
  }
}

.label {
  font-size: 14px;
  font-weight: normal;
  font-stretch: normal;
  line-height: 20px;
  color: #606266;
}

.cards {
  padding: 16px;
}

.access {
  //width: 50%;
  display: flex;
  flex: 1;
  margin-top: 40px;

  .modular {
    margin-top: 30px;
    padding: 10px;
    //width: 15%;
    text-align: center;

    .num {
      font-size: 70px;
      font-weight: 500;
      font-stretch: normal;
      line-height: 32px;
      color: #2c9ceb;
    }

    .subtitle {
      display: inline-block;
      font-weight: normal;
      font-stretch: normal;
      line-height: 50px;
      color: #606266;
    }
  }
}

.system_access {
  height: 80%;
  font-size: 20px;
  font-weight: 500;
  font-stretch: normal;
  line-height: 25px;
  letter-spacing: 1px;
  color: $--color-primary;
}

::v-deep .el-col-24 {
  padding-bottom: 10px;
}
</style>