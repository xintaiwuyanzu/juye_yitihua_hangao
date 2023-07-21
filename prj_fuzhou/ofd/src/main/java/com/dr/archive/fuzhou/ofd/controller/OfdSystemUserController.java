package com.dr.archive.fuzhou.ofd.controller;

import com.dr.archive.fuzhou.ofd.enums.WebOFDTimeModelEnum;
import com.dr.archive.fuzhou.ofd.vo.WebOFDPermissionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 云阅读工具栏 展示与隐藏  （这里还有和权限有关的开关 配置 目前不用权限）
 *
 * @author cuiyj
 * @date 2021-12-26
 */
@RestController("/api/")
public class OfdSystemUserController {

    Logger logger = LoggerFactory.getLogger(OfdSystemUserController.class);

    /**
     * 获取当前用户及当前文档关联的权限信息
     * <p>
     * 返回参中权限内容取值范围如下:
     * <p>
     * 以下为工具栏按钮控制，如果工具栏中的功能按钮全部为 0 ，工具栏也将会消失。
     * head 					0 隐藏  1 显示   		LoGo
     * openFileBtn 				0 隐藏  1 显示   		打开文件
     * printBtn 				0 隐藏  1 显示   		打印
     * goToPageBox 				0 隐藏  1 显示   		翻页
     * zoomPageBox				0 隐藏  1 显示   		页面缩放
     * pageLayoutBtn			0 隐藏  1 显示   		页面布局
     * handToolBtn				0 隐藏  1 显示   		手型工具
     * textSelectBtn			0 隐藏  1 显示   		选择文本工具
     * heightLightBtn			0 隐藏  1 显示   		高亮
     * underlineBtn				0 隐藏  1 显示   		下划线
     * pencilBtn				0 隐藏  1 显示  		铅笔
     * drawingAnnotBtn			0 隐藏  1 显示  		其他标注
     * commentsBtn				0 隐藏  1 显示  		注释隐藏/显示
     * elecSignatureBtn			0 隐藏  1 显示  		签章
     * checkElecSignatureBtn	0 隐藏  1 显示  		验章
     * rotateSwitchBtn			0 隐藏  1 显示  		页面旋转
     * toolBarSearchBtn			0 隐藏  1 显示  		查找（工具栏）
     * 更多：包含
     * （打印，高亮，下划线，铅笔，其他标注，注释隐藏/显示，签章，验章，）
     * moreBtn					0 隐藏  1 显示  		更多
     * exportBtn				0 隐藏  1 显示  		下载文档
     * <p>
     * 以下为导航栏按钮控制，如果导航栏中的功能按钮全部为 0 ，导航栏也将会消失。
     * outlineBtn				0 隐藏  1 显示 			大纲
     * thumbnailBtn				0 隐藏  1 显示 			缩略图
     * commentListBtn			0 隐藏  1 显示 			注释评论
     * searchBtn				0 隐藏  1 显示 			查找
     * semanticTreeBtn			0 隐藏  1 显示 			语义树
     * attachmentBtn			0 隐藏  1 显示 			附件
     * <p>
     * 阅读范围设置:
     * 例:1,3-5,9,10-20 使用逗号进行间隔，全显示ALL，全部不显示None，默认全部显示
     * <p>
     * 时间设置:
     * 当用户选择的时间模式为'0' 绝对时间时, onlineReadTime 与 onlineReadBeginTime有值,时间格式为'yyyy-MM-dd HH:mm:ss';
     * 当用户选择的时间模式为'1' 相对时间时, onlineReadTime有值, 值为int类型整数,代表相对时间的分钟数;
     * 当用户选择的时间模式为'2' 不限时间, 以上两个字段内容将无意义, 为空即可;
     * <p>
     * 隐式水印三项(实际场景中仅需控制开关项即可)：
     * isImplicit        隐式水印开关
     * isShowImplicit    显示隐式水印
     * isPrintImplicit   打印隐式水印
     * <p>
     * areaSelectBtn;      区域选择开关 - 遮盖相关功能开关
     * cover               遮盖开关
     * preCover            预遮盖开关
     * <p>
     * http://localhost:8094/systemuser/permission?accessToken=123&userId=123&uri=123&version=123
     */
    @ResponseBody
    @RequestMapping(value = "systemuser/permission", method = RequestMethod.GET)
    public HashMap<String, Object> getSystemUserPermission(@RequestParam("userId") String userId,
                                                           @RequestParam("fileId") String fileId,
                                                           @RequestParam("version") String version) {
        /* 本接口接收以下四个参数-用于系统内查询关联文件及用户的权限信息 */
        //崔迎杰备注 ： userId 存放的是水印模板id 这里用不到
        //            fileId  这里也不用
        //            version  存放的是工具栏展示的参数  目前三种 （ all 全部展示  none 全部不展示  print 只展示打印 ）
        logger.info("权限获取接口接收请求:   userId:" + userId + " fileId:" + fileId + "  version:" + version);
        /* 组织权限数据返回-此处仅为格式演示-数据为假数据-生产环境需要根据协议类'WebOFDPermissionInfo'中字段的逻辑关系赋值 */
        HashMap<String, Object> map = new HashMap<>();
        map.put("ret", 0);
        map.put("message", "success");
        //这里直接写死的三种情况 后面如果 用的情况太多 建议像咱们系统的水印管理一样 搞成动态的
        if ("none".equals(version)) {
            //不要工具栏
            map.put("data", getPermissionDataNone());
        } else if ("all".equals(version)) {
            //全部展示
            map.put("data", getPermissionDataAll());
        } else {
            //只有打印
            map.put("data", getPermissionDataPrint());
        }
        return map;
    }

