// import fileList from "./components/column/yuanwenForAdjust";
// import metaData from "./components/column/metaData"
import changeEdit from "./components/changeEdit";
//下面是头部的按钮
import search from "./components/header/search";
//import group from "./components/header/group";
//import disassemble from "./components/header/disassemble";
// import freshen from "./components/header/freshen";
//import insert from "./components/header/insert";
//import removeFile from "./components/header/removeFile";
import filereturnRec from "./components/header/filereturnRec";
//import generateArchiveCode from "./components/header/generateArchiveCode";
// import edit from "./components/column/edit";
import {createLibUtil, registerLib} from "./defaultLib";
import metadataFileView from "./components/column/metadataFileView";
// import fileListold from "./components/header/yuanwenOld";
import fileList from "./components/column/yuanwenForAdjust";
//import add from "./components/header/add";
// import deleter from "./components/header/deleter";
//import handover from "./components/header/handover";

/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [
    // {component: fileList},
    // {code: 'edit', component: edit, width: 30},
    {code: 'fileList', component: fileList, width: 30},//TODO 先暂时开启原文手动管理功能
    {code: 'metadataFileView', component: metadataFileView, width: 60},
    // 过程元数据按钮
    // {component: metaData, width: 90},
    //档案调整
    {code: 'changeEdit', component: changeEdit, width: 30},]
/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    // {code: 'freshen', component: freshen}, //刷新按钮
    // {code: 'handover',component: handover},
    //{code: 'add', component: add}, //搜索按钮
    {code: 'search', component: search},
    //{component: freshen},
    {component: filereturnRec},
]

export const MANAGE_ARCHIVE_HEADER_PATH = 'archive/lib/manager/headerParts'
export const MANAGE_ARCHIVE_COLUMN_PATH = 'archive/lib/manage/columnParts'

export const createLib = () => createLibUtil('MANAGE', MANAGE_ARCHIVE_HEADER_PATH, MANAGE_ARCHIVE_COLUMN_PATH)


registerLib(headerParts, MANAGE_ARCHIVE_HEADER_PATH)
registerLib(columnParts, MANAGE_ARCHIVE_COLUMN_PATH)