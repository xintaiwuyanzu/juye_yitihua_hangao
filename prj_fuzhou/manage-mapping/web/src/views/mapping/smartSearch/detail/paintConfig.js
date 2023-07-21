export const paintConfig = {
    fitCenter: true,
    //线
    defaultEdge: {
        style: {
            stroke: '#0164AC',
            endArrow: true,//末尾箭头
        },
    },
    //节点
    defaultNode: {
        size: 50,
        style: {
            cursor: 'pointer',
        }
    },
    //节点配置
    modes: {
        default: [
            "drag-canvas",//允许拖拽画布
            {
                type: "zoom-canvas",//放缩画布
                enableOptimize: true //开启性能优化
            },
            'drag-node',//拖拽节点
            // 'activate-relations',//高亮
            'click-select',//点击选中节点
        ]
    },
    //插件
    //布局
    layout: {
        // Object，可选，布局的方法及其配置项，默认为 random 布局。
        type: 'gForce', // 指定为力导向布局
        preventOverlap: true, // 防止节点重叠
        linkDistance: 150, // 指定边距离为100
        gpuEnabled: true          // 可选，开启 GPU 并行计算，G6 4.0 支持
    },
}