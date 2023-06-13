package com.gjie.kgboot.web.globalprocessor;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.common.constant.ErrorEnum;
import com.gjie.kgboot.common.exception.BaseException;
import com.gjie.kgboot.web.response.BaseWebResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;

@Component
@Aspect
public class WebControllerAspect {

    private final static String WEB_LOG = "WEB_LOG";

    Logger logger = LogManager.getLogger(WebControllerAspect.class);

    @Pointcut("execution (* com.gjie.kgboot.web.controller.*.*(..))")
    public void webGlobalPointCut() {

    }

    /**
     * controller异常封装、接口耗时统计
     * @param proceedingJoinPoint
     * @return
     */
    @Around("webGlobalPointCut()")
    public BaseWebResponse aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long start = System.currentTimeMillis();
        try {
            return (BaseWebResponse)proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            logger.error("访问接口失败,入参:" + JSON.toJSONString(proceedingJoinPoint.getArgs()), t);
            if (t instanceof BaseException) {
                throw (BaseException) t;
            }
            throw new BaseException(ErrorEnum.UN_KNOWN_ERROR);
        } finally {
            String path = StringUtils.EMPTY;
            try {
                path = parseControllerPath(proceedingJoinPoint);
                LogManager.getLogger(WEB_LOG).info(StringUtils.join("接口访问耗时:", path, ":", System.currentTimeMillis() - start));
            } catch (Exception e) {
                throw new BaseException(ErrorEnum.UN_KNOWN_ERROR);
            }
        }
    }


    private String parseControllerPath(ProceedingJoinPoint proceedingJoinPoint) {
        //获取类上的注解
        RequestMapping classRequestMapping = proceedingJoinPoint.getTarget().getClass().getAnnotation(RequestMapping.class);
        String requestPath = StringUtils.EMPTY;
        if (classRequestMapping != null) {
            requestPath += classRequestMapping.value()[0];
        }
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        //获取注解
        RequestMapping requestMappingAnnotation = method.getAnnotation(RequestMapping.class);
        GetMapping getMappingAnnotation = method.getAnnotation(GetMapping.class);
        PostMapping postMappingAnnotation = method.getAnnotation(PostMapping.class);
        String methodPath = null;
        if (requestMappingAnnotation != null) {
            methodPath = requestMappingAnnotation.value()[0];
        }
        if (methodPath == null && getMappingAnnotation != null) {
            methodPath = getMappingAnnotation.value()[0];
        }
        if (methodPath == null && postMappingAnnotation != null) {
            methodPath = postMappingAnnotation.value()[0];
        }
        return requestPath + methodPath;
    }


}
