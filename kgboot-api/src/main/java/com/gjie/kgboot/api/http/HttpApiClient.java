package com.gjie.kgboot.api.http;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.api.http.strategy.analysisresp.AbstractRespAnalysis;
import com.gjie.kgboot.api.http.strategy.analysisresp.RespAnalysisFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class HttpApiClient<Req, Resp> {
    @Autowired
    private RestTemplate restTemplate;


    public HttpBaseResponse<Resp> getHttpResponse(HttpBaseRequest<Req> request) {
        String url = buildUrl(request.getUrl(), request.getUrlVariables());
        String param = request.getParamData() == null ? null :
                JSON.toJSONString(request.getParamData());
        //以string接收
        HttpEntity<String> httpEntity = new HttpEntity(param, request.getHeaders());
        ResponseEntity<String> response =
                restTemplate.exchange(url, request.getHttpMethod(), httpEntity, String.class);
        String body = response.getBody();
        //策略直接解析
        AbstractRespAnalysis respAnalysis = RespAnalysisFactory.getRespAnalysis(request.getAnalysisRespCode());
        HttpBaseResponse<Resp> httpBaseResponse = respAnalysis.analysisResp(body);
        return httpBaseResponse;
    }


    /**
     * 拼接url参数
     *
     * @param url
     * @param urlVariables
     * @return
     */
    private String buildUrl(String url, Map<String, Object> urlVariables) {
        if (CollectionUtils.isEmpty(urlVariables)) {
            return url;
        }
        final String[] variables = {""};
        urlVariables.forEach((k, v) -> {
            variables[0] += StringUtils.join(k, "=", v);
        });
        return StringUtils.join(url, "?", variables[0]);
    }


}
