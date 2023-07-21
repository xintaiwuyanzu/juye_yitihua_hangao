import vue from 'vue'
import {plugins} from '@dr/auto'
import store from "@dr/auto/lib/store";
import router from "./router";
import app from '@dr/framework/src/components/app'
import {useUserContext} from "./useUser";
import {registerField} from "@dr/framework/src/components/formRender";
import {file} from "./fileUploader";
//注册自定义文件上传组件
registerField('file', file)
const opt = {vue}
//构造store
const storeInstance = store.store(opt)
const makePromise = plugin => Promise.resolve(plugin.value.default(vue, router, storeInstance, opt))
//回调所有的plugin
Promise.all(plugins.map(makePromise))
    .then(() => {
        new vue({
                router,
                store: storeInstance,
                setup() {
                    useUserContext()
                    return () => (<app/>)
                }
            }
        ).$mount(opt.el ? opt.el : '#app')
    })