import {Button, Upload} from 'element-ui'
import util from '@dr/framework/src/components/login/util'
import {onMounted, reactive, watch} from 'vue'
import {computeArgs} from "@dr/framework/src/components/formRender/fieldRender";
import {v4} from 'uuid'
import {http} from "@dr/framework/src/plugins/http";

/**
 * 手动封装上传组件，对应后台commonFile模块
 */
const fileUploader = {
    inheritAttrs: false,
    name: 'fileUploader',
    props: {
        //业务类型
        refType: {default: 'default'},
        //分组类型
        groupCode: {default: 'default'},
        //选择文件按钮
        btnText: {default: '上传附件'},
        /**
         * 提示框
         */
        tip: {default: '上传文件最大100MB'},
        /**
         * 业务外键
         */
        value: {}
    },
    setup(props, context) {
        const uuid = v4()
        const fileList = reactive({files: []})
        const loadFiles = async () => {
            if (props.value) {
                const {data} = await http().post('/files/list', {
                    refId: props.value,
                    refType: props.refType,
                    groupCode: props.groupCode
                })
                if (data.success) {
                    fileList.files = data.data.map(d => ({uid: d.id, ...d}))
                } else {
                    fileList.files = []
                }
            }
        }

        onMounted(async () => {
            if (props.value) {
                await loadFiles()
            } else {
                context.emit('input', uuid)
            }
        })
        watch(() => props.value, async (n, o) => {
            if (n != o && n !== uuid) {
                await loadFiles()
            }
        })

        //上传拦截
        const beforeUpload = (file) => file.name.replace(/.+\./, "");
        //删除拦截
        const beforeRemove = (file) => http().post(`/files/delete/${file.uid}`)
        //成功回调
        const onSuccess = (res, file) => {
            if (res.success) {
                file.uid = res.data.id
            }
        }

        return () => {
            const uploadArgs = {
                props: {
                    action: 'api/files/upload',
                    beforeUpload,
                    beforeRemove,
                    onSuccess,
                    //后台接口额外参数
                    data: {
                        refId: props.value,
                        refType: props.refType,
                        groupCode: props.groupCode
                    },
                    //后台接口header参数
                    headers: {
                        $token: util.getToken()
                    },
                    //文件列表
                    fileList: fileList.files
                }
            }
            const tips = props.tip ? <div slot='tip' class="el-upload__tip">{props.tip}</div> : ''
            return (
                <Upload  {...uploadArgs}  >
                    <Button type="primary">{props.btnText}</Button>
                    {tips}
                </Upload>
            )
        }
    }
}
export default fileUploader
export const file = (props, context) => {
    const args = computeArgs(props, context, false)
    return (<fileUploader {...args} />)
}
