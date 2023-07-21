<template>
  <section>
      <nac-info title="年报提交">
          <el-button @click="tongguo('通过')" v-if="this.addForm.status=='待审核'&&$route.query.optype == 'edit'" type="success">通过</el-button>
          <el-button @click="tongguo('驳回')" v-if="this.addForm.status=='待审核'&&$route.query.optype == 'edit'" type="danger">驳回</el-button>
          <el-button @click="$router.back(-1)" type="primary" style="float: right; margin-right: 20px;">
              返回</el-button>
    </nac-info>

    <div style="width: 899px; margin: 0px auto 30px auto; background: #FFFFFF;padding: 10px">
      <el-form :model="addForm" label-width="80px" ref="sourceForm">
        <el-row>
          <el-col class="line title1" :span="4"
                  style="text-align: center;font-size: 20px;line-height: 70px;color: #606266; font-weight: bold;">年检名称:
          </el-col>
          <el-col class="line m" :span="20">
             {{ addForm.nianjianName }}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">单位名称</el-col>
          <el-col class="line" :span="5">(盖章)</el-col>
          <el-col class="line title1" :span="4">单位负责人</el-col>
          <el-col class="line" style="height: 47px" :span="5">
            {{ addForm.unitPerson }}
          </el-col>
          <el-col class="line title1" :span="2">年度</el-col>
          <el-col class="line" style="height: 47px" :span="4">
            {{ addForm.nian }}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">电话</el-col>
          <el-col class="line" style="height: 47px" :span="8">
            {{ addForm.phone }}
          </el-col>
          <el-col class="line title1" :span="4">详细地址</el-col>
          <el-col class="line " style="height: 47px" :span="8">
              {{ addForm.address }}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">档案室主任姓名</el-col>
          <el-col class="line" style="height: 47px" :span="8">
            {{ addForm.archiveDirectorName }}
          </el-col>
          <el-col class="line title1" :span="4">档案员姓名</el-col>
          <el-col class="line" style="height: 47px" :span="8">
            {{ addForm.archivePersonName }}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">档案管理现代化</el-col>
          <el-col class="line " :span="20">
            ①文书档案数据库建设情况：文件级数据库共
            {{ addForm.wenshuwenJianji }}
            条目；
            全文数据库共
            {{ addForm.wenshuQuanwen }}
            页；
            ②业务档案数据库建设情况：条目数据库
            {{ addForm.yewuTiaomu }}
            条，
            全文数据库共
            {{ addForm.yewuQuanwen }}
            页。
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">档案数量</el-col>
          <el-col class="line m" :span="20">
            文书档案:
            {{ addForm.wemshuJuan }}
            卷,
            {{ addForm.wemshuJian }}
            件;
            科技档案:
            {{ addForm.kejiJuan }}
            卷,
            {{ addForm.kejiJian }}
            件;
            会计档案:
            {{ addForm.kuaijiJuan }}
            卷,
            {{ addForm.kuaijiJian }}
            件;
            其他档案:
            {{ addForm.qitaJuan }}
            卷,
            {{ addForm.qitaJian }}
            件;
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">保管条件</el-col>
          <el-col class="line m " :span="20">
            库房面积:
            {{ addForm.baoguanKufang }}
            M2;
            空调:
            {{ addForm.baoguanKongtiao }}
            台;
            灭火器:
            {{ addForm.baoguanMiehuoqi }}
            只;
            档案箱:
            {{ addForm.baoguanXiang }}
            只;
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">本单位自检情况</el-col>
          <el-col class="line m" :span="20">
            {{ addForm.zijian }}
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">年检结果</el-col>
          <el-col class="line m textArea" :span="20">
            <el-input v-if="this.addForm.status=='待审核'"
                      type="textarea"
                      :autosize="{ minRows: 3, maxRows: 3 }" v-model="addForm.nianjian" maxlength="100"
                      clearable/>
            <span v-else>
              {{ addForm.nianjian }}
            </span>
          </el-col>
        </el-row>
        <!--<el-row style="text-align: center;margin-top: 5px" v-if="$route.query.optype == 'edit'">
          <el-button @click="tongguo('通过')" v-if="this.addForm.status=='待审核'" type="primary">通过</el-button>
          <el-button @click="tongguo('驳回')" v-if="this.addForm.status=='待审核'" type="danger">驳回</el-button>
        </el-row>-->
      </el-form>
    </div>
  </section>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
      addForm: {},
      loading: false,
    }
  },
  methods: {
    tongguo(type) {
      let url = '/onlineas/tongguo'
      this.addForm.status = type
      this.$http.post(url, this.addForm).then(
          ({data}) => {
            if (data.success) {
              this.$message.success('操作成功！')
              this.$router.back(-1);
            } else {
              this.$message.error(data.message)
            }
          })
    },
    $init() {
        let id = this.$route.query.id
        this.find(id)
    },
    find(id) {
      this.loading = true
      this.$http.post('/onlineas/findById', {id:id}).then(({data}) => {
        if (data.success) {
          this.addForm = data.data
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    }

  }

}
</script>

<style scoped>
.line {
  line-height: 35px;
  padding: 5px;
  padding-left: 12px;
  border: 1px solid #d0e5fd;
  text-align: left;
  color: #606266;
}

.m {
  text-align: left;
  width: 749px;
  height: 82px;
  line-height: 82px;
}
.el-textarea__inner:hover{
  boder:none;
}

.title1 {
  line-height: 35px;
  border: 1px solid #d0e5fd;
  text-align: left;
  font-size: 16px;
  background: #ecf2ff;
  color: #606266;
}

.l-right {
  float: right;
  text-align: right;
  color: #b3bac7;
  font-size: 14px;
  line-height: 70px;
  margin-right: 10px;
}

.sh {
  height: 82px;
  line-height: 82px;
}

.no {
  font-size: 28px;
  text-align: center;
}
.textArea>>>.el-textarea__inner {
  margin-top: -78px !important;
  margin-left: -4px;
  border: 0;
  resize: none;
}
</style>
