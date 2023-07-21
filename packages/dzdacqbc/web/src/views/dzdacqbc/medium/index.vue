<template>
    <table-index :fields="fields" path="/eSaveMedium" ref="table" :back="$route.query.back"
                 :defaultSearchForm="searchForm">
        <template v-slot:edit="form">
            <el-form-item label="备注:" prop="marks">
                <el-input type="textarea"
                          :rows="5"
                          placeholder="请输入备注信息"
                          v-model="form.marks">
                </el-input>

            </el-form-item>
        </template>

    </table-index>
</template>
<script>
    export default {
        data() {
            return {
                fondParams: {type: 'fond'},
                fields: [
                    {
                        prop: 'mediumName',
                        label: '介质名称',
                        search: true,
                        required: true
                    },
                    {
                        prop: 'mediumType',
                        label: '介质类型',
                        width: "230",
                        fieldType: 'radio',
                        mapper: {'gp': '光盘', 'yp': '硬盘'},
                        required: true
                    },
                    {prop: 'createDate', label: '创建时间', dateFormat: 'YYYY-MM-DD HH:mm:ss', width: 180, edit: false},
                    {prop: 'marks', label: '介质说明', width: 260},
                ],
                searchForm: {id: this.$route.query.id},
                id: '',
                categories: []
            }
        },
        watch: {},
        methods: {}
    }
</script>