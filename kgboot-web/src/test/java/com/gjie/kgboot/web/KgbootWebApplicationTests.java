package com.gjie.kgboot.web;

import com.gjie.kgboot.api.client.http.HttpApiClient;
import com.gjie.kgboot.dao.service.impl.OperateLogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class KgbootWebApplicationTests {




    @Autowired
    private OperateLogServiceImpl operateLogService;

    @Autowired
    private HttpApiClient httpApiClient;




}
