import {Empty} from 'element-ui'
import {useModules} from "@dr/framework/lib/module";
import {FILE_VIEW_MODULE_NAME} from "./index";
import {viewerProps} from "./abstractViewer";

/**
 * 根据文件后缀获取指定的渲染组件
 * @param fileSuffix
 */
function getViewComponent(fileSuffix) {
    const modules = useModules(FILE_VIEW_MODULE_NAME)
    if (fileSuffix) {
        fileSuffix = fileSuffix.toLowerCase()
        for (let i = 0; i < modules.length; i++) {
            const mod = modules[i]
            let fileType = Array.isArray(mod.fileType) ? mod.fileType : [mod.fileType]
            fileType = fileType.map(f => f.toLowerCase())
            if (fileType.includes(fileSuffix)) {
                return mod.component
            }
        }
    }
    return null
}

export default {
    name: 'fileContentView',
    extends: viewerProps,
    props: {
        /**
         * 文件类型，也可以简单理解成文件后缀
         */
        fileType: {type: String},
        /**
         * 文件名称
         */
        fileName: String
    },
    render() {
        let children
        if (this.fileType) {
            //获取文件后缀
            const Cmp = getViewComponent(this.fileType, this.currentNode)
            if (!Cmp) {
                const description = `文件【${this.fileName}】暂不支持预览`
                children = (<Empty description={description}/>)
            } else {
                children = <Cmp url={this.url} requestMethod={this.requestMethod} requestParams={this.requestParams}/>
            }
        } else {
            children = (<Empty description='未选中文件！'/>)
        }
        return children
    }
}