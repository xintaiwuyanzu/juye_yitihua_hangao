import indexMain from "@dr/framework/src/components/indexMain";

const menus = [
    {
        id: 'submit',
        label: '上传',
        data: {url: '/dataClient/inRecord'}
    },
    {
        id: 'list',
        label: '下载',
        data: {url: '/dataClient/outRecord'}
    }
]
export default {
    setup() {
        const sysLoader = () => {
            return {id: 'dataoperationtools', sysName: "数据维护客户端工具"}
        }
        const menuLoader = () => {
            return {data: {data: menus, success: true}}
        }
        const roleLoader = () => {
            return {data: {data: [], success: true}}
        }
        const userLoader = () => {
            return {data: {data: {}, success: true}}
        }
        return () => {
            return (<indexMain menuLoader={menuLoader} roleLoader={roleLoader} userLoader={userLoader}
                               defaultSysLoader={sysLoader}/>)
        }
    },
    mounted() {
        if (this.$route.query.menuPath) {
            const queryTemp = Object.assign({}, this.$route.query)
            this.$nextTick(() => {
                console.log(queryTemp)
                setTimeout(() => {
                    this.$router.replace({name: queryTemp.menuPath, params: {...queryTemp}})
                }, 500)
            })
        }
    }
}