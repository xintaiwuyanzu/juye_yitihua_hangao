package com.dr.archive.common.dict;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.sys.entity.SysDict;
import com.dr.framework.sys.entity.SysDictInfo;
import com.mchange.v1.util.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@Aspect
public class SysDictControllerAspect {
    @Autowired
    CommonMapper commonMapper;

    @Pointcut("execution(* com.dr.framework.common.controller..*(..))")
    public void executeController() {
    }

    /**
     * 配置环绕通知解决编码重复报错问题
     *
     * @param pjd
     * @return
     */
    @Around("executeController()")
    public Object aroudMethod(ProceedingJoinPoint pjd) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Object result = null;
        try {
            boolean flag = true;
            //前置通知执行时根据访问地址判断是不是字典项添加，如果不是直接放行
            if (request.getRequestURI().equals("/api/sysDict/insert")) {
                String[] parameterNames = ((CodeSignature) pjd.getSignature()).getParameterNames();
                int paramIndex = ArrayUtils.indexOf(parameterNames, "entity");
                SysDict entity = (SysDict) pjd.getArgs()[paramIndex];
                //查询字典表中是否存在相同编码
                List<SysDict> dict = commonMapper.selectByQuery(SqlQuery.from(SysDict.class).equal(SysDictInfo.KEYINFO, entity.getKey()));
                if (dict.size() > 0) {
                    flag = false;
                    result = ResultEntity.error("已存在相同编码");
                }
            }
            //如果是其他方法或者字典表中不存在相同编码则放行
            if (flag) {
                result = pjd.proceed();
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
