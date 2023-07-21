export default {
    /**
     * 注入容器组件
     */
    inject: {
        searchContainer: {default: undefined}
    },
    mounted() {
        //启动时注册
        if (this.searchContainer && this.getQuery) {
            this.searchContainer.$emit('addQuery', this)
        }
    },
    beforeDestroy() {
        //销毁时注销
        if (this.searchContainer && this.getQuery) {
            this.searchContainer.$emit('removeQuery', this)
        }
    }
}