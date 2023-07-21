package com.dr.archive.common.dzdacqbc.aop;

import com.dr.archive.common.dzdacqbc.annotation.CqbcLog;
import com.dr.archive.common.dzdacqbc.entity.CqbcLogEntity;
import com.dr.archive.util.DateTimeUtils;
import com.dr.archive.util.HttpContextUtils;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.controller.BaseController;
import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Person;
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
 * @author hyj
 */
@Aspect
@Component
public class CqbcLogAspect {
    @Autowired
    private CommonMapper commonMapper;

    @Pointcut("@annotation( com.dr.archive.common.dzdacqbc.annotation.CqbcLog)")
    public void logPoinCut() {
    }

    //切面 配置通知
    @AfterReturning("logPoinCut()")
    public void saveCqbcLog(JoinPoint joinPoint) {
        //保存日志
        CqbcLogEntity cqbcLogEntity = new CqbcLogEntity();
        cqbcLogEntity.setId(UUIDUtils.getUUID());
        //从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        //获取操作
        CqbcLog myLog = method.getAnnotation(CqbcLog.class);
        if (myLog != null) {
            String value = myLog.value();
            //保存获取的操作
            cqbcLogEntity.setContent(value);
        }
        //获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        //获取请求的方法名
        String methodName = method.getName();
        cqbcLogEntity.setMethod(className + "." + methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        cqbcLogEntity.setCreateDate(DateTimeUtils.getMillis());
        cqbcLogEntity.setOperateDate(DateTimeUtils.getMillis().toString());

        //获取用户ip地址
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        //获取用户名
        ClientInfo clientInfo = BaseController.getClientInfo(request);
        if (clientInfo != null) {
            Person person = clientInfo.getPerson();
            if (person != null) {
                cqbcLogEntity.setCreatePerson(person.getUserCode());
                cqbcLogEntity.setOperatePerson(person.getUserName());
            }
            cqbcLogEntity.setIp(clientInfo.getRemoteIp());
        }
        commonMapper.insert(cqbcLogEntity);
    }
}
