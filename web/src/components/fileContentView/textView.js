import abstractViewer from "./abstractViewer";
import NProgress from "nprogress";

export default {
    name: "textView",
    extends: abstractViewer,
    data() {
        return {data: ''}
    },
    methods: {
        async setUrl() {
            if (this.url) {
                const request = this.requestMethod === 'post' ? this.$post : this.$get
                const url = this.replaceUrl()
                //异步请求数据
                const {data} = await request(url, this.requestParams, {
                    responseType: 'blob',
                    transformResponse(data) {
                        NProgress.done()
                        return {data, success: true};
                    }
                })
                if (data.success) {
                    //转换文件流为文本
                    this.data = await data.data.text()
                } else {
                    this.data = ''
                }
            }
        }
    },
    render() {
        return (<ace value={this.data}/>)
    }
}