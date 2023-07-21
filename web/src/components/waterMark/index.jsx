import {useUser} from "@dr/framework/src/hooks/userUser";
import {reactive, watchEffect} from "vue";
import day from 'dayjs'

const getPixelRatio = (context) => {
    if (!context) {
        return 1;
    }
    const backingStore =
        context.backingStorePixelRatio ||
        context.webkitBackingStorePixelRatio ||
        context.mozBackingStorePixelRatio ||
        context.msBackingStorePixelRatio ||
        context.oBackingStorePixelRatio ||
        context.backingStorePixelRatio ||
        1;
    return (window.devicePixelRatio || 1) / backingStore;
};

/**
 * 前端水印组件，用来给指定的view打水印
 */
export default {
    name: 'WaterMark',
    props: {
        gapX: {type: Number, default: 80},
        gapY: {type: Number, default: 76},
        width: {type: Number, default: 140},
        height: {type: Number, default: 60},
        rotate: {type: Number, default: -10},
        fontColor: {default: 'rgba(0,0,0,.15)'},
        fontSize: {type: Number, default: 14},
        fontFamily: {
            type: String,
            default: ` "Helvetica Neue", Helvetica, Arial, "Microsoft Yahei", "Hiragino Sans GB", "Heiti SC", "WenQuanYi Micro Hei", sans-serif`
        },
        fontStyle: {type: String, default: 'normal'},
        fontWeight: {type: String, default: 'normal'},
    },
    setup(prop) {
        const {user} = useUser()
        const img = reactive({img: ''})
        watchEffect(() => {
            const canvas = document.createElement('canvas');
            const ctx = canvas.getContext('2d');
            const ratio = getPixelRatio(ctx);

            const canvasWidth = `${(prop.gapX + prop.width) * ratio}px`;
            const canvasHeight = `${(prop.gapY + prop.height) * ratio}px`;
            const canvasOffsetLeft = prop.gapX / 2;
            const canvasOffsetTop = prop.gapY / 2;

            canvas.setAttribute('width', canvasWidth);
            canvas.setAttribute('height', canvasHeight);

            // 旋转字符 rotate
            ctx.translate(canvasOffsetLeft * ratio, canvasOffsetTop * ratio);
            ctx.rotate((Math.PI / 180) * Number(prop.rotate));
            const markHeight = prop.height * ratio;

            const markSize = Number(prop.fontSize) * ratio;
            ctx.font = `${prop.fontStyle} normal ${prop.fontWeight} ${markSize}px/${markHeight}px ${prop.fontFamily}`;
            ctx.fillStyle = prop.fontColor;
            const name = (user.userName || '') + `\n` + day().format('YY/MM/DD')
            ctx.fillText(name, 14, 0);
            img.img = canvas.toDataURL()
        })
        return img
    },

    render() {
        return (
            <div
                style={{position: 'relative', height: '100%'}}>
                {this.$slots.default}
                <div style={{
                    zIndex: '1800',
                    position: 'absolute',
                    left: 0,
                    top: 0,
                    width: '100%',
                    height: '100%',
                    backgroundSize: `${212 + this.width}px`,
                    pointerEvents: 'none',
                    backgroundRepeat: 'repeat',
                    backgroundImage: `url('${this.img}')`,
                }}
                />
            </div>
        )
    }
}