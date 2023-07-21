import recovery from "./components/column/recovery";
//下面是头部的按钮
import search from "./components/header/search";
import deleter from "./components/header/deleter";
import {createLibUtil, registerLib} from "./defaultLib";

/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [
    //恢复按钮
    {component: recovery}
]
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    //搜索按钮
    {component: search},
    //删除按钮
    {component: deleter},
]


export const TRASH_ARCHIVE_HEADER_PATH = 'archive/lib/trash/headerParts'
export const TRASH_ARCHIVE_COLUMN_PATH = 'archive/lib/trash/columnParts'

export const createLib = () => createLibUtil('TRASH', TRASH_ARCHIVE_HEADER_PATH, TRASH_ARCHIVE_COLUMN_PATH)

registerLib(headerParts, TRASH_ARCHIVE_HEADER_PATH)
registerLib(columnParts, TRASH_ARCHIVE_COLUMN_PATH)
