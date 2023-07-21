import {inject, onMounted, provide, reactive, watch} from 'vue'
import {useRouter} from '@dr/auto/lib'
import {Message, Notification} from 'element-ui'
import {http} from "@dr/framework/src/plugins/http";
import {useUser} from "@dr/framework/src/hooks/userUser";

export const archiveCarKey = '$Archive_Car'
/**
 * 档案车相关hook，单独把相关代码提取出来为了全局使用
 */
/**
 * 档案车上下文
 */
export const useArchiveCarContext = () => {

    const archiveCarData = reactive({
        show: false,
        //档案车切换弹窗是否显示
        carDrawerShow: 'none',
        //详情表单
        detail: {
            //表单数据Id
            formDataId: '',
            //表单定义Id
            formDefinitionId: '',
            //数据标记
            archiveTag: '',
            //描述
            message: ''
        },
        //当前选中档案车
        archiveCar: {
            //档案车Id
            id: '',
            //档案车名称
            batchName: '',
            batchType: '',
            //档案车数量
            detailCount: 0,
            //创建人Id
            createPerson: ''
        },
        carStatic: false,
        batchType: {},
        tagSelect: []
    })
    provide(archiveCarKey, archiveCarData)
    //切换档案车
    const changeArchiveCar = async (id) => {
        const typeResult = await http().post('/archiveCarBatch/archiveCarType', {withTag: false})
        if (typeResult.data.success) {
            typeResult.data.data.forEach(d => archiveCarData.batchType[d.type] = d)
        }
        const {data} = await http().post('/archiveCarBatch/detail', {id})
        if (data.success && data.data) {
            localStorage.setItem(archiveCarKey, JSON.stringify({id: id, createPerson: data.data.createPerson}))
            archiveCarData.archiveCar = data.data
            Notification({
                title: '切换档案车成功',
                message: `当前默认档案车为【${archiveCarData.archiveCar.batchName}】`,
                type: 'success',
                position: 'bottom-right'
            })
        } else {
            archiveCarData.archiveCar = {
                //档案车Id
                id: '',
                //档案车名称
                batchName: '',
                //档案车数量
                detailCount: 0,
                batchType: ''
            }
        }
    }
    watch(() => archiveCarData.show, async (n) => {
        if (n) {
            const archiveId = localStorage.getItem(archiveCarKey)
            if (archiveId) {
                const archieCar = JSON.parse(archiveId)
                archiveCarData.archiveCar.id = archieCar.id
            }
        }
    })

    watch(() => archiveCarData.archiveCar.batchType, async (n) => {
        archiveCarData.detail.archiveTag = ''
        if (n) {
            const {data} = await http().post('/archiveCarBatch/archiveCarTag', {carType: n, withDynamic: true})
            if (data.success) {
                archiveCarData.tagSelect = data.data
                const batch = archiveCarData.batchType[n]
                if (batch) {
                    archiveCarData.carStatic = batch.tagStatic
                }
            }
        } else {
            archiveCarData.tagSelect = []
            archiveCarData.carStatic = false
        }
    })

    const {router} = useRouter()
    router.beforeEach((to, from, next) => {
        archiveCarData.show = false
        next()
    })

    watch(() => archiveCarData.archiveCar.id, async (n) => {
        if (n) {
            await changeArchiveCar(n)
        }
    })
    return {archiveCarData}
}
/**
 * 导出全局档案车属性
 * @returns {{addToCar: ((function(*=): Promise<void>)|*), archiveCarData: unknown}}
 */
export const useArchiveCar = () => {
    const archiveCarData = inject(archiveCarKey)
    const user = useUser()
    onMounted(() => {
        if (user.user.id) {
            const archiveId = localStorage.getItem(archiveCarKey)
            try {
                const archieCar = JSON.parse(archiveId)
                //如果登录用户切换了，删除上一个用户的缓存信息
                if (archieCar.createPerson !== user.user.id) {
                    localStorage.removeItem(archiveCarKey)
                }
            } catch (e) {
                localStorage.removeItem(archiveCarKey)
            }
        }
    })
    /**
     * 添加一份档案到档案车
     */
    const addToCar = async (selectType = false) => {
        if (!archiveCarData.archiveCar.id) {
            Message.error("请选择或添加档案车")
            archiveCarData.carDrawerShow = 'dropdown'
            return
        }
        if (!selectType) {
            //TODO 默认可以不带标记
            // if (archiveCarData.detail.archiveTag && !selectType) {
            const {data} = await http().post('/archiveCarDetail/insert', {
                batchId: archiveCarData.archiveCar.id,
                ...archiveCarData.detail
            })
            if (data.success) {
                archiveCarData.archiveCar.detailCount = data.data
                const tag = archiveCarData.tagSelect.find(t => t.code === archiveCarData.detail.archiveTag)
                const tagName = tag ? tag.name : archiveCarData.detail.archiveTag
                Notification.success(`添加档案车成功，档案标记【${tagName}】已经设为默认标记`)
                archiveCarData.carDrawerShow = 'none'
            } else {
                Message.error(data.message)
            }
        } else {
            archiveCarData.carDrawerShow = 'addCar'
        }
    }
    return {
        archiveCarData,
        addToCar
    }
}
