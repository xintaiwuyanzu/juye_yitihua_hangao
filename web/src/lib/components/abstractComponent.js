/**
 * 各种组件抽象父类，自动注入相关属性
 */
export default {
    props: ['currentRow', 'currentSelect'],
    computed: {
        //当前表单Id
        formId() {
            return this.eventBus.formId
        },
        //当前门类
        category() {
            return this.eventBus.category
        },
        //当前全宗
        fond() {
            return this.eventBus.fond
        },
        //当前控件所在列表页的上一级列表页，可能为空
        parentIndex() {
            return this.eventBus.parentIndex
        },
        //当前控件所在列表页的下一级列表页，可能为空
        childrenIndex() {
            return this.eventBus.childrenIndex
        }
    },
    inject: {
        //整个档案管理页面的根组件，可以通过this.libIndex.$refs.FILE获取对应的关联的列表页
        libIndex: {default: () => null},
        //事件总线，用来各个组件之间通讯，实际上是列表控件本身
        eventBus: {default: () => null},
    },
    data() {
        return {
            //是否加载数据
            loading: false,
            //基本上每个控件都是弹出框形式
            dialogShow: false
        }
    }
}
