import {register} from "@dr/framework/lib/module";
import {META_DATA_DETAIL_VIEW_MODULE_NAME} from '@archive/core/src/components/metadataFileView'

export default () => {
    register(META_DATA_DETAIL_VIEW_MODULE_NAME, 'businessGuidance', import('./businessGuidance'),
        {
            defaultShow: false,
            label: '问题',
            // role: 'businessGuidance'
        })
}
