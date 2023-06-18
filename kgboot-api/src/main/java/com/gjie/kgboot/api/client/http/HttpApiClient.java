package com.gjie.kgboot.api.client.http;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.api.strategy.http.AbstractRespProcessor;
import com.gjie.kgboot.api.strategy.http.RespProcessorFactory;
import com.gjie.kgboot.common.constant.ErrorEnum;
import com.gjie.kgboot.common.exception.BaseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class HttpApiClient {

    public HttpApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private RestTemplate restTemplate;

    private static Logger logger = LogManager.getLogger("HTTP_API_LOG");

    public HttpBaseResponse getHttpResponse(HttpBaseRequest request) {
        String url = buildUrl(request.getUrl(), request.getUrlVariables());
        String param = request.getParamData() == null ? null :
                JSON.toJSONString(request.getParamData());
        //以string接收
        HttpEntity<String> httpEntity = new HttpEntity(param, request.getHeaders());
        ResponseEntity<String> response =
                null;
        try {
            response = restTemplate.exchange(url, request.getHttpMethod(), httpEntity, String.class);
        } catch (Exception e) {
            logger.error("请求外部接口失败:", e);
            throw new BaseException(ErrorEnum.PARAM_ERROR);
        } finally {
            logger.info(StringUtils.join("请求外部接口:" + url + "，请求:", param, "响应:", JSON.toJSONString(response)));
        }
        String body = response.getBody();
        //策略直接解析
        AbstractRespProcessor respProcessor = RespProcessorFactory.getRespProcessor(request.getAnalysisRespCode());
        if (respProcessor == null) {
            throw new BaseException(ErrorEnum.NO_EXISTS_HTTP_RESP_PROCESSOR);
        }
        HttpBaseResponse httpBaseResponse = respProcessor.analysisResp(body);
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
