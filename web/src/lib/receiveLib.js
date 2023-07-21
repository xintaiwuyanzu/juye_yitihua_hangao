import edit from "./components/column/edit";
import metadataFileView from "./components/column/metadataFileView";
//下面是头部的按钮
import add from "./components/header/add";
//import freshen from "./components/header/freshen"
import search from "./components/header/search";
import deleter from "./components/header/deleter";
import changeEditTop from "./components/header/changeEditTop";
import fileListold from "./components/header/yuanwenOld";
import recycle from "./components/column/recycle";
import returnManage from "./components/header/returnManage";
import moveGO from "./components/header/moveGO";
import repeat from "./components/header/repeat";
import {createLibUtil, registerLib} from "./defaultLib";


/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [//编辑按钮
    {code: 'edit', component: edit, width: 30},
    {code: 'fileListold', component: fileListold, width: 30},
    {code: 'metadataFileView', component: metadataFileView, width: 120},
    {code: 'recycle',component: recycle,width: 35}]
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    //{code: 'freshen', component: freshen}, //添加按钮
    {code: 'add', component: add}, //搜索按钮
    {code: 'search', component: search}, //调整按钮（福州项目）
    {code: 'moveGO', component: moveGO}, //移动功能
    {code: 'repeat', component: repeat}, //查重功能
    {code: 'changeEditTop', component: changeEditTop}, //福州项目档案室系统需要添加，在线归档数据退回
    {code: 'deleter', component: deleter},
    {code: 'returnManage',component: returnManage}]

export const RECEIVE_ARCHIVE_HEADER_PATH = 'archive/lib/receive/headerParts'
export const RECEIVE_ARCHIVE_COLUMN_PATH = 'archive/lib/receive/columnParts'

registerLib(headerParts, RECEIVE_ARCHIVE_HEADER_PATH)
registerLib(columnParts, RECEIVE_ARCHIVE_COLUMN_PATH)

export const createLib = () => createLibUtil('RECEIVE', RECEIVE_ARCHIVE_HEADER_PATH, RECEIVE_ARCHIVE_COLUMN_PATH)
