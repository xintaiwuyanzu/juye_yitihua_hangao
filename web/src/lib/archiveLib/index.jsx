import abstractArchiveLib from "./abstractArchiveLib";
import createFormIndex from "../archiveFormIndex";
import './style.scss'

/**
 * 创建列表显示页面
 * @param h createElement
 * @param title 显示标题
 * @param formId 表单Id
 * @param id id，用来设置refs
 * @param params index创建参数
 * @returns {JSX.Element}
 */
const createIndex = (h, {title, formId, id}, params) => {
    const formIndex = createFormIndex(h, params)
    return <formIndex title={title} formId={formId} categoryType={id}/>
}
/**
 * 创建渲染列表对象
 */
const createLib = (params) => {
    return {
        //继承抽象父类，父类实现相关的逻辑，字类实现渲染相关
        extends: abstractArchiveLib,
        render(h) {
            const {archives} = this
            let content
            if (archives.length > 0) {
                content = archives.map(a => createIndex(h, a, params))
            } else {
                content = (<span>请选择左侧全宗门类树或者指定的分类没有配置元数据</span>)
            }
            let body_style = {
                padding: '-20px',
                height: '860px',
                border: '0'
            }
            if (archives.length > 1) {
                return (
                    <section>
                        <section class="archiveLibIndex">
                            <el-card class="fondTree" shadow="hover">
                                <fond-tree showHide={true} autoSelect on-check={this.check} on-fond={d => this.fond = d}
                                           ref="fondTree" withPermission={true}/>
                            </el-card>
                            <el-card class="tables">
                                <el-tabs value={this.tab_pan} type="border-card">
                                    <el-tab-pane label="案卷级档案" name="first">
                                        <el-card className="tables" body-style={body_style}>{content[0]}</el-card>
                                    </el-tab-pane>
                                    <el-tab-pane label="卷内文件档案" name="second">
                                        <el-card className="tables" body-style={body_style}>{content[1]}</el-card>
                                    </el-tab-pane>
                                </el-tabs>
                            </el-card>
                        </section>
                    </section>
                )
            }
            return (
                <section>
                    <section class="archiveLibIndex">
                        <el-card class="fondTree" shadow="hover">
                            <fond-tree showHide={true} autoSelect on-check={this.check} on-fond={d => this.fond = d}
                                       ref="fondTree" withPermission={true}/>
                        </el-card>
                        <el-card class="tables" shadow="hover">
                            {content}
                        </el-card>
                    </section>
                </section>
            )
        }
    }
}
export default createLib
