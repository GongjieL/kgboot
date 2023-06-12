package com.gjie.kgboot.api.http.strategy.analysisresp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gjie.kgboot.api.http.HttpBaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CommonRespAnalysis extends AbstractRespAnalysis<String> {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String analysisCode() {
        return "common";
    }

    @Override
    public HttpBaseResponse<String> analysisResp(String respStr) {
        JSONObject jsonObject = JSON.parseObject(respStr);
        HttpBaseResponse<String> resp = new HttpBaseResponse<>();
        resp.setData(jsonObject.getString("data"));
        return resp;
    }
}
