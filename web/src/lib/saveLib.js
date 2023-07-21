import repeat from "./components/header/repeat";
import search from "./components/header/search";
import returnManage from "./components/header/returnManage";
import expJuanNeiMuLu from "./components/header/expJuanNeiMuLu";

import yuanwen from "./components/column/yuanwen";
import favorites from "./components/column/favorites"
import material from "./components/column/material"
import {createLibUtil, registerLib} from "./defaultLib";


/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [
    //原文列表按钮
    {component: yuanwen, width: 50},
    // //加入收藏按钮
    {component: favorites},
    // //加入素材库按钮
    {component: material},
    {
        component: expJuanNeiMuLu,
        width: 60,
        target: 'FILE'
    },

]
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    //搜索按钮
    {component: search},
    // 数据查重按钮
    {component: repeat},
    // //生成案卷目录按钮
    {component: returnManage},
]

export const SAVE_ARCHIVE_HEADER_PATH = 'archive/lib/save/headerParts'
export const SAVE_ARCHIVE_COLUMN_PATH = 'archive/lib/save/columnParts'

export const createLib = () => createLibUtil('SAVE', SAVE_ARCHIVE_HEADER_PATH, SAVE_ARCHIVE_COLUMN_PATH)

registerLib(headerParts, SAVE_ARCHIVE_HEADER_PATH)
registerLib(columnParts, SAVE_ARCHIVE_COLUMN_PATH)
