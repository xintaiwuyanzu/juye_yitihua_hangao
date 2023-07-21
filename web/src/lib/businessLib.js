import search from "./components/header/search";
import freshen from "./components/header/freshen";
import {createLibUtil, registerLib} from "./defaultLib";

/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = []
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    {component: freshen},
    //搜索按钮
    {component: search}
]


export const BUSINESS_ARCHIVE_HEADER_PATH = 'archive/lib/business/headerParts'
export const BUSINESS_ARCHIVE_COLUMN_PATH = 'archive/lib/business/columnParts'

export const createLib = () => createLibUtil('BUSINESS', BUSINESS_ARCHIVE_HEADER_PATH, BUSINESS_ARCHIVE_COLUMN_PATH)

registerLib(headerParts, BUSINESS_ARCHIVE_HEADER_PATH)
registerLib(columnParts, BUSINESS_ARCHIVE_COLUMN_PATH)