package com.dr.archive.fuzhou.portal.service.impl;

import com.dr.framework.common.dao.CommonMapper;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.sys.service.DefaultLoginTokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: caor
 * @Date: 2022-06-21 18:48
 * @Description:
 */
@Component
public class ApiTokenHandler extends DefaultLoginTokenHandler {
    @Autowired
    private CommonMapper commonMapper;

    @Override
    public String auth(Person person) {
        if (isApiClient()) {
            return person.getId();
        }
        return super.auth(person);
    }

    @Override
    public Person deAuth(String token) {
        if (isApiClient()) {
            return commonMapper.selectById(Person.class, token);
        }
        return super.deAuth(token);
    }

    private boolean isApiClient() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String isClient = request.getHeader("$apiClient");
            if (StringUtils.hasText(isClient)) {
                return "true".equalsIgnoreCase(isClient);
            }
        }
        return false;
    }

    HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
            return request;
        } else {
            return null;
        }
    }
}
