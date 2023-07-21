import add from './header/add'
import sendCheck from "./header/sendCheck";
import edit from './column/edit'
import deleter from './header/deleter'
import fileList from './column/yuanwen'
import group from './header/group'
import disassemble from './header/disassemble'
import expFileList from '@archive/receive-base/src/lib/header/expFilelist'
import upAndDown from './header/upAndDown'
import box from './header/box'
import fourSexTest from './column/fourSexTest'
import testRecord from './column/testRecord'
import flag from './header/flag'
import recycle from './column/recycle'
import favorites from './column/favorites'
import recovery from './column/recovery'
import material from './column/material'
import changeOfLap from './column/changeOfLap'
import search from "./header/search";
import repeat from './header/repeat'
import receive from '@archive/receive-base/src/lib/header/receive'

/**
 * 渲染在上面的控件
 * @type {*[]}
 */
export const headerParts = [
    //添加按钮
    {code: 'add', component: add, type: ['PRE', 'MANAGE', 'SAVE']},
    //搜索按钮
    {code: 'search', component: search, type: ['PRE', 'MANAGE', 'SAVE', 'RECYCLE']},
    //提报年检
    {code: 'sendCheck', component: sendCheck, type: ['MANAGE']},
    //接收按钮
    //{code: 'receive', component: receive, type: ['PRE']},
    {code: 'receive', component: receive, type: ['PRE']},
    //查重按钮
    {code: 'repeat', component: repeat, type: ['PRE']},
    //组卷按钮
    {code: 'group', component: group, group: 'group', type: ['PRE', 'MANAGE']},
    //拆卷按钮
    {code: 'disassemble', component: disassemble, group: 'group', type: ['PRE', 'MANAGE']},
    //立卷按钮
    {code: 'flag', component: flag, group: 'group', type: ['MANAGE']},
    //生成案卷目录按钮
    {code: 'expFileList', component: expFileList, type: ['PRE']},
    //档案上下架按钮
    {code: 'upAndDown', component: upAndDown, type: ['SAVE', 'MANAGE']},
    //装盒按钮
    {code: 'box', component: box, type: ['SAVE', 'MANAGE']},
    //移交按钮
    // {code: 'send', component: send, type: ['PRE']},
    //删除按钮
    {code: 'deleter', component: deleter, type: ['RECYCLE', 'PRE', 'MANAGE']},
]
/**
 * 渲染在table操作一栏的控件
 * @type {*[]}
 */
export const columnParts = [
    //编辑按钮
    {code: 'edit', component: edit, type: ['PRE', 'MANAGE', 'SAVE']},
    //原文列表按钮
    {code: 'fileList', component: fileList, type: ['PRE', 'MANAGE', 'SAVE']},
    //四性检测按钮
    {code: 'fourSexTest', component: fourSexTest, type: ['PRE']},
    //查看检测结果按钮
    {code: 'testRecord', component: testRecord, type: ['PRE']},
    //删除进回收站按钮
    {code: 'recycle', component: recycle, type: ['PRE', 'MANAGE']},
    //加入收藏按钮
    {code: 'favorites', component: favorites, type: ['MANAGE']},
    //恢复按钮
    {code: 'recovery', component: recovery, type: ['RECYCLE']},
    //加入素材库按钮
    {code: 'material', component: material, type: ['MANAGE']},
    //调卷按钮
    {code: 'changeOfLap', component: changeOfLap, type: ['MANAGE']},
]



