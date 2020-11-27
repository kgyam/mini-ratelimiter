package com.kgyam.throttler.alg;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶
 */
public class TokenBucketAlg implements LimitAlg {

    /*
    允许通过的请求数，可理解为令牌桶里面的最大令牌数
     */
    private final int limit;
    /*
    令牌桶生成速率
     */
    private long permit;
    /*
    令牌桶
     */
    private Queue<Integer> bucket;
    private boolean nodelay;
    private ScheduledExecutorService pool;

    public TokenBucketAlg(int limit, long permit, boolean nodelay) {
        this.limit = limit;
        this.permit = permit;
        this.nodelay = nodelay;
        init();
    }


    private void init() {
        if (limit <= 0) {
            return;
        }
        if (permit <= 0) {
            return;
        }
        bucket = new ArrayBlockingQueue<>(limit, false);

        //无需延迟生成令牌，初始化马上生成好所有令牌
        if (nodelay) {
            for (int i = 0; i < limit; i++) {
                bucket.offer(1);
            }
        }

        pool = Executors.newScheduledThreadPool(10);
        pool.scheduleAtFixedRate(() -> {
            bucket.offer(1);
        }, 1000, permit, TimeUnit.MILLISECONDS);
    }


    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        return bucket.poll() == null ? false : true;
    }
}
