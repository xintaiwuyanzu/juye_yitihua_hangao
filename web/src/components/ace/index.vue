<template>
  <editor @init="editorInit"
          :value="value"
          @input="v=>$emit('input',v)"
          lang="text"
          style="overflow:scroll"
          :options="options"
          theme="chrome"/>
</template>
<script>

export default {
  name: 'ace',
  components: {
    editor: require('vue2-ace-editor')
  },
  props: {
    /**
     * 数据值
     */
    value: {type: String}
  },
  data() {
    return {
      content: '',
      options: {
        // enableBasicAutocompletion: true, // 启用基本自动完成
        // enableSnippets: true, // 启用代码段
        // enableLiveAutocompletion: true, // 启用实时自动完成
        printMargin: false,//显示设置页边距
        printMarginColumn: 30,//设置页边距
        showPrintMargin: false, //去除编辑器里的竖线
        highlightGutterLine: false,//高亮边线
        // displayIndentGuides: false, // 显示参考线
        enableEmmet: true, // 启用Emmet
        tabSize: 6, // 标签大小
        fontSize: 14, // 设置字号
        useWorker: true, // 使用辅助对象
        enableMultiselect: true, //     选中多处
        readOnly: true, // 是否只读
        showFoldWidgets: true, // 显示折叠部件
        fadeFoldWidgets: true, // 淡入折叠部件
        wrap: true, //换行
        animatedScroll: true//滚动动画
      }
    }
  },
  methods: {
    editorInit() {
      require('brace/ext/language_tools')
      require('brace/mode/html')
      require('brace/mode/javascript')
      require('brace/mode/less')
      require('brace/theme/chrome')
      require('brace/snippets/javascript')
    }
  }
}
</script>