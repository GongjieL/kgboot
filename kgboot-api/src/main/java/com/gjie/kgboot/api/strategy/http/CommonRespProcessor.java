package com.gjie.kgboot.api.strategy.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.gjie.kgboot.api.client.http.HttpBaseResponse;

public class CommonRespProcessor extends AbstractRespProcessor<String> {
    @Override
    public String processorCode() {
        return "common";
    }

    @Override
    public HttpBaseResponse<String> analysisResp(String respStr) {
        return JSON.parseObject(respStr, new TypeReference<HttpBaseResponse<String>>() {
        });
    }
}
