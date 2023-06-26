package com.gjie.kgboot.web.globalprocessor;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.common.constant.ErrorEnum;
import com.gjie.kgboot.common.exception.BaseException;
import com.gjie.kgboot.web.response.BaseWebResponse;
import com.sun.xml.internal.ws.client.ResponseContext;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import org.apache.catalina.session.StandardSession;
import org.apache.catalina.session.StandardSessionFacade;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
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
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("webGlobalPointCut()")
    public BaseWebResponse aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        long start = System.currentTimeMillis();
        try {
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            boolean admin = subject.hasRole("admin");


//            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//            //设置cookie
//            //获取session
//            StandardSessionFacade session = (StandardSessionFacade) requestAttributes.getSessionMutex();
//            //获取session中的userId
//            Object user = session.getAttribute("user");
//            //session中的user和传的userId比较
//            if (user != null && !user.equals(proceedingJoinPoint.getArgs()[0])) {
//                System.out.println("越权访问");
//            }

            return (BaseWebResponse) proceedingJoinPoint.proceed();
        } catch (Throwable t) {
            t.printStackTrace();
            logger.error("访问接口失败,入参:" + JSON.toJSONString(proceedingJoinPoint.getArgs()), t);
            if (t instanceof BaseException) {
                throw (BaseException) t;
            }
            throw new BaseException(ErrorEnum.UN_KNOWN_ERROR);
        } finally {
            String path = StringUtils.EMPTY;
            try {
                path = parseControllerPath(proceedingJoinPoint);
                LogManager.getLogger(WEB_LOG).info(StringUtils.join("接口路径:", path, "，耗时:", System.currentTimeMillis() - start, "ms"));
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
