<template>
  <section>
    <nac-info title="年报提交">
        <el-button @click="save('addForm')" type="success">保存
        </el-button>
        <el-button @click="$router.back(-1)" type="primary" style="float: right; margin-right: 20px;">
            返回</el-button>
    </nac-info>
    <div style="width: 899px; margin: 0px auto 30px auto; background: #FFFFFF;padding: 10px">
      <el-form :model="addForm" ref="addForm" :rules="rule">
        <el-row>
          <el-col class="line title1" :span="4"
                  style="text-align: center;font-size: 20px;line-height: 65px;color: #606266; font-weight: bold;">年检名称:
          </el-col>
          <el-col class="line m1" :span="20">
            <el-form-item prop="nianjianName" class="nianjianName">
            <el-input type="textarea"
                      style="font-size: 15px;margin-bottom: 10px"
                      :autosize="{ minRows: 2, maxRows: 2 }"
                      v-model="addForm.nianjianName" maxlength="100" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">单位名称</el-col>
          <el-col class="line" :span="5">(盖章)</el-col>
          <el-col class="line title1" :span="4">单位负责人</el-col>
          <el-col class="line1" :span="5">
          <el-form-item prop="unitPerson" class="border">
            <el-input v-model="addForm.unitPerson" maxlength="100" clearable/>
          </el-form-item>
          </el-col>
          <el-col class="line title1" :span="2">年度</el-col>
          <el-col class="line1" :span="4">
            <el-form-item prop="year" class="border">
              <el-date-picker
                      v-model="addForm.year"
                      type="year" style="width: 100%"
                      placeholder="选择年">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">电话</el-col>
          <el-col class="line1" :span="8">
            <el-form-item prop="phone" class="border">
            <el-input v-model="addForm.phone" maxlength="100" clearable/>
            </el-form-item>
          </el-col>
          <el-col class="line title1" :span="4">详细地址</el-col>
          <el-col class="line1 border" :span="8">
            <el-input v-model="addForm.address" maxlength="100" clearable/>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1" :span="4">档案室主任姓名</el-col>
          <el-col class="line1" :span="8">
            <el-form-item prop="archiveDirectorName" class="border">
            <el-input v-model="addForm.archiveDirectorName" maxlength="100" clearable/>
            </el-form-item>
          </el-col>
          <el-col class="line title1" :span="4">档案员姓名</el-col>
          <el-col class="line1" :span="8">
            <el-form-item prop="archivePersonName" class="border">
            <el-input v-model="addForm.archivePersonName" maxlength="100" clearable/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">档案管理现代化</el-col>
          <el-col class="line " :span="20">
            ①文书档案数据库建设情况：文件级数据库共
            <el-input v-model="addForm.wenshuwenJianji" style="width:8%" maxlength="100" />
            条目；
            全文数据库共
            <el-input style="width:8%" v-model="addForm.wenshuQuanwen" maxlength="100" />
            页；
            ②业务档案数据库建设情况：条目数据库
            <el-input v-model="addForm.yewuTiaomu" style="width:8%" maxlength="100" />
            条，
            全文数据库共
            <el-input style="width:8%" v-model="addForm.yewuQuanwen" maxlength="100" />
            页。
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">档案数量</el-col>
          <el-col class="line " :span="20">
            文书档案:
            <el-input v-model="addForm.wemshuJuan" style="width:8%" maxlength="100" />
            卷,
            <el-input v-model="addForm.wemshuJian" style="width:8%" maxlength="100" />
            件;
            科技档案:
            <el-input v-model="addForm.kejiJuan" style="width:8%" maxlength="100" />
            卷,
            <el-input v-model="addForm.kejiJian" style="width:8%" maxlength="100" />
            件;
            会计档案:
            <el-input v-model="addForm.kuaijiJuan" style="width:8%" maxlength="100" />
            卷,
            <el-input v-model="addForm.kuaijiJian" style="width:8%" maxlength="100" />
            件;
            其他档案:
            <el-input v-model="addForm.qitaJuan" style="width:8%" maxlength="100" />
            卷,
            <el-input v-model="addForm.qitaJian" style="width:8%" maxlength="100" />
            件;
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">保管条件</el-col>
          <el-col class="line m " :span="20">
            库房面积:
            <el-input v-model="addForm.baoguanKufang" style="width:8%" maxlength="100" />
            M2;
            空调:
            <el-input v-model="addForm.baoguanKongtiao" style="width:8%" maxlength="100" />
            台;
            灭火器:
            <el-input v-model="addForm.baoguanMiehuoqi" style="width:8%" maxlength="100" />
            只;
            档案箱:
            <el-input v-model="addForm.baoguanXiang" style="width:8%" maxlength="100" />
            只;
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">本单位自检情况</el-col>
          <el-col class="line m" :span="20">
            <el-input type="textarea" :autosize="{ minRows: 3, maxRows: 3 }"
                      v-model="addForm.zijian" maxlength="100"
                      clearable/>
          </el-col>
        </el-row>
        <el-row>
          <el-col class="line title1 sh" :span="4">年检结果</el-col>
          <el-col class="line m" :span="20">
            {{ addForm.nianjian }}
          </el-col>
        </el-row>
        <!--<el-row style="text-align: center;margin-top: 5px" >

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
      rule:{
        nianjianName: [{required: true, message: '方案名称不能为空', trigger: 'blur'}],
        year: [{type: 'date',required: true, message: '年检年度不能为空!', trigger: 'change'}],
        unitPerson: [{required: true, message: '单位负责人不能为空!', trigger: 'blur'}],
        phone: [{required: true, message: '电话不能为空!', trigger: 'blur'}],
        archiveDirectorName: [{required: true, message: '档案室主任姓名不能为空!', trigger: 'blur'}],
        archivePersonName: [{required: true, message: '档案员姓名不能为空!', trigger: 'blur'}],
      }
    }
  },
  methods: {
    $init() {
      if (this.$route.query.optype == 'edit') {
          let id = this.$route.query.id
          this.find(id)
      }
    },
    find(id) {
      this.loading = true
      this.$http.post('/onlineas/findById', {id:id}).then(({data}) => {
        if (data.success) {
          this.addForm = data.data
            this.addForm.year = data.data.nian
        } else {
          this.$message.error(data.message)
        }
        this.loading = false
      })
    },
    save(formName) {
      this.$refs.addForm.validate((valid) => {
        if (valid) {
          if (!this.addForm.year.length||this.addForm.year.length>4){
            this.addForm.nian = new Date(this.addForm.year).getFullYear()
          }
          let url = ''
          if (this.$route.query.optype == 'add') {
            url = '/onlineas/insert'
          } else {
            url = '/onlineas/update'
          }
          this.$http.post(url, this.addForm).then(
              ({data}) => {
                if (data.success) {
                  this.$message.success('操作成功！')
                  this.$router.back(-1);
                } else {
                  this.$message.error(data.message)
                }
              })
        } else {
          return false;
        }
      });
    },
  }

}
</script>

<style scoped>
.line {
  line-height: 51px;
  padding: 5px;
  padding-left: 12px;
  border: 1px solid #d0e5fd;
  text-align: left;
  color: #606266;
}
.border>>>.el-input__inner {
  border: 0;
}
.line1{
  text-align: center;
  line-height: 51px;
  padding-top: 10px;
  /*padding-bottom: 18px;*/
  margin-bottom: 0;
  padding-left: 12px;
  border: 1px solid #d0e5fd;
  text-align: left;
  color: #606266;
}

.m {
  text-align: left;
  height: 116px;
  line-height: 70px;
}

.title1 {
  line-height: 51px;
  border: 1px solid #d0e5fd;
  text-align: left;
  font-size: 16px;
  background: #ecf2ff;
  color: #606266;
}
.m1{
  line-height: 51px;
  text-align: left;
  height: 77px;
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
  height: 116px;
  line-height: 77px;
}

.no {
  font-size: 28px;
  text-align: center;
}
.nianjianName>>>.el-textarea__inner {
  border: 0;
  resize: none;
}
.m>>>.el-textarea__inner {
  margin-left: -4px;
  border: 0;
  resize: none;
}
</style>
