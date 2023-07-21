import indexMain from "@dr/framework/src/components/indexMain";

const menus = [
    {
        id: 'submit',
        label: '查档登记',
        data: {url: '/submit'}
    },
    {
        id: 'list',
        label: '我的查档',
        data: {url: '/list'}
    }
]
export default {
    setup() {
        const menuLoader = () => {
            return {data: {data: menus}}
        }
        return () => {
            return (<indexMain menuLoader={menuLoader}/>)
        }
    }
}