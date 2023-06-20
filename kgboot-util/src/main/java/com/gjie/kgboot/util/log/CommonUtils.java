package com.gjie.kgboot.util.log;

import com.alibaba.fastjson.JSON;
import com.gjie.kgboot.common.constant.ErrorEnum;
import com.gjie.kgboot.common.exception.BaseException;
import com.gjie.kgboot.util.log.bo.BaseTask;

import java.util.Arrays;
import java.util.concurrent.DelayQueue;
import java.util.function.Function;

public class CommonUtils {
    public static <M, N> Runnable retry(Integer num, Long initDelay, Function<M, N> function, M m, N... expectedResults) {
        if (expectedResults == null) {
            throw new BaseException("重试期望值不可为空");
        }
        DelayQueue<BaseTask<M, N>> baseTasks = new DelayQueue<>();
        if (num == 0) {
            num = 5;
        }
        if (initDelay < 0) {
            initDelay = 0l;
        }
        for (int i = 0; i < num; i++) {
            BaseTask<M, N> task = new BaseTask<M, N>("task" + i, initDelay);
            if (i < 5) {
                initDelay = initDelay < 200 ? 200 : initDelay + initDelay * 2;
            } else {
                initDelay += 1000;
            }
            baseTasks.add(task);
        }
        //执行
        return () -> {
            Exception exception = null;
            while (!baseTasks.isEmpty()) {
                try {
                    baseTasks.take();
                    N apply = function.apply(m);
                    if (Arrays.asList(expectedResults).contains(apply)) {
                        return;
                    }
                } catch (Exception e) {
                    exception = e;
                }
            }
            if (exception != null) {
                throw new BaseException(ErrorEnum.METHOD_EXECUTE_ERROR, exception.getMessage());
            }
            throw new BaseException(ErrorEnum.METHOD_EXECUTE_ERROR, "执行不满足期望值:" + JSON.toJSONString(Arrays.asList(
                    expectedResults)));
        };
    }
}