    /**
     * 固定全是 0  即不显示工具栏
     *
     * @return
     */
    private WebOFDPermissionInfo getPermissionDataNone() {
        /* 组织权限数据 */
        WebOFDPermissionInfo permissionInfo = new WebOFDPermissionInfo();
        permissionInfo.setPermission("2");                /* 权限总开关 2-有权限  0-无任何权限	 */
        permissionInfo.setHead("0");                    /* LOGO栏		 */
        permissionInfo.setOpenFileBtn("0");                /* 打开文件按钮 	 */
        permissionInfo.setSaveBtn("0");                    /* 保存文件按钮	 */
        permissionInfo.setPrintBtn("0");                /* 打印按钮		 */
        permissionInfo.setGoToPageBox("0");                /* 跳转指定页框	 */
        permissionInfo.setZoomPageBox("0");                /* 页面缩放		 */
        permissionInfo.setPageLayoutBtn("0");            /* 页面布局		 */
        permissionInfo.setHandToolBtn("0");                /* 手型工具		 */
        permissionInfo.setTextSelectBtn("0");            /* 选择文本工具 	 */
        permissionInfo.setHeightLightBtn("0");            /* 高亮			 */
        permissionInfo.setUnderlineBtn("0");            /* 下划线		 */
        permissionInfo.setPencilBtn("0");                /* 铅笔			 */
        permissionInfo.setDrawingAnnotBtn("0");            /* 其他标注		 */
        permissionInfo.setCommentsBtn("0");                /* 注释			 */
        permissionInfo.setElecSignatureBtn("0");        /* 签章			 */
        permissionInfo.setCheckElecSignatureBtn("0");    /* 验章			 */
        permissionInfo.setRotateSwitchBtn("0");            /* 页面旋转		 */
        permissionInfo.setExportBtn("0");                /* 下载文档		 */

        permissionInfo.setOutlineBtn("0");                /* 大纲			 */
        permissionInfo.setThumbnailBtn("0");            /* 缩略图		 */
        permissionInfo.setCommentListBtn("0");            /* 注释评论		 */
        permissionInfo.setSearchBtn("0");                /* 查找			 */
        permissionInfo.setSemanticTreeBtn("0");            /* 语义树		 */
        permissionInfo.setAttachmentBtn("0");            /* 附件			 */

        /* 阅读页数控制字段 */
//        permissionInfo.setPageRange("1,2,3-4");
        /* 阅读时间控制 */
        permissionInfo.setTimeModel(WebOFDTimeModelEnum.UNLIMITED_TIME.getTimeModel());
        /* 在线阅读起始时间 */
        //permissionInfo.setOnlineReadBeginTime("2021-04-08 15:30:00");
        /* 在线阅读结束时间 */
        //permissionInfo.setOnlineReadTime("2021-04-08 12:00:00");

        /* 隐写溯源三项 */
        permissionInfo.setIsImplicit("0");    /* 实际环境中,仅需控制这个开关即可，其他两个字段为保留字段供后期需求使用 */
        permissionInfo.setIsPrintImplicit("0");
        permissionInfo.setIsShowImplicit("0");

        /* 遮盖预遮盖 */
        permissionInfo.setAreaSelectBtn("0");        /* 区域选择工具开关-遮盖相关所有功能的开关 */
        permissionInfo.setCover("0");
        permissionInfo.setPreCover("0");


        return permissionInfo;
    }

