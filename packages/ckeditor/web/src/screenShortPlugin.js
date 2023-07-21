import ScreenShort from 'js-web-screen-shot';
import ButtonView from '@ckeditor/ckeditor5-ui/src/button/buttonview';
import imageIcon from './scrrenShort.svg';
import {Plugin} from "@ckeditor/ckeditor5-core";

//浏览器是否支持截屏功能
let enableWebRtc = false
if (navigator.mediaDevices) {
    enableWebRtc = !!navigator.mediaDevices.getDisplayMedia
}
/**
 * ckEditor追加截屏插件
 * @param editor
 */

export default class ScreenShortPlugin extends Plugin {

    static get pluginName() {
        return 'ScreenShort';
    }

    init() {
        const editor = this.editor;
        editor.ui.componentFactory.add('ScreenShort', locale => {
            const view = new ButtonView(locale);
            view.set({
                label: '截图',
                icon: imageIcon,
                tooltip: true
            });
            view.on('execute', () => {
                new ScreenShort({
                    loadCrossImg: true,
                    enableWebRtc,
                    level: 5000,
                    // 截图确认按钮回调函数
                    completeCallback(base64) {
                        editor.model.change(writer => {
                            const imageElement = writer.createElement('imageBlock', {
                                src: base64
                            });
                            editor.model.insertContent(imageElement, editor.model.document.selection);
                        });
                    }
                });
            });
            return view;
        });
    }

}