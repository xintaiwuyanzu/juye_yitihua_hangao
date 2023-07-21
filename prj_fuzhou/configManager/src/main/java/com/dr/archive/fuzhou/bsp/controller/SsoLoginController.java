package com.dr.archive.fuzhou.bsp.controller;

import cn.dustlight.captcha.annotations.CodeValue;
import com.dr.archive.fuzhou.bsp.autoconfig.BspConfig;
import com.dr.archive.fuzhou.bsp.loginRecord.service.LoginRecordService;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.organise.query.PersonQuery;
import com.dr.framework.core.organise.service.LoginService;
import com.dr.framework.core.organise.service.OrganisePersonService;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.bo.ClientInfo;
import com.dr.framework.core.web.annotations.Current;
import com.dr.framework.sys.controller.LogController;
import com.dr.framework.sys.entity.Log;
import com.dr.framework.sys.service.SysAdminService;
import com.inspur.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;

/**
 * 这里是浪潮sso登录相关的代码
 *
 * @Author: caor
 * @Date: 2021-12-06 11:48
 * @Description:
 */
@RestController
@RequestMapping({"api/ssoLogin"})
public class SsoLoginController {
    @Autowired
    BspConfig bspConfig;
    @Autowired
    OrganisePersonService organisePersonService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    LoginService loginService;
    @Autowired
    SysAdminService adminService;
    @Autowired
    LogController logAdmin;
    @Value("${server.session.timeout:30m}")
    Duration timeout;
    @Autowired
    LoginRecordService loginRecordService;
    /**
     * 根据浪潮sso token校验登录信息
     *
     * @return
     * @throws Exception
     */
    @PostMapping("validate")
    public ResultEntity<String> validate(@RequestParam String username,
                                         @RequestParam String password,
                                         @RequestParam(defaultValue = LoginService.LOGIN_TYPE_DEFAULT) String loginType,
                                         @Current ClientInfo clientInfo,
                                         @CodeValue String code,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {
        try {
            Person person = loginService.login(username, password, loginType, clientInfo.getRemoteIp());

            //将用户的id存入登录记录表中，如果存在则不存， 但都需要修改登录状态
            loginRecordService.insertPerson(person);

            String token = loginService.auth(person);
            Cookie cookie = new Cookie(SecurityHolder.TOKEN_HEADER_KEY, token);
            response.addHeader(SecurityHolder.TOKEN_HEADER_KEY, token);
            //设置超时时间为2小时
            String path = request.getContextPath();
            if (!StringUtils.hasText(path)) {
                path = "/";
            }
            cookie.setMaxAge((int) timeout.getSeconds());
            cookie.setPath(path);
            cookie.setHttpOnly(true);
            if (StringUtils.hasText(clientInfo.getRemoteIp())) {
                //异常排除
                cookie.setDomain(clientInfo.getRemoteIp());
            }
            response.addCookie(cookie);
            return ResultEntity.success(token);
        } catch (Exception e) {
            return ResultEntity.error("用户名或密码错误");
        }
    }

    /**
     * 获取门户访问地址
     *
     * @return
     */
    @PostMapping("getPortalUrl")
    public ResultEntity<String> getPortalUrl() {
        Map<String, String> portalAppInfo = applicationService.findAppinfoByKey(bspConfig.getPortalCode());
        if (portalAppInfo != null) {
            return ResultEntity.success(portalAppInfo.get("URL"));
        }
        return ResultEntity.error("查询失败");
    }

    /**
     * 福州项目修改密码 TODO 只能修改我们系统创建的账号，除非从前台传 登陆方式参数
     *
     * @param person
     * @param request
     * @param oldPwd
     * @param newPwd
     * @param adminId 前台加密后的密码
     * @return
     */
    @PostMapping({"/changePassword"})
    public ResultEntity<String> changePassword(@Current Person person, HttpServletRequest request, String oldPwd, String newPwd, String adminId) {
        changePassword(person.getId(), oldPwd, newPwd, adminId);
        ResultEntity res = ResultEntity.success();
        Log log = new Log(person.getUserCode(), "管理员修改密码", person.getUserCode(), person.getUserName() + "修改密码", "-", res.getCode(), "SYS_person", "Person", "changePassword");
        log.setCreateDate(System.currentTimeMillis());
        log.setCreatePerson(person.getUserCode());
        log.setId(UUID.randomUUID().toString());
        this.logAdmin.insert(request, log);
        return res;
    }

    @PostMapping("validateClient")
    public ResultEntity<String> validateClient(String userId) throws Exception {
        Assert.isTrue(StringUtils.hasText(userId), "未找到用户Id");
        Person person = organisePersonService.getPerson(new PersonQuery.Builder().idEqual(userId).build());
        Assert.isTrue(person != null, "未找到指定的用户");
        String authToken = loginService.auth(person);
        //TODO 这里应该尝试同步一下用户信息
        return ResultEntity.success(authToken);
    }

    public void changePassword(String personId, String oldPwd, String newPwd, String adminId) {
        Assert.isTrue(StringUtils.hasText(personId), "管理员记录号不能为空！");
        Assert.isTrue(StringUtils.hasText(oldPwd), "管理员密码不能为空！");
        Assert.isTrue(StringUtils.hasText(newPwd), "新密码不能为空！");
        Person person = this.organisePersonService.getPerson((PersonQuery) ((PersonQuery.Builder) (new PersonQuery.Builder()).idEqual(new String[]{personId})).build());
        Assert.notNull(person, "该管理员不存在");
        this.loginService.login(adminId, oldPwd);
        this.loginService.changePassword(personId, newPwd, new String[0]);
    }
}