    /**
     * 固定全是 0  即不显示工具栏
     *
     * @return
     */
    private WebOFDPermissionInfo getPermissionDataPrint() {
        /* 组织权限数据 */
        WebOFDPermissionInfo permissionInfo = new WebOFDPermissionInfo();
        permissionInfo.setPermission("2");                /* 权限总开关 2-有权限  0-无任何权限	 */
        permissionInfo.setHead("0");                    /* LOGO栏		 */
        permissionInfo.setOpenFileBtn("0");                /* 打开文件按钮 	 */
        permissionInfo.setSaveBtn("0");                    /* 保存文件按钮	 */
        permissionInfo.setPrintBtn("0");                /* 打印按钮		 */
        permissionInfo.setGoToPageBox("1");                /* 跳转指定页框	 */
        permissionInfo.setZoomPageBox("1");                /* 页面缩放		 */
        permissionInfo.setPageLayoutBtn("0");            /* 页面布局		 */
        permissionInfo.setHandToolBtn("1");                /* 手型工具		 */
        permissionInfo.setTextSelectBtn("1");            /* 选择文本工具 	 */
        permissionInfo.setHeightLightBtn("0");            /* 高亮			 */
        permissionInfo.setUnderlineBtn("0");            /* 下划线		 */
        permissionInfo.setPencilBtn("0");                /* 铅笔			 */
        permissionInfo.setDrawingAnnotBtn("0");            /* 其他标注		 */
        permissionInfo.setCommentsBtn("0");                /* 注释			 */
        permissionInfo.setElecSignatureBtn("0");        /* 签章			 */
        permissionInfo.setCheckElecSignatureBtn("0");    /* 验章			 */
        permissionInfo.setRotateSwitchBtn("0");            /* 页面旋转		 */
        permissionInfo.setExportBtn("0");                /* 下载文档		 */

        permissionInfo.setOutlineBtn("0");                /* 大纲			 */
        permissionInfo.setThumbnailBtn("0");            /* 缩略图		 */
        permissionInfo.setCommentListBtn("0");            /* 注释评论		 */
        permissionInfo.setSearchBtn("0");                /* 查找			 */
        permissionInfo.setSemanticTreeBtn("0");            /* 语义树		 */
        permissionInfo.setAttachmentBtn("0");            /* 附件			 */

        /* 阅读页数控制字段 */
//        permissionInfo.setPageRange("1,2,3-4");
        /* 阅读时间控制 */
        permissionInfo.setTimeModel(WebOFDTimeModelEnum.UNLIMITED_TIME.getTimeModel());
        /* 在线阅读起始时间 */
        //permissionInfo.setOnlineReadBeginTime("2021-04-08 15:30:00");
        /* 在线阅读结束时间 */
        //permissionInfo.setOnlineReadTime("2021-04-08 12:00:00");

        /* 隐写溯源三项 */
        permissionInfo.setIsImplicit("0");    /* 实际环境中,仅需控制这个开关即可，其他两个字段为保留字段供后期需求使用 */
        permissionInfo.setIsPrintImplicit("0");
        permissionInfo.setIsShowImplicit("0");

        /* 遮盖预遮盖 */
        permissionInfo.setAreaSelectBtn("0");        /* 区域选择工具开关-遮盖相关所有功能的开关 */
        permissionInfo.setCover("0");
        permissionInfo.setPreCover("0");


        return permissionInfo;
    }

