import vue from "vue";
import VueRouter from "vue-router";
import login from "./components/login";
import indexMain from "./components/indexMain";
import submit from "./views/submit";
import list from "./views/list";
import detail from "./views/detail";
import detailItem from "./views/detailItem";
import {components} from '@dr/auto'
import utils from '@dr/auto/lib/utils'
//注册所有全局组件
components.forEach(({name, component}) => vue.component(name, utils.makeSync(component)))

vue.use(VueRouter)
const routes = [
    {path: '/', redirect: '/login'},
    {path: '/login', component: login},
    {
        path: '/main', component: indexMain,
        children: [
            {path: '/home', component: submit},
            {path: '/submit', component: submit},
            {path: '/list', component: list},
            {path: '/detail', component: detail},
            {path: '/detailItem', component: detailItem},
        ]
    }
]

export default new VueRouter({base: process.env.BASE_URL, routes})