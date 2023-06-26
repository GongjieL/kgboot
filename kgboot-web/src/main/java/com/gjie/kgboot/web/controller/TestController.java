package com.gjie.kgboot.web.controller;

import com.gjie.kgboot.api.client.http.HttpApiClient;
import com.gjie.kgboot.api.client.http.HttpBaseRequest;
import com.gjie.kgboot.api.client.http.HttpBaseResponse;
import com.gjie.kgboot.api.client.redis.CacheExecuteResult;
import com.gjie.kgboot.api.client.redis.KgBootRedisClient;
import com.gjie.kgboot.dao.mapper.boot.KgbootSessionMapper;
import com.gjie.kgboot.dao.service.KgbootSessionService;
import com.gjie.kgboot.dao.service.OperateLogService;
import com.gjie.kgboot.dao.service.impl.OperateLogServiceImpl;
import com.gjie.kgboot.web.response.BaseWebResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HttpApiClient httpApiClient;

//    @Autowired
//    private KafkaProducerClient kafkaProducerClient;

    @Resource
    private KgbootSessionService kgbootSessionService;

    @Resource
    private OperateLogService operateLogService;

    @Resource(name = "stringRedisTemplate")
    RedisTemplate redisTemplate;

    @Autowired
    private KgBootRedisClient kgBootRedisClient;

    @Autowired
    private KgbootSessionMapper kgbootSessionMapper;


    @GetMapping(value = "/abc")
    public BaseWebResponse<String> test(@RequestParam String abc) {
        Subject subject = SecurityUtils.getSubject();
        kgbootSessionService.list();
        UsernamePasswordToken token = new UsernamePasswordToken("zhangsan", "123456");
        subject.login(token);
        System.out.println(1);

//        ListenableFuture<CacheExecuteResult> future =
//                kgBootRedisClient.eliminateCache("jjj");
//        future.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
//            @Override
//            public void onFailure(Throwable ex) {
//                System.out.println("failure");
//            }
//
//            @Override
//            public void onSuccess(CacheExecuteResult result) {
//                System.out.println("success");
//            }
//        });
//
//        redisTemplate.opsForValue().get("");
////        kafkaProducerClient.sendMessageAsync("test-1",null,null,"test abc");
//        HttpBaseRequest<Map<String, String>> request = new HttpBaseRequest<>();
//        operateLogService.test();
//        request.setUrl("http://43.135.135.141:8080/openai/auth");
//        request.setHttpMethod(HttpMethod.GET);
//        Map<String, Object> data = new HashMap<>();
//        data.put("wd", "%E8%A7%A3%E6%9E%90");
//        request.setUrlVariables(data);
//        request.setAnalysisRespCode("common");
//        HttpBaseResponse<String> httpResponse = httpApiClient.getHttpResponse(request);

        return BaseWebResponse.<String>builder()
                .success(true)
                .data("hello world")
                .code(200)
                .build();
    }


    @GetMapping(value = "/bcd")
    public BaseWebResponse<String> test2(@RequestParam String abc) {
        Subject subject = SecurityUtils.getSubject();
        return null;
    }


}
