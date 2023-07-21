import container from "./container";
import {register} from "@dr/framework/lib/module";

export const FILE_VIEW_MODULE_NAME = "fileView";
register(FILE_VIEW_MODULE_NAME, 'image', import('./imageView'), {fileType: ['png', 'jpg', 'jpeg', 'bmp', 'gif']})
register(FILE_VIEW_MODULE_NAME, 'txt', import('./textView'), {fileType: ['xml', 'txt', 'vue', 'js', 'conf', 'java', 'css', 'scss']})
register(FILE_VIEW_MODULE_NAME, 'docx', import('./wordView'), {fileType: ['doc', 'docx']})

//OFD是福州项目特有的功能
register(FILE_VIEW_MODULE_NAME, 'ofd', import('./ofdView'), {fileType: ['ofd']})
register(FILE_VIEW_MODULE_NAME, 'pdf', import('./pdfView'), {fileType: ['pdf']})
register(FILE_VIEW_MODULE_NAME, 'flv', import('./mediaView'), {fileType: ['mp4', 'mp3', 'flv']})
register(FILE_VIEW_MODULE_NAME, 'tiff', import('./tiffView'), {fileType: ['tiff', 'tif']})
/**
 * 模块名称
 * @type {string}
 */
export default container