package com.gjie.kgboot.api.client.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;


public class HttpBaseRequest<T> {

    /**
     * http请求头
     */
    protected HttpHeaders headers;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;


    /**
     * post请求参数
     */
    private T paramData;


    private Map<String,Object> urlVariables;

    /**
     * url
     */

    private String url;

    private String analysisRespCode;


    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeaders(HttpHeaders headers) {
        this.headers = headers;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public T getParamData() {
        return paramData;
    }

    public void setParamData(T paramData) {
        this.paramData = paramData;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAnalysisRespCode() {
        return analysisRespCode;
    }

    public void setAnalysisRespCode(String analysisRespCode) {
        this.analysisRespCode = analysisRespCode;
    }

    public Map<String, Object> getUrlVariables() {
        return urlVariables;
    }

    public void setUrlVariables(Map<String, Object> urlVariables) {
        this.urlVariables = urlVariables;
    }
}