    /**
     * 固定全是 1  即显示工具栏所有按钮
     *
     * @return
     */
    private WebOFDPermissionInfo getPermissionDataAll() {
        /* 组织权限数据 */
        WebOFDPermissionInfo permissionInfo = new WebOFDPermissionInfo();
        permissionInfo.setPermission("2");                /* 权限总开关 2-有权限  0-无任何权限	 */
        permissionInfo.setHead("1");                    /* LOGO栏		 */
        permissionInfo.setOpenFileBtn("1");                /* 打开文件按钮 	 */
        permissionInfo.setSaveBtn("1");                    /* 保存文件按钮	 */
        permissionInfo.setPrintBtn("0");                /* 打印按钮		 */
        permissionInfo.setGoToPageBox("1");                /* 跳转指定页框	 */
        permissionInfo.setZoomPageBox("1");                /* 页面缩放		 */
        permissionInfo.setPageLayoutBtn("1");            /* 页面布局		 */
        permissionInfo.setHandToolBtn("1");                /* 手型工具		 */
        permissionInfo.setTextSelectBtn("1");            /* 选择文本工具 	 */
        permissionInfo.setHeightLightBtn("1");            /* 高亮			 */
        permissionInfo.setUnderlineBtn("1");            /* 下划线		 */
        permissionInfo.setPencilBtn("1");                /* 铅笔			 */
        permissionInfo.setDrawingAnnotBtn("1");            /* 其他标注		 */
        permissionInfo.setCommentsBtn("1");                /* 注释			 */
        permissionInfo.setElecSignatureBtn("1");        /* 签章			 */
        permissionInfo.setCheckElecSignatureBtn("1");    /* 验章			 */
        permissionInfo.setRotateSwitchBtn("1");            /* 页面旋转		 */
        permissionInfo.setExportBtn("1");                /* 下载文档		 */

        permissionInfo.setOutlineBtn("1");                /* 大纲			 */
        permissionInfo.setThumbnailBtn("1");            /* 缩略图		 */
        permissionInfo.setCommentListBtn("1");            /* 注释评论		 */
        permissionInfo.setSearchBtn("1");                /* 查找			 */
        permissionInfo.setSemanticTreeBtn("1");            /* 语义树		 */
        permissionInfo.setAttachmentBtn("1");            /* 附件			 */

        /* 阅读页数控制字段 */
//        permissionInfo.setPageRange("1,2,3-4");
        /* 阅读时间控制 */
        permissionInfo.setTimeModel(WebOFDTimeModelEnum.UNLIMITED_TIME.getTimeModel());
        /* 在线阅读起始时间 */
        //permissionInfo.setOnlineReadBeginTime("2021-04-08 15:30:00");
        /* 在线阅读结束时间 */
        //permissionInfo.setOnlineReadTime("2021-04-08 12:00:00");

        /* 隐写溯源三项 */
        permissionInfo.setIsImplicit("1");    /* 实际环境中,仅需控制这个开关即可，其他两个字段为保留字段供后期需求使用 */
        permissionInfo.setIsPrintImplicit("1");
        permissionInfo.setIsShowImplicit("1");

        /* 遮盖预遮盖 */
        permissionInfo.setAreaSelectBtn("1");        /* 区域选择工具开关-遮盖相关所有功能的开关 */
        permissionInfo.setCover("1");
        permissionInfo.setPreCover("1");


        return permissionInfo;
    }

    /**
     * @param ip IP of the client
     * @return The user inforamtion of the systemuser, in json format
     * <p>
     * http://localhost:8094/systemuser?ip=123
     * @cuiyj 这里目前没用到
     * Get user information of the systemuser, such as userID, userName, accessToken
     */
    @ResponseBody
    @RequestMapping(value = "/systemuser", method = RequestMethod.GET)
    public HashMap<String, Object> getSystemUserInfo(@RequestParam(value = "ip", required = false, defaultValue = "") String ip) {
        Pattern pattern = Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        String userId = pattern.matcher(ip).matches() ? ip : "123";

        /* FAKE DATA */
        String userName = "Guest";
        String accessToken = "accessToken";
        HashMap<String, Object> map = new HashMap<>();
        map.put("ret", 0);
        map.put("message", "success");

        HashMap<String, Object> mapData = new HashMap<>();
        mapData.put("userId", userId);
        mapData.put("userName", userName);
        mapData.put("accessToken", accessToken);
        map.put("data", mapData);
        return map;

    }

}
