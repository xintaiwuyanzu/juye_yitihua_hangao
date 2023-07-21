/**
 * 抽象父类，和工具方法
 *
 */
export default {
    props: {
        /**
         * 表单定义Id
         */
        formDefinitionId: {type: String},
        /**
         *表单实例
         */
        formData: {type: Object, default: () => ({})},
    },
    inject: {
        /**
         * 注入容器祖先
         */
        container: {default: () => null}
    }
}
