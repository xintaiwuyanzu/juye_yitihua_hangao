/**
 * 这里面是导入导出模块相关功能，
 * 主要是注入到和核心包相关代码
 */
import impFileList from "./header/impFileList";
import expFilelist from "./header/expFilelist";
import impCatalogList from "./header/impCatalogList";

import {register} from "@dr/framework/lib/module";
import {DEFAULT_ARCHIVE_HEADER_PATH} from '@archive/core/src/lib/defaultLib'
import {MANAGE_ARCHIVE_HEADER_PATH} from '@archive/core/src/lib/manageLib'
import {RECEIVE_ARCHIVE_HEADER_PATH} from '@archive/core/src/lib/receiveLib'
import {SAVE_ARCHIVE_HEADER_PATH} from '@archive/core/src/lib/saveLib'


register(DEFAULT_ARCHIVE_HEADER_PATH, 'impFilelist', impFileList)

/**
 * 注册管理库按钮
 */
//目录导入按钮
//register(MANAGE_ARCHIVE_HEADER_PATH, 'impFilelist', impFileList)
//生成案卷目录按钮
register(MANAGE_ARCHIVE_HEADER_PATH, 'expFilelist', expFilelist)


/**
 * 注册接收库按钮
 */
//目录导入按钮
//register(RECEIVE_ARCHIVE_HEADER_PATH, 'impCatalogList', impCatalogList)

/**
 * 注册长期保存库按钮
 */
//生成案卷目录按钮
register(SAVE_ARCHIVE_HEADER_PATH, 'expFilelist', expFilelist)
