import './metadataFileView.scss'
import splitPane from 'vue-splitpane/src/split-pane'
import fileContent1 from "./fileContent";
import {reactive, watchEffect} from 'vue'
import {http} from "@dr/framework/src/plugins/http";
import {META_DATA_DETAIL_VIEW_MODULE_NAME, META_DATA_HEADER_VIEW_MODULE_NAME} from "./index";
import {filterComponents} from './utils'

/**
 * 档案详情查看页面，因为需要动态处理，所以就直接写成jsx了
 */
export default {
    components: {fileContent1},
    name: 'metadataFileView',
    props: {
        //dialog的标题
        title: {default: '查看档案目录详情'},
        //表单数据
        formData: {type: Object, default: () => ({})},
        //表单定义Id
        formDefinitionId: {type: String},
        //下面是附件相关的参数
        refType: {default: 'default'},
        groupCode: {default: 'default'},
        keyword: {type: String},
        //预览是否带水印  ，true:带水印
        watermark: {default: true},
        watermarkStatus:{type:Boolean,default:true},
        //是否打印  true :不下载
        printStatus:{type:Boolean},
        //是否下载   true ：不打印
        downloadStatus:{type:Boolean},
        //默认拆分比例
        defaultPercent: {type: Number, default: 68},
        //是否显示标题头
        showHeader: {type: Boolean, default: true},
        /**
         * 头部包含的的组件名称
         */
        headerIncludes: {type: [String, Array], default: () => ([])},
        /**
         * 头部排除的组件名称
         */
        headerExcludes: {type: [String, Array], default: () => ([])},
        /**
         * 是否默认显示详情
         */
        defaultShowDetail: {type: Boolean, default: true},
        /**
         * 详情包含的组件名称
         */
        detailIncludes: {type: [String, Array], default: () => ([])},
        /**
         * 详情排除的组件名称
         */
        detailExcludes: {type: [String, Array], default: () => ([])},
        /**
         * 是否默认显示tab
         */
        defaultShowTabs: {type: Boolean, default: true},
        /**
         * 是否默认显示tab下的档案详情
         */
        defaultShowFormData: {type: Boolean, default: true},
    },
    data() {
        return {
            //当前选中的文件
            currentNode: {},
            //false是否正在拖拽
            dragging: false,
            /**
             * 详情显示状态
             */
            detailShow: true,
            /**
             * 详情tab模型
             */
            detailTabModel: 0,
            currentFileId: ''
        }
    },
    computed: {
        /**
         * 所有详情组件
         */
        detailComponents() {
            return filterComponents(META_DATA_DETAIL_VIEW_MODULE_NAME, this.detailIncludes, this.detailExcludes, this)
        }
    },

    /**
     * 向子元素提供自身信息
     * @returns {{container: default}}
     */
    provide() {
        return {
            container: this
        }
    },
    setup(prop) {
        const fileTree = reactive({fileTree: []})
        //监听属性，动态加载数据
        watchEffect(async () => {
            if (prop.formData.id) {
                const {data} = await http().post('fileView/fileTree', {
                    refId: prop.formData.id,
                    refType: prop.refType,
                    groupCode: prop.groupCode
                })
                if (data.success > 0) {
                    fileTree.fileTree = data.data
                    //文件列表名称中文排序
                    fileTree.fileTree.sort(function (a, b) {
                        return a['label'].localeCompare(b['label'])
                    })
                } else {
                    fileTree.fileTree = []
                }
            } else {
                fileTree.fileTree = []
            }
        })
        return {fileTree}
    },
    methods: {
        $init() {
            this.detailShow = this.defaultShowDetail
        },
        getFileId(v){
            this.currentFileId=v
            // this.$parent.getCurr(v)
            this.$emit('getCurr')
        },
        showFile(v) {
            console.log(this.printStatus)
            this.currentNode = v
        },
        //下面两个方法是为了监听拖拽事件，拖拽时iframe添加遮罩，防止拖拽事件丢失
        mousedown(e) {
            if (e.target.className) {
                if (e.target.className.indexOf('splitter-pane-resizer') >= 0) {
                    this.dragging = true
                }
            }
        },
        mouseup() {
            this.dragging = false
        }
    },
    render() {
        //是否有文件列表
        const hasFile = this.fileTree.fileTree.length > 0
        let header = ''
        //动态计算头部信息
        if (this.showHeader) {
            const fileName = this.currentNode.label ? '【当前查看文件名称】：' + this.currentNode.label : ''
            const modules = filterComponents(META_DATA_HEADER_VIEW_MODULE_NAME, this.headerIncludes, this.headerExcludes, this)
                .map(m => <m.component formDefinitionId={this.formDefinitionId} formData={this.formData}/>)
             header = (
                 <nac-info title={this.title + fileName} back={false}>
                     {modules}
                     {this.$slots.default}
                 </nac-info>
            )
        }

        const buildDetailContent = (hasFile) => {
            const slot = hasFile ? 'paneR' : 'default'
            const modules = this.detailComponents[this.detailTabModel]
            return (
                <section slot={slot} class='detailContainer'>
                    {this.$slots.detailTop}
                    {<el-tabs v-show={this.defaultShowTabs} class='detailTabs' type="border-card" v-model={this.detailTabModel}>
                        {this.detailComponents.map(m => <el-tab-pane label={m.label || m.name}/>)}
                    </el-tabs>}
                    <modules.component v-show={this.defaultShowFormData} class='detailContext' formDefinitionId={this.formDefinitionId}
                                       formData={this.formData}/>
                    {this.$slots.detailBottom}
                </section>
            )
        }
        const detailContent = buildDetailContent(hasFile)

        let children
        //显示按钮
        let btn = ''
        if (hasFile) {
            const maskStyle = this.dragging ? {width: '100%', height: '100%'} : {width: 0, height: 0}
            const click = () => this.detailShow = !this.detailShow
            btn = (<el-button type='primary' class='changeBtn'
                              on={{click}}>
                {this.detailShow ? '关闭详情' : '打开详情'}
            </el-button>)
            const fileViews = (
                <fileContent1
                    class='files'
                    getFileId={this.getFileId}
                    keyword={this.keyword}
                    watermark={this.watermark}
                    watermarkStatus = {this.watermarkStatus}
                    printStatus = {this.printStatus}
                    downloadStatus = {this.downloadStatus}
                    on={{showFile: this.showFile}}
                    fileTree={this.fileTree.fileTree}></fileContent1>)
            if (this.detailShow) {
                //动态计算左侧信息
                children = (
                    <splitPane default-percent={this.defaultPercent} split="vertical"
                               nativeOn={{mousedown: this.mousedown, mouseup: this.mouseup}}>
                        <section slot='paneL'>
                            {fileViews}
                            <section class='mask' style={maskStyle}/>
                        </section>
                        {detailContent}
                    </splitPane>
                )
            } else {
                children = fileViews
            }
        } else {
            //动态计算右侧信息
            children = detailContent
        }
        return (
            <section class="metaView">
                {header}
                {btn}
                <section class="index_main">
                    {children}
                </section>
            </section>
        )
    },
}



