package com.kgyam.ratelimiter.alg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixedAlg implements LimitAlg {
    private static final long TRY_LOCK_TIMEOUT = 200L;  // 200ms.
    private AtomicInteger counter = new AtomicInteger(0);
    private final int limit;
    private Lock lock;
    private long gap;
    private long time;

    public FixedAlg(int limit) {
        this.limit = limit;
        this.lock = new ReentrantLock();
    }

    public boolean tryAcquire() {
        if (limit <= 0) {
            return true;
        }
        if (time == 0L) {
            time = System.currentTimeMillis();
        }
        int _update = counter.incrementAndGet();
        if (_update <= limit) {
            return true;
        }

        try {
            if (lock.tryLock(TRY_LOCK_TIMEOUT, TimeUnit.MILLISECONDS)) {
                long _currentTime = System.currentTimeMillis();
                if (_currentTime - time > gap) {
                    counter.set(0);
                    time = _currentTime;
                }
                _update = counter.incrementAndGet();
                return _update <= limit;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return false;
    }
}
