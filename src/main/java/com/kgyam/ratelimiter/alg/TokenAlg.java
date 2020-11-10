package com.kgyam.ratelimiter.alg;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 令牌桶
 */
public class TokenAlg implements LimitAlg {

    private final int limit;
    /*
    往bucket存放令牌得速率
     */
    private long permit;
    private Queue<Integer> bucket;
    private ScheduledExecutorService pool;

    public TokenAlg(int limit, long permit) {
        this.limit = limit;
        this.permit = permit;
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
        for (int i = 0; i < limit; i++) {
            bucket.offer(1);
        }

        pool = Executors.newScheduledThreadPool(10);
        pool.scheduleAtFixedRate(() -> {
            boolean _flag = bucket.offer(1);
        }, 1000, permit, TimeUnit.MILLISECONDS);
    }


    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        return bucket.poll() == null ? false : true;
    }
}
