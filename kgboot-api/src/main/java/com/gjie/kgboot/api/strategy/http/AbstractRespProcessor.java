package com.gjie.kgboot.api.strategy.http;

import com.gjie.kgboot.api.http.HttpBaseResponse;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractRespProcessor<T> implements InitializingBean {



    public abstract String processorCode();


    @Override
    public void afterPropertiesSet() throws Exception {
        RespProcessorFactory.registerRespProcessor(this);
    }

    public abstract HttpBaseResponse<T> analysisResp(String resp);


}
