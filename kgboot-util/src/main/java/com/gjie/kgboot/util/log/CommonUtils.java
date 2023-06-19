package com.gjie.kgboot.util.log;

import com.gjie.kgboot.util.log.bo.BaseTask;

import java.util.concurrent.DelayQueue;
import java.util.function.Function;

public class CommonUtils<M, N> {
    public Runnable retry(Integer num, Long initDelay, Function<M, N> function, M m, N expectedResult) {
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
            while (!baseTasks.isEmpty()) {
                try {
                    baseTasks.take();
                    N apply = function.apply(m);
                    if (expectedResult.equals(apply)) {
                        return;
                    }
                } catch (Exception e) {
                    //todo 记录错误日志
                    e.printStackTrace();
                }
            }
            throw new RuntimeException("重试失败");
        };
    }
}
