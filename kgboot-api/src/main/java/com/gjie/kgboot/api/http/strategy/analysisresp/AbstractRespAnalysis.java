package com.gjie.kgboot.api.http.strategy.analysisresp;

import com.gjie.kgboot.api.http.HttpBaseResponse;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract class AbstractRespAnalysis<T> implements BeanPostProcessor {

    public abstract String analysisCode();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        //注册
        RespAnalysisFactory.registerRespAnalysis(this);
        return bean;
    }


    public abstract HttpBaseResponse<T> analysisResp(String resp);


}
