import qs from 'qs'

export const viewerProps = {
    props: {
        /**
         * 数据后台请求路径
         */
        url: {type: String},
        /**
         * 请求方法
         */
        requestMethod: {type: String, default: 'post'},
        /**
         * 请求参数
         */
        requestParams: {type: Object},

        printStatusProps:{type:Boolean},

        downloadStatusProps:{type:Boolean}
    }
}


//抽象视图组件
const abstractViewer = {
    extends: viewerProps,
    //TODO 将来看情况处理编辑场景
    methods: {
        /**
         * 初始化的时候根据参数设置url路径
         * @return {Promise<void>}
         */
        async $init() {
            if (this.setUrl) {
                await this.setUrl()
            }
        },
        buildUrl() {
            if (this.requestParams) {
                return `${this.url}?${qs.stringify(this.requestParams)}`
            } else {
                return this.url
            }
        },
        replaceUrl() {
            return this.url.startsWith('/api') ? this.url.replace('/api', '') : this.url
        }
    },
    computed: {
        changeParams() {
            const {url, requestMethod, requestParams} = this
            return {url, requestMethod, requestParams}
        }
    },
    watch: {
        /**
         * 监听参数变化
         */
        changeParams() {
            if (this.setUrl) {
                this.setUrl()
            }
        }
    }
}

export const abstractIframeView = {
    extends: abstractViewer,
    beforeDestroy() {
        //销毁iframe
        const iframe = this.$refs.iframe
        if (iframe) {
            const win = iframe.contentWindow
            try {
                win.document.write('')
                win.document.clear()
            } catch (e) {
                console.warn(`销毁iframe失败${e}`)
            }
            iframe.src = 'about:blank'
        }
    }
}

export default abstractViewer