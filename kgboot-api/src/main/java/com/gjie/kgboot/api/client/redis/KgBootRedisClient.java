package com.gjie.kgboot.api.client.redis;

import com.gjie.kgboot.util.log.CommonUtils;
import com.gjie.kgboot.util.log.bo.BaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.DelayQueue;
import java.util.function.Function;

public class KgBootRedisClient {
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate redisTemplate;

    private Long delayDeleteTime;

    public void setDelayDeleteTime(Long delayDeleteTime) {
        this.delayDeleteTime = delayDeleteTime;
    }

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    {
//        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
//        threadPoolTaskExecutor.initialize();
//    }


//    private Boolean testMethod(Boolean firstExec) {
//        int t = 1;
//        if (!firstExec) {
//            t = 2;
//        }
//        System.out.println(String.format("第%d次执行方法", t));
//        int i = RandomUtils.nextInt(0, 10);
//        if (i % 3 != 0) {
//            System.out.println(String.format("第%d次执行方法失败", t));
//            t = i / 0;
//        }
//        System.out.println(String.format("第%d次执行方法成功", t));
//
//        return i >= 0;
//    }

    /**
     * 淘汰缓存
     *
     * @param key
     * @return
     */
    public ListenableFuture<CacheExecuteResult> eliminateCache(String key) {
        return eliminateCache(key, true, null);
    }

    /**
     * 缓存删除重试
     *
     * @param key
     * @param future
     */
    private void retryEliminateCache(String key, SettableListenableFuture<CacheExecuteResult> future) {
        //执行方法
        Function<String, Boolean> function = new Function<String, Boolean>() {
            @Override
            public Boolean apply(String key) {
                return redisTemplate.delete(key);
            }
        };
        //调用重试方法
        ListenableFuture<?> listenableFuture = threadPoolTaskExecutor.submitListenable(
                CommonUtils.retry(3, delayDeleteTime, function, key, true, false));
        //回调
        listenableFuture.addCallback(new ListenableFutureCallback<Object>() {
            @Override
            public void onFailure(Throwable ex) {
                //失败返回删除失败,用户可在onFailure捕获到异常
                future.setException(ex);
            }

            @Override
            public void onSuccess(Object result) {
                //成功就ok还需要后续延迟删除
                future.set(new CacheExecuteResult(true));
            }
        });
    }


    private ListenableFuture<CacheExecuteResult> eliminateCache(String key, Boolean firstDel,
                                                                SettableListenableFuture<CacheExecuteResult> future) {
        if (future == null) {
            future = new SettableListenableFuture<>();
        }
        //直接线程池执行
        ListenableFuture<CacheExecuteResult> commonExecute = threadPoolTaskExecutor.submitListenable(new Callable<CacheExecuteResult>() {
            @Override
            public CacheExecuteResult call() throws Exception {
                redisTemplate.delete(key);
                return new CacheExecuteResult(true);
            }
        });
        //针对非重试的监听
        SettableListenableFuture<CacheExecuteResult> finalFuture = future;
        commonExecute.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
            @Override
            public void onFailure(Throwable ex) {
                //失败重试
                retryEliminateCache(key, finalFuture);
                //第一次直接返回
                if (firstDel) {
                    return;
                }
                //延迟删除的需要针对重试进行监听
                finalFuture.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        finalFuture.setException(ex);
                    }

                    @Override
                    public void onSuccess(CacheExecuteResult result) {
                        finalFuture.set(result);
                    }
                });
            }

            @Override
            public void onSuccess(CacheExecuteResult result) {
                //第二次执行成功需设置成功标志位
                if (firstDel) {
                    try {
                        DelayQueue<BaseTask> delayQueue = new DelayQueue<>();
                        delayQueue.add(new BaseTask("delayDel", delayDeleteTime));
                        delayQueue.take();
                        eliminateCache(key, false, finalFuture);
                    } catch (Exception e) {
                        finalFuture.setException(e);
                        finalFuture.set(null);
                    }
                } else {
                    finalFuture.set(new CacheExecuteResult(true));
                }
            }
        });
        return finalFuture;
    }
}
