<template>
    <section v-if="hideTree" class="fondTree" style="display: flex;flex-direction: column">
        <el-button v-if="showHide" @click="hideTree = false" type="text">隐藏</el-button>
        <el-select filterable :disabled="selectDisAble" v-model="selectFond" placeholder="请选择全宗">
            <el-option v-for="item in fonds" :key="item.id"
                       :label="`${item.data.code}  ${item.label}`" :value="item.id">
            </el-option>
        </el-select>
        <kufang-tree style="flex:1"
                     ref="tree"
                     :fond-id="selectFond"
                     :show-check="showCheck"
                     @check="v=>$emit('check',v)"
                     :with-permission="withPermission"/>
    </section>
    <section v-else class="fondTree" style="display: flex;flex-direction: column">
        <el-button type="text" @click="hideTree = true">展开</el-button>

    </section>
</template>
<script>
import abstractFond from "./abstractFond";
import kufangTree from "../kufangTree";

/**
 * 全宗门类树
 */
export default {
    extends: abstractFond,
    components: {kufangTree},
    props: {
        //是否自动选择第一个或者默认的全宗
        autoSelect: {type: Boolean, default: false},
        showHide: {type: Boolean, default: false}
    },
    computed: {
        /**
         * 当前选中的全宗
         */
        currentFond() {
            if (this.selectFond && this.fonds) {
                const fond = this.fonds.find(f => f.id === this.selectFond)
                return fond ? fond.data : {}
            }
            return {}
        },
        selectDisAble() {
            return !!this.fondId || this.fondId === 'template'
        }
    },
    watch: {
        fondId(v) {
            this.selectFond = v
        },
        selectFond() {
            this.$emit('fond', this.currentFond)
        }
    },
    data() {
        return {
            //选中的全宗Id
            selectFond: this.fondId,
            //所有全宗
            fonds: [],
            hideTree: true
        }
    },
    methods: {
        /**
         * 加载所有的全宗资源
         * @returns {Promise<void>}
         */
        async loadFonds() {
            this.loading = true
            const data = await this.$post(this.url, {type: 'fond'})
            if (data.data.success) {
                this.fonds = data.data.data
                if (!this.selectDisAble && this.autoSelect && this.fonds.length > 0) {
                    this.selectFond = this.fonds[0].id
                }
            }
            this.loading = false
        },
        async loadCategory() {
            await this.$refs.tree.loadCategory()
        },
        $init() {
            this.loadFonds()
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
        }
    }
}
</script>
