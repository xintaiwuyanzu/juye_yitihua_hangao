package com.dr.archive.fuzhou.ofd.watermark.demo;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caor
 * @date 2021-12-25 14:26
 */
public class URLGeneratorForExtensionDemo {
    private static Gson gson = new Gson();

    /**
     * TODO 此方法为生成云阅读访问路径的逻辑示例：
     * 1>云阅读现支持的对接模式有三：
     * 1.在云阅读的页面访问路径后拼接docuri参数，此参数可支持打开HTTP协议的文件，如还需回写编辑完的文件，那么还需拼接saveuri参数(值为对接系统的接收文件的接口地址)
     * 示例：
     * http://localhost:8080/viewer/pc/index.html?docuri={http文件下载地址}&saveuri={upload文件的目标地址}
     * 2.云阅读的页面访问路径后拼接docParam参数，此参数可根据参数内容与云阅读的接口配置文件决定如何与外部系统的接口集成
     * 2.1 如果拼接docParam与docParam=restfulsingleparam，那么三方接口服务的所有接口仅需声明一个入参，并将入参名称配置到云阅读的接口交互文件中
     * 示例：
     * http://localhost:8080/viewer/pc/index.html?docParam={对接系统所需的明文参数值}&paramType=restfulsingleparam
     * 2.2 如果拼接了docParam=xxxx&paramType=restfulmultiparam,那么云阅读将认定docParam的值为加密的json字符串。云阅读后台
     * 将会根据配置文件中的密钥使用AES算法解密，然后将docParam解密出的json与配置文件中的各个param与head配置进行匹配。
     * 然后使用匹配的值请求对接的接口服务。
     * 示例：
     * http://localhost:8080/viewer/pc/index.html?docParam={对接系统所有接口的入参值组成的加密JSON串}&paramType=restfulmultiparam
     * <p>
     * TODO 2>云阅读支持配置多个extensionApi.conf文件:
     * 如果要与接口对接支持,那么地址栏参数中要拼接linkip字段,linkip字段的值就是您在云阅读安装目录/modules/reader/plugins/下的文件后缀名
     * 比如: extensionApi-x1.conf,那么您的linkip字段后拼接的参数就是x1
     * 拼接示例如下:
     * http://localhost:8080/viewer/pc/index.html?docParam={您组织的接口请求参数}&paramType={请求参模式}&linkip={多配置文件的文件名称后缀}
     */
    public static void main(String[] args) {


//        final File file = new File("http://192.168.1.134:80/api/files/downLoad/5342fe7b-a609-4ae7-a4d2-cf53f521c32e?download=false");
//        final String path = file.getPath();
//        System.out.println(file.exists());
//        System.out.println(path);

        /* 云阅读根页面地址 */
        //String webOfdIndexUrl = "http://10.6.6.171:8081/foxit-webpdf-web/mobile/demo.html";

//        String webOfdIndexUrl = "http://192.168.1.143:8088/viewer/pc/index.html";
        String webOfdIndexUrl = "http://192.168.1.143:8088/viewer/pc/index.html";
        /* 文件索引-此处为了演示直接声明的是文件名 */
        String file1 = "http://192.168.1.134:80/mb.ofd";
//        String file2 = "motor.ofd";
//        String file3 = "2019区块链白皮书.ofd";
        /* docParam : 如果是JSON多参数加密的逻辑，那么就应该先封装对象 */
        Map<String, String> docParamObj = new HashMap<>();
        docParamObj.put("fileId", file1);
//        docParamObj.put("userId", "x1xwaefaew234242324sfdvsd8888");
        /* docParam : 如果是单参数，那么docParam后直接跟着具体值 */
        String docParam = gson.toJson(docParamObj);

//        System.out.println("示例连接 --->  " + URLGeneratorForExtension.generateWebOFDUrl(webOfdIndexUrl, docParam, URLGeneratorForExtension.WebOFDBizTypeEnum.restfulmultiparam, null));
    }
}
