package com.gjie.kgboot.web;

import com.gjie.kgboot.api.http.HttpApiClient;
import com.gjie.kgboot.api.http.HttpBaseRequest;
import com.gjie.kgboot.api.http.HttpBaseResponse;
import com.gjie.kgboot.dao.domain.OperateLog;
import com.gjie.kgboot.dao.service.impl.OperateLogServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class KgbootWebApplicationTests {

    @Autowired
    private OperateLogServiceImpl operateLogService;

    @Autowired
    private HttpApiClient httpApiClient;

    @Test
    void contextLoads() {
        String test = operateLogService.test();
        Logger logger = LogManager.getLogger(HttpApiClient.class);
        logger.error("abc",new RuntimeException("解决计算机三级"));
        HttpBaseRequest<Map<String,String>> request = new HttpBaseRequest<>();
        request.setUrl("http://43.135.135.141:8080/openai/auth");
        request.setHttpMethod(HttpMethod.GET);
        Map<String, Object> data = new HashMap<>();
        data.put("wd","%E8%A7%A3%E6%9E%90");
        request.setUrlVariables(data);
        request.setAnalysisRespCode("common2");
        HttpBaseResponse<String> httpResponse = httpApiClient.getHttpResponse(request);
        OperateLog operateLog = operateLogService.getById(1);
        System.out.println(operateLog);
    }

}
