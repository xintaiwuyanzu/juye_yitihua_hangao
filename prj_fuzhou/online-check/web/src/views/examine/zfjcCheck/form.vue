<template>
  <section>
    <el-form :model="searchForm" ref="searchForm" inline class="searchForm">
      <el-form-item label="检查人员:" prop="personName">
        <el-input v-model="searchForm.personName" clearable placeholder="请输入检查人员"/>
      </el-form-item>
      <el-form-item label="检查批次:" prop="pici">
        <el-input v-model="searchForm.pici" clearable placeholder="请输入检查批次"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="searchF" size="mini">搜 索</el-button>
        <el-button @click="$refs.searchForm.resetFields()" size="mini">重 置</el-button>
        <el-button type="primary" @click="add()" size="mini">添加执法检查</el-button>
        <el-button type="primary" @click="addtz()" size="mini">下载检查通知单</el-button>
        <el-button type="primary" @click="tongbao()" size="mini">下载检查通报</el-button>
      </el-form-item>
    </el-form>
    <el-dialog :visible.sync="edit" :title="(form.id?'编辑':'发起执法检查')" width="80%"
               :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="150px" :inline="false">

        <el-card class="box-card">
          <div>
            <el-row>
              <el-col :span="18">
                <el-form-item label="人员" prop="personId">
                  <el-select multiple
                             style="width: 88%"
                             v-model="form.personId"
                             filterable clearable
                             default-first-option
                             placeholder="请选择人员">
                    <el-option
                        v-for="(item,key) in persons"
                        :key="key"
                        :label="item.userName"
                        :value="item.id"/>
                  </el-select>
                  <el-button @click="suijiPerson()" type="primary">随机</el-button>
                </el-form-item>
              </el-col>
              <el-col :span="18">
                <el-form-item label="检查单位" prop="organiseId">
                  <el-select multiple v-if="!form.id"
                             style="width: 88%"
                             v-model="form.organiseId"
                             filterable clearable
                             default-first-option
                             placeholder="请选择检查单位">
                    <el-option
                        v-for="item in organiseIds"
                        :key="item.id"
                        :label="item.organiseName"
                        :value="item.id"/>
                  </el-select>
                  <el-select v-else disabled
                             style="width: 88%"
                             v-model="form.organiseName"
                             filterable clearable
                             default-first-option
                             :placeholder="form.organiseName">
                  </el-select>

                  <el-button @click="suijiDanwei" type="primary" :disabled="form.id">随机
                  </el-button>

                </el-form-item>
              </el-col>
              <el-col :span="3">
                <el-input-number v-model="num" controls-position="right" @change="handleChange" :min="1"
                                 :max="10"></el-input-number>
              </el-col>
              <el-col :span="3">
                <span style="line-height: 20px;"> 家单位</span>
              </el-col>
              <el-col :span="20">

                <el-form-item label="检查内容" prop="categoryId">

                  <!--                  <select-dict type="zfjc.type" style="width: 200px" v-model="form.categoryId" placeholder="请选择检查内容"-->
                  <!--                               @change="suiji(form.categoryId)"/>-->
                  <!--                  <el-input v-if="form.id"-->
                  <!--                            placeholder="" disabled="true"-->
                  <!--                            v-model="form.categorySmallName"-->
                  <!--                            clearable>-->
                  <!--                  </el-input>-->
                  <el-cascader
                      style="width: 90%"
                      v-model="form.categoryIdContact"
                      :options="options"
                      :props="props"
                      :show-all-levels="false"
                      clearable>

                  </el-cascader>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="计划检查时间" prop="jihuaTime">
                  <el-date-picker
                      v-model="jihuaTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">

                <el-form-item label="实际检查时间" prop="shijiTime">
                  <el-date-picker
                      v-model="shijiTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="结果填写时间" prop="jieguoTime">
                  <el-date-picker
                      v-model="jieguoTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间"
                  >
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="通知整改时间" prop="tongzhiTime">
                  <el-date-picker
                      v-model="tongzhiTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="整改到期时间" prop="zhenggaiTime">
                  <el-date-picker
                      v-model="zhenggaiTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="整改完成时间" prop="wanchengTime">
                  <el-date-picker
                      v-model="wanchengTime"
                      type="date"
                      value-format="timestamp"
                      default-time="12:00:00"
                      placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="状态" required v-show="form.id">
                  <select-dict v-model="form.status" type="zfjc.status"
                               placeholder="请选择状态"/>
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="批次" v-show="!form.id">
                  <el-input
                      placeholder="请输入批次"
                      v-model="form.pici"
                      clearable>
                  </el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </div>
        </el-card>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="saveLibForm" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
    <el-dialog
        title="检查单详情"
        :visible.sync="tongzhiShow"
        width="30%"
        center>
      <div class="block">
        <span class="demonstration">默认</span>
        <el-date-picker
            v-model="timeinterval"
            value-format="yyyy年MM月"
            format="yyyy-MM"
            type="monthrange"
            range-separator="至"
            start-placeholder="开始月份"
            end-placeholder="结束月份">
        </el-date-picker>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="addtz" v-loading="loading">保 存</el-button>
      </div>
    </el-dialog>
  </section>
</template>

<script>
import moment from 'moment'

export default {

  props: {

    multipleSelection: Array //这样可以指定传入的类型，如果类型不对，会警告
  },

}
</script>
