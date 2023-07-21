export default {
    props: {
        //标题
        title: {type: String}, //表单定义Id
        formId: {type: String}, //档案类型  'PRO', 'ARC', 'FILE'
        categoryType: {type: String}
    },
    inject: [//根页面对象
        'libIndex'], computed: {
        //当前全宗对象
        fond() {
            return this.libIndex.fond
        }, //当前分类对象
        category() {
            return this.libIndex.category
        }, /**
         * 动态计算上一级的控件列表控件
         * 文件和案卷联动的时候用到
         * @returns {Vue | Element | Vue[] | Element[]}
         */
        parentIndex() {
            if (this.categoryType === 'FILE') {
                return this.libIndex.childIndexs.ARC
            } else if (this.categoryType === 'ARC') {
                return this.libIndex.childIndexs.PRO
            }
        }, /**
         * 动态计算下一级控件列表控件
         * 文件和案卷联动的时候用到
         * @returns {Vue | Element | Vue[] | Element[]}
         */
        childrenIndex() {
            if (this.categoryType === 'PRO') {
                return this.libIndex.childIndexs.ARC
            } else if (this.categoryType === 'ARC') {
                return this.libIndex.childIndexs.FILE
            }
        }
    },
    provide() {
        return {
            eventBus: this
        }
    },
    data() {
        return {
            loading: false, //列表数据
            data: [], //所有孩子自定义组件
            queryItem: [], //当前点击的一行数据
            currentRow: {}, //当前选中的多组数据
            currentSelect: [], //列表显示方案
            listFields: [], page: {
                size: 15, index: 0, total: 0
            }, open: false, //提示
            tips: 0, //该全宗的移交时限配置
            handOverConfig: [], delayList: [],
        }
    },
    methods: {
        async delete() {
            try {
                await this.$confirm('此操作将彻底删除选中项无法恢复, 是否继续?', '提示', {
                    confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
                })
                this.loading = true
                const {data} = await this.$post('/receive/offline/deleteForm', {
                    categoryId: this.category.id, formId: this.formId, id: this.currentSelect.map(i => i.id).join(",")
                })
                if (data.success) {
                    this.$message.success('删除成功,请稍后刷新！')
                    await this.loadData()
                } else {
                    this.$message.error(data.message)
                }
            } catch (e) {
                console.log(e)
            }
            //清空当前选中状态
            //this.currentSelect = {}
            this.currentRow = {}
            //this.currentSelect = []
            this.loading = false
        }, /**
         * 更新状态
         * @returns {Promise<void>}
         */
        async changeStatus(params) {
            this.loading = true
            const {data} = await this.$post('/manage/formData/updateStatus', {formDefinitionId: this.formId, ...params})
            if (data.success) {
                this.$message.success('操作成功！')
                // await this.record(params)
                await this.loadData()
            } else {
                this.$message.error(data.message)
                this.loading = false
            }
        },
        // record(val) {
        //     if (val.status === 'RECYCLE') {
        //         this.$post('/archiveMetadataRecord/newChangeMetadata', {
        //             type: 'RECYCLE',
        //             formDefinitionId: this.formId,
        //             oldFormData: '',
        //             newFormData: JSON.stringify(val.row)
        //         })
        //     } else {
        //         this.$post('/archiveMetadataRecord/newChangeMetadata', {
        //             type: 'RECOVERY',
        //             formDefinitionId: this.formId,
        //             oldFormData: '',
        //             newFormData: JSON.stringify(val.row)
        //         })
        //     }
        // },
        async loadData(param) {
            this.loading = true
            //默认查询参数
            const defaultParams = this.getFormQuery(param)
            const {data} = await this.$post('/manage/formData/formDataPage', Object.assign(//默认参数
                defaultParams, //分页参数
                this.page, //需要查询的参数
                param), {timeout: 20000})
            this.loading = false
            if (data.success) {
                this.data = data.data.data
                this.page.total = data.data.total
                this.page.size = data.data.size
                this.page.index = data.data.start / data.data.size + 1
            } else {
                this.data = []
                this.page = {
                    size: 15, index: 0, total: 0
                }
            }
            this.queryItem.forEach(i => {
                //告诉所有的孩子组件数据加载完成了
                i.$emit('loaded')
            })
        }, /**
         * 根据查询类型获取查询条件
         * @param queryType
         * @returns {*}
         */
        getQueryByQueryType(queryType) {
            switch (queryType) {
                case "select":
                    return this.getCurrentSelectQuery()
                case "query":
                    return this.getFormQuery()
                default:
                    return this.getAllQuery()
            }
        }, /**
         * 获取表单查询条件
         */
        getFormQuery() {
            //从所有的孩子获取查询参数
            let query = [...this.query]
            this.queryItem.forEach(i => {
                if (i.getQuery) {
                    const itemQuery = i.getQuery()
                    if (itemQuery) {
                        query = query.concat(itemQuery)
                    }
                }
            })
            return this.getDefaultQueryWithParams(query)
        }, /**
         * 给子控件用，
         * 获取当前选中查询对象
         */
        getCurrentSelectQuery() {
            const query = [{key: "id", type: 'i', value: this.currentSelect.map(i => i.id).join(',')}]
            return this.getDefaultQueryWithParams(query)
        }, /**
         * 获取全部查询的查询条件
         */
        getAllQuery() {
            let query = [...this.query]
            return this.getDefaultQueryWithParams(query)
        }, /**
         * 将query拼成默认查询参数
         * @param query
         * @returns {{_QUERY: string, fondId: *, formDefinitionId: String, categoryId: *}}
         */
        getDefaultQueryWithParams(query) {
            return {
                //全宗Id
                fondId: this.fond.id, //分类Id
                categoryId: this.category.id, //表单定义Id
                formDefinitionId: this.formId, //查询条件
                _QUERY: JSON.stringify(query)
            }
        },
        async loadListShowScheme() {
            this.loading = true
            if (this.formId) {
                const {data} = await this.$http.post('/manage/form/selectDisplayByDefinition', {
                    formDefinitionId: this.formId, formType: 'list', formCode: 'list'
                })
                if (data.success) {
                    const listFields = data.data.fields
                    //这里加载字典
                    for (const index in listFields) {
                        const field = listFields[index]
                        if (field.meta && field.meta.dict) {
                            await this.$loadDict(field.meta.dict)
                        }
                    }
                    this.listFields = listFields
                } else {
                    this.$message.error(data.message)
                }
            }
            this.loading = false
        }, //一行点击事件
        rowClick(v) {
            this.currentRow = v
            if (this.childrenIndex) {
                this.childrenIndex.$emit('loadData')
            }
        }, //选择事件
        select(v) {
            this.currentSelect = v
        },
        getQuery() {
            if (this.currentRow.id) {
                //档案给文件提供查询条件
                //return [{key: 'ARCHIVE_CODE', type: 's', value: this.currentRow['ARCHIVE_CODE']}]
                //console.log('currentRow is =====>', this.currentRow)
                //return [{key: 'AJDH', type: 's', value: this.currentRow['ARCHIVE_CODE']}]
               // return [{key: 'AJDH', type: 'e', value: this.currentRow['ARCHIVE_CODE']}]
                return [{key: 'AJDH', type: 'e', value: this.currentRow['ARCHIVE_CODE']}]
            } else {
                return []
            }
        },
        rowColor() {
            /* this.tips = 0
             let time1 = new Date().getTime();
             let time = row.row.createDate
             //先判断有无延迟记录 todo 但凡有别的办法，我也不会这样写 当档案数据足够大 延迟移交表足够大，等着超时把

             //todo 有问题的代码 先注掉

             // this.getTips(row.row.id)

             console.log(this.tips)
             if (this.tips === 0) {
                 if (time !== null && time !== '') {
                     let number = parseInt(time) + this.handOverConfig.warningTime;
                     let number2 = parseInt(time) + this.handOverConfig.handoverTime;
                     if (number < time1) {

                         if (number2 < time1) {
                             console.log('红')
                             return 'highlight-row-red';
                         }
                         console.log('黄')
                         return 'highlight-row-yellow';
                     }
                 }
             } else if (this.tips === 2) {
                 console.log('紫')
                 return 'highlight-row-pop';
             } else {
                 //什么也不做
                 return '';
             }*/
            return ''
        },
        getTips(id) {
            for (let i = 0; i < this.delayList.length; i++) {
                if (this.delayList[i].archiveId === id) {
                    this.tips = 1;
                    if (new Date().getTime() > parseInt(this.delayList[i].newHangOverTime)) {
                        this.tips = 2
                    }
                }
            }
        }, //获取移交配置
        async getHandOverConfig() {
            this.loading = true
            const {data} = await this.$http.post('/handOverConfig/page', {organiseId: this.fond.id, page: false})
            if (data.success) {
                this.handOverConfig = data.data[0];
                console.log(this.handOverConfig)
            }
            this.loading = false
        },
        async getDelayList() {
            this.loading = true
            const {data} = await this.$http.post('/delayHandOver/page', {page: false})
            if (data.success) {
                this.delayList = data.data;
            }
            this.loading = false
        },
        sort({order, prop}) {
            this.loadData({orderType: order, orderKey: prop})
        },
    },
    mounted() {
        //移交时限配置
        // this.getHandOverConfig()
        //延时列表
        // this.getDelayList()

        this.libIndex.$emit('childIndex', {type: this.categoryType, child: this})
        //按照分类查询数据
        this.query.push({key: 'CATE_GORY_CODE', type: 'e', value: this.category.code})
        //监听子控件刷新数据方法
        this.$on('loadData', this.loadData)
        this.$on('delete', this.delete)
        this.$on('changeStatus', this.changeStatus)
        //加载显示方案
        this.loadListShowScheme()
            .then(this.loadData)
        this.$nextTick(() => {
            if (this.childrenIndex) {
                this.childrenIndex.$emit('addQueryItem', this)
            }
        })
    },
    created() {
        this.$on('addQueryItem', (item) => {
            if (item) {
                this.queryItem.push(item);
            }
        });
        //控件删除事件
    }
}
