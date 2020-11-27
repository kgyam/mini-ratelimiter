package com.kgyam.throttler.alg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 固定窗口
 * <p>
 * 存在的问题：
 * 1.精度问题：假如窗口时间是1min，请求限制数是100r。
 * 系统在0:55时候接入100r，1:00之前的请求会掐断。
 * 关键到1:01再次冲入100r，这些请求是可以进入到系统的。
 * 在0:50到1:50时间窗口其实系统就接入了200个request
 *
 */
public class FixedAlg implements LimitAlg {
    private static final long TRY_LOCK_TIMEOUT = 200L;  // 200ms.
    private AtomicInteger counter = new AtomicInteger(0);
    /*
    限流数
     */
    private final int limit;
    private final Lock lock;
    /*
    时间窗口
     */
    private int interval;
    private long lastReqTime;
    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    public FixedAlg(int limit, int interval) {
        this.limit = limit;
        this.interval = interval;
        lock = new ReentrantLock();
    }

    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        if (lastReqTime == 0L) {
            lastReqTime = System.currentTimeMillis();
        }
        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                //判断是否已经跨越时间窗口
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastReqTime > timeUnit.toMillis(interval)) {
                    counter.set(0);
                    lastReqTime = currentTime;
                }
            }
            int update = counter.incrementAndGet();
            return update <= limit;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }
}
