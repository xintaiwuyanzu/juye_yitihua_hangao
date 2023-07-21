package com.dr.archive.manage.log.aop;

import com.dr.archive.manage.log.annotation.SysLog;
import com.dr.archive.manage.log.entity.SysLogEntity;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.HttpContextUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Organise;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.security.SecurityHolder;
import com.dr.framework.core.security.bo.ClientInfo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author caor
 * @date 2020/8/12 11:06
 */
@Aspect
@Component
public class SysLogAspect {
    @Autowired
    private CommonMapper commonMapper;

    @Pointcut("@annotation( com.dr.archive.manage.log.annotation.SysLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveSysLog(JoinPoint joinPoint) {
        //保存日志
        SysLogEntity sysLogEntity = new SysLogEntity();
        sysLogEntity.setId(UUIDUtils.getUUID());
        sysLogEntity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        SysLog myLog = method.getAnnotation(SysLog.class);
        if (myLog != null) {
            String value = myLog.value();
            //保存获取的操作
            sysLogEntity.setOperation(value);
        }
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        sysLogEntity.setMethod(className + "." + methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        sysLogEntity.setCreateDate(DateTimeUtils.getMillis());

        //获取用户ip地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //获取用户名
        ClientInfo clientInfo = BaseController.getClientInfo(request);
        if (clientInfo != null) {
            Person person = clientInfo.getPerson();
            if (person != null) {
                sysLogEntity.setCreatePerson(person.getUserCode());
            }
            sysLogEntity.setIp(clientInfo.getRemoteIp());
        }
        commonMapper.insert(sysLogEntity);
    }
}
