import dayjs from 'dayjs'
import '../lib'
import './archive.scss'

const fmtDate = (v, fmt) => {
    if (!v) {
        return v
    }
    try {
        return dayjs(v).format(fmt)
    } catch (e) {
        return v
    }
}
export default (vue, router, store) => {
    //修改element全局控件大小
    /*vue.prototype.$ELEMENT = {
        size: 'medium',
        zIndex: 2000
    }*/
    //修改http请求加载超时时间
    vue.prototype.$http.defaults.timeout = 10000

    vue.prototype.$moment = dayjs
    vue.filter('null', (v) => (v && v !== 'null') ? v : '')
    vue.filter('date', (v, fmt) => fmtDate(v, fmt ? fmt : 'YYYY-MM-DD'))
    vue.filter('datetime', (v, fmt) => fmtDate(v, fmt ? fmt : 'YYYY-MM-DD HH:mm:ss'))
    vue.directive('focus', {
        inserted: function (el) {
            el.childNodes.forEach(e => {
                if (e.tagName === 'INPUT' || e.tagName === 'input') {
                    e.focus()
                }
            })
        }
    })
    const archiveTypes = [
        {label: '项目', id: '2'},
        {label: '案卷', id: '1'},
        {label: '文件', id: '0'},
        {label: '卷内文件', id: '4'},
        // {label: '件盒', id: '3'}
    ]
    /*const impSchema = [
        {label: '导入', id: '1'},
        {label: '导出', id: '2'}
    ]*/
    const fileType = [
        {label: 'excel', id: '1'},
        {label: 'xml', id: '2'}
    ]
    const mineType = [
        {label: '目录原文', id: 'application/zip'},
        {label: 'excel', id: 'application/vnd.ms-excel'},
        {label: 'xml', id: 'application/xml'},
        {label: 'dbf', id: 'application/x-dbf'},
        //{label: 'access', id: 'application/x-msaccess'},
        //{label: 'json', id: 'application/json'},
        //{label: 'csv', id: 'text/csv'},
    ]
    const dicts = {archiveTypes}
    const appraisalType = [
        {label: '密级鉴定', id: 'securityLevel'},
        {label: '保管期限鉴定', id: 'saveTerm'},
        {label: '开放范围鉴定', id: 'openScope'},
        {label: '价值鉴定', id: 'worth'},
        {label: '销毁鉴定', id: 'destruction'}
    ]
    const saveTerm = [
        {label: '永久', id: 'Y'},
        {label: '定期30年', id: 'D30'},
        {label: '定期10年', id: 'D10'},
        {label: '永久（老）', id: '001'},
        {label: '长期（老）', id: '002'},
        {label: '短期（老）', id: '003'}
    ]
    const securityLevel = [
        {label: '国内', id: '1'},
        {label: '内部', id: '2'},
        {label: '秘密', id: '3'},
        {label: '机密', id: '4'},
        {label: '绝密', id: '5'}
    ]
    const openScope = [
        {label: '开放', id: '1'},
        {label: '不开放', id: '2'}
    ]
    //表单显示方案类型
    const formDisplayType = [
        {label: '列表页面', id: 'list'},
        {label: '添加页面', id: 'form'},
        {label: '查询表单', id: 'search'},
        {label: '详情表单', id: 'detail'},
        {label: '所有详情表单', id: 'detailAll'}
    ]
    const testType = [
        {label: '完整性', id: 'INTEGRITY'},
        {label: '真实性', id: 'AUTHENTICITY'},
        {label: '安全性', id: 'SECURITY'},
        {label: '可用性', id: 'USABILITY'}
    ]
    const checkLink = [
        {label: '归档环节', id: 'ARCHIVING'},
        {label: '移交环节', id: 'TRANSFORM'},
        {label: '长期保存环节', id: 'FILLING'}
    ]
    /**
     * 虚拟库房：库房类型
     */
    const locType = [
        {label: '文字档案', id: '1'},
        {label: '图形档案', id: '2'},
        {label: '声像档案', id: '3'},
    ]
    const articleStatus = [
        {label: '下线', id: '0'},
        {label: '上线', id: '1'},
    ]
    /**
     * 需要跟后台ArchiveMetadataRecordService对应
     * @type {[{label: string, id: string},{label: string, id: string}]}
     */
    const metadataChangeType = [
        {label: '编辑', id: 'EDIT'},
        {label: '入库', id: 'INGL'},
        {label: '出库', id: 'OUTGL'},
        {label: '添加', id: 'ADD'},
        {label: '接收', id: 'RECEIVE'},
        {label: '调整', id: 'TIAOZHENG'},
        {label: '删除', id: 'RECYCLE'},
        {label: '恢复', id: 'RECOVERY'},
        {label: '鉴定', id: 'JIANDING'},
        {label: '到期鉴定', id: 'JIANDING_DAOQI'},
        {label: '开放鉴定', id: 'JIANDING_KAIFANG'},
        {label: '在线移交', id: 'ONLINE_YIJIAO'},
    ]
    const checkName = [
        {label: '著录项目数据长度检测', id: 'AUTHENTICITY_LENGTH'},
        {label: '著录项目数据类别、字段格式检测', id: 'AUTHENTICITY_TYPE'},
        {label: '设定值域的著录项目值域符合度检测', id: 'AUTHENTICITY_RANGE'},
        {label: '著录项目数椐合理性检测，著录项目数据范围检测', id: 'AUTHENTICITY_RATIONALITY'},
        {label: '著录项目数据包含特殊字符检测', id: 'AUTHENTICITY_SPECIALCHARACTERS'},
        {label: '目录是否关联电子文件内容', id: 'AUTHENTICITY_ISRELATIONFILE'},
        {label: '电子文件内容数据大小的一致性检测', id: 'AUTHENTICITY_FILESIZE'},
        {label: '电子文件内容数据电子属性的一致性检测', id: 'AUTHENTICITY_FILEPROPERTY'},
        {label: '电子文件生效信息的有效性检测', id: 'AUTHENTICITY_FILEEFFECTIVEDATE'},
        {label: '电子文件目录必填著录项目检测', id: 'INTEGRITY_REQUIRED'},
        {label: '归档信息包元数据完整性检测', id: 'INTEGRITY_METADATA'},
        {label: '案卷级归档数椐包中卷内文件数量和实际条目数量的相符性检测', id: 'INTEGRITY_INNERFILENUM'},
        {label: '文件级归档信息包中文件数量和电子文件数的相符性检测', id: 'INTEGRITY_FILENUM'},
        {label: '归档信息包中目录数据的可读性检测', id: 'USABILITY_READ'},
        {label: '电子文件来源真实性', id: '1-1-2'},
        {label: '电子档案元数据准确性', id: '1-2-1'},
        {label: '电子文件元数据准确性', id: '1-2-2'},
        {label: '电子文件元数据准确性', id: '1-2-3'},
        {label: '电子文件内容真实性', id: '1-3-1'},
        {label: '电子文件内容真实性', id: '1-3-2'},
        {label: '元数据与内容关联一致性', id: '1-4-1'},
        {label: '归档信息包真实性', id: '1-5-1'},
        {label: '归档信息包真实性', id: '1-5-2'},
        {label: '元数据项数据重复性检测', id: 'YJ-1-8'},
        {label: '档号连续性元数据项检测', id: 'GD-2-6'},
        {label: '元数据项数据包含特殊字符检测', id: 'YJ-1-6'},
        {label: '电子文件数据总量', id: '2-1-1'},
        {label: '电子文件数据总量', id: '2-1-2'},
        {label: '电子文件元数据完整性', id: '2-2-1'},
        {label: '电子文件元数据完整性', id: '2-2-2'},
        {label: '电子文件元数据完整性', id: '2-2-3'},
        {label: '电子文件内容完整性', id: '2-3-1'},
        {label: '归档信息包完整性', id: '2-4-1'},
        {label: '归档信息包完整性', id: '2-4-2'},
        {label: '归档信息包完整性', id: '2-4-3'},
        {label: '归档信息包病毒', id: '4-1-1'},
        {label: '归档载体安全性', id: '4-2-1'},
        {label: '归档过程安全性', id: '4-3-1'},
        {label: '电子文件元数据可用性', id: '3-1-1'},
        {label: '电子文件内容可用性', id: '3-2-1'},
        {label: '电子文件内容可用性', id: '3-2-2'},
        {label: '电子文件软硬件环境', id: '3-3-1'},
        {label: '归档信息包可用性', id: '3-4-1'},
        {label: '归档信息包可用性', id: '3-4-2'}
    ]

    const authenticityCheckName = [
        {label: '电子文件来源真实性', id: '1-1-2'},
        {label: '电子档案元数据准确性', id: '1-2-1'},
        {label: '电子文件元数据准确性', id: '1-2-2'},
        {label: '电子文件元数据准确性', id: '1-2-3'},
        {label: '电子文件内容真实性', id: '1-3-1'},
        {label: '电子文件内容真实性', id: '1-3-2'},
        {label: '元数据与内容关联一致性', id: '1-4-1'},
        {label: '归档信息包真实性', id: '1-5-1'},
        {label: '归档信息包真实性', id: '1-5-2'},
        {label: '元数据项数据重复性检测', id: 'YJ-1-8'},
        {label: '档号连续性元数据项检测', id: 'GD-2-6'},
        {label: '元数据项数据包含特殊字符检测', id: 'YJ-1-6'}
    ]

    const integrityCheckName = [
        {label: '电子文件数据总量', id: '2-1-1'},
        {label: '电子文件数据总量', id: '2-1-2'},
        {label: '电子文件元数据完整性', id: '2-2-1'},
        {label: '电子文件元数据完整性', id: '2-2-2'},
        {label: '电子文件元数据完整性', id: '2-2-3'},
        {label: '电子文件内容完整性', id: '2-3-1'},
        {label: '归档信息包完整性', id: '2-4-1'},
        {label: '归档信息包完整性', id: '2-4-2'},
        {label: '归档信息包完整性', id: '2-4-3'}
    ]

    const securityCheckName = [
        {label: '归档信息包病毒', id: '4-1-1'},
        {label: '归档载体安全性', id: '4-2-1'},
        {label: '归档过程安全性', id: '4-3-1'}
    ]

    const usabilityCheckName = [
        {label: '电子文件元数据可用性', id: '3-1-1'},
        {label: '电子文件内容可用性', id: '3-2-1'},
        {label: '电子文件内容可用性', id: '3-2-2'},
        {label: '电子文件软硬件环境', id: '3-3-1'},
        {label: '归档信息包可用性', id: '3-4-1'},
        {label: '归档信息包可用性', id: '3-4-2'}
    ]
    // /**
    //  * 职务
    //  */
    // const dutys = [
    //     {label: '局长', id: '局长'},
    //     {label: '处长', id: '处长'},
    //     {label: '科长', id: '科长'},
    //     {label: '科员', id: '科员'},
    // ]
    // // dicts.dutys = dutys
    // dicts['user.duty'] = dutys
    //状态 0：未提交  1：受理中  2：审核通过  3：审核不通过
    dicts['archive.applyType'] = [
        {label: '未提交', id: '0'},
        {label: '受理中', id: '1'},
        {label: '审核通过', id: '2'},
        {label: '审核不通过', id: '3'},
    ]
    dicts['cms.articleStatus'] = articleStatus
    /*dicts['impexp.schemeType'] = impSchema*/
    dicts['impexp.fileType'] = fileType
    dicts['impexp.mineType'] = mineType
    dicts['archive.appraisalType'] = appraisalType
    dicts['archive.saveTerm'] = saveTerm
    dicts['archive.securityLevel'] = securityLevel
    dicts['archive.openScope'] = openScope
    dicts['archive.testType'] = testType
    dicts['checkLink'] = checkLink
    dicts['loc.type'] = locType
    dicts['archive.metadataChangeType'] = metadataChangeType
    dicts['archive.checkName'] = checkName
    dicts['archive.authenticity.checkName'] = authenticityCheckName
    dicts['archive.integrity.checkName'] = integrityCheckName
    dicts['archive.security.checkName'] = securityCheckName
    dicts['archive.usability.checkName'] = usabilityCheckName
    dicts.formDisplayType = formDisplayType
    dicts.fieldDisplayType = [
        {label: '文本', id: 'input'},
        {label: '时间', id: 'date'},
        {label: '数字', id: 'number'}
    ]
    dicts.impSourceTypes = [
        {label: '立档单位', id: '1'},
        {label: '打包接收', id: '2'},
        {label: '历史数据', id: '3'},
        {label: '数字化成果', id: '4'},
        {label: '征集档案', id: '5'},
        {label: '寄存档案', id: '6'}
    ]
    dicts.successStatus = [
        {label: '成功', id: '1'},
        {label: '失败', id: '0'}
    ]
    store.commit('dictLoaded', dicts)
}