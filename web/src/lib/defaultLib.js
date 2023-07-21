import {register, useModules} from "@dr/framework/lib/module";
import {columnParts, headerParts} from "./components";
import archiveLib from "./archiveLib";


/**
 * 注册模块工具方法
 * @param parts
 * @param modulePath
 */
export const registerLib = (parts, modulePath) => {
    parts.forEach(h => {
        register(modulePath, {name: h.code, ...h})
    })
}
/**
 * 创建库工具类
 * @param type
 * @param headerPath
 * @param columnPath
 * @returns {{extends: {provide(): {category: *, libIndex: this, fond: *}, data(): {formInfo: {}, hideTree: boolean, childIndexs: {ARC: null, PRO: null, FILE: null}, tab_pan: string, category: {}, loading: boolean, fond: {}}, computed: {archives(): (T[]|[])}, methods: {$init(): void, check(*): Promise<void>, goSecondTab(), addChild({type: *, child: *}): void}, mounted(): void, props: {autoCloseMenu: {default: boolean}, query: {type: Object | ObjectConstructor}}}, render(*): *}}
 */
export const createLibUtil = (type, headerPath, columnPath) => {
    return archiveLib({type, headerParts: useModules(headerPath), columnParts: useModules(columnPath)})
}
/**
 * 默认档案库所有组件key
 * @type {string}
 */
export const DEFAULT_ARCHIVE_HEADER_PATH = 'archive/lib/default/headerParts'
/**
 * 档案相关的列组件
 * @type {string}
 */
export const DEFAULT_ARCHIVE_COLUMN_PATH = 'archive/lib/default/columnParts'


//注册所有的头部组件
registerLib(headerParts, DEFAULT_ARCHIVE_HEADER_PATH)
//注册所有的列组件
registerLib(columnParts, DEFAULT_ARCHIVE_COLUMN_PATH)


