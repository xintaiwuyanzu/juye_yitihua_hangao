import utils from '@dr/auto/lib/utils'
import {registerField} from "@dr/framework/src/components/formRender";
import {file} from "../lib/fileUploader";

/**
 * vue-uploader 懒加载
 * @param vue
 */
export default (vue) => {
    vue.component('uploader', utils.makeSync(import(/* webpackChunkName: "up" */'vue-simple-uploader/src/components/uploader')))
    vue.component('uploader-btn', utils.makeSync(import(/* webpackChunkName: "up-drop" */'vue-simple-uploader/src/components/btn')))
    vue.component('uploader-drop', utils.makeSync(import(/* webpackChunkName: "up-drop" */'vue-simple-uploader/src/components/drop')))
    vue.component('uploader-unsupport', utils.makeSync(import(/* webpackChunkName: "up-unsupport" */'vue-simple-uploader/src/components/unsupport')))
    vue.component('uploader-list', utils.makeSync(import(/* webpackChunkName: "up-list" */'vue-simple-uploader/src/components/list')))
    vue.component('uploader-files', utils.makeSync(import(/* webpackChunkName: "up-files" */'vue-simple-uploader/src/components/files')))
    vue.component('uploader-file', utils.makeSync(import(/* webpackChunkName: "up-file" */'vue-simple-uploader/src/components/file')))
    //注册自定义文件上传组件
    registerField('file', file)
}
