<template>
  <!--  :tableProp="tableProp" @dataLoaded="dataLoaded"-->
  <table-index :fields="fields" back path="stdTag" title="国家标准标签管理"/>
</template>
<script>
  /**
   * 国家标准标签
   */
  export default {
    name: "stdTag",
    data() {
      return {
        fields: {
          labelName1st: {label: '一级标签内容'},
          labelName2nd: {label: '二级标签内容'}
        },
        tableProp: {
          spanMethod: this.objectSpanMethod
        },
        spanMap: new Map()
      }
    },
    methods: {
      //合并单元格方法
      objectSpanMethod({row, column, rowIndex, columnIndex}) {
        const key = row.labelName1st
        if (columnIndex === 1) {
          const index = this.spanMap.get(key)[0]
          const length = this.spanMap.get(key)[1]
          if (rowIndex === index) {
            return [length, 1]
          } else {
            return [0, 0]
          }
        }
      },
      /**
       * 数据加载完成后回调
       * @param data
       */
      dataLoaded(data) {
        if (data.data) {
          let d = data.data
          //获取单元格合并的数组
          let map = new Map()
          //把一级标签去掉
          d = d.filter(i => {
            return i.labelName2nd !== ''
          })
          //获取每个一级标签的二级标签数量
          d.forEach((i, index) => {
            const key = i.labelName1st
            if (map.has(key)) {
              let value = map.get(key)
              value.push(i)
            } else {
              map.set(key, [i])
            }
          })
          //合并的数据  ['一级':[二级开始的index,二级数量]]
          let index = 0
          for (let key of map.keys()) {
            this.spanMap.set(key, [index, map.get(key).length])
            index = index + map.get(key).length
          }
        }
      }
    }
  }
</script>