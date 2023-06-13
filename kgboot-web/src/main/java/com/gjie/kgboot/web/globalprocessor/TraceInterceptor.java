package com.gjie.kgboot.web.globalprocessor;

import com.gjie.kgboot.common.constant.CommonConstants;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * traceId拦截器
 */
public class TraceInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // "traceId"
        MDC.put(CommonConstants.LOG_TRACE_ID, UUID.randomUUID().toString());
        return true;
    }
}