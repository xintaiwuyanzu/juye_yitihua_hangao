<template>
    <el-tree highlight-current
             v-loading="loading"
             :data="categorys"
             accordion
             ref="tree"
             @node-click="n=>$emit('check',n)"
             default-expand-all
             node-key="id"
             :show-checkbox="showCheck">
    </el-tree>
</template>
<script>
import abstractFond from "./kufangfondTree/abstractFond";

export default {
    extends: abstractFond,
    watch: {
        fondId() {
            this.loadCategory()
        }
    },
    data() {
        return {
            //所有门类
            categorys: [],
            //当前选中门类
            currentCategory: {}
        }
    },
    methods: {
        loadCategory() {
            if (this.fondId){
                this.$http.post('archiveType/getAllTypeTree', {fondId:this.fondId,pid: 'root'})
                    .then(({data}) => {
                        if (data.success) {
                            this.categorys = data.data ? data.data : []
                        }
                    })
            }
        },
        /**
         * 获取选中的节点
         * @returns {D[]}
         */
        getCheckedNodes() {
            return this.$refs.tree.getCheckedNodes();
        },
        /**
         * 获取选中的Id
         * @returns {D[]}
         */
        getCheckedKeys() {
            return this.$refs.tree.getCheckedKeys()
        },
        $init() {
            this.loadCategory()
        }
    }
}
</script>
