import {register} from "@dr/framework/lib/module";
import {META_DATA_DETAIL_VIEW_MODULE_NAME} from '@archive/core/src/components/metadataFileView'

export default () => {
    register(META_DATA_DETAIL_VIEW_MODULE_NAME, 'keyWord', import('./keyWord'), {label: '关键词'})
}
