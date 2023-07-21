<template>
    <table-index title="库房管理" :fields="fields"
                 path="locationkufang"
                 :edit="true"
                 :delete="true"
                 :insert="true"
                 ref="table">
      <template v-slot:table-$btns="{row}">
        <el-button type="text" @click="details(row)" size="mini" width="80">库房详情</el-button>
      </template>
    </table-index>
</template>

<script>

export default {
  data() {
    return {
      fields: {
        name:{
          label: '库房名称',
          align: 'center',
          component: 'text',
          route: true,
          routerPath: 'location/area',
          search:true,
          required:true,
          queryProp: ['id', 'name']
        },
        locFloor:{ label: '楼层', width: "80",required:true},
        locArea:{ label: '档案室面积', width: "100",},
        description:{label: '库藏描述', width: "300"},
        createDate: {label: '创建时间', edit:false,search:false, dateFormat: "YYYY-MM-DD HH:mm:ss",width: 150}
      },
    }
  },
  methods: {
    details(row){
      console.log(row)
      this.$router.push({path:'/entityfiles/entity/location/denseframe/roomDetail',query:{ id : row.id }})
    }
  }
}
</script>
