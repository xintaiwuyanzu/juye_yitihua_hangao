import abstractFormIndex from "./abstractFormIndex";

/**
 * 创建单个组件
 */
const createSingle = (h, c, {currentRow, currentSelect}) => {
    return <c currentSelect={currentSelect} currentRow={currentRow}/>
}

/**
 * 根据参数创建所有的头部控件
 */
const createHeaderComponents = (h, props, headerParts) => {
    return headerParts
        .map(p => createSingle(h, p.component, props))
}
/**
 * 根据参数创建所有的列控件
 */
const createColumnComponents = (h, {currentRow, currentSelect}, columnParts) => {
    return {
        default: s => {
            return columnParts.map(a => {
                const c = a.component
                if (c) {
                    return <c row={s.row} currentSelect={currentSelect} currentRow={currentRow}/>
                }
            })
        }
    }
}
/**
 * 动态创建列
 * @param h
 * @param listFields
 * @returns {*}
 */
const createColumns = (h, listFields, context) => {
    return listFields.map(f => {
        if (f.meta && f.meta.dict) {
            //字典转换
            const formatter = (r, i, c) => {
                return context.$options.filters.dict(c, f.meta.dict)
            }
            return (
                <el-table-column label={f.name} prop={f.code}
                                 formatter={formatter}
                                 align="center"
                                 min-width={f.remarks}
                                 show-overflow-tooltip>
                </el-table-column>
            )
        } else {
            return <el-table-column label={f.name} prop={f.code}
                                    sortable="custom"
                                    align="center"
                                    min-width={f.remarks}
                                    show-overflow-tooltip/>
        }
    })
}


/**
 * hoc 方法，动态创建表单渲染列表
 * @param h
 * @param query 默认查询条件
 * @param defaultForm 默认表单属性
 */
const createFormIndex = (h, {columnParts = [], headerParts = [], type}) => {
    return {
        //继承抽象父类，父类实现相关的逻辑，字类实现渲染相关
        extends: abstractFormIndex,
        data() {
            return {
                //等于 e
                //LIKE l
                //START_WITH   s
                //END_WITH  ed
                //in i
                //查询条件
                query: [
                    //状态作为查询条件
                    {key: 'status_info', type: 'i', value: type},
                ],
                defaultForm: {status_info: type}
            }
        },
        render(h) {
            const props = {
                currentRow: this.currentRow,
                currentSelect: this.currentSelect
            }
            const headers = createHeaderComponents(h, props, headerParts)
            let content = '加载中...'
            const {listFields} = this
            if (listFields.length > 0) {
                const filterColumnParts = columnParts
                    .filter(c => {
                        if (c.target) {
                            return c.target !== this.categoryType
                        } else {
                            return true
                        }
                    })
                const columnSlots = createColumnComponents(h, props, filterColumnParts)
                //计算操作栏宽度
                const editWidth = filterColumnParts
                    .map(c => c.width ? c.width : 30).reduce((t, a) => t + a + 6, 0) + 30
                const columns = createColumns(h, listFields, this)
                content = (
                    <div class="index_main">
                        <div class="table-container">
                            <el-table ref="multipleTable" data={this.data}
                                      on-row-click={this.rowClick}
                                      on-selection-change={this.select}
                                      border height="100%"
                                      v-loading={this.loading}
                                      on-sort-change={this.sort}
                                      element-loading-text="拼命加载中..."
                                      row-class-name={this.rowColor}>
                                <el-table-column type="selection" width="50" align="center"/>
                                <column-index page={this.page}/>
                                {columns}
                                <el-table-column label="操作" align="center" class-name="editColumn"
                                                 width={editWidth}
                                                 scopedSlots={columnSlots}>
                                </el-table-column>
                            </el-table>
                        </div>
                        <page page={this.page} on-current-change={index => {
                            this.loadData({index})
                            this.$refs.multipleTable.bodyWrapper.scrollTop = 0;

                        }
                        }/>
                    </div>
                )
            }
            return (
                <section>
                    <nac-info title={this.title}>
                        {headers}
                    </nac-info>
                    {content}
                </section>
            )
        }
    }
}
export default createFormIndex
