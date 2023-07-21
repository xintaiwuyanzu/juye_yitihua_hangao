<template>
    <div>
        <el-cascader :value="id" style="width: 100%" placeholder="请选择人员" :options="options" @change="change"></el-cascader>
    </div>
</template>
<script>
    export default {
        name: 'selectOrganise',
        props: {
            id: {type: String},
        },
        data() {
            return {
                organises:[],
                belongOrganisePersons: [],
                options: [],
            }
        },
        methods: {
            change(v) {
                this.$emit('selected', v)
            }
        },
        mounted() {
            this.$http.post('/organise/getAllDepartment')
                .then(({data}) => {
                    if (data.success) {
                        this.organises = data.data ? data.data : []
                        this.organises.forEach(item => {
                            let obj = {value: item.id, label: item.organiseName, children: []}
                            this.$http.post('/person/page', {page: false, defaultOrganiseId: item.id})
                                .then(({data}) => {
                                    if (data.success) {
                                        this.belongOrganisePersons = data.data ? data.data : []
                                        this.belongOrganisePersons.forEach(item => {
                                            obj.children.push({value: item.id, label: item.userName})
                                        })
                                    }
                                })
                            this.options.push(obj)
                        })
                    }
                })
        }
    }
</script>
