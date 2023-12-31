package com.gjie.kgboot.web;

import com.gjie.kgboot.api.client.http.HttpApiClient;
import com.gjie.kgboot.api.client.kafka.KafkaProducerClient;
import com.gjie.kgboot.dao.service.impl.OperateLogServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KgbootWebApplicationTests {

    @Autowired
    private OperateLogServiceImpl operateLogService;

    @Autowired
    private HttpApiClient httpApiClient;

    @Autowired
    private KafkaProducerClient kafkaProducerClient;

    @Test
    void contextLoads() throws Exception {
        kafkaProducerClient.sendMessageAsync("hello-topic321",null,"0912","0912");
//        String test = operateLogService.test();
//        Logger logger = LogManager.getLogger(HttpApiClient.class);
//        logger.error("abc",new RuntimeException("解决计算机三级"));
//        HttpBaseRequest<Map<String,String>> request = new HttpBaseRequest<>();
//        request.setUrl("http://43.135.135.141:8080/openai/auth");
//        request.setHttpMethod(HttpMethod.GET);
//        Map<String, Object> data = new HashMap<>();
//        data.put("wd","%E8%A7%A3%E6%9E%90");
//        request.setUrlVariables(data);
//        request.setAnalysisRespCode("common2");
//        HttpBaseResponse<String> httpResponse = httpApiClient.getHttpResponse(request);
//        OperateLog operateLog = operateLogService.getById(1);
//        System.out.println(operateLog);
    }

}
