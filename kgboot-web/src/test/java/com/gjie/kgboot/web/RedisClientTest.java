package com.gjie.kgboot.web;

import com.gjie.kgboot.api.client.redis.CacheExecuteResult;
import com.gjie.kgboot.api.client.redis.KgBootRedisClient;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@SpringBootTest
public class RedisClientTest {
    @Mock
    private RedisTemplate redisTemplate;

    @InjectMocks
    private KgBootRedisClient kgBootRedisClient;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;


    @Before
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * 成功/失败情况
     */
    @Test
    public void testDelayDelete() {
        Mockito.when(redisTemplate.delete("success")).thenReturn(true);
        Mockito.when(redisTemplate.delete("error")).thenThrow(new RuntimeException());
        kgBootRedisClient.setThreadPoolTaskExecutor(threadPoolTaskExecutor);
        kgBootRedisClient.setDelayDeleteTime(200l);

        //成功情况
        ListenableFuture<CacheExecuteResult> future =
                kgBootRedisClient.eliminateCache("success");
        try {
            future.get();
        } catch (Exception e) {
        }
        final Boolean[] successFlags = {false, false};
        future.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
            @Override
            public void onFailure(Throwable ex) {
                successFlags[0] = false;
            }

            @Override
            public void onSuccess(CacheExecuteResult result) {
                successFlags[0] = true;
            }
        });
        //失败情况
        ListenableFuture<CacheExecuteResult> failureFuture =
                kgBootRedisClient.eliminateCache("failure");
        try {
            future.get();
        } catch (Exception e) {
        }
        failureFuture.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
            @Override
            public void onFailure(Throwable ex) {
                successFlags[1] = false;
            }

            @Override
            public void onSuccess(CacheExecuteResult result) {
                successFlags[1] = true;
            }
        });
        //校验成功
        Assert.assertEquals(successFlags[0], true);
        //校验失败
        Assert.assertEquals(successFlags[1], false);

    }


    @Test
    public void testRandomDelete() {
        Mockito.when(redisTemplate.delete("success")).thenAnswer((new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                return randomExecFailure(true);
            }
        }));

        kgBootRedisClient.setThreadPoolTaskExecutor(threadPoolTaskExecutor);
        kgBootRedisClient.setDelayDeleteTime(200l);

        //成功情况
        ListenableFuture<CacheExecuteResult> future =
                kgBootRedisClient.eliminateCache("success");
        try {
            future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Boolean[] successFlags = {false};
        future.addCallback(new ListenableFutureCallback<CacheExecuteResult>() {
            @Override
            public void onFailure(Throwable ex) {
                successFlags[0] = false;
            }

            @Override
            public void onSuccess(CacheExecuteResult result) {
                successFlags[0] = true;
            }
        });

    }


    private Boolean randomExecFailure(Boolean firstExec) {
        int t = 1;
        if (!firstExec) {
            t = 2;
        }
        System.out.println(String.format("第%d次执行方法", t));
        int i = RandomUtils.nextInt(0, 10);
        if (i % 3 != 0) {
            System.out.println(String.format("第%d次执行方法失败", t));
            t = i / 0;
        }
        System.out.println(String.format("第%d次执行方法成功", t));

        return i >= 0;
    }
}
