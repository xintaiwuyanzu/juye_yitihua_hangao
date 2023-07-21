import yuanwen from "./components/column/yuanwen";
//下面是头部的按钮
import search from "./components/header/search";
import {createLibUtil, registerLib} from "./defaultLib";

/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [
    //原文列表按钮
    {component: yuanwen, width: 50}
]
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    //搜索按钮
    {component: search},
]

export const PRE_ARCHIVE_HEADER_PATH = 'archive/lib/pre/headerParts'
export const PRE_ARCHIVE_COLUMN_PATH = 'archive/lib/pre/columnParts'

export const createLib = () => createLibUtil('PRE', PRE_ARCHIVE_HEADER_PATH, PRE_ARCHIVE_COLUMN_PATH)

registerLib(headerParts, PRE_ARCHIVE_HEADER_PATH)
registerLib(columnParts, PRE_ARCHIVE_COLUMN_PATH)