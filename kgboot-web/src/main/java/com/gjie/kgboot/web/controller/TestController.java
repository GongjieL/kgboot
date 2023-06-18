package com.gjie.kgboot.web.controller;

import com.gjie.kgboot.api.client.http.HttpApiClient;
import com.gjie.kgboot.api.client.http.HttpBaseRequest;
import com.gjie.kgboot.api.client.http.HttpBaseResponse;
import com.gjie.kgboot.api.client.kafka.KafkaProducerClient;
import com.gjie.kgboot.dao.service.impl.OperateLogServiceImpl;
import com.gjie.kgboot.web.response.BaseWebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private HttpApiClient httpApiClient;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Autowired
    private OperateLogServiceImpl operateLogService;


    @GetMapping(value = "/abc")
    public BaseWebResponse<String> test(@RequestParam String abc) {
        kafkaProducerClient.sendMessageAsync("test-1",null,null,"test abc");
        HttpBaseRequest<Map<String, String>> request = new HttpBaseRequest<>();
        operateLogService.test();
        request.setUrl("http://43.135.135.141:8080/openai/auth");
        request.setHttpMethod(HttpMethod.GET);
        Map<String, Object> data = new HashMap<>();
        data.put("wd", "%E8%A7%A3%E6%9E%90");
        request.setUrlVariables(data);
        request.setAnalysisRespCode("common");
        HttpBaseResponse<String> httpResponse = httpApiClient.getHttpResponse(request);

        return BaseWebResponse.<String>builder()
                .success(true)
                .data("hello world")
                .code(200)
                .build();
    }


}
