<template>
    <section>
        <div style="display: flex;flex-direction: row;height: 100%;overflow: auto;padding: 5px">
            <fond-tree autoSelect @check="check" style="overflow:scroll;width: 300px"
                       :withPermission="true"></fond-tree>
            <div class="index_main" style="padding: 0px;height: 99%" v-loading="loading">
                <div class="table-container" style="height: 80%">
                    <table-index :fields="fields" ref="table" path="order" :insert="true"
                                 :delete="true"
                                 :defaultInsertForm="defaultInsertForm"
                                 :defaultSearchForm="defaultSearchForm"
                                 :edit="false">
                    </table-index>
                </div>
            </div>
        </div>
    </section>
</template>

<script>
    export default {
        name: "index",
        data() {
            return {
                form: {},
                current: {},
                show: false,
                id: '',
                loading: false,
                fields: [
                    {
                        prop: 'fieldOrder',
                        label: '排序字段',
                        required: true,
                        search: true,
                        fieldType: 'select',
                        dictKey: 'orderfield'
                    }, {
                        prop: 'code',
                        label: '所属库',
                        required: true,
                        fieldType: 'select',
                        dictKey: 'order'
                    }, {
                        prop: 'orderType',
                        label: '排序方式',
                        required: true,
                        fieldType: 'select',
                        dictKey: 'orderType'
                    }, {
                        prop: 'archiveType',
                        label: '档案类型',
                        required: true,
                        fieldType: 'select',
                        dictKey: 'archiveType'
                    }, {
                        prop: 'order',
                        label: '顺序',
                        required: true,
                    }
                ],
                defaultInsertForm: {
                    cateGoryId: ''
                },
                defaultSearchForm: {
                    cateGoryId: ''
                }
            }
        },
        methods: {
            check(v) {
                this.defaultInsertForm.cateGoryId = v.data.id
                this.defaultSearchForm.cateGoryId = v.data.id
                this.$refs.table.loadData()
            }
        }
    }
</script>

<style scoped>
    .table-wrapper {
        display: flex;
        flex-direction: column;
        flex: 1;
        overflow: auto;
        height: 840px !important;
        margin-bottom: 0;
    }
</style>