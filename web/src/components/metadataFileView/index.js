import metadataFileView from './metadataFileView'
import {register} from "@dr/framework/lib/module";


//详情模块名称
export const META_DATA_DETAIL_VIEW_MODULE_NAME = "metaDataFileDetailView";
register(META_DATA_DETAIL_VIEW_MODULE_NAME, 'metadataDetail', import('./detail/metadataDetail'), {label: '基本信息元数据'})


//头部模块名称
export const META_DATA_HEADER_VIEW_MODULE_NAME = "metaDataFileHeaderView";
register(META_DATA_HEADER_VIEW_MODULE_NAME, 'metaRecord', import('./header/metaRecord'))


export default metadataFileView
//管理过程元数据按钮
export {default as metaRecord} from './header/metaRecord'