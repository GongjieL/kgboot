package com.gjie.kgboot.api.client.redis;

import com.gjie.kgboot.util.log.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.SettableListenableFuture;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class KgBootRedisClient {
    @Resource(name = "stringRedisTemplate")
    private RedisTemplate redisTemplate;


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    /**
     * 缓存删除重试
     *
     * @param key
     * @param future
     */
    private void retryEliminateCache(String key, SettableListenableFuture<CacheExecuteResult> future) {
        CommonUtils<String, Boolean> commonUtils = new CommonUtils<>();
        //执行方法
        Function<String, Boolean> function = new Function<String, Boolean>() {
            @Override
            public Boolean apply(String key) {
                return redisTemplate.delete(key);
            }
        };
        threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        //执行
        ListenableFuture<?> listenableFuture = threadPoolTaskExecutor.submitListenable(
                commonUtils.retry(3, 200l, function, key, true));
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


    private ListenableFuture<CacheExecuteResult> eliminateCache(String key, Boolean delayDel) {
        //每次进来是新的
        SettableListenableFuture<CacheExecuteResult> future = new SettableListenableFuture<>();
        //首先删除
        Boolean delete = null;
        try {
            if (delayDel) {
//                delete = redisTemplate.delete(key);
                Thread.sleep(2000);
            } else {
                Thread.sleep(3000);

            }
        } catch (Exception e) {
            //异常重试，不管失败还是成功，直接返回
            retryEliminateCache(key, future);
            //如果第一次删除，需要设置失败
            if (delayDel) {
                future.addCallback(new ListenableFutureCallback<Object>() {
                    @Override
                    public void onFailure(Throwable ex) {
                        //失败返回删除失败,用户可在onFailure捕获到异常
                        future.setException(ex);
                    }

                    @Override
                    public void onSuccess(Object result) {
                        future.set(new CacheExecuteResult(true));
                        //理论上读操作小于重试的最小时间，重试不用延迟删除了
//                        eliminateCache(key, false);
                    }
                });
            }
            return future;
        }
        //如果第一次成功，需要延迟删除
        if (delayDel) {
            System.out.println("第一次成功");
            //延迟操作
            return eliminateCache(key, false);
        }
        future.set(new CacheExecuteResult(true));
        return future;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
//        SettableListenableFuture<CacheExecuteResult> future = new SettableListenableFuture<>();
        ListenableFuture<CacheExecuteResult> future = new KgBootRedisClient().eliminateCache("", true);
        System.out.println(System.currentTimeMillis() - start);
        future.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("cuowu:" + ex.getMessage());
            }

            @Override
            public void onSuccess(CacheExecuteResult result) {
                System.out.println("success");
            }
        });
    }
}
